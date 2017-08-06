/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RecipeNow;

import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author Eric
 */
public class Recipe implements Serializable {  
    
    private int ID;
    private String name;
    private String description;
    private int[] ingredients;
    private int chefID;
    private int numCooked;
    private int rating;
    private int calories;


    public Recipe(int ID, String name, String description, String ingredients, int chefID, int numCooked, int rating, int calories){
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.ingredients = Arrays.stream(ingredients.substring(1, ingredients.length()-1).split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
        this.chefID = chefID;
        this.numCooked = numCooked;
        this.rating = rating;
        this.calories = calories;
    }
    public Recipe(int ID, String name, String description, int[] ingredients, int chefID, int numCooked, int rating, int calories){
        this(ID,name,description,Arrays.toString(ingredients), chefID, numCooked, rating, calories);
    }
    
    
    /**
     * @param ingredients the ingredients to set
     */
    public void setIngredients(int[] ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * @return the numCooked
     */
    public int getNumCooked() {
        return numCooked;
    }

    /**
     * @param numCooked the numCooked to set
     */
    public void setNumCooked(int numCooked) {
        this.numCooked = numCooked;
    }

    /**
     * @return the calories
     */
    public int getCalories() {
        return calories;
    }

    /**
     * @param calories the calories to set
     */
    public void setCalories(int calories) {
        this.calories = calories;
    }
    
 
    /**
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the ingredients
     */
    public String getIngredients() {
        return Arrays.toString(ingredients);
    }
    public int[] getIngredientsArray() {
        return ingredients;
    }

    /**
     * @param ingredients the ingredients to set
     */
    public void setIngredients(String ingredients) {
        this.ingredients = Arrays.stream(ingredients.substring(1, ingredients.length()-1).split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
    }

    /**
     * @return the rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * @return the chefID
     */
    public int getChefID() {
        return chefID;
    }

    /**
     * @param chefID the chefID to set
     */
    public void setChefID(int chefID) {
        this.chefID = chefID;
    }
    
    @Override
    public String toString(){
        return getName();
    }
}
