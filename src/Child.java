import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class Child extends User {

    public Child(String username, String password, Connection conn, JFrame MainFrame) {
        super(username, password, conn, MainFrame);
    }

    @Override
    public void fetchMoviesFromDatabase() {
        this.movies.clear();
        this.panes.clear();
        String sql = "SELECT * FROM Movies WHERE parentalRestriction = false";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Movie m = new Movie(conn, rs.getInt("movieID"), rs.getString("title"),
                        rs.getInt("releaseYear"), rs.getString("language"),
                        rs.getString("countryOfOrigin"), rs.getString("genre"),
                        rs.getString("directorld"), rs.getBoolean("isWatched"),
                        rs.getString("leadingActorld"), rs.getString("supportingActorld"),
                        rs.getString("about"), rs.getInt("rating"), "",
                        rs.getString("poster"), rs.getBoolean("parentalRestriction"));
                this.movies.add(m);
                moviePane pane = new moviePane(conn,m.getTitle(),m.getPoster(),m.getMovieID(),getMainFrame(),this);
                this.panes.add(pane);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int getWatchedCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM User_Critics WHERE userID = ? AND rating IS NOT NULL";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, this.getUserID());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) count = rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return count;
    }
}