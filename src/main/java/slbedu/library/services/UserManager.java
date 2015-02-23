package slbedu.library.services;

import slbedu.library.dao.UserDAO;
import slbedu.library.model.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("user")
public class UserManager {

    @Inject
    private UserDAO userDAO;
    
    @Inject
    private UserContext context;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void registerUser(User newUser) {
        userDAO.addUser(newUser);
        context.setCurrentUser(newUser);
    }

}
