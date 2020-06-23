import auction.dataaccess.UserDAO;
import auction.models.User;
import auction.services.UserService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UserDAOTest {
    @Mock
    private UserDAO userDAO = null;
    private User testUser = null;
    private User testUser2 = null;
    private User testUser3 = null;
    private User testUser4 = null;
    private String userName;
    private String password = "password";
    private String creditCardInfo;
    private int role;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void init() {
        testUser = new User();
        userDAO = new UserDAO();
        testUser2 = new User();
        testUser3 = new User();
        testUser4 = new User();

        userName = "bobRose";
        creditCardInfo = "1234567890123456";
        role = 1;
        password = "password";
        testUser.setUserName(userName);
        testUser.setPassword(password);
        testUser.setCreditCardNumber(creditCardInfo);
        testUser.setRole(role);


        userName = "Tom Jones";
        password = "password1";
        creditCardInfo = "23123456";
        role = 2;
        testUser2.setUserId(4);
        testUser2.setUserName(userName);
        testUser2.setPassword(password);
        testUser2.setCreditCardNumber(creditCardInfo);
        testUser2.setRole(role);

        userName = "Bill Murray";
        password = "password2";
        creditCardInfo = "2312345938173646";
        role = 3;
        testUser3.setUserName(userName);
        testUser3.setPassword(password);
        testUser3.setCreditCardNumber(creditCardInfo);
        testUser3.setRole(role);

        userName = "Greg Cox";
        password = "password3";
        creditCardInfo = "23123456";
        role = 4;
        testUser4.setUserName(userName);
        testUser4.setPassword(password);
        testUser4.setCreditCardNumber(creditCardInfo);
        testUser4.setRole(role);
    }

    @Test
    public void testSave() {
        assertTrue(userDAO.save(testUser));
        assertFalse(userDAO.save(testUser2));
        assertFalse(userDAO.save(testUser3));
        assertFalse(userDAO.save(testUser4));
    }

    @Test
    public void testDelete() {
        assertTrue(userDAO.delete(testUser));
    }

    @Test
    public void testUpdate(){
        assertTrue(userDAO.update(testUser4));
    }

    @Test
    public void testRetrieveAll(){
        List<User> users = new ArrayList<User>();
        users = userDAO.retrieveAll();
        assertEquals(users.size(), users.size());
    }

    @Test
    public void testRetrieveById(){
        User newUser= userDAO.retrieveByID(4);
        assertTrue(newUser.equals(testUser2));
    }

    @Test
    public void testLogin(){
        UserService userService = new UserService(userDAO);
        assertNotEquals(userService.loginUser("dylanchhin", "123"),0);
    }

    @Test
    public void testLogout(){
        UserService auth = new UserService(userDAO);
        assertFalse(auth.logout(1));
    }

    //@After
}

