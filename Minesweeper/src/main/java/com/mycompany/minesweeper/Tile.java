/*
 * Tile.java
 *
 * Ethan Kwon
 * ICS4U
 * ISU Assignment
 * February 5, 2021
 *
 * This file will contain the Tile class which will contain the bombs and 
 * numbers for the Board
 */
package com.mycompany.minesweeper;

import javax.swing.JButton;

public class Tile extends JButton {
    // Declare variables
        // Position in board
    private int row, column;
        // Number of surrounding bombs
    public int surrounding;
        // If the button is a bomb
    private boolean isBomb;
        // If the button is flagged
    private boolean flagged;
    
    public Tile(int r, int c) {
        // Constructor
        row = r;
        column = c;
        isBomb = false;
        flagged = false;
    }
    
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getSurrounding() {
        return surrounding;
    }

    public void setSurrounding(int surrounding) {
        this.surrounding = surrounding;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public void setIsBomb(boolean isBomb) {
        this.isBomb = isBomb;
    }
    
    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }
    
}
