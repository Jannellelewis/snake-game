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
        private static final Color FLOWER_CENTER_COLOR = Color.WHITE;
        private static final Color PETAL_COLOR = Color.PINK;
        private static final Color BG_COLOR = new Color(30, 30, 30);
        private static final Color TEXT_COLOR = Color.WHITE;
        private static final int TIMER_DELAY = 150;
        private static final int SPEED_INCREMENT = 5;
        private static final int MIN_DELAY = 20;
        private static final Color[] PETAL_COLORS = {Color.YELLOW, Color.BLUE, Color.GREEN, Color.ORANGE, new Color(128, 0, 128), Color.CYAN};

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
        private boolean gameStarted;
        private int recentScore;
        private int totalScore;
        private int currentDelay;
        private Color currentPetalColor;
        private final java.util.Random random = new java.util.Random();

        public GamePanel() {
            setBackground(BG_COLOR);
            setFocusable(true);
            recentScore = -1;
            gameStarted = false;
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
            currentDelay = TIMER_DELAY;
            currentPetalColor = PETAL_COLORS[0];
            ladybug.add(new Point(9, 10));  // trail
            ladybug.add(new Point(10, 10)); // head (ladybug)
            spawnFood();
            if (timer != null) {
                timer.setDelay(currentDelay);
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
                                gameStarted = true;
                                timer.start();
                            }
                            break;
                    }
                }
            });
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(java.awt.event.MouseEvent e) {
                    if (!gameStarted && getStartButton().contains(e.getPoint())) {
                        startGame();
                    }
                }
            });
        }

        private void initializeTimer() {
            timer = new Timer(TIMER_DELAY, e -> {
                if (gameStarted && !gameOver) {
                    moveSnake();
                }
            });
        }

        private void startGame() {
            resetGame();
            gameStarted = true;
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
            if (gameOver || !gameStarted) {
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
                currentDelay = Math.max(MIN_DELAY, currentDelay - SPEED_INCREMENT);
                timer.setDelay(currentDelay);
                currentPetalColor = PETAL_COLORS[score % PETAL_COLORS.length];
                spawnFood();
            } else {
                ladybug.remove(0);
            }
            repaint();
        }

        private void endGame() {
            gameOver = true;
            recentScore = score;
            timer.stop();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            if (!gameStarted) {
                drawStartScreen(g2d);
            } else if (gameOver) {
                drawGameOver(g2d);
            } else {
                drawGrid(g2d);
                drawFlower(g2d);
                drawLadybug(g2d);
                drawScore(g2d);
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
            g.setColor(FLOWER_CENTER_COLOR);
            g.fillOval(centerX - centerSize / 2, centerY - centerSize / 2, centerSize, centerSize);

            // Draw petals with changing color
            g.setColor(currentPetalColor);
            g.fillOval(centerX - petalSize / 2, centerY - CELL_SIZE / 2, petalSize, petalSize); // top
            g.fillOval(centerX - petalSize / 2, centerY + CELL_SIZE / 2 - petalSize, petalSize, petalSize); // bottom
            g.fillOval(centerX - CELL_SIZE / 2, centerY - petalSize / 2, petalSize, petalSize); // left
            g.fillOval(centerX + CELL_SIZE / 2 - petalSize, centerY - petalSize / 2, petalSize, petalSize); // right
        }

        private void drawScore(Graphics2D g) {
            g.setColor(TEXT_COLOR);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            String scoreText = "Score: " + score;
            int textWidth = g.getFontMetrics().stringWidth(scoreText);
            g.drawString(scoreText, getWidth() - textWidth - 10, 20);
        }

        private void drawStartScreen(Graphics2D g) {
            int width = getWidth();
            int height = getHeight();

            g.setColor(BG_COLOR);
            g.fillRect(0, 0, width, height);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            String title = "LadyBug in Spring";
            FontMetrics fm = g.getFontMetrics();
            int titleWidth = fm.stringWidth(title);
            g.drawString(title, (width - titleWidth) / 2, height / 3);

            Rectangle button = getStartButton();
            g.setColor(new Color(70, 130, 180));
            g.fillRect(button.x, button.y, button.width, button.height);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            String buttonText = "Start the Game";
            int buttonTextWidth = g.getFontMetrics().stringWidth(buttonText);
            g.drawString(buttonText, button.x + (button.width - buttonTextWidth) / 2, button.y + button.height / 2 + 7);

            g.setFont(new Font("Arial", Font.PLAIN, 18));
            String recentText = "Most recent score: " + (recentScore < 0 ? "N/A" : recentScore);
            int recentTextWidth = g.getFontMetrics().stringWidth(recentText);
            g.drawString(recentText, (width - recentTextWidth) / 2, button.y + button.height + 40);
        }

        private Rectangle getStartButton() {
            int width = 200;
            int height = 50;
            int x = (getWidth() - width) / 2;
            int y = getHeight() / 2;
            return new Rectangle(x, y, width, height);
        }

        private Rectangle getContinueButton() {
            int width = 150;
            int height = 40;
            int x = (getWidth() - width) / 2;
            int y = getHeight() / 2 + 60;
            return new Rectangle(x, y, width, height);
        }

        private Rectangle getAddScoreButton() {
            int width = 250;
            int height = 40;
            int x = (getWidth() - width) / 2;
            int y = getHeight() / 2 + 110;
            return new Rectangle(x, y, width, height);
        }

        private void drawGameOver(Graphics2D g) {
            int width = getWidth();
            int height = getHeight();
            g.setColor(BG_COLOR);
            g.fillRect(0, 0, width, height);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            String message = "YOU DIED";
            FontMetrics fm = g.getFontMetrics();
            int messageWidth = fm.stringWidth(message);
            g.drawString(message, (width - messageWidth) / 2, height / 2 - 20);

            g.setFont(new Font("Arial", Font.PLAIN, 18));
            String scoreMessage = "Score before you died: " + score;
            int scoreWidth = g.getFontMetrics().stringWidth(scoreMessage);
            g.drawString(scoreMessage, (width - scoreWidth) / 2, height / 2 + 10);

            Rectangle continueButton = getContinueButton();
            g.setColor(new Color(34, 139, 34));
            g.fillRect(continueButton.x, continueButton.y, continueButton.width, continueButton.height);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            String continueText = "Continue Playing";
            int continueTextWidth = g.getFontMetrics().stringWidth(continueText);
            g.drawString(continueText, continueButton.x + (continueButton.width - continueTextWidth) / 2, continueButton.y + continueButton.height / 2 + 6);

            Rectangle addButton = getAddScoreButton();
            g.setColor(new Color(70, 130, 180));
            g.fillRect(addButton.x, addButton.y, addButton.width, addButton.height);
            g.setColor(Color.WHITE);
            String addText = "Add Current Score to Total Score";
            int addTextWidth = g.getFontMetrics().stringWidth(addText);
            g.drawString(addText, addButton.x + (addButton.width - addTextWidth) / 2, addButton.y + addButton.height / 2 + 6);
        }
    }
}
