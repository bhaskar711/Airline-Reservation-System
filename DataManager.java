import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static final String FLIGHTS_FILE = "flights.dat";
    private static final String RESERVATIONS_FILE = "reservations.dat";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void saveFlights(List<Flight> flights) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FLIGHTS_FILE))) {
            for (Flight flight : flights) {
                writer.println(String.format("%d,%s,%s,%d,%d,%.2f,%s",
                    flight.getFlightNumber(),
                    flight.getDeparture(),
                    flight.getDesignation(),
                    flight.getTotalSeats(),
                    flight.getAvailableSeats(),
                    flight.getPrice(),
                    flight.getDepartureTime().format(formatter)
                ));
            }
            System.out.println("Flights data saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving flights data: " + e.getMessage());
        }
    }

    public static List<Flight> loadFlights() {
        List<Flight> flights = new ArrayList<>();
        File file = new File(FLIGHTS_FILE);
        
        if (!file.exists()) {
            // Create default flights if file doesn't exist
            flights.add(new Flight(123, "Mumbai", "Delhi", 150, LocalDateTime.now().plusDays(1), 250.0));
            flights.add(new Flight(188, "Mumbai", "London", 200, LocalDateTime.now().plusDays(2), 850.0));
            flights.add(new Flight(332, "Mumbai", "Bangalore", 120, LocalDateTime.now().plusDays(1), 180.0));
            return flights;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    Flight flight = new Flight(
                        Integer.parseInt(parts[0]), // flightNumber
                        parts[1], // departure
                        parts[2], // designation
                        Integer.parseInt(parts[3]), // totalSeats
                        LocalDateTime.parse(parts[6], formatter), // departureTime
                        Double.parseDouble(parts[5]) // price
                    );
                    flight.setAvailableSeats(Integer.parseInt(parts[4])); // availableSeats
                    flights.add(flight);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading flights data: " + e.getMessage());
        }
        return flights;
    }

    public static void saveReservations(List<Reservation> reservations) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RESERVATIONS_FILE))) {
            for (Reservation reservation : reservations) {
                writer.println(String.format("%s,%s,%d,%s,%s",
                    reservation.getReservationId(),
                    reservation.getName(),
                    reservation.getFlight().getFlightNumber(),
                    reservation.getStatus().toString(),
                    reservation.getBookingTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ));
            }
            System.out.println("Reservations data saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving reservations data: " + e.getMessage());
        }
    }

    public static List<Reservation> loadReservations(List<Flight> flights) {
        List<Reservation> reservations = new ArrayList<>();
        File file = new File(RESERVATIONS_FILE);
        
        if (!file.exists()) {
            return reservations;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    // Find the corresponding flight
                    Flight flight = null;
                    for (Flight f : flights) {
                        if (f.getFlightNumber() == Integer.parseInt(parts[2])) {
                            flight = f;
                            break;
                        }
                    }
                    
                    if (flight != null) {
                        Reservation reservation = new Reservation(parts[1], flight);
                        reservation.setStatus(Reservation.ReservationStatus.valueOf(parts[3]));
                        reservations.add(reservation);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading reservations data: " + e.getMessage());
        }
        return reservations;
    }
} 