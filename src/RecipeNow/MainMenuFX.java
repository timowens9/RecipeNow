/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RecipeNow;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Youngmin
 */
public class MainMenuFX extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainMenuFXML.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        stage.setTitle("Main Menu");
        stage.setScene(new Scene(root1));  
        stage.show();
        
    }

    /**
     * @param args the command line arguments
     */
    public void start() throws IOException {
        // Launch Main Menu page
        launch();
    }

    
}
