import java.sql.*;
import java.util.Scanner;
import java.io.*;

public class Reports {
	private Connection connection = null;
	private Statement statement = null;
	private ResultSet result = null;

	public Reports(Connection con) {
		connection = con;
	}

	public void generateReport() {
		try {
			statement = connection.createStatement();
			Scanner scanner = new Scanner(System.in);
			
			// Only Part that needs to be changed between each method.
			//************************************************
			System.out.println("Generate Report");
			System.out.println("Enter Valid Start Date for Report (YYYY-MM-DD format):");
			String startDate = scanner.nextLine();

			System.out.println("Enter Valid End Date for Report (YYYY-MM-DD format):");
			String endDate = scanner.nextLine();

			String sql = "SELECT Name AS Distributor,DistAccountNum AS Acc_Num , p.PublicationID, p.Title, SUM(NumCopies), SUM(Price) AS TotalPrice "
					+ "FROM Publication p, Distributors d NATURAL JOIN Orders o WHERE p.PublicationID = o.PublicationID AND ProduceByDate "
					+ ">= '"+ startDate +"' AND ProduceByDate <= '"+ endDate +"' GROUP BY DistAccountNum, p.PublicationID;";

			result = statement.executeQuery(sql);
			
			DBTablePrinter.printResultSet(result);
			//************************************************
			connection.commit();
			scanner.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();

			// If there is an error then rollback the changes.
			System.out.println("Rolling back data here....");
			try {
				if (connection != null)
					System.out.println("Error Generating Report, Please Check Format");
					connection.rollback();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();

		} 
	}

}
