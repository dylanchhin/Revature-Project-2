package auction.dataaccess;

import auction.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDAO that interacts with the database
 */
public class UserDAO implements DAO<User, Integer> {
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
     * No arg constructor
     */
    public UserDAO(){
        this.connectionUtils = new PostGresConnectionUtil();
    }

    /**
     *
     * @param user The user to be persisted
     * @return boolean
     * attempts to save data into the database and returns a boolean specifying
     * whether or not it was successful
     */
    @Override
    public boolean save(User user) {
        connection = null;
        preparedStatement = null;
        Boolean exists = ifExists(user.getUserName());

        if (exists){
            System.out.println("User Name taken and cannot be added");
            return false;
        }
        String saveStatement = "INSERT INTO " + connectionUtils.getDefaultSchema() + "." + "users"
                + " (username, password, cardinfo, userrole) VALUES (?,?,?,?)";
        try {
            connection = connectionUtils.getConnection();
            preparedStatement = connection.prepareStatement(saveStatement);
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getCreditCardNumber());
            preparedStatement.setInt(4, user.getRole());
            preparedStatement.executeUpdate();
            System.out.println(preparedStatement);
            closeAll(connection, preparedStatement);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @return A list of the users in the system
     */
    @Override
    public List<User> retrieveAll() {
        connection = null;
        resultSet = null;
        preparedStatement = null;
        List<User> userList = new ArrayList<>();
        String selectStatement = "SELECT * FROM " + connectionUtils.getDefaultSchema() + "." + "users";

        try {
            connection = connectionUtils.getConnection();
            preparedStatement = connection.prepareStatement(selectStatement);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                userList.add(new User(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getInt(5)));
            }
            if(resultSet != null) resultSet.close();
            closeAll(connection, preparedStatement);
        } catch (SQLException e ) {
            e.printStackTrace();
        }
        return userList;
    }

    /**
     * @param id The id of the User to be returned
     * @return The User t be returned given the id
     */
    @Override
    public User retrieveByID(Integer id) {
        connection = null;
        preparedStatement = null;
        User user = new User();
        resultSet = null;

        if(id==-1){
            user.setUserId(findByUserName(user.getUserName()));
        }

        try{
            connection = connectionUtils.getConnection();
            String selectStatement = "SELECT * FROM " + connectionUtils.getDefaultSchema() + "." + "users"
                    + " WHERE  userid = " + id;
            preparedStatement = connection.prepareStatement(selectStatement);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user.setUserId(resultSet.getInt(1));
                user.setUserName(resultSet.getString(2));
                user.setPassword(resultSet.getString(3));
                user.setCreditCardNumber(resultSet.getString(4));
                user.setRole(resultSet.getInt(5));
            }
            else
                System.out.println("No user by that id found.");
            if(resultSet != null) resultSet.close();
            closeAll(connection, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * @param user The user to be removed
     * @return Returns whether or not the operation succeeded
     */
    @Override
    public boolean delete(User user) {
        connection = null;
        preparedStatement = null;
        if(user.getUserId()==-1){
            user.setUserId(findByUserName(user.getUserName()));
        }

        String deleteStatement = "DELETE FROM " + connectionUtils.getDefaultSchema() + "." + "users"
                + " WHERE userid = ?";
        try {
            connection = connectionUtils.getConnection();
            preparedStatement = connection.prepareStatement(deleteStatement);
            preparedStatement.setInt(1, user.getUserId());
            preparedStatement.executeUpdate();
            closeAll(connection, preparedStatement);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param user the id of the user to be updated
     * @return Returns whether or not the operation succeeded
     */
    @Override
    public boolean update(User user) {
        connection = null;
        preparedStatement = null;
        if(user.getUserId()==-1){
            user.setUserId(findByUserName(user.getUserName()));
        }
        else
            System.out.println("Now the userID is: " + user.getUserName());

        String updateStatement = "UPDATE " + connectionUtils.getDefaultSchema() + "." + "users"
                + " SET username = ?, password = ?, cardinfo = ?, userrole = ? WHERE userid = ?";
        try
        {
            connection = connectionUtils.getConnection();
            preparedStatement = connection.prepareStatement(updateStatement);
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getCreditCardNumber());
            preparedStatement.setInt(4, user.getRole());
            preparedStatement.setInt(5, user.getUserId());
            preparedStatement.executeUpdate();
            System.out.println(preparedStatement);
            closeAll(connection, preparedStatement);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param name
     * @return
     */
    private int findByUserName(String name){
        int result = 0;
        connection = null;
        preparedStatement = null;
        resultSet = null;
        String findUserId = "SELECT userid FROM " + connectionUtils.getDefaultSchema() + "." + "users"
                + " WHERE username = \'" + name + "\'";
        System.out.println(findUserId);
        try
        {
            connection = connectionUtils.getConnection();
            preparedStatement = connection.prepareStatement(findUserId);
            //ExecuteQuery used for select
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result = resultSet.getInt(1);
                System.out.println("User found");
            }
            if(resultSet != null) resultSet.close();
            closeAll(connection, preparedStatement);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * @param userName
     * @return boolean
     */
    private boolean ifExists(String userName){
        connection = null;
        preparedStatement = null;
        resultSet = null;
        String checkUsers = "SELECT * FROM " + connectionUtils.getDefaultSchema() + "." + "users"
                + " WHERE username = \'" + userName + "\'";
        try
        {
            connection = connectionUtils.getConnection();
            preparedStatement = connection.prepareStatement(checkUsers);
            //ExecuteQuery used for insert,update,delete
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
               return true;
            }
            if(resultSet != null) resultSet.close();
            closeAll(connection, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     *
     * @param connection
     * @param preparedStatement
     */
    private void closeAll(Connection connection, PreparedStatement preparedStatement){
        try {
            if (preparedStatement != null)
                preparedStatement.close();
            if (connection != null)
                connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }//End closeAll

    /**
     *
     * @param userId The id of the user to check if they have a current session
     * @return Returns whether or not the operation succeeded
     */
    public boolean getSession(int userId){
        connection = null;
        preparedStatement = null;
        resultSet = null;
        Boolean foundSession = null;
        String checkSession = "SELECT userid FROM ebay_schema.session WHERE userid = " + userId + ";";
        try{
            connection = connectionUtils.getConnection();
            preparedStatement = connection.prepareStatement(checkSession);
            resultSet = preparedStatement.executeQuery();
            System.out.println(preparedStatement);
            if (resultSet.next()) {
                foundSession = true;
                System.out.println("Not adding");
            }
            else {
                foundSession = false;
                System.out.println("adding");
            }
            closeAll(connection, preparedStatement);
            return foundSession;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return foundSession;
    }

    /**
     *
     * Creates the session table if it doesn't exist
     */
    public void createSession()
    {
        String createTable = "CREATE TABLE if not exists ebay_schema.session ("
                +  "sessionid serial PRIMARY KEY,"
                +  "userid serial REFERENCES ebay_schema.users(userid) UNIQUE"
                +  ");";
        connection = null;
        preparedStatement = null;
        try{
            connection = connectionUtils.getConnection();
            preparedStatement = connection.prepareStatement(createTable);
            preparedStatement.executeUpdate();


        }catch (SQLException e){
            e.printStackTrace();
        }
        closeAll(connection, preparedStatement);
    }

    /**
     *
     * @param user The user to be inserted into the session table
     * @return returns a 0 if no session was created, otherwise returns a 1
     */
    public int insertSession(User user){
        String insertUser = "INSERT INTO ebay_schema.session(userid) VALUES ('" + user.getUserId() + "');";
        int inserted = 0;
        connection = null;
        preparedStatement = null;
        try{
            connection = connectionUtils.getConnection();
            preparedStatement = connection.prepareStatement(insertUser);
            inserted = preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        closeAll(connection, preparedStatement);
        if (inserted==0)
            System.out.println("Session already exists");
        else
            System.out.println("Session added");
        return inserted;
    }

    /**
     *
     * @param sessionId The session ID to be removed from the session table
     * @return Returns whether or not the operation succeeded
     */
    public boolean removeSession(int sessionId){
        String logoutString = "DELETE FROM  ebay_schema.session WHERE sessionid = "
                + sessionId + ";";
        int inserted = 0;
        connection = null;
        preparedStatement = null;
        try{
            connection = connectionUtils.getConnection();
            preparedStatement = connection.prepareStatement(logoutString);
            inserted = preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        closeAll(connection, preparedStatement);
        if (inserted==0){
            System.out.println("No session to remove");
            return false;
        }
        else {
            System.out.println("Session removed");
            return true;
        }
    }
}//End UserDao









