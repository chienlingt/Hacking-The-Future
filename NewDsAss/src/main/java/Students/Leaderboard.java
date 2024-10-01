package Students;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.*;

public class Leaderboard extends JFrame {
    private JTable leaderboardTable;
    private DefaultTableModel tableModel;
    private String currentStudent;

    // Constructor initialize leaderboard and set up GUI
    public Leaderboard(String currentStudent) {
        this.currentStudent = currentStudent;

        setTitle("LEADERBOARD"); // Window title
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

         // Create header with title of table
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.BLACK);

        JLabel leaderboardLabel = new JLabel("LEADERBOARD", SwingConstants.CENTER);
        leaderboardLabel.setFont(new Font("Arial", Font.BOLD, 40));
        leaderboardLabel.setForeground(new Color(255, 215, 0)); //Gold color
        leaderboardLabel.setOpaque(true);
        leaderboardLabel.setBackground(Color.BLACK);
        leaderboardLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        headerPanel.add(leaderboardLabel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // Create leaderboard table
        String[] columns = {"RANK", "USERNAME", "POINTS"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        
        leaderboardTable = new JTable(tableModel);
        leaderboardTable.setFont(new Font("Arial", Font.PLAIN, 20)); 
        leaderboardTable.setRowHeight(30);

        // Disable selection inside table
        leaderboardTable.setCellSelectionEnabled(false);
        leaderboardTable.setRowSelectionAllowed(false);
        leaderboardTable.setColumnSelectionAllowed(false);

        // Customize table header
        JTableHeader header = leaderboardTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 28));
        header.setBackground(Color.GRAY);
        header.setForeground(Color.BLACK);
        header.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        headerRenderer.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
        for (int i = 0; i < header.getColumnModel().getColumnCount(); i++) {
            header.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        // Add table to scroll pane make sure it is able to show rank for all students
        JScrollPane scrollPane = new JScrollPane(leaderboardTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Highlight (set the background color to yellow) the current logged in student's cells
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.CENTER); 

                if (table.getValueAt(row, 1).equals(currentStudent)) {
                    label.setBackground(Color.YELLOW);
                } else {
                    label.setBackground(Color.WHITE);
                }

                label.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY)); 
                return label;
            }
        };
        for (int i = 0; i < leaderboardTable.getColumnCount(); i++) {
            leaderboardTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        // Create footer with "Back" button
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(Color.BLACK);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 18));
        backButton.setFocusPainted(false);
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(Color.BLACK);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backButton.addActionListener(e -> backToMain());

        footerPanel.add(backButton);
        add(footerPanel, BorderLayout.SOUTH); 

        // Load leaderboard data
        populateLeaderboard();
    }

    // Load from database and add the data to leaderboard table according to points and pointLastUpdated
    public void populateLeaderboard() {

        try (Connection connection = Database.MyDataBase.establishConnection()) {
            String sql = "SELECT `l_id`, `stu_name`, `points` FROM `leaderboard` ORDER BY `points` DESC, `pointLastUpdated` ASC";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            // Clear table
            while (tableModel.getRowCount() > 0) {
                tableModel.removeRow(0);
            }

            int rank = 1;
            while (resultSet.next()) {
                String usernameDb = resultSet.getString("stu_name");
                int points = resultSet.getInt("points");

                tableModel.addRow(new Object[]{rank, usernameDb, points});
                rank++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     // Handle the action when the user clicks the "Back" button
    private void backToMain() {
        Login.Main main = new Login.Main(currentStudent, "Student");
        main.setVisible(true); 
        dispose();
    }
    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            Leaderboard leaderboard = new Leaderboard("currentStudentName");
            leaderboard.setVisible(true);
        });
    }
}
