package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import dataaccess.PostGresConnectionUtil;
import dataaccessobjects.UserDAO;
import models.CurrentBid;
import models.User;
import services.BiddingService;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CurrentBidServlet extends HttpServlet {

    BiddingService biddingService;
    UserDAO userDa;
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    /*
    * service -- runs for each and every request made, after the init method
                 has run at least once.
     */
        System.out.println("Servicing MyServlet");
        biddingService = new BiddingService();
        userDa = new UserDAO(new PostGresConnectionUtil());
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try
        {
            PrintWriter out = resp.getWriter();
            Cookie[] cookies = req.getCookies();
            if (cookies != null)
            {
                for(int i=0; i<cookies.length; i++)
                {
                    if(cookies[i].getName().equals("userName")) {
                        User newUser = userDa.findByUserName(cookies[i].getValue());
                        int auctionID = Integer.parseInt(req.getParameter("auctionid"));
                        CurrentBid currentBid = biddingService.currentBid(newUser.getUserId(), auctionID);
                        ObjectMapper om = new ObjectMapper();
                        String json = om.writeValueAsString(currentBid);
                        resp.setStatus(201);
                        resp.setContentType("application/json");
                        resp.setCharacterEncoding("UTF-8");
                        out.print(json);
                        out.flush();
                    }
                    else {
                            resp.setStatus(201);
                            out.write("Don't have valid access");
                        }
                }
            }
        }catch(Exception e)
        {
            resp.setStatus(206);
            e.printStackTrace();
            PrintWriter out = resp.getWriter();
            out.write("Something went wrong");
        }
    }
    //Adding Bid to table
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
