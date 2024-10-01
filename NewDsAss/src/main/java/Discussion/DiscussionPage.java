// DiscussionPage.java
package Discussion;

import Booking.User;
import Login.Main;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiscussionPage extends JFrame {
    private JPanel discussionPanel;
    private JTextField postField;
    private JButton postButton;
    private JButton backButton;
    private User currentUser;
    private List<DiscussionPost> discussions;
    private JScrollPane scrollPane;

    // Constructor for creating DiscussionPage with username and userType
    public DiscussionPage(String loggedInUsername, String currentUserType) {
        currentUser = new User("", loggedInUsername, "", currentUserType); // Assuming User class is available
        initializeFrame();
    }

    // Constructor for creating DiscussionPage with existing User object
    public DiscussionPage(User user) {
        currentUser = user;
        initializeFrame();
    }

    // Method to initialize the frame components
    private void initializeFrame() {
        setTitle("Discussion Page");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize discussions list
        discussions = new ArrayList<>();

        // Create and add title label
        JLabel titleLabel = new JLabel("Discussion Page");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Create and add discussion panel with scroll pane
        discussionPanel = new JPanel();
        discussionPanel.setLayout(new BoxLayout(discussionPanel, BoxLayout.Y_AXIS));
        discussionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        scrollPane = new JScrollPane(discussionPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Disable horizontal scrolling
        add(scrollPane, BorderLayout.CENTER);

        // Create and add components for posting
        postField = new JTextField();
        postField.setFont(new Font("Arial", Font.PLAIN, 14));
        postField.setBorder(new EmptyBorder(5, 5, 5, 5));

        postButton = new JButton("Post");
        postButton.setFont(new Font("Arial", Font.BOLD, 14));
        postButton.addActionListener(new PostButtonListener());

        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.addActionListener(new BackButtonListener());

        JPanel postPanel = new JPanel(new BorderLayout(5, 5));
        postPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        postPanel.add(postField, BorderLayout.CENTER);
        postPanel.add(postButton, BorderLayout.EAST);
        postPanel.add(backButton, BorderLayout.WEST);

        add(postPanel, BorderLayout.SOUTH);

        // Load discussions from database
        loadAndDisplayDiscussions();
    }

    // Method to load and display discussions
    private void loadAndDisplayDiscussions() {
        List<DiscussionPost> discussions = loadDiscussionsFromDatabase();
        displayDiscussions(discussions);
    }

    // Method to load discussions from the database
    private List<DiscussionPost> loadDiscussionsFromDatabase() {
        List<DiscussionPost> discussions = new ArrayList<>();
        String query = "SELECT id, username, role, content, post_date FROM discussion ORDER BY post_date";
        try (Connection conn = Database.MyDataBase.establishConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String role = rs.getString("role");
                String content = rs.getString("content");
                Timestamp timestamp = rs.getTimestamp("post_date");
                discussions.add(new DiscussionPost(id, username, role, content, timestamp));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return discussions;
    }

    // Method to display discussions in the UI
    private void displayDiscussions(List<DiscussionPost> discussions) {
        discussionPanel.removeAll(); // Clear the panel before adding new posts

        for (DiscussionPost post : discussions) {
            discussionPanel.add(createPostPanel(post));
            discussionPanel.add(Box.createVerticalStrut(10)); // Add space between posts
        }

        discussionPanel.revalidate();
        discussionPanel.repaint();
    }

    // Method to create a panel for a discussion post
    private JPanel createPostPanel(DiscussionPost post) {
        JPanel postPanel = new JPanel(new BorderLayout());
        postPanel.setBorder(new LineBorder(Color.GRAY, 1, true));
        postPanel.setBackground(Color.WHITE);
        postPanel.setBorder(BorderFactory.createCompoundBorder(
                postPanel.getBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel userLabel = new JLabel(post.getUsername() + " (" + post.getRole() + ") [" + post.getTimestamp() + "]: ");
        userLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JTextArea contentArea = new JTextArea(post.getContent());
        contentArea.setFont(new Font("Arial", Font.PLAIN, 14));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setBackground(Color.WHITE);
        contentArea.setBorder(null);

        JScrollPane contentScrollPane = new JScrollPane(contentArea);
        contentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentScrollPane.setBorder(null);

        // Create and configure the "View Comments" button
        JButton viewCommentsButton = new JButton("View Comments");
        viewCommentsButton.setFont(new Font("Arial", Font.PLAIN, 12));
        viewCommentsButton.setPreferredSize(new Dimension(120, 25)); // Smaller button
        viewCommentsButton.addActionListener(e -> openPostDetails(post));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(userLabel, BorderLayout.CENTER);
        headerPanel.add(viewCommentsButton, BorderLayout.EAST);

        postPanel.add(headerPanel, BorderLayout.NORTH);
        postPanel.add(contentScrollPane, BorderLayout.CENTER);

        return postPanel;
    }

    // Method to open post details when the "View Comments" button is clicked
    private void openPostDetails(DiscussionPost post) {
        JFrame postDetailsFrame = new JFrame("Post Details");
        postDetailsFrame.setSize(600, 400); // Increased the size of the frame
        postDetailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        postDetailsFrame.setLocationRelativeTo(this);
        postDetailsFrame.setLayout(new BorderLayout());

        // Create a text area for the post content and make it larger
        JTextArea postContentArea = new JTextArea(post.getUsername() + " (" + post.getRole() + ") [" + post.getTimestamp() + "]:\n\n" + post.getContent());
        postContentArea.setFont(new Font("Arial", Font.BOLD, 14));
        postContentArea.setLineWrap(true);
        postContentArea.setWrapStyleWord(true);
        postContentArea.setEditable(false);
        postContentArea.setBackground(Color.WHITE);
        postContentArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Add the post content area to a scroll pane
        JScrollPane postContentScrollPane = new JScrollPane(postContentArea);
        postContentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        postContentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel commentPanel = new JPanel();
        commentPanel.setLayout(new BoxLayout(commentPanel, BoxLayout.Y_AXIS));
        commentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane commentScrollPane = new JScrollPane(commentPanel);
        commentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        commentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        postDetailsFrame.add(postContentScrollPane, BorderLayout.NORTH); // Add the post content scroll pane
        postDetailsFrame.add(commentScrollPane, BorderLayout.CENTER);

        JTextField commentField = new JTextField();
        commentField.setFont(new Font("Arial", Font.PLAIN, 14));
        commentField.setBorder(new EmptyBorder(5, 5, 5, 5));

        JButton commentButton = new JButton("Comment");
        commentButton.setFont(new Font("Arial", Font.BOLD, 14));
        commentButton.addActionListener(e -> {
            String commentContent = commentField.getText().trim();
            if (!commentContent.isEmpty()) {
                Comment comment = new Comment(post.getId(), currentUser.getUsername(), commentContent);
                saveComment(comment);
                loadAndDisplayComments(post.getId(), commentPanel, commentScrollPane);
                commentField.setText("");

                // Scroll to the bottom of the page after adding a comment
                SwingUtilities.invokeLater(() -> {
                    JViewport viewport = commentScrollPane.getViewport();
                    Rectangle bottomBounds = viewport.getViewRect();
                    bottomBounds.y = commentPanel.getHeight() - viewport.getHeight();
                    viewport.scrollRectToVisible(bottomBounds);
                });
            }
        });

        JPanel commentPostPanel = new JPanel(new BorderLayout(5, 5));
        commentPostPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        commentPostPanel.add(commentField, BorderLayout.CENTER);
        commentPostPanel.add(commentButton, BorderLayout.EAST);

        postDetailsFrame.add(commentPostPanel, BorderLayout.SOUTH);

        loadAndDisplayComments(post.getId(), commentPanel, commentScrollPane);

        postDetailsFrame.setVisible(true);
    }

    // Method to load comments from the database for a specific post
    private List<Comment> loadCommentsFromDatabase(int postId) {
        List<Comment> comments = new ArrayList<>();
        String query = "SELECT username, content, comment_date FROM comments WHERE post_id = ? ORDER BY comment_date";
        try (Connection conn = Database.MyDataBase.establishConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, postId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String username = rs.getString("username");
                    String content = rs.getString("content");
                    Timestamp timestamp = rs.getTimestamp("comment_date");
                    comments.add(new Comment(username, content, timestamp));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return comments;
    }

    // Method to display comments in the UI
    private void displayComments(int postId, List<Comment> comments, JPanel commentPanel) {
        commentPanel.removeAll(); // Clear the panel before adding new comments

        for (Comment comment : comments) {
            JPanel commentPanelContainer = new JPanel(new BorderLayout());
            commentPanelContainer.setBorder(new LineBorder(Color.GRAY, 1, true));
            commentPanelContainer.setBackground(Color.WHITE);
            commentPanelContainer.setBorder(BorderFactory.createCompoundBorder(
                    commentPanelContainer.getBorder(),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            JLabel userLabel = new JLabel(comment.getUsername() + " [" + comment.getTimestamp() + "]: ");
            userLabel.setFont(new Font("Arial", Font.BOLD, 12));

            JTextArea contentArea = new JTextArea(comment.getContent());
            contentArea.setFont(new Font("Arial", Font.PLAIN, 14));
            contentArea.setLineWrap(true);
            contentArea.setWrapStyleWord(true);
            contentArea.setEditable(false);
            contentArea.setBackground(Color.WHITE);
            contentArea.setBorder(null);

            JScrollPane contentScrollPane = new JScrollPane(contentArea);
            contentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            contentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            contentScrollPane.setBorder(null);

            commentPanelContainer.add(userLabel, BorderLayout.NORTH);
            commentPanelContainer.add(contentScrollPane, BorderLayout.CENTER);

            commentPanel.add(commentPanelContainer);
            commentPanel.add(Box.createVerticalStrut(10)); // Add space between comments
        }

        commentPanel.revalidate();
        commentPanel.repaint();
    }

    // Method to save a comment to the database
    private void saveComment(Comment comment) {
        String query = "INSERT INTO comments (post_id, username, content) VALUES (?, ?, ?)";
        try (Connection conn = Database.MyDataBase.establishConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, comment.getPostId());
            stmt.setString(2, comment.getUsername());
            stmt.setString(3, comment.getContent());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Method to load and display comments for a specific post
    private void loadAndDisplayComments(int postId, JPanel commentPanel, JScrollPane commentScrollPane) {
        List<Comment> comments = loadCommentsFromDatabase(postId);
        displayComments(postId, comments, commentPanel);

        // Scroll to the bottom of the comments panel
        SwingUtilities.invokeLater(() -> {
            JScrollBar verticalScrollBar = commentScrollPane.getVerticalScrollBar();
            verticalScrollBar.setValue(verticalScrollBar.getMaximum());
        });
    }


    // ActionListener for the back button
    private class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose(); // Close the current window
        }
    }

    // ActionListener for the post button
    private class PostButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String postContent = postField.getText().trim();
            if (!postContent.isEmpty()) {
                DiscussionPost post = new DiscussionPost(currentUser.getUsername(), currentUser.getRole(), postContent);
                saveDiscussion(post);
                loadAndDisplayDiscussions(); // Refresh the discussion area after posting
                postField.setText("");
            }
        }

        // Method to save a discussion post to the database
        private void saveDiscussion(DiscussionPost post) {
            String query = "INSERT INTO discussion (username, role, content) VALUES (?, ?, ?)";
            try (Connection conn = Database.MyDataBase.establishConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, post.getUsername());
                stmt.setString(2, post.getRole());
                stmt.setString(3, post.getContent());
                stmt.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    
}

// Class representing a discussion post
class DiscussionPost implements Serializable {
    private int id;
    private String username;
    private String role;
    private String content;
    private Timestamp timestamp;

    public DiscussionPost(int id, String username, String role, String content, Timestamp timestamp) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.content = content;
        this.timestamp = timestamp;
    }

    public DiscussionPost(String username, String role, String content) {
        this.username = username;
        this.role = role;
        this.content = content;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    // Getters for the discussion post attributes
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}

// Class representing a comment on a discussion post
class Comment implements Serializable {
    private int postId;
    private String username;
    private String content;
    private Timestamp timestamp;

    public Comment(int postId, String username, String content) {
        this.postId = postId;
        this.username = username;
        this.content = content;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public Comment(String username, String content, Timestamp timestamp) {
        this.username = username;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters for the comment attributes
    public int getPostId() {
        return postId;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
