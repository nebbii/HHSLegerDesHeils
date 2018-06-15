/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.awt.Color;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
    private JTable mainTable;
    JFrame jf = new JFrame();
    int rowCount;

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

        jf = new JFrame();

        jf.setSize(864, 576);
        jf.setTitle("Leger Des Heils Database Applicatie");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Left Sidebar
        JPanel OptionList = this.getQueryButtons();

        // Right Main area
        //JTable mainTable = this.getQueryToTable(handler.getUitDienstResult());
        mainFrame = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                OptionList, new JScrollPane(mainTable));

        jf.add(mainFrame);
        jf.setVisible(true);
    }

    public JPanel getQueryButtons() {
        JPanel list = new JPanel();
        list.setLayout(new GridLayout(25, 1));
        
        JLabel amountOfSignals = new JLabel("Amount: "+ rowCount);
        
        JButton[] buttons = new JButton[25];

        buttons[0] = new JButton("getUitDienstResult");
        buttons[1] = new JButton("getInProfitNotInAD");
        buttons[2] = new JButton("getInADNotInProfit");
        buttons[3] = new JButton("getInADnotInClever");
        buttons[4] = new JButton("getNoBaAccountForUserInClever");
        buttons[5] = new JButton("getInCleverBaAcNotReal");
        buttons[6] = new JButton("getOutOfFunctionInClever");
        buttons[7] = new JButton("getInProfitNotInClever");
        buttons[8] = new JButton("getOutOfServiceInProfitButNotClever");
        buttons[9] = new JButton("getUserInCleverNotInProfit");
        buttons[10] = new JButton("WriteToNewBD");

        class ClickListener1 implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent event) {
                mainTable = getQueryToTable(handler.getUitDienstResult());
                jf.setVisible(false);
                createMainView();
                
            }
        }
        
        class ClickListener2 implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent event) {
                mainTable = getQueryToTable(handler.getInProfitNotInAD());
                jf.setVisible(false);
                createMainView();
            }
        }
        class ClickListener3 implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent event) {
                mainTable = getQueryToTable(handler.getInADNotInProfit());
                jf.setVisible(false);
                createMainView();

            }
        }
        class ClickListener4 implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent event) {
                mainTable = getQueryToTable(handler.getInADnotInClever());
                jf.setVisible(false);
                createMainView();
            }
        }
        class ClickListener5 implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent event) {
                mainTable = getQueryToTable(handler.getNoBaAccountForUserInClever());
                jf.setVisible(false);
                createMainView();
            }
        }
        class ClickListener6 implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent event) {
                mainTable = getQueryToTable(handler.getInCleverBaAcNotReal());
                jf.setVisible(false);
                createMainView();
            }
        }
        class ClickListener7 implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent event) {
                mainTable = getQueryToTable(handler.getOutOfFunctionInClever());
                jf.setVisible(false);
                createMainView();
            }
        }
        class ClickListener8 implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent event) {
                mainTable = getQueryToTable(handler.getInProfitNotInClever());
                jf.setVisible(false);
                createMainView();
            }
        }
        class ClickListener9 implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent event) {
                mainTable = getQueryToTable(handler.getOutOfServiceInProfitButNotClever());
                jf.setVisible(false);
                createMainView();
            }
        }
        class ClickListener10 implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent event) {
                mainTable = getQueryToTable(handler.getUserInCleverNotInProfit());
                jf.setVisible(false);
                createMainView();
            }
        }
        class ClickListener11 implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    DataProcessor d = new DataProcessor();
                } catch (SQLException ex) {
                    Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SAXException ex) {
                    Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        ActionListener cl1 = new ClickListener1();
        ActionListener cl2 = new ClickListener2();
        ActionListener cl3 = new ClickListener3();
        ActionListener cl4 = new ClickListener4();
        ActionListener cl5 = new ClickListener5();
        ActionListener cl6 = new ClickListener6();
        ActionListener cl7 = new ClickListener7();
        ActionListener cl8 = new ClickListener8();
        ActionListener cl9 = new ClickListener9();
        ActionListener cl10 = new ClickListener10();
        ActionListener cl11 = new ClickListener11();
        buttons[0].addActionListener(cl1);
        buttons[1].addActionListener(cl2);
        buttons[2].addActionListener(cl3);
        buttons[3].addActionListener(cl4);
        buttons[4].addActionListener(cl5);
        buttons[5].addActionListener(cl6);
        buttons[6].addActionListener(cl7);
        buttons[7].addActionListener(cl8);
        buttons[8].addActionListener(cl9);
        buttons[9].addActionListener(cl10);
        buttons[10].addActionListener(cl11);
        
        list.add(amountOfSignals);
        
        for (JButton i : buttons) {
            if (i != null) {
                list.add(i);
            }
        }

        return list;
    }

    /**
     * Gives a JTable with all table data
     *
     * @param rs
     * @return
     */
    public JTable getQueryToTable(ResultSet rs) {
        JTable table = null;
        Object[][] data = null;

        try {
            // metadata for column count
            ResultSetMetaData rsmd = rs.getMetaData();

            // count amount of rows
            rs.last();
            rowCount = rs.getRow();
            rs.beforeFirst();
            
            data = new Object[rowCount-1][rsmd.getColumnCount()];

            // get column names
            String[] columnNames = new String[rsmd.getColumnCount()];
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                // add columns
                columnNames[i] = rsmd.getColumnName(i + 1);
                //data[0][i] = columnNames[i];
            }

            // add data
            System.out.println(rs.getMetaData());
            try {
                if (!rs.next()) {
                    System.out.println("No records found");
                } else {
                    int index = 0;

                    while (rs.next()) {
                        System.out.print("Record found: ");
                        if (index < rowCount) {
                            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                                data[index][i] = rs.getString(i + 1);

                                System.out.print(rs.getString(i + 1));
                                System.out.print(" ");
                            }
                            System.out.println("");
                            index++;
                        }
                    }
                }
            } catch (SQLException e) {
                System.out.println("SQL Exception:");
                System.out.println(e.getMessage());
            }

            //System.out.println(rs.getArray(columnNames[0]));
            DefaultTableModel model = new DefaultTableModel(data, columnNames);

            table = new JTable(model);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return table;
    }

    /**
     * Source: https://stackoverflow.com/a/14968272
     *
     * Returns document containing DB parameters from the XML file
     *
     * @return Document
     */
    private static Document getParameters() throws ParserConfigurationException, SAXException, IOException {
        File parameters = new File("src\\app\\DBParameters.xml");
        DocumentBuilderFactory documentBuilderFactory
                = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder
                = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(parameters);

        return document;
    }
}
