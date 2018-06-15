/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Start application
 *
 * @author Ben
 */
public class App {

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Demo application");
        frame.setSize(300, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        //placeComponents(panel);
        new Frame();
        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {

        panel.setLayout(null);

        JLabel userLabel = new JLabel("User");
        userLabel.setBounds(10, 10, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(100, 10, 160, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 40, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 40, 160, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("login");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);

        class ClickListener1 implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent event) {
                String un;
                String pw;
                String connectString;
                Statement stmt;
                Connection conn;
                un = userText.getText();
                pw = passwordText.getText();

                String connectionString = "jdbc:sqlserver://localhost" + ";user=" + un + ";password=" + pw;
                try {
                    conn = DriverManager.getConnection(connectionString);
                    new Frame();
                } catch (SQLException e) {
                    System.out.print("Mislukt: ");
                    System.out.println(e.getMessage());
                    JOptionPane.showMessageDialog(loginButton, "Login not correct");
                } catch (Exception ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        ActionListener cl1 = new ClickListener1();
        loginButton.addActionListener(cl1);

    }
    //
}
