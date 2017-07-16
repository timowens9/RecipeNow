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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
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
    private Button mainMenu_printIngList;
    @FXML
    private AnchorPane mainMenu_addIng;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.db = NewUserViewFXController.db;
    }    

    @FXML
    private void mainMenu_printIngListMouseClicked(MouseEvent event) {
        try {
            db.printIngredientTable();
        } catch (SQLException ex) {
            System.out.println("SQL Exception: " + ex.getMessage());
        }
    }

    @FXML
    private void mainMenu_addIngMouseClicked(MouseEvent event) throws IOException {
        IngredientMenuFX ing = new IngredientMenuFX();
        ing.start(new Stage());
    }
    
}
