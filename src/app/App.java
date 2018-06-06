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
import java.sql.Connection;
import java.sql.DriverManager;
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
        String connUrl = document.getElementsByTagName("ConnectString").item(0).getTextContent();

        // start connection
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Connection connection = DriverManager.getConnection(connUrl);
        System.out.println("Connection made");
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
