/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RecipeNow;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class app extends Application {

    public static Stage base;
    public static Scene login;
    public static NewUserViewFXController loginCntl;

    @Override
    public void start(Stage primaryStage) throws Exception {
        base = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("NewUserViewFXML.fxml"));
        Parent root = loader.load();
        loginCntl = loader.getController();
        base.setTitle("RecipeNow - Login");
        login = new Scene(root);
        base.setScene(login);
        base.show();
    }

    public static void main(String[] args) { 
        DatabaseHelper.connectToDatabase();
        launch();
    }
    @Override
    public void stop(){
        DatabaseHelper.closeConnection();
    }
    public static class DatabaseHelper {

        private static final String DRIVER = "com.mysql.jdbc.Driver";
        private static final String JDBC_URL = "jdbc:mysql://"
                + "35.190.183.62:3306/recipenow?"
                + "user=eric&password=cv@zE.#jv:j8T_sA&useSSL=false";
        private static Connection dbConnection;
        private static PreparedStatement pStatement = null;
        private static Statement statement = null;

        public static void createUserTable() throws SQLException {

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

        public static void printUserTable() {
            try {
                statement = dbConnection.createStatement();
                ResultSet res = statement.executeQuery("SELECT * FROM recipe_users");
                System.out.println("---------------Currrent User Table----------------");
                while (res.next()) {
                    System.out.println("UserId: " + res.getString("userID") + " Username: " + res.getString("username")
                            + " Password: " + res.getString("password"));
                }
            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
            }
        }

        public static void createIngredientTable() throws SQLException {
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

        public static void createRecipeTable() throws SQLException {
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

        public static boolean ingredientInsertIntoTable(Ingredient ingredient) {
            String dairy = ingredient.isDairy() ? "1" : "0";
            String insertTableSQL = "INSERT INTO ingredient"
                    + "(ingredient_Name, calories_Count, is_dairy) " + "VALUES"
                    + "('" + ingredient.getName() + "', '" + ingredient.getCalories() + "', " + dairy + ")";

            boolean hasDuplicate = checkDuplicate("ingredient", "ingredient_Name", ingredient.getName());
            if (!hasDuplicate) {
                try {
                    statement = dbConnection.createStatement();

                    System.out.println(insertTableSQL);

                    // execute insert SQL stetement
                    statement.executeUpdate(insertTableSQL);

                } catch (SQLException e) {
                    System.err.println("SQLException: " + e.getMessage());
                }
                return false;
            } else {
                System.out.println("There is a duplicate Ingredient or there was an error");
                return true;
            }
        }

        public static boolean ingredientEditIntoTable(Ingredient ingredient) {
            String dairy = ingredient.isDairy() ? "1" : "0";
            String insertEditTableSQL = "UPDATE ingredient "
                    + "SET ingredient_Name = '"
                    + ingredient.getName() + "', "
                    + "calories_Count = '"
                    + ingredient.getCalories() + "', "
                    + "is_Dairy = "
                    + dairy
                    + " WHERE ingredientID = " + "'" + ingredient.getID() + "';";

            System.out.println(insertEditTableSQL);
            try {
                statement = dbConnection.createStatement();
                // execute insert SQL stetement
                boolean ingredientExists = checkDuplicate("ingredient", "ingredientID", Integer.toString(ingredient.getID()));
                if (ingredientExists) {
                    statement.execute(insertEditTableSQL);
                    return true;
                }
            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
            }
            return false;

        }

        public static boolean ingredientDeleteIntoTable(Ingredient ingredient) {

            String insertDeleteTableSQL = "DELETE FROM ingredient "
                    + "WHERE ingredientID = " + "'" + ingredient.getID() + "';";
            System.out.println(insertDeleteTableSQL);
            try {
                statement = dbConnection.createStatement();
                // execute insert SQL statement
                boolean ingredientExists = checkDuplicate("ingredient", "ingredientID", Integer.toString(ingredient.getID()));
                if (ingredientExists) {
                    statement.execute(insertDeleteTableSQL);
                    return true;
                }
            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
            }
            return false;
        }

        public static boolean recipeInsertIntoTable(Recipe recipe) {

            try {
                boolean hasDuplicate = checkDuplicate("recipe", "recipeID", Integer.toString(recipe.getID()));
                if (!hasDuplicate) {
                    pStatement = dbConnection.prepareStatement("INSERT INTO recipe"
                            + "(recipe_Name, recipe_Description, recipe_Ingredients, recipe_ChefID, recipe_numCooked, recipe_Rating, recipe_Calorie ) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?)");
                    pStatement.setString(1, recipe.getName());
                    pStatement.setString(2, recipe.getDescription());
                    pStatement.setString(3, recipe.getIngredients());
                    pStatement.setInt(4, recipe.getChefID());
                    pStatement.setInt(5, recipe.getNumCooked());
                    pStatement.setInt(6, recipe.getRating());
                    pStatement.setInt(7, recipe.getCalories());

                    // execute insert SQL stetement
                    pStatement.executeUpdate();
                    return false;
                } else {
                    System.err.println("There is a duplicate Recipe");
                }
            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
            }
            return true;
        }

        public static boolean recipeEditIntoTable(Recipe recipe) {

            try {
                pStatement = dbConnection.prepareStatement("UPDATE recipe SET recipe_Name = ?,"
                        + "recipe_Description = ?, recipe_Ingredients = ?, recipe_chefID = ?, "
                        + "recipe_numCooked = ?, recipe_Rating = ?, recipe_Calorie = ? WHERE recipeID = ?");
                pStatement.setString(1, recipe.getName());
                pStatement.setString(2, recipe.getDescription());
                pStatement.setString(3, recipe.getIngredients());
                pStatement.setInt(4, recipe.getChefID());
                pStatement.setInt(5, recipe.getNumCooked());
                pStatement.setInt(6, recipe.getRating());
                pStatement.setInt(7, recipe.getCalories());
                pStatement.setInt(8, recipe.getID());
                // execute insert SQL stetement
                boolean recipeExists = checkDuplicate("recipe", "recipeID", Integer.toString(recipe.getID()));
                if (recipeExists) {
                    pStatement.execute();
                    return true;
                }
            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
            }
            return false;

        }

        public static boolean recipeDeleteIntoTable(Recipe recipe) {
            String recipeDeleteTableSQL = "DELETE FROM recipe "
                    + "WHERE recipeID = " + "'" + recipe.getID() + "';";
            System.out.println(recipeDeleteTableSQL);
            try {
                statement = dbConnection.createStatement();
                // execute insert SQL statement
                boolean recipeExists = checkDuplicate("recipe", "recipeID", Integer.toString(recipe.getID()));
                if (recipeExists) {
                    statement.execute(recipeDeleteTableSQL);
                    return false;
                }
            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
            }
            return true;
        }

        public static boolean userInsertIntoTable(String userName, String userPassword) {

            String insertTableSQL = "INSERT INTO recipe_users"
                    + "(USERNAME, PASSWORD ) " + "VALUES"
                    + "('" + userName + "','" + userPassword + "')";

            boolean hasDuplicate = checkDuplicate("recipe_users", "username", userName);
            try {
                if (!hasDuplicate) {
                    statement = dbConnection.createStatement();

                    System.out.println(insertTableSQL);

                    // execute insert SQL stetement
                    statement.executeUpdate(insertTableSQL);
                    return false;
                } else {
                    System.out.println("There is a duplicate Username");
                }
            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
            }
            return true;
        }

        public static void closeConnection() {
            try {

                if (statement != null) {
                    statement.close();
                }

                if (dbConnection != null) {
                    dbConnection.close();
                }
                System.out.println("Disconnected from database");
            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
            }
        }

        private static void connectToDatabase() {

            try {
                Class.forName(DRIVER).newInstance();
                if (dbConnection == null || dbConnection.isClosed()){
                    dbConnection = DriverManager.getConnection(JDBC_URL);
                    System.out.println("Connected to Database");
                }
                else
                    System.out.println("Connection already open");

            } catch (Exception e) {
                System.out.println(e.getMessage());

            }
        }

        public static ResultSet getQuerySet(String query) {
            try {

                statement = dbConnection.createStatement();
                ResultSet res = statement.executeQuery(query);
                return res;
            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
            }
            return null;
        }

        public static boolean checkDuplicate(String tableName, String columnName, String target) {
            try {
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
            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
            }

            return true;
        }

        public static boolean deleteAccount(String username) {
            try {
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
            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
            }
            return false;
        }

        public static ObservableList<Ingredient> updateIngredientList(ObservableList<Ingredient> curList) {
            try {
                ResultSet res = getQuerySet("SELECT * FROM ingredient");
                while (res.next()) {
                    boolean dupl = false;
                    for (Ingredient ing : curList) {
                        if (ing.getName().equals(res.getString("ingredient_Name"))) {
                            dupl = true;
                        }
                    }
                    Ingredient cur = new Ingredient(res.getInt("ingredientID"), res.getString("ingredient_Name"), res.getInt("calories_Count"), res.getInt("is_dairy"));
                    if (!dupl) {
                        curList.add(cur);
                    }
                }
                return curList;
            } catch (SQLException ex) {
                System.err.println("SQL Exception: " + ex.getMessage());
            }
            return null;
        }

        public static ObservableList<Recipe> updateRecipeList(ObservableList<Recipe> curList) {
            try {
                ResultSet res = getQuerySet("SELECT * FROM recipe");
                while (res.next()) {
                    boolean dupl = false;
                    for (Recipe recipe : curList) {
                        if (recipe.getName().equals(res.getString("recipe_Name"))) {
                            dupl = true;
                        }
                    }
                    Recipe cur = new Recipe(res.getInt("recipeID"),
                            res.getString("recipe_Name"), res.getString("recipe_Description"),
                            res.getString("recipe_Ingredients"), res.getInt("recipe_chefID"),
                            res.getInt("recipe_numCooked"), res.getInt("recipe_Rating"),
                            res.getInt("recipe_Calorie")
                    );
                    if (!dupl) {
                        curList.add(cur);
                    }
                }
                return curList;
            } catch (SQLException ex) {
                System.err.println("SQL Exception: " + ex.getMessage());
            }
            return null;
        }

        public static boolean changeUserName(String currentUserName, String newUserName) {

            try {
                pStatement = dbConnection.prepareStatement("UPDATE recipe_users SET username = ? "
                        + "WHERE username = ?");
                pStatement.setString(1, newUserName);
                pStatement.setString(2, currentUserName);

                // execute insert SQL stetement
                boolean currentUserExists = checkDuplicate("recipe_users", "username", currentUserName);
                if (!currentUserExists) {
                    return false;
                }

                boolean newUserExists = checkDuplicate("recipe_users", "username", newUserName);
                if (!newUserExists) {
                    pStatement.execute();
                    return true;
                }
            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
            }
            return false;

        }

        public static boolean changeUserPassword(String currentUserName, String newPassWord) {
            try {
                pStatement = dbConnection.prepareStatement("UPDATE recipe_users SET password = ? "
                        + "WHERE username = ?");
                pStatement.setString(1, newPassWord);
                pStatement.setString(2, currentUserName);

                // execute insert SQL stetement
                boolean currentUserExists = checkDuplicate("recipe_users", "username", currentUserName);
                if (currentUserExists) {
                    pStatement.execute();
                    return true;
                }
            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
            }
            return false;
        }

    }

}
