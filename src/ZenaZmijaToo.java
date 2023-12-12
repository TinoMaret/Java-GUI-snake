import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class ZenaZmijaToo extends JPanel implements ActionListener {
    private final int SCREEN_WIDTH = 608;
    private final int SCREEN_HEIGHT = 608;
    private final int SCREEN_UNIT = 32;
    private int[] xAxis = new int[SCREEN_WIDTH*SCREEN_HEIGHT/SCREEN_UNIT];
    private int[] yAxis = new int[SCREEN_HEIGHT*SCREEN_WIDTH/SCREEN_UNIT];
    private int bodyParts = 3;
    private int applesEaten = 0;
    private int[] appleLocation = new int[2];
    private char direction = 'R';
    private boolean running = false;
    private Random random;
    private Timer timer;
    private Image bgImage;
    private Image appleImage;
    private Image headDown;
    private Image headUp;
    private Image headLeft;
    private Image headRight;
    private Image snake1;
    private Image snake2;
    private Image snake3;
    private Image snake4;
    private Image snake5;
    private Image snake6;
    private Image tailDown;
    private Image tailUp;
    private Image tailLeft;
    private Image tailRight;
    private List<Point> directionChange = new ArrayList<>();




    public ZenaZmijaToo(){
        try {
            bgImage = ImageIO.read(new File("src/snakemap.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        random = new Random();
        setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        setOpaque(false);
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
        StartGame();
    }

    private void StartGame(){
        newApple();
        running = true;
        timer = new Timer(20, this);
        timer.start();
        try {
            appleImage = ImageIO.read(new File("src/apple.png"));
        }catch(IOException e){
            e.printStackTrace();
        }
        for (int i = 0; i < bodyParts; i++) {
            xAxis[i] = 64 + (bodyParts-i)*32;
            yAxis[i] = 64;
        }
        try {
            snake1 = ImageIO.read(new File("src/snake1.png"));
            snake2 = ImageIO.read(new File("src/snake2.png"));
            snake3 = ImageIO.read(new File("src/snake3.png"));
            snake4 = ImageIO.read(new File("src/snake4.png"));
            snake5 = ImageIO.read(new File("src/snake5.png"));
            snake6 = ImageIO.read(new File("src/snake6.png"));
            headDown = ImageIO.read(new File("src/headDOWN.png"));
            headLeft = ImageIO.read(new File("src/headLEFT.png"));
            headRight = ImageIO.read(new File("src/headRIGHT.png"));
            headUp = ImageIO.read(new File("src/headUP.png"));
            tailDown = ImageIO.read(new File("src/tailDOWN.png"));
            tailLeft = ImageIO.read(new File("src/tailLEFT.png"));
            tailRight = ImageIO.read(new File("src/tailRIGHT.png"));
            tailUp = ImageIO.read(new File("src/tailUP.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newApple() {
        appleLocation[0] = random.nextInt(1,(SCREEN_WIDTH / SCREEN_UNIT)-1) * SCREEN_UNIT;
        appleLocation[1] = random.nextInt(2,(SCREEN_HEIGHT / SCREEN_UNIT)-1) * SCREEN_UNIT;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(bgImage, 0,0,this);
        draw(g);
    }

    public void draw(Graphics g){
        if(running){
            g.drawImage(appleImage,appleLocation[0], appleLocation[1],SCREEN_UNIT,SCREEN_UNIT,this);
            for (int i = 0; i < bodyParts; i++) {
                Point pointOfChange = new Point(xAxis[i],yAxis[i]);
                if(i==bodyParts-1){
                    directionChange.remove(pointOfChange);
                    switch (checkTail(xAxis[i], yAxis[i], xAxis[i-1], yAxis[i-1])){
                        case 'L':
                            g.drawImage(tailLeft,xAxis[i],yAxis[i],SCREEN_UNIT,SCREEN_UNIT,this);
                            break;
                        case 'R':
                            g.drawImage(tailRight,xAxis[i],yAxis[i],SCREEN_UNIT,SCREEN_UNIT,this);
                            break;
                        case 'D':
                            g.drawImage(tailDown,xAxis[i],yAxis[i],SCREEN_UNIT,SCREEN_UNIT,this);
                            break;
                        case 'U':
                            g.drawImage(tailUp,xAxis[i],yAxis[i],SCREEN_UNIT,SCREEN_UNIT,this);
                            break;
                        case '0':
                            running = false;
                            break;
                    }
                }else if(directionChange.contains(pointOfChange)){
                    switch (checkTwist(xAxis[i+1],yAxis[i+1],xAxis[i],yAxis[i],xAxis[i-1],yAxis[i-1])){
                        case 0:
                            running = false;
                            break;
                        case 6:
                            g.drawImage(snake6,xAxis[i],yAxis[i],SCREEN_UNIT,SCREEN_UNIT,this);
                            break;
                        case 1:
                            g.drawImage(snake1,xAxis[i],yAxis[i],SCREEN_UNIT,SCREEN_UNIT,this);
                            break;
                        case 3:
                            g.drawImage(snake3,xAxis[i],yAxis[i],SCREEN_UNIT,SCREEN_UNIT,this);
                            break;
                        case 4:
                            g.drawImage(snake4,xAxis[i],yAxis[i],SCREEN_UNIT,SCREEN_UNIT,this);
                            break;
                    }
                }else if(i == 0){
                    switch (direction){
                        case 'R':
                            g.drawImage(headRight,xAxis[i],yAxis[i],SCREEN_UNIT,SCREEN_UNIT,this);
                            break;
                        case 'L':
                            g.drawImage(headLeft,xAxis[i],yAxis[i],SCREEN_UNIT,SCREEN_UNIT,this);
                            break;
                        case 'U':
                            g.drawImage(headUp,xAxis[i],yAxis[i],SCREEN_UNIT,SCREEN_UNIT,this);
                            break;
                        case 'D':
                            g.drawImage(headDown,xAxis[i],yAxis[i],SCREEN_UNIT,SCREEN_UNIT,this);
                            break;
                    }
                }else{
                    switch (checkBody(xAxis[i-1],yAxis[i-1],xAxis[i],yAxis[i],xAxis[i+1],yAxis[i+1])) {
                        case 2:
                            g.drawImage(snake2,xAxis[i],yAxis[i],SCREEN_UNIT,SCREEN_UNIT,this);
                            break;
                        case 5:
                            g.drawImage(snake5,xAxis[i],yAxis[i],SCREEN_UNIT,SCREEN_UNIT,this);
                            break;
                        case 0:
                            g.fillRect(xAxis[i],yAxis[i],SCREEN_UNIT,SCREEN_UNIT);
                            break;
                    }
                }

            }
            g.setColor(Color.white);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }
    public int checkTwist(int xPrev, int yPrev, int X, int Y, int xNext, int yNext){
        if(((xPrev == X && yPrev-32 == Y)&&(X+32 == xNext && Y == yNext)) || ((xPrev-32 == X && yPrev == Y) && (X == xNext && Y+32 == yNext)))
            return 1;
        if(((xPrev-32 == X && yPrev == Y)&&(X == xNext && Y-32 == yNext)) || ((xPrev == X && yPrev+32 == Y) && (X+32 == xNext && Y == yNext)))
            return 4;
        if(((xPrev+32 == X && yPrev == Y)&&(X == xNext && Y+32 == yNext)) || ((xPrev == X && yPrev-32 == Y) && (X-32 == xNext && Y == yNext)))
            return 3;
        else
            return 6;
    }


    public char checkTail(int X, int Y, int xNext, int yNext){
        if(X+32 == xNext && Y == yNext)
            return 'L';
        if(X-32 == xNext && Y == yNext)
            return 'R';
        if(X == xNext && Y+32 == yNext)
            return 'U';
        if(X == xNext && Y-32 == yNext)
            return 'D';
        return '0';
    }

    public int checkBody(int xPrev, int yPrev, int X, int Y, int xNext, int yNext){
        if(yPrev == Y && Y == yNext)
            return 2;
        if(xPrev == X && X == xNext)
            return 5;
        return 0;
    }

    public void move(){
        for (int i = bodyParts; i > 0; i--) {
            xAxis[i] = xAxis[i - 1];
            yAxis[i] = yAxis[i - 1];
        }
        switch (direction) {
            case 'U':
                yAxis[0] = yAxis[0] - SCREEN_UNIT;
                break;
            case 'D':
                yAxis[0] = yAxis[0] + SCREEN_UNIT;
                break;
            case 'L':
                xAxis[0] = xAxis[0] - SCREEN_UNIT;
                break;
            case 'R':
                xAxis[0] = xAxis[0] + SCREEN_UNIT;
                break;
        }
    }

    public void checkApple(){
        if (xAxis[0] == appleLocation[0] && yAxis[0] == appleLocation[1]) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    public void checkCollision(){
        for (int i = bodyParts; i > 0; i--) {
            if (xAxis[0] == xAxis[i] && yAxis[0] == yAxis[i]) {
                running = false;
            }
        }

        if (xAxis[0] < 32 || xAxis[0] > SCREEN_WIDTH-32 || yAxis[0] < 32 || yAxis[0] > SCREEN_HEIGHT-64) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkApple();
            checkCollision();
        }
        repaint();
    }


    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT && direction != 'R' && direction != 'L') {
                Point pointOfChange = new Point(xAxis[0],yAxis[0]);
                directionChange.add(pointOfChange);
                System.out.println(xAxis[0] + " " + yAxis[0]);
                System.out.println(directionChange.contains(pointOfChange));
                direction = 'L';
            } else if (key == KeyEvent.VK_RIGHT && direction != 'L' && direction != 'R') {
                Point pointOfChange = new Point(xAxis[0],yAxis[0]);
                directionChange.add(pointOfChange);
                System.out.println(xAxis[0] + " " + yAxis[0]);
                System.out.println(directionChange.contains(pointOfChange));
                direction = 'R';
            } else if (key == KeyEvent.VK_UP && direction != 'D' && direction != 'U') {
                Point pointOfChange = new Point(xAxis[0],yAxis[0]);
                directionChange.add(pointOfChange);
                System.out.println(xAxis[0] + " " + yAxis[0]);
                System.out.println(directionChange.contains(pointOfChange));
                direction = 'U';
            } else if (key == KeyEvent.VK_DOWN && direction != 'U' && direction != 'D') {
                Point pointOfChange = new Point(xAxis[0],yAxis[0]);
                directionChange.add(pointOfChange);
                System.out.println(xAxis[0] + " " + yAxis[0]);
                System.out.println(directionChange.contains(pointOfChange));
                direction = 'D';
            }
        }
    }

    public static class Point{
        int x;
        int y;
        public Point(int newx,int newy){
            x = newx;
            y = newy;
        }

        public int getX(){
            return x;
        }
        public int getY(){
            return y;
        }

        @Override
        public boolean equals(Object other) {
            if(this == other)
                return true;
            if(!(other instanceof Point))
                return false;
            Point otherPoint= (Point)other;
            if(this.x == otherPoint.getX() && this.y == otherPoint.getY())
                return true;
            return false;
        }
    }
}
