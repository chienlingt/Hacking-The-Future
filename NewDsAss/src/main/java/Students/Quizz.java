package Students;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Quizz extends JFrame {

    private JCheckBox scienceCheckBox;
    private JCheckBox techCheckBox;
    private JCheckBox engineeringCheckBox;
    private JCheckBox mathCheckBox;
    private JPanel quizListPanel;
    private String username;
    private JTextField totalQuizField;
    private JTextField completedQuizField;
    private JTextField totalPointsField;

    // Constructor to initialize the Quizz JFrame and set the GUI
    public Quizz(String username) {
        this.username = username;
        
        setTitle("Quiz"); // Windows title
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        
        // Create top panel with title "Quiz", themes and summary on top of window
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE); 
        JLabel quizLabel = new JLabel("QUIZ", SwingConstants.CENTER);
        quizLabel.setOpaque(true);
        quizLabel.setForeground(new Color(0, 153, 204)); 
        quizLabel.setBackground(Color.WHITE);
        quizLabel.setFont(new Font("Maiandra GD", Font.BOLD, 48));
        topPanel.add(quizLabel, BorderLayout.NORTH);

        // Create panel to select themes with themes checkboxes
        JPanel themePanel = new JPanel(new BorderLayout());
        themePanel.setBackground(Color.WHITE); 
        JPanel themeSubPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        themeSubPanel.setBackground(Color.WHITE); 

        JLabel themesLabel = new JLabel("Themes:");
        themesLabel.setFont(new Font("Maiandra GD", Font.BOLD, 16)); 
        scienceCheckBox = new JCheckBox("Science");
        techCheckBox = new JCheckBox("Technology");
        engineeringCheckBox = new JCheckBox("Engineering");
        mathCheckBox = new JCheckBox("Mathematics");

        scienceCheckBox.setBackground(Color.WHITE);
        techCheckBox.setBackground(Color.WHITE);
        engineeringCheckBox.setBackground(Color.WHITE);
        mathCheckBox.setBackground(Color.WHITE);
        
        Font checkboxFont = new Font("Maiandra GD", Font.BOLD, 16);
        scienceCheckBox.setFont(checkboxFont);
        techCheckBox.setFont(checkboxFont);
        engineeringCheckBox.setFont(checkboxFont);
        mathCheckBox.setFont(checkboxFont);

        // default is all themes selected
        scienceCheckBox.setSelected(true);
        techCheckBox.setSelected(true);
        engineeringCheckBox.setSelected(true);
        mathCheckBox.setSelected(true);

        themeSubPanel.add(themesLabel);
        themeSubPanel.add(scienceCheckBox);
        themeSubPanel.add(techCheckBox);
        themeSubPanel.add(engineeringCheckBox);
        themeSubPanel.add(mathCheckBox);

        // Create panel for summary of total quiz, completed quiz and total points
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        summaryPanel.setBackground(Color.WHITE);

        JLabel totalQuizLabel = new JLabel("Total Quiz:");
        totalQuizField = new JTextField(5);
        totalQuizField.setEditable(false);
        JLabel completedQuizLabel = new JLabel("Completed:");
        completedQuizField = new JTextField(5);
        completedQuizField.setEditable(false);
        JLabel totalPointsLabel = new JLabel("Total Points:");
        totalPointsField = new JTextField(5);
        totalPointsField.setEditable(false);

        Font summaryFont = new Font("Maiandra GD", Font.BOLD, 16);
        totalQuizLabel.setFont(summaryFont);
        completedQuizLabel.setFont(summaryFont);
        totalPointsLabel.setFont(summaryFont);
        totalQuizField.setFont(summaryFont);
        completedQuizField.setFont(summaryFont);
        totalPointsField.setFont(summaryFont);

        summaryPanel.add(totalQuizLabel);
        summaryPanel.add(totalQuizField);
        summaryPanel.add(completedQuizLabel);
        summaryPanel.add(completedQuizField);
        summaryPanel.add(totalPointsLabel);
        summaryPanel.add(totalPointsField);

        themePanel.add(themeSubPanel, BorderLayout.WEST);
        themePanel.add(summaryPanel, BorderLayout.EAST);
        topPanel.add(themePanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Create panel to display all the quizzes
        quizListPanel = new JPanel(new GridBagLayout()); 
        quizListPanel.setBackground(Color.WHITE); 
        JScrollPane scrollPane = new JScrollPane(quizListPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
        scrollPane.getViewport().setBackground(Color.WHITE); 
        add(scrollPane, BorderLayout.CENTER);

        // Create bottom panel for "Back" button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0)); 
        bottomPanel.setBackground(Color.WHITE); 
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 45)); 
        backButton.setBackground(new Color(204, 229, 255)); 
        backButton.setForeground(Color.BLACK); 
        backButton.setFont(new Font("Maiandra GD", Font.BOLD, 14)); 
        backButton.addActionListener(e -> backToMain());
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Filter quizzes based on themes selected
        filterQuizzes();

        // Add action listeners to checkboxes for filtering quizzes dynamically according to themes selected
        scienceCheckBox.addActionListener(e -> filterQuizzes());
        techCheckBox.addActionListener(e -> filterQuizzes());
        engineeringCheckBox.addActionListener(e -> filterQuizzes());
        mathCheckBox.addActionListener(e -> filterQuizzes());

        setVisible(true);
    }
    
    //  Method to filter quizzes based on selected themes
    private void filterQuizzes() {
        
        String selectedThemes = "";
        if (scienceCheckBox.isSelected()) selectedThemes += "'Science', ";
        if (techCheckBox.isSelected()) selectedThemes += "'Technology', ";
        if (engineeringCheckBox.isSelected()) selectedThemes += "'Engineering', ";
        if (mathCheckBox.isSelected()) selectedThemes += "'Mathematics', ";

        if (!selectedThemes.isEmpty()) {
            selectedThemes = selectedThemes.substring(0, selectedThemes.length() - 2); 
        }

        // ArrayList for storing all quizzes and filtered quizzes based on themes selected
        List<Quiz> allQuizzes = new ArrayList<>();
        List<Quiz> filteredQuizzes = new ArrayList<>();

        String query = "SELECT qs.quiz_id, qc.Title, qc.Theme, qc.Username, qs.status " +
                "FROM quiz_status qs " +
                "JOIN quiz_create qc ON qs.quiz_id = qc.Id " +
                "WHERE qs.stu_name = ? " +
                "ORDER BY qs.quiz_id ASC";

        try (Connection conn = Database.MyDataBase.establishConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int quizId = rs.getInt("quiz_id");
                String title = rs.getString("Title");
                String theme = rs.getString("Theme");
                String eduName = rs.getString("Username");
                String status = rs.getString("status");

                Quiz quiz = new Quiz(quizId, title, theme, eduName, status);
                allQuizzes.add(quiz);

                if (selectedThemes.contains("'" + theme + "'")) {
                    filteredQuizzes.add(quiz);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }

        // Show the summary of total quiz and completed quiz
        int totalQuizzes = allQuizzes.size();
        long completedQuizzes = allQuizzes.stream().filter(quiz -> quiz.status.equals("Completed")).count();
        totalQuizField.setText(String.valueOf(totalQuizzes));
        completedQuizField.setText(String.valueOf(completedQuizzes));

        // Fetch and show the latest point 
        int totalPoints = fetchTotalPoints();
        totalPointsField.setText(String.valueOf(totalPoints));

        // Show the quiz based on the order of quiz Id
        filteredQuizzes.sort(Comparator.comparingInt((Quiz quiz) -> quiz.quizId));

        // Display all the quizzes based on selected themes
        displayQuizzes(filteredQuizzes);
    }

    // Method to fetch points from the leaderbaord table in database
    private int fetchTotalPoints() {
        int totalPoints = 0;
        String query = "SELECT points FROM leaderboard WHERE stu_name = ?";

        try (Connection conn = Database.MyDataBase.establishConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                totalPoints = rs.getInt("points");
            }
            
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return totalPoints;
    }

     // Method to display quizzes in the quiz list panel
    private void displayQuizzes(List<Quiz> quizzes) {
    quizListPanel.removeAll();
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10); 
    gbc.fill = GridBagConstraints.BOTH; 
    gbc.weightx = 1.0; 
    gbc.weighty = 1.0; 

    // Create quiz panel for each quiz and change colour based on the status
    for (int i = 0; i < quizzes.size(); i++) {
        Quiz quiz = quizzes.get(i);
        JPanel quizPanel = new JPanel(new BorderLayout());
        quizPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 10));
        
        // Panel will change to green if the quiz is completed otherwise it remains blue
        quizPanel.setBackground(quiz.status.equals("Completed") ? new Color(210, 255, 173)  : new Color(204, 229, 255)); 
        quizPanel.setPreferredSize(new Dimension(450, 300)); 

        // Set info of each quiz inside the quiz panel
        JLabel quizInfo = new JLabel("<html><div style='font-size:14px; color: black; text-align: center;'>Id: " + quiz.quizId + "<br><b>Title: " + quiz.title + "</b><br>Theme: " + quiz.theme + "<br>Created by: " + quiz.eduName + "<br>Status: " + quiz.status + "</div></html>");
        quizInfo.setHorizontalAlignment(SwingConstants.CENTER); 
        quizPanel.add(quizInfo, BorderLayout.CENTER);

        // Change the button inside quiz panel to COMPLETED is the status is Completed otherwise remain as START
        JButton actionButton = new JButton(quiz.status.equals("Completed") ? "COMPLETED" : "START");
        actionButton.setPreferredSize(new Dimension(150, 35)); 
        actionButton.setBackground(Color.WHITE); 
        actionButton.setForeground(Color.BLACK); 
        // The COMPLETED button will be disabled
        actionButton.setEnabled(!quiz.status.equals("Completed"));
        // When user press the START button it will direct user to QuizDetails page
        actionButton.addActionListener(e -> {
            if (!quiz.status.equals("Completed")) {
                new QuizDetails(quiz.quizId, username); 
                dispose();
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        buttonPanel.setBackground(quizPanel.getBackground());
        buttonPanel.add(actionButton);

        quizPanel.add(buttonPanel, BorderLayout.SOUTH);

        gbc.gridx = i % 2; // 2 columns
        gbc.gridy = i / 2;
        quizListPanel.add(quizPanel, gbc);
    }

    // Make sure the size is fix regardless the number of quizzes to ensure consistensy
    int totalQuizzes = quizzes.size();
    while (totalQuizzes < 4) {
        // Add empty panel if number of quiz show is less than 4
        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(450, 300));
        emptyPanel.setBackground(Color.WHITE);
        gbc.gridx = totalQuizzes % 2;
        gbc.gridy = totalQuizzes / 2;
        quizListPanel.add(emptyPanel, gbc);
        totalQuizzes++;
    }

    quizListPanel.revalidate();
    quizListPanel.repaint();
}

    // Inner class for Quiz object
    private static class Quiz {
        int quizId;
        String title;
        String theme;
        String eduName;
        String status;

        Quiz(int quizId, String title, String theme, String eduName, String status) {
            this.quizId = quizId;
            this.title = title;
            this.theme = theme;
            this.eduName = eduName;
            this.status = status;
        }
    }

    // Handle the action when the user clicks the "Back" button
    private void backToMain() {
        Login.Main main = new Login.Main(username, "Student");
        main.setVisible(true); 
        dispose();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Quizz("loggedInUsername"));
    }
}
