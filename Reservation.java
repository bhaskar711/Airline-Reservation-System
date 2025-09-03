import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Reservation {
    private String reservationId;
    private String passengerName;
    private Flight flight;
    private LocalDateTime bookingTime;
    private ReservationStatus status;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public enum ReservationStatus {
        CONFIRMED, CANCELLED, PENDING
    }

    public Reservation(String passengerName, Flight flight) {
        this.reservationId = generateReservationId();
        this.passengerName = passengerName;
        this.flight = flight;
        this.bookingTime = LocalDateTime.now();
        this.status = ReservationStatus.CONFIRMED;
    }

    private String generateReservationId() {
        return "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public Flight getFlight() {
        return flight;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
    }

    public boolean isActive() {
        return status == ReservationStatus.CONFIRMED;
    }

    @Override
    public String toString() {
        return String.format("Reservation %s | Passenger: %s | Flight: %d | Status: %s | Booked: %s",
            reservationId, passengerName, flight.getFlightNumber(), status, 
            bookingTime.format(formatter));
    }
}