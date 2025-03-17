package com.example.ComputerizedGarden.Model;

public class Pest extends Insect {
    // ------ Constructors ------
    public Pest(String name, int row, int col) {
        super(name, row, col);
    }

    // ------ Accessor Methods ------
    @Override
    public boolean isPest() {
        return true;
    }
}