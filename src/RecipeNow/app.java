/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RecipeNow;

import java.sql.SQLException;

public class app {
    
     public static void main(String[] args) throws SQLException {
        
        
        System.out.println("This is a test");
        
        System.out.println("Testing from Eric");
        System.out.println("This is a test from Youngmin #2");
        
        DataBaseHelper db = new DataBaseHelper();
        db.createUserTable();
        db.insertIntoTable(1,"TestUser","testPass");
       // db.insertIntoTable("Tim", "dogbone");
       // db.printTable();
    }
    

}
