package com.salesmanager.web.files;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
 

import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 














import javax.swing.ImageIcon;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.salesmanager.web.utils.DateUtil;
import com.salesmanager.web.utils.ImageMarkLogoByIcon;

 
/**
 * SpringMVC中的文件上传
 * 1)由于SpringMVC使用的是commons-fileupload实现,所以先要将其组件引入项目中
 * 2)在SpringMVC配置文件中配置MultipartResolver处理器(可在此加入对上传文件的属性限制)
 * 3)在Controller的方法中添加MultipartFile参数(该参数用于接收表单中file组件的内容)
 * 4)编写前台表单(注意enctype="multipart/form-data"以及<input type="file" name="****"/>)
 * PS:由于这里使用了ajaxfileupload.js实现无刷新上传,故本例中未使用表单
 * ---------------------------------------------------------------------------------------------
 * 这里用到了如下的jar
 * commons-io-2.4.jar
 * commons-fileupload-1.3.jar
 * commons-logging-1.1.2.jar
 * spring-aop-3.2.4.RELEASE.jar
 * spring-beans-3.2.4.RELEASE.jar
 * spring-context-3.2.4.RELEASE.jar
 * spring-core-3.2.4.RELEASE.jar
 * spring-expression-3.2.4.RELEASE.jar
 * spring-jdbc-3.2.4.RELEASE.jar
 * spring-oxm-3.2.4.RELEASE.jar
 * spring-tx-3.2.4.RELEASE.jar
 * spring-web-3.2.4.RELEASE.jar
 * spring-webmvc-3.2.4.RELEASE.jar
 * ---------------------------------------------------------------------------------------------
 * @create Sep 14, 2013 5:06:09 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
@Controller
@RequestMapping("file")
public class FileControl {
	public static final String serverPath = "/udisk/upload";
    /**
     * 这里这里用的是MultipartFile[] myfiles参数,所以前台就要用<input type="file" name="myfiles"/>
     * 上传文件完毕后返回给前台[0`filepath],0表示上传成功(后跟上传后的文件路径),1表示失败(后跟失败描述)
     */
    @RequestMapping(value="/upload.html")
    public void uploadImg( @RequestParam MultipartFile[] myfiles, HttpServletRequest request, HttpServletResponse response) throws IOException{
        //可以在上传文件的同时接收其它参数
    	String fileName = "" ,savePath="";
       // System.out.println("收到用户[" + uname + "]的文件上传请求");
        //如果用的是Tomcat服务器，则文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\upload\\文件夹中
        //这里实现文件上传操作用的是commons.io.FileUtils类,它会自动判断/upload是否存在,不存在会自动创建
        //String realPath = request.getSession().getServletContext().getRealPath("/upload");
     // 取礼物id作为上传附件的附件名
		String originalFilename = myfiles[0].getOriginalFilename();
		// 拼接附件名
//		String fileRealName = originalFilename.substring(0,
//				originalFilename.lastIndexOf("."));
		fileName = DateUtil.generateTimeStamp() + new Random().nextInt(10000) + "."
				+ originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
		String realPath = serverPath + "/" + DateUtil.generateTimeStamp();
		savePath = realPath + "/" + fileName;
        response.setContentType("text/plain; charset=UTF-8");
        PrintWriter out = response.getWriter();
        for(MultipartFile myfile : myfiles){
        	// 文件写入磁盘
			File dirToMake = new File(realPath);
			if (!dirToMake.exists()) {
				dirToMake.mkdirs();
			}
            if(myfile.isEmpty()){
                out.print("1`请选择文件后上传");
                out.flush();
            }else{
                originalFilename = myfile.getOriginalFilename();
                try {
                    //此处也可以使用Spring提供的MultipartFile.transferTo(File dest)方法实现文件的上传
                     FileUtils.copyInputStreamToFile(myfile.getInputStream(), new File(savePath));
                    //FileCopyUtils.copy(myfile.getBytes(), new File(savePath));
                } catch (IOException e) {
                    System.out.println("文件[" + originalFilename + "]上传失败,堆栈轨迹如下");
                    e.printStackTrace();
                    out.print("1`文件上传失败，请重试！！");
                    out.flush();
                }
            }
        }
        out.print("0`" + savePath);
        out.flush();
        System.out.println(savePath);
    }
    @RequestMapping(value="/upsyload.html")
    public void uploadSYImg( @RequestParam MultipartFile[] myfiles, HttpServletRequest request, HttpServletResponse response) throws IOException{
        //可以在上传文件的同时接收其它参数
    	String fileName = "" ,savePath="";
        // 取礼物id作为上传附件的附件名
		String originalFilename = myfiles[0].getOriginalFilename();
		// 拼接附件名
		fileName = DateUtil.generateTimeStamp() + new Random().nextInt(10000) + "."
				+ originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
		String realPath = serverPath + "/" + DateUtil.generateTimeStamp();
		savePath = realPath + "/" + fileName;
        response.setContentType("text/plain; charset=UTF-8");
        PrintWriter out = response.getWriter();
        for(MultipartFile myfile : myfiles){
        	// 文件写入磁盘
			File dirToMake = new File(realPath);
			if (!dirToMake.exists()) {
				dirToMake.mkdirs();
			}
            if(myfile.isEmpty()){
                out.print("1`请选择文件后上传");
                out.flush();
            }else{
                originalFilename = myfile.getOriginalFilename();
                try {
                    //此处也可以使用Spring提供的MultipartFile.transferTo(File dest)方法实现文件的上传
                	
                  /*  ImageMarkLogoByIcon icon = new ImageMarkLogoByIcon();
        			String auth_path="img/suiyin.png";
        			InputStream in = myfile.getInputStream();
        			InputStream out_in = icon.markImageByIcon(in, auth_path, originalFilename.substring(originalFilename.indexOf("."),originalFilename.length() ));
        			*/
                    FileUtils.copyInputStreamToFile(myfile.getInputStream(), new File(savePath));
                } catch (IOException e) {
                    System.out.println("文件[" + originalFilename + "]上传失败,堆栈轨迹如下");
                    e.printStackTrace();
                    out.print("1`文件上传失败，请重试！！");
                    out.flush();
                }
            }
        }
        out.print("0`" + savePath);
        out.flush();
        System.out.println(savePath);
    }
    @RequestMapping("/showImage.html")
	public void showImage(HttpServletResponse response,String path) throws Exception {  
        try{
        	FileInputStream hFile = new FileInputStream(path); // 以byte流的方式打开文件 d:\1.gif   
            int i=hFile.available(); //得到文件大小   
            byte data[]=new byte[i];   
            hFile.read(data);  //读数据   
            response.setContentType("image/*"); //设置返回的文件类型   
            OutputStream toClient=response.getOutputStream(); //得到向客户端输出二进制数据的对象   
            toClient.write(data);  //输出数据   
            toClient.flush();  
            toClient.close();   
            hFile.close();
        }catch(Exception e){
        	System.out.println(e.getMessage());
        	System.out.println("图片不存在");
        }
	} 
    
 
}