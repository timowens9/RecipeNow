package RecipeNow;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.ObservableList;

public class DatabaseHelper {

    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String JDBC_URL = "jdbc:mysql://"
            + "35.190.183.62:3306/recipenow?"
            + "user=eric&password=cv@zE.#jv:j8T_sA&useSSL=false";
    private Connection dbConnection = null;
    private Statement statement = null;

    public DatabaseHelper() {
        dbConnection = getDBConnection();

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
                + "is_dairy BIT, " // is_dariy bit ? 
                + "PRIMARY KEY (ingredientID))";

        statement = dbConnection.createStatement();
        statement.execute(createTableSQL);
        System.out.println("Table \"Ingredient\" is created!");
    }
    
    public void createRecipeTable() throws SQLException {
        String createTableSQL
                = "CREATE TABLE recipe ("
                + "recipeID INT NOT NULL AUTO_INCREMENT, "
                + "recipe_Name VARCHAR(50) NOT NULL, "
                + "recipe_Description VARCHAR(5000) NOT NULL, "
                + "recipe_Ingredients VARCHAR(200) NOT NULL, "
                + "recipe_ChefID INT NOT NULL, "
                + "recipe_numCooked INT NOT NULL, "
                + "recipe_Rating INT NOT NULL, "
                + "recipe_Calorie INT NOT NULL, "
                + "PRIMARY KEY (recipeID))";
        //String createTableSQL1 = "DROP TABLE IF EXISTS recipe;";
        statement = dbConnection.createStatement();
        statement.execute(createTableSQL);
        System.out.println("Table \"Recipe\" is created!");
    }
    public boolean ingredientInsertIntoTable(Ingredient ingredient){
        String dairy  = ingredient.isDairy()?"1":"0";
        String insertTableSQL = "INSERT INTO ingredient"
                + "(ingredient_Name, calories_Count, is_dairy) " + "VALUES"
                + "('" + ingredient.getName() + "', '" + ingredient.getCalories() + "', " + dairy + ")";
                
        

        boolean hasDuplicate = checkDuplicate("ingredient", "ingredient_Name", ingredient.getName());
        if (!hasDuplicate) {
            try{
                statement = dbConnection.createStatement();
            

                System.out.println(insertTableSQL);

                // execute insert SQL stetement
                statement.executeUpdate(insertTableSQL);
            
            }
            catch(SQLException e){
                System.err.println("SQLException: "+e.getMessage());
            }
            return false;
        } else {
            System.out.println("There is a duplicate Ingredient or there was an error");
            return true;
        }
    }
    public boolean ingredientInsertIntoTable(String ingredName, int calories, int dairy) throws SQLException {
        
        Ingredient ingredient = new Ingredient(0, ingredName, calories, dairy);
        return ingredientInsertIntoTable(ingredient);

    }
    
    public boolean ingredientEditIntoTable(Ingredient ingredient){
        String dairy = ingredient.isDairy()?"1":"0";
        String insertEditTableSQL = "UPDATE ingredient "
                + "SET ingredient_Name = '"
                + ingredient.getName() + "', "
                + "calories_Count = '"
                + ingredient.getCalories() + "', "
                + "is_Dairy = '"
                + dairy
                + "' WHERE ingredientID = " + "'" + ingredient.getID() + "';";
        
        System.out.println(insertEditTableSQL);
        try{
            statement = dbConnection.createStatement();
            // execute insert SQL stetement
            boolean ingredientExists = checkDuplicate("ingredient", "ingredientID", Integer.toString(ingredient.getID()));
            if(ingredientExists) {
                statement.execute(insertEditTableSQL);
                return true;
            }
        }
        catch(SQLException e){
            System.err.println("SQLException: " + e.getMessage());
        }
        return false;
             
    }
    
    public boolean ingredientDeleteIntoTable(Ingredient ingredient){
        
        String insertDeleteTableSQL = "DELETE FROM ingredient "
                + "WHERE ingredientID = " + "'" + ingredient.getID() + "';";
        System.out.println(insertDeleteTableSQL);
        try{
            statement = dbConnection.createStatement();
            // execute insert SQL statement
            boolean ingredientExists = checkDuplicate("ingredient", "ingredientID", Integer.toString(ingredient.getID()));
            if(ingredientExists) {
                statement.execute(insertDeleteTableSQL);
                return true;
            }
        }
        catch(SQLException e){
            System.err.println("SQLException: " + e.getMessage());
        }
        return false;
    }
    
    public boolean recipeDeleteIntoTable(String recipeName) throws SQLException {
        String recipeDeleteTableSQL = "DELETE FROM recipe "
                + "WHERE recipe_Name = " + "'" + recipeName + "';";
        System.out.println(recipeDeleteTableSQL);
        dbConnection = getDBConnection();
        statement = dbConnection.createStatement();
        // execute insert SQL statement
        boolean recipeExists = checkDuplicate("recipe", "recipe_Name", recipeName);
        if(recipeExists) {
            statement.execute(recipeDeleteTableSQL);
            return false;
        }

        return true;
    }
    
    public boolean recipeInsertIntoTable(String recipeName, String recipeDescription, String ingredientIds, int chefID, int rating, int calorie ) throws SQLException {
        //String insertTableSQL = "INSERT INTO recipe"
          //      + "(recipe_Name, recipe_Description, recipe_Ingredients, recipe_ChefID, recipe_numCooked, recipe_rating) " + "VALUES"
             //   + "('" + recipeName + "', '" + recipeDescription + "', '" + ingredientIds + "', '" + chefID +  "', '" + '0' + "', '" + rating "')";
        String insertTableSQL = "INSERT INTO recipe"
                + "(recipe_Name, recipe_Description, recipe_Ingredients, recipe_ChefID, recipe_numCooked, recipe_rating, recipe_Calorie ) " + "VALUES"
                + "('" + recipeName + "','" + recipeDescription + "','" + ingredientIds +
                "','" + chefID + "','" + "0" +  "','" + rating + "','" + calorie + "')";        
        

        boolean hasDuplicate = checkDuplicate("recipe", "recipe_Name", recipeName);
        if (!hasDuplicate) {
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
    
    public boolean userInsertIntoTable(String userName, String userPassword) throws SQLException {

        String insertTableSQL = "INSERT INTO recipe_users"
                + "(USERNAME, PASSWORD ) " + "VALUES"
                + "('" + userName + "','" + userPassword + "')";

        boolean hasDuplicate = checkDuplicate("recipe_users", "username", userName);

        if (!hasDuplicate) {
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
        System.out.println("---------------Currrent User Table----------------");
        while (res.next()) {
            System.out.println("UserId: " + res.getString("userID") + " Username: " + res.getString("username")
                    + " Password: " + res.getString("password"));
        }

    }
    
    public void printRecipeTable() throws SQLException {
        
        statement = dbConnection.createStatement();
        ResultSet res = statement.executeQuery("SELECT * FROM recipe");
        System.out.println("---------------Currrent Recipe Table----------------");
        while (res.next()) {
            System.out.println("RecipeId: " + res.getString("recipeID") + " Recipe Name: " + res.getString("recipe_Name"));
            System.out.println("Recipe Description: " + res.getString("recipe_Description"));
            
            String[] recipeSplit = res.getString("recipe_Ingredients").split(",");
            String ingredientNames = "";
            for(int i = 0; i < recipeSplit.length; i++) {
                String query = "SELECT ingredient_name FROM ingredient "
                + "WHERE ingredientID = " + "'" + recipeSplit[i] + "';";
                ResultSet resultSet = getQuerySet(query);
                if (resultSet.first()) {
                    ingredientNames += resultSet.getString("ingredient_name") + ", ";
                }
            }
            
            // Remove last comma
            ingredientNames = ingredientNames.substring(0, ingredientNames.length()-2);
            
            System.out.println("Recipe Ingredients: " + ingredientNames);
            System.out.println("Recipe ChedId: " + res.getString("recipe_ChefID") + " Recipe Number of Times Cooked: " + res.getString("recipe_numCooked"));
            System.out.println("Recipe Rating: " + res.getString("recipe_rating"));
            System.out.println("Recipe Calroie: " + res.getString("recipe_Calorie"));
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
            System.out.println("Connected to Database");
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

    public boolean checkDuplicate(String tableName, String columnName, String target) {
        try{
        statement = dbConnection.createStatement();
        ResultSet res = statement.executeQuery("SELECT * FROM " + tableName);

        while (res.next()) {
            // Check duplicate
            if (res.getString(columnName).equals(target)) {
                //System.out.println(target);             
                return true;
            }
        }

        return false;
        }
        catch(SQLException e){
            System.err.println("SQLException: "+ e.getMessage());
        }
        
        return true;
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

        // Delete account by userid
        String deleteStatement = "DELETE FROM recipe_users WHERE userID=" + userID;
        int deleteResult = statement.executeUpdate(deleteStatement);
        return deleteResult > 0;
    }

    public ObservableList<Ingredient> updateIngredientList(ObservableList<Ingredient> curList){
        try{
            ResultSet res = getQuerySet("SELECT * FROM ingredient");
            while (res.next()){
                boolean dupl = false;
                for(Ingredient ing: curList){
                    if (ing.getName().equals(res.getString("ingredient_Name")))
                        dupl = true;
                }
                Ingredient cur = new Ingredient(res.getInt("ingredientID"), res.getString("ingredient_Name"), res.getInt("calories_Count"), res.getInt("is_dairy"));
                if (!dupl)
                    curList.add(cur);
            }
            return curList;
        }
        catch(SQLException ex){
            System.err.println("SQL Exception: " + ex.getMessage());
        }
        return null;
    }

   


}
