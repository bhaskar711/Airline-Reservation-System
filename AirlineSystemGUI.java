import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AirlineSystemGUI extends JFrame {
    private List<Flight> flights;
    private List<Reservation> reservations;
    private JTabbedPane tabbedPane;
    private JTable flightsTable;
    private JTable reservationsTable;
    private DefaultTableModel flightsTableModel;
    private DefaultTableModel reservationsTableModel;
    private JLabel statusLabel;
    

    
    // Form fields for cancellation
    private JTextField cancelReservationIdField;
    private JTextArea reservationDetailsArea;
    private JButton cancelReservationButton;
    private FlightSearchPanel searchPanel;
    
    private JLabel flightsLabel;
    private JLabel reservationsLabel;
    private JLabel availableSeatsLabel;
    
    public AirlineSystemGUI() {
        initializeData();
        setupGUI();
        loadData();
        updateTables();
    }
    
    private void initializeData() {
        flights = DataManager.loadFlights();
        reservations = DataManager.loadReservations(flights);
        searchPanel = new FlightSearchPanel(flights, reservations);
    }
    
    private void setupGUI() {
        setTitle("Air Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Create main container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Flights", createFlightsPanel());
        tabbedPane.addTab("Search & Book", createSearchPanel());
        tabbedPane.addTab("Reservations", createReservationsPanel());
        tabbedPane.addTab("Cancel Booking", createCancellationPanel());
        tabbedPane.addTab("Reports", createReportsPanel());
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Create status bar
        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createEtchedBorder());
        mainPanel.add(statusLabel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
        
        // Add window listener for saving data on exit
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveData();
            }
        });
        
        // Update flights with real-time data after GUI is set up
        SwingUtilities.invokeLater(() -> {
            updateFlightsWithRealTimeData();
        });
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(25, 118, 210));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("✈️ Air Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Flight Reservation and Management");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(200, 200, 200));
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        panel.add(titlePanel, BorderLayout.WEST);
        
        // Add refresh button
        JButton refreshButton = new JButton("🔄 Refresh");
        refreshButton.setBackground(new Color(76, 175, 80));
        refreshButton.setForeground(Color.BLACK);
        refreshButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        refreshButton.addActionListener(e -> refreshData());
        
        // Add real-time update button
        JButton realTimeButton = new JButton("✈️ Update Flights");
        realTimeButton.setBackground(new Color(255, 152, 0));
        realTimeButton.setForeground(Color.BLACK);
        realTimeButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        realTimeButton.addActionListener(e -> updateFlightsWithRealTimeData());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(realTimeButton);
        buttonPanel.add(refreshButton);
        
        panel.add(buttonPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createFlightsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Create table
        String[] columns = {"Flight #", "From", "To", "Departure", "Available Seats", "Total Seats", "Price ($)", "Occupancy (%)"};
        flightsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        flightsTable = new JTable(flightsTableModel);
        flightsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        flightsTable.getTableHeader().setReorderingAllowed(false);
        
        // Style the table
        flightsTable.setRowHeight(30);
        flightsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        flightsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        flightsTable.getTableHeader().setBackground(new Color(240, 240, 240));
        flightsTable.setDefaultRenderer(Object.class, new TableRenderer());
        
        JScrollPane scrollPane = new JScrollPane(flightsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Available Flights"));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add summary panel
        JPanel summaryPanel = createSummaryPanel();
        panel.add(summaryPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("System Summary"));
        panel.setBackground(new Color(248, 249, 250));
        
        flightsLabel = new JLabel("Total Flights: " + flights.size());
        reservationsLabel = new JLabel("Total Reservations: " + getTotalReservationsCount());
        availableSeatsLabel = new JLabel("Total Available Seats: " + getTotalAvailableSeats());
        
        flightsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        reservationsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        availableSeatsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        panel.add(flightsLabel);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(reservationsLabel);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(availableSeatsLabel);
        
        return panel;
    }
    
    private void updateSummaryPanel() {
        if (flightsLabel != null) flightsLabel.setText("Total Flights: " + flights.size());
        if (reservationsLabel != null) reservationsLabel.setText("Total Reservations: " + getTotalReservationsCount());
        if (availableSeatsLabel != null) availableSeatsLabel.setText("Total Available Seats: " + getTotalAvailableSeats());
    }
    

    
    private JPanel createReservationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Create table
        String[] columns = {"Reservation ID", "Passenger Name", "Flight #", "Destination", "Status", "Booking Date"};
        reservationsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        reservationsTable = new JTable(reservationsTableModel);
        reservationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reservationsTable.getTableHeader().setReorderingAllowed(false);
        
        // Style the table
        reservationsTable.setRowHeight(30);
        reservationsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        reservationsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        reservationsTable.getTableHeader().setBackground(new Color(240, 240, 240));
        reservationsTable.setName("reservationsTable");
        reservationsTable.setDefaultRenderer(Object.class, new TableRenderer());
        
        JScrollPane scrollPane = new JScrollPane(reservationsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("All Reservations"));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSearchPanel() {
        return searchPanel;
    }
    
    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create main content panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createTitledBorder("Generate Comprehensive Report"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Generate Comprehensive Report Button
        JButton generateReportButton = new JButton("📈 Generate Comprehensive Report");
        generateReportButton.setBackground(new Color(156, 39, 176));
        generateReportButton.setForeground(Color.BLACK);
        generateReportButton.setFont(new Font("Arial", Font.BOLD, 16));
        generateReportButton.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        generateReportButton.addActionListener(e -> generateComprehensiveReport());
        
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(generateReportButton, gbc);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        // Add information panel
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("📁 Report Information"));
        infoPanel.setBackground(new Color(248, 249, 250));
        
        JTextArea infoText = new JTextArea(
            "Comprehensive Report Features:\n\n" +
            "📊 Summary Section: Total, active, and cancelled reservation counts\n" +
            "📋 Detailed Booking History: Complete booking records with timestamps\n" +
            "❌ Detailed Cancellation History: Complete cancellation records with reasons\n" +
            "📈 Performance Analytics: System usage statistics\n\n" +
            "File Format: CSV (compatible with Excel)\n" +
            "File Name: airline_management_report.csv\n" +
            "Location: Same directory as the application\n\n" +
            "The report includes all booking and cancellation data for complete audit trail."
        );
        infoText.setEditable(false);
        infoText.setBackground(new Color(248, 249, 250));
        infoText.setFont(new Font("Arial", Font.PLAIN, 12));
        
        infoPanel.add(infoText, BorderLayout.CENTER);
        panel.add(infoPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createCancellationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Cancel a Reservation"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Reservation ID for cancellation
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Reservation ID:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        cancelReservationIdField = new JTextField(20);
        formPanel.add(cancelReservationIdField, gbc);
        
        // Search button
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0.0;
        
        JButton searchButton = new JButton("🔍 Search Reservation");
        searchButton.setBackground(new Color(25, 118, 210));
        searchButton.setForeground(Color.BLACK);
        searchButton.setFont(new Font("Arial", Font.BOLD, 12));
        searchButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        searchButton.addActionListener(e -> searchReservation());
        formPanel.add(searchButton, gbc);
        
        // Reservation details area
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 5, 5, 5);
        
        reservationDetailsArea = new JTextArea();
        reservationDetailsArea.setEditable(false);
        reservationDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        reservationDetailsArea.setBackground(new Color(248, 249, 250));
        reservationDetailsArea.setText("Enter Reservation ID and click 'Search Reservation' to view details.");
        
        JScrollPane scrollPane = new JScrollPane(reservationDetailsArea);
        scrollPane.setPreferredSize(new Dimension(400, 150));
        formPanel.add(scrollPane, gbc);
        
        // Cancel button
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(10, 5, 5, 5);
        
        cancelReservationButton = new JButton("❌ Cancel Reservation");
        cancelReservationButton.setBackground(new Color(244, 67, 54));
        cancelReservationButton.setForeground(Color.BLACK);
        cancelReservationButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelReservationButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        cancelReservationButton.setEnabled(false);
        cancelReservationButton.addActionListener(e -> cancelReservation());
        formPanel.add(cancelReservationButton, gbc);
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        // Add warning panel
        JPanel warningPanel = new JPanel(new BorderLayout());
        warningPanel.setBorder(BorderFactory.createTitledBorder("⚠️ Important Notice"));
        warningPanel.setBackground(new Color(255, 248, 225));
        
        JTextArea warning = new JTextArea(
            "• Enter the exact Reservation ID (e.g., RES-ABC12345)\n" +
            "• Cancellation will immediately free up the seat for other passengers\n" +
            "• Cancelled reservations cannot be restored\n" +
            "• You will be asked to confirm cancellation and provide a reason\n" +
            "• The system will automatically update seat availability"
        );
        warning.setEditable(false);
        warning.setBackground(new Color(255, 248, 225));
        warning.setFont(new Font("Arial", Font.PLAIN, 12));
        
        warningPanel.add(warning, BorderLayout.CENTER);
        panel.add(warningPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void loadData() {
        // Data is loaded in initializeData()
    }
    
    private void updateTables() {
        // Update flights table
        flightsTableModel.setRowCount(0);
        for (Flight flight : flights) {
            Object[] row = {
                flight.getFlightNumber(),
                flight.getDeparture(),
                flight.getDesignation(),
                flight.getDepartureTime().format(DateTimeFormatter.ofPattern("MMM dd, HH:mm")),
                flight.getAvailableSeats(),
                flight.getTotalSeats(),
                String.format("$%.2f", flight.getPrice()),
                String.format("%.1f%%", flight.getOccupancyRate())
            };
            flightsTableModel.addRow(row);
        }
        
        // Update reservations table
        reservationsTableModel.setRowCount(0);
        for (Reservation reservation : reservations) {
            Object[] row = {
                reservation.getReservationId(),
                reservation.getName(),
                reservation.getFlight().getFlightNumber(),
                reservation.getFlight().getDesignation(),
                reservation.getStatus().toString(),
                reservation.getBookingTime().format(DateTimeFormatter.ofPattern("MMM dd, HH:mm"))
            };
            reservationsTableModel.addRow(row);
        }
    }
    

    
    private Reservation currentReservationToCancel = null;
    
    private void searchReservation() {
        String reservationId = cancelReservationIdField.getText().trim();
        
        if (reservationId.isEmpty()) {
            showError("Please enter Reservation ID.");
            return;
        }
        
        // Find reservation by ID
        Reservation foundReservation = null;
        for (Reservation reservation : reservations) {
            if (reservation.getReservationId().equalsIgnoreCase(reservationId) && reservation.isActive()) {
                foundReservation = reservation;
                break;
            }
        }
        
        if (foundReservation == null) {
            reservationDetailsArea.setText("❌ Reservation not found!\n\n" +
                "Reservation ID: " + reservationId + "\n" +
                "Possible reasons:\n" +
                "• ID is incorrect\n" +
                "• Reservation doesn't exist\n\n" +
                "Please check the Reservation ID and try again.");
            currentReservationToCancel = null;
            cancelReservationButton.setEnabled(false);
            return;
        }
        
        // Display reservation details
        currentReservationToCancel = foundReservation;
        displayReservationDetails(foundReservation);
        
        // Enable cancel button
        cancelReservationButton.setEnabled(true);
    }
    
    private void displayReservationDetails(Reservation reservation) {
        StringBuilder details = new StringBuilder();
        details.append("✅ RESERVATION FOUND\n");
        details.append("═══════════════════════════════════════\n\n");
        details.append("📋 Reservation Details:\n");
        details.append("• Reservation ID: ").append(reservation.getReservationId()).append("\n");
        details.append("• Passenger Name: ").append(reservation.getName()).append("\n");
        details.append("• Status: ").append(reservation.getStatus()).append("\n");
        details.append("• Booking Date: ").append(reservation.getBookingTime().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss"))).append("\n\n");
        
        details.append("✈️ Flight Details:\n");
        details.append("• Flight Number: ").append(reservation.getFlight().getFlightNumber()).append("\n");
        details.append("• Route: ").append(reservation.getFlight().getDeparture()).append(" → ").append(reservation.getFlight().getDesignation()).append("\n");
        details.append("• Departure: ").append(reservation.getFlight().getDepartureTime().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"))).append("\n");
        details.append("• Price: $").append(String.format("%.2f", reservation.getFlight().getPrice())).append("\n\n");
        
        details.append("⚠️ Click 'Cancel Reservation' to proceed with cancellation.");
        
        reservationDetailsArea.setText(details.toString());
    }
    
    private void cancelReservation() {
        if (currentReservationToCancel == null) {
            showError("Please search for a reservation first.");
            return;
        }
        
        // Create custom dialog with reason selection and confirmation
        JDialog dialog = new JDialog(this, "Cancel Reservation", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        
        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(244, 67, 54));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel headerLabel = new JLabel("⚠️ Confirm Reservation Cancellation");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        
        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Reason selection panel
        JPanel reasonPanel = new JPanel(new BorderLayout());
        reasonPanel.setBorder(BorderFactory.createTitledBorder("Cancellation Reason"));
        
        String[] reasons = {
            "Change of plans",
            "Found better price",
            "Schedule conflict",
            "Personal emergency",
            "Weather concerns",
            "Other"
        };
        
        JComboBox<String> reasonComboBox = new JComboBox<>(reasons);
        reasonComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        reasonPanel.add(reasonComboBox, BorderLayout.CENTER);
        
        // Warning message
        JLabel warningLabel = new JLabel("⚠️ This action cannot be undone!");
        warningLabel.setFont(new Font("Arial", Font.BOLD, 12));
        warningLabel.setForeground(new Color(244, 67, 54));
        warningLabel.setHorizontalAlignment(SwingConstants.CENTER);
        warningLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton confirmButton = new JButton("✅ Confirm Cancellation");
        confirmButton.setBackground(new Color(244, 67, 54));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFont(new Font("Arial", Font.BOLD, 12));
        confirmButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        
        JButton cancelButton = new JButton("❌ Cancel");
        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        
        // Add action listeners
        confirmButton.addActionListener(e -> {
            String selectedReason = (String) reasonComboBox.getSelectedItem();
            
            // Cancel reservation
            currentReservationToCancel.cancel();
            currentReservationToCancel.getFlight().increaseAvailableSeats();
            
            // Export cancellation to Excel
            ExcelExporter.exportCancellationHistory(currentReservationToCancel, selectedReason);
            
            // Update UI
            updateTables();
            loadData();
            cancelReservationIdField.setText("");
            reservationDetailsArea.setText("Enter Reservation ID and click 'Search Reservation' to view details.");
            
            String cancelledId = currentReservationToCancel.getReservationId();
            currentReservationToCancel = null;
            cancelReservationButton.setEnabled(false);
            
            dialog.dispose();
            
            showSuccess("Reservation cancelled successfully!\n\n" +
                "Reservation ID: " + cancelledId + "\n" +
                "Reason: " + selectedReason + "\n" +
                "Seat has been freed up for other passengers.");
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        // Assemble dialog
        contentPanel.add(reasonPanel, BorderLayout.CENTER);
        contentPanel.add(warningLabel, BorderLayout.SOUTH);
        
        dialog.add(headerPanel, BorderLayout.NORTH);
        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void refreshData() {
        updateTables();
        loadData();
        searchPanel.refreshData(flights, reservations);
        statusLabel.setText("Data refreshed at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }
    
    private void updateFlightsWithRealTimeData() {
        try {
            // Check if API key is configured
            if (!AviationStackService.isApiKeyConfigured()) {
                System.out.println("AviationStack API key not configured. Using mock data.");
                statusLabel.setText("Using mock flight data (configure API key for real-time data)");
                return;
            }
            
            // Update flights with real-time data
            AviationStackService.updateFlightsDaily(flights);
            
            // Update the UI
            updateTables();
            searchPanel.refreshData(flights, reservations);
            updateSummaryPanel(); // <-- update summary after API call
            
            statusLabel.setText("Flights updated with real-time data at " + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            
        } catch (Exception e) {
            System.err.println("Error updating flights with real-time data: " + e.getMessage());
            statusLabel.setText("Error updating flights: " + e.getMessage());
        }
    }
    
    private void saveData() {
        DataManager.saveFlights(flights);
        DataManager.saveReservations(reservations);
        statusLabel.setText("Data saved successfully");
    }
    
    // Export methods
    private void exportCurrentState() {
        ExcelExporter.exportCurrentState(reservations);
        showSuccess("Current state exported to current_state.csv");
        statusLabel.setText("Current state exported successfully");
    }
    
    private void exportBookingHistory() {
        // This method is now deprecated for appending new bookings. Use exportBookingHistory(reservation) when booking.
        showError("Exporting all bookings at once is not supported. Bookings are now appended individually.");
        statusLabel.setText("Booking history export not supported in bulk mode");
    }
    
    private void exportCancellationHistory() {
        // Export all cancelled reservations
        for (Reservation reservation : reservations) {
            if (reservation.getStatus() == Reservation.ReservationStatus.CANCELLED) {
                ExcelExporter.exportCancellationHistory(reservation, "Cancelled by user");
            }
        }
        showSuccess("Cancellation history exported to cancellation_history.csv");
        statusLabel.setText("Cancellation history exported successfully");
    }
    
    private void generateComprehensiveReport() {
        ExcelExporter.createExcelReport();
        showSuccess("Comprehensive report generated: airline_management_report.csv");
        statusLabel.setText("Comprehensive report generated successfully");
    }
    
    private int getTotalReservationsCount() {
        return reservations.size();
    }
    
    private int getTotalAvailableSeats() {
        return flights.stream().mapToInt(Flight::getAvailableSeats).sum();
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
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
} 