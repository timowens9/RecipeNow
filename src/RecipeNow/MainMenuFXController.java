/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RecipeNow;

import RecipeNow.app.DatabaseHelper;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.scene.Node;

/**
 * FXML Controller class
 *
 * @author Youngmin
 */
public class MainMenuFXController implements Initializable, GuiHelper {
       
    @FXML
    private Button mainMenu_ingButton;
    @FXML
    private Button mainMenu_recipeButton;
    @FXML
    private MenuItem mainMenu_logoutMenuItem;
    @FXML
    private AnchorPane mainMenu;
    @FXML
    private Button mainMenu_localRecipeButton;
    @FXML
    private Button mainMenu_changeUserName;
    @FXML
    private Button mainMenu_changePassWord;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    

    @FXML
    // Ingredient Menu 
    private void goToIngredientMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("IngredientMenuFXML.fxml"));
        AnchorPane ingMenu = (AnchorPane) loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(ingMenu));
        stage.setTitle("RecipeNow - Ingredient Menu");
        stage.show();
    }
    
    @FXML
    // Recipe Menu
    private void goToRecipeMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RecipeMenuFXML.fxml"));
        AnchorPane recipeMenu = (AnchorPane) loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(recipeMenu));
        stage.setTitle("RecipeNow - Recipe Menu");
        stage.show();
    }
    
    @FXML
    // Local Recipe Menu
    private void goToLocalRecipeMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RecipeLocalMenuFXML.fxml"));
        AnchorPane recipeMenu = (AnchorPane) loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(recipeMenu));
        stage.setTitle("RecipeNow - Local Recipe Menu");
        stage.show();
    }
    
    @FXML 
    private void logout(ActionEvent event) throws IOException {
        app.base.setScene(app.login);
        app.base.setTitle("RecipeNow - Login");
    }

    @FXML
    private void changeUsername(ActionEvent event) {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Change Username");
        dialog.setHeaderText("Enter a new Username");
        
        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Change", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
        
        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField username = new TextField();
        username.setText(app.loginCntl.getUsername());
        username.setEditable(false);
        TextField username2 = new TextField();
        username2.setPromptText("New Username");
        
        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("New Username:"), 0, 1);
        grid.add(username2, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);
        
        // Do some validation (using the Java 8 lambda syntax).
        username2.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });
        
        dialog.getDialogPane().setContent(grid);
        
        // Request focus on the username field by default.
        Platform.runLater(() -> username2.requestFocus());
        
        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), username2.getText());
        }
        return null;
        });
        
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(usernamePassword -> {
            if (usernamePassword.getKey().equals(usernamePassword.getValue())) {
                new Alert(Alert.AlertType.ERROR, "The entered new username is same as the current username").showAndWait();
            } else {
                // Do username update
                boolean updateResult = DatabaseHelper.changeUserName(usernamePassword.getKey(), usernamePassword.getValue());
                if(updateResult) {
                    new Alert(Alert.AlertType.INFORMATION, "Username change successful").showAndWait();
                    app.loginCntl.setUsername(usernamePassword.getValue());
                } else {
                    new Alert(Alert.AlertType.ERROR, "Username change failed").showAndWait();
                }
            }
        });
    }

    @FXML
    private void changePassword(ActionEvent event) {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Change Password");
        dialog.setHeaderText("Enter a new Password");
        
        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Change", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
        
        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField username = new TextField();
        username.setText(app.loginCntl.getUsername());
        username.setEditable(false);
        TextField password = new TextField();
        password.setPromptText("New Password");
        
        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("New Password:"), 0, 1);
        grid.add(password, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);
        
        // Do some validation (using the Java 8 lambda syntax).
        password.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty() || password.getText().isEmpty());
        });
        
        dialog.getDialogPane().setContent(grid);
        
        // Request focus on the username field by default.
        Platform.runLater(() -> password.requestFocus());
        
        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter((ButtonType dialogButton) -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });
        
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(usernamePassword -> {
                // Do username update
                boolean updateResult = DatabaseHelper.changeUserPassword(usernamePassword.getKey(), usernamePassword.getValue());
                if(updateResult) {
                    new Alert(Alert.AlertType.INFORMATION, "Password change successful").showAndWait();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Password change failed").showAndWait();
                }
            }
        );
    }


    @Override
    public void resetComponent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }




    
}
