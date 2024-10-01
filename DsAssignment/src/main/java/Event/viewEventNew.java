package Event;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class viewEventNew {

    private String loggedInUsername;
    private String currentUserType;
    public JFrame frame;
    private JTable todayEventTable;
    private JTable upcomingEventTable;
    private JTable myEventTable;
    private DefaultTableModel todayTableModel;
    private DefaultTableModel upcomingTableModel;
    private DefaultTableModel myEventTableModel;
    private LocalDate today; 
    private DateTimeFormatter formatter;
    private String todayString;

    // Constructor for the viewEventNew class
    public viewEventNew(String loggedInUsername, String currentUserType) {
        this.loggedInUsername = loggedInUsername; 
        this.currentUserType = currentUserType; 
        this.today = LocalDate.now(); 
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.todayString = today.format(formatter);

        frame = new JFrame("View Event"); // Windows title
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Set different layout for students and other roles
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(currentUserType.equals("Student") ? 3 : 2, 1));

        // Today's event table visible to every roles
        todayTableModel = new NonEditableTableModel(
                new Object[][]{},
                new String[]{"Event Id", "Title", "Description", "Venue", "Date", "Start Time", "End Time"}
        );
        todayEventTable = new JTable(todayTableModel);
        JScrollPane todayScrollPane = new JScrollPane(todayEventTable);
        todayScrollPane.setPreferredSize(new Dimension(950, 200));
        panel.add(createPanelWithTitle(todayScrollPane, "Today's Event"));

        // Upcoming event table visible to every roles
        upcomingTableModel = new NonEditableTableModel(
                new Object[][]{},
                new String[]{"Event Id", "Title", "Description", "Venue", "Date", "Start Time", "End Time"}
        );
        upcomingEventTable = new JTable(upcomingTableModel);
        JScrollPane upcomingScrollPane = new JScrollPane(upcomingEventTable);
        upcomingScrollPane.setPreferredSize(new Dimension(950, 200));
        panel.add(createPanelWithTitle(upcomingScrollPane, "Upcoming Event"));

        // Only student will see this my event table as only student able to register
        if (currentUserType.equals("Student")) {
            myEventTableModel = new NonEditableTableModel(
                    new Object[][]{},
                    new String[]{"Event Id", "Title", "Description", "Venue", "Date", "Start Time", "End Time"}
            );
            myEventTable = new JTable(myEventTableModel);
            JScrollPane myEventScrollPane = new JScrollPane(myEventTable);
            myEventScrollPane.setPreferredSize(new Dimension(950, 200));
            panel.add(createPanelWithTitle(myEventScrollPane, "My Events"));
        }

        frame.add(panel);
        frame.setVisible(true);

        // Create buttonPanel with "Back" button and "Register" button based on selected role
        JPanel buttonPanel = new JPanel();

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> backToMain());
        buttonPanel.add(backButton);

        // Only student can register for the events thus register button only visible to students
        if (currentUserType.equals("Student")) {
            JButton registerButton = new JButton("Register");
            registerButton.addActionListener(evt -> registerForEvent());
            buttonPanel.add(registerButton);
        }

        frame.add(buttonPanel, BorderLayout.SOUTH);
        
        // Display the events on tables
        displayEvents();
    }

    // Create a panel with a title and the table
    private JPanel createPanelWithTitle(Component component, String title) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title);
        label.setFont(new Font("Maiandra GD", Font.BOLD, 20));
        panel.add(label, BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    // Handle the action when the user clicks the "Back" button
    private void backToMain() {
        Login.Main main = new Login.Main(loggedInUsername, currentUserType);
        main.show();
        frame.dispose();
    }

    // Handle the action when the user clicks the "Register" button (only for students)
    private void registerForEvent() {
        RegisterEvent registerEvent = new RegisterEvent(loggedInUsername, currentUserType);
        frame.dispose();
    }

    // Display events in the tables (today's events, upcoming events, and my events)
    private void displayEvents() {
        String todayEventSql = "SELECT * FROM event_create WHERE Date = ? ORDER BY Start_Time, Id";
        String upcomingEventSql = "SELECT * FROM event_create WHERE Date > ? ORDER BY Date, Start_Time, Id LIMIT 3";
        String myEventSql = "SELECT * FROM event_create WHERE Id IN (SELECT event_id FROM event_reg WHERE stu_username = ?)";

        Connection connection = Database.MyDataBase.establishConnection();

            // Add today's event if there is any
            try (PreparedStatement todayEventPst = connection.prepareStatement(todayEventSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                todayEventPst.setString(1, todayString);
                try (ResultSet todayEventRs = todayEventPst.executeQuery()) {
                    System.out.println("Executed query for today's events");
                    todayTableModel.setRowCount(0);
                    if (!todayEventRs.next()) {
                        System.out.println("No events for today");
                        todayTableModel.addRow(new Object[]{"No events", "", "", "", "", "", ""});
                    } else {
                        todayEventRs.beforeFirst();
                        while (todayEventRs.next()) {
                            addEventToTable(todayTableModel, todayEventRs);
                        }
                    }
                }
            
            // Add 3 upcoming events if there is any
            try (PreparedStatement upcomingEventPst = connection.prepareStatement(upcomingEventSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                upcomingEventPst.setString(1, todayString);
                try (ResultSet upcomingEventRs = upcomingEventPst.executeQuery()) {
                    System.out.println("Executed query for upcoming events");
                    upcomingTableModel.setRowCount(0);
                    if (!upcomingEventRs.next()) {
                        System.out.println("No upcoming events");
                        upcomingTableModel.addRow(new Object[]{"No events", "", "", "", "", "", ""});
                    } else {
                        upcomingEventRs.beforeFirst();
                        while (upcomingEventRs.next()) {
                            addEventToTable(upcomingTableModel, upcomingEventRs);
                        }
                    }
                }
            }
            
            // Add students' registered event in my event table if got any
            if (currentUserType.equals("Student")) {
                try (PreparedStatement myEventPst = connection.prepareStatement(myEventSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                    myEventPst.setString(1, loggedInUsername);
                    try (ResultSet myEventRs = myEventPst.executeQuery()) {
                        System.out.println("Executed query for my events");
                        myEventTableModel.setRowCount(0);
                        if (!myEventRs.next()) {
                            System.out.println("No my events");
                            myEventTableModel.addRow(new Object[]{"No events", "", "", "", "", "", ""});
                        } else {
                            myEventRs.beforeFirst();
                            while (myEventRs.next()) {
                                addEventToTable(myEventTableModel, myEventRs);
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

     // Add a row of event data to the specified table model
    private void addEventToTable(DefaultTableModel tableModel, ResultSet rs) throws SQLException {
        String eventId = rs.getString("Id");
        String eventName = rs.getString("Name");
        String eventDescription = rs.getString("Description");
        String eventVenue = rs.getString("Venue");
        String eventDate = rs.getString("Date");
        String startTimeString = rs.getString("Start_Time");
        String endTimeString = rs.getString("End_Time");

        System.out.println("Adding event to table: " + eventName);

        tableModel.addRow(new Object[]{
            eventId, eventName, eventDescription, eventVenue, eventDate, startTimeString, endTimeString
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            viewEventNew viewEvent = new viewEventNew("username", "Student");
        });
    }
}

// Custom table model class that makes the table cells non-editable
class NonEditableTableModel extends DefaultTableModel {

    public NonEditableTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
