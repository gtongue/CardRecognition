import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Garrett Tongue on 7/17/2017.
 */
public class Window extends JFrame {

    Panel p = new Panel();
    public Window()
    {
        this.setSize(720, 1280);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setContentPane(p);
        this.setVisible(true);
    }
    public void SetImage(BufferedImage img)
    {
        p.setImage(img);
    }
    public Graphics2D GetGraphics()
    {
        return p.g2d;
    }
}
