package models;

public class CurrentBid {

    double userCurrentBid;

    double itemCurrentBid;

    public CurrentBid()
    {
        userCurrentBid = 0;

        itemCurrentBid = 0;
    }

    public CurrentBid(double userCurrentBid, double itemCurrentBid)
    {
        this.userCurrentBid = userCurrentBid;

        this.itemCurrentBid = itemCurrentBid;
    }

    public double getUserCurrentBid() {
        return userCurrentBid;
    }

    public void setUserCurrentBid(double userCurrentBid) {
        this.userCurrentBid = userCurrentBid;
    }

    public double getItemCurrentBid() {
        return itemCurrentBid;
    }

    public void setItemCurrentBid(double itemCurrentBid) {
        this.itemCurrentBid = itemCurrentBid;
    }
}
