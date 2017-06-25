package RecipeNow;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JTextField;

public class DatabaseHelper {

    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String JDBC_URL = "jdbc:mysql://"
            + "sql9.freemysqlhosting.net:3306/sql9181289?"
            + "user=sql9181289&password=d5uI7fHA5U";
    private Connection dbConnection = null;
    private Statement statement = null;

    public DatabaseHelper() {
        dbConnection = getDBConnection();
        System.out.println("Connected to Database");

    }

    public void createUserTable() throws SQLException {

        // delete table if exists
        // String deleteTableSQL = "DROP TABLE IF EXISTS recipe_users"; 
        String createTableSQL
                = "CREATE TABLE recipe_users ("
                + "userID INT NOT NULL AUTO_INCREMENT, "
                + "username VARCHAR(20) NOT NULL, "
                + "password VARCHAR(20) NOT NULL, PRIMARY KEY (userID))";

        statement = dbConnection.createStatement();

        statement.execute(createTableSQL);

        System.out.println("Table \"recipe_users\" is created!");

    }

    public void createIngredientTable() throws SQLException {
        String createTableSQL
                = "CREATE TABLE ingredient ("
                + "ingredientID INT NOT NULL AUTO_INCREMENT, "
                + "ingredient_Name VARCHAR(50) NOT NULL, "
                + "calories_Count INT NOT NULL, "
                + "is_dairy VarChar(10), " // is_dariy bit ? 
                + "PRIMARY KEY (ingredientID))";

        statement = dbConnection.createStatement();
        statement.execute(createTableSQL);
        System.out.println("Table \"Ingredient\" is created!");
    }

    public boolean ingredientInsertIntoTable(String ingredName, int calories, int dairy) throws SQLException {

        String insertTableSQL = "INSERT INTO ingredient"
                + "(ingredient_Name, calories_Count, is_dairy) " + "VALUES"
                + "('" + ingredName + "', '" + calories + "', " + dairy + ")";
        /*
        String insertTableSQL = "INSERT INTO ingredient"
                + "(ingredient_Name, calories_Count, is_dairy) " + "VALUES"
                + "('" + ingredName + "', '" + calories + "', '" + dairy + "')";
        */
        //System.out.println(insertTableSQL);

        boolean hasDuplicate = checkDuplicate("ingredient", "ingredient_Name", ingredName);
        if (!hasDuplicate) {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();

            System.out.println(insertTableSQL);

            // execute insert SQL stetement
            statement.executeUpdate(insertTableSQL);
            return false;
        } else {
            System.out.println("There is a duplicate Ingredient");
            return true;
        }

    }
    
    public boolean ingredientEditIntoTable(String ingredName, int calories, int dairy) throws SQLException{
        
        String insertEditTableSQL = "UPDATE ingredient "
                + "SET ingredient_Name = " 
                + "'" + ingredName + "'" + ", "
                + "calories_Count = "
                + "'" + calories + "'" + ", "
                + "is_Dairy = "
                + dairy
                + " WHERE ingredient_Name = " + "'" + ingredName + "';";
        
        System.out.println(insertEditTableSQL);
        dbConnection = getDBConnection();
        statement = dbConnection.createStatement();
        // execute insert SQL stetement
        statement.executeUpdate(insertEditTableSQL);
        return false;
             
    }
    
    public boolean ingredientDeleteIntoTable(String ingredName) throws SQLException {
        
        String insertDeleteTableSQL = "DELETE FROM ingredient "
                + "WHERE ingredient_name = " + "'" + ingredName + "';";
        System.out.println(insertDeleteTableSQL);
        dbConnection = getDBConnection();
        statement = dbConnection.createStatement();
        // execute insert SQL statement
        statement.execute(insertDeleteTableSQL);
        return false;
    }
    
    

    public boolean userInsertIntoTable(String userName, String userPassword) throws SQLException {

        String insertTableSQL = "INSERT INTO recipe_users"
                + "(USERNAME, PASSWORD ) " + "VALUES"
                + "('" + userName + "','" + userPassword + "')";

        boolean hasDuplicate = checkDuplicate("recipe_users", "userName", userName);

        if (!hasDuplicate) {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();

            System.out.println(insertTableSQL);

            // execute insert SQL stetement
            statement.executeUpdate(insertTableSQL);
            return false;
        } else {
            System.out.println("There is a duplicate Username");
            return true;
        }

        /*
        dbConnection = getDBConnection();
        statement = dbConnection.createStatement();

        //System.out.println(insertTableSQL);

        // execute insert SQL stetement
        statement.executeUpdate(insertTableSQL);
         */
    }

    /*
    public void deleteRow(String tableName, String primaryKey) throw SQLException {
        
        String insertTableSQL = "DELETE FROM " + tableName + " WHERE ";
        
        
    }
     */
    public void printUserTable() throws SQLException {

        statement = dbConnection.createStatement();
        ResultSet res = statement.executeQuery("SELECT * FROM recipe_users");
        System.out.println("---------------Currrent Table----------------");
        while (res.next()) {
            System.out.println("UserId: " + res.getString("userID") + " Username: " + res.getString("username")
                    + " Password: " + res.getString("password"));
        }

    }
    
    public void printIngredientTable()throws SQLException {
        
        statement = dbConnection.createStatement();
        ResultSet res = statement.executeQuery("SELECT * FROM ingredient");
        System.out.println("---------------Currrent Table----------------");
        
        while (res.next()) {
            
            System.out.println("Ingredient Name: "  +res.getString("ingredient_Name")+" || " + "Calorie Count: "
                    + res.getString("calories_Count")+" || " + "Is Dairy: " + res.getString("is_dairy"));
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

    private Connection getDBConnection() {

        try {
            Class.forName(DRIVER).newInstance();
            dbConnection = DriverManager.getConnection(JDBC_URL);
            return dbConnection;

        } catch (Exception e) {
            System.out.println(e.getMessage());

        }

        return dbConnection;

    }

    public ResultSet getQuerySet(String query) throws SQLException {

        statement = this.dbConnection.createStatement();
        ResultSet res = statement.executeQuery(query);
        return res;

    }

    public boolean checkDuplicate(String tableName, String columnName, String target) throws SQLException {

        boolean hasDuplicate = false;
        statement = this.dbConnection.createStatement();
        ResultSet res = statement.executeQuery("SELECT * FROM " + tableName);

        while (res.next()) {
            // Check duplicate
            if (res.getString(columnName).equals(target)) {
                //System.out.println(target);             
                return true;
            }
        }

        return hasDuplicate;

    }

    public boolean deleteAccount(String username) throws SQLException {

        statement = dbConnection.createStatement();
        int userID;
        // account table 
        ResultSet res = statement.executeQuery("SELECT * FROM recipe_users WHERE username='" + username + "'");
        if (res.first()) {
            userID = res.getInt("userID");
        } else {
            return false;
        }

        System.out.println(userID);
        // Delete account by userid
        String deleteStatement = "DELETE FROM recipe_users WHERE userID=" + userID;
        int deleteResult = statement.executeUpdate(deleteStatement);
        return deleteResult > 0;
    }

}
