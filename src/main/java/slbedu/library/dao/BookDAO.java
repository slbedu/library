package slbedu.library.dao;

import java.util.Collection;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import slbedu.library.model.Book;
import slbedu.library.model.User;

@Singleton
public class BookDAO {

    @PersistenceContext
    private EntityManager em;

    public void addBook(Book book) {
        Book foundBook = findByAuthorAndTitle(book.getAuthor(), book.getTitle());
        if (foundBook == null) {
            em.persist(book);
        } else {
            int currentAmount = foundBook.getAmount() + 1;
            foundBook.setAmount(currentAmount);
        }
    }

    public Collection<Book> getAllBooks() {
        return em.createNamedQuery("getAllBooks", Book.class).getResultList();
    }

    public Book findById(long key) {
        return em.find(Book.class, key);
    }

    public Book findByAuthorAndTitle(String author, String title) {
        TypedQuery<Book> query = em
                .createNamedQuery("findByAuthorAndTitle", Book.class)
                .setParameter("author", author).setParameter("title", title);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void borrowBook(Book bookToBorrow, User userWhoTakesTheBook) {
        Book foundBook = findById(bookToBorrow.getId());
        int newAmount = foundBook.getAmount() - 1;
        foundBook.setAmount(newAmount);
        userWhoTakesTheBook.getCurrentBooks().add(foundBook);
    }

    public void returnBook(Book book, User user) {
        Book foundBook = findById(book.getId());
        int newAmount = book.getAmount() + 1;
        foundBook.setAmount(newAmount);
        User userWhoReturnsTheBook = em.find(User.class, user.getId());
        userWhoReturnsTheBook.getCurrentBooks().remove(foundBook);
    }

}
