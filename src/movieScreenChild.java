import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class movieScreenChild extends javax.swing.JFrame {
    Connection conn;
    int movieID;
    Movie movie;
    Child currentUser;
    JFrame previousFrame;
    Utilities utilities = new Utilities();
    private String aboutText = "";

    public movieScreenChild(Connection conn, int movieID, JFrame previousFrame, Child currentUser) {
        this.conn = conn;
        this.movieID = movieID;
        this.previousFrame = previousFrame;
        this.currentUser = currentUser;
        initComponents();
        this.setSize(450, 750);
        this.setLocationRelativeTo(previousFrame);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        loadMovieDetails();
    }

    private void loadMovieDetails() {
        try {
            String movieSql = "SELECT * FROM Movies WHERE movieID = ?";
            try (PreparedStatement ps = conn.prepareStatement(movieSql)) {
                ps.setInt(1, movieID);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    titleLabel.setText(rs.getString("title"));
                    directorLabel.setText("Director: " + rs.getString("directorLD"));
                    yearLabel.setText("Year: " + rs.getInt("releaseYear"));
                    genreLabel.setText("Genre: " + rs.getString("genre"));
                    ratingLabel.setText("Avg Rating: " + rs.getInt("rating") + "/10");
                    aboutArea.setText(rs.getString("about"));
                    String posterPath = rs.getString("poster");
                    posterLabel.setIcon(utilities.setIconSize(150, 200, posterPath));
                }
            }

            String criticSql = "SELECT rating, isWatched FROM User_Critics WHERE userID = ? AND movieID = ?";
            try (PreparedStatement ps = conn.prepareStatement(criticSql)) {
                ps.setInt(1, currentUser.getUserID());
                ps.setInt(2, movieID);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int userRating = rs.getInt("rating");
                    yourRatingLabel.setText("Your Rating: " + (userRating > 0 ? userRating + "/10" : "None"));
                    isWatchedCheckBox.setSelected(rs.getBoolean("isWatched"));
                } else {
                    yourRatingLabel.setText("Your Rating: None");
                    isWatchedCheckBox.setSelected(false);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {
        setTitle("Movie Details");
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        posterLabel = new JLabel();
        posterLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titleLabel = new JLabel();
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        directorLabel = new JLabel();
        yearLabel = new JLabel();
        genreLabel = new JLabel();
        ratingLabel = new JLabel();
        yourRatingLabel = new JLabel();
        
        Font infoFont = new Font("Segoe UI", Font.PLAIN, 14);
        directorLabel.setFont(infoFont);
        yearLabel.setFont(infoFont);
        genreLabel.setFont(infoFont);
        ratingLabel.setFont(infoFont);
        yourRatingLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        infoPanel.add(directorLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(yearLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(genreLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(ratingLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(yourRatingLabel);

        JLabel aboutTitle = new JLabel("Summary:");
        aboutTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        aboutTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        aboutArea = new JTextArea(6, 25);
        aboutArea.setEditable(false);
        aboutArea.setLineWrap(true);
        aboutArea.setWrapStyleWord(true);
        aboutArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane aboutScrollPane = new JScrollPane(aboutArea);
        aboutScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        isWatchedCheckBox = new JCheckBox("Mark as Watched");
        isWatchedCheckBox.setFont(new Font("Segoe UI", Font.BOLD, 13));
        isWatchedCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addCommentBtn = new JButton("Add/Edit Review");
        addToWatchlistBtn = new JButton("Add to Watchlist");
        backBtn = new JButton("Back");

        addCommentBtn.addActionListener(e -> {
            String ratingStr = JOptionPane.showInputDialog(this, "Rate this movie (1-10):");
            if (ratingStr == null || ratingStr.trim().isEmpty()) return;

            int rating;
            try {
                rating = Integer.parseInt(ratingStr);
                if (rating < 1 || rating > 10) {
                    JOptionPane.showMessageDialog(this, "Rating must be between 1 and 10.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number format!");
                return;
            }

            String comment = JOptionPane.showInputDialog(this, "Write your comment:");
            if (comment == null) comment = "";

            boolean watched = isWatchedCheckBox.isSelected();

            try {
                String checkSql = "SELECT * FROM User_Critics WHERE userID = ? AND movieID = ?";
                boolean exists = false;
                try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                    checkStmt.setInt(1, currentUser.getUserID());
                    checkStmt.setInt(2, movieID);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next()) exists = true;
                }

                if (exists) {
                    String updateSql = "UPDATE User_Critics SET rating = ?, comment = ?, isWatched = ? WHERE userID = ? AND movieID = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, rating);
                        updateStmt.setString(2, comment);
                        updateStmt.setBoolean(3, watched);
                        updateStmt.setInt(4, currentUser.getUserID());
                        updateStmt.setInt(5, movieID);
                        updateStmt.executeUpdate();
                    }
                } else {
                    String insertSql = "INSERT INTO User_Critics (userID, movieID, rating, comment, isWatched) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setInt(1, currentUser.getUserID());
                        insertStmt.setInt(2, movieID);
                        insertStmt.setInt(3, rating);
                        insertStmt.setString(4, comment);
                        insertStmt.setBoolean(5, watched);
                        insertStmt.executeUpdate();
                    }
                }
                JOptionPane.showMessageDialog(this, "Review saved!");
                loadMovieDetails();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database Error!");
            }
        });

        addToWatchlistBtn.addActionListener(e -> {
            Watchlist wl = new Watchlist(conn, currentUser.getUserID());
            if (wl.addMovie(movieID)) {
                JOptionPane.showMessageDialog(this, "Added to watchlist!");
            }
        });

        backBtn.addActionListener(e -> {
            previousFrame.setVisible(true);
            this.dispose();
        });

        buttonPanel.add(addCommentBtn);
        buttonPanel.add(addToWatchlistBtn);
        buttonPanel.add(backBtn);

        mainPanel.add(posterLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(infoPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JPanel aboutContainer = new JPanel();
        aboutContainer.setLayout(new BoxLayout(aboutContainer, BoxLayout.Y_AXIS));
        aboutContainer.add(aboutTitle);
        aboutContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        aboutContainer.add(aboutScrollPane);
        
        mainPanel.add(aboutContainer);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(isWatchedCheckBox);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(buttonPanel);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JLabel titleLabel, directorLabel, yearLabel, genreLabel, ratingLabel, yourRatingLabel, posterLabel;
    private JTextArea aboutArea;
    private JCheckBox isWatchedCheckBox;
    private JButton addCommentBtn, addToWatchlistBtn, backBtn;
}