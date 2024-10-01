package Event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class RegisterEvent {

    private String loggedInUsername; 
    private String currentUserType; 
    private JFrame frame;
    private JComboBox<String> eventTypeComboBox; // Dropdown for event type
    private JComboBox<String> eventComboBox; // Dropdown for event selection
    private JTextArea eventDetailsTextArea; // Text area for event details
    private String todayString; 
    private Connection connection;

    // Constructor initializes variables and sets up the GUI
    public RegisterEvent(String loggedInUsername, String currentUserType) {
        this.loggedInUsername = loggedInUsername;
        this.currentUserType = currentUserType;
        this.todayString = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        frame = new JFrame("Event Registration");// Windows title
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setBackground(Color.WHITE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Set dropdown for event type selection
        JLabel eventTypeLabel = new JLabel("Select Event Type:");
        eventTypeLabel.setFont(new Font("Maiandra GD", Font.BOLD, 16));
        eventTypeComboBox = new JComboBox<>(new String[]{"Today's Events", "Upcoming Events"});
        eventTypeComboBox.setFont(new Font("Maiandra GD", Font.PLAIN, 16));
        eventTypeComboBox.addActionListener(e -> loadEvents());

        // Set dropdown for event selection
        JLabel eventLabel = new JLabel("Select Event:");
        eventLabel.setFont(new Font("Maiandra GD", Font.BOLD, 16));
        eventComboBox = new JComboBox<>();
        eventComboBox.setFont(new Font("Maiandra GD", Font.PLAIN, 16));
        eventComboBox.addActionListener(e -> displayEventDetails());

        // Set text area for event details
        JLabel eventDetailsLabel = new JLabel("Event Details:");
        eventDetailsLabel.setFont(new Font("Maiandra GD", Font.BOLD, 16));
        eventDetailsTextArea = new JTextArea();
        eventDetailsTextArea.setFont(new Font("Maiandra GD", Font.PLAIN, 16));
        eventDetailsTextArea.setBackground(new Color(255, 255, 153));
        eventDetailsTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        eventDetailsTextArea.setEditable(false);

        formPanel.add(eventTypeLabel);
        formPanel.add(eventTypeComboBox);
        formPanel.add(eventLabel);
        formPanel.add(eventComboBox);
        formPanel.add(eventDetailsLabel);
        formPanel.add(new JScrollPane(eventDetailsTextArea));

        // Set Register button for student
        JPanel buttonPanel = new JPanel();
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> registerForEvent());

        // Set back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> backToViewEvent());

        buttonPanel.add(backButton);
        buttonPanel.add(registerButton);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);

        // Connect to database
        connection = Database.MyDataBase.establishConnection();
        // Load the events
        loadEvents();
    }

// Load events based on selected type (today's or upcoming)
    private void loadEvents() {
        eventComboBox.removeAllItems();
        eventDetailsTextArea.setText("");
        String eventType = (String) eventTypeComboBox.getSelectedItem();
        String query = "SELECT * FROM event_create WHERE Date = ? ORDER BY Start_Time, Id";
        if (eventType.equals("Upcoming Events")) {
            query = "SELECT * FROM event_create WHERE Date > ? ORDER BY Date, Start_Time, Id LIMIT 3";
        }
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, todayString);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String eventId = rs.getString("Id");
                    String eventName = rs.getString("Name");
                    eventComboBox.addItem(eventId + " - " + eventName);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Failed to load events: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Display details of the selected event
    private void displayEventDetails() {
        String selectedItem = (String) eventComboBox.getSelectedItem();
        if (selectedItem == null) {
            return;
        }
        String eventId = selectedItem.split(" - ")[0];
        String query = "SELECT * FROM event_create WHERE Id = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, eventId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String eventDetails = String.format(
                            "Event ID: %s\nTitle: %s\nDescription: %s\nVenue: %s\nDate: %s\nStart Time: %s\nEnd Time: %s",
                            rs.getString("Id"),
                            rs.getString("Name"),
                            rs.getString("Description"),
                            rs.getString("Venue"),
                            rs.getString("Date"),
                            rs.getString("Start_Time"),
                            rs.getString("End_Time")
                    );
                    eventDetailsTextArea.setText(eventDetails);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Failed to load event details: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Register for the selected event
    private void registerForEvent() {
        String selectedItem = (String) eventComboBox.getSelectedItem();
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(frame, "Please select an event to register.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String eventId = selectedItem.split(" - ")[0];
        String eventDate = null;
        LocalTime eventStartTime = null;

        // Get the start time and date for the selected event 
        String eventQuery = "SELECT Date, Start_Time FROM event_create WHERE Id = ?";
        try (PreparedStatement pst = connection.prepareStatement(eventQuery)) {
            pst.setString(1, eventId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    eventDate = rs.getString("Date");
                    eventStartTime = rs.getTime("Start_Time").toLocalTime();
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Failed to get event date and time: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (eventDate == null || eventStartTime == null) {
            JOptionPane.showMessageDialog(frame, "Event date or start time is not available.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the event is today's event and if it has already started
        if (eventDate.equals(todayString)) {
            LocalTime currentTime = LocalTime.now();
            if (currentTime.isAfter(eventStartTime)) {
                JOptionPane.showMessageDialog(frame, "The event has already started and cannot be registered.",
                        "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Check if already registered for the same event
        String existingEventRegSql = "SELECT COUNT(*) AS count FROM event_reg WHERE stu_username = ? AND event_id = ?";
        try (PreparedStatement existingEventRegPst = connection.prepareStatement(existingEventRegSql)) {
            existingEventRegPst.setString(1, loggedInUsername);
            existingEventRegPst.setString(2, eventId);
            try (ResultSet existingEventRegRs = existingEventRegPst.executeQuery()) {
                if (existingEventRegRs.next()) {
                    int count = existingEventRegRs.getInt("count");
                    if (count > 0) {
                        JOptionPane.showMessageDialog(frame,
                                "You have already registered for the event '" + getEventTitle(eventId) + "'.\n"
                                + "You cannot register for the same event multiple times.",
                                "Registration Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Failed to check existing event registration: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if there are events already scheduled on the selected date
        String existingEventSql = "SELECT Name FROM event_create WHERE Id IN "
                + "(SELECT event_id FROM event_reg WHERE stu_username = ? AND Date = ?)";
        try (PreparedStatement existingEventPst = connection.prepareStatement(existingEventSql)) {
            existingEventPst.setString(1, loggedInUsername);
            existingEventPst.setString(2, eventDate);
            try (ResultSet existingEventRs = existingEventPst.executeQuery()) {
                if (existingEventRs.next()) {
                    String existingEventName = existingEventRs.getString("Name");
                    String errorMessage = String.format(
                            "Sorry, you cannot register for another event on the same date.\n"
                            + "Existing Event: %s\nEvent Date: %s", existingEventName, eventDate);
                    JOptionPane.showMessageDialog(frame, errorMessage,
                            "Registration Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Failed to check existing events: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the parent has already booked for today
        String parentBookingSql = "SELECT COUNT(*) AS count FROM bookings WHERE child_id = ? AND booking_date = ?";
        try (PreparedStatement parentPst = connection.prepareStatement(parentBookingSql)) {
            parentPst.setString(1, loggedInUsername);
            parentPst.setString(2, eventDate);
            try (ResultSet parentRs = parentPst.executeQuery()) {
                if (parentRs.next()) {
                    int count = parentRs.getInt("count");
                    if (count > 0) {
                        JOptionPane.showMessageDialog(frame,
                                "Sorry, your parent has already made a booking for " + eventDate
                                + "\nYou cannot register for any events on this day.",
                                "Registration Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Failed to check parent booking: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Register the student for the event
        String insertSql = "INSERT INTO event_reg (event_id, stu_username, reg_date) VALUES (?, ?, ?)";
        try (PreparedStatement insertPst = connection.prepareStatement(insertSql)) {
            insertPst.setString(1, eventId);
            insertPst.setString(2, loggedInUsername);
            insertPst.setString(3, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            insertPst.executeUpdate();

            updateLeaderboard(getEventTitle(eventId), 5);

            String message = "<html><div style='font-family: Maiandra GD; font-size: 12px; text-align: center;'>"
                    + "Congratulations!<br>You have successfully registered for the event '" + getEventTitle(eventId)
                    + "'.<br>You have earned 5 points.<br><br>"
                    + "<b>Your current points: " + getCurrentPoints() + "</b></div></html>";
            JOptionPane.showMessageDialog(frame, message, "Registration Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Failed to check existing registration: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Get the title of the event
    private String getEventTitle(String eventId) {
        String eventTitle = "";
        String eventTitleSql = "SELECT Name FROM event_create WHERE Id = ?";
        try (PreparedStatement eventTitlePst = connection.prepareStatement(eventTitleSql)) {
            eventTitlePst.setString(1, eventId);
            try (ResultSet eventTitleRs = eventTitlePst.executeQuery()) {
                if (eventTitleRs.next()) {
                    eventTitle = eventTitleRs.getString("Name");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Failed to get event title: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return eventTitle;
    }

// Get the start time of the event
    private String getEventStartTime(String eventId) {
        String startTime = "";
        String startTimeSql = "SELECT Start_Time FROM event_create WHERE Id = ?";
        try (PreparedStatement startTimePst = connection.prepareStatement(startTimeSql)) {
            startTimePst.setString(1, eventId);
            try (ResultSet startTimeRs = startTimePst.executeQuery()) {
                if (startTimeRs.next()) {
                    Time startTimeValue = startTimeRs.getTime("Start_Time");
                    LocalTime localStartTime = startTimeValue.toLocalTime();
                    startTime = localStartTime.format(DateTimeFormatter.ofPattern("HH:mm"));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Failed to get event start time: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return startTime;
    }

    // Get the current points of the user
    private int getCurrentPoints() {
        int currentPoints = 0;
        String selectSql = "SELECT points FROM leaderboard WHERE stu_name = ?";
        try (PreparedStatement selectPst = connection.prepareStatement(selectSql)) {
            selectPst.setString(1, loggedInUsername);
            try (ResultSet selectRs = selectPst.executeQuery()) {
                if (selectRs.next()) {
                    currentPoints = selectRs.getInt("points");
                } else {
                    System.out.println("User not found in leaderboard.");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error getting current points: " + ex.getMessage());
        }
        return currentPoints;
    }

    // Update the leaderboard with new points
    private void updateLeaderboard(String eventName, int points) {
        String selectSql = "SELECT * FROM leaderboard WHERE stu_name = ?";
        String updateSql = "UPDATE leaderboard SET points = ?, pointLastUpdated = ? WHERE stu_name = ?";
        try (PreparedStatement selectPst = connection.prepareStatement(selectSql);
                PreparedStatement updatePst = connection.prepareStatement(updateSql)) {
            selectPst.setString(1, loggedInUsername);
            try (ResultSet selectRs = selectPst.executeQuery()) {
                if (selectRs.next()) {
                    int currentPoints = selectRs.getInt("points");
                    int newPoints = currentPoints + points;
                    updatePst.setInt(1, newPoints);
                    updatePst.setString(2, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    updatePst.setString(3, loggedInUsername);
                    updatePst.executeUpdate();
                } else {
                    System.out.println("User not found in leaderboard.");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error updating leaderboard: " + ex.getMessage());
        }
    }

    // Go back to the view event window
    private void backToViewEvent() {
        viewEventNew viewEvent = new viewEventNew(loggedInUsername, currentUserType);
        frame.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegisterEvent registerEvent = new RegisterEvent("username", "Student");
        });
    }
}
