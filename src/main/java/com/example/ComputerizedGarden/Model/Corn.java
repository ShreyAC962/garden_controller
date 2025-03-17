package com.example.ComputerizedGarden.Model;

public class Corn extends Plant {

    private static int count;


    public Corn(int row, int col) {
        super("Corn", 9, 20, row, col, 4);
        count += 1;
    }


    public int getCount() {
        return count;
    }


    public void decrementCount() {
        count -= 1;
    }
}
