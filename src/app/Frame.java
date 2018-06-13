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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author willem
 */
public class Frame {
    private Handler h;
    
    public Frame() throws Exception {
        
        Document document = getParameters();
        String conn = document.getElementsByTagName("ConnectString").item(0).getTextContent();
        String usr = document.getElementsByTagName("Username").item(0).getTextContent();
        String pwd = document.getElementsByTagName("Password").item(0).getTextContent();

        JFrame jf = new JFrame();
        jf.setSize(640, 480);
        jf.setTitle("Check fouten in DB");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(1, 3));//gridlayout moet nog veranderd worden.

        JButton toonMax = new JButton("check foutieve gegevens");

        h = new Handler(conn, usr, pwd);

        class Listener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent event) {
                /*try {
                    h.compareData(conn, usr, pwd);
                } catch (SQLException ex) {
                    Logger.getLogger(BuildFrame.class.getName()).log(Level.SEVERE, null, ex);
                }*/
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
        File parameters = new File("src\\app\\DBParameters.xml");
        DocumentBuilderFactory documentBuilderFactory = 
                DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = 
                documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(parameters);
        
        return document;
    }
}
