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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Youngmin
 */
public class IngredientMenuFXController implements Initializable, GuiHelper {

    @FXML
    private TextField newIng_ingName;
    @FXML
    private TextField newIng_ingCal;
    @FXML
    private CheckBox newIng_isDairy;
    @FXML
    private Button newIng_deleteIng;

    private DatabaseHelper db;
    @FXML
    private Button newIng_addIng;
    @FXML
    private Button newIng_editIng;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.db = NewUserViewFXController.db;
    }    

    @FXML
    private void newIng_ingNameMouseClicked(MouseEvent event) {
        // Empty text field when user clicks 
        if(newIng_ingName.getText().equals("Enter Ingredient Name") || newIng_ingName.getText().isEmpty()) {
            newIng_ingName.setText(""); 
        }
    }

    @FXML
    private void newIng_ingCalMouseClicked(MouseEvent event) {
        // Empty text field when user clicks 
        if(newIng_ingCal.getText().equals("Enter Ingredient Calorie") || newIng_ingCal.getText().isEmpty()) {
            newIng_ingCal.setText(""); 
        }  
    }

    @Override
    public boolean checkNull() {
       // Check if the two textfields are null or default    
        return newIng_ingName.getText().equals("") || newIng_ingCal.getText().equals("");
    }

    @Override
    public void resetComponent() {
        newIng_ingName.setText("Enter Ingredient Name");
        newIng_ingCal.setText("Enter Ingredient Calorie");
        
    }

    @Override
    public void closeFrame() {
        // to be updated
    }

    @FXML
    private void newIng_deleteIngActionPerformed(MouseEvent event) {
        // Check if name and calorie text fields are filled 
        boolean checkNull = newIng_ingName.getText().equals("Enter Ingredient Name") || newIng_ingName.getText().isEmpty();
        boolean deleteSuccess = false;
        if(!checkNull) {
            try {
                deleteSuccess = this.db.ingredientDeleteIntoTable(newIng_ingName.getText());
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
    private void newIng_addIngActionPerformed(MouseEvent event) {
        // Check if name and calorie text fields are filled 
        boolean checkNull = checkNull();
        boolean hasDuplicate;
        int dairy = newIng_isDairy.isSelected() == true ? 1 : 0;
        
        if(!checkNull) {
            String ingredName = newIng_ingName.getText();
            int ingredCal;
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
                hasDuplicate = this.db.ingredientInsertIntoTable(ingredName, ingredCal, dairy);
                if(hasDuplicate) {
                    System.out.println("Add Ingredient Failed");
                    new Alert(Alert.AlertType.ERROR, "The ingredient is already in the database or Server Connection has failed").showAndWait();
                } else {
                    System.out.println("Add Ingredient Success " + " Ingredient Name: " + newIng_ingName.getText() + " Ingredient Calories: " + newIng_ingCal.getText());
                    new Alert(Alert.AlertType.INFORMATION, "Add Ingredient Success").showAndWait();
                }
            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex.getMessage());
            } 
        }
        resetComponent();
        
    }

    @FXML
    private void newIng_editIngActionPerformed(MouseEvent event) {
        // Check if name and calorie text fields are filled 
        boolean checkNull = checkNull();
        boolean hasDuplicate = false;
        int dairy = newIng_isDairy.isSelected() == true ? 1 : 0;
        String ingredName = newIng_ingName.getText();
        int ingredCal;
        //System.out.println(checkNull());
        if(!checkNull) {
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
                hasDuplicate = this.db.ingredientEditIntoTable(ingredName, ingredCal, dairy);
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
    
}
