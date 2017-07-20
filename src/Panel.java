import org.opencv.core.*;

import javax.swing.*;
import java.awt.*;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Garrett Tongue on 7/17/2017.
 */
public class Panel extends JPanel {
    BufferedImage image;
    public Graphics2D g2d;
    public ArrayList<org.opencv.core.Point> points = new ArrayList<>();

    public Panel()
    {

    }

    public void paintComponent(Graphics g)
    {
        g2d = (Graphics2D) g;
        if(image != null)
        {
            g2d.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
        }
        g2d.setColor(Color.red);
        for(org.opencv.core.Point p : points)
        {
            g2d.fillRect((int)p.x,(int)p.y,10,10);
        }
        repaint();
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
