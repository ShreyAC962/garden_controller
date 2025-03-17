package com.example.ComputerizedGarden.Model;

public abstract class Insect {
    // ------ Instance Variables ------
    private final String name;
    private final int row;
    private final int col;

    // ------ Constructors ------
    public Insect(String name, int row, int col) {
        this.name = name;
        this.row = row;
        this.col = col;
    }

    // ------ Accessors ------
    public String getName() {
        return name;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    // ------ Abstract Methods ------
    public abstract boolean isPest();
}
