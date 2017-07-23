/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RecipeNow;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Youngmin
 */
public class MainMenuFXController implements Initializable {
    
    private DatabaseHelper db;
    
    @FXML
    private Button mainMenu_ingButton;
    @FXML
    private Button mainMenu_recipeButton;
    @FXML
    private MenuItem mainMenu_logoutMenuItem;
    @FXML
    private Button mainMenu_printRecipeListButton;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = app.db;
    }    
    

    @FXML
    // Ingredient Menu 
    private void mainMenu_addIngAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("IngredientMenuFXML.fxml"));
        AnchorPane ingMenu = (AnchorPane) loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(ingMenu));
        stage.setTitle("RecipeNow - Ingredient Menu");
        stage.show();
    }
    
    @FXML
    // Recipe Menu
    private void mainMenu_addRecipeAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RecipeMenuFXML.fxml"));
        AnchorPane recipeMenu = (AnchorPane) loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(recipeMenu));
        stage.setTitle("RecipeNow - Recipe Menu");
        stage.show();
    }
    
    @FXML 
    private void mainMenu_logoutAction(ActionEvent event) throws IOException {
        app.base.setScene(app.login);
        app.base.setTitle("RecipeNow - Login");
    }
    private void stop(){
        db.closeConnection();
    }
    
}
