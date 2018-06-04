/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.xml.parsers.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;

/**
 *
 * @author User
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // get parameters
        Document document = getParameters();
        String conn = document.getElementsByTagName("ConnectString").item(0).getTextContent();
        String usr = document.getElementsByTagName("Username").item(0).getTextContent();
        String pwd = document.getElementsByTagName("Password").item(0).getTextContent();
        
        JFrame jf = new JFrame();
        jf.setSize(1920, 1080);
        jf.setTitle("Check fouten in DB");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(1, 3));//gridlayout moet nog veranderd worden.
        
        JButton toonMax = new JButton("check foutive gegevens");
        
        class Listener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent event) {
                applicatieldh.CheckDB db = new applicatieldh.CheckDB();
            }
        }
        
        ActionListener Listener = new Listener();
        toonMax.addActionListener(Listener);
       
        p.add(toonMax);
        
        jf.add(p);
        jf.setVisible(true);
        
    }
     
    /**
     * Source: https://stackoverflow.com/a/14968272
     * 
     * Returns document containing DB parameters 
     * from the XML file
     * 
     * @return Document
     */
    private static Document getParameters() throws ParserConfigurationException, SAXException, IOException {
        File parameters = new File("src\\legerdesheilsapp\\DBParameters.xml");
        DocumentBuilderFactory documentBuilderFactory = 
                DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = 
                documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(parameters);
        
        return document;
    }
}
