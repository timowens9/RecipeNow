/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RecipeNow;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;

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
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = app.db;
        
    }    
    
    @FXML
    private void newIng_deleteIngActionPerformed(ActionEvent event) {
        // Check if name and calorie text fields are filled 
        boolean checkNull = newIng_ingName.getText().equals("Enter Ingredient Name") || newIng_ingName.getText().isEmpty();
        boolean deleteSuccess = false;
        if(!checkNull) {
            try {
                deleteSuccess = db.ingredientDeleteIntoTable(newIng_ingName.getText());
            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex.getMessage());
            }
            
            if(deleteSuccess) {
                System.out.println("Delete Ingredient Failed");
                new Alert(Alert.AlertType.ERROR, "Delete Ingredient Failed").showAndWait();
                resetComponent();
            } else {
                System.out.println("Delete Ingredient Success " + " Ingredient Name:" + newIng_ingName.getText());
                new Alert(Alert.AlertType.INFORMATION, "Delete Ingredient Success").showAndWait();
                resetComponent();
            }
            
        }
        resetComponent();
    }

    @FXML
    private void newIng_addIngActionPerformed(ActionEvent event) {
        // Check if name and calorie text fields are filled 
        boolean hasDuplicate;
        int dairy = newIng_isDairy.isSelected()?1:0;
        
        if(!checkNull()) {
            String ingredName = newIng_ingName.getText();
            int ingredCal;
            try {
            ingredCal = Integer.parseInt(newIng_ingCal.getText());
            hasDuplicate = db.ingredientInsertIntoTable(ingredName, ingredCal, dairy);
                if(hasDuplicate) {
                    System.out.println("Add Ingredient Failed");
                    new Alert(Alert.AlertType.ERROR, "The ingredient is already in the database or Server Connection has failed").showAndWait();
                } else {
                    System.out.println("Add Ingredient Success " + " Ingredient Name: " + newIng_ingName.getText() + " Ingredient Calories: " + newIng_ingCal.getText());
                    new Alert(Alert.AlertType.INFORMATION, "Add Ingredient Success").showAndWait();
                }
            } catch (NumberFormatException ex) {
                
                System.out.println("NumberFormatException: " + ex.getMessage());
                new Alert(Alert.AlertType.ERROR, "Please only enter numeric values").showAndWait();
                resetComponent();
                
                // exit function
                return;
            }
            catch (SQLException e){
                System.err.println("SQLError: " + e.getMessage());
            }
        }
        resetComponent();
        
    }

    @FXML
    private void newIng_editIngActionPerformed(ActionEvent event) {
        // Check if name and calorie text fields are filled 
        boolean hasDuplicate = false;
        int dairy = newIng_isDairy.isSelected() == true ? 1 : 0;
        String ingredName = newIng_ingName.getText();
        int ingredCal;
        //System.out.println(checkNull());
        if(!checkNull()) {
            try {
                ingredCal = Integer.parseInt(newIng_ingCal.getText());
            } catch (NumberFormatException ex) {
                System.out.println("NumberFormatException: " + ex.getMessage());
                new Alert(Alert.AlertType.ERROR, "Please only enter numeric values").showAndWait();
                resetComponent();
                // exit function
                return; 
            }
            
            try {
                hasDuplicate = db.ingredientEditIntoTable(ingredName, ingredCal, dairy);
            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex.getMessage());
            }
            if(hasDuplicate) {
                System.out.println("Edit Ingredient Failed");
                new Alert(Alert.AlertType.ERROR, "The ingredient is already in the database or Server Connection has failed").showAndWait();
            } else {
                System.out.println("Edit Ingredient Success" + " Ingredient Name: " + newIng_ingName.getText() + " Ingredient Calories " + newIng_ingCal.getText());
                new Alert(Alert.AlertType.INFORMATION, "Edit Ingredient Success").showAndWait();
            } 
        }
        resetComponent();
    }

    @Override
    public boolean checkNull() {
        return (newIng_ingCal.getText().equals("") || newIng_ingName.getText().equals(""));
    }

    @Override
    public void resetComponent() {
        newIng_ingCal.setText("");
        newIng_ingName.setText("");
    }

    @Override
    public void closeFrame() {
    }
    
}
