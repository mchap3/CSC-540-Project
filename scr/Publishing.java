import java.sql.*;
import java.util.Scanner;

//javac database_setup.java DBManager.java DBTablePrinter.java User.java Reports.java Distribution.java Production.java Publishing.java Publisher.java DistributionTeam.java Editor.java FinancialTeam.java
//java database_setup

/**
 * Publishing API class containing all methods for Publication related
 * operations.
 *
 * @author Morgan Chapman
 */
public class Publishing {

	private DBManager db = null;
	private ResultSet result = null;
	private Scanner scanner = null;

	/**
	 * Constructor with DBManager for database connection and Scanner for user
	 * input.
	 * 
	 * @param dbM DBManager object
	 * @param s   Scanner object
	 */
	public Publishing(DBManager dbM, Scanner s) {
		db = dbM;
		scanner = s;
	}

	/**
	 * Assigns editor to publication.
	 */
	public void assignEditorToPublication() {
		try {
			// display editors
			result = db.query("SELECT * FROM Employees NATURAL JOIN Editors;");
			System.out.println("Editor list:");
			DBTablePrinter.printResultSet(result);

			// display publications
			result = db.query("SELECT * FROM Publication;");
			System.out.println("Publication list:");
			DBTablePrinter.printResultSet(result);

			// select editor and publication
			System.out.print("Enter employee ID: ");
			int employeeID = scanner.nextInt();
			scanner.nextLine();
			System.out.print("Enter publication ID: ");
			int publicationID = scanner.nextInt();
			scanner.nextLine();

			// insert into Edits table
			String sql = String.format("INSERT INTO Edits VALUES (%d, %d);", publicationID, employeeID);
			db.update(sql);

			// print results
			result = db.query(String.format("SELECT * FROM Edits WHERE PublicationID = %d AND EmpID = %d;",
					publicationID, employeeID));
			System.out.println("\nSuccessfully Added Following Record");
			DBTablePrinter.printResultSet(result);
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prints responsibilites of given editor.
	 */
	public void viewEditorResponsibilities() {
		try {
			// display editors
			result = db.query("SELECT * FROM Employees NATURAL JOIN Editors;");
			System.out.println("Editor list:");
			DBTablePrinter.printResultSet(result);

			// select editor to update
			System.out.print("Enter employee ID: ");
			int employeeID = scanner.nextInt();
			scanner.nextLine();

			// select from Publication table
			String sql = String.format("SELECT * FROM Publication WHERE PublicationID IN"
					+ "(SELECT PublicationID FROM Edits WHERE EmpID = %d);", employeeID);
			result = db.query(sql);

			// print results
			System.out.println();
			System.out.println("\nEditor Responsibilities:");
			DBTablePrinter.printResultSet(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds editor: creates new employee, adds it to Editors table.
	 */
	public void addEditor() {
		try {
			// start transaction
			db.disableAutocommit();

			// enter new values
			System.out.print("Enter name: ");
			String name = scanner.nextLine();
			System.out.print("Enter type (Staff/Invited): ");
			String type = scanner.nextLine();
			System.out.print("Enter Phone Number: ");
			String phone = scanner.nextLine();
			System.out.print("Enter Email: ");
			String email = scanner.nextLine();
			System.out.print("Enter Address: ");
			String address = scanner.nextLine();

			// insert into Employees table
			String sql = String.format("INSERT INTO Employees(Name, Type, Phone, Email, Address)"
					+ "VALUES ('%s', '%s', '%s', '%s', '%s');", name, type, phone, email, address);
			db.update(sql);

			// get ID of newly created employee
			result = db.query("SELECT last_insert_id();");
			result.next();
			int employeeID = result.getInt(1);

			// insert into Editors table
			sql = String.format("INSERT INTO Editors VALUES (%d);", employeeID);
			db.update(sql);

			// commit and print results
			result = db.query(String.format("SELECT * FROM Employees WHERE EmpID = %d;", employeeID));
			if (db.commit()) {
				System.out.println("\nSuccessfully Added Following Record");
				DBTablePrinter.printResultSet(result);
				System.out.println();
			}

			// end transaction
			db.enableAutocommit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates editor information.
	 */
	public void updateEditor() {
		try {
			// display editors
			result = db.query("SELECT * FROM Employees NATURAL JOIN Editors;");
			System.out.println("Editor list:");
			DBTablePrinter.printResultSet(result);

			// select editor to update
			System.out.print("Enter employee ID to edit: ");
			int employeeID = scanner.nextInt();
			scanner.nextLine();

			// enter new values
			System.out.print("Enter new employee ID (or 0 to keep current): ");
			int newEmployeeID = scanner.nextInt();
			scanner.nextLine();
			System.out.print("Enter new name (or blank to keep current): ");
			String newName = scanner.nextLine();
			System.out.print("Enter new type (Staff/Invited, or blank to keep current): ");
			String newType = scanner.nextLine();
			System.out.print("Enter new phone (or blank to keep current): ");
			String newPhone = scanner.nextLine();
			System.out.print("Enter new email (or blank to keep current): ");
			String newEmail = scanner.nextLine();
			System.out.print("Enter new address (or blank to keep current): ");
			String newAddress = scanner.nextLine();
//			System.out.print("Enter new active status (true/false, or blank to keep current): ");
//			String newActiveStatus = scanner.nextLine();

			// create SET portion of update string
			String sql = "";
			if (newEmployeeID != 0)
				sql += "EmpID = " + newEmployeeID;
			if (!newName.isEmpty())
				sql += String.format("%sName = '%s'", sql.isEmpty() ? "" : ", ", newName);
			if (!newType.isEmpty())
				sql += String.format("%sType = '%s'", sql.isEmpty() ? "" : ", ", newType);
			if (!newPhone.isEmpty())
				sql += String.format("%sPhone = '%s'", sql.isEmpty() ? "" : ", ", newPhone);
			if (!newEmail.isEmpty())
				sql += String.format("%sEmail = '%s'", sql.isEmpty() ? "" : ", ", newEmail);
			if (!newAddress.isEmpty())
				sql += String.format("%sAddress = '%s'", sql.isEmpty() ? "" : ", ", newAddress);
//			if (!newActiveStatus.isEmpty())
//				sql += String.format("%sActive = %s", sql.isEmpty() ? "" : ", ", newActiveStatus);

			// update if necessary
			if (!sql.isEmpty()) {
				db.update(String.format("Update Employees SET %s WHERE EmpID = %d;", sql, employeeID));
				// print results
				result = db.query(String.format("SELECT * FROM Employees WHERE EmpID = %d;",
						newEmployeeID == 0 ? employeeID : newEmployeeID));
				System.out.println("\nSuccessfully Updated Following Record");
				DBTablePrinter.printResultSet(result);
				System.out.println();
			} else {
				System.out.println("No updates inputted");
				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deletes editor from database.
	 */
	public void deleteEditor() {
		try {
			// display editors
			result = db.query("SELECT * FROM Employees NATURAL JOIN Editors;");
			System.out.println("Editor list:");
			DBTablePrinter.printResultSet(result);

			System.out.print("Enter employee ID to delete: ");
			int employeeID = scanner.nextInt();
			scanner.nextLine();

			String sql = String.format("DELETE FROM Employees WHERE EmpID = %d;", employeeID);
			db.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds author: creates new employee, adds it to Authors table.
	 */
	public int addAuthor() {
		try {
			// start transaction
			db.disableAutocommit();

			// enter new values
			System.out.print("Enter name: ");
			String name = scanner.nextLine();
			System.out.print("Enter type (Staff/Invited): ");
			String type = scanner.nextLine();
			System.out.print("Enter Phone Number: ");
			String phone = scanner.nextLine();
			System.out.print("Enter Email: ");
			String email = scanner.nextLine();
			System.out.print("Enter Address: ");
			String address = scanner.nextLine();

			// insert into Employees table
			String sql = String.format("INSERT INTO Employees(Name, Type, Phone, Email, Address)"
					+ "VALUES ('%s', '%s', '%s', '%s', '%s');", name, type, phone, email, address);
			db.update(sql);

			// get ID of newly created employee
			result = db.query("SELECT last_insert_id();");
			result.next();
			int employeeID = result.getInt(1);

			// insert into Authors table
			sql = String.format("INSERT INTO Authors VALUES (%d);", employeeID);
			db.update(sql);

			// commit and print results
			result = db.query(String.format("SELECT * FROM Employees WHERE EmpID = %d;", employeeID));
			if (db.commit()) {
				System.out.println("\nSuccessfully Added Following Record");
				DBTablePrinter.printResultSet(result);
				System.out.println();
			}

			// end transaction
			db.enableAutocommit();

			return employeeID;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * Updates author information.
	 */
	public void updateAuthor() {
		try {
			// display authors
			result = db.query("SELECT * FROM Employees NATURAL JOIN Authors;");
			System.out.println("Author list:");
			DBTablePrinter.printResultSet(result);

			// select author to update
			System.out.print("Enter employee ID to edit: ");
			int employeeID = scanner.nextInt();
			scanner.nextLine();

			// enter new values
			System.out.print("Enter new employee ID (or 0 to keep current): ");
			int newEmployeeID = scanner.nextInt();
			scanner.nextLine();
			System.out.print("Enter new name (or blank to keep current): ");
			String newName = scanner.nextLine();
			System.out.print("Enter new type (Staff/Invited, or blank to keep current): ");
			String newType = scanner.nextLine();
			System.out.print("Enter new phone (or blank to keep current): ");
			String newPhone = scanner.nextLine();
			System.out.print("Enter new email (or blank to keep current): ");
			String newEmail = scanner.nextLine();
			System.out.print("Enter new address (or blank to keep current): ");
			String newAddress = scanner.nextLine();
//			System.out.print("Enter new active status (true/false, or blank to keep current): ");
//			String newActiveStatus = scanner.nextLine();

			// create SET portion of update string
			String sql = "";
			if (newEmployeeID != 0)
				sql += "EmpID = " + newEmployeeID;
			if (!newName.isEmpty())
				sql += String.format("%sName = '%s'", sql.isEmpty() ? "" : ", ", newName);
			if (!newType.isEmpty())
				sql += String.format("%sType = '%s'", sql.isEmpty() ? "" : ", ", newType);
			if (!newPhone.isEmpty())
				sql += String.format("%sPhone = '%s'", sql.isEmpty() ? "" : ", ", newPhone);
			if (!newEmail.isEmpty())
				sql += String.format("%sEmail = '%s'", sql.isEmpty() ? "" : ", ", newEmail);
			if (!newAddress.isEmpty())
				sql += String.format("%sAddress = '%s'", sql.isEmpty() ? "" : ", ", newAddress);
//			if (!newActiveStatus.isEmpty())
//				sql += String.format("%sActive = %s", sql.isEmpty() ? "" : ", ", newActiveStatus);

			// update if necessary
			if (!sql.isEmpty()) {
				db.update(String.format("Update Employees SET %s WHERE EmpID = %d;", sql, employeeID));
				// print results
				result = db.query(String.format("SELECT * FROM Employees WHERE EmpID = %d;",
						newEmployeeID == 0 ? employeeID : newEmployeeID));
				System.out.println("\nSuccessfully Updated Following Record");
				DBTablePrinter.printResultSet(result);
				System.out.println();
			} else {
				System.out.println("No updates inputted");
				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deletes author from database.
	 */
	public void deleteAuthor() {
		try {
			// display authors
			result = db.query("SELECT * FROM Employees NATURAL JOIN Authors;");
			System.out.println("Author list:");
			DBTablePrinter.printResultSet(result);

			System.out.print("Enter employee ID to delete: ");
			int employeeID = scanner.nextInt();
			scanner.nextLine();

			String sql = String.format("DELETE FROM Employees WHERE EmpID = %d;", employeeID);
			db.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void helper() {

		System.out.println("\nCommand Code | Command Description             | Arguments it needs");
		System.out.println("-------------|---------------------------------|-------------------");

		String[][] help = { { "  P1         | assign editor to publication    | ", "employee ID, publication ID" },
				{ "  P2         | view editor responsibilities    | ", "employee ID" },
				{ "  P3         | add editor                      | ", "name, type" },
				{ "  P4         | update editor                   | ", "employee ID, name, type" },
				{ "  P5         | delete editor                   | ", "employee ID" },
				{ "  P6         | add author                      | ", "employee ID, name, type" },
				{ "  P7         | update author                   | ", "employee ID, name, type" },
				{ "  P8         | delete author                   | ", "employee ID" } };

		for (int i = 0; i < help.length; i++) {
			System.out.println(help[i][0] + help[i][1]);
		}
		System.out.println();
	}

	public void command(String com) {
		System.out.println("Your Command: " + com + "\n");

		switch (com.toLowerCase()) {

		case "p1":
			assignEditorToPublication();
			break;

		case "p2":
			viewEditorResponsibilities();
			break;

		case "p3":
			addEditor();
			break;

		case "p4":
			updateEditor();
			break;

		case "p5":
			deleteEditor();
			break;

		case "p6":
			addAuthor();
			break;

		case "p7":
			updateAuthor();
			break;

		case "p8":
			deleteAuthor();
			break;

		default:
			System.out.println("Here are the Valid Command Codes, and their required information");
			helper();
			break;
		}

	}

}
