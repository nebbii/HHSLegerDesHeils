/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.sql.*;
import java.util.*;

/**
 *
 * @author User
 */
public class App {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        // get parameters
        Document document = getParameters();
        String conn = document.getElementsByTagName("ConnectString").item(0).getTextContent();
        String usr = document.getElementsByTagName("Username").item(0).getTextContent();
        String pwd = document.getElementsByTagName("Password").item(0).getTextContent();
        Scanner sc = new Scanner(System.in);
        
        String state;
        
        try {
            state = "login_start";
            
            // get connection
            Connection myConn = DriverManager.getConnection(conn, usr, pwd);
            
            // get login users
            Statement myStmt = myConn.createStatement();
            
            System.out.println("Leger Des Heils DB Programma:");
            System.out.println("Vul in uw gebruikersnaam:");
            while(state.equals("login_start") && sc.hasNextLine()) {
                String user = sc.nextLine();
                String sql = "select * from gebruikers where user='"+user+"'";
                ResultSet myRs = myStmt.executeQuery(sql);
                
                // check if user exists
                if(myRs.next()) {
                    System.out.println("User found!");
                    state = "login_success";
                } else {
                    System.out.println("No user found.");
                }
            }
            
            System.out.println("End of Program");
            
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
        
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
