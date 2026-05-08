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
import javax.swing.ImageIcon;
import javax.swing.JPanel;

// This pane is for mini sized banners of movies.
public class moviePane extends JPanel {
    // Utilities utilities = new Utilities();

    private Image posterImage;
    private boolean isHovered = false;
    private String movieTitle;

    public moviePane(String title, String poster) {
        this.movieTitle = title;

        ImageIcon icon = new ImageIcon(poster);
        this.posterImage = icon.getImage();

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
                System.out.println(movieTitle + " kartına tıklandı!");

                // İleride buraya filmin detay ekranını (MovieDetailFrame) açan kodu yazacaksın.
                // Örnek: new MovieDetailFrame(movieTitle).setVisible(true);
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