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
import java.util.ArrayList;

/**
 *
 * @author Ben
 */
public class Handler {
    
    
    
    String connectString;
    String user;
    String pass;
    ArrayList<ArrayList<String>> obj;

    public ArrayList<ArrayList<String>> getObj() {
        return obj;
    }

    public Handler(String connectString, String usr, String pwd) {
        String connectionString = connectString + ";" + usr + ";" + pwd;
        try {
            Connection conn = DriverManager.getConnection(connectionString);

            System.out.println("verbinding gemaakt...");
            conn.close();
            compareData(connectString,usr,pwd);
        } catch (SQLException e) {
            System.out.println("Fout: SQL-server is niet beschikbaar!");

        }
    }

    public void compareData(String connectString, String usr, String pwd) throws SQLException {

        Statement stmt = null;
        obj = new ArrayList<ArrayList<String>>();

        String uitDienstQuery
                = "select Username_Pre2000 , ContractEndDate "
                + "from [AD-Export]  "
                + "LEFT JOIN [AfasProfit-Export] ON Username_Pre2000 = EmployeeUsername "
                + "WHERE Disabled = '0' "
                + "AND ContractEndDate < '2018-06-11'";

        String inProfitNotInAD
                = "select EmployeeUsername "
                + "FROM [AfasProfit-Export]  "
                + "LEFT JOIN [AD-Export] ON Username_Pre2000 = EmployeeUsername "
                + "WHERE Username_Pre2000 IS NULL ";

        String inADNotInProfit
                = "select Username_Pre2000 "
                + "from [AD-Export]  "
                + "LEFT JOIN [AfasProfit-Export] ON Username_Pre2000 = EmployeeUsername "
                + "WHERE EmployeeUsername IS NULL ";

        String inADnotInClever //signaal 2.4
                = "SELECT ad.[Username_Pre2000] "
                + "FROM [AD-Export] AS ad "
                + "LEFT JOIN [PersoonCodes] AS pc ON pc.[Code] = ad.[Username_Pre2000] "
                + "WHERE pc.[Code] IS NULL  "
                + "AND ad.[Disabled] != '0'";

        String NoBaAccountForUserInClever //signaal 2.1 
                = "SELECT p.[ID] AS pid "
                + "FROM [Medewerker] AS m "
                + "JOIN [Persoon] AS p ON m.[PersoonID] = p.[ID] "
                + "JOIN [PersoonCodes] AS pc ON p.[ID] = pc.[PersoonID] "
                + "JOIN [Werkzaam] w ON w.[MedewerkerID] = m.[ID] "
                + "WHERE pc.[Code] = 'Andere Code'  "
                + "OR pc.[Code] IS NULL";

        String amountOfResults21 //signaal 2.1 WERKT NIET
                = "SELECT count(p.[ID]) AS amount "
                + "FROM [Medewerker] AS m "
                + "JOIN [Persoon] AS p ON m.[PersoonID] = p.[ID] "
                + "JOIN [PersoonCodes] AS pc ON p.[ID] = pc.[PersoonID] "
                + "JOIN [Werkzaam] w ON w.[MedewerkerID] = m.[ID] "
                + "WHERE pc.[Code] = 'Andere Code'  "
                + "OR pc.[Code] IS NULL";

        String OutOfServiceInClever //signaal 2.3 WERKT NIET
                = "SELECT p.[ID] , pc.[Code] "
                + "FROM [Medewerker] AS m "
                + "JOIN [Persoon] AS p ON m.[PersoonID] = p.[ID] "
                + "JOIN [PersoonCodes] AS pc ON p.[ID] = pc.[PersoonID] "
                + "JOIN [AD-Export] AS a ON pc.[Code] = a.[Username_Pre2000] "
                + "JOIN [Werkzaam] AS w ON w.[MedewerkerID] = m.[ID] "
                + "WHERE pc.[Code] != 'Andere Code'  "
                + "AND a.[Disabled] = '0' ";
        //+ "AND pc.[Einddatum] < '2018-06-11' ";

        String connectionString = connectString + ";" + usr + ";" + pwd;

        try {
            Connection conn = DriverManager.getConnection(connectionString);

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
                System.out.print(rs.getString("Username_Pre2000") + "\t\t");
                System.out.println(rs.getString("ContractEndDate") + " Medewerker uit dienst in Profit, account is in AD actief");
                ArrayList<String> tempRow = new ArrayList<>();
                tempRow.add(rs.getString("Username_Pre2000"));
                tempRow.add(rs.getString("ContractEndDate"));
                obj.add(tempRow);
                
                //String [] results= new String[rs.getInt("Username_Pre2000")];
            }
            
            
            ResultSet rs2 = stmt.executeQuery(inProfitNotInAD);
            while (rs2.next()) {
                System.out.println(rs2.getString("EmployeeUsername") + "\t RDS User naam in Profit bestaat niet in de AD");

            }

            ResultSet rs3 = stmt.executeQuery(inADNotInProfit);
            while (rs3.next()) {
                System.out.println(rs3.getString("Username_Pre2000") + "\t AD Account, onbekend in Profit");

            }

            ResultSet rs4 = stmt.executeQuery(inADnotInClever);
            while (rs4.next()) {
                System.out.println(rs4.getString("Username_Pre2000") + "\t AD Account, onbekend in Clever");

            }
            ResultSet rs5 = stmt.executeQuery(NoBaAccountForUserInClever); //WEKRT NIET
            while (rs5.next()) {
                System.out.println(rs5.getString("pid") + "\t RDS naam in Clevernew is niet ingevuld");

            }
            ResultSet rs7 = stmt.executeQuery(amountOfResults21); //WEKRT NIET
            while (rs7.next()) {
                System.out.println(rs7.getString("amount") + "\t results in this query");

            }

            /*
            ResultSet rs6 = stmt.executeQuery(OutOfServiceInClever);
            while (rs6.next()) {
                System.out.print(rs6.getString("ID") + "\t ");
                System.out.println(rs6.getString("Code") + "\t Medewerker uit dienst in CleverNew, account in AD actief");

            }*/
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.out.println("Fout: SQL-server is niet beschikbaar!");

        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
    
    
}
