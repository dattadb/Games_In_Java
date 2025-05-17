package Games;


	
	import javax.swing.*;
	import java.awt.*;
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;

	public class FlyingKite extends JPanel implements ActionListener {
	    private static final int WIDTH = 800; // Window width
	    private static final int HEIGHT = 600; // Window height
	    private static final int DELAY = 10; // Delay in milliseconds

	    private int kiteX = WIDTH / 2; // Initial kite X position
	    private int kiteY = HEIGHT / 2; // Initial kite Y position
	    private int xDirection = 2; // X direction speed
	    private int yDirection = 2; // Y direction speed

	    public FlyingKite() {
	        setPreferredSize(new Dimension(WIDTH, HEIGHT));
	        setBackground(Color.CYAN);
	        Timer timer = new Timer(DELAY, this);
	        timer.start();
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        drawKite(g);
	    }

	    private void drawKite(Graphics g) {
	        // Draw the kite
	        int[] xPoints = {kiteX, kiteX + 20, kiteX, kiteX - 20}; // Kite diamond X points
	        int[] yPoints = {kiteY - 20, kiteY, kiteY + 20, kiteY}; // Kite diamond Y points
	        g.setColor(Color.RED);
	        g.fillPolygon(xPoints, yPoints, 4);

	        // Draw the kite string
	        g.setColor(Color.BLACK);
	        g.drawLine(kiteX, kiteY + 20, kiteX, kiteY + 100);

	        // Draw the bows on the string
	        for (int i = 0; i <= 5; i++) {
	            int stringY = kiteY + 20 + (i * 15);
	            g.drawLine(kiteX - 5, stringY, kiteX + 5, stringY + 5);
	            g.drawLine(kiteX + 5, stringY, kiteX - 5, stringY + 5);
	        }
	    }

	    @Override
	    public void actionPerformed(ActionEvent e) {
	        // Update kite position
	        kiteX += xDirection;
	        kiteY += yDirection;

	        // Check for boundaries and reverse direction if necessary
	        if (kiteX <= 20 || kiteX >= WIDTH - 20) xDirection = -xDirection;
	        if (kiteY <= 20 || kiteY >= HEIGHT - 20) yDirection = -yDirection;

	        // Redraw the panel
	        repaint();
	    }

	    public static void main(String[] args) {
	        JFrame frame = new JFrame("Flying Kite");
	        FlyingKite kite = new FlyingKite();
	        frame.add(kite);
	        frame.pack();
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
	    }
	}



