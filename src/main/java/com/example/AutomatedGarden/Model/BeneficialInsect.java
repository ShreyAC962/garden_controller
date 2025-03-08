package com.example.AutomatedGarden.Model;

public class BeneficialInsect extends Insect {

    // ------ Constructors ------
    public BeneficialInsect(String name, int row, int col) {
        super(name, row, col);
    }

    // ----- Accessor Methods -----
    @Override
    public boolean isPest() {
        return false;
    }
}