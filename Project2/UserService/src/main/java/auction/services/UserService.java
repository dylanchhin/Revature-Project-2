package auction.services;

import auction.dataaccess.UserDAO;
import auction.models.User;

import java.util.List;

public class UserService {
    private final UserDAO userDao;

    /**
     * No arg constructor
     */
    public UserService(){
        this.userDao = new UserDAO();
    }

    /**
     * Arged constructor takes a UserDAO than creates the UserService
     * @param dao The dao that will be used to create the user service
     */
    public UserService(UserDAO dao) {
        this.userDao = dao;
    }

    /**
     *Attempt to login the passed username/password to the database
     * @param username The username input
     * @param password The input password
     * @return - User object of logged in user - null if fails
     */
    public int loginUser(String username, String password) {
        User user = null;
        List<User> userList;
        int check = 0;
        userList = userDao.retrieveAll();
        for (User users: userList) {
            if (users.getUserName().equals(username) && users.getPassword().equals(password)) {
                user = new User();
                user.setUserId(users.getUserId());
                user.setUserName(users.getUserName());
                user.setPassword(users.getPassword());
                user.setCreditCardNumber(users.getCreditCardNumber());
                user.setRole(users.getRole());
                break;
            }
        }
        userDao.createSession();
        Boolean getSession = userDao.getSession(user.getUserId());
        if (!getSession) {
            check = userDao.insertSession(user);
            System.out.println(check);
        }
        else
            check = 1;
        return check;
    }
    /**
     *Attempt to logout a user by removing their session
     * @param sessionId the sessionId of the logged in user
     * @return - whether of not the logout succeeded
     */
    public boolean logout(int sessionId){
        int loggedOut = 0;
        boolean worked = false;
        if (sessionId>0) {
            userDao.removeSession(sessionId);
        }
        if (loggedOut==0){
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * @param name The username being input to create a new user account
     * @param password The password being input to create a new account
     * @return whether the registration passed or not
     */
    public boolean registerUser(String name, String password){
        return userDao.save(new User(name, password));
    }
}
