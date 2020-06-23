package auction.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AuctionListJSONWrapper {
    public final List<AuctionJSONWrapper> auctions;

    public AuctionListJSONWrapper() {
        auctions = new ArrayList<>();
    }
    public AuctionListJSONWrapper(List<AuctionJSONWrapper> auctions) {
        this.auctions = auctions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuctionListJSONWrapper that = (AuctionListJSONWrapper) o;
        return Objects.equals(auctions, that.auctions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(auctions);
    }
}
