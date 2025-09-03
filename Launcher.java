import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Launcher extends JFrame {
    
    public Launcher() {
        setupLauncher();
    }
    
    private void setupLauncher() {
        setTitle("Air Management System - Launcher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(248, 249, 250));
        
        // Header
        JLabel headerLabel = new JLabel("✈️ Air Management System");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setForeground(new Color(25, 118, 210));
        
        JLabel subtitleLabel = new JLabel("Choose your preferred interface");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setForeground(new Color(100, 100, 100));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(headerLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        
        // GUI Button
        JButton guiButton = createStyledButton("🖥️ Graphical User Interface", new Color(76, 175, 80));
        guiButton.addActionListener(e -> launchGUI());
        
        // Console Button
        JButton consoleButton = createStyledButton("💻 Console Interface", new Color(25, 118, 210));
        consoleButton.addActionListener(e -> launchConsole());
        
        buttonsPanel.add(guiButton);
        buttonsPanel.add(consoleButton);
        
        mainPanel.add(buttonsPanel, BorderLayout.CENTER);
        
        // Info panel
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
        
        JTextArea infoText = new JTextArea(
            "• GUI Interface: Modern graphical interface with tabs and tables\n" +
            "• Console Interface: Traditional command-line interface\n" +
            "• Both interfaces use the same data and functionality\n" +
            "• Data is automatically saved between sessions"
        );
        infoText.setEditable(false);
        infoText.setBackground(new Color(248, 249, 250));
        infoText.setFont(new Font("Arial", Font.PLAIN, 11));
        infoText.setLineWrap(true);
        infoText.setWrapStyleWord(true);
        
        infoPanel.add(infoText, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        button.setFocusPainted(false);
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.brighter());
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
        
        return button;
    }
    
    private void launchGUI() {
        dispose(); // Close launcher
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Show splash screen
            SplashScreen splash = new SplashScreen();
            splash.showSplash();
            
            // Wait a bit then show main window
            Timer timer = new Timer(2500, e -> {
                new AirlineSystemGUI().setVisible(true);
            });
            timer.setRepeats(false);
            timer.start();
        });
    }
    
    private void launchConsole() {
        dispose(); // Close launcher
        SwingUtilities.invokeLater(() -> {
            // Run console version in a separate thread
            new Thread(() -> {
                AirlineSystem.main(new String[0]);
            }).start();
        });
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Launcher().setVisible(true);
        });
    }
} 