/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author onatu
 */
public abstract class User {

    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getUserType() {
        return userType;
    }

    public String getEmail() {
        return email;
    }

    public boolean isParent() {
        return isParent;
    }

    private int userID;
    private String username;
    private String password;
    private int userType;//1 for father,2 for mother,3 for son,4 for daughter
    private String email;
    private boolean isParent;
    private JFrame MainFrame;

    public ArrayList<Movie> movies;
    public ArrayList<moviePane> panes;
    Connection conn;

    public User(String username, String password,Connection conn,JFrame MainFrame) {
        this.MainFrame=MainFrame;
        this.conn=conn;
    try(PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
        stmt.setString(1, username);
        stmt.setString(2, password);

        try(ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                this.userID = rs.getInt("userID");
                this.username = rs.getString("username");
                this.password = rs.getString("password");
                this.userType = rs.getInt("userType");
                this.email = rs.getString("email");
                this.isParent = rs.getBoolean("isParent");
            } else {
                JOptionPane.showMessageDialog(null, "One or both of your inputs are wrong.", "Wrong Input", JOptionPane.WARNING_MESSAGE);
            }


        }
    }
    catch (SQLException e){
    e.printStackTrace();
    }
        //Still in constructor
        movies=new ArrayList<>();
    panes=new ArrayList<>();
        fetchMoviesFromDatabase();
    }

    void fetchMoviesFromDatabase(){

        try(PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Movies")) {

            try(ResultSet rs = stmt.executeQuery()) {

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
                    moviePane pane = new moviePane(conn,movie.getTitle(),movie.getPoster(),movie.getMovieID(),MainFrame);

                    if(isParent) {
                        movies.add(movie);
                        panes.add(pane);
                    }
                    else {
                        if(!movie.isParentalRestriction())
                            movies.add(movie);
                            panes.add(pane);
                    }
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    void listInMovies(JPanel mainPanel){

        mainPanel.removeAll();

        for(int i = 0;i<movies.size();i++){

            JPanel moviePane = panes.get(i);

            mainPanel.add(moviePane);

        }
        mainPanel.revalidate();
        mainPanel.repaint();

    }

    public void searchMovies(String searchTerm,JFrame frame) {
            searchScreen searchScreen = new searchScreen(searchTerm, conn, this,frame);
            searchScreen.setVisible(true);
    }
}
