import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AirlineSystem {
    private static List<Flight> flights = new ArrayList<>();
    private static List<Reservation> reservations = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeSystem();
        runMainMenu();
    }

    private static void initializeSystem() {
        // Load existing data or create default flights
        flights = DataManager.loadFlights();
        reservations = DataManager.loadReservations(flights);
        
        System.out.println("Airline Reservation System initialized successfully!");
        System.out.println("Loaded " + flights.size() + " flights and " + reservations.size() + " reservations.");
    }

    private static void runMainMenu() {
        while (true) {
            displayMainMenu();
            int choice = getValidIntegerInput();

            switch (choice) {
                case 1:
                    FlightManager.displayAvailableFlights(flights);
                    break;
                case 2:
                    ReservationManager.bookFlight(flights, reservations, scanner);
                    break;
                case 3:
                    ReservationManager.viewReservations(reservations);
                    break;
                case 4:
                    ReservationManager.cancelBooking(reservations, scanner);
                    break;
                case 5:
                    saveDataAndExit();
                    return;
                default:
                    System.out.println("Invalid option, please try again");
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n====== Airline Reservation System ======");
        System.out.println("1. Display Available Flights");
        System.out.println("2. Book a Flight");
        System.out.println("3. View Reservations");
        System.out.println("4. Cancel Booking");
        System.out.println("5. Exit");
        System.out.print("Choose an option: ");
    }

    private static int getValidIntegerInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number:");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // Consume leftover newline
        return value;
    }

    private static void saveDataAndExit() {
        System.out.println("Saving data...");
        DataManager.saveFlights(flights);
        DataManager.saveReservations(reservations);
        System.out.println("Thank you for using the Airline Reservation System!");
        scanner.close();
    }
} 