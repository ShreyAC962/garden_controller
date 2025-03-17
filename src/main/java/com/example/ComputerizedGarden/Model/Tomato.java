package com.example.ComputerizedGarden.Model;

public class Tomato extends Plant {
    // ------ Instance Variables ------
    private static int count;

    // ------ Constructors ------
    public Tomato(int row, int col) {
        super("Tomato", 9, 20, row, col, 4);
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
