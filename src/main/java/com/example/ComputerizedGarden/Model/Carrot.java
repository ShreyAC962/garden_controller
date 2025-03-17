package com.example.ComputerizedGarden.Model;

public class Carrot extends Plant {

    private static int count;


    public Carrot(int row, int col) {
        super("Carrot", 8, 15, row, col, 3);
        count += 1;
    }


    public int getCount() {
        return count;
    }


    public void decrementCount() {
        count -= 1;
    }
}