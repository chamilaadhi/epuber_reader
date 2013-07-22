/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.epuber.documentdata;

/**
 *
 * @author Chamila
 */
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;

public class SaveImage {

    private final String title;
    private String path;
    int width;
    int height;
    private final String location;

    public SaveImage(Book book ,String path) {
       this.location=path;
       title=book.getTitle().replaceAll(" ", "_");
       File f=new File(location+File.separator+"images"); //create a folder to store image
       f.mkdir();
      
        try {
                  Resource res = book.getCoverImage();

            BufferedImage src = ImageIO.read(res.getInputStream());
            if(src!=null)
            {
                BufferedImage image = toBufferedImage(src);
                save(image, "jpg", res.getId());
            }
            else
                System.out.println("no image");

        } catch (IOException ex) {
            Logger.getLogger(SaveImage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    private  void save(BufferedImage image, String ext,String fname) {
        String fileName = fname;
        path=location+File.separator+"images"+File.separator+fileName + "." + ext;
        File file = new File(path);

        try {
            ImageIO.write(image, ext, file);  // ignore returned boolean
        } catch(IOException e) {
            System.out.println("Write error for " + file.getPath() +
                               ": " + e.getMessage());
        }
    }

    private  BufferedImage toBufferedImage(Image src) {
         width = src.getWidth(null);
         height = src.getHeight(null);
        int type = BufferedImage.TYPE_INT_RGB;  // other options
        BufferedImage dest = new BufferedImage(width, height, type);
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
        return dest;
    }
    public String getImagePath(){
    return path;
    }
}
