/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
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
    private JSplitPane mainFrame;
    
    public Frame() throws Exception {
        
        Document document = getParameters();
        String conn = document.getElementsByTagName("ConnectString").item(0).getTextContent();
        String usr = document.getElementsByTagName("Username").item(0).getTextContent();
        String pwd = document.getElementsByTagName("Password").item(0).getTextContent();

        h = new Handler(conn, usr, pwd);
        
        this.createMainView();
    }
    
    /**
     * Render the main frame
     */
    public void createMainView() {
        JFrame jf = new JFrame();
        jf.setSize(864, 576);
        jf.setTitle("Leger Des Heils Database Applicatie");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Left Sidebar
        JPanel OptionList = this.getQueryButtons();
        
        // Right Main area
        JTable MainTable = this.getQueryToTable(h.getInADNotInProfit());
        
        mainFrame = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
                OptionList, MainTable);
                
        jf.add(mainFrame);
        jf.setVisible(true);
    }
    
    public JPanel getQueryButtons() {
        JPanel list = new JPanel();
        list.setLayout(new GridLayout(25,1));
        
        JButton[] buttons = new JButton[25];
        
        buttons[0] = new JButton("getUitDienstResult");
        buttons[1] = new JButton("getInProfitNotInAD");
        buttons[2] = new JButton("getInADNotInProfit");
        buttons[3] = new JButton("getInADnotInClever");
        
        for (JButton i : buttons) {
            if(i!=null) {
                list.add(i);
            }
        }
        
        return list;
    }
    
    public JTable getQueryToTable(ResultSet rs) {
        JTable table = null;
        
        /*try {
            while(rs.next()) {
                System.out.println(rs.getString("TABLE_NAME"));
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }*/
        
        String[] columnNames = {"id", "Name"};
        Object[][] data = new Object[2][2];
        
        data[0][0] = "1Test";
        data[0][1] = "2Test";
        data[1][0] = "3Test";
        data[1][1] = "4Test";
        
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        
        table = new JTable(model);
        
        return table;
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
