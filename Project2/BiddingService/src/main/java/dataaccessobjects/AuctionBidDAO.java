package dataaccessobjects;
import dataaccess.ConnectionUtils;
import models.Auction;
import models.AuctionBid;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuctionBidDAO implements DAO<AuctionBid, Integer> {
    private ConnectionUtils connectionUtils;
    private static final String TABLENAME = "auctionbids";
    public AuctionBidDAO(ConnectionUtils connectionUtils) {
        if(connectionUtils != null) {
            this.connectionUtils = connectionUtils;
        }
    }

    /**
     * Inserts new auctionbid row into table
     * @param obj new Auction bid
     * @return if the insert passed or not
     */
    @Override
    public boolean save(AuctionBid obj) {
        Connection connection = null;
        PreparedStatement auctionBidStatement;
        boolean wasPassed = false;
        try {
            connection = connectionUtils.getConnection();
            String saveStatement = "INSERT INTO " + connectionUtils.getDefaultSchema() + "." + TABLENAME + " (auctionID, bidderID, sellerID, bidamount, timestamp) VALUES (?,?,?,?,?)";
            auctionBidStatement = connection.prepareStatement(saveStatement);
            auctionBidStatement.setInt(1, obj.getAuctionID());
            auctionBidStatement.setInt(2, obj.getBidderID());
            auctionBidStatement.setInt(3, obj.getSellerID());
            auctionBidStatement.setDouble(4, obj.getBidAmount());
            auctionBidStatement.setTimestamp(5, Timestamp.valueOf(obj.getTimestamp()));
            auctionBidStatement.executeUpdate();
            wasPassed = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert connection != null;
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return wasPassed;
    }

    /**
     * Reads all auction bids from the table
     * @return List of all auction bids made
     */
    @Override
    public List<AuctionBid> retrieveAll() {
        Connection connection = null;
        ArrayList<AuctionBid> auctionbids = new ArrayList<>();
        try {
            connection = connectionUtils.getConnection();
            String sql = "SELECT * FROM " + connectionUtils.getDefaultSchema() + "." + TABLENAME;
            PreparedStatement auctionBidStatement = connection.prepareStatement(sql);
            ResultSet resultSet = auctionBidStatement.executeQuery();

            while (resultSet.next()) {
                auctionbids.add(new AuctionBid(resultSet.getInt("auctionid"), resultSet.getInt("bidderid"),
                resultSet.getInt("sellerid"), resultSet.getDouble("bidamount"), resultSet.getTimestamp("timestamp").toLocalDateTime()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert connection != null;
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return auctionbids;
    }

    /**
     * Currenlty not used
     * @param integer Auction ID
     * @return Would return a single auction Bid
     */
    @Override
    public AuctionBid retrieveByID(Integer integer) {

        return null;
    }

    /**
     * Reads all auction bids by a specific auctionid
     * @param auctionid passed auction id
     * @return list of all auction bids
     */
    public List<AuctionBid> retrieveAllByAuctionID(Integer auctionid) {

        Connection connection = null;
        ArrayList<AuctionBid> auctionbids = new ArrayList<>();

        try {
            connection = connectionUtils.getConnection();
            String sql = "SELECT * FROM " + connectionUtils.getDefaultSchema() + "." + TABLENAME + " WHERE auctionID = ?";
            PreparedStatement auctionBidStatement = connection.prepareStatement(sql);
            auctionBidStatement.setInt(1, auctionid);
            ResultSet resultSet = auctionBidStatement.executeQuery();

            while (resultSet.next()) {
                auctionbids.add(new AuctionBid(resultSet.getInt("auctionID"), resultSet.getInt("bidderID"),
                resultSet.getInt("sellerID"), resultSet.getDouble("bidamount"), resultSet.getTimestamp("timestamp").toLocalDateTime()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert connection != null;
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return auctionbids;
    }

    /**
     * Reads all auction bids by a specific auctionid
     * @param bidderid passed bidder id
     * @return List of all bids by bidder id
     */
    public List<AuctionBid> retrieveAllByBidderID(Integer bidderid) {

        Connection connection = null;
        ArrayList<AuctionBid> auctionbids = new ArrayList<>();

        try {
            connection = connectionUtils.getConnection();
            String sql = "SELECT * FROM " + connectionUtils.getDefaultSchema() + "." + TABLENAME + " WHERE bidderID = ?";
            PreparedStatement auctionBidStatement = connection.prepareStatement(sql);
            auctionBidStatement.setInt(1, bidderid);
            ResultSet resultSet = auctionBidStatement.executeQuery();

            while (resultSet.next()) {
                auctionbids.add(new AuctionBid(resultSet.getInt("auctionID"), resultSet.getInt("bidderID"),
                        resultSet.getInt("sellerID"), resultSet.getDouble("bidamount"), resultSet.getTimestamp("timestamp").toLocalDateTime()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert connection != null;
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return auctionbids;
    }

    public AuctionBid retrieveByAuctionIDAndBidderID(Integer auctionID, Integer bidderID) {

        Connection connection = null;
        AuctionBid auctionBid = new AuctionBid();
        try {
            connection = connectionUtils.getConnection();
            String sql = "SELECT * FROM " + connectionUtils.getDefaultSchema() + "." + TABLENAME + " WHERE auctionID = ? AND bidderID = ?";
            PreparedStatement auctionBidStatement = connection.prepareStatement(sql);
            auctionBidStatement.setInt(1, auctionID);
            auctionBidStatement.setInt(2, bidderID);
            ResultSet resultSet = auctionBidStatement.executeQuery();

            if (resultSet.next()) {
                auctionBid = new AuctionBid(resultSet.getInt("auctionid"), resultSet.getInt("bidderid"),
                        resultSet.getInt("sellerid"), resultSet.getDouble("bidamount"), resultSet.getTimestamp("timestamp").toLocalDateTime());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert connection != null;
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return auctionBid;
    }

    /**
     * Reads all auction bids by a specific auctionid
     * @param auctionid passed auction id
     * @return the highest bidder for the selected auction
     */
    public AuctionBid getHighestBid(Integer auctionid) {

        Connection connection = null;
        AuctionBid auctionbid = new AuctionBid();

        try {
            connection = connectionUtils.getConnection();
            String sql = "SELECT auctionid, bidderid, bidamount  FROM " + connectionUtils.getDefaultSchema() + "." + TABLENAME + " WHERE auctionID = ? ORDER BY bidamount DESC";
            PreparedStatement auctionBidStatement = connection.prepareStatement(sql);
            auctionBidStatement.setInt(1, auctionid);
            ResultSet resultSet = auctionBidStatement.executeQuery();

            if (resultSet.next()) {
                auctionbid = (new AuctionBid(resultSet.getInt("auctionid"), resultSet.getInt("bidderid"),
                        0, resultSet.getDouble("bidamount"), null));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert connection != null;
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return auctionbid;
    }

    /**
     * Reads all auction bids by a specific auctionid
     * @param auctionID passed auction id
     * @param bidderID passed bidder id
     * @return if the bid exist in the table or not
     */
    public boolean doesBidExist(Integer auctionID, Integer bidderID) {

        Connection connection = null;
        ArrayList<AuctionBid> auctionbids = new ArrayList<>();
        boolean doesExist = false;

        try {
            connection = connectionUtils.getConnection();
            String sql = "SELECT * FROM " + connectionUtils.getDefaultSchema() + "." + TABLENAME + " WHERE auctionID = ? AND bidderID = ?";
            PreparedStatement auctionBidStatement = connection.prepareStatement(sql);
            auctionBidStatement.setInt(1, auctionID);
            auctionBidStatement.setInt(2, bidderID);
            ResultSet resultSet = auctionBidStatement.executeQuery();

            while (resultSet.next()) {
                auctionbids.add(new AuctionBid(resultSet.getInt("auctionID"), resultSet.getInt("bidderID"),
                        resultSet.getInt("sellerID"), resultSet.getDouble("bidamount"), resultSet.getTimestamp("timestamp").toLocalDateTime()));
            }
            if(auctionbids.size() > 0)
            {
                doesExist = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert connection != null;
                connection.close();
            } catch (SQLException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return doesExist;
    }
    /**
     * Deletes a row from the auction bid table given auction and bidder id
     * @param obj deleted auction bid object
     * @return if the delte was successful
     */
    @Override
    public boolean delete(AuctionBid obj) {
        Connection connection = null;
        try {
            connection = connectionUtils.getConnection();
            String sql = "DELETE FROM " + connectionUtils.getDefaultSchema() + "." + TABLENAME + " WHERE auctionID = ? AND bidderID = ?";
            PreparedStatement auctionBidStatement = connection.prepareStatement(sql);
            auctionBidStatement.setInt(1, obj.getAuctionID());
            auctionBidStatement.setInt(2, obj.getBidderID());
            auctionBidStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                assert connection != null;
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * Updates a row in the auction bid table
     * @param newObj auction row to be updated
     * @return if the row was updated or not
     */
    @Override
    public boolean update(AuctionBid newObj) {
        Connection connection = null;
        String sql = "UPDATE " + connectionUtils.getDefaultSchema() + "." + TABLENAME + " SET bidamount = ?, timestamp = ? WHERE auctionID = ? AND bidderID = ?";
        try
        {
            connection = connectionUtils.getConnection();
            PreparedStatement auctionBidStatement = connection.prepareStatement(sql);

            auctionBidStatement.setDouble(1, newObj.getBidAmount());
            auctionBidStatement.setTimestamp(2, Timestamp.valueOf(newObj.getTimestamp()));
            auctionBidStatement.setInt(3, newObj.getAuctionID());
            auctionBidStatement.setDouble(4, newObj.getBidderID());

            auctionBidStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                assert connection != null;
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
