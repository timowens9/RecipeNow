package RecipeNow;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBaseHelper {

    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String JDBC_URL = "jdbc:derby:RecipeNow;create=true";

    Connection conn;

    public DataBaseHelper() {
        try {
            this.conn = DriverManager.getConnection(JDBC_URL);
            System.out.println("Connected to Database");
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (this.conn != null) {

        }

    }

    public void createUserTable() throws SQLException {
        Connection dbConnection = null;
        Statement statement = null;

        String createTableSQL = "CREATE TABLE USER_NAME("
                + "USER_ID int(5) NOT NULL, "
                + "USERNAME VARCHAR(10) NOT NULL, "
                + "PASSWORD VARCHAR(10) NOT NULL, "
                + "PRIMARY KEY (USER_ID), "
                + ")";

        try {
            
            statement = conn.createStatement();

            System.out.println(createTableSQL);
            // execute the SQL stetement
            //conn.createStatement().execute(createTableSQL);
            statement.execute(createTableSQL);

            System.out.println("Table \"USER_NAME\" is created!");

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        } finally {

            if (statement != null) {
                statement.close();
            }

            if (dbConnection != null) {
                dbConnection.close();
            }

        }
    }

    public void insertIntoTable(int userID, String userName, String password) {

        try {
            conn.createStatement().execute("INSERT INTO USER_NAME Values ('" + userID + "', '" + userName + "', '" + password + "')");
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void printTable() {

        try {
            Statement statement = this.conn.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM UserTable");
            while (res.next()) {
                System.out.println(res.getString("user") + " " + res.getString("password"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(DataBaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
