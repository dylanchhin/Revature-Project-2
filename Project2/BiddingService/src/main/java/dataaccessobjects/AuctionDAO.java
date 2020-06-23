package dataaccessobjects;

import dataaccess.ConnectionUtils;
import models.Auction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuctionDAO  implements DAO<Auction, Integer>{
    private ConnectionUtils connectionUtils = null;

    public AuctionDAO(ConnectionUtils connectionUtils) {
        if(connectionUtils != null) {
            this.connectionUtils = connectionUtils;
        }
    }

    @Override
    public boolean save(Auction obj) {


        String saveStatement = "INSERT INTO " + connectionUtils.getDefaultSchema() + "." + "auction"
                + " (itemid, sellerid, enddate, startingprice, reserveprice) VALUES (?,?,?,?,?) returning auctionid";
        try (Connection connection = connectionUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(saveStatement)){
            preparedStatement.setInt(1, obj.getItemID());
            preparedStatement.setInt(2, obj.getSellerID());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(obj.getEndDate()));
            preparedStatement.setBigDecimal(4, obj.getStartingPrice());
            preparedStatement.setBigDecimal(5, obj.getReservePrice());

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    obj.setAuctionID(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public List<Auction> retrieveAll() {
        ArrayList<Auction> auctions = new ArrayList<>();

        String sql = "SELECT * FROM " + connectionUtils.getDefaultSchema() + "." + "auction";
        try (Connection connection = connectionUtils.getConnection();
             Statement auctionStatement = connection.createStatement();
             ResultSet resultSet = auctionStatement.executeQuery(sql)){

            while (resultSet.next()) {

                auctions.add(new Auction(
                        resultSet.getInt("auctionid"),
                        resultSet.getInt("itemid"),
                        resultSet.getInt("sellerid"),
                        resultSet.getTimestamp("enddate").toLocalDateTime(),
                        resultSet.getBigDecimal("startingprice"),
                        resultSet.getBigDecimal("reserveprice")
                ));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return auctions;
    }


    @Override
    public Auction retrieveByID(Integer integer) {
        Auction auction=null;
        String selectStatement = "SELECT * FROM " + connectionUtils.getDefaultSchema() + "." + "auction"
                + " WHERE auctionid = ?";
        try (Connection connection = connectionUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectStatement)){
            preparedStatement.setInt(1, integer);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    auction = new Auction();

                    auction.setAuctionID(resultSet.getInt("auctionid"));
                    auction.setItemID(resultSet.getInt("itemid"));
                    auction.setSellerID(resultSet.getInt("sellerid"));
                    auction.setEndDate(resultSet.getTimestamp("enddate").toLocalDateTime());
                    auction.setStartingPrice(resultSet.getBigDecimal("startingprice"));
                    auction.setReservePrice(resultSet.getBigDecimal("reserveprice"));
                } else
                    System.out.println("No user by that id found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return auction;
    }

    @Override
    public boolean delete(Auction obj) {
        String deleteStatement = "DELETE FROM " + connectionUtils.getDefaultSchema() + "." + "auction"
                + " WHERE auctionid = ?";
        try (Connection connection = connectionUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteStatement)){
            preparedStatement.setInt(1, obj.getAuctionID());
            preparedStatement.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Auction obj) {
        String updateStatement = "UPDATE " + connectionUtils.getDefaultSchema() + ".auction SET itemid = ?, sellerid = ?, " +
                " enddate = ?, startingprice = ?, reserveprice = ? WHERE auctionid = ?";
        try (Connection connection = connectionUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateStatement)){
            preparedStatement.setInt(1,obj.getItemID());
            preparedStatement.setInt(2,obj.getSellerID());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(obj.getEndDate()));
            preparedStatement.setBigDecimal(4,obj.getStartingPrice());
            preparedStatement.setBigDecimal(5,obj.getReservePrice());
            preparedStatement.setInt(6,obj.getAuctionID());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
