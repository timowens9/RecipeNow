package RecipeNow;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class DatabaseHelper {

    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String JDBC_URL = "jdbc:mysql://"
            + "sql9.freemysqlhosting.net:3306/sql9181289?"
            + "user=sql9181289&password=d5uI7fHA5U";
    private static Connection dbConnection = null;
    private static Statement statement = null;

    public DatabaseHelper() {
        dbConnection = getDBConnection();
        System.out.println("Connected to Database");

    }

    public void createUserTable() throws SQLException {
        String createTableSQL = "CREATE TABLE recipe_users ("
                + "userID INT NOT NULL AUTO_INCREMENT, "
                + "username VARCHAR(20) NOT NULL, "
                + "password VARCHAR(20) NOT NULL, PRIMARY KEY (userID))";
        statement = dbConnection.createStatement();

        System.out.println(createTableSQL);
        // execute the SQL stetement
        statement.execute(createTableSQL);
        System.out.println("Table \"dbuser\" is created!");

    }

    public void insertIntoTable(String userName, String userPassword) throws SQLException {

        String insertTableSQL = "INSERT INTO recipe_users"
                + "(USERNAME, PASSWORD ) " + "VALUES"
                + "('"+userName+"','"+userPassword+"')";
        
                

        dbConnection = getDBConnection();
        statement = dbConnection.createStatement();

        System.out.println(insertTableSQL);

        // execute insert SQL stetement
        statement.executeUpdate(insertTableSQL);

        System.out.println("Record is inserted into recipe_users table!");
    }

    public void printTable() throws SQLException {

        statement = dbConnection.createStatement();
        ResultSet res = statement.executeQuery("SELECT * FROM recipe_users");
        while (res.next()) {
            System.out.println("User: " + res.getString("username") + "; Password: " + res.getString("password"));
        }

    }


    public void closeConnection() throws SQLException {
        if (statement != null) {
            statement.close();
        }

        if (dbConnection != null) {
            dbConnection.close();
        }
    }
    private static Connection getDBConnection() {

        try {
            Class.forName(DRIVER).newInstance();
            dbConnection = DriverManager.getConnection(JDBC_URL);
            return dbConnection;

        } catch (Exception e) {
            System.out.println(e.getMessage());

        }

        return dbConnection;

    }

}
