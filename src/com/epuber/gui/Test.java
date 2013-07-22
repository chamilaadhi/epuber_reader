/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.epuber.gui;

import java.io.File;

/**
 *
 * @author Chamila
 */
public class Test {
    public static void main(String[] args) {
        FileChooser f=new FileChooser();
        f.setVisible(true);
       File file= f.getSelectedFile();
       while(file==null)
           file= f.getSelectedFile();
        System.out.println(file.getAbsolutePath());

    }
}
