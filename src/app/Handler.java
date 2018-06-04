/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Ben
 */
public class Handler {
    String connectString;
    String user;
    String pass;
    
    public Handler(String connectString, String usr, String pwd) {
        String connectionString = connectString;
        try {
            Connection conn = DriverManager.getConnection(connectionString);

            System.out.println("verbinding gemaakt...");
            conn.close();
        } catch (SQLException e) {
            System.out.println("Fout: SQL-server is niet beschikbaar!");

        }
    }
}
