/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RecipeNow;

import RecipeNow.app.DatabaseHelper;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author Youngmin, Eric
 */
public class IngredientMenuFXController implements Initializable, GuiHelper {


    @FXML
    private Button newIng_deleteIng;
    @FXML
    private ListView newIng_ingredientList;
    
    @FXML
    private Button newIng_addIng;
    @FXML
    private Button newIng_editIng;
    @FXML
    private AnchorPane IngMenu;
    
    private ObservableList<Ingredient> ingredientList = FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ingredientList = DatabaseHelper.updateIngredientList(ingredientList);
        newIng_ingredientList.setItems(ingredientList.sorted((Ingredient i1, Ingredient i2) -> i1.getName().compareToIgnoreCase(i2.getName())));
    }    
    
    @FXML
    private void addIngredient(ActionEvent event) {
        //Custom Add Ingredient dialog box using http://code.makery.ch/blog/javafx-dialogs-official/ tutorial.
        showDialog(false);
        
    }

    @FXML
    private void editIngredient(ActionEvent event) {
        int curInd = newIng_ingredientList.getSelectionModel().getSelectedIndex();
        if (curInd >= 0){
            showDialog(true);
        }
    }
    
    @FXML
    private void deleteIngredient(ActionEvent event) {
        int curInd = newIng_ingredientList.getSelectionModel().getSelectedIndex();
        if (curInd >= 0){
            Ingredient curIngredient = (Ingredient) newIng_ingredientList.getSelectionModel().getSelectedItem();
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to delete the ingredient: " + curIngredient.getName());
            confirmation.setTitle("Delete Ingredient");
            confirmation.setHeaderText("Delete Ingredient?");
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK){
                    DatabaseHelper.ingredientDeleteIntoTable(curIngredient);
                    ingredientList.remove(curIngredient);
                    resetComponent();
                }
            });
        }
        else{
            Alert noSelect = new Alert(Alert.AlertType.ERROR, "You must select an ingredient to delete");
            noSelect.setTitle("No Selection");
            noSelect.setHeaderText("No Ingredient Selected");
        }
    }


    @Override
    public void resetComponent() {
        ingredientList = DatabaseHelper.updateIngredientList(ingredientList);
        newIng_ingredientList.setItems(ingredientList.sorted((Ingredient i1, Ingredient i2) -> i1.getName().compareToIgnoreCase(i2.getName())));
    }

    public void showDialog(boolean edit){
        Dialog<Ingredient> addIng = new Dialog<>();
        Ingredient ingredient = (Ingredient)newIng_ingredientList.getSelectionModel().getSelectedItem();
        
        addIng.setTitle("Add Ingredient");
        addIng.setHeaderText("Create a new ingredient in the database");
        addIng.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        GridPane addIngGrid = new GridPane();
        addIngGrid.setHgap(10);
        addIngGrid.setVgap(15);
        addIngGrid.setPadding(new Insets(20,100,10,15));
        
        TextField ingName = new TextField();
        ingName.setPromptText("Name");
        TextField ingCalories = new TextField();
        ingCalories.setPromptText("Calories");
        
        CheckBox ingDairy = new CheckBox("Dairy");
        
        if(edit){
            ingName.setText(ingredient.getName());
            ingCalories.setText(Integer.toString(ingredient.getCalories()));
            ingDairy.setSelected(ingredient.isDairy());
        }
        addIngGrid.add(new Label("Name"), 0, 0);
        addIngGrid.add(ingName, 1, 0);
        addIngGrid.add(new Label("Calories"), 0, 1);
        addIngGrid.add(ingCalories, 1, 1);
        addIngGrid.add(ingDairy, 0, 2);
        
        Node okButton = addIng.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(!edit);
        
        ingName.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty() || ingCalories.getText().trim().isEmpty());
        });
        
        ingCalories.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                ingCalories.setText(newValue.replaceAll("[^\\d]", ""));
            }
            okButton.setDisable(newValue.trim().isEmpty() || ingName.getText().trim().isEmpty());
        });
        
        addIng.getDialogPane().setContent(addIngGrid);
        Platform.runLater(() -> ingName.requestFocus());
        
        addIng.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK){
                if (edit){
                    ingredient.setName(ingName.getText());
                    ingredient.setDairy(ingDairy.isSelected());
                    ingredient.setCalories(Integer.parseInt(ingCalories.getText()));
                    return ingredient;
                }
                else
                    return new Ingredient(0, ingName.getText(), Integer.parseInt(ingCalories.getText()), ingDairy.isSelected());
            }
            return null;
        });
        
        Optional<Ingredient> result = addIng.showAndWait();
        
        result.ifPresent(tempIngredient -> {
            if (edit)
                DatabaseHelper.ingredientEditIntoTable(tempIngredient);
            else
                DatabaseHelper.ingredientInsertIntoTable(tempIngredient);
            resetComponent();
        });
    }
}
