package auction.services;

import auction.json.AuctionJSONWrapper;
import auction.json.AuctionListJSONWrapper;
import auction.models.Auction;
import auction.models.Item;

import java.util.ArrayList;
import java.util.List;

public class AuctionJSONService {
    private final AuctionService service;

    public AuctionJSONService(AuctionService service) {
        this.service = service;
    }

    public AuctionJSONWrapper getAuctionJSONObject(Auction auction) {
        if (auction == null)
            return null;
        Item item = service.getAuctionItem(auction);
        if (item == null)
            return null;
        return new AuctionJSONWrapper(auction, item);
    }

    public AuctionListJSONWrapper getAuctionJSONObjects(List<Auction> auctions) {
        List<AuctionJSONWrapper> ret = new ArrayList<>();
        for (Auction auction: auctions) {
            Item item = service.getAuctionItem(auction);
            ret.add(new AuctionJSONWrapper(auction, item));
        }

        return new AuctionListJSONWrapper(ret);
    }
}
