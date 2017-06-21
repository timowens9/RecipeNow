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
                
                
                

		String createTableSQL = "CREATE TABLE USER_LIST("
				+ "USER_ID int NOT NULL, "
				+ "USERNAME VARCHAR(20) NOT NULL, "
				+ "PASSWORD VARCHAR(20) NOT NULL, "
				+ "PRIMARY KEY (USERNAME) "
				+ ")";

		try {
			dbConnection = getDBConnection();
			statement = dbConnection.createStatement();

			System.out.println(createTableSQL);
                        // execute the SQL stetement
			statement.execute(createTableSQL);

			System.out.println("Table \"dbuser\" is created!");

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

    public void insertIntoTable(int userNum, String userName, String userPassword) throws SQLException {

        Connection dbConnection = null;
		Statement statement = null;

		String insertTableSQL = "INSERT INTO USER_LIST"
				+ "(USER_ID, USERNAME, PASSWORD ) " + "VALUES"
				+ "(userID,'userName','userPassword')";
				

		try {
			dbConnection = getDBConnection();
			statement = dbConnection.createStatement();

			System.out.println(insertTableSQL);

			// execute insert SQL stetement
			statement.executeUpdate(insertTableSQL);

			System.out.println("Record is inserted into USER_LIST table!");

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

    private static Connection getDBConnection() {

        Connection dbConnection = null;

        try {
		Class.forName(DRIVER).newInstance();
		dbConnection = DriverManager.getConnection(JDBC_URL);
		return dbConnection;

        } catch (ClassNotFoundException|SQLException e) {
		System.out.println(e.getMessage());

        }

        try {



        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

        return dbConnection;

    }

}
