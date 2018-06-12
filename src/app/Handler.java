/*
ophalen data van mensen in de active directory
select ad.Username, p.ContractEndDate
from AD.Export ad LEFT JOIN AfasProfit-Export p ON ad.Username_Pre2000 = p.EmployeeUsername
WHERE ad.Disabled = '0'

 */
package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Ben
 */
public class Handler {

    String connectString;
    String user;
    String pass;
    Connection conn;
    
    public Handler(String connectString, String usr, String pwd) {
        try {
            Connection conn = DriverManager.getConnection(connectString,usr,pwd);
            
            System.out.println("verbinding gemaakt...");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void compareData(String connectString, String usr, String pwd) throws SQLException {

        Statement stmt = null;

        String uitDienstQuery = "select Username_Pre2000 , ContractEndDate "
                + "from AD-Export  LEFT JOIN AfasProfit-Export ON Username_Pre2000 = EmployeeUsername "
                + "WHERE Disabled = '0' AND ContractEndDate < '2018-06-11'";

        String inProfitNotInAD = "select EmployeeUsername "
                + "FROM AfasProfit-Export  LEFT JOIN AD-Export ON Username_Pre2000 = EmployeeUsername "
                + "WHERE Username_Pre2000 IS NULL ";

        String inADNotInProfit = "select Username_Pre2000 "
                + "from AD-Export  LEFT JOIN AfasProfit-Export ON Username_Pre2000 = EmployeeUsername "
                + "WHERE EmployeeUsername IS NULL ";
        
        String inADnotClever = "select Username_Pre2000 "
                + "from AD-Export  LEFT JOIN [] ON Username_Pre2000 = EmployeeUsername "
                + "WHERE Disabled = '0' AND ContractEndDate < '2018-06-11'"; 


        try {
            stmt = conn.createStatement();

            /**
             * Vergelijk Profit met de AD Als BA-Account in Afas Profit niet
             * voorkomt in BA-Account van de AD Genereer signaal "RDS User naam
             * in Profit bestaat niet in de AD"
             *
             * Als BA-Account een Datum Einde Contract heeft in Profit maar nog
             * actief is in de AD Genereer SIgnaal "Medewerker uit dienst in
             * Profit, account is in AD actief"
             *
             * Als een BA Account in de AD niet voorkomt in Profit genereer
             * Signaal "AD Account, onbekend in Profit"
             *
             */
            ResultSet rs = stmt.executeQuery(uitDienstQuery);
            while (rs.next()) {
                String coffeeName = rs.getString("Username_Pre2000");
                System.out.print(coffeeName + "\t\t");
                String coffeeName2 = rs.getString("ContractEndDate");
                System.out.println(coffeeName2);
            }

            ResultSet rs2 = stmt.executeQuery(inProfitNotInAD);
            while (rs2.next()) {
                String coffeeName = rs2.getString("EmployeeUsername");
                System.out.println(coffeeName + "\t IS NOT IN AD");

            }

            ResultSet rs3 = stmt.executeQuery(inADNotInProfit);
            while (rs3.next()) {
                String coffeeName = rs3.getString("Username_Pre2000");
                System.out.println(coffeeName + "\t IS NOT IN PROFIT");

            }
            
            
/*
            ResultSet rs4 = stmt.executeQuery(inADNotInProfit);
            while (rs4.next()) {
                String coffeeName = rs4.getString("Username_Pre2000");
                System.out.println(coffeeName + "\t IS NOT IN PROFIT");

            }*/
            

        } catch (SQLException e) {
            System.out.println("Error:");
            System.out.println(e.getMessage());
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
    
    public void getConn() throws SQLException {
        Connection conn = DriverManager.getConnection(connectString,user,pass);
        
        this.conn = conn;
        
        
    }
}
