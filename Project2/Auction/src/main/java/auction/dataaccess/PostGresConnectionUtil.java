package auction.dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostGresConnectionUtil extends ConnectionUtils {
    static {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public PostGresConnectionUtil() {
        this.url = "jdbc:postgresql://" + System.getenv("POSTGRES_URL") + ":" + System.getenv("POSTGRES_PORT") + "/" + System.getenv("POSTGRES_DATABASE_NAME");
        this.username = System.getenv("POSTGRES_USERNAME");
        this.password = System.getenv("POSTGRES_PASSWORD");
        this.defaultSchema = System.getenv("POSTGRES_DEFAULT_SCHEMA");
    }

    public PostGresConnectionUtil(String url, String username, String password, String schema) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.defaultSchema = schema;
    }


    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
