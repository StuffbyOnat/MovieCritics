

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author onatu
 */
public class Utilities {
    
    public ImageIcon setIconSize(int x,int y,String iconPath){
    
    ImageIcon PlayIcon = new ImageIcon(getClass().getResource(iconPath));
        Image scaledImage = PlayIcon.getImage().getScaledInstance(x, y, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImage);
        return resizedIcon;
    }
    
}
