/*
 * Board.java
 * 
 * Ethan Kwon
 * ICS4U
 * ISP Assignment
 * February 5, 2021
 * 
 * This file contains the Board class which will contain the Tile and 
 * have features to run the Minesweeper game
 */
package com.mycompany.minesweeper;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Board extends JPanel implements MouseListener {
    // Declare variables
    private int width, height;
    private int bombs;
    private boolean isRunning;
    Tile[][] board;
    
    public Board(int width, int height, int bombs) {
        // constructor
        this.width = width;
        this.height = height;
        this.bombs = bombs;
        this.isRunning = true;
        
        // Creates array of JButtons
        board = new Tile[this.width][this.height];
        
        // Frame holding tiles
        JFrame frame = new JFrame("Minesweeper");
        // Set frame size
        frame.setSize(600,600);
        // Do not let user change size of frame
        frame.setResizable(false);
        // Stop running on close
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create setting panel
        JPanel setting = new JPanel(new FlowLayout());
        // Reset JButton
        JButton reset = new JButton("Reset");
        // Creates new Board and disposes of the game frame
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.board = new Board(width, height, bombs);
                frame.dispose();
            }
        });
        
        // Menu JButton
        JButton menu = new JButton("Menu");
        // Disposes of game frame and creates Menu
        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                Menu menu = new Menu();
            }
        });
        
        // Add menu JButton and reset JButton
        setting.add(reset);
        setting.add(menu);
        
        // Create minesweeper panel
        JPanel grid = new JPanel();
        grid.setLayout(new GridLayout(this.width, this.height));
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                // Create grid of JButton and add them to the array
                Tile tile = new Tile(row, col);
                tile.addMouseListener(this);
                grid.add(tile);
                board[row][col] = tile;
            }
        }
        
        // Create split pane to hold setting panel and grid panel
        JSplitPane split = new JSplitPane(SwingConstants.HORIZONTAL, setting, 
                grid);
        // Don't let user change sizes of each panel
        split.setDividerSize(0);
        
        // Layout Manager
        frame.add(split);
        
        // Generate bombs
        generateBombs();
        // Count number of bombs around each tile
        countBombs();
        
        // Make frame visible
        frame.setVisible(true);
    }
    
    // Method to create bombs
    public void generateBombs() {
        int count = 0;
        while (count < bombs) {
            // Get random location to place bomb
            int row = (int)(Math.random()*board.length);
            int col = (int)(Math.random()*board[0].length);
            if (board[row][col].isBomb() == false) {
                board[row][col].setIsBomb(true);
                count++;
            }
        }
    }
    
    // Calls the updateCount method to count the amount of bombs around each
    // tile
    public void countBombs() {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                updateCount(row, col);
            }
        }
    }
    
    // Counts the amount of bombs around each tile
    public void updateCount(int r, int c) {
        if (!board[r][c].isBomb()) {
            return;
        }
        for (int row = r-1; row <= r+1; row++) {
            for (int col = c-1; col <= c+1; col++) {
                try {
                    board[row][col].surrounding++;
                } catch (Exception e) {
                    // index went out of bounds
                }
            }
        }
    }
    
    // Disables all tiles on the board
    public void disableTiles() {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                board[row][col].setEnabled(false);
            }
        }
    }
    
    // Shows the answer for the board
    public void displayBoard() {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                // Set any flags on tile to null
                board[row][col].setIcon(null);
                // If tile is a bomb, show the bomb image
                if (board[row][col].isBomb()) {
                    BufferedImage img;
                    try {
                        img = ImageIO.read(new File(
                                "src/main/java/com/mycompany/minesweeper/"
                                        + "bomb.png"));
                        Image bomb = img.getScaledInstance(
                                board[row][col].getWidth(), 
                            board[row][col].getHeight(), Image.SCALE_SMOOTH);
                        board[row][col].setIcon(new ImageIcon(bomb));
                    } catch (IOException ex) {
                        System.out.println("Error");
                    }
                } 
                // If there are no surrounding bombs, show an empty tile
                else if (board[row][col].surrounding == 0){
                    board[row][col].setText("");
                }
                // Otherwise the tile is not a bomb and should show number of 
                // bombs next to tile
                else {
                    board[row][col].setText(board[row][col].getSurrounding()+"");
                }
            }
        }
        // Disable the tiles
        disableTiles();
        // Repaint
        repaint();
    }
    
    // Recursive method that shows empty tiles and border of bombs
    public void showTile(int r, int c) {
        // base case is when there is nothing to do (chosen tile has a bomb next
        // to it):
        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length ||
                !board[r][c].isEnabled()) {
            return;
        } else if (board[r][c].surrounding != 0) {
            board[r][c].setIcon(null);
            board[r][c].setText(board[r][c].surrounding+"");
            board[r][c].setEnabled(false);
        } else {
            board[r][c].setIcon(null);
            board[r][c].setText("");
            board[r][c].setEnabled(false);
            showTile(r-1, c);
            showTile(r+1, c);
            showTile(r, c-1);
            showTile(r, c+1);
        }
    }
    
    // Check if the game is done
    public void checkGame() {
        int count = 0;
        // Count number of disabled squares
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (!board[row][col].isEnabled() && !board[row][col].isBomb()) {
                    count++;
                }
            }
        }
        
        // If number of disabled squares is number of squares in grid - number
        // of bombs, show user that they won
        if (count == (width * height - bombs)) {
            JOptionPane.showMessageDialog(null, "You win");
            displayBoard();
            isRunning = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // On left click and game is running
        if (e.getButton() == 1 && this.isRunning) {
            Tile tile = (Tile)(e.getComponent());
            // If the tile is a bomb
            if (tile.isBomb()) {
                // Stop the game from running
                this.isRunning = false;
                // Tell user they hit a bomb
                JOptionPane.showMessageDialog(null, "You hit a mine!");
                // Show the answers for the board
                displayBoard();
            } 
            // Otherwise
            else {
                // Set image to null
                tile.setIcon(null);
                // Show number of surrounding tiles
                showTile(tile.getRow(), tile.getColumn());
                // Check if the game is done
                checkGame();
            }
        }
        // On right click and game is running
        else if (e.getButton() == 3 && this.isRunning 
                && e.getComponent().isEnabled()) {
            Tile tile = (Tile)(e.getComponent());
            // If the tile is flagged
            if (tile.isFlagged()) {
                // Set icon to null
                tile.setIcon(null);
                // Set tile flagged to false
                tile.setFlagged(false);
            } 
            // Otherwise, if it is not flagged and there is no text
            else if (tile.isFlagged() == false 
                    && tile.getText().isBlank()){
                // Set tile flagged to true
                tile.setFlagged(true);
                // Set delete any text
                tile.setText("");
                // Set image to flag picture
                BufferedImage img;
                try {
                    img = ImageIO.read(new File(
                            "src/main/java/com/mycompany/minesweeper/"
                                    + "Minesweeper_flag.svg.png"));
                    Image flag = img.getScaledInstance(
                            e.getComponent().getWidth(), 
                        e.getComponent().getHeight(), Image.SCALE_SMOOTH);
                    tile.setIcon(new ImageIcon(flag));
                } catch (IOException ex) {
                    System.out.println("Error");
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
    
}
