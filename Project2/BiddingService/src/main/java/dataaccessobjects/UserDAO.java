package dataaccessobjects;

import dataaccess.ConnectionUtils;
import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private ConnectionUtils connectionUtils;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    /**
     * Arged constructor to create a UserDAO
     * @param connectionUtils The connectionUtils to use when creating the UserDAO
     * UserDAO constructor
     */
    public UserDAO(ConnectionUtils connectionUtils) {
        if(connectionUtils != null) {
            this.connectionUtils = connectionUtils;
        }
    }

    /**
     *
     * @param name
     * @return
     */
    public User findByUserName(String name){
        Connection connection = null;
        User newUser = new User();
        try {
            connection = connectionUtils.getConnection();
            String sql = "SELECT * FROM " + connectionUtils.getDefaultSchema() + "." + "users"
                    + " WHERE username = ?";
            PreparedStatement auctionBidStatement = connection.prepareStatement(sql);
            auctionBidStatement.setString(1, name);
            ResultSet resultSet = auctionBidStatement.executeQuery();

            if (resultSet.next()) {
                newUser = new User(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getInt(5));
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
        return newUser;
    }

}//End UserDao









