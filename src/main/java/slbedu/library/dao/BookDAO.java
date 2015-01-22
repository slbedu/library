package slbedu.library.dao;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import slbedu.library.model.Book;

public class BookDAO {
	private EntityManager em;

	public BookDAO(EntityManager em) {
		this.em = em;
	}

	public void addBook(Book book) {
		EntityTransaction tx = beginTransaciton();
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

	public void borrowBook(Book book) {
		EntityTransaction tx = beginTransaciton();
		Book foundBook = findById(book.getId());
		int newAmount = foundBook.getAmount() - 1;
		foundBook.setAmount(newAmount);
		commitTransaction(tx);
	}

	public void returnBook(Book book) {
		EntityTransaction tx = beginTransaciton();
		Book foundBook = findById(book.getId());
		int newAmount = book.getAmount() + 1;
		foundBook.setAmount(newAmount);
		commitTransaction(tx);
	}

	public EntityTransaction beginTransaciton() {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		return tx;
	}

	public void commitTransaction(EntityTransaction tx) {
		try {
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}
}
