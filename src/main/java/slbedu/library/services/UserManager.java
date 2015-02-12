package slbedu.library.services;

import java.io.Serializable;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import slbedu.library.dao.UserDAO;
import slbedu.library.model.User;

@Stateful
@Path("user")
public class UserManager implements Serializable {

    private static final long serialVersionUID = 6780857238650315773L;

    private User currentUser;

    @Inject
    private UserDAO userDAO;

    @POST
    @Consumes
    public User registerUser(User newUser) {
        userDAO.addUser(newUser);
        currentUser = newUser;
        return currentUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
