/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.sql.*;

/**
 *
 * @author Ben
 */
public class Handler {
    String conn;
    String user;
    String pass;
    
    public Handler(String conn, String usr, String pwd) {
        try {
            // get connection
            Connection myConn = DriverManager.getConnection(conn, usr, pwd);
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
