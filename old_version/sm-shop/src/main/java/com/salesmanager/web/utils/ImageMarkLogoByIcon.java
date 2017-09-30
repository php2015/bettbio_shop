/*package com.salesmanager.web.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ImageMarkLogoByIcon {
	public static InputStream mark(InputStream is,String gs, Color markContentColor, String waterMarkContent) {  
        try {  
            // 读取原图片信息  
            Image srcImg = ImageIO.read(is);  
            int srcImgWidth = srcImg.getWidth(null);  
            int srcImgHeight = srcImg.getHeight(null);  
            // 加水印  
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);  
            Graphics2D g = bufImg.createGraphics();  
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);  
            //Font font = new Font("Courier New", Font.PLAIN, 12);  
            Font font = new Font("宋体", Font.PLAIN, 50);    
            g.setColor(markContentColor); //根据图片的背景设置水印颜色  
            AlphaComposite ac=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);//设置水印字体透明度
            g.setComposite(ac);
            
            g.setFont(font);  
            int x = (srcImgWidth - getWatermarkLength(waterMarkContent, g))/2;  
            int y = srcImgHeight/2; 
            //g.drawString(waterMarkContent, x, y); 
            //g.drawArc(x, y, 20, 5, 0, 5);
            g.rotate(srcImgWidth*4);
            //g.drawString(waterMarkContent, x/4, 3*y);
            
            g.dispose(); 
            ByteArrayOutputStream os = new ByteArrayOutputStream();  
            ImageIO.write(bufImg, gs, os);  
            InputStream inputStream = new ByteArrayInputStream(os.toByteArray()); 
            return inputStream;
            
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;
    }  
      
    *//** 
     * 获取水印文字总长度 
     * @param waterMarkContent 水印的文字 
     * @param g 
     * @return 水印文字总长度 
     *//*  
    public static int getWatermarkLength(String waterMarkContent, Graphics2D g) {  
        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());  
    }  
      
    public static void WaterMarkUtils() {  
        // 原图位置, 输出图片位置, 水印文字颜色, 水印文字  
        //ImageMarkLogoByIcon.mark("e:/test.png", "e:/afterTest.png", Color.red, "水印效果测试");  
    }  
    
    public static void main(String[] args) throws IOException {
    	File file = new File("e:/rr/test.jpg");
    	InputStream test = ImageMarkLogoByIcon.mark(new FileInputStream(file), "jpg", Color.red, "水印效果测试");
    	File copy = new File("e:/rr/copy.jpg");
    	System.out.println(">>>>>>>>>>>>>>>");
    	FileOutputStream out = new FileOutputStream(copy);  
    	byte[] b = new byte[1024];
    	while((test.read(b)) != -1){
    		out.write(b);
		}
    	out.close();
    	test.close();
	}
}
*/

package com.salesmanager.web.utils;   
      
import java.awt.AlphaComposite;   
import java.awt.Graphics2D;   
import java.awt.Image;   
import java.awt.RenderingHints;   
import java.awt.image.BufferedImage;   
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;   

import javax.imageio.ImageIO;   
import javax.swing.ImageIcon;   

    public class ImageMarkLogoByIcon {        
        /**  
         * 给图片添加水印、可设置水印图片旋转角度  
         * @param iconPath 水印图片路径  
         * @param srcImgPath 源图片路径  
         * @param targerPath 目标图片路径  

         */  
        public  InputStream markImageByIcon(InputStream srcImgPath ,String iconPath,  
                String gs) {   
            try {   
                Image srcImg = ImageIO.read(srcImgPath);   
      
                BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),   
                        srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);   
                // 得到画笔对象   
                Graphics2D g = buffImg.createGraphics();   
                // 设置对线段的锯齿状边缘处理   
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,   
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);   
                g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg   
                        .getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);   
                // 水印图象的路径 水印一般为gif或者png的，这样可设置透明度   
                InputStream is = ImageMarkLogoByIcon.class.getClassLoader().getResourceAsStream("img/suiyin.png");
                Image img = ImageIO.read(is);
                float alpha = 0.5f; // 透明度   
                int width = srcImg.getWidth(null);
                int height = srcImg.getHeight(null);
                /*// 表示水印图片的位置   
                int width_biao = img.getWidth(null);
                int height_biao = img.getHeight(null);
                
                int new_width_biao = width_biao; 
                int new_height_biao = height_biao; 

                if(width_biao > width){ 

	                new_width_biao = width; 
	
	                new_height_biao = (int)((double)new_width_biao/width_biao*height); 

                } 

                if(new_height_biao > height){ 

	                new_height_biao = height; 
	
	                new_width_biao = (int)((double)new_height_biao/height_biao*new_width_biao); 

                }  */
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,alpha));  
               /* g.drawImage(img, (width - new_width_biao),
                        0, new_width_biao, new_height_biao, null);
	            g.drawImage(img, (width - new_width_biao) / 2,
	                        (height - new_width_biao) / 2, new_width_biao, new_height_biao, null);
	            g.drawImage(img, 0,
	            		 (height - new_width_biao), new_width_biao, new_height_biao, null);*/
                
                g.drawImage(img, 0,
	            		 0, width, height, null);
                
                g.dispose();   
      
                ByteArrayOutputStream os = new ByteArrayOutputStream();  
                ImageIO.write(buffImg, gs, os);  
                InputStream inputStream = new ByteArrayInputStream(os.toByteArray()); 
                System.out.println("图片完成添加Icon印章。。。。。。");  
                return inputStream; 
            } catch (Exception e) {   
                e.printStackTrace();   
            } 
            return null;
               
        }   
        
       /* public static void main(String[] args) throws IOException {
        	String path = ImageMarkLogoByIcon.class.getClassLoader().getResource(".").getPath();
//        	File file = new File(ImageMarkLogoByIcon.class.getResource("/resources/img/suiyin.png"));
        	File file = new File(path + "img/suiyin.png");
        	String filepath = path + "img/suiyin.png";
        	System.out.println(file.exists());
        	InputStream is = ImageMarkLogoByIcon.class.getClassLoader().getResourceAsStream("img/suiyin.png");
        	System.out.println(is.available());
        	System.out.println(path + "img/suiyin.png");
    	}*/
    }  