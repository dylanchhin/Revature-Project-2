package models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Auction {
    private int auctionID;
    private int itemID;
    private int sellerID;
    private LocalDateTime endDate;
    // Floating point values are bad for currency
    private BigDecimal startingPrice;
    private BigDecimal reservePrice;

    public Auction() {}
    public Auction(int auctionID, int itemID, int seller, LocalDateTime endDate, BigDecimal startingPrice, BigDecimal reservePrice) {
        this.auctionID = auctionID;
        this.itemID = itemID;
        this.sellerID = seller;
        this.endDate = endDate;
        this.startingPrice = startingPrice;
        this.reservePrice = reservePrice;
    }
    public Auction(int itemID, int seller, LocalDateTime endDate, BigDecimal startPrice, BigDecimal reservePrice) {
        this(-1, itemID, seller, endDate, startPrice, reservePrice);
    }
    public int getAuctionID() {
        return auctionID;
    }

    public void setAuctionID(int auctionID) {
        this.auctionID = auctionID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getSellerID() {
        return sellerID;
    }

    public void setSellerID(int sellerID) {
        this.sellerID = sellerID;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(BigDecimal startingPrice) {
        this.startingPrice = startingPrice;
    }

    public BigDecimal getReservePrice() {
        return reservePrice;
    }

    public void setReservePrice(BigDecimal reservePrice) {
        this.reservePrice = reservePrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Auction auction = (Auction) o;
        return auctionID == auction.auctionID &&
                itemID == auction.itemID &&
                sellerID == auction.sellerID &&
                Objects.equals(endDate, auction.endDate) &&
                Objects.equals(startingPrice, auction.startingPrice) &&
                Objects.equals(reservePrice, auction.reservePrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(auctionID, itemID, sellerID, endDate, startingPrice, reservePrice);
    }
}
