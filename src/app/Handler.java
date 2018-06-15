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
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author Ben
 */
public class Handler {

    Connection conn;

    public Connection getConn() {
        return conn;
    }
    String connectString;
    String user;
    String pass;
    Statement stmt;
    String date = LocalDate.now().toString();

    public Handler(String connectString, String usr, String pwd) {
        String connectionString = connectString + ";" + usr + ";" + pwd;
        try {
            conn = DriverManager.getConnection(connectionString);
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

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
        } catch (SQLException e) {
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
    public int doResultSetCount(ResultSet resultSet) throws SQLException {
        try {
            int i = 0;
            while (resultSet.next()) {
                i++;
            }
            return i;
        } catch (SQLException e) {
            System.out.println("Error getting row count");
        }

        System.out.println("error:");
        resultSet.first();
        return 0;
    }

    /**
     * Signaal 1.2 Medewerker uit dienst in Profit, account is in AD actief
     *
     * @return
     */
    public ResultSet getUitDienstResult() {
        String query;

        query = "select Username_Pre2000 , ContractEndDate"
                + " from [AD-Export]"
                + " JOIN [AfasProfit-Export] ON Username_Pre2000 = EmployeeUsername"
                + " WHERE Disabled = '0' "
                + " AND ContractEndDate < '" + date + "'";
        System.out.println(query);
        return this.doQuery(query);
    }

    /**
     *
     * Signaal 1.1 
     *RDS User naam in Profit bestaat niet in de AD
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
     * AD Account onbekend in Profit 1.3
     *
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
     * AD Account onbekend in Clever 2.4
     *
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
     * RDS naam in Clevernew is niet ingevuld 2.1
     *
     * @return
     */
    public ResultSet getNoBaAccountForUserInClever() {
        String query;
        query = "SELECT p.[ID] AS PersoonID "
                + "FROM [Medewerker] AS m "
                + "JOIN [Persoon] AS p ON m.[PersoonID] = p.[ID] "
                + "JOIN [PersoonCodes] AS pc ON p.[ID] = pc.[PersoonID] "
                + "WHERE pc.[Code] = 'Andere Code'  "
                + "OR pc.[Code] IS NULL "
                + "AND p.[ID] IS NOT NULL";

        return this.doQuery(query);
    }

    /**
     * RDS naam in CleverNew bestaat niet in AD 2.2
     *
     * @return
     */
    public ResultSet getInCleverBaAcNotReal() {
        String query;
        query = "SELECT pc.[Code] AS BaAccount, p.[ID] AS personID "
                + "FROM [Medewerker] AS m "
                + "JOIN [Persoon] AS p ON m.[PersoonID] = p.[ID] "
                + "JOIN [PersoonCodes] AS pc ON p.[ID] = pc.[PersoonID] "
                + "WHERE [Code] != 'Andere Code' "
                + "AND [Code] NOT IN ( SELECT Username_Pre2000 FROM [AD-Export])";

        return this.doQuery(query);
    }

    /**
     * Medewerker uit dienst in CleverNew, account in AD actief 2.3
     *
     * @return
     */
    public ResultSet getOutOfFunctionInClever() {
        String query;
        query = "SELECT pc.[Code] AS BaAccount, pc.[Einddatum] "
                + "FROM [Medewerker] AS m "
                + "JOIN [Persoon] AS p ON m.[PersoonID] = p.[ID] "
                + "JOIN [PersoonCodes] AS pc ON p.[ID] = pc.[PersoonID] "
                + "JOIN [AD-Export] AS a ON pc.[Code] = a.[Username_Pre2000] "
                + "WHERE pc.[Code] != 'Andere Code'  "
                + "AND a.[Disabled] = '0' "
                + "AND pc.[Einddatum]< '" + date + "'";

        return this.doQuery(query);
    }

    /**
     * RDS User naam in Profit bestaat niet in Clever 3.1
     *
     * @return
     */
    public ResultSet getInProfitNotInClever() {
        String query;
        query = "SELECT a.[EmployeeUsername] AS [BaAccount] "
                + "FROM [AfasProfit-Export] AS a "
                + "WHERE a.[EmployeeUsername] NOT IN (SELECT pc.[Code] FROM [Medewerker] AS m JOIN [Persoon] AS p ON m.[PersoonID] = p.[ID] JOIN [PersoonCodes] AS pc ON p.[ID] = pc.[PersoonID]) ";
        return this.doQuery(query);
    }

    /**
     * Medewerker uit dienst in Profit, account is in Clever actief 3.2
     *
     * @return
     */
    public ResultSet getOutOfServiceInProfitButNotClever() {
        String query;
        query = "SELECT  a.[EmployeeUsername] AS [BaAccount], p.[ID] AS [personID] "
                + "FROM [AfasProfit-Export] AS a "
                + "JOIN [PersoonCodes] AS pc  ON a.[EmployeeUsername] = pc.[Code] "
                + "JOIN [Persoon] AS p ON p.[ID] = pc.[PersoonID] "
                + "JOIN [Medewerker] AS m ON m.[PersoonID] = p.[ID] "
                + "WHERE a.[ContractEndDate] IS NOT NULL "
                + "AND a.[ContractEndDate] < '" + date + "'";

        return this.doQuery(query);
    }

    /**
     * RDS User naam in Clever bestaat niet in Afas Profit 3.3
     *
     * @return
     */
    public ResultSet getUserInCleverNotInProfit() {
        String query;
        query = "SELECT pc.[Code] AS [baAccount], p.[ID] AS [personID] "
                + "FROM [Medewerker] AS m "
                + "JOIN [Persoon] AS p ON p.[ID] = m.[PersoonID] "
                + "JOIN [PersoonCodes] AS pc ON pc.[PersoonID] = p.[ID] "
                + "WHERE pc.[Code] != 'Andere Code' "
                + "AND pc.[Code] NOT IN (SELECT a.[EmployeeUsername] FROM [AfasProfit-Export] AS a)";

        return this.doQuery(query);
    }

}
