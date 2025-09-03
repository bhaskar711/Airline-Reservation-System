import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExcelExporter {
    
    private static final String BOOKING_HISTORY_FILE = "booking_history.csv";
    private static final String CANCELLATION_HISTORY_FILE = "cancellation_history.csv";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // CSV Headers
    private static final String BOOKING_HEADER = "Timestamp,Reservation_ID,Passenger_Name,Flight_Number,Departure,Destination,Departure_Time,Price,Status,Booking_Date";
    private static final String CANCELLATION_HEADER = "Timestamp,Reservation_ID,Passenger_Name,Flight_Number,Departure,Destination,Cancellation_Reason,Cancellation_Date,Original_Booking_Date,Original_Price";
    
    public static void exportBookingHistory(Reservation reservation) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOKING_HISTORY_FILE, true))) {
            // Write header if file is empty
            File file = new File(BOOKING_HISTORY_FILE);
            if (file.length() == 0) {
                writer.println(BOOKING_HEADER);
            }
            // Write only the new booking
            String bookingRecord = String.format("%s,%s,%s,%d,%s,%s,%s,%.2f,%s,%s",
                LocalDateTime.now().format(formatter),
                reservation.getReservationId(),
                escapeCsvField(reservation.getName()),
                reservation.getFlight().getFlightNumber(),
                escapeCsvField(reservation.getFlight().getDeparture()),
                escapeCsvField(reservation.getFlight().getDesignation()),
                reservation.getFlight().getDepartureTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                reservation.getFlight().getPrice(),
                reservation.getStatus().toString(),
                reservation.getBookingTime().format(formatter)
            );
            writer.println(bookingRecord);
            System.out.println("Booking history exported to " + BOOKING_HISTORY_FILE);
        } catch (IOException e) {
            System.err.println("Error exporting booking history: " + e.getMessage());
        }
    }
    
    public static void exportCancellationHistory(Reservation reservation, String reason) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CANCELLATION_HISTORY_FILE, true))) {
            // Write header if file is empty
            File file = new File(CANCELLATION_HISTORY_FILE);
            if (file.length() == 0) {
                writer.println(CANCELLATION_HEADER);
            }
            
            // Write cancellation data
            String cancellationRecord = String.format("%s,%s,%s,%d,%s,%s,%s,%s,%s,%.2f",
                LocalDateTime.now().format(formatter),
                reservation.getReservationId(),
                escapeCsvField(reservation.getName()),
                reservation.getFlight().getFlightNumber(),
                escapeCsvField(reservation.getFlight().getDeparture()),
                escapeCsvField(reservation.getFlight().getDesignation()),
                escapeCsvField(reason),
                LocalDateTime.now().format(formatter),
                reservation.getBookingTime().format(formatter),
                reservation.getFlight().getPrice()
            );
            writer.println(cancellationRecord);
            
            System.out.println("Cancellation history exported to " + CANCELLATION_HISTORY_FILE);
            
        } catch (IOException e) {
            System.err.println("Error exporting cancellation history: " + e.getMessage());
        }
    }
    
    public static void exportAllHistory(List<Reservation> reservations) {
        // Only export cancellations for cancelled reservations
        for (Reservation reservation : reservations) {
            if (reservation.getStatus() == Reservation.ReservationStatus.CANCELLED) {
                // For cancelled reservations, we'll use a default reason since we don't store it
                exportCancellationHistory(reservation, "Cancelled by user");
            }
        }
    }
    
    public static void createExcelReport() {
        try {
            // Create a comprehensive Excel-like report in CSV format
            String reportFile = "airline_management_report.csv";
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(reportFile))) {
                writer.println("AIRLINE MANAGEMENT SYSTEM - COMPREHENSIVE REPORT");
                writer.println("Generated on: " + LocalDateTime.now().format(formatter));
                writer.println();
                
                // Summary section
                writer.println("SUMMARY");
                writer.println("=======");
                writer.println("Total Reservations: " + countReservations());
                writer.println("Active Reservations: " + countActiveReservations());
                writer.println("Cancelled Reservations: " + countCancelledReservations());
                writer.println();
                
                // Detailed booking history
                writer.println("DETAILED BOOKING HISTORY");
                writer.println("=======================");
                writer.println(BOOKING_HEADER);
                
                // Read and append booking history
                appendFileContent(BOOKING_HISTORY_FILE, writer);
                writer.println();
                
                // Detailed cancellation history
                writer.println("DETAILED CANCELLATION HISTORY");
                writer.println("============================");
                writer.println(CANCELLATION_HEADER);
                
                // Read and append cancellation history
                appendFileContent(CANCELLATION_HISTORY_FILE, writer);
                
            }
            
            System.out.println("Comprehensive report created: " + reportFile);
            
        } catch (IOException e) {
            System.err.println("Error creating Excel report: " + e.getMessage());
        }
    }
    
    private static void appendFileContent(String sourceFile, PrintWriter writer) {
        try (BufferedReader reader = new BufferedReader(new FileReader(sourceFile))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header line
                }
                writer.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + sourceFile + ": " + e.getMessage());
        }
    }
    
    private static int countReservations() {
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKING_HISTORY_FILE))) {
            int count = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("Timestamp")) { // Skip header
                    count++;
                }
            }
            return count;
        } catch (IOException e) {
            return 0;
        }
    }
    
    private static int countActiveReservations() {
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKING_HISTORY_FILE))) {
            int count = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("CONFIRMED")) {
                    count++;
                }
            }
            return count;
        } catch (IOException e) {
            return 0;
        }
    }
    
    private static int countCancelledReservations() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CANCELLATION_HISTORY_FILE))) {
            int count = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("Timestamp")) { // Skip header
                    count++;
                }
            }
            return count;
        } catch (IOException e) {
            return 0;
        }
    }
    
    private static String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }
        // Escape quotes and wrap in quotes if contains comma
        String escaped = field.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }
    
    public static void exportCurrentState(List<Reservation> reservations) {
        // Export current state of all reservations
        try (PrintWriter writer = new PrintWriter(new FileWriter("current_state.csv"))) {
            writer.println("CURRENT RESERVATION STATE");
            writer.println("=========================");
            writer.println(BOOKING_HEADER);
            
            for (Reservation reservation : reservations) {
                String record = String.format("%s,%s,%s,%d,%s,%s,%s,%.2f,%s,%s",
                    LocalDateTime.now().format(formatter),
                    reservation.getReservationId(),
                    escapeCsvField(reservation.getName()),
                    reservation.getFlight().getFlightNumber(),
                    escapeCsvField(reservation.getFlight().getDeparture()),
                    escapeCsvField(reservation.getFlight().getDesignation()),
                    reservation.getFlight().getDepartureTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    reservation.getFlight().getPrice(),
                    reservation.getStatus().toString(),
                    reservation.getBookingTime().format(formatter)
                );
                writer.println(record);
            }
            
            System.out.println("Current state exported to current_state.csv");
            
        } catch (IOException e) {
            System.err.println("Error exporting current state: " + e.getMessage());
        }
    }
} 