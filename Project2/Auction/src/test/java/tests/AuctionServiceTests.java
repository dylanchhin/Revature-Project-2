package tests;

import auction.dataaccess.AuctionDAO;
import auction.dataaccess.ItemDAO;
import auction.models.Auction;
import auction.models.Item;
import auction.services.AuctionService;
import org.junit.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class AuctionServiceTests {
    @Mock
    AuctionDAO auctionDAO;

    @Mock
    ItemDAO itemDAO;

    @InjectMocks
    AuctionService service;

    // I still don't know what this does, but whatever
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void init() {
        service = new AuctionService(auctionDAO, itemDAO);
    }

    @Test
    public void shouldCreateAuctionNewItem() {
        Item item = new Item(-1, "Test Item", "Insert description here");
        int sellerID = 10;
        LocalDateTime endDate = LocalDateTime.of(2020, 6, 15, 12, 30);
        BigDecimal start = new BigDecimal("4.56789");
        BigDecimal reserve = new BigDecimal("123");
        Auction expected = new Auction(-1, item.getItemID(), sellerID, endDate,
                start.setScale(2, RoundingMode.DOWN), reserve.setScale(2, RoundingMode.DOWN));

        Mockito.when(itemDAO.retrieveByID(5)).thenReturn(null);
        Mockito.when(itemDAO.save(Mockito.any())).thenReturn(true);
        Mockito.when(auctionDAO.save(Mockito.any())).thenReturn(true);

        Auction actual = service.createAuction(item, sellerID, endDate, start, reserve);
        Assert.assertEquals("Didn't insert correct auction!", expected, actual);
    }

    @Test
    public void shouldCreateAuctionExistingItem() {
        Item item = new Item(10, "Blah test", "Blah description test");
        Auction expected = new Auction(-1, item.getItemID(), 50, LocalDateTime.of(2021, 1, 1, 12, 0),
                new BigDecimal("10"), new BigDecimal("20"));

        Mockito.when(itemDAO.retrieveByID(10)).thenReturn(item);
        Mockito.verify(itemDAO, Mockito.never()).save(Mockito.any()); // Check that a new item is not created
        Mockito.when(auctionDAO.save(Mockito.any())).thenReturn(true);

        Auction actual = service.createAuction(expected, new Item(10, null, null));
        Assert.assertEquals("Didn't insert correct auction!", expected, actual);
    }

    @Test
    public void shouldNotCreateExpiredAuction() {
        Auction newAuction = new Auction(6, 12, 18, LocalDateTime.now(ZoneOffset.UTC).minusHours(3),
                new BigDecimal("123.45"), new BigDecimal("678.90"));

        Mockito.verify(auctionDAO, Mockito.never()).save(Mockito.any());

        boolean result = service.createAuction(newAuction);
        Assert.assertFalse("Didn't not create auction!", result);
    }

    @Test
    public void shouldUpdateAuctionNewItem() {
        Item newItem = new Item();
        newItem.setItemID(12);
        Auction auction = new Auction();
        auction.setAuctionID(360);
        auction.setItemID(10);
        Auction expected = new Auction();
        expected.setAuctionID(360);
        expected.setItemID(12);

        Mockito.when(auctionDAO.retrieveByID(360)).thenReturn(auction);
        Mockito.when(itemDAO.retrieveByID(12)).thenReturn(null);
        Mockito.when(itemDAO.save(newItem)).thenReturn(true);
        Mockito.when(auctionDAO.update(expected)).thenReturn(true);

        boolean updated = service.updateAuction(auction, newItem);
        Assert.assertTrue("Failed to update auction item", updated);
        Assert.assertEquals("Didn't update item correctly", expected, auction);
    }

    @Test
    public void shouldUpdateAuctionExistingItem() {
        int auctionID = 12345;
        int itemID = 67890;
        Item item = new Item(itemID, "Test", "More testing");
        Auction auction = new Auction(auctionID, itemID, 123, LocalDateTime.now(ZoneOffset.UTC),
                new BigDecimal("333.33"), new BigDecimal("666.66"));
        Auction expected = new Auction(auctionID, itemID, 124, auction.getEndDate(), auction.getStartingPrice(),
                new BigDecimal("6969.69"));
        Item expectedItem = new Item(itemID, "No longer a test", "This code is 100% working");

        Mockito.when(auctionDAO.retrieveByID(auctionID)).thenReturn(auction);
        Mockito.when(itemDAO.retrieveByID(itemID)).thenReturn(item);
        Mockito.when(itemDAO.update(expectedItem)).thenReturn(true);
        Mockito.when(auctionDAO.update(expected)).thenReturn(true);

        boolean updated = service.updateAuction(expected, expectedItem);
        Assert.assertTrue("Failed to update auction", updated);
    }

    @Test
    public void shouldGetAuctionItem() {
        Item item = new Item(42, "Hello", "World");
        int auctionID = 360;
        Auction auction = new Auction(auctionID, item.getItemID(), 5,
                LocalDateTime.of(2020, 6, 12, 3, 0),
                new BigDecimal("10.99"), new BigDecimal("25.00"));

        Mockito.when(auctionDAO.retrieveByID(auctionID)).thenReturn(auction);
        Mockito.when(itemDAO.retrieveByID(item.getItemID())).thenReturn(item);

        Item actual = service.getAuctionItem(auctionID);
        Assert.assertEquals("Didn't return correct auction item!", item, actual);
    }

    @Test
    public void testAuctionEnded() {
        int auctionID = 17;
        Auction auction = new Auction(auctionID, 15, 200,
                LocalDateTime.now(ZoneOffset.UTC).minusSeconds(30), new BigDecimal("100.56"), new BigDecimal("123.45"));

        Mockito.when(auctionDAO.retrieveByID(auctionID)).thenReturn(auction);

        boolean ended = service.isAuctionEnded(auctionID);
        Assert.assertTrue("Auction not ended!", ended);
    }

    @Test
    public void testAuctionNotEnded() {
        int auctionID = 23;
        Auction auction = new Auction(auctionID, 17, 404,
                LocalDateTime.now(ZoneOffset.UTC).plusYears(400), new BigDecimal("455.64"), new BigDecimal("456.87"));

        Mockito.when(auctionDAO.retrieveByID(auctionID)).thenReturn(auction);

        boolean ended = service.isAuctionEnded(auctionID);
        Assert.assertFalse("Auction not not ended!", ended);
    }

    @Test
    public void testItemSearch() {
        final List<Item> items = new ArrayList<>();
        items.add(new Item(10, "Van Gogh painting", "Very rare painting, excellent condition!"));
        items.add(new Item(11, "Sweater", "Just a normal sweater that I don't want anymore"));
        items.add(new Item(12, "van gogh sculpture", "idk who made this, but whatever"));

        List<Auction> auctions = new ArrayList<>();
        auctions.add(new Auction(1, 10, 20, null, null, null));
        auctions.add(new Auction(2, 11, 21, null, null, null));
        auctions.add(new Auction(3, 12, 21, null, null, null));
        auctions.add(new Auction(4, 12, 22, null, null, null));

        Mockito.when(auctionDAO.retrieveAll()).thenReturn(auctions);
        Mockito.when(itemDAO.retrieveByID(Mockito.anyInt())).thenAnswer(i -> items.get((Integer)i.getArgument(0) - 10));

        List<Auction> expected = new ArrayList<>();
        expected.add(auctions.get(0));
        expected.add(auctions.get(2));
        expected.add(auctions.get(3));
        List<Auction> actual = service.findByItemName("van gogh");

        Assert.assertArrayEquals("Didn't return correct search results!", expected.toArray(), actual.toArray());
    }
}
