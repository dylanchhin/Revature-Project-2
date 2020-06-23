package servlets;

import dataaccess.PostGresConnectionUtil;
import dataaccessobjects.UserDAO;
import models.User;
import services.BiddingService;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AuctionWinnerServlet extends HttpServlet {
    private BiddingService biddingService;
    UserDAO userDa;
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    /*
    * service -- runs for each and every request made, after the init method
                 has run at least once.
     */
        System.out.println("Servicing MyServlet");
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
        biddingService = new BiddingService();
        super.init();
    }

    //Adding Bid to table
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try
        {
            PrintWriter out = resp.getWriter();
            boolean auctionWinner;
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (int i = 0; i < cookies.length; i++) {
                    if(cookies[i].getName().equals("userName")) {
                        User newUser = userDa.findByUserName(cookies[i].getValue());
                        if (newUser.getRole() == 2) {
                            int auctionID = Integer.parseInt(req.getParameter("auctionid"));
                            //Find auction date to see if it is over
                            auctionWinner = biddingService.calculateAuctionWinner(auctionID);
                            if (auctionWinner) {
                                resp.setStatus(201);
                                out.write("Auction is now over");
                            } else {
                                resp.setStatus(206);
                                out.write("Something went wrong");
                            }
                        } else {
                            resp.setStatus(201);
                            out.write("Don't have valid access");
                        }
                    }
                }
            }
        }catch(Exception e)
        {
            try{
                PrintWriter out = resp.getWriter();
                resp.setStatus(206);
                out.write("Something Went Wrong");
            }catch(IOException i)
            {
                i.printStackTrace();
            }
        }
    }
}
