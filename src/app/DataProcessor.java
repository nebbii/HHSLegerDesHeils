/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.awt.List;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import static java.lang.ProcessBuilder.Redirect.to;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;
import static java.time.LocalDate.from;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author willem
 */
public class DataProcessor {

    private Handler handler;
    Connection connection;
    Statement stmt;

    public DataProcessor() throws SQLException, ParserConfigurationException, SAXException, IOException {
        //Load all data 

        Document document = getParameters();
        String connString = document.getElementsByTagName("ConnectString").item(0).getTextContent();
        String usr = document.getElementsByTagName("Username").item(0).getTextContent();
        String pwd = document.getElementsByTagName("Password").item(0).getTextContent();
        String connectString;
        handler = new Handler(connString, usr, pwd);

        String connectionString = connString + ";" + usr + ";" + pwd;
        try {
            connection = DriverManager.getConnection(connectionString);
            connection.setCatalog("SignalDB");
            System.out.println("Tweede Verbinding Gemaakt");
        } catch (SQLException e) {
            System.out.print("Mislukt: ");
            System.out.println(e.getMessage());
        }

        ResultSet r1 = handler.getUitDienstResult();
        String s1 = "Medewerker uit dienst in Profit, account is in AD actief";
        ArrayList<String> c1 = new ArrayList<>();
        c1.add("Username");
        c1.add("EndDate");
        c1.add("SignalDiscr");
        export(c1, s1, r1);

        ResultSet r2 = handler.getInProfitNotInAD();
        String s2 = "RDS User naam in Profit bestaat niet in de AD";
        ArrayList<String> c2 = new ArrayList<>();
        c2.add("Username");
        c2.add("SignalDiscr");
        export(c2, s2, r2);

        ResultSet r3 = handler.getInADNotInProfit();
        String s3 = "Account onbekend in Profit";
        ArrayList<String> c3 = new ArrayList<>();
        c3.add("Username");
        c3.add("SignalDiscr");
        export(c3, s3, r3);

        ResultSet r4 = handler.getInADnotInClever();
        String s4 = "AD Account onbekend in Clever";
        ArrayList<String> c4 = new ArrayList<>();
        c4.add("Username");
        c4.add("SignalDiscr");
        export(c4, s4, r4);

        ResultSet r5 = handler.getNoBaAccountForUserInClever();
        String s5 = "RDS naam in Clevernew is niet ingevuld";
        ArrayList<String> c5 = new ArrayList<>();
        c5.add("Username");
        c5.add("SignalDiscr");
        export(c5, s5, r5);

        ResultSet r6 = handler.getInCleverBaAcNotReal();
        String s6 = "RDS naam in CleverNew bestaat niet in AD";
        ArrayList<String> c6 = new ArrayList<>();
        c6.add("Username");
        c6.add("PersonID");
        c6.add("SignalDiscr");
        export(c6, s6, r6);

        ResultSet r7 = handler.getOutOfFunctionInClever();
        String s7 = "Medewerker uit dienst in CleverNew, account in AD actief";
        ArrayList<String> c7 = new ArrayList<>();
        c7.add("Username");
        c7.add("EndDate");
        c7.add("SignalDiscr");
        export(c7, s7, r7);

        ResultSet r8 = handler.getInProfitNotInClever();
        String s8 = "RDS User naam in Profit bestaat niet in Clever";
        ArrayList<String> c8 = new ArrayList<>();
        c8.add("Username");
        c8.add("SignalDiscr");
        export(c8, s8, r8);

        ResultSet r9 = handler.getOutOfServiceInProfitButNotClever();
        String s9 = "Medewerker uit dienst in Profit, account is in Clever actief";
        ArrayList<String> c9 = new ArrayList<>();
        c9.add("Username");
        c9.add("PersonID");
        c9.add("SignalDiscr");
        export(c9, s9, r9);

        ResultSet r10 = handler.getUserInCleverNotInProfit();
        String s10 = "RDS User naam in Clever bestaat niet in Afas Profit";
        ArrayList<String> c10 = new ArrayList<>();
        c10.add("Username");
        c10.add("PersonID");
        c10.add("SignalDiscr");
        export(c10, s10, r10);
        System.out.println("DB FILLED");

        Statement deleteDoubles = connection.createStatement();
        deleteDoubles.execute("DELETE FROM Signals "
                + "WHERE SignalID NOT IN ("
                + "SELECT MIN(SignalID) as SignalID "
                + "FROM Signals "
                + "GROUP BY UserName, SignalDiscr)");
    }

    private static Document getParameters() throws ParserConfigurationException, SAXException, IOException {
        File parameters = new File("src\\app\\DBParameters.xml");
        DocumentBuilderFactory documentBuilderFactory
                = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder
                = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(parameters);

        return document;
    }

    public void export(ArrayList<String> columns, String s, ResultSet dataSet) throws SQLException {

        ResultSetMetaData meta = dataSet.getMetaData();

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO [Signals]" + " ("
                + columns.stream().collect(Collectors.joining(", "))
                + ") VALUES ("
                + columns.stream().map(c -> "?").collect(Collectors.joining(", "))
                + ")"
        )) {

            while (dataSet.next()) {
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    preparedStatement.setObject(i, dataSet.getObject(i));
                }

                preparedStatement.setObject(columns.indexOf("SignalDiscr") + 1, s);
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        }
    }

    public void deleteDoubles() throws SQLException, ParserConfigurationException, SAXException, IOException {

        Document document = getParameters();
        String connString = document.getElementsByTagName("ConnectString").item(0).getTextContent();
        String usr = document.getElementsByTagName("Username").item(0).getTextContent();
        String pwd = document.getElementsByTagName("Password").item(0).getTextContent();
        String connectString;

        String connectionString = connString + ";" + usr + ";" + pwd;
        connection = DriverManager.getConnection(connectionString);
        Statement delete = connection.createStatement();
    }

    public void exporteerCSV() throws SQLException, ParserConfigurationException, SAXException, IOException {
        Document document = getParameters();
        String connString = document.getElementsByTagName("ConnectString").item(0).getTextContent();
        String usr = document.getElementsByTagName("Username").item(0).getTextContent();
        String pwd = document.getElementsByTagName("Password").item(0).getTextContent();
        String connectString;
        String connectionString = connString + ";" + usr + ";" + pwd;
        try {
            connection = DriverManager.getConnection(connectionString);
            connection.setCatalog("SignalDB");
            System.out.println("Tweede Verbinding Gemaakt");
        } catch (SQLException e) {
            System.out.print("Mislukt: ");
            System.out.println(e.getMessage());
        }
        
        File file = new File("C:\\Users\\willem\\Desktop\\"+ "SignalExport" + ".csv");

        try (FileOutputStream fop = new FileOutputStream(file)) {

            if (!file.exists()) {
                file.createNewFile();
            }

            String query = "SELECT * FROM Signals";
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {

                int nummerOfColumns = rs.getMetaData().getColumnCount();

                Writer out = new OutputStreamWriter(new BufferedOutputStream(fop));

                for (int i = 1; i < (nummerOfColumns + 1); i++) {

                    out.append(rs.getMetaData().getColumnName(i));
                    if (i < nummerOfColumns) {
                        out.append(",");
                    } else {
                        out.append("\r\n");
                    }
                }

                while (rs.next()) {
                    for (int i = 1; i < (nummerOfColumns + 1); i++) {
                        out.append(rs.getString(i));
                        if (i < nummerOfColumns) {
                            out.append(",");
                        } else {
                            out.append("\r\n");
                        }
                    }
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

}
