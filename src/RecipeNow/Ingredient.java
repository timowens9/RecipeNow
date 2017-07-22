/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RecipeNow;

/**
 *
 * @author Eric
 */
public class Ingredient {
    private int ID;
    private String name;
    private boolean dairy;
    private int calories;

    public Ingredient(int ID, String name, int calories, int dairy){
        this.ID = ID;
        this.name = name;
        this.calories = calories;
        this.dairy = dairy==1;
    }
    public Ingredient(int ID, String name, int calories, boolean dairy){
        this.ID = ID;
        this.name = name;
        this.calories = calories;
        this.dairy = dairy;
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
     * @return the dairy
     */
    public boolean isDairy() {
        return dairy;
    }

    /**
     * @param dairy the dairy to set
     */
    public void setDairy(boolean dairy) {
        this.dairy = dairy;
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
    
    @Override public String toString(){
        return getName();
    }
}
