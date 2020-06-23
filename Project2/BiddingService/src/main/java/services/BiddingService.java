package services;
import dataaccess.PostGresConnectionUtil;
import dataaccessobjects.AuctionBidDAO;
import dataaccessobjects.AuctionDAO;
import dataaccessobjects.AuctionWinnerDAO;
import models.Auction;
import models.AuctionBid;
import models.AuctionWinner;
import models.CurrentBid;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class BiddingService {

    private final AuctionBidDAO auctionBidDAO;

    private final AuctionWinnerDAO auctionWinnerDAO;

    private final AuctionDAO auctionDAO;

    public BiddingService()
    {
        this.auctionBidDAO = new AuctionBidDAO(new PostGresConnectionUtil());
        this.auctionWinnerDAO = new AuctionWinnerDAO(new PostGresConnectionUtil());
        this.auctionDAO = new AuctionDAO(new PostGresConnectionUtil());
    }

    public BiddingService(AuctionBidDAO auctionBidDAO, AuctionWinnerDAO auctionWinnerDAO, AuctionDAO auctionDAO)
    {
        this.auctionBidDAO = auctionBidDAO;
        this.auctionWinnerDAO = auctionWinnerDAO;
        this.auctionDAO = auctionDAO;
    }

    /**
     * Function that places a bid for an item in the auction bid table
     * @param auctionBid passed auction bid
     * @return if the bid was successfully placed
     */
    public boolean bid(AuctionBid auctionBid){
        boolean wasValid;
        LocalDateTime rightNow = LocalDateTime.now();

        if(auctionBid.getBidderID() == 0)
        {
            wasValid = false;
        }
        else
        {
            Auction currentAuction = auctionDAO.retrieveByID(auctionBid.getAuctionID());
            auctionBid.setSellerID(currentAuction.getSellerID());
            if(rightNow.isAfter(currentAuction.getEndDate()))
            {
                wasValid = false;
            }
            else
            {
                AuctionBid highestBid = auctionBidDAO.getHighestBid(auctionBid.getAuctionID());
                if(highestBid.getBidAmount() < auctionBid.getBidAmount())
                {
                    auctionBid.setTimestamp(rightNow);
                    if(auctionBidDAO.doesBidExist(auctionBid.getAuctionID(), auctionBid.getBidderID()))
                    {
                        wasValid = auctionBidDAO.update(auctionBid);
                    }
                    else
                    {
                        wasValid = auctionBidDAO.save(auctionBid);
                    }
                }
                else
                {
                    wasValid = false;
                }
            }
        }
        return wasValid;
    }

    /**
     * Retrieves the bidder's history of wins
     * @param bidderid passed bidder id
     * @return list of wins from bidderid
     */
    public List<AuctionWinner> getBuyHistory(int bidderid)
    {
        return auctionWinnerDAO.retrieveAllByBidderID(bidderid);
    }

    /**
     * Retrieves user list of items bid on
     * @param bidderID passed bidderid
     * @return list of items user has bid on
     */
    public List<AuctionBid> getBiddingList(int bidderID)
    {
        return auctionBidDAO.retrieveAllByBidderID(bidderID);
    }

    /**
     * Checks to see current bid on an item
     * @param bidderID passed bidderid
     * @param auctionID passed userid
     * @return if the user is outbidded or not
     */
    public CurrentBid currentBid(int bidderID, int auctionID)
    {
        AuctionBid highestBid = auctionBidDAO.getHighestBid(auctionID);
        AuctionBid userBid = auctionBidDAO.retrieveByAuctionIDAndBidderID(auctionID, bidderID);
        CurrentBid currentbid = new CurrentBid(userBid.getBidAmount(), highestBid.getBidAmount());
        return currentbid;
    }
    /**
     * Caculates auction winner an inserts them into auction winner table
     * @param auctionID passed auction id
     * @return if the insert was successful or not
     */
    public boolean calculateAuctionWinner(int auctionID)
    {
        AuctionBid auctionBid = auctionBidDAO.getHighestBid(auctionID);
        if(auctionBid.getBidderID() == 0)
        {
            return false;
        }
        Auction auction = auctionDAO.retrieveByID(auctionID);
        LocalDateTime rightNow = LocalDateTime.now();
        auction.setEndDate(rightNow);
        boolean wasUpdated = auctionDAO.update(auction);
        if(wasUpdated)
        {
            AuctionWinner auctionWinner = new AuctionWinner(0, auctionBid.getAuctionID(), auctionBid.getBidderID(), auctionBid.getBidAmount());
            return (auctionWinnerDAO.save(auctionWinner));
        }
        else
        {
            return false;
        }
    }
}
