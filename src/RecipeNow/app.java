/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RecipeNow;

import java.sql.SQLException;

public class app {
    public static NewUserCntl start;
     public static void main(String[] args) throws SQLException {
        

        
        //DatabaseHelper db = new DatabaseHelper();
        
        //This would erase the db and create new table
        //db.createUserTable();
        //db.createIngredientTable();
        //Testing
        //db.insertIntoTable("TestUser2","testPass");
        //db.printTable();
        //db.closeConnection();
        
        /*
        username: test1
        password: test1
        */
        // Start login page
        start = new NewUserCntl();
        
    }
    

}
