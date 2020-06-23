package tests;

import auction.json.AuctionJSONConverter;
import auction.json.AuctionJSONWrapper;
import auction.json.AuctionListJSONWrapper;
import auction.models.Auction;
import auction.models.Item;
import auction.services.AuctionJSONService;
import auction.services.AuctionService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuctionJSONTests {
    private AuctionJSONConverter jsonConverter;
    private Auction auction;
    private Item item;
    private AuctionJSONWrapper wrapper;

    @Mock
    AuctionService auctionService;

    @InjectMocks
    AuctionJSONService jsonService;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Before
    public void init() {
        jsonConverter = new AuctionJSONConverter();
        auction = new Auction(123, 456, 789, LocalDateTime.of(2020, 7, 4, 5, 55),
                new BigDecimal("100.00"), new BigDecimal("101.11"));
        item = new Item(456, "Item name", "Item description");
        wrapper = new AuctionJSONWrapper(auction, item);
    }

    @Test
    public void checkAuctionWrapper() {
        Auction actual = wrapper.toAuction();
        Item actualItem = wrapper.toItem();

        Assert.assertEquals("Didn't store correct auction", auction, actual);
        Assert.assertEquals("Didn't store correct item", item, actualItem);
    }

    @Test
    public void checkSerializationSingle() {
        String json = jsonConverter.serialize(wrapper);
        System.out.println(json);

        AuctionJSONWrapper deserialized = jsonConverter.deserializeAuction(json);
        Assert.assertEquals("Didn't serialize and/or deserialize correctly!", wrapper, deserialized);
    }

    @Test
    public void checkSerializationMultiple() {
        AuctionListJSONWrapper listWrapper = new AuctionListJSONWrapper();
        // Just gonna be lazy and use the same auction 3 times...
        for (int i = 0; i < 3; ++i)
            listWrapper.auctions.add(wrapper);
        String json = jsonConverter.serialize(listWrapper);
        System.out.println(json);

        AuctionListJSONWrapper deserialized = jsonConverter.deserializeAuctions(json);
        Assert.assertEquals("Didn't serialize and/or deserialize correctly!", listWrapper, deserialized);
    }

    @Test
    public void testServiceWrapperCreateSingle() {
        Mockito.when(auctionService.getAuctionItem(auction)).thenReturn(item);

        AuctionJSONWrapper actual = jsonService.getAuctionJSONObject(auction);
        Assert.assertEquals("Didn't return correct object!", wrapper, actual);
    }

    @Test
    public void testServiceWrapperCreateMultiple() {
        // Once again being lazy and using the same auction multiple times...
        List<Auction> auctions = new ArrayList<>();
        AuctionListJSONWrapper expected = new AuctionListJSONWrapper();
        for (int i = 0; i < 5; ++i) {
            auctions.add(auction);
            expected.auctions.add(new AuctionJSONWrapper(auction, item));
        }
        Mockito.when(auctionService.getAuctionItem(auction)).thenReturn(item);

        AuctionListJSONWrapper actual = jsonService.getAuctionJSONObjects(auctions);
        Assert.assertEquals("Didn't return correct object!", expected, actual);
    }
}
