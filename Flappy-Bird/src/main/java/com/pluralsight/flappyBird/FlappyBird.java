package com.pluralsight.flappyBird;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;



public class FlappyBird extends JPanel implements ActionListener {
    int boardwidth = 360;
    int boardheight = 640;

    //Images
    Image backgroundIMG;
    Image topPipeIMG;
    Image bottomPipeImg;
    Image birdimg;
    //Bird
    int birdX = boardwidth/8;
    int birdy = boardheight/2;
    int birdWidth = 34;
    int birdHeight = 24;



    class Bird{
        int x = birdX;
        int y = birdy;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img){
            this.img = img;
        }
    }

   // Game logic
    Bird bird;
    int velocityy = -9;
    int gravity = 1;

    int velocityx = 0;
    Timer gameloop;

    FlappyBird() {
        setPreferredSize(new Dimension(boardwidth, boardheight));
        setBackground(Color.blue);
        //load Images
        backgroundIMG= new ImageIcon("flappybirdbg.png").getImage();
        birdimg = new ImageIcon("flappybird.png").getImage();
        topPipeIMG = new ImageIcon("toppipe.png").getImage();
        bottomPipeImg = new ImageIcon("bottompipe.png").getImage();

        bird = new Bird(birdimg);
        gameloop = new Timer(1000/60, this);//1000/60 = 16.6
        gameloop.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        System.out.println("draw");
        //background
        g.drawImage(backgroundIMG, 0, 0, boardwidth, boardheight, null);

        //Bird
        g.drawImage(bird.img,bird.x,bird.y,bird.width,bird.height, null);

    }
    public void move(){
        //bird
        velocityy += gravity;
        bird.y += velocityy;
        bird.y = Math.max(bird.y, 0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();


    }






}
