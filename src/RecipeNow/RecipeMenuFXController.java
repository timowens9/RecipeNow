/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RecipeNow;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author Youngmin, Eric
 */
public class RecipeMenuFXController implements Initializable, GuiHelper {

    private DatabaseHelper db;
    @FXML
    private ListView recipe_recipeList;
    @FXML
    private Button recipe_addRecipe;
    @FXML
    private Button recipe_editRecipe;
    @FXML
    private Button recipe_deleteRecipe;

    private ObservableList<Recipe> recipeList = FXCollections.observableArrayList();

    private ObservableList<Ingredient> ingredientList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = app.db;
        recipeList = db.updateRecipeList(recipeList);
        recipe_recipeList.setItems(recipeList.sorted((Recipe i1, Recipe i2) -> i1.getName().compareToIgnoreCase(i2.getName())));
        ingredientList = db.updateIngredientList(ingredientList);
    }

    @FXML
    private void recipe_addRecipeActionPerformed(ActionEvent event) {   
        showDialog(false);
    }

    @FXML
    private void recipe_editRecipeActionPerformed(ActionEvent event) {
        if (recipe_recipeList.getSelectionModel().getSelectedIndex() >= 0) {
            showDialog(true);
        }
    }

    @FXML
    private void recipe_deleteRecipeActionPerformed(ActionEvent event) {

        int curInd = recipe_recipeList.getSelectionModel().getSelectedIndex();
        if (curInd >= 0) {
            Recipe curRecipe = (Recipe) recipe_recipeList.getSelectionModel().getSelectedItem();
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to delete the recipe: " + curRecipe.getName());
            confirmation.setTitle("Delete Recipe");
            confirmation.setHeaderText("Delete Recipe?");
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    db.recipeDeleteIntoTable(curRecipe);
                    recipeList.remove(curRecipe);
                    resetComponent();
                }
            });
        } else {
            Alert noSelect = new Alert(Alert.AlertType.ERROR, "You must select a recipe to delete");
            noSelect.setTitle("No Selection");
            noSelect.setHeaderText("No Recipe Selected");
        }
    }

    @Override
    public boolean checkNull() {
        return true;
    }

    @Override
    public void resetComponent() {
        recipeList = db.updateRecipeList(recipeList);
        recipe_recipeList.setItems(recipeList.sorted((Recipe i1, Recipe i2) -> i1.getName().compareToIgnoreCase(i2.getName())));
    }

    @Override
    public void closeFrame() {
    }

    
    private void showDialog(boolean edit){
        Dialog<Recipe> addRecipe = new Dialog<>();
        if(edit){
            addRecipe.setTitle("Edit Recipe");
            addRecipe.setHeaderText("Modify an exisiting recipe in the database");
        }
        else{
            addRecipe.setTitle("Add Recipe");
            addRecipe.setHeaderText("Create a new recipe in the database");
        }
        Recipe curRecipe = (Recipe) recipe_recipeList.getSelectionModel().getSelectedItem();

        //Compose GridPane for dialog
        GridPane addRecipeGrid = new GridPane();
        addRecipeGrid.setPrefSize(500, 900);
        addRecipeGrid.setHgap(10);
        addRecipeGrid.setVgap(15);
        addRecipeGrid.setPadding(new Insets(20, 100, 10, 15));
        //public Recipe(int ID, String name, String description, String ingredients, int chefID, int numCooked, int rating, int calories){
        
        TextField recipeName = new TextField();
        if(edit)
            recipeName.setText(curRecipe.getName());
        recipeName.setPromptText("Name");

        addRecipe.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextArea recipeDescription = new TextArea();
        if(edit)
            recipeDescription.setText(curRecipe.getDescription());
        recipeDescription.setPromptText("Description");
        recipeDescription.setMinHeight(250);
        recipeDescription.setMinWidth(400);
        recipeDescription.setPrefColumnCount(100);
        recipeDescription.setWrapText(true);

        ArrayList<ComboBox> recipeIngredients = new ArrayList<>();
        
        if (edit){
            for (int i : curRecipe.getIngredientsArray()) {
                ComboBox temp = new ComboBox();
                temp.setItems(ingredientList);
                temp.setPrefWidth(250);
                for (Ingredient cur : ingredientList) {
                    if (cur.getID() == i) {
                        temp.setValue(cur);
                    }
                }
                recipeIngredients.add(temp);
            }
        }
        else {
            recipeIngredients.add(new ComboBox());
            recipeIngredients.get(0).setItems(ingredientList);
            recipeIngredients.get(0).setPrefWidth(250);
        }

        Button addIngredient = new Button("Add Ingredient");
        Button removeIngredient = new Button("Remove Ingredient");

        addRecipeGrid.add(new Label("Name"), 0, 0);
        addRecipeGrid.add(recipeName, 1, 0);
        addRecipeGrid.add(new Label("Description"), 0, 1);
        addRecipeGrid.add(recipeDescription, 0, 2, 2, 1);
        addRecipeGrid.add(new Label("Ingredients"), 0, 3);
        
        int j = 0;
        for (ComboBox cur : recipeIngredients) {
            addRecipeGrid.add(cur, 0, 4 + j, 2, 1);
            j++;
        }
        addRecipeGrid.add(addIngredient, 3, 3 + j);
        if (recipeIngredients.size() > 1)
            addRecipeGrid.add(removeIngredient, 4, 3 + j);

        Node okButton = addRecipe.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(!edit);

        addIngredient.setOnAction((ActionEvent event1) -> {
            ComboBox temp = new ComboBox();
            temp.setItems(ingredientList);
            temp.setPrefWidth(250);
            recipeIngredients.add(temp);
            addRecipeGrid.add(recipeIngredients.get(recipeIngredients.size() - 1), 0, recipeIngredients.size() + 3, 2, 1);
            addRecipeGrid.getChildren().remove(addIngredient);
            addRecipeGrid.getChildren().remove(removeIngredient);
            addRecipeGrid.add(addIngredient, 3, recipeIngredients.size() + 3);
            if (recipeIngredients.size() > 1)
                addRecipeGrid.add(removeIngredient, 4, recipeIngredients.size() + 3);
            addRecipe.getDialogPane().getScene().getWindow().sizeToScene();
        });
        removeIngredient.setOnAction((ActionEvent event1) -> {
            if (recipeIngredients.size() > 1){
                ComboBox temp = recipeIngredients.get(recipeIngredients.size() - 1);
                addRecipeGrid.getChildren().remove(temp);
                addRecipeGrid.getChildren().remove(addIngredient);
                addRecipeGrid.getChildren().remove(removeIngredient);
                recipeIngredients.remove(temp);
                addRecipeGrid.add(addIngredient, 3, recipeIngredients.size() + 3);
                if (recipeIngredients.size() > 1)
                    addRecipeGrid.add(removeIngredient, 4, recipeIngredients.size() + 3);
                addRecipe.getDialogPane().getScene().getWindow().sizeToScene();
            }
        });
        recipeName.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty()
                    || recipeDescription.getText().trim().isEmpty());
        });

        recipeDescription.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty()
                    || recipeName.getText().trim().isEmpty());
        });
        
        addRecipe.getDialogPane().setContent(addRecipeGrid);
        
        
        Platform.runLater(() -> recipeName.requestFocus());

        addRecipe.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                int[] ingredients = new int[recipeIngredients.size()];
                int calories = 0;
                int i = 0;
                for (ComboBox cur : recipeIngredients) {
                    Ingredient temp = (Ingredient) cur.getValue();
                    ingredients[i] = temp.getID();
                    calories += temp.getCalories();
                    i++;

                }
                if(edit){
                    curRecipe.setName(recipeName.getText());
                    curRecipe.setDescription(recipeDescription.getText());
                    curRecipe.setIngredients(ingredients);
                    curRecipe.setCalories(calories);
                    return curRecipe;
                }
                else{
                    return new Recipe(0, recipeName.getText(), recipeDescription.getText(), ingredients, app.loginCntl.getUserid(), 0, 0, calories);
                }
            }
            return null;
        });

        Optional<Recipe> result = addRecipe.showAndWait();

        result.ifPresent(recipe -> {
            if (edit)
               db.recipeEditIntoTable(recipe);
            else
                db.recipeInsertIntoTable(recipe);
            resetComponent();
        });
    }
}
