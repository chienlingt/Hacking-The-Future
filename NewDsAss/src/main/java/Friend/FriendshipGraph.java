package Friend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.List;

class TreeNode {
    String username;
    List<TreeNode> friends;

    public TreeNode(String username) {
        this.username = username;
        this.friends = new ArrayList<>();
    }
}

public class FriendshipGraph extends JFrame {

    private TreeNode root;
    private Connection connection;
    private String loggedInUsername;
    private String currentUserType;
    private Map<String, JButton> userButtons;
    private Set<String> placedButtons;
    private JPanel mainPanel;

    public FriendshipGraph(String loggedInUsername, String currentUserType) {
        this.loggedInUsername = loggedInUsername;
        this.currentUserType = currentUserType;
        root = new TreeNode(loggedInUsername);
        userButtons = new HashMap<>();
        placedButtons = new HashSet<>();

        connection = Database.MyDataBase.establishConnection();

        buildTreeFromDatabase();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Do nothing on window close
            }
        });

        setTitle("Friendship Graph");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the window

        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawLines(g);
            }
        };
        mainPanel.setLayout(null);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane);

        int panelWidth = calculatePanelWidth(root);
        int panelHeight = calculatePanelHeight(root);
        mainPanel.setPreferredSize(new Dimension(panelWidth, panelHeight + 50)); // Adding extra space for back button

        int centerX = panelWidth / 2;
        int startY = 50;

        addButtons(mainPanel, root, centerX, startY);

        // Add "Back" button
        JButton backButton = new JButton("Back");
        backButton.setBounds(0, 20, 80, 30);
        backButton.addActionListener(e -> {
            dispose();
            new Friends(loggedInUsername, currentUserType).setVisible(true);
        });
        mainPanel.add(backButton);

        setVisible(true);
    }

    private void buildTreeFromDatabase() {
        Set<String> visited = new HashSet<>();
        buildTree(root, loggedInUsername, visited);
        // Fetch friends of friends
        for (TreeNode friend : root.friends) {
            buildTree(friend, friend.username, visited);
        }
    }

    private void buildTree(TreeNode node, String username, Set<String> visited) {
        if (visited.contains(username)) {
            return;
        }
        visited.add(username);

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT Friends FROM student WHERE Username = ?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String friendsString = resultSet.getString("Friends");
                if (friendsString != null && !friendsString.isEmpty()) {
                    String[] friendsArray = friendsString.split(","); // Assuming ',' is the delimiter
                    for (String friendUsername : friendsArray) {
                        TreeNode friendNode = new TreeNode(friendUsername.trim());
                        node.friends.add(friendNode);
                        buildTree(friendNode, friendUsername.trim(), visited);
                    }
                }
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching friends from database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void addButtons(JPanel panel, TreeNode node, int x, int y) {
        int buttonWidth = 100;
        int buttonHeight = 30;
        int verticalSpacing = 150;
        int horizontalSpacing = 250; // Increased horizontal spacing

        if (!placedButtons.contains(node.username)) {
            JButton button = new JButton(node.username);
            button.setBounds(x - buttonWidth / 2, y, buttonWidth, buttonHeight);
            panel.add(button);
            userButtons.put(node.username, button);
            placedButtons.add(node.username);

            button.addActionListener(e -> handleButtonClick(node.username));
        }

        // Calculate the y-coordinate based on the depth of the node
        int friendY = y + verticalSpacing;

        // Calculate the total width needed for all friends
        int totalWidth = node.friends.size() * horizontalSpacing;

        // Calculate the starting x-coordinate for positioning friends horizontally
        int startX = x - totalWidth / 2 + (buttonWidth / 2); // Adjusted for centering

        // Position friends horizontally without overlapping
        for (TreeNode friend : node.friends) {
            JButton friendButton = userButtons.get(friend.username);
            int friendX = startX + node.friends.indexOf(friend) * horizontalSpacing;
            // Add the friend button and recursively add their friends
            addButtons(panel, friend, friendX, friendY);
        }
    }


    private int calculatePanelWidth(TreeNode node) {
        int horizontalSpacing = 400; // Increased horizontal spacing
        return Math.max((calculateMaxWidth(node)) * horizontalSpacing, getWidth());
    }

    private int calculatePanelHeight(TreeNode node) {
        int verticalSpacing = 200; // Increased vertical spacing
        return Math.max((calculateMaxDepth(node) + 1) * verticalSpacing, getHeight());
    }

    private int calculateMaxWidth(TreeNode node) {
        int maxWidth = 1;
        for (TreeNode friend : node.friends) {
            maxWidth += calculateMaxWidth(friend);
        }
        return maxWidth;
    }

    private int calculateMaxDepth(TreeNode node) {
        int maxDepth = 0;
        for (TreeNode friend : node.friends) {
            maxDepth = Math.max(maxDepth, calculateMaxDepth(friend) + 1);
        }
        return maxDepth;
    }

    private void drawLines(Graphics g) {
        // Draw lines between the logged-in user and their direct friends first
        drawDirectFriendsLines(g, root);

        // Draw lines between friends and their friends
        drawLinesHelper(g, root);
    }

    private void drawDirectFriendsLines(Graphics g, TreeNode node) {
        JButton nodeButton = userButtons.get(node.username);
        if (nodeButton == null) {
            return;
        }
        Point nodePoint = nodeButton.getLocation();
        g.setColor(Color.BLUE.darker()); // Dark blue color for direct friends
        for (TreeNode friend : node.friends) {
            JButton friendButton = userButtons.get(friend.username);
            if (friendButton != null) {
                Point friendPoint = friendButton.getLocation();
                g.drawLine(nodePoint.x + 50, nodePoint.y + 25, friendPoint.x + 50, friendPoint.y + 25);
            }
        }
    }

    private void drawLinesHelper(Graphics g, TreeNode node) {
        for (TreeNode friend : node.friends) {
            drawLinesHelper(g, friend);
        }
        JButton nodeButton = userButtons.get(node.username);
        if (nodeButton == null) {
            return;
        }
        Point nodePoint = nodeButton.getLocation();
        for (TreeNode friend : node.friends) {
            JButton friendButton = userButtons.get(friend.username);
            if (friendButton != null) {
                Point friendPoint = friendButton.getLocation();
                g.setColor(node.username.equals(loggedInUsername) ? Color.BLUE.darker() : Color.BLACK);
                g.drawLine(nodePoint.x + 50, nodePoint.y + 25, friendPoint.x + 50, friendPoint.y + 25);
            }
        }
    }
    
    private void handleButtonClick(String username) {
        try {
            if (username.equals(loggedInUsername)) {
                JOptionPane.showMessageDialog(this, "You");
            } else if (checkFriendship(loggedInUsername, username, "Friends")) {
                JOptionPane.showMessageDialog(this, "Your friend");
            } else if (checkFriendship(loggedInUsername, username, "sent_friends")) {
                int option = JOptionPane.showOptionDialog(this,
                        "Friend request has already been sent to this user.",
                        "Profile Details",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[]{"Delete Friend Request", "Close"},
                        "Delete Friend Request");

                if (option == JOptionPane.YES_OPTION) {
                    deleteFriendRequest(username);
                }
            } else if (checkFriendship(loggedInUsername, username, "Friend_Requests")) {
                JOptionPane.showMessageDialog(this,
                        "You have already received a friend request from this user.",
                        "Profile Details",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                int option = JOptionPane.showOptionDialog(this,
                        "Send friend request to this user?",
                        "Profile Details",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[]{"Send Friend Request", "Close"},
                        "Send Friend Request");

                if (option == JOptionPane.YES_OPTION) {
                    sendFriendRequest(username);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error accessing database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private boolean checkFriendship(String username, String friend, String columnName) throws SQLException {
        String query = "SELECT * FROM student WHERE Username=? AND " + columnName + " LIKE ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, username);
        pstmt.setString(2, "%" + friend + "%");
        ResultSet rs = pstmt.executeQuery();
        return rs.next();
    }

    private void deleteFriendRequest(String friendUsername) throws SQLException {
        String deleteFriendRequestsQuery = "UPDATE student SET Friend_Requests = REPLACE(Friend_Requests, ?, '') WHERE Username = ?";
        PreparedStatement deleteFriendRequestsStmt = connection.prepareStatement(deleteFriendRequestsQuery);
        deleteFriendRequestsStmt.setString(1, loggedInUsername + ",");
        deleteFriendRequestsStmt.setString(2, friendUsername);
        deleteFriendRequestsStmt.executeUpdate();

        String deleteSentFriendsQuery = "UPDATE student SET sent_friends = REPLACE(sent_friends, ?, '') WHERE Username = ?";
        PreparedStatement deleteSentFriendsStmt = connection.prepareStatement(deleteSentFriendsQuery);
        deleteSentFriendsStmt.setString(1, friendUsername + ",");
        deleteSentFriendsStmt.setString(2, loggedInUsername);
        deleteSentFriendsStmt.executeUpdate();

        JOptionPane.showMessageDialog(this, "Friend request deleted successfully!");
    }

    private void sendFriendRequest(String friendUsername) throws SQLException {
        String sentFriendsQuery = "UPDATE student SET sent_friends = CONCAT(IFNULL(sent_friends, ''), ?) WHERE Username=?";
        PreparedStatement sentFriendsStmt = connection.prepareStatement(sentFriendsQuery);
        sentFriendsStmt.setString(1, friendUsername + ",");
        sentFriendsStmt.setString(2, loggedInUsername);
        sentFriendsStmt.executeUpdate();

        String friendRequestQuery = "UPDATE student SET Friend_Requests = CONCAT(IFNULL(Friend_Requests, ''), ?) WHERE Username=?";
        PreparedStatement friendRequestStmt = connection.prepareStatement(friendRequestQuery);
        friendRequestStmt.setString(1, loggedInUsername + ",");
        friendRequestStmt.setString(2, friendUsername);
        friendRequestStmt.executeUpdate();

        JOptionPane.showMessageDialog(this, "Friend request sent successfully!");
    }
}
