import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class Type2MainFrame extends javax.swing.JFrame implements MainFrame {

    Utilities utilities;
    Connection conn;
    Child user;
    private JPopupMenu settingsPopup;
    private JPopupMenu menuPopup;

    public Type2MainFrame(Connection conn, String username, String password) {
        this.conn = conn;
        user = new Child(username, password, conn, this);
        utilities = new Utilities();
        initComponents();
        this.setSize(800, 600);
        this.username.setText(username);
        setFrameIcons();
        createPopups();

        if (user.movies != null && !user.movies.isEmpty()) {
            user.listInMovies(mainPanel);
        } else {
            System.out.println("No movies found for this user.");
            mainPanel.revalidate();
            mainPanel.repaint();
        }
    }

    void setFrameIcons(){
        settingsButton.setIcon(utilities.setIconSize(20, 20, "/Icons/settings-icon.png"));
        menuButton.setIcon(utilities.setIconSize(20, 20, "/Icons/menu-icon.png"));
        searchbarButton.setIcon(utilities.setIconSize(20, 20, "/Icons/search-icon.png"));
    }

    private void createPopups() {
        menuPopup = new JPopupMenu();
        JMenuItem trackProgressItem = new JMenuItem("Track Progress");
        JMenuItem watchlistManagerItem = new JMenuItem("Manage Watchlist");

        trackProgressItem.addActionListener(e -> showProgressTracker());
        watchlistManagerItem.addActionListener(e -> showWatchlistManager());

        menuPopup.add(trackProgressItem);
        menuPopup.add(watchlistManagerItem);

        settingsPopup = new JPopupMenu();
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION)
                this.dispose();
                new loginFrame().setVisible(true);
        });
        settingsPopup.add(logoutItem);

        settingsButton.addActionListener(e -> settingsPopup.show(settingsButton, 0, settingsButton.getHeight()));
        menuButton.addActionListener(e -> menuPopup.show(menuButton, 0, menuButton.getHeight()));
    }

    private void showWatchlistManager() {
        StringBuilder sb = new StringBuilder("=== Your Watchlist ===\n\n");
        String sql = "SELECT m.movieID, m.title FROM Movies m JOIN watchlists w ON m.movieID = w.movieID WHERE w.userID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, user.getUserID());
            ResultSet rs = stmt.executeQuery();
            boolean hasItems = false;
            while(rs.next()) {
                hasItems = true;
                sb.append("[").append(rs.getInt("movieID")).append("] ").append(rs.getString("title")).append("\n");
            }

            if(!hasItems) {
                JOptionPane.showMessageDialog(this, "Your watchlist is empty.");
                return;
            }

            String idStr = JOptionPane.showInputDialog(this, sb.toString() + "\nEnter Movie ID to remove:");
            if (idStr != null && !idStr.isEmpty()) {
                int mid = Integer.parseInt(idStr);
                try (PreparedStatement del = conn.prepareStatement("DELETE FROM watchlists WHERE userID = ? AND movieID = ?")) {
                    del.setInt(1, user.getUserID());
                    del.setInt(2, mid);
                    if (del.executeUpdate() > 0) JOptionPane.showMessageDialog(this, "Removed!");
                }
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void showProgressTracker() {
        int myCount = user.getWatchedCount();
        JOptionPane.showMessageDialog(this, "You watched " + myCount + " movies!", "Progress", JOptionPane.INFORMATION_MESSAGE);
    }

    private void initComponents() {
        topPanel = new javax.swing.JPanel();
        welcomeText = new javax.swing.JLabel("Welcome ");
        welcomeText.setFont(new java.awt.Font("Segoe Script", 0, 24));
        username = new javax.swing.JLabel();
        settingsButton = new javax.swing.JButton();
        menuButton = new javax.swing.JButton();
        recentMoviesLabel = new javax.swing.JLabel("Recent Movies :");
        searchbar = new javax.swing.JTextField(10);
        searchbarButton = new javax.swing.JButton();
        middlePanel = new javax.swing.JScrollPane();
        mainPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        topPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        topPanel.add(welcomeText);
        topPanel.add(username);
        topPanel.add(new javax.swing.JLabel(" | "));
        topPanel.add(searchbar);
        topPanel.add(searchbarButton);
        topPanel.add(menuButton);
        topPanel.add(settingsButton);

        searchbarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchbarButtonActionPerformed(evt);
            }
        });

        mainPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        middlePanel.setViewportView(mainPanel);

        getContentPane().add(topPanel, java.awt.BorderLayout.NORTH);
        getContentPane().add(middlePanel, java.awt.BorderLayout.CENTER);

        pack();
    }

    private void searchbarButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String searchTerm = searchbar.getText();
        if(!searchTerm.equals("Search")) {
            user.searchMovies(searchTerm,this);
            this.setVisible(false);
        }
    }

    @Override
    public void refreshMovies() {
        user.fetchMoviesFromDatabase();
        mainPanel.removeAll();
        if (user.movies != null && !user.movies.isEmpty()) {
            user.listInMovies(mainPanel);
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton menuButton;
    private javax.swing.JScrollPane middlePanel;
    private javax.swing.JLabel recentMoviesLabel;
    private javax.swing.JTextField searchbar;
    private javax.swing.JButton searchbarButton;
    private javax.swing.JButton settingsButton;
    private javax.swing.JPanel topPanel;
    private javax.swing.JLabel username;
    private javax.swing.JLabel welcomeText;
}