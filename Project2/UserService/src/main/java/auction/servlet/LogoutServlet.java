package auction.servlet;

import auction.services.UserService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet that logs a user out of the system
 */
public class LogoutServlet extends  HttpServlet{
    @Override
    protected  void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
        UserService userService = new UserService();
        int sessionId = Integer.parseInt(request.getSession().getAttribute("sessionID").toString());
        boolean loggedOut = userService.logout(sessionId);
        request.getSession().invalidate();
        response.setContentType("text/html");
        try {
            if (loggedOut) {
                response.setStatus(201);
                PrintWriter out = response.getWriter();
                out.write("User was logged out");
            } else {
                response.setStatus(206);
                PrintWriter out = response.getWriter();
                out.write("User wasn't logged out");
            }
        }catch (Exception e) {
            response.setStatus(206);
            PrintWriter out = response.getWriter();
            out.write("An error occurred while trying to logout");
        }
        response.sendRedirect("index.jsp");
    }
}
