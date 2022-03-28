import java.sql.*;
import java.util.Scanner;



public class Reports {
	private Connection connection = null;
	private Statement statement = null;
	private ResultSet result = null;
	
	private Scanner scanner = null;

	public Reports(Connection con, Scanner s) {
		scanner = s;
		connection = con;
	}

	
	// Just copy this method and change the inner stuff for each API command
	public void generateReport() {
		try {
			statement = connection.createStatement();
			
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
	
	private static void helper() {

		String[][] help = {

		         { "generate report                                     | ", "Start Date, End Date" },
		         { "total monthly publications sold                     | ", "Publication ID, Start Date, End Date" },
		         { "total monthly publication revenue                   | ", "Start Date, End Date" },
		         { "total monthly expenses                              | ", "Start Date, End Date" },
		         { "total distributors                                  | ", "None" },
		         { "total city revenue                                  | ", "City Name" },
		         { "total distributor revenue                           | ", "Distributor Account Number" },
		         { "total address revenue                               | ", "Address" },
		         { "total payments dy time and type of employee         | ", "Start Date, End Date" },
		         { "total payments by type of employee and work type    | ", "Start Date, End Date" }
		      };
		
		System.out.println("\nCommand                                             | Arguments it needs");
		System.out.println("----------------------------------------------------|-------------------");
		for (int i = 0; i < 10; i++) {
			System.out.println(help[i][0] + help[i][1]);
		}
		
	}
	
	public void command(String com) {
		System.out.println("Your Command: " + com);
		
		switch(com.toLowerCase()) {
		
		case "generate report": generateReport();
			break;
		
		case "total monthly publications sold": 
			break;
			
		case "total monthly publication revenue":
			break;
			
		case "total monthly expenses":
			break;
			
		case "total distributors":
			break;
		case "total city revenue":
			break;
		case "total distributor revenue":
			break;
		case "total address revenue":
			break;
		case "total payments dy time and type of employee":
			break;
		case "total payments by type of employee and work type":
			break;
			
		default: System.out.println("Here are the Valid Command Strings, and their required information");
			helper();
				break;
		}
	}
	

}
