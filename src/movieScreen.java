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
public class movieScreen extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(movieScreen.class.getName());
    Utilities utilities;
    Connection conn;
    int movieID;
    Movie movie;
    JFrame previousFrame;
    /**
     * Creates new form movieScreen
     */
    public movieScreen(Connection conn,String title,String poster,int movieID,JFrame previousFrame) {
        this.previousFrame=previousFrame;
        this.movieID=movieID;
        this.conn=conn;
        utilities=new Utilities();
        initComponents();
        initializeComboBox();
        icon.setIcon(utilities.setIconSize(149,179, poster));

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

        if(!user.getSelectedItem().equals("-User-")) {
            String sql = "SELECT Movies.about,User_Critics.comment,User_Critics.rating FROM User_Critics JOIN Movies ON Movies.movieID=User_Critics.movieID WHERE Movies.movieID = ? AND User_Critics.userID = (SELECT userID FROM Users WHERE username = ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, movieID);
                ps.setString(2, (String) user.getSelectedItem());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        about.setText(rs.getString("about"));
                        about.setLineWrap(true);
                        about.setWrapStyleWord(true);
                        comments.setText(rs.getString("comment"));
                        comments.setLineWrap(true);
                        comments.setWrapStyleWord(true);
                        rating.setText("Rating: " + rs.getInt("rating")+"/10");

                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            about.setText(movie.getAbout());
            about.setLineWrap(true);
            about.setWrapStyleWord(true);
            comments.setText("");
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
                    user.addItem(rs.getString("username"));
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
        jLabel2 = new javax.swing.JLabel();
        user = new javax.swing.JComboBox<>();
        deleteComment = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();

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

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Comments");

        user.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-User-" }));
        user.addActionListener(this::userActionPerformed);

        deleteComment.setText("Delete Comment");
        deleteComment.addActionListener(this::deleteCommentActionPerformed);

        saveButton.setText("Save");

        backButton.setText("Back");

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
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addComponent(user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(parentalRestriction))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(rating, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(83, 83, 83)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deleteComment))
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(backButton)
                    .addComponent(saveButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(posterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(parentalRestriction)
                            .addComponent(user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rating)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(deleteComment)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(saveButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(backButton)
                .addGap(49, 49, 49))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void userActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userActionPerformed
        loadMovieDetailsByUser();
    }//GEN-LAST:event_userActionPerformed

    private void deleteCommentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteCommentActionPerformed
        if(!user.getSelectedItem().equals("-User-")) {
            String username = (String) user.getSelectedItem();
            String sql = "DELETE FROM User_Critics WHERE movieID = ? AND userID = (SELECT userID FROM Users WHERE username = ?)";
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
                    }
                }
            }
            catch (SQLException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,"Error deleting comment: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_deleteCommentActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea about;
    private javax.swing.JButton backButton;
    private javax.swing.JTextArea comments;
    private javax.swing.JButton deleteComment;
    private javax.swing.JLabel icon;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JCheckBox parentalRestriction;
    private javax.swing.JPanel posterPanel;
    private javax.swing.JLabel rating;
    private javax.swing.JButton saveButton;
    private javax.swing.JComboBox<String> user;
    // End of variables declaration//GEN-END:variables
}
