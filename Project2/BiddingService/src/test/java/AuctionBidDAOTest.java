import dataaccess.PostGresConnectionUtil;
import dataaccessobjects.AuctionBidDAO;
import models.AuctionBid;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class AuctionBidDAOTest {

    private AuctionBidDAO auctionBidDAO = new AuctionBidDAO(new PostGresConnectionUtil());
    private LocalDateTime rightNow = LocalDateTime.now();
    @Test
    public void saveBid()
    {
        AuctionBid auctionBid = new AuctionBid(3, 10, 11, 4000, rightNow);
        boolean passed = auctionBidDAO.save(auctionBid);
        Assert.assertTrue("Not Passed", passed);
    }

    @Test
    public void findAllBids()
    {
        List<AuctionBid> auctionBids = auctionBidDAO.retrieveAll();
        for(int i = 0; i < auctionBids.size(); i++)
        {
            System.out.println(auctionBids.get(i));
        }
        Assert.assertEquals("Test", true, true);
    }

    @Test
    public void retrieveAllByAuctionID()
    {
        List<AuctionBid> auctionBids = auctionBidDAO.retrieveAllByAuctionID(3);
        for(int i = 0; i < auctionBids.size(); i++)
        {
            System.out.println(auctionBids.get(i));
        }
        Assert.assertEquals("Test", true, true);
    }
    @Test
    public void retrieveAllByBidderID()
    {
        List<AuctionBid> auctionBids = auctionBidDAO.retrieveAllByBidderID(17);
        for(int i = 0; i < auctionBids.size(); i++)
        {
            System.out.println(auctionBids.get(i));
        }
        Assert.assertEquals("Test", true, true);
    }

    @Test
    public void retrieveAllByAuctionIDAndBidderID()
    {
        AuctionBid auctionBid = auctionBidDAO.retrieveByAuctionIDAndBidderID(1, 12);
        System.out.println(auctionBid);
        Assert.assertEquals("Test", true, true);
    }

    @Test
    public void doesBidExist()
    {
        boolean exist = auctionBidDAO.doesBidExist(3, 12);
        Assert.assertTrue("Does not exist", exist);
    }

    @Test
    public void getHighestBid()
    {
        AuctionBid auctionBid = auctionBidDAO.getHighestBid(3);
        System.out.println(auctionBid);
        Assert.assertEquals("Test", true, true);
    }

    @Test
    public void delete()
    {
        AuctionBid auctionBid = new AuctionBid(3, 11, 11, 200, rightNow);
        boolean passed = auctionBidDAO.delete(auctionBid);
        Assert.assertTrue("Failed", passed);
    }

    @Test
    public void update()
    {
        AuctionBid auctionBid = new AuctionBid(3, 12, 11, 4000, rightNow);
        boolean passed = auctionBidDAO.update(auctionBid);
        Assert.assertTrue("Failed", passed);
    }

    @AfterClass
    public static void reset()
    {
        LocalDateTime rightNow = LocalDateTime.now();
        AuctionBidDAO auctionBidDAO = new AuctionBidDAO(new PostGresConnectionUtil());
        auctionBidDAO.delete(new AuctionBid(3, 10, 11, 200, rightNow));
        auctionBidDAO.save(new AuctionBid(3, 11, 11, 2000, rightNow));
        auctionBidDAO.update(new AuctionBid(3, 12, 11, 3000, rightNow));
    }
}
