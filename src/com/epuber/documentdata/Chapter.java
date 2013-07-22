/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.epuber.documentdata;

import com.epuber.html.HTMLElement;
import java.util.ArrayList;

/**
 *
 * @author Chamila
 */
public class Chapter {
    private ArrayList<HTMLElement> data;
   
    public Chapter() {
        data=new ArrayList<HTMLElement>();
       
    }
    public void addElement(HTMLElement elem){
       data.add(elem);
    }
    
    public ArrayList<HTMLElement> getElements(){
        return data;
    }
    
}
