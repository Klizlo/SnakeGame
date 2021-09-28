/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Klizlo
 * 
 * @version 1.0
 * 
 */

public class Board extends JPanel implements ActionListener{
    
    final private int OBJECT_SIZE = 40;
    final private int B_WIDTH = 600;
    final private int B_HEIGHT = 600;
    final private int MAX_BODY_SIZE = 225;
    final private int DELAY = 140;
    // 140 = 7 FPS, because 1s/7 = 0.14s = 140ms
    
    final private int SNAKE_X[] = new int[MAX_BODY_SIZE];
    final private int SNAKE_Y[] = new int[MAX_BODY_SIZE];
    /* 
       SNAKE_X and SNAKE_Y arrays contain position of each part of the snake
       The maximum size of the snake is 225 [(600*600)/(40/40)]
       SNAKE_X[0] and SNAKE_Y[0] contains coordinates of the snake's head
    */
    
    private int bodySize;
    private int appleX;
    private int appleY;
    
    private boolean leftDirection;
    private boolean rightDirection;
    private boolean upDirection;
    private boolean downDirection;
    private boolean inGame = true;
    
    private final Timer timer;
    
    private Image apple;
    private Image head;
    private Image body;
    private Image tail;

    Board(){
        initBoard();
        initIcons();
        initGame();
        
        timer = new Timer(DELAY, this);
        timer.start();
    }
    
    private void initBoard(){
        
        addKeyListener(new TAdapter());
        setPreferredSize(new Dimension(B_HEIGHT,B_WIDTH));
        setBackground(Color.black);
        setFocusable(true);
        
    }
    
    private void initIcons(){
        ImageIcon iia = new ImageIcon("src/resources/apple.png");
        apple = iia.getImage();
        
        ImageIcon iih = new ImageIcon("src/resources/head.png");
        head = iih.getImage();
        
        ImageIcon iib = new ImageIcon("src/resources/body.png");
        body = iib.getImage();
        
        ImageIcon iit = new ImageIcon("src/resources/tail.png");
        tail = iit.getImage();
        
    }
    
    private void initGame(){
        appleX = 480;
        appleY = 280;
        
        bodySize = 3;
        // initial length of snake including head is 3
        
        for(int i = 0; i < bodySize; i++){
            SNAKE_X[i] = 120 - i*OBJECT_SIZE;
            SNAKE_Y[i] = 280;
        }
        
        
        rightDirection = true;
        leftDirection = false;
        upDirection = false;
        downDirection = false;
        //initial direction of snake's move
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        // method of painting elements on the screen
        super.paintComponent(g);
        
        if(inGame){
            g.drawImage(apple, appleX, appleY, OBJECT_SIZE, OBJECT_SIZE, this);

            for(int i = 0; i < bodySize; i++){
                if(i == 0)
                    g.drawImage(head, SNAKE_X[i], SNAKE_Y[i], OBJECT_SIZE, OBJECT_SIZE, this);
                else if(i == bodySize - 1)
                    g.drawImage(tail, SNAKE_X[i], SNAKE_Y[i], OBJECT_SIZE, OBJECT_SIZE, this);
                else
                    g.drawImage(body, SNAKE_X[i], SNAKE_Y[i], OBJECT_SIZE, OBJECT_SIZE, this);
            }
        }
        else
            checkGameStatus(g);
    }
    
    private void locateApple(){
        //method of locating an apple on the board
        
        Random r = new Random();
        
        int randomPosition = r.nextInt(15) * OBJECT_SIZE;
        // We draw the number (0-14) and multiply it by object's size and then the obtain value will be assigned to the x position of the apple
        appleX = randomPosition;
        
        randomPosition = r.nextInt(15) * OBJECT_SIZE;
        // We draw the number (0-14) and multiply it by object's size and then the obtain value will be assigned to the y position of the apple
        appleY = randomPosition;
        
        for(int i = 0; i < bodySize; i++)
            if(SNAKE_X[i] == appleX && SNAKE_Y[i] == appleY)
                locateApple();
        // We draw the apple position again if it has the same position like one of the parts of snake
        
    }
    
    private void checkApple(){
        //method of checking that head of the snake is on the same position like an apple
        if(SNAKE_X[0] == appleX && SNAKE_Y[0] == appleY){
            bodySize++;
            locateApple();
        }
    }
    
    private void move(){
        
        for(int i = bodySize; i > 0; i--){
            SNAKE_X[i] = SNAKE_X[i-1];
            SNAKE_Y[i] = SNAKE_Y[i-1];
        }
        
        if(leftDirection)
            SNAKE_X[0] -= OBJECT_SIZE;
        if(rightDirection)
            SNAKE_X[0] += OBJECT_SIZE;
        if(upDirection)
            SNAKE_Y[0] -= OBJECT_SIZE;
        if(downDirection)
            SNAKE_Y[0] += OBJECT_SIZE;
    }
    
    private void checkCollision(){
        //a method of checking that the snake goes off the board or that the snake does not bump into itself
        
        for(int i = bodySize; i > 0; i--){
            if((bodySize != 4) && (SNAKE_X[i] == SNAKE_X[0]) && (SNAKE_Y[i] == SNAKE_Y[0]))
                inGame = false;
        }
        
        if(SNAKE_X[0] < 0)
            inGame = false;
        if(SNAKE_X[0] > B_WIDTH)
            inGame = false;
        if(SNAKE_Y[0] < 0)
            inGame = false;
        if(SNAKE_Y[0] > B_HEIGHT)
            inGame = false;
        
    }
    
    public void checkGameStatus(Graphics g){
        //method of checking if we have won or have lost the game and inform the player about iy
        String msg;
        if(bodySize == MAX_BODY_SIZE)
            msg = "YOU WON!";
        else
            msg = "GAME OVER";
        
        Font font = new Font("Consolas", Font.BOLD, 25);
        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(msg, (B_WIDTH - getFontMetrics(font).stringWidth(msg))/2, B_HEIGHT/2 - getFontMetrics(font).getHeight());
        
        msg = "Press any key to start";
        
        font = new Font("Consolas", Font.BOLD, 21);
        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(msg, (B_WIDTH - getFontMetrics(font).stringWidth(msg))/2, B_HEIGHT/2);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(inGame){
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }
    
    private class TAdapter extends KeyAdapter{
    // an auxiliary class for handling keys
        
        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            
            if(!inGame){
                initGame();
                inGame = true;
            }
            
            if(keyCode == KeyEvent.VK_LEFT && !rightDirection){
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(keyCode == KeyEvent.VK_RIGHT && !leftDirection){
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(keyCode == KeyEvent.VK_UP && !downDirection){
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
            if(keyCode == KeyEvent.VK_DOWN && !upDirection){
                downDirection = true;
                leftDirection = false;
                rightDirection = false;
            }

            
        }
        
    }
    
}
