package Games;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class BowAndArrowGame extends JPanel implements ActionListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int DELAY = 15;

    private int bowY = HEIGHT / 2; // Initial position of the bow
    private ArrayList<Arrow> arrows; // List of arrows
    private Target target; // The target
    private Timer timer; // Game timer

    private int score = 0; // Player's score
    private boolean isRunning = true;

    public BowAndArrowGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.CYAN);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isRunning) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP:
                            bowY -= 10; // Move bow up
                            if (bowY < 50) bowY = 50; // Prevent going off-screen
                            break;
                        case KeyEvent.VK_DOWN:
                            bowY += 10; // Move bow down
                            if (bowY > HEIGHT - 100) bowY = HEIGHT - 100; // Prevent going off-screen
                            break;
                        case KeyEvent.VK_SPACE:
                            shootArrow(); // Shoot an arrow
                            break;
                    }
                }
            }
        });
        setFocusable(true);

        arrows = new ArrayList<>();
        target = new Target();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void shootArrow() {
        arrows.add(new Arrow(50, bowY + 20)); // Shoot an arrow from the bow
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGround(g);
        drawBow(g);
        drawArrows(g);
        drawTarget(g);
        drawScore(g);
    }

    private void drawGround(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(0, HEIGHT - 50, WIDTH, 50); // Ground at the bottom
    }

    private void drawBow(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(30, bowY, 10, 60); // Bow representation
    }

    private void drawArrows(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        for (Arrow arrow : arrows) {
            g.fillRect(arrow.x, arrow.y, 20, 5); // Arrow representation
        }
    }

    private void drawTarget(Graphics g) {
        int centerX = target.x + target.radius;
        int centerY = target.y + target.radius;

        for (int i = 0; i < 5; i++) { // Draw concentric circles for the target
            g.setColor(target.getColor(i));
            g.fillOval(centerX - (target.radius - i * 10), centerY - (target.radius - i * 10), 
                       2 * (target.radius - i * 10), 2 * (target.radius - i * 10));
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
            updateArrows();
            updateTarget();
            checkCollisions();
            repaint();
        }
    }

    private void updateArrows() {
        for (int i = 0; i < arrows.size(); i++) {
            Arrow arrow = arrows.get(i);
            arrow.x += 10; // Move the arrow to the right

            // Remove the arrow if it goes off-screen
            if (arrow.x > WIDTH) {
                arrows.remove(i);
            }
        }
    }

    private void updateTarget() {
        target.updatePosition(); // Update the target's position
    }

    private void checkCollisions() {
        for (int i = 0; i < arrows.size(); i++) {
            Arrow arrow = arrows.get(i);
            if (arrow.collidesWith(target)) {
                arrows.remove(i);
                score += 10; // Increase score for a hit
                target.respawn(); // Respawn the target at a new position
                break;
            }
        }
    }

    private static class Arrow {
        int x, y;

        Arrow(int x, int y) {
            this.x = x;
            this.y = y;
        }

        boolean collidesWith(Target target) {
            int centerX = target.x + target.radius;
            int centerY = target.y + target.radius;
            int dx = x - centerX;
            int dy = y - centerY;
            int distanceSquared = dx * dx + dy * dy;
            return distanceSquared <= target.radius * target.radius;
        }
    }

    private static class Target {
        int x, y;
        int radius = 40; // Target radius
        private int direction = 1; // Moving direction (1 for down, -1 for up)

        Target() {
            respawn();
        }

        void updatePosition() {
            y += direction * 2; // Move the target

            // Reverse direction if it hits the top or bottom
            if (y < 50 || y > HEIGHT - 150) {
                direction = -direction;
            }
        }

        void respawn() {
            x = WIDTH - 100; // Fixed horizontal position
            y = (int) (Math.random() * (HEIGHT - 200)) + 50; // Random vertical position
        }

        Color getColor(int layer) {
            Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE};
            return colors[layer % colors.length];
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bow and Arrow Game");
        BowAndArrowGame game = new BowAndArrowGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
