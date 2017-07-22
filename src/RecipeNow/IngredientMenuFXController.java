/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RecipeNow;

import java.net.URL;
import java.sql.SQLException;
import java.util.Comparator;
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
 * @author Youngmin
 */
public class IngredientMenuFXController implements Initializable, GuiHelper {


    @FXML
    private Button newIng_deleteIng;
    @FXML
    private ListView newIng_ingredientList;
    
    private DatabaseHelper db;
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
        db = app.db;
        ingredientList = db.updateIngredientList(ingredientList);
        newIng_ingredientList.setItems(ingredientList.sorted((Ingredient i1, Ingredient i2) -> i1.getName().compareToIgnoreCase(i2.getName())));
    }    
    
    @FXML
    private void newIng_addIngActionPerformed(ActionEvent event) {
        //Custom Add Ingredient dialog box using http://code.makery.ch/blog/javafx-dialogs-official/ tutorial.
        
        Dialog<Ingredient> addIng = new Dialog<>();
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
        
        addIngGrid.add(new Label("Name"), 0, 0);
        addIngGrid.add(ingName, 1, 0);
        addIngGrid.add(new Label("Calories"), 0, 1);
        addIngGrid.add(ingCalories, 1, 1);
        addIngGrid.add(ingDairy, 0, 2);
        
        Node okButton = addIng.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        
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
                return new Ingredient(0, ingName.getText(), Integer.parseInt(ingCalories.getText()), ingDairy.isSelected());
            }
            return null;
        });
        
        Optional<Ingredient> result = addIng.showAndWait();
        
        result.ifPresent(ingredient -> {
            db.ingredientInsertIntoTable(ingredient);
            resetComponent();
        });
    }

    @FXML
    private void newIng_editIngActionPerformed(ActionEvent event) {
        int curInd = newIng_ingredientList.getSelectionModel().getSelectedIndex();
        Ingredient ingredient = (Ingredient)newIng_ingredientList.getSelectionModel().getSelectedItem();
        if (curInd >= 0){
        //Custom Edit Ingredient dialog box using http://code.makery.ch/blog/javafx-dialogs-official/ tutorial.
        
        Dialog<Ingredient> addIng = new Dialog<>();
        addIng.setTitle("Edit Ingredient");
        addIng.setHeaderText("Edit an ingredient in the database");
        addIng.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        GridPane addIngGrid = new GridPane();
        addIngGrid.setHgap(10);
        addIngGrid.setVgap(15);
        addIngGrid.setPadding(new Insets(20,100,10,15));
        
        TextField ingName = new TextField(ingredient.getName());
        ingName.setPromptText("Name");
        TextField ingCalories = new TextField(Integer.toString(ingredient.getCalories()));
        ingCalories.setPromptText("Calories");
        
        CheckBox ingDairy = new CheckBox("Dairy");
        ingDairy.setSelected(ingredient.isDairy());
        
        addIngGrid.add(new Label("Name"), 0, 0);
        addIngGrid.add(ingName, 1, 0);
        addIngGrid.add(new Label("Calories"), 0, 1);
        addIngGrid.add(ingCalories, 1, 1);
        addIngGrid.add(ingDairy, 0, 2);
        
        Node okButton = addIng.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        
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
                ingredient.setName(ingName.getText());
                ingredient.setDairy(ingDairy.isSelected());
                ingredient.setCalories(Integer.parseInt(ingCalories.getText()));
                return ingredient;
            }
            return null;
        });
        
        Optional<Ingredient> result = addIng.showAndWait();
        
        result.ifPresent(editIngredient -> {
            db.ingredientEditIntoTable(editIngredient);
            resetComponent();
        });
        }
    }
    @FXML
    private void newIng_deleteIngActionPerformed(ActionEvent event) {
        int curInd = newIng_ingredientList.getSelectionModel().getSelectedIndex();
        if (curInd >= 0){
            Ingredient curIngredient = (Ingredient) newIng_ingredientList.getSelectionModel().getSelectedItem();
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to delete the ingredient: " + curIngredient.getName());
            confirmation.setTitle("Delete Ingredient");
            confirmation.setHeaderText("Delete Ingredient?");
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK){
                    db.ingredientDeleteIntoTable(curIngredient);
                    resetComponent();
                }
            });
        }
    }

    @Override
    public boolean checkNull() {
        return false;
    }

    @Override
    public void resetComponent() {
        ingredientList = db.updateIngredientList(ingredientList);
        newIng_ingredientList.setItems(ingredientList.sorted((Ingredient i1, Ingredient i2) -> i1.getName().compareToIgnoreCase(i2.getName())));
    }

    @Override
    public void closeFrame() {
    }
    
}
