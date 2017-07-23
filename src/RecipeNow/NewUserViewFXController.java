/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RecipeNow;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Youngmin
 */
public class NewUserViewFXController  implements Initializable, GuiHelper {

    /**
     * @return the userid
     */
    public int getUserid() {
        return userid;
    }

    /**
     * @param userid the userid to set
     */
    public void setUserid(int userid) {
        this.userid = userid;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    
    @FXML
    private Button loginPage_printAccounts;
    @FXML
    private TextField loginPage_userName;
    @FXML
    private PasswordField loginPage_passWord;
    @FXML
    private Button loginPage_Login;
    
    
    public static DatabaseHelper db;
    private boolean isAuthenticated = false;
    private String username;
    private int userid;

    @FXML
    private Button loginPage_Delete;
    @FXML
    private Button loginPage_registration;
    @FXML
    private AnchorPane login_AnchorPane;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = app.db;
    }
    
    
    @FXML
    private void loginPage_printAccounts(ActionEvent event) {
        db.printUserTable();
    }

    @FXML
    private void loginPage_DeleteActionPerformed(ActionEvent event) {
        // Check if login and pw text fields are filled 
        boolean checkNull = checkNull();

        String userName = loginPage_userName.getText();
        String userPw = String.valueOf(loginPage_passWord.getText());

        if (!checkNull) {
            String query = "SELECT * FROM recipe_users WHERE username = '" + userName + "'";
            ResultSet res;
            try {
                res = db.getQuerySet(query);
                if (res.first()) {
                    if (res.getString("password").trim().equals(userPw)) {
                        int userID = res.getInt("userID");
                        //JOptionPane.showMessageDialog(rootPane, "You may delete this account now \nUserid: " + userID
                               // + "\nUsername: " + userName, "Delete Account?", HEIGHT);
                        
                        new Alert(Alert.AlertType.INFORMATION, "You may delete this account now \nUserid: " + userID + 
                                "\nUsername:" + userName).showAndWait();
                        boolean deleteSuccess = db.deleteAccount(userName);
                        if (deleteSuccess) {
                            new Alert(Alert.AlertType.INFORMATION, "Account deleted\nUserid: " + userID
                                + "\nUsername: " + userName).showAndWait();
                        } else {
                            //JOptionPane.showMessageDialog(rootPane, "Account was not deleted\nUser ID: " + userID, "Error", HEIGHT);
                            new Alert(Alert.AlertType.ERROR, "Account was not deleted\nUser ID: " + userID).showAndWait();
                        }
                        resetComponent();
                    } else {
                        //JOptionPane.showMessageDialog(rootPane, "The password provided was incorrect", "Incorrect Password", HEIGHT);
                        new Alert(Alert.AlertType.ERROR, "The password provided was incorrect").showAndWait();
                    }
                } else {
                    //JOptionPane.showMessageDialog(rootPane, "No account with that username exists", "No account", HEIGHT);
                    new Alert(Alert.AlertType.ERROR, "No account with that username exists").showAndWait();
                }
            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex.getMessage());
            }
        }
    }


    @FXML
    private void loginPage_registrationActionPerformed(ActionEvent event) {
         // Check if login and pw text fields are filled 
        boolean checkNull = checkNull();
        boolean hasDuplicate;
        if (!checkNull) {

            // Registration code by Tim
            String userName = loginPage_userName.getText();
            String passWord = loginPage_passWord.getText();
            //System.out.println(password);
            //String IQuery = "INSERT INTO `recipe_users`(`username`,`password`) VALUES('" + username + "', '" + password + "')";
            //System.out.println(IQuery);//print on console
            hasDuplicate = db.userInsertIntoTable(userName, passWord);
            if (hasDuplicate) {
                System.out.println("Registration failed");
                new Alert(Alert.AlertType.ERROR, "The username is already taken or Server Connection has failed").showAndWait();
                resetComponent();
            } else {
                System.out.println("Registration Success " + " Username: " + userName + " Password: " + passWord);
                new Alert(Alert.AlertType.INFORMATION, "Registration Success").showAndWait();
                //loginPage_LoginActionPerformed(evt);
                resetComponent();
            }
            db.printUserTable();
        }
    }
    @FXML
    private void loginPage_LoginActionPerformed(ActionEvent event) throws IOException {
        // Check if login and pw text fields are filled 
        boolean checkNull = checkNull();
        if (!checkNull) {
            String userName = loginPage_userName.getText();
            String userPw = loginPage_passWord.getText();
            String query = ("SELECT * FROM recipe_users");
            ResultSet res;

            // Get data from mysql
            res = db.getQuerySet(query);

            // Run through resultset
            boolean auth = false;
            boolean user = false;

            try {
                while (res.next()) {
                    //System.out.println("UserId: " +  res.getString("userID") + " User: " + res.getString("username") + "; Password: " + res.getString("password"));
                    if (res.getString("username").trim().equals(userName)) {
                        user = true;
                        if (res.getString("password").trim().equals(userPw)) {
                            // Authenticated
                            this.isAuthenticated = true;
                            this.setUserid(res.getInt("userID"));
                            this.setUsername(userName);
                            Alert success = new Alert(Alert.AlertType.INFORMATION, "Login Success\nUserid:" + getUserid() + "\nUsername: " +getUsername());
                            success.setHeaderText("Login Successful");
                            success.showAndWait();
                            auth = true;
                            resetComponent();
                            // Go to Main Menu
                            goToMainMenu();
                            
                        }
                    }
                }
            } catch (SQLException ex) {
                System.out.println("Failed to get resultset from MySQL");
            }
            if (!user && !auth) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Account Not Found");
                alert.setContentText("That username does not exist, would you like to register?");
                Optional<ButtonType> register =  alert.showAndWait();
                if (register.get() == ButtonType.OK) {
                    loginPage_registrationActionPerformed(event);
                }
            } else if (!auth) {
                new Alert(Alert.AlertType.ERROR, "Authentication Failed, incorrect password").showAndWait();
                resetComponent();
            }

        } else {
            // Id or Pw text fields are null
            new Alert(Alert.AlertType.ERROR, "Username and/or Password field is emtpy").showAndWait();
        }
    }
    
    
    @Override
    public boolean checkNull() {
        // Check if the two textfields are null or default    
        return loginPage_userName.getText().equals("") || loginPage_passWord.getText().length() == 0;
    }

    @Override
    public void resetComponent() {
        loginPage_userName.setText("");
        loginPage_passWord.setText("");
    }

    @Override
    public void closeFrame() {
        db.closeConnection();
    }

    private void goToMainMenu() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenuFXML.fxml"));
        AnchorPane mainMenu = (AnchorPane) loader.load();
        Stage stage = app.base;
        stage.setScene(new Scene(mainMenu));
        stage.setTitle("RecipeNow - Main Menu");
        
    }

    
}
