/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RecipeNow;

import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class app extends Application{
    
    public static Stage base;
    public static Scene login;
    public static DatabaseHelper db;
    
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        db = new DatabaseHelper();
        base = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("NewUserViewFXML.fxml"));
        Parent root = loader.load();
        base.setTitle("RecipeNow - Login");
        login = new Scene(root);
        base.setScene(login);
        base.show();    
    }
    
     public static void main(String[] args)  {
         launch();
        
    }
    @Override
    public void stop(){
        try{
            db.closeConnection();
        }
        catch(SQLException e){
            System.err.println("Error: " + e.getMessage());
        }
    }



}
