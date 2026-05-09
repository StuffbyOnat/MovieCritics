/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author onatu
 */
public class movieScreenParent extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(movieScreenParent.class.getName());
    Utilities utilities;
    Connection conn;
    int movieID;
    Movie movie;
    moviePane pane;
    JFrame previousFrame;
    Parent user;
    /**
     * Creates new form movieScreen
     */
    public movieScreenParent(Connection conn,String title,String poster,int movieID,JFrame previousFrame,Parent user,moviePane pane) {
        this.user=user;
        this.previousFrame=previousFrame;
        this.movieID=movieID;
        this.conn=conn;
        utilities=new Utilities();
        initComponents();
        initializeComboBox();
        try {
            icon.setIcon(utilities.setIconSize(149, 179, poster));
        } catch (Exception e) {
            e.printStackTrace();
                JOptionPane.showMessageDialog(null,"Error loading poster: " + e.getMessage(), "Image Load Error", JOptionPane.ERROR_MESSAGE);
        }
        try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM Movies WHERE movieID = ? ")) {
            ps.setInt(1, movieID);
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    movie = new Movie(conn,movieID, title, rs.getInt("releaseYear"),
                            rs.getString("language"),
                            rs.getString("countryOfOrigin"),
                            rs.getString("genre"),
                            rs.getString("directorld"),
                            rs.getBoolean("isWatched"),
                            rs.getString("leadingActorld"),
                            rs.getString("supportingActorld"),
                            rs.getString("about"),
                            rs.getInt("rating"),
                            rs.getString("comments"),
                            poster,
                            rs.getBoolean("parentalRestriction"));
                    //Standard initialization
                    parentalRestriction.setSelected(rs.getBoolean("parentalRestriction"));
                    about.setText(movie.getAbout());
                    about.setLineWrap(true);
                    about.setWrapStyleWord(true);
                    rating.setText("Rating: " + movie.getRating() + "/10");
                    titleField.setText(movie.getTitle());
                    deleteComment.setEnabled(false);
                    directorField.setText(movie.getDirectorLD());
                    yearField.setText(String.valueOf(movie.getReleaseYear()));
                    genreField.setText(movie.getGenre());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Error fetching movie details: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        about.setText(movie.getAbout());
        about.setLineWrap(true);
        about.setWrapStyleWord(true);
        parentalRestriction.setSelected(movie.isParentalRestriction());
    }
    void loadMovieDetailsByUser() {

        if(!user_selector.getSelectedItem().equals("-User-")) {
            String sql = "SELECT Movies.about,User_Critics.comment,User_Critics.rating FROM User_Critics JOIN Movies ON Movies.movieID=User_Critics.movieID WHERE Movies.movieID = ? AND User_Critics.userID = (SELECT userID FROM Users WHERE username = ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, movieID);
                ps.setString(2, (String) user_selector.getSelectedItem());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        about.setText(rs.getString("about"));
                        about.setLineWrap(true);
                        about.setWrapStyleWord(true);
                        comments.setText(rs.getString("comment"));
                        comments.setLineWrap(true);
                        comments.setWrapStyleWord(true);
                        rating.setText("Rating: " + rs.getInt("rating")+"/10");
                        if(comments.getText().isEmpty()){
                            comments.setText("No comment has been made.");
                            deleteComment.setEnabled(false);
                        }
                        else {
                            deleteComment.setEnabled(true);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if(comments.getText().isEmpty()){
                comments.setText("No comment has been made.");
                deleteComment.setEnabled(false);
            }
        }
        else{
            about.setText(movie.getAbout());
            about.setLineWrap(true);
            about.setWrapStyleWord(true);
            comments.setText("");
            deleteComment.setEnabled(false);
            rating.setText("Rating: " + movie.getRating() + "/10");
        }
    }


    void initializeComboBox(){
        //String sql = "SELECT * FROM movies WHERE movieID = ? join User_Critics ON movies.movieID = User_Critics.movieID join users ON User_Critics.userID = users.userID";
        String sql = "SELECT username from Users u join User_Critics uc on u.userID=uc.userID where uc.movieID=?";
        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,movieID);
            try(ResultSet rs=ps.executeQuery()){
                while(rs.next()) {
                    user_selector.addItem(rs.getString("username"));
                }
            }

        }
        catch (SQLException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Error fetching movie details: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        parentalRestriction = new javax.swing.JCheckBox();
        posterPanel = new javax.swing.JPanel();
        icon = new javax.swing.JLabel();
        rating = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        about = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        comments = new javax.swing.JTextArea();
        commentsLabel = new javax.swing.JLabel();
        user_selector = new javax.swing.JComboBox<>();
        deleteComment = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();
        titleField = new javax.swing.JTextField();
        directorLabel = new javax.swing.JLabel();
        directorField = new javax.swing.JTextField();
        yearLabel = new javax.swing.JLabel();
        yearField = new javax.swing.JTextField();
        genreLabel = new javax.swing.JLabel();
        genreField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(800, 600));

        parentalRestriction.setText("Restricted");

        javax.swing.GroupLayout posterPanelLayout = new javax.swing.GroupLayout(posterPanel);
        posterPanel.setLayout(posterPanelLayout);
        posterPanelLayout.setHorizontalGroup(
            posterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, posterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(icon, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                .addContainerGap())
        );
        posterPanelLayout.setVerticalGroup(
            posterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(icon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
        );

        rating.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rating.setText("Rating:");

        about.setColumns(20);
        about.setRows(5);
        jScrollPane1.setViewportView(about);

        comments.setEditable(false);
        comments.setColumns(20);
        comments.setRows(5);
        jScrollPane2.setViewportView(comments);

        commentsLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        commentsLabel.setText("Comments");

        user_selector.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-User-" }));
        user_selector.addActionListener(this::user_selectorActionPerformed);

        deleteComment.setText("Delete Comment");
        deleteComment.addActionListener(this::deleteCommentActionPerformed);

        saveButton.setText("Save");
        saveButton.addActionListener(this::saveButtonActionPerformed);

        backButton.setText("Back");
        backButton.addActionListener(this::backButtonActionPerformed);

        titleField.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        titleField.setText("Title: ");

        directorLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        directorLabel.setText("Director:");

        directorField.setText("director");

        yearLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        yearLabel.setText("Year: ");

        yearField.setText("year");

        genreLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        genreLabel.setText("Genre: ");

        genreField.setText("jTextField1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(posterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(titleField, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(user_selector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(parentalRestriction))
                            .addComponent(genreField, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(genreLabel)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(8, 8, 8)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(rating, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(directorLabel)))
                                .addComponent(directorField))
                            .addComponent(yearLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(83, 83, 83)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deleteComment))
                            .addComponent(commentsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(backButton)
                    .addComponent(saveButton)
                    .addComponent(yearField, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(posterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(parentalRestriction)
                                .addComponent(user_selector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(titleField, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(genreLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(genreField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(commentsLabel)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(rating)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(directorLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(directorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(yearLabel)
                                    .addComponent(yearField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(deleteComment)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(saveButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(backButton)
                .addGap(49, 49, 49))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void user_selectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_user_selectorActionPerformed
        loadMovieDetailsByUser();
    }//GEN-LAST:event_user_selectorActionPerformed

    private void deleteCommentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteCommentActionPerformed
        if(!user_selector.getSelectedItem().equals("-User-")) {
            String username = (String) user_selector.getSelectedItem();
            String sql = "UPDATE User_Critics SET comment = '', rating = NULL WHERE movieID = ? AND userID = (SELECT userID FROM Users WHERE username = ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setInt(1,movieID);
                ps.setString(2,username);
                int areYouSure = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this comment?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if(areYouSure != JOptionPane.YES_OPTION) {
                    return; // User chose not to delete
                }
                else {
                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Comment deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        comments.setText("");
                        deleteComment.setEnabled(false);
                    }
                }
            }
            catch (SQLException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,"Error deleting comment: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_deleteCommentActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        //applying parentalRestriction
        boolean isRestricted = parentalRestriction.isSelected();
        int isSuccessful=user.setRestriction(movieID,isRestricted);

        String username = (String) user_selector.getSelectedItem();




            try (PreparedStatement ps = conn.prepareStatement("UPDATE Movies SET about = ? WHERE movieID = ?")) {
                ps.setString(1, about.getText());
                ps.setInt(2, movieID);
                isSuccessful = ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,"Error saving changes: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }

            try (PreparedStatement ps = conn.prepareStatement("UPDATE Movies set title=? where movieID=?")) {
                ps.setString(1, titleField.getText());
                ps.setInt(2, movieID);
                isSuccessful = ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,"Error saving changes: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }

            try(PreparedStatement ps = conn.prepareStatement("UPDATE Movies set directorld=? where movieID=?")) {
                ps.setString(1, directorField.getText());
                ps.setInt(2, movieID);
                isSuccessful = ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,"Error saving changes: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }

            try(PreparedStatement ps = conn.prepareStatement("UPDATE Movies set releaseYear=? where movieID=?")) {
                ps.setInt(1, Integer.parseInt(yearField.getText()));
                ps.setInt(2, movieID);
                isSuccessful = ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,"Error saving changes: " + e.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }

            try(PreparedStatement ps = conn.prepareStatement("UPDATE Movies set genre=? where movieID=?")) {
                ps.setString(1, genreField.getText());
                ps.setInt(2, movieID);
                isSuccessful = ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,"Error saving changes: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }


        
        user.getMovieByID(movieID).setTitle(titleField.getText());
        user.getPaneByID(movieID).setMovieTitle(titleField.getText());
        user.getMovieByID(movieID).setDirectorLD(directorField.getText());
        user.getMovieByID(movieID).setReleaseYear(Integer.parseInt(yearField.getText()));
        user.getMovieByID(movieID).setGenre(genreField.getText());
        if (isSuccessful > 0) {
            JOptionPane.showMessageDialog(this, "Changes saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No changes were made.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
            previousFrame.setVisible(true);
            this.dispose();
    }//GEN-LAST:event_backButtonActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea about;
    private javax.swing.JButton backButton;
    private javax.swing.JTextArea comments;
    private javax.swing.JLabel commentsLabel;
    private javax.swing.JButton deleteComment;
    private javax.swing.JTextField directorField;
    private javax.swing.JLabel directorLabel;
    private javax.swing.JTextField genreField;
    private javax.swing.JLabel genreLabel;
    private javax.swing.JLabel icon;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JCheckBox parentalRestriction;
    private javax.swing.JPanel posterPanel;
    private javax.swing.JLabel rating;
    private javax.swing.JButton saveButton;
    private javax.swing.JTextField titleField;
    private javax.swing.JComboBox<String> user_selector;
    private javax.swing.JTextField yearField;
    private javax.swing.JLabel yearLabel;
    // End of variables declaration//GEN-END:variables
}
