/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RecipeNow;

import RecipeNow.app.DatabaseHelper;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

public class RecipeLocalMenuFXController implements Initializable, GuiHelper {

    @FXML
    private ListView recipe_recipeList;

    /**
     * Initializes the controller class.
     */
    
    private ObservableList<Recipe> recipeList = FXCollections.observableArrayList();
    
    private ObservableList<Ingredient> ingredientList = FXCollections.observableArrayList();
    
    private RecipeList recipeLocal;
    @FXML
    private Button recipe_viewRecipe;
        
    private HashMap<Integer, String> ingredientMap;
    @FXML
    private Button recipe_deleteRecipe;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        recipeLocal = new RecipeList();
        ingredientMap = new HashMap<>();
        ingredientList = DatabaseHelper.updateIngredientList(ingredientList);
        for(int i = 0; i < ingredientList.size(); i++) {
            ingredientMap.put(ingredientList.get(i).getID(), ingredientList.get(i).getName());
        }
        recipeList = recipeLocal.updateRecipeList();
        recipe_recipeList.setItems(recipeList.sorted((Recipe i1, Recipe i2) -> i1.getName().compareToIgnoreCase(i2.getName())));
    }    

    @FXML
    private void viewRecipe(ActionEvent event) { 
        int curInd = recipe_recipeList.getSelectionModel().getSelectedIndex();
        if (curInd >= 0) {
            Recipe curRecipe = (Recipe) recipe_recipeList.getSelectionModel().getSelectedItem();
            int[] temp = curRecipe.getIngredientsArray();
            String ingredients = "";
            for(int a : temp) {
                ingredients = ingredients.concat(ingredientMap.get(a) + ",");
            }
            ingredients = ingredients.substring(0, ingredients.length()-1);
            
            Alert viewRecipe = new Alert(Alert.AlertType.INFORMATION, curRecipe.getDescription() + 
                    "\n" + "Ingredients: " + ingredients + 
                    "\n" + "Calorie: " + curRecipe.getCalories() + 
                    "\n" + "Rating: "+ curRecipe.getRating());
            viewRecipe.setTitle("View Recipe");
            viewRecipe.setHeaderText(curRecipe.getName());
            viewRecipe.showAndWait();
            
        } else {
            Alert noSelect = new Alert(Alert.AlertType.ERROR, "You must select a recipe to view");
            noSelect.setTitle("No Selection");
            noSelect.setHeaderText("No Recipe Selected");
            noSelect.showAndWait();
        }
        
    }
    
    @FXML
    private void deleteRecipe(ActionEvent event) {
        int curInd = recipe_recipeList.getSelectionModel().getSelectedIndex();
        if (curInd >= 0) {
            Recipe curRecipe = (Recipe) recipe_recipeList.getSelectionModel().getSelectedItem();
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to delete the recipe: " + curRecipe.getName());
            confirmation.setTitle("Delete Recipe");
            confirmation.setHeaderText("Delete Recipe?");
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    recipeLocal.deleteRecipe(curRecipe);
                    recipeList = recipeLocal.updateRecipeList();
                    resetComponent();
                }
            });
        } else {
            Alert noSelect = new Alert(Alert.AlertType.ERROR, "You must select a recipe to view");
            noSelect.setTitle("No Selection");
            noSelect.setHeaderText("No Recipe Selected");
            noSelect.showAndWait();
        }
    }

    @Override
    public void resetComponent() {
        //recipe_recipeList = new ListView();
        recipe_recipeList.setItems(recipeList.sorted((Recipe i1, Recipe i2) -> i1.getName().compareToIgnoreCase(i2.getName())));
    }    
}
