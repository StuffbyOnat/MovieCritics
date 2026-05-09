/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.*;
/**
 *
 * @author onatu
 */
public class Type1MainFrame extends javax.swing.JFrame implements MainFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Type1MainFrame.class.getName());
    Utilities utilities;
    Connection conn;
    Parent user;
    private JPopupMenu menuPopup;
    private JPopupMenu settingsPopup;
    
    /**
     * Creates new form Type1MainFrame
     */
    public Type1MainFrame(Connection conn, String username,String password) {

        this.conn=conn;
        user = new Parent(username, password, conn,this);
        utilities=new Utilities();
        initComponents();
        this.setSize(800, 600);
        setFrameIcons();
        createPopups();
        user.listInMovies(mainPanel);
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
        JMenuItem familyItem = new JMenuItem("Family Analytics");

        addMovie.addActionListener(e -> {
           String title = JOptionPane.showInputDialog(this, "Enter movie title:");
           int releaseYear = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter release year:"));
              String language = JOptionPane.showInputDialog(this, "Enter language:");
                String country = JOptionPane.showInputDialog(this, "Enter country of origin:");
                String genre = JOptionPane.showInputDialog(this, "Enter genre:");
                String director = JOptionPane.showInputDialog(this, "Enter director:");
                String leadingActor = JOptionPane.showInputDialog(this, "Enter leading actor:");
                String supportingActor = JOptionPane.showInputDialog(this, "Enter supporting actor:");
                String about = JOptionPane.showInputDialog(this, "Enter about:");
                String posterPath = JOptionPane.showInputDialog(this, "Enter poster path:");
                int rating = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter rating (0-10):"));
                if(rating<0 || rating>10){
                    JOptionPane.showMessageDialog(this, "Rating must be between 0 and 10. Setting to 10 by default.");
                    rating=10;
                }
                boolean isRestricted = JOptionPane.showConfirmDialog(this, "Is this movie restricted?", "Parental Restriction", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

                try(PreparedStatement stmt = conn.prepareStatement("INSERT INTO movies (title, releaseYear, language, countryOfOrigin, genre, directorld, leadingActorld, supportingActorld, about,rating, poster, parentalRestriction) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
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
                    if(affectedRows>0){
                        try(ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                int newMovieID = generatedKeys.getInt(1);
                                Movie newMovie = new Movie(conn, newMovieID, title, releaseYear, language, country, genre, director, false, leadingActor, supportingActor, about, rating, "", posterPath, isRestricted);
                                user.movies.add(newMovie);
                                moviePane newPane = new moviePane(conn,title,posterPath,newMovieID,this,(Parent)user);
                                user.panes.add(newPane);
                                mainPanel.add(newPane);
                                mainPanel.revalidate();
                                mainPanel.repaint();
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to add movie.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error adding movie: " + ex.getMessage());
                }
        });

        removeMovie.addActionListener(ev -> {
            int movieID = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter movie ID to remove:"));
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM movies WHERE movieID = ?")) {
                stmt.setInt(1, movieID);
                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {

                    refreshMovies();
                    JOptionPane.showMessageDialog(this, "Movie removed successfully.");
                }
                 else{
                    JOptionPane.showMessageDialog(this, "No movie found with the given ID.");
                }
            }
            catch (SQLException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error removing movie: " + e.getMessage());
            }
    });

        menuPopup.add(addMovie);
        menuPopup.add(removeMovie);
        menuPopup.addSeparator();
        menuPopup.add(familyItem);


        settingsPopup = new JPopupMenu();

        JMenuItem logoutItem = new JMenuItem("Logout");
        JMenuItem themeItem = new JMenuItem("Dark Theme");
        JMenuItem prefsItem = new JMenuItem("Preferences");

        themeItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Theme settings coming soon..."));

        settingsPopup.add(themeItem);
        settingsPopup.add(prefsItem);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        topPanel = new javax.swing.JPanel();
        welcomeText = new javax.swing.JLabel();
        username = new javax.swing.JLabel();
        settingsButton = new javax.swing.JButton();
        menuButton = new javax.swing.JButton();
        recentMoviesLabel = new javax.swing.JLabel();
        searchbar = new javax.swing.JTextField();
        searchbarButton = new javax.swing.JButton();
        middlePanel = new javax.swing.JScrollPane();
        mainPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        welcomeText.setFont(new java.awt.Font("Segoe Script", 0, 24)); // NOI18N
        welcomeText.setText("Welcome");

        username.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        username.setText("Username");

        menuButton.addActionListener(this::menuButtonActionPerformed);

        recentMoviesLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
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
                .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                                .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
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
    }// </editor-fold>//GEN-END:initComponents

    private void searchbarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchbarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchbarActionPerformed

    private void searchbarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchbarButtonActionPerformed
        String searchTerm = searchbar.getText();
                if(!searchTerm.equals("Search"))
            user.searchMovies(searchTerm,this);

    }//GEN-LAST:event_searchbarButtonActionPerformed

    private void searchbarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchbarFocusGained
if(searchbar.getText().equals("Search")){
searchbar.setText("");
}
    }//GEN-LAST:event_searchbarFocusGained

    private void searchbarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchbarFocusLost
        if(searchbar.getText().isEmpty()){
            searchbar.setText("Search");
        }
    }//GEN-LAST:event_searchbarFocusLost

    private void menuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuButtonActionPerformed
            menuPopup.show(menuButton, 0, menuButton.getHeight());
    }//GEN-LAST:event_menuButtonActionPerformed

    @Override
    public void refreshMovies() {
        user.movies.clear();
        user.panes.clear();
        user.fetchMoviesFromDatabase();
        user.listInMovies(mainPanel);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    // End of variables declaration//GEN-END:variables
}
