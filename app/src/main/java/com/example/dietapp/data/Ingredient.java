package com.example.dietapp.data;

public class Ingredient implements Cloneable {

    public int id;
    public String name;
    public int amount;

    public float[] nutrients = new float[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

    @Override
    public Ingredient clone() {
        Ingredient clonedIng = new Ingredient();
        clonedIng.id = id;
        clonedIng.name = name;
        clonedIng.amount = amount;
        clonedIng.nutrients = nutrients.clone();
        return clonedIng;
    }
}
