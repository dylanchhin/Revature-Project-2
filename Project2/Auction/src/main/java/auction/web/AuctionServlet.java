package auction.web;

import auction.json.AuctionJSONConverter;
import auction.json.AuctionJSONWrapper;
import auction.json.AuctionListJSONWrapper;
import auction.models.Auction;
import auction.models.Item;
import auction.models.User;
import auction.services.AuctionJSONService;
import auction.services.AuctionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/auctions/*")
public class AuctionServlet extends HttpServlet {
    private AuctionService service;
    private AuctionJSONService jsonService;
    private AuctionJSONConverter jsonConverter;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        ServletContext context = config.getServletContext();
        service = (AuctionService)context.getAttribute("auctionService");
        jsonService = (AuctionJSONService)context.getAttribute("jsonService");
        jsonConverter = (AuctionJSONConverter)context.getAttribute("jsonConverter");
    }

    private String getRequestBody(HttpServletRequest req) throws IOException {
        return req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
    }

    private static int getID(String url) {
        String[] urlParts = url.split("/");
        if (urlParts.length != 2)
            return -HttpServletResponse.SC_BAD_REQUEST;

        int id = -1;
        try {
            id = Integer.parseInt(urlParts[1]);
        }
        catch (NumberFormatException ex) {
            return -HttpServletResponse.SC_NOT_FOUND;
        }
        return id;
    }

    private void searchAuctions(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String nameQuery = req.getParameter("query");
        String sellerQueryStr = req.getParameter("seller");
        int parsed = 0;
        if (sellerQueryStr != null) {
            try {
                parsed = Integer.parseInt(sellerQueryStr);
            }
            catch (NumberFormatException ex) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }
        // Weird workaround to stop Java/IntelliJ from complaining about uninitialized variables or captured lambda variables
        // not being "final or effectively final"
        final int sellerID = parsed;

        // Do the search
        List<Auction> searchResults;
        if (nameQuery == null && sellerQueryStr == null)
            searchResults = service.getAllAuctions();
        else if (nameQuery == null)
            searchResults = service.findBySellerID(sellerID);
        else {
            searchResults = service.findByItemName(nameQuery);
            if (sellerQueryStr != null) {
                searchResults = searchResults.stream().filter(a -> a.getSellerID() == sellerID).collect(Collectors.toList());
            }
        }

        // Convert to JSON
        AuctionListJSONWrapper wrappedResults = jsonService.getAuctionJSONObjects(searchResults);
        String json = jsonConverter.serialize(wrappedResults);
        resp.getWriter().write(json);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // I don't know how necessary these next two lines are, or if this is the right place to write them,
        // but I'm gonna put them here anyways
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        String url = req.getPathInfo();

        if (url == null || url.equals("")) { // GET /auctions
            // Get all auctions
            AuctionListJSONWrapper auctions = jsonService.getAuctionJSONObjects(service.getAllAuctions());
            String json = jsonConverter.serialize(auctions);
            writer.write(json);
        }
        else {
            String[] urlParts = url.split("/");
            if (urlParts.length != 2) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            if (urlParts[1].equals("search"))
                searchAuctions(req, resp);
            else { // Try to parse ID
                int id = 0;
                try {
                    id = Integer.parseInt(urlParts[1]);

                    // Get auction by ID
                    Auction auc = service.getAuction(id);
                    if (auc == null)
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    else {
                        AuctionJSONWrapper wrapper = jsonService.getAuctionJSONObject(auc);
                        writer.write(jsonConverter.serialize(wrapper));
                    }
                }
                catch (NumberFormatException ex) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            }


        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO make sure that user is authenticated and has the proper permissions to do this
        String url = req.getPathInfo();
        boolean success = false;
        if (url == null || url.equals("")) {
            String json = getRequestBody(req);
            AuctionJSONWrapper newAuction = jsonConverter.deserializeAuction(json);
            if (newAuction != null) {
                Auction auction = newAuction.toAuction();
                Item auctionItem = newAuction.toItem();
                Auction ret = service.createAuction(auction, auctionItem);
                if (ret != null) {
                    // Return created object in response body
                    resp.setCharacterEncoding("UTF-8");
                    resp.setContentType("application/json");
                    auctionItem = service.getAuctionItem(ret);
                    AuctionJSONWrapper wrapper = new AuctionJSONWrapper(ret, auctionItem); // Are the IDs
                    resp.getWriter().write(jsonConverter.serialize(wrapper));
                    success = true;
                }
            }
        }

        if (success)
            resp.setStatus(HttpServletResponse.SC_CREATED);
        else
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO make sure that user is authenticated and has the proper permissions to do this
        String url = req.getPathInfo();
        if (url == null || url.equals(""))
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        else {
            int id = getID(url);
            if (id >= 0) {
                // Get updated auction info from request body
                String json = getRequestBody(req);
                AuctionJSONWrapper updatedAuction = jsonConverter.deserializeAuction(json);
                if (updatedAuction != null) {
                    Auction auc = updatedAuction.toAuction();
                    Item aucItem = updatedAuction.toItem();
                    if (service.updateAuction(auc, aucItem))
                        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    else
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
                else
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            else
                resp.sendError(-id);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO make sure that user is authenticated and has the proper permissions to do this
        String url = req.getPathInfo();
        if (url == null || url.equals(""))
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        else {
            int id = getID(url);
            if (id >= 0) {
                service.removeAuction(id);
                resp.setStatus(HttpServletResponse.SC_OK);
            }
            else
                resp.sendError(-id);
        }
    }

}
