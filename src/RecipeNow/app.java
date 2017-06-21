/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RecipeNow;

import java.sql.SQLException;

public class app {
    
     public static void main(String[] args) throws SQLException {
        
         
        // NewUserCntl user = new NewUserCntl();
        DatabaseHelper db = new DatabaseHelper();
        //db.createUserTable();
        //db.insertIntoTable("TestUser","testPass");
        // db.insertIntoTable("Tim", "dogbone");
        db.printTable();
        //db.closeConnection();
    }
    

}
