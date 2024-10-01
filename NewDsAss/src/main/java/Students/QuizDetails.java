package Students;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.JTextComponent;

public class QuizDetails extends JFrame {
    private JLabel titleLabel;
    private JLabel themeLabel;
    private JTextArea descriptionTextArea;
    private JTextArea contentTextArea;
    private JButton backButton;
    private JButton submitButton;
    private int quizId;
    private String loggedInUsername;

    // Constructor to initialize QuizDetails JFrame and set up GUI
    public QuizDetails(int quizId, String loggedInUsername) {
        this.quizId = quizId;
        this.loggedInUsername = loggedInUsername;
        
        // Fetch quiz details from the database
        fetchQuizDetails();
        
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle("Quiz Details"); // Windows title

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);

        // Create and customize content panel for quiz details
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                "Quiz Details", // Panel's title
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Maiandra GD", Font.BOLD, 30),
                Color.DARK_GRAY));
        contentPanel.setBackground(new Color(255, 255, 224)); // Light yellow background
        contentPanel.setPreferredSize(new Dimension(600, 400));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add Title, Theme, Description and Content(Link) inside content panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(titleLabel, gbc);

        gbc.gridy++; // Next row
        contentPanel.add(themeLabel, gbc);

        gbc.gridy++;
        contentPanel.add(new JScrollPane(descriptionTextArea), gbc);

        gbc.gridy++;
        contentPanel.add(new JScrollPane(contentTextArea), gbc);

        // Create and handle "Back" button
        backButton = new JButton("Back");
        backButton.setBackground(new Color(0, 51, 102)); // Darker ocean blue
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Maiandra GD", Font.BOLD, 20)); 
        backButton.setPreferredSize(new Dimension(100, 45)); 
        backButton.addActionListener(e -> {
            new Quizz(loggedInUsername);
            dispose();
        });
        
        // Create and handle "Submit" button
        submitButton = new JButton("Submit");
        submitButton.setBackground(new Color(51, 204, 51)); // Forest green background
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Maiandra GD", Font.BOLD, 20)); 
        submitButton.setPreferredSize(new Dimension(100, 45)); 
        submitButton.addActionListener(e -> submitQuiz());

        // Add "Back" and "Submit" button into button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(backButton);
        buttonPanel.add(submitButton);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);

        setVisible(true);
    }

    // Fetch quiz details from the database
    private void fetchQuizDetails() {

        try (Connection conn = Database.MyDataBase.establishConnection();) {
            String quizDetailsSql = "SELECT Title, Description, Theme, Content FROM quiz_create WHERE Id = ?";
            PreparedStatement quizDetailsStmt = conn.prepareStatement(quizDetailsSql);
            quizDetailsStmt.setInt(1, quizId);
            ResultSet rs = quizDetailsStmt.executeQuery();

            if (rs.next()) {
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String theme = rs.getString("Theme");
                String content = rs.getString("Content");

                titleLabel = new JLabel("Title: " + title);
                titleLabel.setFont(new Font("Maiandra GD", Font.BOLD, 50));

                themeLabel = new JLabel("Theme: " + theme);
                themeLabel.setFont(new Font("Maiandra GD", Font.PLAIN, 35));

                descriptionTextArea = new JTextArea(description);
                descriptionTextArea.setLineWrap(true); // Move to next line if not visible thus no need horizontal scroll
                descriptionTextArea.setWrapStyleWord(true); // Move whole word to next line
                descriptionTextArea.setEditable(false);
                descriptionTextArea.setBorder(BorderFactory.createTitledBorder("Description"));
                descriptionTextArea.setFont(new Font("Maiandra GD", Font.PLAIN, 30));

                contentTextArea = new JTextArea(content);
                contentTextArea.setLineWrap(true);
                contentTextArea.setWrapStyleWord(true);
                contentTextArea.setEditable(false);
                contentTextArea.setBorder(BorderFactory.createTitledBorder("Content"));
                contentTextArea.setFont(new Font("Maiandra GD", Font.PLAIN, 30));

                // Enable clickable links in contentTextArea
                enableClickableLinks(contentTextArea);

                // Initialize buttons
                initButtons();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

            // Initialize Back and Submit buttons
            private void initButtons() {
                backButton = new JButton("Back");
                backButton.setBackground(new Color(0, 51, 102)); // Darker ocean blue
                backButton.setForeground(Color.WHITE);
                backButton.setFont(new Font("Maiandra GD", Font.BOLD, 20)); 
                backButton.setPreferredSize(new Dimension(100, 45)); 
                backButton.addActionListener(e -> {
                    dispose();
                    new Quizz(loggedInUsername);
                });

                submitButton = new JButton("Submit");
                submitButton.setBackground(new Color(51, 204, 51)); // Forest green background
                submitButton.setForeground(Color.WHITE);
                submitButton.setFont(new Font("Maiandra GD", Font.BOLD, 20)); 
                submitButton.setPreferredSize(new Dimension(100, 45));  
                submitButton.addActionListener(e -> submitQuiz());
              }


    // Make the link in Content text area to be clickable
    private void enableClickableLinks(JTextComponent textComponent) {
        String text = textComponent.getText();
        // Regular Expression Pattern for a valid URL
        Pattern pattern = Pattern.compile("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String url = text.substring(start, end);
            textComponent.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // action triggered by single mouse click
                    if (e.getClickCount() == 1) {
                        try {
                            Desktop.getDesktop().browse(new java.net.URI(url));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    // Method to submit the Quiz after done and update database
    private void submitQuiz() {

        try (Connection conn = Database.MyDataBase.establishConnection()) {
            // Update quiz_status table after completed a quiz
            String updateStatusSql = "UPDATE quiz_status SET status = 'Completed', completeTime = ? WHERE stu_name = ? AND quiz_id = ?";
            PreparedStatement updateStatusStmt = conn.prepareStatement(updateStatusSql);
            updateStatusStmt.setString(1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            updateStatusStmt.setString(2, loggedInUsername);
            updateStatusStmt.setInt(3, quizId);
            int rowsAffected = updateStatusStmt.executeUpdate();

             // Insert status if the quiz not already present
            if (rowsAffected == 0) {
                String insertStatusSql = "INSERT INTO quiz_status (stu_name, quiz_id, status, completeTime) VALUES (?, ?, 'Completed', ?)";
                PreparedStatement insertStatusStmt = conn.prepareStatement(insertStatusSql);
                insertStatusStmt.setString(1, loggedInUsername);
                insertStatusStmt.setInt(2, quizId);
                insertStatusStmt.setString(3, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                insertStatusStmt.executeUpdate();
            }

            // Update leaderboard table which add 2 points for every quiz done
            String updatePointsSql = "UPDATE leaderboard SET points = points + 2, pointLastUpdated = ? WHERE stu_name = ?";
            PreparedStatement updatePointsStmt = conn.prepareStatement(updatePointsSql);
            updatePointsStmt.setString(1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            updatePointsStmt.setString(2, loggedInUsername);
            updatePointsStmt.executeUpdate();

            // Fetch current points of user from database
            String getPointsSql = "SELECT points FROM leaderboard WHERE stu_name = ?";
            PreparedStatement getPointsStmt = conn.prepareStatement(getPointsSql);
            getPointsStmt.setString(1, loggedInUsername);
            ResultSet resultSet = getPointsStmt.executeQuery();

            int points = 0;
            if (resultSet.next()) {
                points = resultSet.getInt("points");
            }

            // Message upon submitted a quiz
            String message = "<html><div style='font-family: Maiandra GD; font-size: 12px; text-align: center;'>" +
                    "Congratulations!<br>You have successfully completed the quiz.<br>" +
                    "You have earned 2 points.<br><br>" +
                    "<b>Your current points: " + points + "</b></div></html>";
            JOptionPane.showMessageDialog(this, message, "Quiz Completed", JOptionPane.INFORMATION_MESSAGE);

            // Open Quizz page after the message
            new Quizz(loggedInUsername);
            setVisible(false);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            QuizDetails quizDetails = new QuizDetails(1, "JohnDoe");
        });
    }
}
     
