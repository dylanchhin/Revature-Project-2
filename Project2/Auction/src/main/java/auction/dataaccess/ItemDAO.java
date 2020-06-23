package auction.dataaccess;

import auction.models.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO implements DAO<Item, Integer>{

    private ConnectionUtils connectionUtils = null;

    public ItemDAO(ConnectionUtils connectionUtils) {
        if(connectionUtils != null) {
            this.connectionUtils = connectionUtils;
        }
    }

    @Override
    public boolean save(Item item) {
        String saveStatement = "INSERT INTO " + connectionUtils.getDefaultSchema() + "." + "item"
                + " (itemname, itemdescription) VALUES (?,?) returning itemid";
        try (Connection connection = connectionUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(saveStatement)){

            preparedStatement.setString(1, item.getName());
            preparedStatement.setString(2, item.getDescription());

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    item.setItemID(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Item> retrieveAll() {
        String selectStatement = "SELECT * FROM " + connectionUtils.getDefaultSchema() + "." + "item";

        List<Item> itemList = new ArrayList<>();
        try (Connection connection = connectionUtils.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectStatement)){

            while (resultSet.next()) {
                itemList.add(new Item(resultSet.getString("itemName"), resultSet.getString("itemDescription")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemList;
    }

    @Override
    public Item retrieveByID(Integer id) {
        String selectStatement = "SELECT * FROM " + connectionUtils.getDefaultSchema() + "." + "item"
                + " WHERE  itemid = ?";
        Item item = null;
        try (Connection connection = connectionUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectStatement)){
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    item = new Item();

                    item.setItemID(resultSet.getInt("itemid"));
                    item.setName(resultSet.getString("itemname"));
                    item.setDescription(resultSet.getString("itemdescription"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public boolean delete(Item item) {
        String deleteStatement = "DELETE FROM " + connectionUtils.getDefaultSchema() + "." + "item"
                + " WHERE itemid = ?";
        try (Connection connection = connectionUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteStatement)) {

            preparedStatement.setInt(1, item.getItemID());
            preparedStatement.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Item newObj) {
        String updateStatement = "UPDATE " + connectionUtils.getDefaultSchema()
                + ".item SET itemname = ?, itemdescription = ? where itemid = ?";
        try (Connection connection = connectionUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateStatement))
        {
            preparedStatement.setString(1, newObj.getName());
            preparedStatement.setString(2, newObj.getDescription());
            preparedStatement.setInt(3, newObj.getItemID());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
