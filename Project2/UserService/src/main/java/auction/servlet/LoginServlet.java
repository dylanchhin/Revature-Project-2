package auction.servlet;

import auction.services.UserService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet that logs a user into the system
 */
public class LoginServlet extends HttpServlet {
    private UserService userService;
    private final int SESSION_TIMEOUT = 400;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        ServletContext context = config.getServletContext();
        userService = (UserService) context.getAttribute("userService");
    }

    @Override
    public void destroy() {
        /*
         * destroy -- gets called when the server needs it to be.
         *           most likely at server shutdown.
         * */
        System.out.println("Destroy MyServlet");
        super.destroy();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        int sessionId = userService.loginUser(userName, password);
        if (sessionId>=1){
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(SESSION_TIMEOUT);
            session.setAttribute("userName", userName);
            Cookie nameCookie = new Cookie("userName",userName);
            nameCookie.setPath("/");
            response.addCookie(nameCookie);
            response.setStatus(201);
            PrintWriter out = response.getWriter();
            out.println("User " + nameCookie.getValue() + System.getProperty("line.separator"));
            doGet(request, response);
        }
        else{
            PrintWriter out = response.getWriter();
            out.println("Unable to log into the system" + System.getProperty("line.separator"));
            response.setStatus(206);
        }
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response){
        try{
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            //Reading cookies
            Cookie c[]=request.getCookies();
            //Displaying User name value from cookie
            for(int i = 0; i<c.length;i++)
                out.println("User name: "+c[i].getValue() + " Length of C: " + c.length + System.getProperty("line.separator"));
            out.close();
        }catch(Exception exp){
            System.out.println(exp);
        }
    }


}
