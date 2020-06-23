package auction.servlet;

import auction.dataaccess.*;
import auction.models.User;
import auction.services.UserService;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener {
    // Data access layer
    private ConnectionUtils connectionUtils = null;
    private UserDAO userDao = null;

    // Service layer
    private UserService userService = null;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();
        System.out.println("Servlet context initialized");

        connectionUtils = new PostGresConnectionUtil();
        userDao = new UserDAO(connectionUtils);

        userService = new UserService(userDao);
        context.setAttribute("userService", userService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Servlet context destroyed");
    }
}