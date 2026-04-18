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
        private static final Color LADYBUG_COLOR = Color.RED;
        private static final Color TRAIL_COLOR = Color.WHITE;
        private static final Color FLOWER_COLOR = Color.YELLOW;
        private static final Color PETAL_COLOR = Color.PINK;
        private static final Color BG_COLOR = new Color(30, 30, 30);
        private static final Color TEXT_COLOR = Color.WHITE;
        private static final int TIMER_DELAY = 150;
        private static final Color[] FLOWER_COLORS = {Color.YELLOW, Color.BLUE, Color.GREEN, Color.ORANGE, new Color(128, 0, 128), Color.CYAN};

        private enum Direction {
            UP, DOWN, LEFT, RIGHT
        }

        private List<Point> ladybug;
        private Direction currentDirection;
        private Direction nextDirection;
        private Timer timer;
        private Point food;
        private int score;
        private boolean gameOver;
        private Color currentFlowerColor;
        private final java.util.Random random = new java.util.Random();

        public GamePanel() {
            setBackground(BG_COLOR);
            setFocusable(true);
            initializeControls();
            initializeTimer();
            resetGame();
        }

        @Override
        public void addNotify() {
            super.addNotify();
            requestFocusInWindow();
        }

        private void resetGame() {
            ladybug = new ArrayList<>();
            currentDirection = Direction.RIGHT;
            nextDirection = Direction.RIGHT;
            score = 0;
            gameOver = false;
            currentFlowerColor = Color.YELLOW;
            ladybug.add(new Point(9, 10));  // trail
            ladybug.add(new Point(10, 10)); // head (ladybug)
            spawnFood();
            if (timer != null) {
                timer.restart();
            }
            requestFocusInWindow();
            repaint();
        }

        private void initializeControls() {
            addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyPressed(java.awt.event.KeyEvent e) {
                    switch (e.getKeyCode()) {
                        case java.awt.event.KeyEvent.VK_UP:
                            if (currentDirection != Direction.DOWN) {
                                nextDirection = Direction.UP;
                            }
                            break;
                        case java.awt.event.KeyEvent.VK_DOWN:
                            if (currentDirection != Direction.UP) {
                                nextDirection = Direction.DOWN;
                            }
                            break;
                        case java.awt.event.KeyEvent.VK_LEFT:
                            if (currentDirection != Direction.RIGHT) {
                                nextDirection = Direction.LEFT;
                            }
                            break;
                        case java.awt.event.KeyEvent.VK_RIGHT:
                            if (currentDirection != Direction.LEFT) {
                                nextDirection = Direction.RIGHT;
                            }
                            break;
                        case java.awt.event.KeyEvent.VK_R:
                            if (gameOver) {
                                resetGame();
                            }
                            break;
                    }
                }
            });
        }

        private void initializeTimer() {
            timer = new Timer(TIMER_DELAY, e -> moveSnake());
            timer.start();
        }

        private void spawnFood() {
            int cols = getWidth() / CELL_SIZE;
            int rows = getHeight() / CELL_SIZE;
            if (cols <= 0 || rows <= 0) {
                cols = 20;
                rows = 20;
            }

            boolean[][] occupied = new boolean[cols][rows];
            for (Point segment : ladybug) {
                if (segment.x >= 0 && segment.x < cols && segment.y >= 0 && segment.y < rows) {
                    occupied[segment.x][segment.y] = true;
                }
            }

            int freeCells = cols * rows - ladybug.size();
            if (freeCells <= 0) {
                food = null;
                return;
            }

            int target = random.nextInt(freeCells);
            for (int x = 0; x < cols; x++) {
                for (int y = 0; y < rows; y++) {
                    if (occupied[x][y]) {
                        continue;
                    }
                    if (target == 0) {
                        food = new Point(x, y);
                        return;
                    }
                    target--;
                }
            }
        }

        private void moveSnake() {
            if (gameOver) {
                return;
            }

            currentDirection = nextDirection;
            Point head = ladybug.get(ladybug.size() - 1);
            int cols = getWidth() / CELL_SIZE;
            int rows = getHeight() / CELL_SIZE;
            int newX = head.x;
            int newY = head.y;

            switch (currentDirection) {
                case UP -> newY -= 1;
                case DOWN -> newY += 1;
                case LEFT -> newX -= 1;
                case RIGHT -> newX += 1;
            }

            if (newX < 0 || newX >= cols || newY < 0 || newY >= rows) {
                endGame();
                return;
            }

            Point newHead = new Point(newX, newY);
            boolean eating = food != null && newHead.equals(food);
            boolean collision;
            if (eating) {
                collision = ladybug.contains(newHead);
            } else {
                collision = ladybug.subList(1, ladybug.size()).contains(newHead);
            }

            if (collision) {
                endGame();
                return;
            }

            ladybug.add(newHead);
            if (eating) {
                score += 1;
                currentFlowerColor = FLOWER_COLORS[score % FLOWER_COLORS.length];
                spawnFood();
            } else {
                ladybug.remove(0);
            }
            repaint();
        }

        private void endGame() {
            gameOver = true;
            timer.stop();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            
            drawGrid(g2d);
            drawFlower(g2d);
            drawLadybug(g2d);
            drawScore(g2d);
            if (gameOver) {
                drawGameOver(g2d);
            }
        }

        private void drawGrid(Graphics2D g) {
            g.setColor(GRID_COLOR);
            int rows = getHeight() / CELL_SIZE;
            int cols = getWidth() / CELL_SIZE;
            
            for (int i = 0; i <= cols; i++) {
                int x = i * CELL_SIZE;
                g.drawLine(x, 0, x, getHeight());
            }
            
            for (int i = 0; i <= rows; i++) {
                int y = i * CELL_SIZE;
                g.drawLine(0, y, getWidth(), y);
            }
        }

        private void drawLadybug(Graphics2D g) {
            for (int i = 0; i < ladybug.size(); i++) {
                Point segment = ladybug.get(i);
                int x = segment.x * CELL_SIZE;
                int y = segment.y * CELL_SIZE;
                if (i == ladybug.size() - 1) { // head
                    g.setColor(LADYBUG_COLOR);
                    g.fillOval(x + 1, y + 1, CELL_SIZE - 2, CELL_SIZE - 2);
                } else { // trail - white dotted lines
                    g.setColor(TRAIL_COLOR);
                    int dotSize = CELL_SIZE / 4;
                    g.fillOval(x + CELL_SIZE / 2 - dotSize / 2, y + CELL_SIZE / 2 - dotSize / 2, dotSize, dotSize);
                }
            }
        }

        private void drawFlower(Graphics2D g) {
            if (food == null) {
                return;
            }
            int x = food.x * CELL_SIZE;
            int y = food.y * CELL_SIZE;
            int centerX = x + CELL_SIZE / 2;
            int centerY = y + CELL_SIZE / 2;
            int petalSize = CELL_SIZE / 4;
            int centerSize = CELL_SIZE / 6;

            // Draw center
            g.setColor(currentFlowerColor);
            g.fillOval(centerX - centerSize / 2, centerY - centerSize / 2, centerSize, centerSize);

            // Draw petals
            g.setColor(PETAL_COLOR);
            g.fillOval(centerX - petalSize / 2, centerY - CELL_SIZE / 2, petalSize, petalSize); // top
            g.fillOval(centerX - petalSize / 2, centerY + CELL_SIZE / 2 - petalSize, petalSize, petalSize); // bottom
            g.fillOval(centerX - CELL_SIZE / 2, centerY - petalSize / 2, petalSize, petalSize); // left
            g.fillOval(centerX + CELL_SIZE / 2 - petalSize, centerY - petalSize / 2, petalSize, petalSize); // right
        }

        private void drawScore(Graphics2D g) {
            g.setColor(TEXT_COLOR);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Score: " + score, 10, 20);
        }

        private void drawGameOver(Graphics2D g) {
            int width = getWidth();
            int height = getHeight();
            g.setColor(new Color(0, 0, 0, 170));
            g.fillRect(0, 0, width, height);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            String message = "Game Over";
            FontMetrics fm = g.getFontMetrics();
            int messageWidth = fm.stringWidth(message);
            g.drawString(message, (width - messageWidth) / 2, height / 2 - 20);

            g.setFont(new Font("Arial", Font.PLAIN, 18));
            String scoreMessage = "Final Score: " + score;
            int scoreWidth = g.getFontMetrics().stringWidth(scoreMessage);
            g.drawString(scoreMessage, (width - scoreWidth) / 2, height / 2 + 10);

            String restartMessage = "Press R to restart";
            int restartWidth = g.getFontMetrics().stringWidth(restartMessage);
            g.drawString(restartMessage, (width - restartWidth) / 2, height / 2 + 40);
        }
    }
}
