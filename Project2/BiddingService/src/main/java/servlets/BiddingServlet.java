package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import dataaccess.PostGresConnectionUtil;
import dataaccessobjects.UserDAO;
import models.AuctionBid;
import models.User;
import services.BiddingService;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

public class BiddingServlet extends HttpServlet {
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
                        if (newUser.getRole() == 1) {
                            resp.setCharacterEncoding("UTF-8");
                            int tempUserID = newUser.getUserId();
                            List<AuctionBid> auctionBids = biddingService.getBiddingList(tempUserID);
                            ObjectMapper om = new ObjectMapper();
                            String json = om.writeValueAsString(auctionBids);
                            resp.setContentType("application/json");
                            resp.setCharacterEncoding("UTF-8");
                            resp.setStatus(201);
                            out.print(json);
                            out.flush();
                        } else {
                            resp.setStatus(201);
                            out.write("Don't have valid access");
                        }
                    }
                }
            }
        }catch(Exception e)
        {
            resp.setStatus(206);
            PrintWriter out = resp.getWriter();
            out.write("Something Went Wrong");
        }
    }

    //Adding Bid to table
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try
        {
            PrintWriter out = resp.getWriter();
            Cookie[] cookies = req.getCookies();
            if (cookies != null)
            {
                for(int i=0; i<cookies.length; i++)
                {
                    if(cookies[i].getName().equals("userName"))
                    {
                        User newUser = userDa.findByUserName(cookies[i].getValue());
                        if(newUser.getRole() == 1)
                        {
                            int auctionID = Integer.parseInt(req.getParameter("auctionid"));
                            double amount = Double.parseDouble(req.getParameter("amount"));
                            LocalDateTime rightNow = LocalDateTime.now();
                            AuctionBid auctionBid = new AuctionBid(auctionID, newUser.getUserId(), 0, amount, rightNow);
                            boolean bidValid = biddingService.bid(auctionBid);
                            resp.setStatus(201);
                            if(bidValid)
                            {
                                out.write("Bid Passed");
                            }
                            else
                            {
                                out.write("Bid Failed Expired Date/Not High Enough Bid");
                            }
                        }
                        else
                        {
                            resp.setStatus(201);
                            out.write("Don't have valid access");
                        }
                    }
                }
            }
        }catch(Exception e)
        {
            resp.setStatus(206);
            PrintWriter out = resp.getWriter();
            out.write("Something Went Wrong");
        }
    }
}
