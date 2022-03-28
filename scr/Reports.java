import java.sql.*;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;


public class Reports {
	private Connection connection = null;
	private Statement statement = null;
	private ResultSet result = null;

	public Reports(Connection con) {
		connection = con;
	}

	
	// Just copy this method and change the inner stuff for each API command
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
	
	private static void GUI_helper() {
		JFrame frame = new JFrame("API Helper");
		JPanel panel = new JPanel();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		String[][] help = {
		         { "generate report", "Start Date, End Date" },
		         { "total monthly publications sold", "Publication ID, Start Date, End Date" },
		         { "total monthly publication revenue", "Start Date, End Date" },
		         { "total monthly expenses", "Start Date, End Date" },
		         { "total distributors", "None" },
		         { "total city revenue", "City Name" },
		         { "total distributor revenue", "Distributor Account Number" },
		         { "total address revenue", "Address" },
		         { "total payments dy time and type of employee", "Start Date, End Date" },
		         { "total payments by type of employee and work type", "Start Date, End Date" }
		      };
		
		String[] header = { "Command", "Arguments it needs" };
		JTable table = new JTable(help, header);
	      panel.add(new JScrollPane(table));
	      frame.add(panel);
	      frame.setSize(550, 400);
		frame.setSize(550, 400);
		frame.setVisible(true);
		
		
	}
	
	public void command(String com) {
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
			
		default: System.out.println("Here are the Valid Command Strings");
			GUI_helper();
				break;
		}
	}
	

}
