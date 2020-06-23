package auction.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuctionJSONConverter {
    private final ObjectMapper mapper;

    public AuctionJSONConverter() {
        mapper = new ObjectMapper();
    }

    public String serialize(AuctionJSONWrapper auction) {
        String result = null;
        try {
            result = mapper.writeValueAsString(auction);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String serialize(AuctionListJSONWrapper auctions) {
        String result = null;
        try {
            result = mapper.writeValueAsString(auctions);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public AuctionJSONWrapper deserializeAuction(String json) {
        AuctionJSONWrapper ret = null;
        try {
            ret = mapper.readValue(json, AuctionJSONWrapper.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ret;
    }
    public AuctionListJSONWrapper deserializeAuctions(String json) {
        AuctionListJSONWrapper ret = null;
        try {
            ret = mapper.readValue(json, AuctionListJSONWrapper.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ret;
    }
}
