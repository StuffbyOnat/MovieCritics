import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Watchlist {
    private Connection conn;
    private int userID;

    public Watchlist(Connection conn, int userID) {
        this.conn = conn;
        this.userID = userID;
    }

    public boolean addMovie(int movieID) {
        // Önce listede var mı kontrol et
        String checkSql = "SELECT * FROM watchlists WHERE userID = ? AND movieID = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, userID);
            checkStmt.setInt(2, movieID);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "Bu film zaten izleme listenizde!");
                return false;
            }

            // Yoksa ekle
            String insertSql = "INSERT INTO watchlists (userID, movieID) VALUES (?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, userID);
                insertStmt.setInt(2, movieID);
                return insertStmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}