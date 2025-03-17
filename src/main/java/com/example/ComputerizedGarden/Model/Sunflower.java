package com.example.ComputerizedGarden.Model;

public class Sunflower extends Plant {
    // ------ Instance Variables ------
    private static int count;

    // ------ Constructors ------
    public Sunflower(int row, int col) {
        super("Sunflower", 8, 15, row, col, 3);
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