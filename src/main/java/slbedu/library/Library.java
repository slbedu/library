package slbedu.library;

import java.util.Date;
import java.util.Set;

import javax.persistence.EntityManager;

import slbedu.library.dao.BookDAO;
import slbedu.library.dao.UserDAO;
import slbedu.library.model.Book;
import slbedu.library.model.User;
import slbedu.library.utils.DatabaseUtils;

public class Library {
    private static UserDAO userDAO;
    private static BookDAO bookDAO;

    public static void main(String[] args) {
        DatabaseUtils dbUtils = new DatabaseUtils();
        dbUtils.addTestDataToDB();
        EntityManager em = dbUtils.initEntityManager();
        userDAO = new UserDAO(em);
        bookDAO = new BookDAO(em);
        registerUser("FourthUser", "Test1234", "fourth.user@somemail.com",
                new Date());
        borrowBook("FourthUser", "Ernest Hemingway", "The Old Man and the Sea");
        Set<Book> booksOfUser = findCurrenBooksOfUser("FourthUser");
        System.out.println(booksOfUser);
        dbUtils.closeEntityManager(em);
    }

    private static Set<Book> findCurrenBooksOfUser(String userName) {
        User user = userDAO.findUserByName(userName);
        if (user == null) {
            System.out.println("User does not exist in database");
            return null;
        }
        return user.getCurrentBooks();

    }

    private static void borrowBook(String userName, String author, String title) {
        User userToBorrow = userDAO.findUserByName(userName);
        if (userToBorrow == null) {
            System.out
                    .println("User does not exist in database and cannot borrow books");
            return;
        }

        Book bookToBorrow = bookDAO.findByAuthorAndTitle(author, title);
        if (bookToBorrow == null) {
            System.out.println("Book does not exists and cannot be borrowed");
            return;
        }
        bookDAO.borrowBook(bookToBorrow, userToBorrow);
    }

    private static void registerUser(String userName, String password,
                                     String email, Date dateOfBirth) {
        boolean userExistng = userDAO.findUserByName(userName) != null;
        if (userExistng) {
            System.out.println("User already exists");
            return;
        }
        userDAO.addUser(new User(userName, password, email, dateOfBirth));
    }
}
