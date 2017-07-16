/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RecipeNow;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Youngmin
 */
public class NewUserViewFX extends Application {
    
    public static Stage login;
    
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("NewUserViewFXML.fxml"));
        Scene scene = new Scene(root);
        login = stage;
        login.setTitle("Login");
        login.setScene(scene);
        login.show();
    }

    /**
     * @param args the command line arguments
     */
    public void start() {
        // Launch login page
        launch();
    }
    
    
}
