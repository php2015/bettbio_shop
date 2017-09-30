/**
 * Created by philip on 16-11-18.
 */
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageScaler {

    /**
     * @param args
     */
    public static void listFile(String dirname)
    {
        File file = new File(dirname);
        File[] filelist=file.listFiles();
        for(int i=0;i<filelist.length;i++)
        {
            System.out.println(filelist[i].getName());
        }
    }
    public static void scaleImage(String dirname)
    {
        File file = new File(dirname);
        File[] filelist=file.listFiles();
        for(int i=0;i<filelist.length;i++)
        {
            //toSmall(dirname,filelist[i].getName());
            toSmall(dirname,filelist[i].getName());
            System.out.println(filelist[i].getName());
        }
    }
    public static void depart(String dirName,String fileName)
    {

        Image image = null;
        try {
            // Read from a file
            File file = new File(dirName+"/"+fileName);
            image = ImageIO.read(file);
            //int width=image.getWidth(null);
            //int height=image.getHeight(null);
            BufferedImage bimage = new BufferedImage( 500,500,BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d  = bimage.createGraphics();
            //g2d.drawImage(image, 0, 0, null);
            g2d.drawImage(image, -82, -88, 1040,824, null);

            g2d.dispose();
            File fileOut = new File(dirName+"/small_"+fileName);

            //bimage.getWidth()
            ImageIO.write(bimage, "jpg", fileOut);


        } catch (IOException e) {
            e.printStackTrace();

        }

    }
    private static void toSmall(String dirName,String fileName)
    {
        Image image = null;
        try {
            // Read from a file
            File file = new File(dirName+"/"+fileName);
            image = ImageIO.read(file);
            int width=image.getWidth(null);
            int height=image.getHeight(null);
            BufferedImage bimage = new BufferedImage( width/2, height/2, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d  = bimage.createGraphics();
            //g2d.drawImage(image, 0, 0, null);
            g2d.drawImage(image, 0, 0, width/2, height/2, null);

            g2d.dispose();
            File fileOut = new File(dirName+"/small_"+fileName);

            //bimage.getWidth()
            ImageIO.write(bimage, "png", fileOut);


        } catch (IOException e) {
            e.printStackTrace();

        }

    }
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        toSmall(args[0],args[1]);
    }
    public static void main2(String[] args) {
        // TODO Auto-generated method stub
        if(args.length>=1){

            scaleImage(args[0]);
        }
    }

}

