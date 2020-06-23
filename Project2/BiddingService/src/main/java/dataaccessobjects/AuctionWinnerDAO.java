package dataaccessobjects;

import dataaccess.ConnectionUtils;
import models.AuctionWinner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuctionWinnerDAO implements DAO<AuctionWinner, Integer> {

    private ConnectionUtils connectionUtils;
    private static final String TABLENAME = "auctionwinner";
    public AuctionWinnerDAO(ConnectionUtils connectionUtils) {
        if(connectionUtils != null) {
            this.connectionUtils = connectionUtils;
        }
    }

    /**
     * Insert new winner int otable
     * @param obj New winner
     * @return if the row was succeesfully saved
     */
    @Override
    public boolean save(AuctionWinner obj) {

        Connection connection = null;
        PreparedStatement auctionBidStatement;
        boolean wasPassed = false;
        if(obj.getWinnerID() == 0)
        {
            try {
                connection = connectionUtils.getConnection();
                String saveStatement = "INSERT INTO " + connectionUtils.getDefaultSchema() + "." + TABLENAME + " (winnerID, auctionID, bidderID, winningAmount) VALUES (default,?,?,?)";
                auctionBidStatement = connection.prepareStatement(saveStatement);
                auctionBidStatement.setInt(1, obj.getAuctionID());
                auctionBidStatement.setInt(2, obj.getBidderID());
                auctionBidStatement.setDouble(3, obj.getWinningAmount());
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
        }
        else
        {
            try {
                connection = connectionUtils.getConnection();
                String saveStatement = "INSERT INTO " + connectionUtils.getDefaultSchema() + "." + TABLENAME + " (winnerID, auctionID, bidderID, winningAmount) VALUES (?,?,?,?)";
                auctionBidStatement = connection.prepareStatement(saveStatement);
                auctionBidStatement.setInt(1, obj.getWinnerID());
                auctionBidStatement.setInt(2, obj.getAuctionID());
                auctionBidStatement.setInt(3, obj.getBidderID());
                auctionBidStatement.setDouble(4, obj.getWinningAmount());
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
        }

        return wasPassed;
    }

    /**
     * Did not need to use this function
     * @return Would have returned the list of all winners
     */
    @Override
    public List<AuctionWinner> retrieveAll() {
        return null;
    }

    /**
     * Retrieve all winners by the bidder id
     * @param bidderID passed bidder id
     * @return List of winners by bidder id
     */
    public List<AuctionWinner> retrieveAllByBidderID(Integer bidderID) {
        Connection connection = null;
        ArrayList<AuctionWinner> auctionWinners = new ArrayList<>();
        try {
            connection = connectionUtils.getConnection();
            String sql = "SELECT * FROM " + connectionUtils.getDefaultSchema() + "." + TABLENAME + " WHERE bidderID = ?";
            PreparedStatement auctionBidStatement = connection.prepareStatement(sql);
            auctionBidStatement.setInt(1, bidderID);
            ResultSet resultSet = auctionBidStatement.executeQuery();

            while (resultSet.next()) {
                auctionWinners.add(new AuctionWinner(resultSet.getInt("winnerID"), resultSet.getInt("auctionID"),
                        resultSet.getInt("bidderID"), resultSet.getDouble("winningAmount")));
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
        return auctionWinners;
    }

    /**
     * Rerieve row by winner id
     * @param integer bassed id
     * @return the specified winner
     */
    @Override
    public AuctionWinner retrieveByID(Integer integer) {
        Connection connection = null;
        AuctionWinner auctionWinners = null;
        try {
            connection = connectionUtils.getConnection();
            String sql = "SELECT * FROM " + connectionUtils.getDefaultSchema() + "." + TABLENAME + " WHERE winnerid = ?";
            PreparedStatement auctionBidStatement = connection.prepareStatement(sql);
            auctionBidStatement.setInt(1, integer);
            ResultSet resultSet = auctionBidStatement.executeQuery();

            if (resultSet.next()) {
                auctionWinners = (new AuctionWinner(resultSet.getInt("winnerID"), resultSet.getInt("auctionID"),
                        resultSet.getInt("bidderID"), resultSet.getDouble("winningAmount")));
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
        return auctionWinners;
    }

    /**
     * Delete specified row
     * @param obj row to be deleted
     * @return If the delete was sucessful or not
     */
    @Override
    public boolean delete(AuctionWinner obj) {
        Connection connection = null;
        try {
            connection = connectionUtils.getConnection();
            String sql = "DELETE FROM " + connectionUtils.getDefaultSchema() + "." + TABLENAME + " WHERE winnerid = ?";
            PreparedStatement auctionBidStatement = connection.prepareStatement(sql);
            auctionBidStatement.setInt(1, obj.getWinnerID());
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
     * Update row
     * @param newObj row to be updated
     * @return if the update was successful or not
     */
    @Override
    public boolean update(AuctionWinner newObj) {
        Connection connection = null;
        String sql = "UPDATE " + connectionUtils.getDefaultSchema() + "." + TABLENAME + " SET auctionID = ?, bidderID = ?,  winningAmount = ? WHERE winnerID = ?";
        try
        {
            connection = connectionUtils.getConnection();
            PreparedStatement auctionBidStatement = connection.prepareStatement(sql);
            auctionBidStatement.setInt(1, newObj.getAuctionID());
            auctionBidStatement.setInt(2, newObj.getBidderID());
            auctionBidStatement.setDouble(3, newObj.getWinningAmount());
            auctionBidStatement.setInt(4, newObj.getWinnerID());
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
}
