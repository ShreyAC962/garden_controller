package com.example.ComputerizedGarden.Model;

public class Watermelon extends Plant {

    private static int count;


    public Watermelon(int row, int col) {
        super("Watermelon", 8, 18, row, col, 3);
        count += 1;
    }


    public int getCount() {
        return count;
    }


    public void decrementCount() {
        count -= 1;
    }
}