import org.opencv.core.Core;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Garrett Tongue on 7/17/2017.
 */
public class main {
    static Window w = new Window();
    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
    public static void main(String [] args)
    {
//        Mat result = ImageComparator.TemplateMatch("imgs//godsire1.jpg", "imgs//image3.jpg");
 //       BufferedImage img = ImageComparator.MatToBufferedImage(result);
  //      w.SetImage(img);
        /*ImageComparator.TemplateMatch("imgs//godsire1.jpg", "imgs//image3.jpg");
        ImageComparator.TemplateMatch("imgs//godsire1.jpg", "imgs//image3.jpg");
        ImageComparator.TemplateMatch("imgs//godsire1.jpg", "imgs//image3.jpg");
        ImageComparator.TemplateMatch("imgs//godsire1.jpg", "imgs//image3.jpg");*/

        ImageComparator.EdgeDetection("imgs//test2.jpg");
    }
}
