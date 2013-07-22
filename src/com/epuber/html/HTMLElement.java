/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.epuber.html;

/**
 *
 * @author Chamila
 */
public class HTMLElement {
    protected  String data="";
    public final static  int HEADING= 1;
    public final static int PARAGRAPH=2;
    protected int type;

    public HTMLElement(String data) {
        this.data=data;
    }

public String getData(){
    return data;
}
public int getType(){
    return type;
}
public void setType(int type){
    this.type=type;
}
}
