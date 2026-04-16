import javax.swing.*;

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
        public GamePanel() {
            setBackground(java.awt.Color.BLACK);
        }
    }
}
