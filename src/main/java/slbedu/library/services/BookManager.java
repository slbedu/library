package slbedu.library.services;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import slbedu.library.dao.BookDAO;
import slbedu.library.model.Book;

@Stateless
@Path("book")
public class BookManager implements Serializable {

    private static final long serialVersionUID = 1274073464259696963L;

    @Inject
    private BookDAO bookDAO;

    @GET
    @Produces("application/json")
    public Collection<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }

    @GET
    @Produces("application/json")
    public Book getBook(@QueryParam("bookId") String bookId) {
        return bookDAO.findById(Long.parseLong(bookId));
    }

}
