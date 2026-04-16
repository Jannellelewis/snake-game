import javax.swing.*;
import java.awt.*;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class SnakeGame extends JFrame {
    private GamePanel gamePanel;

    public SnakeGame() {
        setTitle("Snake");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        gamePanel = new GamePanel();
        add(gamePanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SnakeGame frame = new SnakeGame();
            frame.setVisible(true);
        });
    }

    private static class GamePanel extends JPanel {
        private static final int CELL_SIZE = 30;
        private static final Color GRID_COLOR = new Color(60, 60, 60);
        private static final Color SNAKE_COLOR = Color.GREEN;
        private static final Color BG_COLOR = new Color(30, 30, 30);
        
        private List<Point> snake;

        public GamePanel() {
            setBackground(BG_COLOR);
            initializeSnake();
        }

        private void initializeSnake() {
            snake = new ArrayList<>();
            // Starting position: 3 segments near center, facing right
            snake.add(new Point(8, 10));  // tail
            snake.add(new Point(9, 10));  // middle
            snake.add(new Point(10, 10)); // head
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            
            drawGrid(g2d);
            drawSnake(g2d);
        }

        private void drawGrid(Graphics2D g) {
            g.setColor(GRID_COLOR);
            int rows = getHeight() / CELL_SIZE;
            int cols = getWidth() / CELL_SIZE;
            
            // Draw vertical lines
            for (int i = 0; i <= cols; i++) {
                int x = i * CELL_SIZE;
                g.drawLine(x, 0, x, getHeight());
            }
            
            // Draw horizontal lines
            for (int i = 0; i <= rows; i++) {
                int y = i * CELL_SIZE;
                g.drawLine(0, y, getWidth(), y);
            }
        }

        private void drawSnake(Graphics2D g) {
            g.setColor(SNAKE_COLOR);
            for (Point segment : snake) {
                int x = segment.x * CELL_SIZE;
                int y = segment.y * CELL_SIZE;
                g.fillRect(x + 1, y + 1, CELL_SIZE - 2, CELL_SIZE - 2);
            }
        }
    }
}
