package auction.json;

import auction.models.Auction;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class AuctionJSONWrapper {
    public int id;
    public int seller_id;
    public long end_date;
    public String starting_price;
    public String reserve_price;
    public static class ItemWrapper {
        public int id;
        public String name;
        public String desc;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ItemWrapper it = (ItemWrapper) o;
            return id == it.id &&
                    Objects.equals(name, it.name) &&
                    Objects.equals(desc, it.desc);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, desc);
        }
    }
    public ItemWrapper item;

    public AuctionJSONWrapper() {}
    public AuctionJSONWrapper(Auction auction, auction.models.Item it) {
        id = auction.getAuctionID();
        seller_id = auction.getSellerID();
        end_date = auction.getEndDate().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
        starting_price = auction.getStartingPrice().toString();
        reserve_price = auction.getReservePrice().toString();
        item = new ItemWrapper();
        item.id = it.getItemID();
        item.name = it.getName();
        item.desc = it.getDescription();
    }

    public Auction toAuction() {
        LocalDateTime endDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(end_date), ZoneId.systemDefault());
        return new Auction(id, item.id, seller_id, endDate, new BigDecimal(starting_price), new BigDecimal(reserve_price));
    }

    public auction.models.Item toItem() {
        return new auction.models.Item(item.id, item.name, item.desc);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuctionJSONWrapper wrapper = (AuctionJSONWrapper) o;
        return id == wrapper.id &&
                seller_id == wrapper.seller_id &&
                end_date == wrapper.end_date &&
                Objects.equals(starting_price, wrapper.starting_price) &&
                Objects.equals(reserve_price, wrapper.reserve_price) &&
                Objects.equals(item, wrapper.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, seller_id, end_date, starting_price, reserve_price, item);
    }
}
