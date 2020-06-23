package auction.servlet;

import auction.models.User;
import auction.services.UserService;

import java.io.*;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Registration extends HttpServlet {
    private UserService userService = new UserService();
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    /*
    * service -- runs for each and every request made, after the init method
                 has run at least once.
     */
        System.out.println("Servicing MyServlet");
        super.service(req, resp);
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
    public void init() throws ServletException {
        /*
         * init -- beginning of the servlet lifecycle
         *         it runs once, if the servlet has never been initialize
         *         when the first request to a matching url pattern is made.
         *         You can preload servlet with <load-on-startup> in the web.xml.
         * */
        System.out.println("Init MyServlet");
        super.init();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            response.setContentType("text/html");

            String userName=request.getParameter("userName");
            String password=request.getParameter("userPass");
            User newUser = new User(userName, password);
            boolean registered = userService.registerUser(newUser.getUserName(), newUser.getPassword());
            if (registered){
                response.setStatus(201);
                PrintWriter out = response.getWriter();
                out.write("User was registered");
            }
            else
            {
                response.setStatus(206);
                PrintWriter out = response.getWriter();
                out.write("User wasn't registered");
            }
        }catch(Exception e) {
            response.setStatus(206);
            PrintWriter out = response.getWriter();
            out.write("An error occurred while trying to register");
        }
    }
}
