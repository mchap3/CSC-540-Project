import java.sql.*;
import java.util.Scanner;

//javac database_setup.java DBManager.java DBTablePrinter.java User.java Reports.java Distribution.java Production.java Publishing.java Publisher.java DistributionTeam.java Editor.java FinancialTeam.java
//java database_setup

/**
 * Reports object, used for generating reports.
 * 
 * @author Chaitanya Patel
 *
 */
public class Reports {

	/** Database manager object */
	private DBManager db = null;

	/** result object that sets the result of a query */
	private ResultSet result = null;

	/** Scanner object for reading input */
	private Scanner scanner = null;

	/**
	 * constructor
	 * 
	 * @param dbM database manager
	 * @param s   scanner
	 */
	public Reports(DBManager dbM, Scanner s) {
		db = dbM;
		scanner = s;

	}

	// Just copy this method and change the inner stuff for each API command

	/**
	 * generates reports
	 */
	public void generateReport() {
		try {

			// start transaction
			db.disableAutocommit();

			// Only Part that needs to be changed between each method.
			// ************************************************
			System.out.println("\nGenerate Report");
			System.out.println("Enter Valid Start Date for Report (YYYY-MM-DD format):");
			String startDate = scanner.nextLine();

			System.out.println("Enter Valid End Date for Report (YYYY-MM-DD format):");
			String endDate = scanner.nextLine();

			String sql = "SELECT Name AS Distributor,DistAccountNum AS Acc_Num , p.PublicationID, p.Title, SUM(NumCopies), SUM(TotalCost) AS TotalPrice "
					+ "FROM Publication p, Distributors d NATURAL JOIN Orders o WHERE p.PublicationID = o.PublicationID AND ProduceByDate "
					+ ">= '" + startDate + "' AND ProduceByDate <= '" + endDate
					+ "' GROUP BY DistAccountNum, p.PublicationID;";

			// commitQuery is for querying, commitUpdate is for everything else and that
			// wont return anything unlike this one.
			result = db.query(sql);

			// commit and if successful, print the table
			if (db.commit()) {
				DBTablePrinter.printResultSet(result);
			}

			System.out.println();
			// ************************************************

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			// enable auto-commit after transaction
			db.enableAutocommit();
		}

	}

	/**
	 * calculates publications sold
	 */
	public void totalPublicationsSold() {
		try {

			// start transaction
			db.disableAutocommit();

			// Only Part that needs to be changed between each method.
			// ************************************************
			System.out.println("\nTotal Publications Sold");
			System.out.println("Enter Valid Start Date for Report (YYYY-MM-DD format):");
			String startDate = scanner.nextLine();

			System.out.println("Enter Valid End Date for Report (YYYY-MM-DD format):");
			String endDate = scanner.nextLine();

			System.out.println("Enter a Valid Publication ID Number:");
			String pubID = scanner.nextLine();

			String sql = "SELECT PublicationID, Title, SUM(NumCopies) AS Total_Copies, SUM(TotalCost) AS Total_Price "
					+ "FROM Publication p NATURAL JOIN Orders o WHERE " + "ProduceByDate >= '" + startDate
					+ "' AND ProduceByDate <= '" + endDate + "' AND PublicationID= " + pubID
					+ " GROUP BY PublicationID;";

			// commitQuery is for querying, commitUpdate is for everything else and that
			// wont return anything unlike this one.
			result = db.query(sql);

			// commit and if successful, print the table
			if (db.commit()) {
				DBTablePrinter.printResultSet(result);
			}

			System.out.println();
			// ************************************************
			// commit will happen in the DBManager when the commitQuery or commitUpdate is
			// run.

		} catch (Exception e) {
			System.out.println("Something Went Wrong, please check that you filled out the form correctly\n");
			e.printStackTrace();

		} finally {
			// enable auto-commit after transaction
			db.enableAutocommit();
		}

	}

	/**
	 * calculates total revenue
	 */
	public void totalRevenue() {
		try {

			// start transaction
			db.disableAutocommit();

			// Only Part that needs to be changed between each method.
			// ************************************************
			System.out.println("\nTotal Revenue");
			System.out.println("Enter Valid Start Date for Report (YYYY-MM-DD format):");
			String startDate = scanner.nextLine();

			System.out.println("Enter Valid End Date for Report (YYYY-MM-DD format):");
			String endDate = scanner.nextLine();

			String sql = "SELECT SUM(TotalCost) AS Total_Revenue_Time_Period FROM Orders " + "WHERE ProduceByDate >= '"
					+ startDate + "' AND ProduceByDate <= '" + endDate + "';";

			// commitQuery is for querying, commitUpdate is for everything else and that
			// wont return anything unlike this one.
			result = db.query(sql);

			// commit and if successful, print the table
			if (db.commit()) {
				DBTablePrinter.printResultSet(result);
			}

			System.out.println();
			// ************************************************
			// commit will happen in the DBManager when the commitQuery or commitUpdate is
			// run.

		} catch (Exception e) {
			System.out.println("Something Went Wrong, please check that you filled out the form correctly\n");

			e.printStackTrace();

		} finally {
			// enable auto-commit after transaction
			db.enableAutocommit();
		}

	}

	/**
	 * calculates total expenses
	 */
	public void totalExpenses() {
		try {

			// start transaction
			db.disableAutocommit();

			// Only Part that needs to be changed between each method.
			// ************************************************
			System.out.println("\nTotal Expenses");
			System.out.println("Enter Valid Start Date for Report (YYYY-MM-DD format):");
			String startDate = scanner.nextLine();

			System.out.println("Enter Valid End Date for Report (YYYY-MM-DD format):");
			String endDate = scanner.nextLine();

			String sql = "SELECT SUM(ShippingCosts) AS Total_ShippingCosts_Time_Period FROM Orders "
					+ "WHERE ProduceByDate >= '" + startDate + "' AND ProduceByDate <= '" + endDate + "';";

			String sq2 = "SELECT SUM(Amount) AS Total_EmployeeCosts_Time_Period FROM Payments "
					+ "WHERE SubmitDate >= '" + startDate + "' AND SubmitDate <= '" + endDate + "';";

			// commitQuery is for querying, commitUpdate is for everything else and that
			// wont return anything unlike this one.

			result = db.query(sql);
			ResultSet result2 = db.query(sq2);

			// commit and if successful, print the tables
			if (db.commit()) {
				System.out.println("\nShipping Costs:");
				DBTablePrinter.printResultSet(result);
				System.out.println("\nEmployee Costs:");
				DBTablePrinter.printResultSet(result2);
			}

			System.out.println();
			// ************************************************
			// commit will happen in the DBManager when the commitQuery or commitUpdate is
			// run.

		} catch (Exception e) {
			System.out.println("Something Went Wrong, please check that you filled out the form correctly\n");

			e.printStackTrace();

		} finally {
			// enable auto-commit after transaction
			db.enableAutocommit();
		}

	}

	/**
	 * returns total number of distributors
	 */
	public void totalDistributors() {
		try {

			// start transaction
			db.disableAutocommit();

			// Only Part that needs to be changed between each method.
			// ************************************************
			System.out.println("\nTotal Distributors");

			String sql = "SELECT COUNT(DistAccountNum) AS Total_Distributors FROM Distributors;";

			// commitQuery is for querying, commitUpdate is for everything else and that
			// wont return anything unlike this one.
			result = db.query(sql);

			// commit and if successful, print the table
			if (db.commit()) {
				DBTablePrinter.printResultSet(result);
			}
			// ************************************************
			// commit will happen in the DBManager when the commitQuery or commitUpdate is
			// run.

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			// enable auto-commit after transaction
			db.enableAutocommit();
		}

	}

	/**
	 * calculates total city revenue
	 */
	public void totalCityRevenue() {
		try {

			// start transaction
			db.disableAutocommit();

			// Only Part that needs to be changed between each method.
			// ************************************************
			System.out.println("\nTotal City Revenue");

			System.out.println("Enter Valid City: ");
			String city = scanner.nextLine();

			String sql = "SELECT SUM(TotalCost) AS Total_Revenue_For_City FROM Orders o, Distributors d "
					+ "WHERE o.DistAccountNum = d.DistAccountNum AND City = '" + city + "';";

			// commitQuery is for querying, commitUpdate is for everything else and that
			// wont return anything unlike this one.
			result = db.query(sql);

			// commit and if successful, print the table
			if (db.commit()) {
				DBTablePrinter.printResultSet(result);
			}
			System.out.println();
			// ************************************************
			// commit will happen in the DBManager when the commitQuery or commitUpdate is
			// run.

		} catch (Exception e) {
			System.out.println("Something Went Wrong, please check that you filled out the form correctly\n");

			e.printStackTrace();

		} finally {
			// enable auto-commit after transaction
			db.enableAutocommit();
		}

	}

	/**
	 * calculates total distributor revenue
	 */
	public void totalDistributorRevenue() {
		try {

			// start transaction
			db.disableAutocommit();

			// Only Part that needs to be changed between each method.
			// ************************************************
			System.out.println("\nTotal Distributor Revenue");

			System.out.println("Enter Valid Distributor ID: ");
			String DistID = scanner.nextLine();

			String sql = "SELECT SUM(TotalCost) AS Total_Revenue_For_Distributor FROM Orders o, Distributors d "
					+ "WHERE o.DistAccountNum = d.DistAccountNum AND d.DistAccountNum = " + DistID + ";";

			// commitQuery is for querying, commitUpdate is for everything else and that
			// wont return anything unlike this one.
			result = db.query(sql);

			// commit and if successful, print the table
			if (db.commit()) {
				DBTablePrinter.printResultSet(result);
			}
			System.out.println();
			// ************************************************
			// commit will happen in the DBManager when the commitQuery or commitUpdate is
			// run.

		} catch (Exception e) {
			System.out.println("Something Went Wrong, please check that you filled out the form correctly\n");

			e.printStackTrace();

		} finally {
			// enable auto-commit after transaction
			db.enableAutocommit();
		}

	}

	/**
	 * calculates address revenue
	 */
	public void totalAddressRevenue() {
		try {

			// start transaction
			db.disableAutocommit();

			// Only Part that needs to be changed between each method.
			// ************************************************
			System.out.println("\nTotal Address Revenue");

			System.out.println("Enter Valid Street Address: ");
			String address = scanner.nextLine();

			String sql = "SELECT SUM(TotalCost) AS Total_Revenue_For_Address FROM Orders o, Distributors d "
					+ "WHERE o.DistAccountNum = d.DistAccountNum AND d.Address = '" + address + "';";

			// commitQuery is for querying, commitUpdate is for everything else and that
			// wont return anything unlike this one.
			result = db.query(sql);

			// commit and if successful, print the table
			if (db.commit()) {
				DBTablePrinter.printResultSet(result);
			}
			System.out.println();
			// ************************************************
			// commit will happen in the DBManager when the commitQuery or commitUpdate is
			// run.

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			// enable auto-commit after transaction
			db.enableAutocommit();
		}

	}

	/**
	 * calculates payments by time and type of employees
	 */
	public void totalPaymentsByTimeAndTypeofEmployee() {
		try {

			// start transaction
			db.disableAutocommit();

			// Only Part that needs to be changed between each method.
			// ************************************************
			System.out.println("\nTotal Payments By Time and Type of Employee");
			System.out.println("Enter Valid Start Date for Report (YYYY-MM-DD format):");
			String startDate = scanner.nextLine();

			System.out.println("Enter Valid End Date for Report (YYYY-MM-DD format):");
			String endDate = scanner.nextLine();

			String sql = "SELECT '" + startDate + " to " + endDate
					+ "' AS PayPeriod, Type, SUM(Amount) AS Total_Pay_For_WorkType "
					+ "FROM Payments p, Employees e, Editors ed WHERE p.EmpID = e.EmpID AND p.EmpID = ed.EmpID AND "
					+ " SubmitDate >= '" + startDate + "' AND SubmitDate <= '" + endDate + "' GROUP BY Type;";

			String sq2 = "SELECT '" + startDate + " to " + endDate
					+ "' AS PayPeriod, Type, SUM(Amount) AS Total_Pay_For_WorkType "
					+ "FROM Payments p, Employees e, Authors ath WHERE p.EmpID = e.EmpID AND p.EmpID = ath.EmpID AND "
					+ " SubmitDate >= '" + startDate + "' AND SubmitDate <= '" + endDate + "' GROUP BY Type;";

// commitQuery is for querying, commitUpdate is for everything else and that wont return anything unlike this one.

			result = db.query(sql);
			ResultSet result2 = db.query(sq2);

			// commit and if successful, print the tables
			if (db.commit()) {
				System.out.println("\nEditor Pay:");
				DBTablePrinter.printResultSet(result);
				System.out.println("\nAuthor Pay:");
				DBTablePrinter.printResultSet(result2);

			}

			System.out.println();
			// ************************************************
			// commit will happen in the DBManager when the commitQuery or commitUpdate is
			// run.

		} catch (Exception e) {
			System.out.println("Something Went Wrong, please check that you filled out the form correctly\n");

			e.printStackTrace();

		} finally {
			// enable auto-commit after transaction
			db.enableAutocommit();
		}

	}

	/**
	 * total payments by type of employee and work type
	 */
	public void totalPaymentsByTypeOfEmployeeAndWorkType() {
		try {

			// start transaction
			db.disableAutocommit();

			// Only Part that needs to be changed between each method.
			// ************************************************
			System.out.println("\nTotal Payments By Type of Employee and Work Type");
			System.out.println("Enter Valid Start Date for Report (YYYY-MM-DD format):");
			String startDate = scanner.nextLine();

			System.out.println("Enter Valid End Date for Report (YYYY-MM-DD format):");
			String endDate = scanner.nextLine();

			String sql = "SELECT '" + startDate + " to " + endDate
					+ "' AS PayPeriod, SUM(Amount) AS Total_Pay_For_WorkType "
					+ "FROM Payments p, Employees e, Editors ed, Edits et WHERE p.EmpID = e.EmpID AND p.EmpID = ed.EmpID AND "
					+ "p.EmpID = et.EmpID AND SubmitDate >= '" + startDate + "' AND SubmitDate <= '" + endDate + "';";

			String sq2 = "SELECT '" + startDate + " to " + endDate
					+ "' AS PayPeriod, SUM(Amount) AS Total_Pay_For_WorkType "
					+ "FROM Payments p, Employees e, Authors ed, WritesBook et WHERE p.EmpID = e.EmpID AND p.EmpID = ed.EmpID AND "
					+ "p.EmpID = et.EmpID AND SubmitDate >= '" + startDate + "' AND SubmitDate <= '" + endDate + "';";

			String sq3 = "SELECT '" + startDate + " to " + endDate
					+ "' AS PayPeriod, SUM(Amount) AS Total_Pay_For_WorkType "
					+ "FROM Payments p, Employees e, Authors ed, WritesArticle et WHERE p.EmpID = e.EmpID AND p.EmpID = ed.EmpID AND "
					+ "p.EmpID = et.EmpID AND SubmitDate >= '" + startDate + "' AND SubmitDate <= '" + endDate + "';";

			// commitQuery is for querying, commitUpdate is for everything else and that
			// wont return anything unlike this one.
			result = db.query(sql);
			ResultSet result2 = db.query(sq2);
			ResultSet result3 = db.query(sq3);
			// commit and if successful, print the tables
			if (db.commit()) {
				System.out.println("\nEditor Pay: ");
				DBTablePrinter.printResultSet(result);
				System.out.println("\nAuthor Pay: for Book:");
				DBTablePrinter.printResultSet(result2);
				System.out.println("\nAuthor Pay: for Article:");
				DBTablePrinter.printResultSet(result3);
			}

			System.out.println();
			// ************************************************
			// commit will happen in the DBManager when the commitQuery or commitUpdate is
			// run.

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			// enable auto-commit after transaction
			db.enableAutocommit();
		}

	}

	/**
	 * return list of distributors
	 */
	public void listOfAllDistributors() {
		try {

			// start transaction
			db.disableAutocommit();

			// Only Part that needs to be changed between each method.
			// ************************************************
			System.out.println("\nList of all Distributors");

			String sql = "SELECT * FROM Distributors;";

			// commitQuery is for querying, commitUpdate is for everything else and that
			// wont return anything unlike this one.
			result = db.query(sql);

			// commit and if successful, print the table
			if (db.commit()) {
				DBTablePrinter.printResultSet(result);
			}
			// ************************************************
			// commit will happen in the DBManager when the commitQuery or commitUpdate is
			// run.

		} catch (Exception e) {
			System.out.println("Something Went Wrong, please check that you filled out the form correctly\n");

			e.printStackTrace();

		} finally {
			// enable auto-commit after transaction
			db.enableAutocommit();
		}

	}

	/**
	 * returns list of employees
	 */
	public void listOfAllEmployees() {
		try {

			// start transaction
			db.disableAutocommit();

			// Only Part that needs to be changed between each method.
			// ************************************************
			System.out.println("\nList of all Distributors");

			String sql = "SELECT * FROM Employees e NATURAL JOIN Editors ed;";
			String sq2 = "SELECT * FROM Employees e NATURAL JOIN Authors a;";

			// commitQuery is for querying, commitUpdate is for everything else and that
			// wont return anything unlike this one.
			result = db.query(sql);
			ResultSet result2 = db.query(sq2);
			System.out.println();
			// commit and if successful, print the tables
			if (db.commit()) {
				System.out.println("\nEditors:");
				DBTablePrinter.printResultSet(result);
				System.out.println("\nAuthors:");
				DBTablePrinter.printResultSet(result2);
			}
			// ************************************************
			// commit will happen in the DBManager when the commitQuery or commitUpdate is
			// run.

		} catch (Exception e) {
			System.out.println("Something Went Wrong, please check that you filled out the form correctly\n");

			e.printStackTrace();

		} finally {
			// enable auto-commit after transaction
			db.enableAutocommit();
		}

	}

	/**
	 * 
	 */
	private static void helper() {

		System.out.println("\nCommand Code | Command Description                                 | Arguments it needs");
		System.out.println("-------------|-----------------------------------------------------|-------------------");

		String[][] help = {
				{ "  R1         | generate report                                     | ", "Start Date, End Date" },
				{ "  R2         | total publications sold                             | ",
						"Publication ID, Start Date, End Date" },
				{ "  R3         | total publication revenue                           | ", "Start Date, End Date" },
				{ "  R4         | total expenses                                      | ", "Start Date, End Date" },
				{ "  R5         | total distributors                                  | ", "None" },
				{ "  R6         | total city revenue                                  | ", "City Name" },
				{ "  R7         | total distributor revenue                           | ",
						"Distributor Account Number" },
				{ "  R8         | total address revenue                               | ", "Address" },
				{ "  R9         | total payments dy time and type of employee         | ", "Start Date, End Date" },
				{ "  R10        | total payments by type of employee and work type    | ", "Start Date, End Date" },
				{ "  R11        | List of all Distributors                            | ", "None" },
				{ "  R12        | List of all Employees                               | ", "None" } };

		for (int i = 0; i < 12; i++) {
			System.out.println(help[i][0] + help[i][1]);
		}
		System.out.println();
	}

	public void command(String com) {
		System.out.println("Your Command: " + com + "\n");

		switch (com.toLowerCase()) {

		case "r1":
			generateReport();
			break;

		case "r2":
			totalPublicationsSold();
			break;

		case "r3":
			totalRevenue();
			break;

		case "rR4":
			totalExpenses();
			break;

		case "r5":
			totalDistributors();
			break;

		case "r6":
			totalCityRevenue();
			break;

		case "r7":
			totalDistributorRevenue();
			break;

		case "r8":
			totalAddressRevenue();
			break;

		case "r9":
			totalPaymentsByTimeAndTypeofEmployee();
			break;

		case "r10":
			totalPaymentsByTypeOfEmployeeAndWorkType();
			break;

		case "r11":
			listOfAllDistributors();
			break;

		case "r12":
			listOfAllEmployees();
			break;

		default:
			System.out.println("Here are the Valid Command Codes, and their required information");
			helper();
			break;
		}

	}

}
