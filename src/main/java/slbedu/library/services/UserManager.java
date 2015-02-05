package slbedu.library.services;

import java.io.Serializable;
import java.util.Date;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import slbedu.library.dao.UserDAO;
import slbedu.library.model.User;

@SessionScoped
public class UserManager implements Serializable {

    private static final long serialVersionUID = 6780857238650315773L;

    private User currentUser;

    @Inject
    private UserDAO userDAO;

    public User registerUser(String username, String password, String email) {
        currentUser = new User(username, password, email, new Date());
        userDAO.addUser(currentUser);
        return currentUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
