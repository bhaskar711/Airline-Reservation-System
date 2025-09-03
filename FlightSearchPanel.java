import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class FlightSearchPanel extends JPanel {
    private JTextField departureField;
    private JTextField destinationField;
    private JTextField dateField;
    private JButton searchButton;
    private JTextArea resultsArea;
    private JButton bookButton;
    private JTextField passengerNameField;
    private JComboBox<Flight> flightSelectionComboBox;
    private List<Flight> allFlights;
    private List<Reservation> reservations;
    private Flight selectedFlight;
    
    public FlightSearchPanel(List<Flight> flights, List<Reservation> reservations) {
        this.allFlights = flights;
        this.reservations = reservations;
        setupSearchPanel();
    }
    
    private void setupSearchPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create search form panel
        JPanel searchFormPanel = createSearchFormPanel();
        add(searchFormPanel, BorderLayout.NORTH);
        
        // Create results panel
        JPanel resultsPanel = createResultsPanel();
        add(resultsPanel, BorderLayout.CENTER);
        
        // Create booking panel
        JPanel bookingPanel = createBookingPanel();
        add(bookingPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createSearchFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Search Flights"));
        panel.setBackground(new Color(248, 249, 250));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Departure City
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("From (Departure City):"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        departureField = new JTextField(15);
        panel.add(departureField, gbc);
        
        // Destination City
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        panel.add(new JLabel("To (Destination City):"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        destinationField = new JTextField(15);
        panel.add(destinationField, gbc);
        
        // Date
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        dateField = new JTextField(15);
        dateField.setText(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        panel.add(dateField, gbc);
        
        // Search Button
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0.0;
        
        searchButton = new JButton("🔍 Search Flights");
        searchButton.setBackground(new Color(25, 118, 210));
        searchButton.setForeground(Color.BLACK);
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        searchButton.addActionListener(e -> searchFlights());
        panel.add(searchButton, gbc);
        
        return panel;
    }
    
    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Search Results"));
        
        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultsArea.setBackground(new Color(255, 255, 255));
        resultsArea.setText("Enter search criteria and click 'Search Flights' to find available flights.");
        
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        scrollPane.setPreferredSize(new Dimension(600, 200));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createBookingPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Book Selected Flight"));
        panel.setBackground(new Color(248, 249, 250));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Flight Selection (when multiple flights available)
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Select Flight Number:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        flightSelectionComboBox = new JComboBox<>();
        flightSelectionComboBox.addActionListener(e -> updateSelectedFlight());
        panel.add(flightSelectionComboBox, gbc);
        
        // Passenger Name
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Passenger Name:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        passengerNameField = new JTextField(20);
        panel.add(passengerNameField, gbc);
        
        // Book Button
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0.0;
        
        bookButton = new JButton("✈️ Book Flight");
        bookButton.setBackground(new Color(76, 175, 80));
        bookButton.setForeground(Color.BLACK);
        bookButton.setFont(new Font("Arial", Font.BOLD, 14));
        bookButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        bookButton.setEnabled(false);
        bookButton.addActionListener(e -> bookSelectedFlight());
        panel.add(bookButton, gbc);
        
        return panel;
    }
    
    private void searchFlights() {
        String departure = departureField.getText().trim();
        String destination = destinationField.getText().trim();
        String dateStr = dateField.getText().trim();
        
        // Validate inputs
        if (departure.isEmpty() || destination.isEmpty() || dateStr.isEmpty()) {
            resultsArea.setText("Please fill in all fields: departure city, destination city, and date.");
            return;
        }
        
        try {
            LocalDate searchDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            
            // Show loading message
            resultsArea.setText("🔍 Searching for real-time flights...\nPlease wait...");
            
            // Get real-time flight data
            List<Flight> realTimeFlights = AviationStackService.getRealTimeFlights();
            
            // Filter flights for the specific date and route
            List<Flight> matchingFlights = realTimeFlights.stream()
                .filter(flight -> flight.getDeparture().equalsIgnoreCase(departure))
                .filter(flight -> flight.getDesignation().equalsIgnoreCase(destination))
                .filter(flight -> flight.getDepartureTime().toLocalDate().equals(searchDate))
                .filter(Flight::hasAvailableSeats)
                .collect(Collectors.toList());
            
            if (matchingFlights.isEmpty()) {
                resultsArea.setText("No flights available at this time.\n\n" +
                    "Search Criteria:\n" +
                    "From: " + departure + "\n" +
                    "To: " + destination + "\n" +
                    "Date: " + dateStr + "\n\n" +
                    "Please try different dates or routes.\n\n" +
                    "💡 Tip: Try searching for tomorrow's date or different cities.");
                bookButton.setEnabled(false);
                selectedFlight = null;
                flightSelectionComboBox.removeAllItems();
            } else {
                displayMatchingFlights(matchingFlights);
            }
            
        } catch (Exception e) {
            resultsArea.setText("Error searching flights: " + e.getMessage() + 
                "\n\nPlease check your internet connection and try again.");
            bookButton.setEnabled(false);
            selectedFlight = null;
        }
    }
    
    private void displayMatchingFlights(List<Flight> flights) {
        StringBuilder sb = new StringBuilder();
        sb.append("Found ").append(flights.size()).append(" available flight(s):\n\n");
        
        // Calculate total cost range
        double minCost = flights.stream().mapToDouble(Flight::getPrice).min().orElse(0.0);
        double maxCost = flights.stream().mapToDouble(Flight::getPrice).max().orElse(0.0);
        
        if (flights.size() > 1) {
            sb.append("💰 COST RANGE: $").append(String.format("%.2f", minCost))
              .append(" - $").append(String.format("%.2f", maxCost)).append("\n\n");
        }
        
        for (int i = 0; i < flights.size(); i++) {
            Flight flight = flights.get(i);
            sb.append(i + 1).append(". Flight ").append(flight.getFlightNumber())
              .append(": ").append(flight.getDeparture()).append(" → ").append(flight.getDesignation())
              .append("\n   Departure: ").append(flight.getDepartureTime().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")))
              .append("\n   Available Seats: ").append(flight.getAvailableSeats())
              .append("\n   💰 COST: $").append(String.format("%.2f", flight.getPrice()))
              .append("\n\n");
        }
        
        sb.append("To book a flight, select the flight number and enter passenger details below.");
        resultsArea.setText(sb.toString());
        
        // Enable booking if flights found
        if (!flights.isEmpty()) {
            // Populate flight selection combo box
            flightSelectionComboBox.removeAllItems();
            for (Flight flight : flights) {
                flightSelectionComboBox.addItem(flight);
            }
            
            selectedFlight = flights.get(0); // Default to first flight
            flightSelectionComboBox.setSelectedItem(selectedFlight);
            bookButton.setEnabled(true);
        }
    }
    
    private void updateSelectedFlight() {
        Flight selected = (Flight) flightSelectionComboBox.getSelectedItem();
        if (selected != null) {
            selectedFlight = selected;
        }
    }
    
    private void bookSelectedFlight() {
        if (selectedFlight == null) {
            JOptionPane.showMessageDialog(this, "Please search for flights first.", "No Flight Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String passengerName = passengerNameField.getText().trim();
        if (passengerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter passenger name.", "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!selectedFlight.hasAvailableSeats()) {
            JOptionPane.showMessageDialog(this, "Sorry, no seats available on this flight.", "No Seats Available", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create reservation
        Reservation reservation = new Reservation(passengerName, selectedFlight);
        reservations.add(reservation);
        selectedFlight.decreaseAvailableSeats();
        
        // Export booking to Excel
        ExcelExporter.exportBookingHistory(reservation);
        
        // Clear form
        passengerNameField.setText("");
        flightSelectionComboBox.removeAllItems();
        bookButton.setEnabled(false);
        selectedFlight = null;
        
        // Show success message
        JOptionPane.showMessageDialog(this, 
            "Flight booked successfully!\n" +
            "Reservation ID: " + reservation.getReservationId() + "\n" +
            "Passenger: " + passengerName + "\n" +
            "Flight: " + reservation.getFlight().getFlightNumber() + " to " + reservation.getFlight().getDesignation(),
            "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);
        
        // Refresh search results
        searchFlights();
    }
    
    public void refreshData(List<Flight> flights, List<Reservation> reservations) {
        this.allFlights = flights;
        this.reservations = reservations;
    }
} 