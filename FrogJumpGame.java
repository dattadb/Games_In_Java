package Games;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class FrogJumpGame extends JPanel implements ActionListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 400;
    private static final int GROUND_LEVEL = HEIGHT - 50;
    private static final int FROG_SIZE = 40;
    private static final int OBSTACLE_WIDTH = 30;
    private static final int OBSTACLE_HEIGHT = 50;
    private static final int DELAY = 15;

    private int frogX = 100;
    private int frogY = GROUND_LEVEL - FROG_SIZE;
    private int jumpVelocity = 0;
    private boolean inAir = false;

    private Timer timer;
    private ArrayList<Rectangle> obstacles;
    private int obstacleSpeed = 5;
    private Random random;

    private boolean isRunning = true;
    private int score = 0;

    public FrogJumpGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.CYAN);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && !inAir) {
                    jumpVelocity = -15;
                    inAir = true;
                }
            }
        });
        setFocusable(true);

        obstacles = new ArrayList<>();
        random = new Random();

        timer = new Timer(DELAY, this);
        timer.start();

        spawnObstacle();
    }

    private void spawnObstacle() {
        int x = WIDTH + random.nextInt(300); // Random distance between obstacles
        obstacles.add(new Rectangle(x, GROUND_LEVEL - OBSTACLE_HEIGHT, OBSTACLE_WIDTH, OBSTACLE_HEIGHT));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGround(g);
        drawFrog(g);
        drawObstacles(g);
        drawScore(g);
    }

    private void drawGround(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(0, GROUND_LEVEL, WIDTH, HEIGHT - GROUND_LEVEL);
    }

////    private void drawFrog(Graphics g) {
//        g.setColor(Color.ORANGE);
//        g.fillRect(frogX, frogY, FROG_SIZE, FROG_SIZE);
//    }

    private void drawFrog(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillOval(frogX, frogY, FROG_SIZE, FROG_SIZE); // Draw the circular frog
    }

    private void drawObstacles(Graphics g) {
        g.setColor(Color.RED);
        for (Rectangle obstacle : obstacles) {
            g.fillRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        }
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 30);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isRunning) {
            updateFrog();
            updateObstacles();
            checkCollisions();
            repaint();
        }
    }

    private void updateFrog() {
        // Apply gravity
        if (inAir) {
            frogY += jumpVelocity;
            jumpVelocity += 1; // Gravity effect
        }

        // Stop jump if hitting the ground
        if (frogY >= GROUND_LEVEL - FROG_SIZE) {
            frogY = GROUND_LEVEL - FROG_SIZE;
            inAir = false;
        }
    }

    private void updateObstacles() {
        for (int i = 0; i < obstacles.size(); i++) {
            Rectangle obstacle = obstacles.get(i);
            obstacle.x -= obstacleSpeed;

            // Remove obstacles that move off-screen
            if (obstacle.x + OBSTACLE_WIDTH < 0) {
                obstacles.remove(i);
                score++;
                spawnObstacle();
            }
        }
    }

    private void checkCollisions() {
        for (Rectangle obstacle : obstacles) {
            if (obstacle.intersects(new Rectangle(frogX, frogY, FROG_SIZE, FROG_SIZE))) {
                isRunning = false;
                timer.stop();
                JOptionPane.showMessageDialog(this, "Game Over! Your Score: " + score, "Game Over", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Frog Jump Game");
        FrogJumpGame game = new FrogJumpGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
