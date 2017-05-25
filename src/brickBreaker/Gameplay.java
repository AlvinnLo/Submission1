package brickBreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;
import javax.swing.JPanel;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Created by loc8537 on 5/5/2017.
 */
public class Gameplay extends JPanel implements KeyListener, ActionListener
{
    JPanel jp = new JPanel();
    JLabel jl = new JLabel();

    private boolean play =false;
    private int score = 0;

    private int totalBricks = 35;

    private Timer timer;
    private int delay = 8;

    private int playerX = 310;
    private int playerY = 200;
    //starting position for the slide

    private int ballposX = 120;
    private int ballposY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;

    private MapGenerator map;

    public Gameplay()
    {
        map = new MapGenerator(5,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g)
    {

        //backgrounds
        g.setColor(Color.black);
        g.fillRect(1,1, 692, 592);

        //backgrounds
        g.setColor(Color.black);
        g.fillRect(1,1, 692, 592);

        map.draw((Graphics2D)g);

        //billy herrington image
        //jl.setIcon(new ImageIcon("billy.jpg"));
        //jp.add(jl);
        //add(jp);
        //validate();

        // borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        //scores
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Scores: "+score, 550, 30);

        //directions
        g.setColor(Color.white);

        g.setFont(new Font("serif", Font.BOLD, 15));
        g.drawString("Controls: A for left and D for Right For Blue Paddle", 15, 25);


        // the paddle
        g.setColor(Color.blue);
        g.fillRect(playerX, 550, 100, 8);


        // the ball 2
        g.setColor(Color.pink);
        g.fillOval(ballposX, ballposY, 20, 20);

        if(totalBricks <= 0){
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You won: ", 270, 290);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart ", 230, 320);

            g.setColor(Color.yellow);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Purchase Full Version for $6.99 ", 210, 350);
        }


        if(ballposY > 570){
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);

            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You like embarrassing me huh?", 140, 300);

            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Score:" + score, 270, 350);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart ", 230, 400);

        }


        g.dispose();

    }

    public void actionPerformed(ActionEvent e)
    {
        timer.start();
        if(play)
        {
            if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8)))
            {
                ballYdir = -ballYdir;
            }


            A: for(int i = 0; i<map.map.length; i++){
                for(int j = 0; j<map.map[0].length; j++){
                    if(map.map[i][j] > 0){
                        int brickX = j*map.brickWidth + 80;
                        int brickY = i*map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
                        Rectangle brickRect = rect;

                        if(ballRect.intersects(brickRect)){
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width){
                                ballXdir = -ballXdir;
                            } else{
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }

            ballposX += ballXdir;
            ballposY += ballYdir;
            //left
            if(ballposX < 0)
            {
                ballXdir = -ballXdir;
            }
            //top
            if(ballposY < 0)
            {
                ballYdir = -ballYdir;
            }
            //right
            if(ballposX > 670)
            {
                ballXdir = -ballXdir;
            }
        }

        //so the panel can move
        repaint();
    }


    public void keyTyped(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {}

    public void keyPressed(KeyEvent e)
    {

        if(e.getKeyCode() == KeyEvent.VK_D)
        {
            if(playerY >= 600)
            {
                playerY = 600;
            }
            else
            {
                moveUp();
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_A)
        {
            if(playerY < 10)
            {
                playerY = 10;
            }
            else
            {
                moveDown();
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_Q)
        {
            destroyer();
        }


        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play){
                play = true;
                ballposX = 120;
                ballposY = 350;
                ballXdir =  -1;
                ballYdir = -2;
                playerX = 310;
                score = 0;
                totalBricks = 35;
                map = new MapGenerator(5, 7);

                repaint();
            }
        }
    }


    public void moveRight()
    {
        play = true;
        playerX += 20;
    }

    public void moveLeft()
    {
        play = true;
        playerX -= 20;
    }




}