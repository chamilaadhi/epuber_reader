package com.epuber.reader;

import com.epuber.documentdata.Chapter;
import com.epuber.documentdata.Document;
import com.epuber.gui.FileChooser;
import com.epuber.gui.InfoUI;
import com.epuber.gui.Wait;
import com.epuber.html.HTMLElement;
import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.lib.uno.helper.Factory;
import com.sun.star.lang.XSingleComponentFactory;
import com.sun.star.registry.XRegistryKey;
import com.sun.star.lib.uno.helper.WeakBase;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextRange;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public final class ePUBer_Reader extends WeakBase
   implements com.sun.star.lang.XServiceInfo,
              com.sun.star.frame.XDispatchProvider,
              com.sun.star.lang.XInitialization,
              com.sun.star.frame.XDispatch
{
    private final XComponentContext m_xContext;
    private com.sun.star.frame.XFrame m_xFrame;
    private static final String m_implementationName = ePUBer_Reader.class.getName();
    private static final String[] m_serviceNames = {
        "com.sun.star.frame.ProtocolHandler" };
    private XTextDocument xDoc;
    private XText xText;
    private XTextCursor xTCursor;
    private XMultiServiceFactory xMSFDoc;
    private Document doc;


    public ePUBer_Reader( XComponentContext context )
    {
        m_xContext = context;
        
    };

    public static XSingleComponentFactory __getComponentFactory( String sImplementationName ) {
        XSingleComponentFactory xFactory = null;

        if ( sImplementationName.equals( m_implementationName ) )
            xFactory = Factory.createComponentFactory(ePUBer_Reader.class, m_serviceNames);
        return xFactory;
    }

    public static boolean __writeRegistryServiceInfo( XRegistryKey xRegistryKey ) {
        return Factory.writeRegistryServiceInfo(m_implementationName,
                                                m_serviceNames,
                                                xRegistryKey);
    }

    // com.sun.star.lang.XServiceInfo:
    public String getImplementationName() {
         return m_implementationName;
    }

    public boolean supportsService( String sService ) {
        int len = m_serviceNames.length;

        for( int i=0; i < len; i++) {
            if (sService.equals(m_serviceNames[i]))
                return true;
        }
        return false;
    }

    public String[] getSupportedServiceNames() {
        return m_serviceNames;
    }

    // com.sun.star.frame.XDispatchProvider:
    public com.sun.star.frame.XDispatch queryDispatch( com.sun.star.util.URL aURL,
                                                       String sTargetFrameName,
                                                       int iSearchFlags )
    {
        if ( aURL.Protocol.compareTo("com.epuber.reader.epuber_reader:") == 0 )
        {
            if ( aURL.Path.compareTo("open") == 0 )
                return this;
            if ( aURL.Path.compareTo("dict") == 0 )
                return this;
            if ( aURL.Path.compareTo("info") == 0 )
                return this;
        }
        return null;
    }

    // com.sun.star.frame.XDispatchProvider:
    public com.sun.star.frame.XDispatch[] queryDispatches(
         com.sun.star.frame.DispatchDescriptor[] seqDescriptors )
    {
        int nCount = seqDescriptors.length;
        com.sun.star.frame.XDispatch[] seqDispatcher =
            new com.sun.star.frame.XDispatch[seqDescriptors.length];

        for( int i=0; i < nCount; ++i )
        {
            seqDispatcher[i] = queryDispatch(seqDescriptors[i].FeatureURL,
                                             seqDescriptors[i].FrameName,
                                             seqDescriptors[i].SearchFlags );
        }
        return seqDispatcher;
    }

    // com.sun.star.lang.XInitialization:
    public void initialize( Object[] object )
        throws com.sun.star.uno.Exception
    {
        if ( object.length > 0 )
        {
            m_xFrame = (com.sun.star.frame.XFrame)UnoRuntime.queryInterface(
                com.sun.star.frame.XFrame.class, object[0]);
        }
    }

    // com.sun.star.frame.XDispatch:
     public void dispatch( com.sun.star.util.URL aURL,
                           com.sun.star.beans.PropertyValue[] aArguments )
    {
         if ( aURL.Protocol.compareTo("com.epuber.reader.epuber_reader:") == 0 )
        {
            if ( aURL.Path.compareTo("open") == 0 )
            {
                // add your own code here
                File f=getEPUB();
               
                System.out.println(f.getAbsolutePath());
                xDoc = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, m_xFrame.getController().getModel());
                xText=xDoc.getText(); //get the text document model
                xTCursor = xText.createTextCursor();//get the text cursor
                xMSFDoc = (XMultiServiceFactory)UnoRuntime.queryInterface(XMultiServiceFactory.class, xDoc);
                 doc=null;
                try {
                    doc = new Document(f.getAbsolutePath());

                } catch (IOException ex) {
                    Logger.getLogger(ePUBer_Reader.class.getName()).log(Level.SEVERE, null, ex);
                }
               //insertCoverPage(doc);
                Wait wait=new Wait();
                wait.setVisible(true);
               insertData(doc.getBookChapters());
               wait.dispose();
              
                return;
            }
            if ( aURL.Path.compareTo("dict") == 0 )
            {
                // add your own code here
                return;
            }
            if ( aURL.Path.compareTo("info") == 0 )
            {
                // add your own code here
                InfoUI info=new InfoUI(doc.getMetaData());
                info.setVisible(true);
                return;
            }
        }
    }

    public void addStatusListener( com.sun.star.frame.XStatusListener xControl,
                                    com.sun.star.util.URL aURL )
    {
        // add your own code here
    }

    public void removeStatusListener( com.sun.star.frame.XStatusListener xControl,
                                       com.sun.star.util.URL aURL )
    {
        // add your own code here
    }

    private void insertData(ArrayList<Chapter> chapters){
      for (Chapter c : chapters) {
                 String t="";
                 String heading="";
                for(HTMLElement elem:c.getElements()) {

                    if(elem.getType()==HTMLElement.HEADING){
                        //xTCursor.gotoEnd(false);
                   // boldText(elem.getData(), xTCursor.getStart(),true);
                    heading=elem.getData();
                    continue;

                    }
                   // if(elem.getType()==HTMLElement.PARAGRAPH){
                    //boldText(elem.getData(), xTCursor.getStart(),false);
                    //continue;

                   // }

                    //System.out.println(elem.getData());

                    t=t+elem.getData()+"\n";
                }

                            boldText(heading, xTCursor.getStart(),true);
                            boldText("", xTCursor.getStart(), false);
                            xText.insertString(xTCursor, t, false);


             }
    }
    private void insertCoverPage(Document d){

        try {
            Object oGraphic = null;
            oGraphic = xMSFDoc.createInstance("com.sun.star.text.TextGraphicObject"); //create Graphic object
            //create Graphic object
            XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, oGraphic );
            xText.insertTextContent(xTCursor, xTextContent, true);
            XPropertySet xPropSet = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, oGraphic);
            //get the property set for the graphic object. the properties of the image can be changed by this
            File sFile = new File(d.getCoverImagePath()); //get the images in the temperary directory and
            StringBuffer sUrl = new StringBuffer("file:///"); //generat the URL
            sUrl.append(sFile.getCanonicalPath().replace('\\', '/'));

            xPropSet.setPropertyValue("AnchorType", com.sun.star.text.TextContentAnchorType.AT_PARAGRAPH);
            // Setting the graphic url
            xPropSet.setPropertyValue("GraphicURL", sUrl.toString());
            // Setting the horizontal position
            xPropSet.setPropertyValue("HoriOrientPosition", new Integer(5500));
            // Setting the vertical position
            xPropSet.setPropertyValue("VertOrientPosition", new Integer(4200));
            // Setting the width
            xPropSet.setPropertyValue("Width", new Integer(100* 110));
            // Setting the height
            xPropSet.setPropertyValue("Height", new Integer(100 * 100));
        } catch (IOException ex) {
            Logger.getLogger(ePUBer_Reader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ePUBer_Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private  void boldText(String text, XTextRange range, boolean bold){
        try {
            XPropertySet xCursorProps = (XPropertySet) UnoRuntime.queryInterface(
            XPropertySet.class, range);
            if(bold)

            {

             xCursorProps.setPropertyValue("CharWeight", new Float(com.sun.star.awt.FontWeight.BOLD));
            xCursorProps.setPropertyValue("CharHeight", (float)20);
            xText.insertString(range, "\n\n\n"+text+"\n\n", true);

            }
            else
            {
            xCursorProps.setPropertyValue("CharWeight", new Float(com.sun.star.awt.FontWeight.NORMAL));
            xCursorProps.setPropertyValue("CharHeight", (float)12);}


           // xText.insertString(range, text, true);
        } catch (Exception ex) {
            Logger.getLogger(ePUBer_Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private File getEPUB(){
        FileChooser f=new FileChooser();
        f.setVisible(true);
       File file= f.getSelectedFile();
       while(file==null)
           file= f.getSelectedFile();

       return file;
    }



}
