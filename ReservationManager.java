import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReservationManager {
    
    public static void bookFlight(List<Flight> flights, List<Reservation> reservations, Scanner scanner) {
        FlightManager.displayAvailableFlights(flights);
        
        System.out.print("Enter the flight number to book: ");
        int flightNumber = getValidIntegerInput(scanner);
        
        Flight selectedFlight = FlightManager.findFlightByNumber(flights, flightNumber);
        
        if (selectedFlight == null) {
            System.out.println("Invalid flight number. Please try again.");
            return;
        }
        
        if (!FlightManager.isFlightAvailable(selectedFlight)) {
            System.out.println("Sorry, no seats available on this flight.");
            return;
        }
        
        System.out.print("Enter passenger name: ");
        String passengerName = scanner.nextLine().trim();
        
        if (passengerName.isEmpty()) {
            System.out.println("Passenger name cannot be empty.");
            return;
        }
        
        // Create reservation
        Reservation reservation = new Reservation(passengerName, selectedFlight);
        reservations.add(reservation);
        
        // Update seat availability
        selectedFlight.decreaseAvailableSeats();
        
        System.out.println("Booking successful!");
        System.out.println("Reservation Details:");
        System.out.println("Passenger: " + passengerName);
        System.out.println("Flight: " + selectedFlight.getFlightNumber() + " to " + selectedFlight.getDesignation());
    }
    
    public static void viewReservations(List<Reservation> reservations) {
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }
        
        System.out.println("\n---- Current Reservations ----");
        System.out.printf("%-20s %-15s %-15s%n", "Passenger Name", "Flight Number", "Destination");
        System.out.println("------------------------------------------------");
        
        for (Reservation reservation : reservations) {
            System.out.printf("%-20s %-15d %-15s%n", 
                reservation.getName(),
                reservation.getFlight().getFlightNumber(),
                reservation.getFlight().getDesignation());
        }
    }
    
    public static void cancelBooking(List<Reservation> reservations, Scanner scanner) {
        if (reservations.isEmpty()) {
            System.out.println("No reservations to cancel.");
            return;
        }
        
        System.out.print("Enter passenger name to cancel booking: ");
        String passengerName = scanner.nextLine().trim();
        
        Reservation reservationToCancel = null;
        for (Reservation reservation : reservations) {
            if (reservation.getName().equalsIgnoreCase(passengerName)) {
                reservationToCancel = reservation;
                break;
            }
        }
        
        if (reservationToCancel == null) {
            System.out.println("No reservation found for passenger: " + passengerName);
            return;
        }
        
        // Restore seat availability
        reservationToCancel.getFlight().increaseAvailableSeats();
        
        // Remove reservation
        reservations.remove(reservationToCancel);
        
        System.out.println("Reservation cancelled successfully for: " + passengerName);
    }
    
    private static int getValidIntegerInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number:");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // Consume leftover newline
        return value;
    }
} 