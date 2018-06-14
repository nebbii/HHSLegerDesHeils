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
    Statement stmt;
    
    public Handler(String connectString, String usr, String pwd) {
        String connectionString = connectString + ";" + usr + ";" + pwd;
        try {
            conn = DriverManager.getConnection(connectionString);
            stmt = conn.createStatement();
            
            System.out.println("Verbinding Gemaakt");
        } catch (SQLException e) {
            System.out.print("Mislukt: ");
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Executes query and returns ResultSet object
     * 
     * @param stmt
     * @param query
     * @return 
     */
    public ResultSet doQuery(String query) {
        ResultSet rs = null;
        
        System.out.println("Current Query:");
        System.out.println(query);
        try {
            rs = stmt.executeQuery(query);
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return rs;
    }
    
    /**
     * Source: modified version of https://stackoverflow.com/a/35290713/2931464
     * 
     * @param resultSet
     * @return
     * @throws SQLException 
     */
    public int doResultSetCount(ResultSet resultSet) throws SQLException{
        try{
            int i = 0;
            while (resultSet.next()) {
                i++;
            }
            return i;
        } catch (SQLException e){
           System.out.println("Error getting row count");
        }
        
        System.out.println("error:");
        resultSet.first();
        return 0;
    }
    
    /**
     * Signaal 1.2
     * Medewerker uit dienst in Profit, account is in AD actief
     * @return 
     */
    public ResultSet getUitDienstResult() {
        String query;
        
        query = "select Username_Pre2000 , ContractEndDate"
                + " from [AD-Export]"
                + " LEFT JOIN [AfasProfit-Export] ON Username_Pre2000 = EmployeeUsername"
                + " WHERE Disabled = '0' "
                + " AND ContractEndDate < '2018-06-11'";
        System.out.println(query);
        return this.doQuery(query);
    }
    
    /**
     *  
     * Signaal 1.1
     * Medewerker uit dienst in Profit, account is in AD actief
     * @return 
     */
    
    public ResultSet getInProfitNotInAD() {
        String query;
        query = "select EmployeeUsername "
                + "FROM [AfasProfit-Export]  "
                + "LEFT JOIN [AD-Export] ON Username_Pre2000 = EmployeeUsername "
                + "WHERE Username_Pre2000 IS NULL ";

        return this.doQuery(query);
    }
    
    /**
     * AD Account onbekend in Profit
     * 1.3
     * @return 
     */
    public ResultSet getInADNotInProfit() {
        String query;
        query = "select Username_Pre2000 "
                + "from [AD-Export]  "
                + "LEFT JOIN [AfasProfit-Export] ON Username_Pre2000 = EmployeeUsername "
                + "WHERE EmployeeUsername IS NULL ";

        return this.doQuery(query);
    }
    
    /**
     * AD Account onbekend in Profit
     * 2.4
     * @return 
     */
    
    public ResultSet getInADnotInClever() {
        String query;
        query = "SELECT ad.[Username_Pre2000] "
                + "FROM [AD-Export] AS ad "
                + "LEFT JOIN [PersoonCodes] AS pc ON pc.[Code] = ad.[Username_Pre2000] "
                + "WHERE pc.[Code] IS NULL  "
                + "AND ad.[Disabled] != '0'";

        return this.doQuery(query);
    }
    
    /**
     * RDS naam in Clevernew is niet ingevuld
     * 2.1
     * @return 
     */
    public ResultSet getNoBaAccountForUserInClever() {
        String query;
        query = "SELECT p.[ID] AS PersoonID "
                + "FROM [Medewerker] AS m "
                + "JOIN [Persoon] AS p ON m.[PersoonID] = p.[ID] "
                + "JOIN [PersoonCodes] AS pc ON p.[ID] = pc.[PersoonID] "
                + "JOIN [Werkzaam] w ON w.[MedewerkerID] = m.[ID] "
                + "WHERE pc.[Code] = 'Andere Code'  "
                + "OR pc.[Code] IS NULL "
                + "AND p.[ID] IS NOT NULL";

        return this.doQuery(query);
    }
    
    /**
     * RDS naam  in CleverNew bestaat niet in AD
     * Query not yet functional
     * 2.2
     * @return 
     */
    public ResultSet getInCleverBaAcNotReal() {
        String query;
        query = "SELECT p.[ID] AS personID, pc.[Code] AS BaAccount " +
            "FROM [Medewerker] AS m " +
            "JOIN [Persoon] AS p ON m.[PersoonID] = p.[ID] " +
            "JOIN [PersoonCodes] AS pc ON p.[ID] = pc.[PersoonID] " +
            "JOIN [Werkzaam] AS w ON w.[MedewerkerID] = m.[ID]" +
            "WHERE [Code] NOT IN ( " +
            "SELECT [Username_Pre2000] " +
            "FROM [AD-Export]) " +
            "AND [Code] != 'Andere Code'";

        return this.doQuery(query);
    }
            
    
    
}
