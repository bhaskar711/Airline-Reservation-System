import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SplashScreen extends JWindow {
    private JProgressBar progressBar;
    private JLabel statusLabel;
    
    public SplashScreen() {
        createSplashScreen();
    }
    
    private void createSplashScreen() {
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(25, 118, 210));
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(21, 101, 192), 2));
        
        // Create content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        // Title and subtitle
        JLabel titleLabel = new JLabel("✈️ Air Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel subtitleLabel = new JLabel("Flight Reservation and Management System");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(200, 200, 200));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        contentPanel.add(titlePanel, BorderLayout.CENTER);
        
        // Progress bar and status
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setOpaque(false);
        progressPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString("Loading...");
        progressBar.setFont(new Font("Arial", Font.BOLD, 12));
        progressBar.setForeground(new Color(76, 175, 80));
        progressBar.setBackground(new Color(200, 200, 200));
        progressBar.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        statusLabel = new JLabel("Initializing system...");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        progressPanel.add(progressBar, BorderLayout.CENTER);
        progressPanel.add(statusLabel, BorderLayout.SOUTH);
        
        contentPanel.add(progressPanel, BorderLayout.SOUTH);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
        
        // Set window properties
        setSize(500, 300);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
    }
    
    public void updateProgress(int progress, String status) {
        progressBar.setValue(progress);
        statusLabel.setText(status);
    }
    
    public void showSplash() {
        setVisible(true);
        
        // Simulate loading process
        Timer timer = new Timer(50, new ActionListener() {
            int progress = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                progress += 2;
                
                if (progress <= 20) {
                    updateProgress(progress, "Loading flight data...");
                } else if (progress <= 40) {
                    updateProgress(progress, "Loading reservation data...");
                } else if (progress <= 60) {
                    updateProgress(progress, "Initializing user interface...");
                } else if (progress <= 80) {
                    updateProgress(progress, "Setting up database connections...");
                } else if (progress <= 100) {
                    updateProgress(progress, "Starting application...");
                }
                
                if (progress >= 100) {
                    ((Timer) e.getSource()).stop();
                    dispose();
                }
            }
        });
        timer.start();
    }
} 