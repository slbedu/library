package slbedu.library.utils;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import slbedu.library.dao.BookDAO;
import slbedu.library.dao.UserDAO;
import slbedu.library.model.Book;
import slbedu.library.model.User;

public class DatabaseUtils {

    private static User[] USERS = {
            new User("First User", "123456", "first.user@somemail.com",
                    new Date()),
            new User("Second User", "Test1234", "second.user@somemail.com",
                    new Date()),
            new User("Third User", "98411TA", "third.user@somemail.com",
                    new Date())};

    private static Book[] BOOKS = {
            new Book("The Old Man and the Sea", "Ernest Hemingway",
                    "978-3-16-148410-0", 5),
            new Book("Tom Sawyer", "Mark Twain", "978-4-16-241512-0", 0)};

    private EntityManagerFactory emf;

    public DatabaseUtils(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void addTestDataToDB() {
        EntityManager em = initEntityManager();
        deleteData(em);
        addTestUsers(em);
        addTestBooks(em);
        closeEntityManager(em);
    }

    private void deleteData(EntityManager em) {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Book").executeUpdate();
        em.createQuery("DELETE FROM User").executeUpdate();
        em.getTransaction().commit();
   }

    public EntityManager initEntityManager() {
        return emf.createEntityManager();
    }

    public void closeEntityManager(EntityManager em) {
        if (em.isOpen()) {
            em.close();
        }
    }

    private void addTestUsers(EntityManager em) {
        UserDAO userDAO = new UserDAO(em);
        for (User user : USERS) {
            userDAO.addUser(user);
        }
    }

    private void addTestBooks(EntityManager em) {
        BookDAO bookDAO = new BookDAO(em);
        for (Book book : BOOKS) {
            bookDAO.addBook(book);
        }
    }
}
