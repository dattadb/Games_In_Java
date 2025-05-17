package Games;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class RainbowTargetGame extends JPanel implements ActionListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int TARGET_RADIUS = 50;
    private static final int ARROW_WIDTH = 5;
    private static final int ARROW_HEIGHT = 20;
    private static final int BOW_POSITION = HEIGHT - 100;
    private static final int DELAY = 15;

    private Timer timer;
    private ArrayList<Arrow> arrows;
    private ArrayList<Target> targets;
    private Random random;

    private int score = 0;
    private boolean isRunning = true;

    public RainbowTargetGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.CYAN);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && isRunning) {
                    shootArrow();
                }
            }
        });
        setFocusable(true);

        arrows = new ArrayList<>();
        targets = new ArrayList<>();
        random = new Random();

        timer = new Timer(DELAY, this);
        timer.start();

        spawnTarget();
    }

    private void shootArrow() {
        arrows.add(new Arrow(WIDTH / 2 - ARROW_WIDTH / 2, BOW_POSITION));
    }

    private void spawnTarget() {
        int x = random.nextInt(WIDTH - TARGET_RADIUS * 2) + TARGET_RADIUS;
        targets.add(new Target(x, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGround(g);
        drawBow(g);
        drawArrows(g);
        drawTargets(g);
        drawScore(g);
    }

    private void drawGround(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(0, HEIGHT - 50, WIDTH, 50);
    }

    private void drawBow(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(WIDTH / 2 - 20, BOW_POSITION, 40, 10); // Bow representation
    }

    private void drawArrows(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        for (Arrow arrow : arrows) {
            g.fillRect(arrow.x, arrow.y, ARROW_WIDTH, ARROW_HEIGHT);
        }
    }

    private void drawTargets(Graphics g) {
        for (Target target : targets) {
            int radius = TARGET_RADIUS;
            for (int i = 0; i < 7; i++) { // Draw 7 rings for the rainbow
                g.setColor(getRainbowColor(i));
                g.drawOval(target.x - radius, target.y - radius, radius * 2, radius * 2);
                radius -= 7; // Decrease radius for inner rings
            }
        }
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 30);
    }

    private Color getRainbowColor(int index) {
        Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN};
        return colors[index % colors.length];
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isRunning) {
            updateArrows();
            updateTargets();
            checkCollisions();
            repaint();
        }
    }

    private void updateArrows() {
        for (int i = 0; i < arrows.size(); i++) {
            Arrow arrow = arrows.get(i);
            arrow.y -= 10; // Move the arrow upward

            // Remove the arrow if it goes off-screen
            if (arrow.y + ARROW_HEIGHT < 0) {
                arrows.remove(i);
            }
        }
    }

    private void updateTargets() {
        for (int i = 0; i < targets.size(); i++) {
            Target target = targets.get(i);
            target.y += 2; // Move the target downward

            // Remove the target if it goes off-screen
            if (target.y - TARGET_RADIUS > HEIGHT) {
                targets.remove(i);
                spawnTarget();
            }
        }
    }

    private void checkCollisions() {
        for (int i = 0; i < arrows.size(); i++) {
            Arrow arrow = arrows.get(i);
            for (int j = 0; j < targets.size(); j++) {
                Target target = targets.get(j);
                if (arrow.collidesWith(target)) {
                    arrows.remove(i);
                    targets.remove(j);
                    score += 10;
                    spawnTarget();
                    break;
                }
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
            int centerX = target.x;
            int centerY = target.y;
            int dx = x - centerX;
            int dy = y - centerY;
            int distanceSquared = dx * dx + dy * dy;
            return distanceSquared <= TARGET_RADIUS * TARGET_RADIUS;
        }
    }

    private static class Target {
        int x, y;

        Target(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Rainbow Target Game");
        RainbowTargetGame game = new RainbowTargetGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
