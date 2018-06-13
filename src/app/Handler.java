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
    
    Connection conn;
    String connectString;
    String user;
    String pass;
    
    public Handler(String connectString, String usr, String pwd) {
        String connectionString = connectString + ";" + usr + ";" + pwd;
        try {
            conn = DriverManager.getConnection(connectionString);

            System.out.println("verbinding gemaakt...");
            conn.close();
        } catch (SQLException e) {
            System.out.println("Fout: SQL-server is niet beschikbaar!");

        }
    }
    
    /*public void compareData(String connectString, String usr, String pwd) throws SQLException {

        Statement stmt = null;

        String connectionString = connectString + ";" + usr + ";" + pwd;

        try {
            Connection conn = DriverManager.getConnection(connectionString);

            stmt = conn.createStatement();
            
            ResultSet rs = this.getUitDienstResult(stmt);
            
            
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.out.println("Fout: SQL-server is niet beschikbaar!");

        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }*/
    
    /**
     * Executes query and returns ResultSet object
     * 
     * @param stmt
     * @param query
     * @return 
     */
    public ResultSet doQuery(Statement stmt, String query) {
        ResultSet rs = null;

        try {
            rs = stmt.executeQuery(query);
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return rs;
    }
    
    /**
     *  Medewerker uit dienst in Profit, account is in AD actief
     * 
     * @param stmt
     * @return 
     */
    public ResultSet getUitDienstResult(Statement stmt) {
        String query;
        query = "select Username_Pre2000 , ContractEndDate "
                + "from [AD-Export]  "
                + "LEFT JOIN [AfasProfit-Export] ON Username_Pre2000 = EmployeeUsername "
                + "WHERE Disabled = '0' "
                + "AND ContractEndDate < '2018-06-11'";
        
        return this.doQuery(stmt, query);
    }
    
    public ResultSet getInProfitNotInAD(Statement stmt) {
        String query;
        query = "select EmployeeUsername "
                + "FROM [AfasProfit-Export]  "
                + "LEFT JOIN [AD-Export] ON Username_Pre2000 = EmployeeUsername "
                + "WHERE Username_Pre2000 IS NULL ";

        return this.doQuery(stmt, query);
    }
    
    /**
     * AD Account onbekend in Profit
     * 
     * @param stmt
     * @return 
     */
    public ResultSet getInADNotInProfit(Statement stmt) {
        String query;
        query = "select Username_Pre2000 "
                + "from [AD-Export]  "
                + "LEFT JOIN [AfasProfit-Export] ON Username_Pre2000 = EmployeeUsername "
                + "WHERE EmployeeUsername IS NULL ";

        return this.doQuery(stmt, query);
    }
    
    public ResultSet getInADnotInClever(Statement stmt) {
        String query;
        query = "SELECT ad.[Username_Pre2000] "
                + "FROM [AD-Export] AS ad "
                + "LEFT JOIN [PersoonCodes] AS pc ON pc.[Code] = ad.[Username_Pre2000] "
                + "WHERE pc.[Code] IS NULL  "
                + "AND ad.[Disabled] != '0'";

        return this.doQuery(stmt, query);
    }
    
    /**
     * RDS naam in Clevernew is niet ingevuld
     * Query not yet functional
     * 
     * @param stmt
     * @return 
     */
    public ResultSet getNoBaAccountForUserInClever(Statement stmt) {
        String query;
        query = "SELECT p.[ID] AS pid "
                + "FROM [Medewerker] AS m "
                + "JOIN [Persoon] AS p ON m.[PersoonID] = p.[ID] "
                + "JOIN [PersoonCodes] AS pc ON p.[ID] = pc.[PersoonID] "
                + "JOIN [Werkzaam] w ON w.[MedewerkerID] = m.[ID] "
                + "WHERE pc.[Code] = 'Andere Code'  "
                + "OR pc.[Code] IS NULL";

        return this.doQuery(stmt, query);
    }
    
    /**
     * Werkt nog niet, krijgt count resultaten met join
     * 
     * @param stmt
     * @return 
     *
    public ResultSet getAmountOfResults21(Statement stmt) {
        String query;
        query = "SELECT count(p.[ID]) AS amount "
                + "FROM [Medewerker] AS m "
                + "JOIN [Persoon] AS p ON m.[PersoonID] = p.[ID] "
                + "JOIN [PersoonCodes] AS pc ON p.[ID] = pc.[PersoonID] "
                + "JOIN [Werkzaam] w ON w.[MedewerkerID] = m.[ID] "
                + "WHERE pc.[Code] = 'Andere Code'  "
                + "OR pc.[Code] IS NULL";

        return this.doQuery(stmt, query);
    }*/
    
    /**
     * Werkt nog niet, checkt out of service in clever
     * 
     * @param stmt
     * @return 
     *
    public ResultSet getOutOfServiceInClever(Statement stmt) {
        String query;
        query = "SELECT p.[ID] , pc.[Code] "
                + "FROM [Medewerker] AS m "
                + "JOIN [Persoon] AS p ON m.[PersoonID] = p.[ID] "
                + "JOIN [PersoonCodes] AS pc ON p.[ID] = pc.[PersoonID] "
                + "JOIN [AD-Export] AS a ON pc.[Code] = a.[Username_Pre2000] "
                + "JOIN [Werkzaam] AS w ON w.[MedewerkerID] = m.[ID] "
                + "WHERE pc.[Code] != 'Andere Code'  "
                + "AND a.[Disabled] = '0' ";

        return this.doQuery(stmt, query);
    }*/
            
    
    
}
