package com.example.AutomatedGarden.Model;

public class Mango extends Plant {
    // ------ Instance Variables ------
    private static int count;

    // ------ Constructors ------
    public Mango(int row, int col) {
        super("Mango", 10, 18, row, col,6);
        count += 1;
    }

    // ------ Accessor Methods ------
    public int getCount() {
        return count;
    }

    // ------ Mutator Methods ------
    public void decrementCount() {
        count -= 1;
    }
}
