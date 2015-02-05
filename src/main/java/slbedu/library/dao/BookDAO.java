package slbedu.library.dao;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import slbedu.library.model.Book;
import slbedu.library.model.User;

@ApplicationScoped
public class BookDAO {
    @Inject
    private EntityManager em;

    public void addBook(Book book) {
        EntityTransaction tx = beginTransaction();
        Book foundBook = findByAuthorAndTitle(book.getAuthor(), book.getTitle());
        if (foundBook == null) {
            em.persist(book);
        } else {
            int currentAmount = foundBook.getAmount() + 1;
            foundBook.setAmount(currentAmount);
        }
        commitTransaction(tx);
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
        EntityTransaction tx = beginTransaction();
        Book foundBook = findById(bookToBorrow.getId());
        int newAmount = foundBook.getAmount() - 1;
        foundBook.setAmount(newAmount);
        userWhoTakesTheBook.getCurrentBooks().add(foundBook);
        commitTransaction(tx);
    }

    public void returnBook(Book book, User user) {
        EntityTransaction tx = beginTransaction();
        Book foundBook = findById(book.getId());
        int newAmount = book.getAmount() + 1;
        foundBook.setAmount(newAmount);
        User userWhoReturnsTheBook = em.find(User.class, user.getId());
        userWhoReturnsTheBook.getCurrentBooks().remove(foundBook);
        commitTransaction(tx);
    }

    private EntityTransaction beginTransaction() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        return tx;
    }

    private void commitTransaction(EntityTransaction tx) {
        try {
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
}
