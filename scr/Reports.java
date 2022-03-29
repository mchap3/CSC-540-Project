import java.sql.*;
import java.util.Scanner;


//javac database_setup.java DBManager.java DBTablePrinter.java Reports.java User.java
//java database_setup

public class Reports {

	private DBManager db = null;
	private ResultSet result = null;
	
	private Scanner scanner = null;

	public Reports(DBManager dbM, Scanner s) {
		db = dbM;
		scanner = s;

	}

	
	// Just copy this method and change the inner stuff for each API command
	public void generateReport() {
		try {

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

			result = db.commit(sql);
			
			DBTablePrinter.printResultSet(result);
			//************************************************
			// commit will happen in the command method after everything is run 
			
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
