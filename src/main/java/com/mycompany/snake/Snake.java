/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.snake;

import javax.swing.JFrame;

/**
 *
 * @author Klizlo
 * 
 * @version 1.0
 * 
 */
public class Snake extends JFrame{
    
    Snake(){
        setName("Snake");
        setResizable(false);
        add(new Board());
        pack();
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        Snake s = new Snake();
        s.setVisible(true);
    }
    
}
