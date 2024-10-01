package Booking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.io.*;

public class BookingSystemGUI extends JFrame {

    // Map to store destination names and their coordinates
    private static Map<String, double[]> destinations = new HashMap<>();

    // Components
    private JComboBox<String> destinationComboBox;
    private JComboBox<String> childrenComboBox;
    private JComboBox<LocalDate> dateComboBox;
    private JTextArea outputArea;
    private String parentId;
    private double userX = 0; // User's X coordinate
    private double userY = 0; // User's Y coordinate

    // Constructor
    public BookingSystemGUI(String username) {
        parentId = username;
        
        setTitle("Booking System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load destinations and user coordinates
        loadDestinations();

        // Setup layout
        setLayout(new BorderLayout());

        // Panel for selections
        JPanel selectionPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        // Destination selection
        selectionPanel.add(new JLabel("Select Destination:"));
        destinationComboBox = new JComboBox<>(getFormattedDestinations());
        selectionPanel.add(destinationComboBox);

        // Children selection
        selectionPanel.add(new JLabel("Select Child:"));
        childrenComboBox = new JComboBox<>();
        selectionPanel.add(childrenComboBox);

        // Date selection
        selectionPanel.add(new JLabel("Select Date:"));
        dateComboBox = new JComboBox<>();
        selectionPanel.add(dateComboBox);

        // Add selection panel to frame
        add(selectionPanel, BorderLayout.NORTH);

        // Output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton bookButton = new JButton("Book");
        JButton backButton = new JButton("Back"); // Add Back button

        buttonPanel.add(bookButton);
        buttonPanel.add(backButton); // Add Back button to the panel
        add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        bookButton.addActionListener(e -> makeBooking());
        backButton.addActionListener(e -> goBack()); // Add action listener for Back button

        // Load children initially
        loadChildren();

        // Load dates automatically when child selection changes
        childrenComboBox.addActionListener(e -> {
            loadDates(); // Load dates automatically when child selection changes
        });

        // Load dates automatically when GUI is opened
        loadDates();
    }
    
    // Method to go back to the previous screen
    private void goBack() {
        // Dispose of the current window
        this.dispose();

        // Open the previous screen
        // For example, if your previous screen is the login screen:
        new Login.Main(); // Adjust this according to your actual previous screen class
    }
    
    // Method to check if an event is scheduled for a child on a particular date
    private boolean hasEventOnDate(String childId, LocalDate date) {
        String query = "SELECT COUNT(*) FROM event_reg er JOIN event_create ec ON er.event_id = ec.Id WHERE er.stu_username = ? AND ec.Date = ?";
        try (Connection con = Database.MyDataBase.establishConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, childId);
            stmt.setObject(2, date);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to load destinations and user coordinates
    private void loadDestinations() {
        // Load destinations from file
        try (BufferedReader br = new BufferedReader(new FileReader("BookingDestination.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String name = line;
                String[] coordinates = br.readLine().split(", ");
                if (coordinates.length == 2) {
                    double x = Double.parseDouble(coordinates[0]);
                    double y = Double.parseDouble(coordinates[1]);
                    destinations.put(name, new double[]{x, y});
                } else {
                    System.err.println("Invalid format for coordinates: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load user coordinates from the database
        try (Connection con = Database.MyDataBase.establishConnection()){
            loadUserCoordinates(con, parentId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to load user coordinates from the database
    private void loadUserCoordinates(Connection conn, String parentId) throws SQLException {
        String query = "SELECT X, Y FROM User WHERE Username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, parentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    userX = rs.getDouble("X");
                    userY = rs.getDouble("Y");
                }
            }
        }
    }

    // Method to load children
    private void loadChildren() {
        try (Connection con = Database.MyDataBase.establishConnection()) {
            java.util.List<String> children = getAvailableChildren(con, parentId);
            childrenComboBox.removeAllItems();
            for (String child : children) {
                childrenComboBox.addItem(child);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to load available dates
    private void loadDates() {
        try (Connection con = Database.MyDataBase.establishConnection()) {
            String selectedChild = (String) childrenComboBox.getSelectedItem();
            if (selectedChild != null) {
                java.util.List<LocalDate> availableDates = getAvailableDates(con, selectedChild);
                dateComboBox.removeAllItems();
                for (LocalDate date : availableDates) {
                    dateComboBox.addItem(date);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to make a booking
    private void makeBooking() {
        try (Connection con = Database.MyDataBase.establishConnection()) {
            String selectedDestination = (String) destinationComboBox.getSelectedItem();
            String selectedChild = (String) childrenComboBox.getSelectedItem();
            LocalDate selectedDate = (LocalDate) dateComboBox.getSelectedItem();

            if (selectedDestination != null && selectedChild != null && selectedDate != null) {
                selectedDestination = selectedDestination.split(" - ")[0]; // Extract destination name

                // Check if the child has an event on the selected date
                if (hasEventOnDate(selectedChild, selectedDate)) {
                    outputArea.append("The child has an event on the selected date. Booking cannot be made.\n");
                    return;
                }

                // Check if the child already has a booking on the selected date
                if (hasBookingOnDate(con, selectedChild, selectedDate)) {
                    outputArea.append("The child already has a booking on the selected date. Booking cannot be made.\n");
                    return;
                }

                bookDestination(con, parentId, selectedChild, selectedDestination, selectedDate);
                outputArea.append("Booking successful for " + selectedDestination + " on " + selectedDate + " for child " + selectedChild + " by " + parentId + "\n");

                loadDates(); // Load dates automatically after booking
            } else {
                outputArea.append("Please make sure all selections are made.\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to check if a child has a booking on a particular date
    private boolean hasBookingOnDate(Connection conn, String childId, LocalDate date) throws SQLException {
        String query = "SELECT COUNT(*) FROM bookings WHERE child_id = ? AND booking_date = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, childId);
            stmt.setObject(2, date);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    // Helper method to get available children for booking
    private java.util.List<String> getAvailableChildren(Connection conn, String parentId) throws SQLException {
        java.util.List<String> children = new ArrayList<>();
        String query = "SELECT Child1, Child2, Child3, Child4, Child5, Child6, Child7, Child8, Child9, Child10 FROM User WHERE Username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, parentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    for (int i = 1; i <= 10; i++) {
                        String child = rs.getString("Child" + i);
                        if (child != null && !child.isEmpty()) {
                            children.add(child);
                        }
                    }
                }
            }
        }
        return children;
    }

    // Helper method to get available dates for booking
    private java.util.List<LocalDate> getAvailableDates(Connection conn, String childId) throws SQLException {
        java.util.List<LocalDate> availableDates = new ArrayList<>();
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        for (int i = 0; i < 7; i++) {
            LocalDate date = tomorrow.plus(i, ChronoUnit.DAYS);
            if (isDateAvailable(conn, childId, date)) {
                availableDates.add(date);
            }
        }
        return availableDates;
    }

    // Helper method to check if a date is available for booking
    private boolean isDateAvailable(Connection conn, String childId, LocalDate date) throws SQLException {
        String query = "SELECT COUNT(*) FROM bookings WHERE child_id = ? AND booking_date = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, childId);
            stmt.setObject(2, date);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) == 0;
            }
        }
    }

    // Helper method to book a destination
    private void bookDestination(Connection conn, String parentId, String childId, String destination, LocalDate date) throws SQLException {
        String insertBooking = "INSERT INTO bookings (child_id, parent_id, destination, booking_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertBooking)) {
            stmt.setString(1, childId);
            stmt.setString(2, parentId);
            stmt.setString(3, destination);
            stmt.setObject(4, date);
            stmt.executeUpdate();
        }
    }

    // Helper method to calculate the distance between two points
    private double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    // Helper method to get formatted destinations with distances, sorted by distance
    private String[] getFormattedDestinations() {
        java.util.List<String> formattedDestinations = new ArrayList<>();

        // Create a list of destination entries with distances
        java.util.List<Map.Entry<String, double[]>> destinationEntries = new ArrayList<>(destinations.entrySet());
        destinationEntries.sort((entry1, entry2) -> {
            double distance1 = calculateDistance(userX, userY, entry1.getValue()[0], entry1.getValue()[1]);
            double distance2 = calculateDistance(userX, userY, entry2.getValue()[0], entry2.getValue()[1]);
            return Double.compare(distance1, distance2);
        });

        // Format the sorted destinations with distances
        for (Map.Entry<String, double[]> entry : destinationEntries) {
            String name = entry.getKey();
            double[] coordinates = entry.getValue();
            double distance = calculateDistance(userX, userY, coordinates[0], coordinates[1]);
            formattedDestinations.add(String.format("%s - %.2f km", name, distance));
        }

        return formattedDestinations.toArray(new String[0]);
    }
    
    public static void main(String[] args) {
        // Assuming you have a username for the parent, you can pass it here
        String parentUsername = "TanChinPeng";

        // Create an instance of the BookingSystemGUI
        BookingSystemGUI bookingSystemGUI = new BookingSystemGUI(parentUsername);

        // Make the GUI visible
        bookingSystemGUI.setVisible(true);
    }

}
