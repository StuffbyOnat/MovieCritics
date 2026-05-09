import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Parent extends User{

    public Parent(String username, String password, Connection conn, JFrame MainFrame) {
        super(username, password, conn, MainFrame);



    }

    public void viewFamilyAnalytics(){

    }

    public void moderateContent(){
            //This methods purpose is done by deleteComment button in movieScreenParent class. It is here to show that parents have the right to moderate content.
    }

    public int setRestriction(int movieID,boolean isRestricted){
        try(PreparedStatement stmt = conn.prepareStatement("UPDATE movies SET parentalRestriction = ? WHERE movieID = ?")) {
            stmt.setBoolean(1, isRestricted);
            stmt.setInt(2, movieID);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void manageAccounts(){


    }

    public void editMovie(Movie movie){
        //This methods job is done by the interactive movieScreenParent class. It is here to show that parents have the right to edit movies.
        setMovieName(movie,movie.getTitle());
    }
    public void setMovieName(Movie movie,String name){
        getMovieByID(movie.getMovieID()).setTitle(movie.getTitle());

    }

    public void removeMovie(Movie movie){


    }

    public void addMovie(Movie movie){
     try(PreparedStatement stmt = conn.prepareStatement("INSERT INTO movies (title, releaseYear, language, countryOfOrigin, genre, directorld, leadingActorld, supportingActorld, about, rating) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, movie.getTitle());
            stmt.setInt(2, movie.getReleaseYear());
            stmt.setString(3, movie.getLanguage());
            stmt.setString(4, movie.getCountryOfOrigin());
            stmt.setString(5, movie.getGenre());
            stmt.setString(6, movie.getDirectorLD());
            stmt.setString(7, movie.getLeadingActorLD());
            stmt.setString(8, movie.getSupportingActorLD());
            stmt.setString(9, movie.getAbout());
            stmt.setInt(10, movie.getRating());
            stmt.executeUpdate();
        } catch (SQLException e) {
         e.printStackTrace();
     }

    }

    @Override
    public int getUserID() {
        return super.getUserID();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public int getUserType() {
        return super.getUserType();
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public boolean isParent() {
        return super.isParent();
    }
}


