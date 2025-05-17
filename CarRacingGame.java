package Games;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class CarRacingGame extends JPanel implements ActionListener, KeyListener {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;
    private static final int ROAD_WIDTH = 200;

    private Timer timer;
    private int playerX = WIDTH / 2 - 25; // Player car X position
    private int playerY = HEIGHT - 100; // Player car Y position
    private int carWidth = 50;
    private int carHeight = 50;
    private int speed = 5;

    private ArrayList<Rectangle> obstacles = new ArrayList<>();
    private Random random = new Random();

    private int score = 0;
    private boolean gameOver = false;

    public CarRacingGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.GRAY);
        timer = new Timer(20, this);
        timer.start();
        addKeyListener(this);
        setFocusable(true);
        generateObstacles();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw road
        g.setColor(Color.DARK_GRAY);
        g.fillRect(WIDTH / 2 - ROAD_WIDTH / 2, 0, ROAD_WIDTH, HEIGHT);

        // Draw lane lines
        g.setColor(Color.WHITE);
        for (int i = 0; i < HEIGHT; i += 50) {
            g.fillRect(WIDTH / 2 - 2, i, 4, 25);
        }

        // Draw player's car
        g.setColor(Color.RED);
        g.fillRect(playerX, playerY, carWidth, carHeight);

        // Draw obstacles
        g.setColor(Color.BLUE);
        for (Rectangle obstacle : obstacles) {
            g.fillRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        }

        // Draw score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 20);

        // Draw game over message
        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.setColor(Color.RED);
            g.drawString("GAME OVER", WIDTH / 2 - 100, HEIGHT / 2);
            g.drawString("Score: " + score, WIDTH / 2 - 80, HEIGHT / 2 + 50);
        }
    }

    private void generateObstacles() {
        for (int i = 0; i < 3; i++) {
            int obstacleX = WIDTH / 2 - ROAD_WIDTH / 2 + random.nextInt(ROAD_WIDTH - carWidth);
            int obstacleY = -carHeight - i * 200; // Spread obstacles vertically
            obstacles.add(new Rectangle(obstacleX, obstacleY, carWidth, carHeight));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            // Move obstacles
            for (Rectangle obstacle : obstacles) {
                obstacle.y += speed;
                if (obstacle.y > HEIGHT) {
                    obstacle.y = -carHeight;
                    obstacle.x = WIDTH / 2 - ROAD_WIDTH / 2 + random.nextInt(ROAD_WIDTH - carWidth);
                    score++;
                }
                // Check collision
                if (obstacle.intersects(new Rectangle(playerX, playerY, carWidth, carHeight))) {
                    gameOver = true;
                    timer.stop();
                }
            }
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT && playerX > WIDTH / 2 - ROAD_WIDTH / 2) {
                playerX -= 10;
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && playerX < WIDTH / 2 + ROAD_WIDTH / 2 - carWidth) {
                playerX += 10;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Car Racing Game");
        CarRacingGame game = new CarRacingGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
