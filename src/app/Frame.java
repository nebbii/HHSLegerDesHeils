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
import java.sql.ResultSetMetaData;
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
    private Handler handler;
    private JSplitPane mainFrame;
    
    public Frame() throws Exception {
        
        Document document = getParameters();
        String conn = document.getElementsByTagName("ConnectString").item(0).getTextContent();
        String usr = document.getElementsByTagName("Username").item(0).getTextContent();
        String pwd = document.getElementsByTagName("Password").item(0).getTextContent();

        handler = new Handler(conn, usr, pwd);
        
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
        JTable MainTable = this.getQueryToTable(handler.getUitDienstResult());
        
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
        Object[][] data = null;
        
        

        try {
            // metadata for column count
            ResultSetMetaData rsmd = rs.getMetaData();
            
            // count amount of rows
            //int rowCount = handler.doResultSetCount(rs);
            //rs.beforeFirst();
            
            data = new Object[10000][rsmd.getColumnCount()];
            
            // get column names
            String[] columnNames = new String[rsmd.getColumnCount()];
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                // add columns
                columnNames[i] = rsmd.getColumnName(i+1);
                data[0][i] = columnNames[i];
            }
            
            // add data
            System.out.println(rs.getMetaData());
            try {
                if (!rs.next()) {
                   System.out.println("No records found");
                } else {
                    int index = 1;
                    System.out.println("Records found");
                    while (rs.next()) {
                        System.out.print("Currently at index #");
                        System.out.println(index);
                        for (int i = 1; i < rsmd.getColumnCount(); i++) {
                            data[index][i-1] = rs.getString(i);
                        }
                        index++;
                    }
                }
            } catch (SQLException e) {
                System.out.println("SQL Exception:");
                System.out.println(e.getMessage());
            }
            /*while(rs.next()) {
                for (int j = 0; j < rowCount; j++) {
                    data[j][0] = rs.getString(j);
                    System.out.print("120: loop #");
                    System.out.println(j);
                }
                System.out.println("test");
            }*/
            
            //System.out.println(rs.getArray(columnNames[0]));
            
            DefaultTableModel model = new DefaultTableModel(data, columnNames);

            table = new JTable(model);

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        
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
