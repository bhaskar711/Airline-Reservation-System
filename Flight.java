import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Flight {
    private int flightNumber;
    private String designation;
    private String departure;
    private int availableSeats;
    private int totalSeats;
    private LocalDateTime departureTime;
    private double price;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Flight(int flightNumber, String departure, String designation, int totalSeats, LocalDateTime departureTime, double price) {
        this.flightNumber = flightNumber;
        this.departure = departure;
        this.designation = designation;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.departureTime = departureTime;
        this.price = price;
    }

    // Constructor for backward compatibility
    public Flight(int flightNumber, String designation, int availableSeats) {
        this(flightNumber, "Unknown", designation, availableSeats, LocalDateTime.now().plusDays(1), 0.0);
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(int flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        if (availableSeats >= 0 && availableSeats <= totalSeats) {
        this.availableSeats = availableSeats;
        }
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void decreaseAvailableSeats() {
        if (availableSeats > 0) {
            availableSeats--;
        }
    }

    public void increaseAvailableSeats() {
        if (availableSeats < totalSeats) {
        availableSeats++;
        }
    }

    public double getOccupancyRate() {
        return totalSeats > 0 ? ((double)(totalSeats - availableSeats) / totalSeats) * 100 : 0;
    }

    public boolean isFull() {
        return availableSeats == 0;
    }

    public boolean hasAvailableSeats() {
        return availableSeats > 0;
    }

    @Override
    public String toString() {
        return String.format("Flight %d: %s → %s | Seats: %d/%d | Price: $%.2f | Departure: %s", 
            flightNumber, departure, designation, availableSeats, totalSeats, price, 
            departureTime.format(formatter));
    }
}