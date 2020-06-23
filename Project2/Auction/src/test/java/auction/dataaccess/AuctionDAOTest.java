package auction.dataaccess;

import auction.dataaccess.AuctionDAO;
import auction.dataaccess.PostGresConnectionUtil;
import auction.models.Auction;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class AuctionDAOTest extends TestCase {
    private AuctionDAO auctionDAO = new AuctionDAO(new PostGresConnectionUtil());
    protected Auction testAuction=null;

    @Test
    public void testSave() {
        LocalDateTime time = LocalDateTime.of(2020, 7, 4, 12, 30);
        BigDecimal bigDecimal = new BigDecimal("10.00");
        Auction auction = new Auction(-1, 1, 12, time, bigDecimal, bigDecimal);
        boolean result = auctionDAO.save(auction);
        Assert.assertTrue("Save Failed", result);
        Assert.assertTrue("Didn't assign auction id!", auction.getAuctionID() > 0);
        result = auctionDAO.delete(auction);
        Assert.assertTrue("Delete failed", result);
    }

    @Test
    public void testUpdate() {
        LocalDateTime time = LocalDateTime.of(2020, 12, 25, 12, 0);
        BigDecimal bigDecimal = new BigDecimal("10.00");
        Auction auction = new Auction(18,1, 12, time, bigDecimal, bigDecimal);
        Auction old = auctionDAO.retrieveByID(18);
        Assert.assertNotNull("Couldn't retrieve auction", old);

        boolean updated = auctionDAO.update(auction);
        Assert.assertTrue("UPDATE Failed", updated);
        Auction actual = auctionDAO.retrieveByID(18);
        Assert.assertEquals("Didn't update with correct info", auction, actual);
        updated = auctionDAO.update(old);
        Assert.assertTrue("Failed to restore old data", updated);
    }

    @Test
    public void testRetrieveByID() {
        int testID=18;
        testAuction = auctionDAO.retrieveByID(testID);
        Auction expected = new Auction(18, 1, 12,
                LocalDateTime.of(2020, 12, 25, 12, 0),
                new BigDecimal("20.00"), new BigDecimal("30.00"));
        Assert.assertEquals("Didn't return correct auction data", expected, testAuction);
    }

    @Test
    public void testRetrieveAll() {
        List<Auction> auctions= auctionDAO.retrieveAll();
        for (Auction a: auctions)
        {
            System.out.println(a);
        }
        Assert.assertFalse("Retrieval Failed",auctions.isEmpty());
    }
}

