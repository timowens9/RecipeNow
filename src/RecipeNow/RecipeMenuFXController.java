/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RecipeNow;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Youngmin
 */
public class RecipeMenuFXController implements Initializable, GuiHelper {
    
    
    private DatabaseHelper db;
    @FXML
    private TextField newRecipe_recipeName;
    @FXML
    private Button newRecipe_addRecipe;
    @FXML
    private TextField newRecipe_recipeIngredients;
    @FXML
    private TextField newRecipe_recipeRating;
    @FXML
    private TextField newRecipe_recipeChefId;
    @FXML
    private Button newRecipe_deleteRecipe;
    @FXML
    private TextArea newRecipe_recipeInstruction;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = app.db;
    }

    @FXML
    private void newRecipe_addRecipeActionPerformed(ActionEvent event) throws SQLException {
        
        // Check if name and calorie text fields are filled 
        boolean hasDuplicate;
        
        if(!checkNull()) {
            String recipeName = newRecipe_recipeName.getText();
            String recipeDescription = newRecipe_recipeInstruction.getText();
            String recipeIngredientIds = "";
            int recipeRating = Integer.parseInt(newRecipe_recipeRating.getText());
            int chefId = Integer.parseInt(newRecipe_recipeChefId.getText());
            String[] recipeSplit = newRecipe_recipeIngredients.getText().split(",");
            int totalCalorie = 0;

            boolean ingCheck = true;
            String[] ingredientIds = new String[recipeSplit.length];
            for(int i = 0; i < recipeSplit.length; i++) {
                String query = "SELECT * FROM ingredient "
                + "WHERE ingredient_name = " + "'" + recipeSplit[i] + "';";
                ResultSet resultSet = this.db.getQuerySet(query);
                if (!resultSet.first()) {
                    new Alert(Alert.AlertType.ERROR, "One or more Ingredient is not in the database").showAndWait();
                    ingCheck = false;
                } else {
                    ingredientIds[i] = Integer.toString(resultSet.getInt("ingredientID"));
                    totalCalorie += resultSet.getInt("calories_Count");
                }
            }
            if(ingCheck) {
                List<String> strs = Arrays.asList(ingredientIds);
                recipeIngredientIds = String.join(",", strs);
            }
            System.out.println("Ingredients to be added to recipe: " + recipeIngredientIds);
                try {
                    hasDuplicate = db.recipeInsertIntoTable(recipeName, recipeDescription, recipeIngredientIds, recipeRating, chefId, totalCalorie);
                    if(hasDuplicate) {
                        System.out.println("Add Recipe Failed");
                        new Alert(Alert.AlertType.ERROR, "The Recipe is already in the database or Server Connection has failed").showAndWait();
                    } else {
                        System.out.println("Add Recipe Success " + " Recipe Name: " + newRecipe_recipeName.getText());
                        new Alert(Alert.AlertType.INFORMATION, "Add Ingredient Success").showAndWait();
                }
                } catch (NumberFormatException ex) {
                    System.out.println("NumberFormatException: " + ex.getMessage());
                    new Alert(Alert.AlertType.ERROR, "Please only enter numeric values for Chef ID, and Rating").showAndWait();
                    resetComponent();
                    // exit function
                    return;
                }
            }
        resetComponent();
    }
    
    @FXML
    private void newRecipe_deleteRecipeActionPerformed(ActionEvent event) {
        // Check if name and calorie text fields are filled 
        boolean checkNull = newRecipe_recipeName.getText().equals("Enter Recipe Name") || newRecipe_recipeName.getText().isEmpty();
        boolean deleteSuccess = false;
        if(!checkNull) {
            try {
                deleteSuccess = db.recipeDeleteIntoTable(newRecipe_recipeName.getText());
            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex.getMessage());
            }
            
            if(deleteSuccess) {
                System.out.println("Delete Recipe Failed");
                new Alert(Alert.AlertType.ERROR, "Delete Recipe Failed").showAndWait();
                resetComponent();
            } else {
                System.out.println("Delete Recipe Success " + " Recipe Name:" + newRecipe_recipeName.getText());
                new Alert(Alert.AlertType.INFORMATION, "Delete Recipe Success").showAndWait();
                resetComponent();
            }
            
        }
        resetComponent();
    }

    @Override
    public boolean checkNull() {
        return (newRecipe_recipeName.getText().equals("") || 
                newRecipe_recipeInstruction.getText().equals("") ||
                newRecipe_recipeIngredients.getText().equals("") ||
                newRecipe_recipeRating.getText().equals("") ||
                newRecipe_recipeChefId.getText().equals("")
                );
        
    }

    @Override
    public void resetComponent() {
        newRecipe_recipeName.setText("Enter Recipe Name");
        newRecipe_recipeInstruction.setText("Enter Description");
        newRecipe_recipeIngredients.setText("Enter Ingredient names ex) Apple,Orange,Banana");
        newRecipe_recipeRating.setText("Enter Rating ex) 0~5");
        newRecipe_recipeChefId.setText("Enter Chef ID");
    }

    @Override
    public void closeFrame() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

    
}
