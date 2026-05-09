import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import javax.swing.*;

// This pane is for mini sized banners of movies.
public class moviePane extends JPanel {
    // Utilities utilities = new Utilities();

    private Image posterImage;
    private boolean isHovered = false;

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    private String movieTitle;

    public int getMovieID() {
        return movieID;
    }

    private int movieID;
    private JFrame previousFrame;
    Connection conn;
    User user;

    public moviePane(Connection conn,String title, String poster, int movieID,JFrame previousFrame,User user) {
        this.user=user;
        this.conn=conn;
        this.movieTitle = title;
this.movieID=movieID;
try {
    ImageIcon icon = new ImageIcon(getClass().getResource(poster));
    this.posterImage = icon.getImage();
}
catch (Exception e){
    e.printStackTrace();
}
        setPreferredSize(new Dimension(160, 240));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if(user.isParent()) {
                    movieScreenParent movieScreen = new movieScreenParent(conn, movieTitle, poster, movieID, previousFrame, (Parent) user,moviePane.this);
                    movieScreen.setVisible(true);
                    previousFrame.setVisible(false);
                }
                else{
                    movieScreenChild movieScreen = new movieScreenChild(conn, movieID, previousFrame,(Child) user);
                    movieScreen.setVisible(true);
                    previousFrame.setVisible(false);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;


        if (posterImage != null) {
            g2d.drawImage(posterImage, 0, 0, getWidth(), getHeight(), this);
        }

        if (isHovered) {
            g2d.setColor(new Color(0, 0, 0, 180));
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(movieTitle);

            g2d.drawString(movieTitle, (getWidth() - textWidth) / 2, getHeight() - 20);

            g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        } else {
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
    }
}