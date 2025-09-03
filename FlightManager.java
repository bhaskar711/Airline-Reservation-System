import java.util.ArrayList;
import java.util.List;

public class FlightManager {
    
    public static void displayAvailableFlights(List<Flight> flights) {
        System.out.println("\n---- Available Flights ----");
        if (flights.isEmpty()) {
            System.out.println("No flights available.");
            return;
        }
        
        System.out.printf("%-15s %-15s %-15s%n", "Flight Number", "Destination", "Available Seats");
        System.out.println("------------------------------------------------");
        
        for (Flight flight : flights) {
            System.out.printf("%-15d %-15s %-15d%n", 
                flight.getFlightNumber(), 
                flight.getDesignation(), 
                flight.getAvailableSeats());
        }
    }
    
    public static Flight findFlightByNumber(List<Flight> flights, int flightNumber) {
        for (Flight flight : flights) {
            if (flight.getFlightNumber() == flightNumber) {
                return flight;
            }
        }
        return null;
    }
    
    public static boolean isFlightAvailable(Flight flight) {
        return flight != null && flight.getAvailableSeats() > 0;
    }
} 