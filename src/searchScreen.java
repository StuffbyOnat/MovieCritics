/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author onatu
 */
public class searchScreen extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(searchScreen.class.getName());
    Connection conn;
    String initialSearch;
    User user;
    JFrame previousFrame;

    /**
     * Creates new form searchScreen
     */
    public searchScreen(String initialSearch, Connection conn, User user, JFrame previousFrame) {
        this.previousFrame=previousFrame;
        previousFrame.setVisible(false);
        this.setSize(800, 600);
         setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                previousFrame.setVisible(true);
            }
        });
        this.user=user;
        this.initialSearch=initialSearch;
        this.conn=conn;
        initComponents();
        initializeFilters();
        search();

        searchbar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                search();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                search();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                search();
            }

        });
    }

    public void initializeFilters() {
        searchbar.setText(initialSearch);
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT DISTINCT genre FROM Movies");
            while (rs.next()) {
                genre.addItem(rs.getString("genre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT DISTINCT directorld FROM Movies");
            while (rs.next()) {
                director.addItem(rs.getString("directorld"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT DISTINCT releaseYear FROM Movies");
            while (rs.next()) {
                Year.addItem(String.valueOf(rs.getInt("releaseYear")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }



   public void search() {
        performSearch();
        revalidate();
        repaint();
    }
   private void performSearch() {
        mainPanel.removeAll();

        String searchText = searchbar.getText();
        String selectedGenre = (String) genre.getSelectedItem();
        String selectedDirector = (String) director.getSelectedItem();
        String selectedYear = (String) Year.getSelectedItem();

        StringBuilder query = new StringBuilder("SELECT * FROM Movies WHERE 1=1");

        if (!searchText.isEmpty() && !searchText.equals("Search")) {
            query.append(" AND title LIKE ?");
        }
        if (selectedGenre != null && !selectedGenre.equals("Genre")) {
            query.append(" AND genre = ?");
        }
        if (selectedDirector != null && !selectedDirector.equals("Director") && !selectedDirector.trim().isEmpty()) {
            query.append(" AND directorld = ?");
        }
        if (selectedYear != null && !selectedYear.equals("Year")) {
            query.append(" AND releaseYear = ?");
        }

        try (PreparedStatement ps = conn.prepareStatement(query.toString())) {
            int paramIndex = 1;

            if (!searchText.isEmpty() && !searchText.equals("Search")) {
                ps.setString(paramIndex++, "%" + searchText + "%");
            }
            if (selectedGenre != null && !selectedGenre.equals("Genre")) {
                ps.setString(paramIndex++, selectedGenre);
            }
            if (selectedDirector != null && !selectedDirector.equals("Director") && !selectedDirector.trim().isEmpty()) {
                ps.setString(paramIndex++, selectedDirector);
            }
            if (selectedYear != null && !selectedYear.equals("Year")) {
                ps.setInt(paramIndex++, Integer.parseInt(selectedYear));
            }

            try (ResultSet rs = ps.executeQuery()) {
                ArrayList<Movie> searchResults = new ArrayList<>();
                ArrayList<moviePane> panes=new ArrayList<>();
                while (rs.next()) {
                    Movie movie = new Movie(conn,
                            rs.getInt("movieID"),
                            rs.getString("title"),
                            rs.getInt("releaseYear"),
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
                            rs.getString("poster"),
                            rs.getBoolean("parentalRestriction")
                    );
                    moviePane pane = new moviePane(conn,movie.getTitle(),movie.getPoster(),movie.getMovieID(),this);
                    panes.add(pane);
                    if(!user.isParent()&&!movie.isParentalRestriction()){
                        searchResults.add(movie);

                        mainPanel.add(pane);
                    }
                    else{
                        searchResults.add(movie);

                        mainPanel.add(pane);
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Arama sırasında bir hata oluştu.", "Hata", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        mainPanel.revalidate();
        mainPanel.repaint();
   }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        searchPane = new javax.swing.JPanel();
        genre = new javax.swing.JComboBox<>();
        director = new javax.swing.JComboBox<>();
        Year = new javax.swing.JComboBox<>();
        searchbar = new javax.swing.JTextField();
        scrollPane = new javax.swing.JScrollPane();
        mainPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(800, 600));

        genre.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Genre" }));
        genre.addActionListener(this::genreActionPerformed);

        director.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Director", " " }));
        director.addActionListener(this::directorActionPerformed);

        Year.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Year" }));
        Year.addActionListener(this::YearActionPerformed);

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

        javax.swing.GroupLayout searchPaneLayout = new javax.swing.GroupLayout(searchPane);
        searchPane.setLayout(searchPaneLayout);
        searchPaneLayout.setHorizontalGroup(
            searchPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, searchPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(searchbar, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
                .addGroup(searchPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(Year, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(director, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(genre, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(16, 16, 16))
        );
        searchPaneLayout.setVerticalGroup(
            searchPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(genre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(searchPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(director, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchbar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Year, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        getContentPane().add(searchPane, java.awt.BorderLayout.PAGE_START);

        mainPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        scrollPane.setViewportView(mainPanel);

        getContentPane().add(scrollPane, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void genreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_genreActionPerformed
search();
    }//GEN-LAST:event_genreActionPerformed

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

    private void searchbarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchbarActionPerformed
            search();
    }//GEN-LAST:event_searchbarActionPerformed

    private void directorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_directorActionPerformed
search();
    }//GEN-LAST:event_directorActionPerformed

    private void YearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_YearActionPerformed
search();
    }//GEN-LAST:event_YearActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> Year;
    private javax.swing.JComboBox<String> director;
    private javax.swing.JComboBox<String> genre;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JPanel searchPane;
    private javax.swing.JTextField searchbar;
    // End of variables declaration//GEN-END:variables
}
