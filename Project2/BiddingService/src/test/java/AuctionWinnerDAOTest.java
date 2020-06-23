import dataaccess.PostGresConnectionUtil;
import dataaccessobjects.AuctionWinnerDAO;
import models.AuctionBid;
import models.AuctionWinner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.List;

public class AuctionWinnerDAOTest {

    private AuctionWinnerDAO auctionWinnerDAO = new AuctionWinnerDAO(new PostGresConnectionUtil());

    @Test
    public void saveWinner()
    {
        AuctionWinner auctionWinner = new AuctionWinner(99,3, 12, 8000);
        boolean passed = auctionWinnerDAO.save(auctionWinner);
        Assert.assertTrue("Not Passed", passed);
    }

    @Test
    public void retrieveAllBByBidderID()
    {
        boolean notEmpty;
        List<AuctionWinner> auctionwinners = auctionWinnerDAO.retrieveAllByBidderID(12);
        if(auctionwinners.size() > 0)
        {
            notEmpty = true;
        }
        else
        {
            notEmpty = false;
        }
        Assert.assertTrue("Empty", notEmpty);
    }

    @Test
    public void retrieveByWinnerID()
    {
        boolean notEmpty;
        AuctionWinner auctionWinner = auctionWinnerDAO.retrieveByID(101);
        if(auctionWinner.getWinnerID() == 101)
        {
            notEmpty = true;
        }
        else
        {
            notEmpty = false;
        }
        Assert.assertTrue("Empty", notEmpty);
    }

    @Test
    public void delete()
    {
        AuctionWinner auctionWinner = new AuctionWinner(100,3, 12, 8000);
        boolean passed = auctionWinnerDAO.delete(auctionWinner);
        Assert.assertTrue("Not Passed", passed);
    }

    @Test
    public void update()
    {
        AuctionWinner auctionWinner = auctionWinnerDAO.retrieveByID(101);
        double amount = auctionWinner.getWinningAmount();
        amount = amount + 2000;
        auctionWinner.setWinningAmount(amount);
        boolean passed = auctionWinnerDAO.update(auctionWinner);
        AuctionWinner newAuctionWinner = auctionWinnerDAO.retrieveByID(101);
        Assert.assertEquals("Not Equal", amount, newAuctionWinner.getWinningAmount(), .01);
    }

    @AfterClass
    public static void reset()
    {
        AuctionWinnerDAO auctionWinnerDAO = new AuctionWinnerDAO(new PostGresConnectionUtil());
        auctionWinnerDAO.delete(new AuctionWinner(99,3, 12, 8000));
        auctionWinnerDAO.save(new AuctionWinner(100,3, 12, 8000));
        auctionWinnerDAO.update(new AuctionWinner(101,3, 12, 8000));
    }
}
