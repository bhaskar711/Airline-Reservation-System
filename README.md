# Airline-Reservation-System
All-in-one Java Airline Reservation and Management System with both GUI and console interfaces. Features include flight search, booking, cancellations, real-time updates, passenger management, and detailed CSV report exports.
# Air Management System

A Java-based Airline Reservation System with both **Graphical User Interface (GUI)** and **Console Interface**. The system allows users to search for flights, book tickets, view and cancel reservations, and generate reports. Real-time flight data is fetched using the AviationStack API.

## Features

- **Flight Search & Booking:** Search available flights, view details, and book tickets.
- **Reservation Management:** View, manage, and cancel reservations.
- **Real-Time Data:** Fetches live flight data using the AviationStack API.
- **Reports:** Generate and export booking, cancellation, and summary reports.
- **Dual Interface:** Choose between a modern GUI or a simple console interface.
- **Data Persistence:** Saves and loads flight and reservation data.

## Technologies Used

- Java (Swing for GUI)
- AviationStack API (for real-time flight data)
- File I/O for data persistence
- Excel/CSV export for reports

## How to Run

1. **Clone the repository:**
   ```sh
   git clone https://github.com/<your-username>/<repo-name>.git
   cd <repo-name>
   ```

2. **Compile the project:**
   ```sh
   javac *.java
   ```

3. **Run the launcher:**
   ```sh
   java Launcher
   ```
   - Choose GUI or Console interface as preferred.

## Project Structure

- `Launcher.java` - Entry point with interface selection.
- `AirlineSystem.java` - Console-based reservation system.
- `AirlineSystemGUI.java` - Main GUI application.
- `Flight.java` - Flight model.
- `Reservation.java` - Reservation model.
- `DataManager.java` - Handles data loading/saving.
- `AviationStackService.java` - Fetches real-time flight data.
- `ExcelExporter.java` - Exports reports to Excel.
- Other supporting files


## License

This project is for educational purposes.

---

**Developed by:** Bhaskar and Ashok
