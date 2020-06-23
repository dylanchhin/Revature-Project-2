package models;

public class AuctionWinner {
    private int winnerID;

    private int auctionID;

    private int bidderID;

    private double winningAmount;

    public AuctionWinner()
    {
        this.winnerID = 0;
        this.auctionID = 0;
        this.bidderID = 0;
        this.winningAmount = 0;
    }

    public AuctionWinner(int winnerID, int auctionID, int bidderID, double winningAmount)
    {
        this.winnerID = winnerID;
        this.auctionID = auctionID;
        this.bidderID = bidderID;
        this.winningAmount = winningAmount;
    }

    public int getWinnerID() {
        return winnerID;
    }

    public void setWinnerID(int winnerID) {
        this.winnerID = winnerID;
    }

    public int getAuctionID() {
        return auctionID;
    }

    public void setAuctionID(int auctionID) {
        this.auctionID = auctionID;
    }

    public int getBidderID() {
        return bidderID;
    }

    public void setBidderID(int bidderID) {
        this.bidderID = bidderID;
    }

    public double getWinningAmount() {
        return winningAmount;
    }

    public void setWinningAmount(double winningAmount) {
        this.winningAmount = winningAmount;
    }
}
