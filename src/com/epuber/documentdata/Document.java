/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.epuber.documentdata;



import com.epuber.html.Heading;
import com.epuber.html.Paragraph;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.siegmann.epublib.domain.Book;

import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;

/**
 *
 * @author Chamila
 */
public class Document {
    private final Book book;
    private final String path;
    private ArrayList<Chapter> chapters;
    private final String location;
    private final File file;

    public Document(String bookPath) throws IOException {
        path=bookPath;
        EpubReader epubReader = new EpubReader();
        book = epubReader.readEpub(new FileInputStream(path));
       location= path.replaceAll(".epub", "");
        file=new File(location);
        file.mkdir();  //create a temperary directory to store data in the EPUB file

    }

    /*
     * this method retruns the path of the cover image for the selected epub file.
     * the cover image is extracted to a seperate folder from the epub container
     * it is handled by SaveImage class.
     */
    public String getCoverImagePath(){
    String imgPth="";
    SaveImage img=new SaveImage(book,location);
    imgPth=img.getImagePath();

    return imgPth;
    }
    
    /*
     * this method returns the chapters of the epub file as a arraylist of Chapter
     * Chapter contains the headings and the text body as paraghraphs
     */
    public ArrayList<Chapter> getBookChapters(){
    chapters=new ArrayList<Chapter>();

     List<Resource> content = book.getContents(); //get the chapters from epub file
     for (Resource resource : content) {
         // the resources from the epub file are in HTML format. they have to be processed to extract the related info
            processChapters(resource);
     }
    return chapters;
    }
    public MetaData getMetaData(){

       MetaData md=new MetaData();
    md.title=book.getMetadata().getTitles();
    md.copyright=book.getMetadata().getRights();
    md.description=book.getMetadata().getDescriptions();
    md.type=book.getMetadata().getTypes();
    md.publisher=book.getMetadata().getPublishers();
    md.author=book.getMetadata().getAuthors();

    return md;
    }

    /*
     * this method is used to process HTML files from EPUB file and extract the headings
     * and the body text from each chapter. extracted data is added in a Chapter object
     *
     */
    
       private  void processChapters(Resource res){
        BufferedReader br = null;
        Chapter chap=new Chapter();
        try {
            br = new BufferedReader(res.getReader());
            String line="";
                    line = br.readLine();
                   // System.out.println(line);
            String text = line;

            while (line != null) {
               if( line.matches("(.*)(<p.*>)(.*)(</p>)")){ // starting of a paragraph

               String pattern="(?i)(<.*>)(.+?)(</.*>)";// this pattern says line with any tags
               String para=line.replaceAll(pattern, "$2");//replace the tags
               chap.addElement(new Paragraph(para));//add the extracted paragraph

               }
                if( line.matches("(.*)(<h.*>)(.*)(</h.*>)")){ // starting of any heading

               String pattern="(?i)(<.*>)(.+?)(</.*>)";// this pattern says line with any tags
               String heading=line.replaceAll(pattern, "$2");//replace the tags
               chap.addElement(new Heading(heading));//add the extracted paragraph

               }

                line = br.readLine();
                // xText.insertString(xTCursor,s, false);
            }
            //System.out.println(text);
          //  xText.insertString(xTCursor, text, false);
            chapters.add(chap);
        } catch (IOException ex) {
            Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }
       public boolean clearTempData(){

           return file.delete();

       }

}
