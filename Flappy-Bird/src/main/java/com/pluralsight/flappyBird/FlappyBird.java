package com.pluralsight.flappyBird;

import java.awt.*;
import java.awt.event.*;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardwidth = 360;
    int boardheight = 640;

    //Images
    Image backgroundIMG;
    Image topPipeIMG;
    Image bottomPipeImg;
    Image birdimg;
    //Bird
    int birdX = boardwidth / 8;
    int birdy = boardheight / 2;
    int birdWidth = 34;
    int birdHeight = 24;

//bird class
    class Bird {
        int x = birdX;
        int y = birdy;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }

    // Pipes
    int pipex = boardwidth;
    int pipey = 0;
    int pipewidth = 64; // Scaled 1/6
    int pipeheight = 512;

    class Pipe {
        int x = pipex;
        int y = pipey;
        int width = pipewidth;
        int height = pipeheight;
        Image img;
        boolean passed = false;

        Pipe(Image img) {
            this.img = img;
        }
    }


    // Game logic
    Bird bird;
    int velocityy = 0; // bird up and down speed
    int gravity = 1;

    int velocityx = -4; // pipes moving speed (simulates bird moving right)
    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameloop;
    Timer placePipesTimer;
    boolean gameOver = false;
    double score = 0;

    FlappyBird() {
        setPreferredSize(new Dimension(boardwidth, boardheight));
        //setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);
        //load Images
        backgroundIMG = new ImageIcon("flappybirdbg.png").getImage();
        birdimg = new ImageIcon("flappybird.png").getImage();
        topPipeIMG = new ImageIcon("toppipe.png").getImage();
        bottomPipeImg = new ImageIcon("bottompipe.png").getImage();

        bird = new Bird(birdimg);
        pipes = new ArrayList<Pipe>();
        // Pipes Timer
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placepipes();

            }
        });
        placePipesTimer.start();
        // Game timer
        gameloop = new Timer(1000 / 60, this);//1000/60 = 16.6
        gameloop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void placepipes() {
        //(0-1) * pipe-height/2 -> (0-256)
        //128
        // 0 - 128- (0-256) --> 1/4 pipeHeight -> 3/4 pipeHeight

        int randompipey = (int)(pipey - pipeheight/4 - Math.random() *(pipeheight/2));
        int openingSpace = boardheight/4;
        Pipe toppipe = new Pipe(topPipeIMG);
        toppipe.y = randompipey;;
        pipes.add(toppipe);
        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = toppipe.y + pipeheight + openingSpace;
        pipes.add(bottomPipe);
    }

    private void draw(Graphics g) {
        //background
        g.drawImage(backgroundIMG, 0, 0, boardwidth, boardheight, null);

        //Bird
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        // pipes
        for (Pipe pipe : pipes) {
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        //score
        g.setColor(Color.MAGENTA);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if(gameOver) {
            g.drawString(" Game Over:" + String.valueOf((int)score), 10, 35);
        } else {
            g.drawString(String.valueOf((int)score), 10, 35);
        }

    }

    public void move() {
        //bird
        velocityy += gravity;
        bird.y += velocityy;
        bird.y = Math.max(bird.y, 0);
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityx;
            if(!pipe.passed && bird.x > pipe.x + pipe.width){
                pipe.passed = true;
                score += 0.5;
            }
            if(collision(bird,pipe)){
                gameOver = true;
            }
        }
        if(bird.y > boardheight){
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            placePipesTimer.stop();
            gameloop.stop();
        }


    }
    public boolean collision(Bird a, Pipe b){
        return a.x < b.x +b.width && //a's top left corner doesnt touch b top right corner
                a.x +a.width > b.x && //
                a.y < b.y + b.height && //
                a.y + a.height > b.y; //
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityy = -9;
            if (gameOver){
                // restart game
                bird.y = birdy;
                velocityy = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                gameloop.start();
                placePipesTimer.start();
            }

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


}
