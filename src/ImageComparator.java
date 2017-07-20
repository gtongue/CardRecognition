import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;


/**
 * Created by Garrett Tongue on 7/17/2017.
 */
public class ImageComparator {

    public void FirstAttempt(String img1, String img2)
    {

    }

    public static float compareImages(String path1, String path2) {
        System.out.println(path1 + "-" + path2);

        FeatureDetector detector = FeatureDetector.create(FeatureDetector.ORB);
        DescriptorExtractor descriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);

        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);

        // first image
        Mat img1 = Imgcodecs.imread(path1, Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Mat descriptors1 = new Mat();
        MatOfKeyPoint keypoints1 = new MatOfKeyPoint();

        detector.detect(img1, keypoints1);
        descriptor.compute(img1, keypoints1, descriptors1);

        // second image
        Mat img2 = Imgcodecs.imread(path2, Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Mat descriptors2 = new Mat();
        MatOfKeyPoint keypoints2 = new MatOfKeyPoint();

        detector.detect(img2, keypoints2);
        descriptor.compute(img2, keypoints2, descriptors2);

        // match these two keypoints sets
        MatOfDMatch matches = new MatOfDMatch();
        matcher.match(descriptors1, descriptors2, matches);
        DMatch[] matchesArray = matches.toArray();

        float avgDistance = 0;
        for (DMatch m : matchesArray) {
            // how to use these values to detect the similarity? They seem to be way off
            // all of these values are in range 50-80 which seems wrong to me
            avgDistance+=m.distance;
            System.out.println(m.toString());
        }
        avgDistance = avgDistance/matchesArray.length;
        System.out.println("Average Distance " + avgDistance);
        return  avgDistance;
    }

    public static Mat TemplateMatch(String path1, String path2)
    {
        Mat img1 = Imgcodecs.imread(path1, Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Mat img2 = Imgcodecs.imread(path2, Imgcodecs.CV_LOAD_IMAGE_COLOR);
// / Create the result matrix
        int result_cols = img2.cols() - img1.cols() + 1;
        int result_rows = img2.rows() - img1.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

        // / Do the Matching and Normalize
        Imgproc.matchTemplate(img2, img1, result, Imgproc.TM_CCOEFF_NORMED);
        Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());

        // / Localizing the best match with minMaxLoc
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        System.out.println("Max val" + mmr.maxVal);
        System.out.println("Min val" + mmr.minVal);
        return  img1;
    }

    public static BufferedImage MatToBufferedImage(Mat mat)
    {
        if(mat == null) return null;
        BufferedImage img;
        int cols = mat.cols();
        int rows = mat.rows();
        int elemSize = (int)mat.elemSize();
        byte[] data = new byte[cols * rows * elemSize];
        int type;
        mat.get(0, 0, data);
        switch (mat.channels()) {
            case 1:
                type = BufferedImage.TYPE_BYTE_GRAY;
                break;
            case 3:
                type = BufferedImage.TYPE_3BYTE_BGR;
                // bgr to rgb
                byte b;
                for(int i=0; i<data.length; i=i+3) {
                    b = data[i];
                    data[i] = data[i+2];
                    data[i+2] = b;
                }
                break;
            default:
                return null;
        }
        img = new BufferedImage(cols, rows, type);
        img.getRaster().setDataElements(0,0,cols,rows,data);
        return  img;
    }

    public static void EdgeDetection(String path)
    {
        Mat imgSource = Imgcodecs.imread(path);
      //  Imgproc.threshold(imgSource, imgSource, 25, 255, Imgproc.THRESH_BINARY);
       // Imgproc.Canny(imgSource, imgSource, 0, 600);
        //Imgproc.blur(imgSource,imgSource, 9);
        int cols = imgSource.cols();
        int rows = imgSource.rows();
        Mat blur = Mat.zeros(rows, cols, CvType.CV_8UC3);

        Imgproc.medianBlur(imgSource, blur, 13);

        Imgproc.cvtColor(blur,blur,Imgproc.COLOR_BGR2GRAY);

        Mat canny = new Mat();

        Imgproc.Canny(blur, canny, 0, 500);

        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(canny, contours, new Mat(), 3, 2);
        double largestArea = 0;
        int largestIndex = 0;
        for(int i = contours.size()-1; i >= 0; i--)
        {
            MatOfPoint p = contours.get(i);
            if(p.size().area() > largestArea )
            {
               // contours.remove(i);
                largestArea = p.size().area();
                largestIndex = i;
            }

        }
        //MatofPoint -> MatofPoint2f
        Mat contour = Mat.zeros(rows, cols, CvType.CV_8UC3);
        Scalar color = new Scalar(255, 0, 0);

        Mat mask = Mat.zeros(rows, cols, CvType.CV_8UC1);
        MatOfPoint p = contours.get(largestIndex);

        Rect boundingRect = Imgproc.boundingRect(p);
        //Card card = ContourToCard(p,rows,cols);
        Card card = ContourToCard(p,cols,rows);

        ArrayList<Point> corners = new ArrayList<>();
       /* corners.add(boundingRect.tl());
        corners.add(new Point(boundingRect.tl().x + boundingRect.width, boundingRect.tl().y));
        corners.add(boundingRect.br());
        corners.add(new Point(boundingRect.br().x-boundingRect.width, boundingRect.br().y));*/
        corners.add(card.tl);
        corners.add(card.tr);
        corners.add(card.br);
        corners.add(card.bl);

        ArrayList<Point> target = new ArrayList<>();
        target.add(new Point(0, 0));
        target.add(new Point(imgSource.cols(), 0));
        target.add(new Point(imgSource.cols(), imgSource.rows()));
        target.add(new Point(0, imgSource.rows()));

        Mat cornersMat = Converters.vector_Point2f_to_Mat(corners);
        Mat targetMat = Converters.vector_Point2f_to_Mat(target);

        Mat trans = Imgproc.getPerspectiveTransform(cornersMat, targetMat);
        Mat invTrans = Imgproc.getPerspectiveTransform(targetMat, cornersMat);

        Mat proj = new Mat();
        Imgproc.warpPerspective(imgSource, proj, trans, new Size(imgSource.cols(), imgSource.rows()));

        Mat mat = new Mat(boundingRect.width, boundingRect.height, CvType.CV_8UC3);
        Mat newMat = Mat.zeros(rows, cols, CvType.CV_8UC3);
        Imgproc.drawContours(mask, contours, largestIndex, color, Core.FILLED);

        imgSource.copyTo(mat, mask);

        Imgproc.drawContours(imgSource, contours, largestIndex, color, 3);
        main.w.setSize(imgSource.width(), imgSource.height());
        main.w.SetImage(MatToBufferedImage(imgSource));
        main.w.p.points.add(card.bl);
        main.w.p.points.add(card.br);
        main.w.p.points.add(card.tl);
        main.w.p.points.add(card.tr);

    }

    public static Card ContourToCard(MatOfPoint mat, int imgWidth, int imgHeight)
    {
        Card card = new Card();
        System.out.println("Width: " + imgWidth);
        System.out.println("Height: " + imgHeight);
        card.bl = new Point(imgWidth, 0);
        card.br = new Point(0, 0);
        card.tl = new Point(imgWidth, imgHeight);
        card.tr = new Point(0, imgHeight);
        double threshold = 1;
        for(Point p : mat.toArray())
        {
            double x = p.x;
            double y = p.y;
        //    System.out.println("card x: " + card.bl.x + "x: " + x);
        //    System.out.println("card y: " + card.bl.y + "y: " + y);

            if(x < card.bl.x && y > card.bl.y )
            {
                card.bl.x = x;
                card.bl.y = y;
            }
            if(x < card.tl.x && y < card.tl.y )
            {
                card.tl.x = x;
                card.tl.y = y;
            }
            if(x > card.tr.x && y < card.tr.y + 100)
            {
                System.out.println("here");
                card.tr.x = x;
                card.tr.y = y;
            }if(x > card.br.x && y > card.br.y)
            {
                card.br.x = x;
                card.br.y = y;
            }
        }
        System.out.println(card);
        return  card;
    }
}

class Card {
    public Point bl, br, tl, tr;
    public Card()
    {
        bl = new Point(0,0);
        br = new Point(0,0);
        tl = new Point(0,0);
        tr = new Point(0,0);
    }
    public String toString()
    {
        return "bl: " + bl + " br: " + br + " tl: " + tl + " tr: " + tr;
    }
}
