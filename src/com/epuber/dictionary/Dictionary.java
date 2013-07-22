/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.epuber.dictionary;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Chamila
 */
public class Dictionary {

    private static final String DICTSERVER ="services.aonaware.com";
    private static final int PORT =80;

    /*
     * this static method connects to http://services.aonaware.com and use
     * the dictionary web service provided by them for the dictionary lookup
     * the definition is returned as an arraylist of String
     */
     public static ArrayList<String> getMeaning(String word) throws IOException{
     ArrayList<String> definition=new ArrayList<String>();
         Socket socket = new Socket(DICTSERVER, PORT);
            if(socket.isConnected())
            {
                System.out.println("Connected to http://services.aonaware.com");
                System.out.println("Retrieve the definition for "+word);
            }
             httpGetRequest(socket, word);
     //soapRequest(socket, word);
             definition= httpGetResponse(socket);

      return definition;
    }

     /*
      * this method sends a http get request for the web service with the word needed to search
      */
    private static void httpGetRequest(Socket socket, String word) throws IOException{
        BufferedWriter  wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"));
        wr.write("GET /DictService/DictService.asmx/Define?word="+word+" HTTP/1.1\r\n");
        wr.write("Host: services.aonaware.com\r\n");
        wr.write("\r\n");
        wr.flush();

    }
    /*
     * the message retrieved by the web service is collected to an array list. HTML tags are
     * in the message.
     */
    private static ArrayList<String> httpGetResponse(Socket socket) throws IOException{
            BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ArrayList<String> response=new ArrayList<String>();
            String line= rd.readLine();
            while(line != null || !line.matches("(.*)([</WordDefinition>])(.*)"))
            {   response.add(line);
                System.out.println(line);
                line = rd.readLine();
            }
             //</WordDefinition>
             rd.close();
             return response;
    }

    /*
     * this is an alternative to httpget request. here the request is sent as SOAP message
     * this is under construction
     */
    private static void soapRequest(Socket socket, String word) throws IOException{
        String soapReq=
               " <?xml version=\"1.0\" encoding=\"utf-8\"?>"+
               "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">"+
                    " <soap12:Body>"+
                        "<Define xmlns=\"http://services.aonaware.com/webservices/\">"+
                             "<word>"+word+"</word>"+
                        "</Define>"+
                 "</soap12:Body>"+
               "</soap12:Envelope>";

        System.out.println(soapReq.length());


    BufferedWriter  wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"));
    wr.write("POST /DictService/DictService.asmx HTTP/1.1\r\n");
    wr.write("Host: services.aonaware.com\r\n");
    wr.write("Content-Type: application/soap+xml; charset=utf-8\r\n");
    wr.write("Content-Length:625\r\n");
    wr.write("\r\n");
    wr.write(soapReq);
   // wr.write("\r\n");
    wr.flush();



    }

}
