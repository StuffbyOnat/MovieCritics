import javax.swing.*;
import java.sql.*;

public class movieScreenChild extends javax.swing.JFrame {
    Connection conn;
    int movieID;
    Movie movie;
    Child currentUser;
    JFrame previousFrame;
    Utilities utilities = new Utilities();

    public movieScreenChild(Connection conn, int movieID, JFrame previousFrame, Child currentUser) {
        this.conn = conn;
        this.movieID = movieID;
        this.previousFrame = previousFrame;
        this.currentUser = currentUser;
        initComponents();
        this.setSize(400,600);
        this.setLocation(previousFrame.getLocation());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        loadMovieDetails();
    }

    private void loadMovieDetails() {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM Movies WHERE movieID = ?")) {
            ps.setInt(1, movieID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Bilgileri Label'lara aktar (Düzenleme kapalı)
                titleLabel.setText("Title: " + rs.getString("title"));
                directorLabel.setText("Director: " + rs.getString("directorLD"));
                yearLabel.setText("Year: " + rs.getInt("releaseYear"));
                genreLabel.setText("Genre: " + rs.getString("genre"));
                ratingLabel.setText("Average Rating: " + rs.getInt("rating") + "/10");

                aboutArea.setText(rs.getString("about"));
                aboutArea.setEditable(false); // Çocuklar özeti değiştiremez

                String posterPath = rs.getString("poster");
                posterLabel.setIcon(utilities.setIconSize(150, 200, posterPath));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {
        titleLabel = new JLabel();
        directorLabel = new JLabel();
        yearLabel = new JLabel();
        genreLabel = new JLabel();
        ratingLabel = new JLabel();
        posterLabel = new JLabel();
        aboutArea = new JTextArea(5, 20);
        addToWatchlistBtn = new JButton("Add to Watchlist");
        backBtn = new JButton("Back");

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Movie Details");
        setLayout(new java.awt.FlowLayout());

        addToWatchlistBtn.addActionListener(e -> {
            Watchlist wl = new Watchlist(conn, currentUser.getUserID());
            if (wl.addMovie(movieID)) {
                JOptionPane.showMessageDialog(this, "Film listenize eklendi!");
            }
        });

        backBtn.addActionListener(e -> {
            previousFrame.setVisible(true);
            this.dispose();
        });

        add(posterLabel);
        add(titleLabel);
        add(directorLabel);
        add(yearLabel);
        add(aboutArea);
        add(addToWatchlistBtn);
        add(backBtn);

        pack();
        setLocationRelativeTo(null);
    }

    private JLabel titleLabel, directorLabel, yearLabel, genreLabel, ratingLabel, posterLabel;
    private JTextArea aboutArea;
    private JButton addToWatchlistBtn, backBtn;
}