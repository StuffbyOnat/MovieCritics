import javax.swing.*;
import java.sql.*;

public class Type1MainFrame extends javax.swing.JFrame implements MainFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Type1MainFrame.class.getName());
    Utilities utilities;
    Connection conn;
    Parent user;
    private JPopupMenu menuPopup;
    private JPopupMenu settingsPopup;

    public Type1MainFrame(Connection conn, String username, String password) {
        this.conn = conn;
        user = new Parent(username, password, conn, this);
        utilities = new Utilities();
        initComponents();
        this.setSize(800, 600);
        setFrameIcons();
        createPopups();
        user.listInMovies(mainPanel);
        Username.setText(user.getUsername());
    }

    void setFrameIcons(){
        settingsButton.setIcon(utilities.setIconSize(20, 20, "/Icons/settings-icon.png"));
        menuButton.setIcon(utilities.setIconSize(20, 20, "/Icons/menu-icon.png"));
        searchbarButton.setIcon(utilities.setIconSize(20, 20, "/Icons/search-icon.png"));
    }

    private void createPopups() {
        menuPopup = new JPopupMenu();

        JMenuItem addMovie = new JMenuItem("Add Movie");
        JMenuItem removeMovie = new JMenuItem("Remove Movie");
        JMenuItem addUser = new JMenuItem("Add User");
        JMenuItem updateUser = new JMenuItem("Update User");
        JMenuItem removeUser = new JMenuItem("Remove User");
        JMenuItem familyItem = new JMenuItem("Family Analytics");

        addMovie.addActionListener(e -> {
            try {
                String title = JOptionPane.showInputDialog(this, "Enter movie title:");
                if (title == null || title.trim().isEmpty()) return;

                String yearStr = JOptionPane.showInputDialog(this, "Enter release year:");
                if (yearStr == null) return;
                int releaseYear = Integer.parseInt(yearStr);

                String language = JOptionPane.showInputDialog(this, "Enter language:");
                String country = JOptionPane.showInputDialog(this, "Enter country of origin:");
                String genre = JOptionPane.showInputDialog(this, "Enter genre:");
                String director = JOptionPane.showInputDialog(this, "Enter director:");
                String leadingActor = JOptionPane.showInputDialog(this, "Enter leading actor:");
                String supportingActor = JOptionPane.showInputDialog(this, "Enter supporting actor:");
                String about = JOptionPane.showInputDialog(this, "Enter about:");
                String posterPath = JOptionPane.showInputDialog(this, "Enter poster path:");

                String ratingStr = JOptionPane.showInputDialog(this, "Enter rating (0-10):");
                if (ratingStr == null) return;
                int rating = Integer.parseInt(ratingStr);

                if(rating < 0 || rating > 10){
                    JOptionPane.showMessageDialog(this, "Rating must be between 0 and 10. Setting to 10 by default.");
                    rating = 10;
                }
                boolean isRestricted = JOptionPane.showConfirmDialog(this, "Is this movie restricted?", "Parental Restriction", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

                String sql = "INSERT INTO Movies (title, releaseYear, language, countryOfOrigin, genre, directorld, leadingActorld, supportingActorld, about, rating, poster, parentalRestriction) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try(PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, title);
                    stmt.setInt(2, releaseYear);
                    stmt.setString(3, language);
                    stmt.setString(4, country);
                    stmt.setString(5, genre);
                    stmt.setString(6, director);
                    stmt.setString(7, leadingActor);
                    stmt.setString(8, supportingActor);
                    stmt.setString(9, about);
                    stmt.setInt(10, rating);
                    stmt.setString(11, posterPath);
                    stmt.setBoolean(12, isRestricted);

                    int affectedRows = stmt.executeUpdate();
                    if(affectedRows > 0){
                        try(ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                int newMovieID = generatedKeys.getInt(1);
                                Movie newMovie = new Movie(conn, newMovieID, title, releaseYear, language, country, genre, director, false, leadingActor, supportingActor, about, rating, "", posterPath, isRestricted);
                                user.movies.add(newMovie);
                                moviePane newPane = new moviePane(conn, title, posterPath, newMovieID, this, (Parent)user);
                                user.panes.add(newPane);
                                mainPanel.add(newPane);
                                mainPanel.revalidate();
                                mainPanel.repaint();
                            }
                        }
                        JOptionPane.showMessageDialog(this, "Movie added successfully!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to add movie.");
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric values for Year and Rating!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding movie: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        removeMovie.addActionListener(ev -> {
            try {
                String idStr = JOptionPane.showInputDialog(this, "Enter movie ID to remove:");
                if (idStr == null || idStr.trim().isEmpty()) return;
                int movieID = Integer.parseInt(idStr);

                try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Movies WHERE movieID = ?")) {
                    stmt.setInt(1, movieID);
                    int affectedRows = stmt.executeUpdate();
                    if (affectedRows > 0) {
                        refreshMovies();
                        JOptionPane.showMessageDialog(this, "Movie removed successfully.");
                    } else {
                        JOptionPane.showMessageDialog(this, "No movie found with the given ID.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric ID!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error removing movie: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        addUser.addActionListener(e -> {
            try {
                String newUsername = JOptionPane.showInputDialog(this, "Enter username:");
                if (newUsername == null || newUsername.trim().isEmpty()) return;

                String newPassword = JOptionPane.showInputDialog(this, "Enter password:");
                if (newPassword == null || newPassword.trim().isEmpty()) return;

                String typeStr = JOptionPane.showInputDialog(this, "Enter User Type (1=Father, 2=Mother, 3=Son, 4=Daughter):");
                if (typeStr == null) return;
                int userTypeInt = Integer.parseInt(typeStr);
                UserType parsedType = UserType.fromInt(userTypeInt);

                String email = JOptionPane.showInputDialog(this, "Enter email address:");
                if (email == null || email.trim().isEmpty()) return;

                boolean isParent = JOptionPane.showConfirmDialog(this, "Is this user a parent?", "Parent Status", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

                String sql = "INSERT INTO Users (username, password, userType, email, isParent) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, newUsername);
                    stmt.setString(2, newPassword);
                    stmt.setInt(3, parsedType.getValue());
                    stmt.setString(4, email);
                    stmt.setBoolean(5, isParent);

                    int affectedRows = stmt.executeUpdate();
                    if (affectedRows > 0) {
                        JOptionPane.showMessageDialog(this, "User '" + newUsername + "' added successfully!");
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric User Type!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } catch (SQLIntegrityConstraintViolationException ex) {
                JOptionPane.showMessageDialog(this, "Username or Email already exists in the system!", "Duplicate Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding user: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateUser.addActionListener(e -> {
            try {
                String idStr = JOptionPane.showInputDialog(this, "Enter User ID to update:");
                if (idStr == null || idStr.trim().isEmpty()) return;
                int userIdToUpdate = Integer.parseInt(idStr);

                String fetchSql = "SELECT * FROM Users WHERE userID = ?";
                try (PreparedStatement fetchStmt = conn.prepareStatement(fetchSql)) {
                    fetchStmt.setInt(1, userIdToUpdate);
                    try (ResultSet rs = fetchStmt.executeQuery()) {
                        if (!rs.next()) {
                            JOptionPane.showMessageDialog(this, "User not found!", "Warning", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        String currentUsername = rs.getString("username");
                        String currentPassword = rs.getString("password");
                        int currentUserType = rs.getInt("userType");
                        String currentEmail = rs.getString("email");
                        boolean currentIsParent = rs.getBoolean("isParent");

                        String newUsername = (String) JOptionPane.showInputDialog(this, "Update username:", "Update User", JOptionPane.QUESTION_MESSAGE, null, null, currentUsername);
                        if (newUsername == null || newUsername.trim().isEmpty()) return;

                        String newPassword = (String) JOptionPane.showInputDialog(this, "Update password:", "Update User", JOptionPane.QUESTION_MESSAGE, null, null, currentPassword);
                        if (newPassword == null || newPassword.trim().isEmpty()) return;

                        String typeStr = (String) JOptionPane.showInputDialog(this, "Update User Type (1=Father, 2=Mother, 3=Son, 4=Daughter):", "Update User", JOptionPane.QUESTION_MESSAGE, null, null, String.valueOf(currentUserType));
                        if (typeStr == null) return;
                        int userTypeInt = Integer.parseInt(typeStr);
                        UserType parsedType = UserType.fromInt(userTypeInt);

                        String newEmail = (String) JOptionPane.showInputDialog(this, "Update email address:", "Update User", JOptionPane.QUESTION_MESSAGE, null, null, currentEmail);
                        if (newEmail == null || newEmail.trim().isEmpty()) return;

                        int parentOption = JOptionPane.showConfirmDialog(this, "Is this user a parent?", "Parent Status", JOptionPane.YES_NO_OPTION);
                        if (parentOption == JOptionPane.CLOSED_OPTION) return;
                        boolean isParent = (parentOption == JOptionPane.YES_OPTION);

                        String updateSql = "UPDATE Users SET username = ?, password = ?, userType = ?, email = ?, isParent = ? WHERE userID = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setString(1, newUsername);
                            updateStmt.setString(2, newPassword);
                            updateStmt.setInt(3, parsedType.getValue());
                            updateStmt.setString(4, newEmail);
                            updateStmt.setBoolean(5, isParent);
                            updateStmt.setInt(6, userIdToUpdate);

                            int affectedRows = updateStmt.executeUpdate();
                            if (affectedRows > 0) {
                                JOptionPane.showMessageDialog(this, "User '" + newUsername + "' updated successfully!");
                            }
                        }
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric values for ID and User Type!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } catch (SQLIntegrityConstraintViolationException ex) {
                JOptionPane.showMessageDialog(this, "Username or Email already exists in the system!", "Duplicate Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating user: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        removeUser.addActionListener(e -> {
            try {
                String idStr = JOptionPane.showInputDialog(this, "Enter User ID to remove:");
                if (idStr == null || idStr.trim().isEmpty()) return;
                int idToRemove = Integer.parseInt(idStr);

                if (idToRemove == user.getUserID()) {
                    JOptionPane.showMessageDialog(this, "You cannot remove your own account while logged in!", "Action Denied", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Users WHERE userID = ?")) {
                    stmt.setInt(1, idToRemove);
                    int affectedRows = stmt.executeUpdate();
                    if (affectedRows > 0) {
                        JOptionPane.showMessageDialog(this, "User removed successfully.");
                    } else {
                        JOptionPane.showMessageDialog(this, "No user found with the given ID.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric ID!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error removing user: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        familyItem.addActionListener(e -> showFamilyAnalytics());

        menuPopup.add(addMovie);
        menuPopup.add(removeMovie);
        menuPopup.addSeparator();
        menuPopup.add(addUser);
        menuPopup.add(updateUser);
        menuPopup.add(removeUser);
        menuPopup.addSeparator();
        menuPopup.add(familyItem);


        settingsPopup = new JPopupMenu();

        JMenuItem logoutItem = new JMenuItem("Logout");
        JMenuItem themeItem = new JMenuItem("Dark Theme");
        JMenuItem prefsItem = new JMenuItem("Preferences");

        themeItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Theme settings coming soon..."));

        logoutItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
            }
        });

        settingsPopup.add(themeItem);
        settingsPopup.add(prefsItem);
        settingsPopup.addSeparator();
        settingsPopup.add(logoutItem);

        settingsButton.addActionListener(this::settingsButtonActionPerformed);
    }

    private void showFamilyAnalytics() {
        StringBuilder stats = new StringBuilder("=== Family Analytics ===\n\n");

        String sql = "SELECT u.username, COUNT(uc.movieID) AS totalMovies, ROUND(AVG(uc.rating), 1) AS avgRating " +
                "FROM Users u JOIN User_Critics uc ON u.userID = uc.userID " +
                "GROUP BY u.username ORDER BY totalMovies DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String uname = rs.getString("username");
                int total = rs.getInt("totalMovies");
                double avg = rs.getDouble("avgRating");

                stats.append("User: ").append(uname).append("\n")
                        .append("Movies Watched: ").append(total).append("\n")
                        .append("Average Rating Given: ").append(avg).append("/10\n");

                String genreSql = "SELECT m.genre, COUNT(*) as gCount FROM User_Critics uc " +
                        "JOIN Movies m ON uc.movieID = m.movieID " +
                        "WHERE uc.userID = (SELECT userID FROM Users WHERE username = ?) " +
                        "GROUP BY m.genre ORDER BY gCount DESC LIMIT 1";

                try (PreparedStatement gStmt = conn.prepareStatement(genreSql)) {
                    gStmt.setString(1, uname);
                    try (ResultSet gRs = gStmt.executeQuery()) {
                        if (gRs.next()) {
                            stats.append("Most Watched Genre: ").append(gRs.getString("genre"))
                                    .append(" (").append(gRs.getInt("gCount")).append(" movies)\n");
                        } else {
                            stats.append("Most Watched Genre: None\n");
                        }
                    }
                }
                stats.append("-----------------------------\n");
            }

            if (stats.toString().equals("=== Family Analytics ===\n\n")) {
                stats.append("No analytics available yet. Users need to rate movies first.");
            }

            JTextArea textArea = new JTextArea(stats.toString());
            textArea.setEditable(false);
            textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 14));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(350, 400));

            JOptionPane.showMessageDialog(this, scrollPane, "Family Analytics", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching analytics: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        topPanel = new javax.swing.JPanel();
        welcomeText = new javax.swing.JLabel();
        Username = new javax.swing.JLabel();
        settingsButton = new javax.swing.JButton();
        menuButton = new javax.swing.JButton();
        recentMoviesLabel = new javax.swing.JLabel();
        searchbar = new javax.swing.JTextField();
        searchbarButton = new javax.swing.JButton();
        middlePanel = new javax.swing.JScrollPane();
        mainPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        welcomeText.setFont(new java.awt.Font("Segoe Script", 0, 24));
        welcomeText.setText("Welcome");

        Username.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18));
        Username.setText("Username");

        menuButton.addActionListener(this::menuButtonActionPerformed);

        recentMoviesLabel.setFont(new java.awt.Font("Segoe UI", 0, 18));
        recentMoviesLabel.setText("Recent Movies :");

        searchbar.setText("Search");
        searchbar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchbarFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                searchbarFocusLost(evt);
            }
        });
        searchbar.addActionListener(this::searchbarActionPerformed);

        searchbarButton.addActionListener(this::searchbarButtonActionPerformed);

        javax.swing.GroupLayout topPanelLayout = new javax.swing.GroupLayout(topPanel);
        topPanel.setLayout(topPanelLayout);
        topPanelLayout.setHorizontalGroup(
                topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(topPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(welcomeText, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Username, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 185, Short.MAX_VALUE)
                                .addComponent(searchbarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchbar, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(menuButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(settingsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12))
                        .addGroup(topPanelLayout.createSequentialGroup()
                                .addComponent(recentMoviesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        topPanelLayout.setVerticalGroup(
                topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(topPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(topPanelLayout.createSequentialGroup()
                                                .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                .addComponent(welcomeText, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(Username, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(settingsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(topPanelLayout.createSequentialGroup()
                                                                .addGap(48, 48, 48)
                                                                .addComponent(menuButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(recentMoviesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE))
                                        .addGroup(topPanelLayout.createSequentialGroup()
                                                .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(searchbar)
                                                        .addComponent(searchbarButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(0, 0, Short.MAX_VALUE))))
        );

        getContentPane().add(topPanel, java.awt.BorderLayout.PAGE_START);

        mainPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        middlePanel.setViewportView(mainPanel);

        getContentPane().add(middlePanel, java.awt.BorderLayout.CENTER);

        pack();
    }

    private void searchbarActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void searchbarButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String searchTerm = searchbar.getText();
        if(!searchTerm.equals("Search"))
            user.searchMovies(searchTerm, this);
    }

    private void searchbarFocusGained(java.awt.event.FocusEvent evt) {
        if(searchbar.getText().equals("Search")){
            searchbar.setText("");
        }
    }

    private void searchbarFocusLost(java.awt.event.FocusEvent evt) {
        if(searchbar.getText().isEmpty()){
            searchbar.setText("Search");
        }
    }

    private void menuButtonActionPerformed(java.awt.event.ActionEvent evt) {
        menuPopup.show(menuButton, 0, menuButton.getHeight());
    }

    private void settingsButtonActionPerformed(java.awt.event.ActionEvent evt) {
        settingsPopup.show(settingsButton, 0, settingsButton.getHeight());
    }

    @Override
    public void refreshMovies() {
        user.movies.clear();
        user.panes.clear();
        user.fetchMoviesFromDatabase();
        user.listInMovies(mainPanel);
    }

    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton menuButton;
    private javax.swing.JScrollPane middlePanel;
    private javax.swing.JLabel recentMoviesLabel;
    private javax.swing.JTextField searchbar;
    private javax.swing.JButton searchbarButton;
    private javax.swing.JButton settingsButton;
    private javax.swing.JPanel topPanel;
    private javax.swing.JLabel Username;
    private javax.swing.JLabel welcomeText;
}