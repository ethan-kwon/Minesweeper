/*
 * Menu.java
 *
 * Ethan Kwon
 * ICS4U
 * ISU Assignment
 * February 5, 2021
 *
 * This file will contain the Menu class which will display the menu at the 
 * start of the program
 */
package com.mycompany.minesweeper;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Menu {
    private int width, height, bombs;    
    
    public Menu() {
        // Create JFrame and JButtons for the menu
        JFrame menu = new JFrame("Minesweeper Menu");
        JButton play = new JButton("Play");
        JButton rules = new JButton("Rules");
        
        // Set bounds and positions for both JButtons
        play.setBounds(150, 90, 100, 30);
        rules.setBounds(150, 170, 100, 30);
        
        // Add action listener to the play JButton which will start the game
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // menu JFrame disappears
                menu.setVisible(false);
                // Variables for the board
                width = 9;
                height = 9;
                bombs = 10;
                // Call the board
                Main.board = new Board(width, height, bombs);
            }
        });
        
        
        // Add action listener to the rules JButton which will show the rules 
        // for the game
        rules.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // menu JFrame disappears
                menu.setVisible(false);
                // Create rules JFrame
                JFrame rules = new JFrame("Minesweeper Rules");
                // Dispose frame on close
                rules.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                // Set relative location to null
                rules.setLocationRelativeTo(null);
                // Add window listener so that the menu JFrame reappears on 
                // close
                rules.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        menu.setVisible(true);
                    }
                });
                // Container for rules
                Container cp = rules.getContentPane();
                // Create new JTextPane for the text
                JTextPane pane = new JTextPane();
                // Do not let user edit the pane
                pane.setEditable(false);
                // Rules
                pane.setText("The object of Minesweeper is to expose all the "
                        + "open areas on the board without hitting any bombs.\n"
                        + "\nUse the left click button on the mouse to select a "
                        + "space on the grid. If you hit a bomb, you lose. "
                        + "The numbers on the board represent how many bombs are"
                        + " adjacent to a square. For example, if a square has a"
                        + " \"3\" on it, then there are 3 bombs next to that "
                        + "square. The bombs could be above, below, right, left,"
                        + " or diagonal to the square. Use the right click "
                        + "button to flag possible bomb squares. Avoid all the "
                        + "bombs and expose all the empty spaces to win "
                        + "Minesweeper.\n\n The board is the standard easy size "
                        + "from Microsoft's Minesweeper");
                
                // Put the JTextPane as a JScrollPane
                JScrollPane scrollPane = new JScrollPane(pane);
                // Add the JScrollPane to the container
                cp.add(scrollPane, BorderLayout.CENTER);
                // Set size of rules JFrame
                rules.setSize(400,300);
                // Make rules JFrame visible
                rules.setVisible(true);
            }
        });
        
        // Add both buttons to the menu JFrame
        menu.add(play);
        menu.add(rules);
        
        // Set size of menu JFrame
        menu.setSize(400,300);
        // Set menu to not resizable
        menu.setResizable(false);
        // Closes application on close
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Set relative location to null so that it appears to middle of screen
        menu.setLocationRelativeTo(null);
        // Set layout to null
        menu.setLayout(null);
        // Set the menu JFrame to visible
        menu.setVisible(true);
    }
}
