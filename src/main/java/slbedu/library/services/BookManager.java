package slbedu.library.services;

import slbedu.library.dao.BookDAO;
import slbedu.library.model.Book;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collection;


@Stateless
@Path("book")
public class BookManager {

    @Inject
    private BookDAO bookDAO;

    @Inject
    private UserContext userContext;

    @GET
    @Produces("application/json")
    public Collection<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }

    @GET
    @Path("{bookId}")
    @Produces("application/json")
    public Book getBook(@PathParam("bookId") String bookId) {
        return bookDAO.findById(Long.parseLong(bookId));
    }

    @PUT
    @Path("/borrow")
    public Response borrowBook(@QueryParam("bookId") String bookId) {
        Book bookToBorrow = bookDAO.findById(Long.parseLong(bookId));
        if (bookToBorrow != null) {
            bookDAO.borrowBook(bookToBorrow, userContext.getCurrentUser());
        }
        return Response.noContent().build();
    }

}
