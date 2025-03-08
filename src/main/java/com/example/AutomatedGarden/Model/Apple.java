package com.example.AutomatedGarden.Model;

public class Apple extends Plant {

    private static int count;


    public Apple(int row, int col) {
        super("Apple", 9, 20, row, col, 4);
        count += 1;
    }


    public int getCount() {
        return count;
    }


    public void decrementCount() {
        count -= 1;
    }
}
