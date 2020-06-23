package auction.services;

import auction.dataaccess.DAO;
import auction.models.Auction;
import auction.models.Item;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

public class AuctionService {
    private final DAO<Auction, Integer> auctionDao;
    private final DAO<Item, Integer> itemDao;

    public AuctionService(DAO<Auction, Integer> auctionDao, DAO<Item, Integer> itemDao) {
        this.auctionDao = auctionDao;
        this.itemDao = itemDao;
    }

    private boolean checkAuction(Auction auction) {
        // Check that end date is valid (after today)
        if (auction.getEndDate().isBefore(LocalDateTime.now(ZoneOffset.UTC)))
            return false;
        // Round the prices so that they have at most 2 digits after the decimal point
        if (auction.getStartingPrice() != null)
            auction.setStartingPrice(auction.getStartingPrice().setScale(2, RoundingMode.DOWN));
        if (auction.getReservePrice() != null)
            auction.setReservePrice(auction.getReservePrice().setScale(2, RoundingMode.DOWN));

        return true;
    }

    // Wrapper for CRUD operations
    public boolean createAuction(Auction auction) {
        if (auction == null || !checkAuction(auction) || itemDao.retrieveByID(auction.getItemID()) == null)
            return false;
        return auctionDao.save(auction);
    }

    public Auction createAuction(Auction newAuction, Item item) {
        if (!checkAuction(newAuction))
            return null;

        // Check whether trying to create a new auction with an existing item
        if (item.getItemID() > 0) {
            item = itemDao.retrieveByID(item.getItemID());
            if (item == null)
                return null;
        }
        else {
            // Item name cannot be null
            if (item.getName() == null || item.getName().trim().equals(""))
                return null;
            // Insert item into the database (which assigns it an item ID)
            if (!itemDao.save(item))
                return null;
        }

        newAuction.setItemID(item.getItemID());

        if (auctionDao.save(newAuction))
            return newAuction;
        else
            return null;
    }

    public Auction createAuction(Item item, int sellerID, LocalDateTime endDate, BigDecimal startPrice, BigDecimal reservePrice) {
        Auction newAuction = new Auction(-1, sellerID, endDate, startPrice, reservePrice);
        return createAuction(newAuction, item);
    }

    public boolean updateAuction(Auction newAuction) {
        if (newAuction == null)
            return false;
        return auctionDao.update(newAuction);
    }

    public boolean updateAuction(Auction newAuction, Item newItem) {
        // Check that the auction data actually exists
        if (newAuction == null || auctionDao.retrieveByID(newAuction.getAuctionID()) == null)
            return false;

        // Check if item already exists
        Item it = itemDao.retrieveByID(newItem.getItemID());
        if (it != null) {
            if (!itemDao.update(newItem))
                return false;
        }
        // Create the item if it doesn't (this sets newItem.itemID)
        else if (!itemDao.save(newItem))
            return false;

        newAuction.setItemID(newItem.getItemID());
        return auctionDao.update(newAuction);
    }

    public Auction getAuction(int auctionID) {
        return auctionDao.retrieveByID(auctionID);
    }
    public Item getAuctionItem(int auctionID) {
        return getAuctionItem(getAuction(auctionID));
    }
    public Item getAuctionItem(Auction auction) {
        if (auction == null)
            return null;
        return itemDao.retrieveByID(auction.getItemID());
    }

    public List<Auction> getAllAuctions() {
        return auctionDao.retrieveAll();
    }

    public boolean removeAuction(int auctionID) {
        Auction auction = getAuction(auctionID);
        if (auction != null)
            return auctionDao.delete(auction);
        return true;
    }

    // Some other helpers
    public boolean isAuctionEnded(int auctionID) {
        Auction auction = getAuction(auctionID);
        if (auction == null)
            return false;

        return LocalDateTime.now(ZoneOffset.UTC).isAfter(auction.getEndDate());
    }

    public List<Auction> findByItemName(String query) {
        List<Auction> allAuctions = getAllAuctions();
        final String queryUpper = query.toUpperCase();

        // Yay for functional programming!
        return allAuctions.stream()
                .filter(a -> {
                    Item i = itemDao.retrieveByID(a.getItemID());
                    if (i != null)
                        return i.getName().toUpperCase().contains(queryUpper);
                    else
                        return false;
                })
                .collect(Collectors.toList());
    }

    public List<Auction> findBySellerID(int id) {
        List<Auction> allAuctions = getAllAuctions();

        return allAuctions.stream()
                .filter(a -> a.getSellerID() == id)
                .collect(Collectors.toList());
    }
}
