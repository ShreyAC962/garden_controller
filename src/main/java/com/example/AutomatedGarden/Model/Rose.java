package com.example.AutomatedGarden.Model;

public class Rose extends Plant {

    private static int count;


    public Rose(int row, int col) {
        super("Rose", 8, 15, row, col, 3);
        count += 1;
    }


    public int getCount() {
        return count;
    }


    public void decrementCount() {
        count -= 1;
    }
}