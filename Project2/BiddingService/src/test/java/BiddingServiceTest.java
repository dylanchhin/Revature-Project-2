import dataaccessobjects.AuctionBidDAO;
import dataaccessobjects.AuctionDAO;
import dataaccessobjects.AuctionWinnerDAO;
import models.Auction;
import models.AuctionBid;
import models.AuctionWinner;
import models.CurrentBid;
import net.bytebuddy.asm.Advice;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import services.BiddingService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class BiddingServiceTest {
    @Mock
    private AuctionBidDAO auctionBidDAO = null;
    @Mock
    private AuctionWinnerDAO auctionWinnerDAO = null;
    @Mock
    private AuctionDAO auctionDAO = null;
    private AuctionBid auctionBid= null;
    private BiddingService biddingService = null;
    private List<AuctionWinner> auctionWinners = null;
    private List<AuctionBid> auctionBids = null;
    private AuctionWinner auctionWinner = null;
    private AuctionBid highestBid = null;
    private LocalDateTime rightNow = null;
    private Auction auction = null;
    @Before
    public void init() {
        biddingService = new BiddingService(auctionBidDAO, auctionWinnerDAO, auctionDAO);
        rightNow = LocalDateTime.now();
        LocalDateTime aDateTime = LocalDateTime.of(2021, Month.JULY, 29, 19, 30, 40);
        auctionWinners = new ArrayList<>();
        auctionBids = new ArrayList<>();
        auction = new Auction(1, 2, 3, aDateTime, BigDecimal.valueOf(10), BigDecimal.valueOf(10));
        auctionBid = new AuctionBid(1,12, 11, 4000, rightNow);
        highestBid = new AuctionBid(1,10, 11, 2000, rightNow);
        auctionWinner = new AuctionWinner(1, 1, 12, 4000);
        auctionWinners.add(auctionWinner);
        auctionBids.add(auctionBid);
    }


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @Test
    public void bidSuccessBidAlreadyExist()
    {
        Mockito.when(auctionBidDAO.doesBidExist(any(Integer.class),any(Integer.class))).thenReturn(true);
        Mockito.when(auctionBidDAO.update(any(AuctionBid.class))).thenReturn(true);
        Mockito.when(auctionDAO.retrieveByID(any(Integer.class))).thenReturn(auction);
        Mockito.when(auctionBidDAO.getHighestBid(any(Integer.class))).thenReturn(highestBid);
        Assert.assertTrue("Not Passed", biddingService.bid(auctionBid));
    }

    @Test
    public void bidSuccessBidDoesNotExist()
    {
        Mockito.when(auctionBidDAO.doesBidExist(any(Integer.class),any(Integer.class))).thenReturn(false);
        Mockito.when(auctionBidDAO.save(any(AuctionBid.class))).thenReturn(true);
        Mockito.when(auctionDAO.retrieveByID(any(Integer.class))).thenReturn(auction);
        Mockito.when(auctionBidDAO.getHighestBid(any(Integer.class))).thenReturn(highestBid);
        Assert.assertTrue("Not Passed",  biddingService.bid(auctionBid));
    }

    @Test
    public void bidFailUpdateFail()
    {
        Mockito.when(auctionBidDAO.doesBidExist(any(Integer.class),any(Integer.class))).thenReturn(true);
        Mockito.when(auctionBidDAO.update(any(AuctionBid.class))).thenReturn(false);
        Mockito.when(auctionDAO.retrieveByID(any(Integer.class))).thenReturn(auction);
        Mockito.when(auctionBidDAO.getHighestBid(any(Integer.class))).thenReturn(highestBid);
        Assert.assertFalse("Passed", biddingService.bid(auctionBid));
    }

    @Test
    public void bidFailSaveFail()
    {
        Mockito.when(auctionBidDAO.doesBidExist(any(Integer.class),any(Integer.class))).thenReturn(false);
        Mockito.when(auctionBidDAO.save(any(AuctionBid.class))).thenReturn(false);
        Mockito.when(auctionDAO.retrieveByID(any(Integer.class))).thenReturn(auction);
        Mockito.when(auctionBidDAO.getHighestBid(any(Integer.class))).thenReturn(highestBid);
        Assert.assertFalse("Passed", biddingService.bid(auctionBid));
    }

    @Test
    public void getBuyHistory()
    {
        boolean wasReturned;
        Mockito.when(auctionWinnerDAO.retrieveAllByBidderID(any(Integer.class))).thenReturn(auctionWinners);
        auctionWinners = biddingService.getBuyHistory(auctionBid.getBidderID());
        if(auctionWinners.size() > 0)
        {
            wasReturned = true;
        }
        else
        {
            wasReturned = false;
        }
        Assert.assertTrue("False", wasReturned);
    }

    @Test
    public void getBiddingList()
    {
        boolean wasReturned;
        Mockito.when(auctionBidDAO.retrieveAllByBidderID(any(Integer.class))).thenReturn(auctionBids);
        auctionBids = biddingService.getBiddingList(auctionBid.getBidderID());
        if(auctionWinners.size() > 0)
        {
            wasReturned = true;
        }
        else
        {
            wasReturned = false;
        }
        Assert.assertTrue("False", wasReturned);
    }

    @Test
    public void CurrentBid()
    {
        AuctionBid auctionBid1 = new AuctionBid(3, 11, 11, 400, rightNow);
        AuctionBid auctionBid2 = new AuctionBid(3, 11, 11, 300, rightNow);
        Mockito.when(auctionBidDAO.getHighestBid(any(Integer.class))).thenReturn(auctionBid1);
        Mockito.when(auctionBidDAO.retrieveByAuctionIDAndBidderID(any(Integer.class), any(Integer.class))).thenReturn(auctionBid2);
        CurrentBid currentBid = biddingService.currentBid(11, 3);
        Assert.assertEquals(true, currentBid.getItemCurrentBid() == 400 && currentBid.getUserCurrentBid() == 300);
    }

    @Test
    public void CalculateAuctionWinnerSuccess()
    {
        AuctionBid auctionBid1 = new AuctionBid(3, 11, 11, 300, rightNow);
        Mockito.when(auctionBidDAO.getHighestBid(any(Integer.class))).thenReturn(auctionBid1);
        Mockito.when(auctionDAO.retrieveByID(any(Integer.class))).thenReturn(auction);
        Mockito.when(auctionDAO.update(any(Auction.class))).thenReturn(true);
        Mockito.when(auctionWinnerDAO.save((any(AuctionWinner.class)))).thenReturn(true);
        Assert.assertTrue("Something failed", biddingService.calculateAuctionWinner(3));
    }

    @Test
    public void CalculateAuctionWinnerFailUpdate()
    {
        AuctionBid auctionBid1 = new AuctionBid(3, 11, 11, 300, rightNow);
        Mockito.when(auctionBidDAO.getHighestBid(any(Integer.class))).thenReturn(auctionBid1);
        Mockito.when(auctionDAO.retrieveByID(any(Integer.class))).thenReturn(auction);
        Mockito.when(auctionDAO.update(any(Auction.class))).thenReturn(false);
        Assert.assertFalse("It passed", biddingService.calculateAuctionWinner(3));
    }

    @Test
    public void CalculateAuctionWinnerFailIDZero()
    {
        AuctionBid auctionBid1 = new AuctionBid(3, 0, 11, 300, rightNow);
        Mockito.when(auctionBidDAO.getHighestBid(any(Integer.class))).thenReturn(auctionBid1);
        Assert.assertFalse("It passed", biddingService.calculateAuctionWinner(3));
    }
}
