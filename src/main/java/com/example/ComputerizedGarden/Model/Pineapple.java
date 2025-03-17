package com.example.ComputerizedGarden.Model;

public class Pineapple extends Plant {
    // ------ Instance Variables ------
    private static int count;

    // ------ Constructors ------
    public Pineapple(int row, int col) {
        super("Pineapple", 10, 15, row, col,6);
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
