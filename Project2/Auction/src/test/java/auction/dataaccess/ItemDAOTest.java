package auction.dataaccess;


import auction.models.Auction;
import auction.models.Item;
import junit.framework.TestCase;
import org.junit.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class ItemDAOTest extends TestCase {
    private ItemDAO itemDAO = new ItemDAO(new PostGresConnectionUtil());
    protected Item testItem = null;

   /* @BeforeClass
    public void testItemLoader(){

    }*/

    @Test
    public void testSave() {
        String name="Danarrius";
        String descript="a guy with a face";
        testItem = new Item(-1, name, descript);
        boolean passed = itemDAO.save(testItem);

        Assert.assertTrue("true", passed);
        Assert.assertTrue("didn't assign item ID!", testItem.getItemID() > 0);

        passed = itemDAO.delete(testItem);
        Assert.assertTrue("didn't remove item!", passed);
    }

    @Test
    public void testRetrieveAll() {
        List<Item> items= itemDAO.retrieveAll();
        for(int i = 0; i < items.size(); i++)
        {
            System.out.println(items.get(i));
        }
        Assert.assertFalse("Retrieval Failed",items.isEmpty());
    }

    @Test
    public void testRetrieveByID() {
        int testID= 1;
        Item newTestItem = itemDAO.retrieveByID(testID);
        Item expected = new Item(1, "Pencil", "A Normal Pencil");
        Assert.assertEquals("Didn't return correct item", expected, newTestItem);
    }

    @Test
    public void testUpdate() {
        testItem= new Item(1,"NotDanarrius", "doesnt have a face");
        Item old = itemDAO.retrieveByID(1);
        Assert.assertNotNull("Couldn't retrieve item", old);
        boolean updated = itemDAO.update(testItem);
        Assert.assertTrue("UPDATE Failed",updated);
        Item actual = itemDAO.retrieveByID(1);
        Assert.assertEquals("Didn't update with correct information", testItem, actual);

        updated= itemDAO.update(old);
        Assert.assertTrue("Couldn't revert update", updated);
    }
}