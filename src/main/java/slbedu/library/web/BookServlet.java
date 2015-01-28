package slbedu.library.web;

import com.google.gson.Gson;
import slbedu.library.dao.BookDAO;
import slbedu.library.utils.DatabaseUtils;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/BookServlet")
public class BookServlet extends HttpServlet {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Override
    public void init() throws ServletException {
        DatabaseUtils utils = new DatabaseUtils(emf);
        utils.addTestDataToDB();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String bookId = req.getParameter("bookId");

        BookDAO bookDAO = new BookDAO(emf.createEntityManager());
        Gson gson = new Gson();
        String resource = null;

        if (bookId == null) {
            resource = gson.toJson(bookDAO.getAllBooks());
        } else {
            resource = gson.toJson(bookDAO.findById(Long.parseLong(bookId)));
        }

        resp.setContentType("application/json");
        resp.getWriter().print(resource);
        resp.getWriter().flush();
    }


}
