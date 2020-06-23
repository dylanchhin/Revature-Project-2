package models;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AuctionBid {
    private int auctionID;
    private int bidderID;
    private int sellerID;
    private double bidAmount;
    private LocalDateTime timestamp;

    public AuctionBid(){
        auctionID = 0;
        bidderID = 0;
        sellerID = 0;
        bidAmount = 0;
        timestamp = null;
    }

    public AuctionBid(int auctionID, int bidderID, int sellerID, double bidAmount, LocalDateTime timestamp)
    {
        this.auctionID = auctionID;
        this.bidderID = bidderID;
        this.sellerID = sellerID;
        this.bidAmount = bidAmount;
        this.timestamp = timestamp;
    }
    public int getSellerID() {
        return sellerID;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setSellerID(int sellerID) {
        this.sellerID = sellerID;
    }

    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getAuctionID() {
        return auctionID;
    }

    public void setAuctionID(int auctionId) {
        this.auctionID = auctionId;
    }

    public int getBidderID() {
        return bidderID;
    }

    public void setBidderID(int bidderID) {
        this.bidderID = bidderID;
    }

    @Override
    public String toString() {
        return auctionID + " " + bidderID + " " + sellerID + " " + bidAmount + " " + timestamp;
    }
}
