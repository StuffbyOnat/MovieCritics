/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author onatu
 */
public class Movie {

    public moviePane pane;
    private int movieID;
    private String title;
    private int releaseYear;
    private String language;
    private String countryOfOrigin;
    private String genre;
    private String directorLD;
    private boolean isWatched;
    private String leadingActorLD;
    private String supportingActorLD;
    private String about;
    private int rating;
    private String comments;
    private String poster;
    private boolean parentalRestrictions;

    public Movie(int movieID, String title, int releaseYear, String language, String countryOfOrigin, String genre, String directorLD, boolean isWatched, String leadingActorLD, String supportingActorLD, String about, int rating, String comments, String poster, boolean parentalRestrictions) {
        this.pane = new moviePane(title,poster);
        this.movieID = movieID;
        this.title = title;
        this.releaseYear = releaseYear;
        this.language = language;
        this.countryOfOrigin = countryOfOrigin;
        this.genre = genre;
        this.directorLD = directorLD;
        this.isWatched = isWatched;
        this.leadingActorLD = leadingActorLD;
        this.supportingActorLD = supportingActorLD;
        this.about = about;
        this.rating = rating;
        this.comments = comments;
        this.poster = poster;
        this.parentalRestrictions = parentalRestrictions;
    }
    
}
