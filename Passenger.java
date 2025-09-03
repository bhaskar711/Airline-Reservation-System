import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Passenger {
    private String passengerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private List<Reservation> reservations;

    public Passenger(String firstName, String lastName, String email, String phoneNumber, LocalDate dateOfBirth) {
        this.passengerId = generatePassengerId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.reservations = new ArrayList<>();
    }

    private String generatePassengerId() {
        return "PASS-" + System.currentTimeMillis() % 100000;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<Reservation> getReservations() {
        return new ArrayList<>(reservations);
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
    }

    public int getAge() {
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }

    public boolean hasActiveReservations() {
        return reservations.stream().anyMatch(Reservation::isActive);
    }

    @Override
    public String toString() {
        return String.format("Passenger %s: %s %s | Email: %s | Phone: %s | Age: %d",
            passengerId, firstName, lastName, email, phoneNumber, getAge());
    }
}