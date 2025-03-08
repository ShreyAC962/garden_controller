package com.example.AutomatedGarden.Model;

public class Banana extends Plant {

    private static int count;


    public Banana(int row, int col) {
        super("Banana", 8, 15, row, col, 3);
        count += 1;
    }


    public int getCount() {
        return count;
    }


    public void decrementCount() {
        count -= 1;
    }
}