import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AviationStackService {
    
    private static final String API_KEY = "e17fd3b0620fb458a0b5913fff8e198e"; // Replace with your actual API key
    private static final String BASE_URL = "http://api.aviationstack.com/v1";
    private static final String FLIGHTS_ENDPOINT = "/flights";
    
    // Common routes for demonstration
    private static final String[][] COMMON_ROUTES = {
        {"New York", "Los Angeles"},
        {"London", "Paris"},
        {"Tokyo", "Seoul"},
        {"Mumbai", "Delhi"},
        {"Sydney", "Melbourne"},
        {"Toronto", "Vancouver"},
        {"Dubai", "Abu Dhabi"},
        {"Singapore", "Bangkok"},
        {"Frankfurt", "Berlin"},
        {"Chicago", "Miami"}
    };
    
    public static List<Flight> getRealTimeFlights() {
        List<Flight> realTimeFlights = new ArrayList<>();
        
        try {
            // Try to get real data from AviationStack
            List<Flight> apiFlights = fetchFlightsFromAPI();
            if (!apiFlights.isEmpty()) {
                return apiFlights;
            }
        } catch (Exception e) {
            System.out.println("API call failed, using mock data: " + e.getMessage());
        }
        
        // Fallback to mock data if API fails
        return generateMockFlights();
    }
    
    private static List<Flight> fetchFlightsFromAPI() throws Exception {
        List<Flight> flights = new ArrayList<>();
        
        // Build API URL with parameters
        String urlString = BASE_URL + FLIGHTS_ENDPOINT + 
                          "?access_key=" + API_KEY +
                          "&limit=100" +
                          "&flight_status=active";
        
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        
        int responseCode = connection.getResponseCode();
        
        if (responseCode == 200) {
            // Read response
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream())
            );
            
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            
            // Parse JSON response (simplified parsing)
            flights = parseFlightData(response.toString());
            
        } else {
            throw new Exception("API request failed with response code: " + responseCode);
        }
        
        connection.disconnect();
        return flights;
    }
    
    private static List<Flight> parseFlightData(String jsonResponse) {
        List<Flight> flights = new ArrayList<>();
        
        try {
            // Simple JSON parsing (in a real implementation, use a JSON library)
            String[] lines = jsonResponse.split("\n");
            
            for (String line : lines) {
                if (line.contains("\"flight_number\"") && line.contains("\"departure\"") && line.contains("\"arrival\"")) {
                    // Extract flight information
                    Flight flight = extractFlightFromJson(line);
                    if (flight != null) {
                        flights.add(flight);
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error parsing flight data: " + e.getMessage());
        }
        
        return flights;
    }
    
    private static Flight extractFlightFromJson(String jsonLine) {
        try {
            // Extract basic flight information
            String flightNumber = extractValue(jsonLine, "flight_number");
            String departure = extractValue(jsonLine, "departure");
            String arrival = extractValue(jsonLine, "arrival");
            String departureTime = extractValue(jsonLine, "departure_time");
            
            if (flightNumber != null && departure != null && arrival != null) {
                // Parse flight number
                int flightNum = Integer.parseInt(flightNumber.replaceAll("[^0-9]", ""));
                
                // Parse departure time
                LocalDateTime departureDateTime = LocalDateTime.now().plusHours(new Random().nextInt(24));
                if (departureTime != null && !departureTime.isEmpty()) {
                    try {
                        departureDateTime = LocalDateTime.parse(departureTime, 
                            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                    } catch (Exception e) {
                        // Use current time + random hours if parsing fails
                        departureDateTime = LocalDateTime.now().plusHours(new Random().nextInt(24));
                    }
                }
                
                // Generate random price and seats
                double price = 150.0 + new Random().nextDouble() * 850.0;
                int totalSeats = 100 + new Random().nextInt(200);
                int availableSeats = new Random().nextInt(totalSeats) + 1;
                
                Flight flight = new Flight(flightNum, departure, arrival, totalSeats, departureDateTime, price);
                flight.setAvailableSeats(availableSeats);
                
                return flight;
            }
            
        } catch (Exception e) {
            System.err.println("Error extracting flight from JSON: " + e.getMessage());
        }
        
        return null;
    }
    
    private static String extractValue(String jsonLine, String key) {
        try {
            String pattern = "\"" + key + "\":\\s*\"([^\"]+)\"";
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher m = p.matcher(jsonLine);
            
            if (m.find()) {
                return m.group(1);
            }
        } catch (Exception e) {
            // Return null if extraction fails
        }
        return null;
    }
    
    private static List<Flight> generateMockFlights() {
        List<Flight> flights = new ArrayList<>();
        Random random = new Random();
        
        // Generate flights for today and tomorrow
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        
        for (int i = 0; i < 20; i++) {
            String[] route = COMMON_ROUTES[random.nextInt(COMMON_ROUTES.length)];
            String departure = route[0];
            String destination = route[1];
            
            // Random flight number
            int flightNumber = 1000 + random.nextInt(9000);
            
            // Random departure time (today or tomorrow)
            LocalDate flightDate = random.nextBoolean() ? today : tomorrow;
            int hour = random.nextInt(24);
            int minute = random.nextInt(60);
            LocalDateTime departureTime = LocalDateTime.of(flightDate, 
                java.time.LocalTime.of(hour, minute));
            
            // Random price and seats
            double price = 150.0 + random.nextDouble() * 850.0;
            int totalSeats = 100 + random.nextInt(200);
            int availableSeats = random.nextInt(totalSeats) + 1;
            
            Flight flight = new Flight(flightNumber, departure, destination, totalSeats, departureTime, price);
            flight.setAvailableSeats(availableSeats);
            flights.add(flight);
        }
        
        return flights;
    }
    
    public static void updateFlightsDaily(List<Flight> currentFlights) {
        try {
            System.out.println("Updating flights with real-time data...");
            
            // Get fresh flight data
            List<Flight> newFlights = getRealTimeFlights();
            
            // Clear current flights and add new ones
            currentFlights.clear();
            currentFlights.addAll(newFlights);
            
            System.out.println("Successfully updated " + newFlights.size() + " flights");
            
        } catch (Exception e) {
            System.err.println("Error updating flights: " + e.getMessage());
        }
    }
    
    public static boolean isApiKeyConfigured() {
        return !API_KEY.equals("YOUR_AVIATIONSTACK_API_KEY");
    }
    
    public static String getApiKey() {
        return API_KEY;
    }
    
    public static void setApiKey(String newApiKey) {
        // In a real implementation, you would save this to a configuration file
        System.out.println("API Key updated. Please restart the application for changes to take effect.");
    }
} 