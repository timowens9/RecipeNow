/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RecipeNow;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RecipeList implements Serializable {
    
    private ArrayList<Recipe> listOfRecipe = new ArrayList();
    private String listOfRecipeFileName = "recipes/" + "recipe.ser";
    
    public RecipeList() {
        this.readRecipeListFile();
        if(listOfRecipe.isEmpty() || listOfRecipe == null) {
            System.out.println("File does not exist or it is empty");
        } 
    }
    
    public void readRecipeListFile() {
        
        FileInputStream fis = null;
        ObjectInputStream in = null;
        try {
            fis = new FileInputStream(listOfRecipeFileName);
            in = new ObjectInputStream(fis);
            listOfRecipe = (ArrayList)in.readObject();
            in.close();
            if(!listOfRecipe.isEmpty()){
                //System.out.println("Recipe exists");
            } 
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    public void writeRecipeListFile() {
        
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(listOfRecipeFileName);
            out = new ObjectOutputStream(fos);
            out.writeObject(listOfRecipe);
            out.close();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
   }
    
    public void printRecipeList() {

        System.out.println("The Recipe ser file has these Recipes:");
        for(int i = 0; i < listOfRecipe.size(); i++){
            Recipe currentRecipe = (Recipe) listOfRecipe.get(i);
            System.out.println("Recipe Name: " + currentRecipe.getName());
            //System.out.println("Recipe Ingredients: " + currentRecipe.getIngredients());
            //System.out.println("Recipe Calorie: " + currentRecipe.getCalories());
            //System.out.println("Recipe Rating : " + currentRecipe.getRating());
        }
    }
    
    public void addRecipe(Recipe newRecipe) {
        listOfRecipe.add(newRecipe);
        this.writeRecipeListFile();
    }
    
    public ArrayList getList() {
        return this.listOfRecipe;
    }
    
    public void setList(ObservableList<Recipe> curList) {
        listOfRecipe = new ArrayList();
        curList.forEach((temp) -> {
            listOfRecipe.add(temp);
        });
        writeRecipeListFile();
    }
    
    public void deleteRecipe(Recipe curRecipe) {
        for(int i = 0; i < listOfRecipe.size(); i++) {
            if(curRecipe.getName().equals(listOfRecipe.get(i).getName())) {
                listOfRecipe.remove(i);
                break;
            }
        }
        writeRecipeListFile();
    }
    
    public ObservableList<Recipe> updateRecipeList(){
        ObservableList<Recipe> curList = FXCollections.observableArrayList();
        listOfRecipe.forEach((rc) -> {
            curList.add(rc);
        });
        return curList;
    }

    
}
