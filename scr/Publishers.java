import java.sql.*;
import java.util.Scanner;


//javac database_setup.java DBManager.java DBTablePrinter.java Reports.java User.java
//java database_setup

public class Publishers {

	private DBManager db = null;
	private ResultSet result = null;
	private Scanner scanner = null;

	public Publishers(DBManager dbM, Scanner s) {
		db = dbM;
		scanner = s;
	}

	public void assignEditorToPublication() {
		try {
			// user prompt
			System.out.println("\nAssign an Editor to a Publication");
			System.out.println("Enter Employeee ID:");
			int employeeID = Integer.parseInt(scanner.nextLine());
			System.out.println("Enter Publication ID:");
			int publicationID = Integer.parseInt(scanner.nextLine());
			
			// insert into Edits table
			String sql = String.format("INSERT INTO Edits VALUES (%d, %d);", publicationID, employeeID);
			db.update(sql);
			
			// print results
			result = db.query(String.format("SELECT * FROM Edits WHERE PublicationID = %d AND EmpID = %d;", publicationID, employeeID));
			System.out.println("\nSuccessfully Added Following Record");
			DBTablePrinter.printResultSet(result);
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void viewEditorResponsibilities() {
		try {
			// user prompt
			System.out.println("\nView an Editor's Responsibilities");
			System.out.println("Enter Employeee ID:");
			int employeeID = Integer.parseInt(scanner.nextLine());

			// select from Publication table
			String sql = String.format("SELECT * FROM Publication WHERE PublicationID IN" +
				"(SELECT PublicationID FROM Edits WHERE EmpID = %d);", employeeID);
			result = db.query(sql);
				
			// print results
			System.out.println();
			System.out.println("\nEditor Responsibilities:");
			DBTablePrinter.printResultSet(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addEditor() {
		try {
			// start transaction
			db.disableAutocommit();

			// user prompt
			System.out.println("\nAdd a New Editor");
			System.out.println("Enter Name:");
			String name = scanner.nextLine();
			System.out.println("Enter Type (Staff/Invited):");
			String type = scanner.nextLine();

			// insert into Employees table
			String sql = String.format("INSERT INTO Employees(Name, Type, Active) VALUES ('%s', '%s', true);", name, type);
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

	public void updateEditor() {
		try {
			// select editor to update
			System.out.print("Enter employee ID: ");
			int employeeID = scanner.nextInt(); scanner.nextLine();
			
			// enter new values
			System.out.print("Enter new employee ID (or 0 to keep current): ");
			int newEmployeeID = scanner.nextInt(); scanner.nextLine();
			System.out.print("Enter new name (or blank to keep current): ");
			String newName = scanner.nextLine();
			System.out.print("Enter new type (Staff/Invited, or blank to keep current): ");
			String newType = scanner.nextLine();
			System.out.print("Enter new active status (true/false, or blank to keep current): ");
			String newActiveStatus = scanner.nextLine();
			
			// create SET portion of update string
			String sql = "";
			if (newEmployeeID != 0)
				sql += "EmpID = " + newEmployeeID;
			if (!newName.isEmpty())
				sql += String.format("%sName = '%s'", sql.isEmpty() ? "" : ", ", newName);
			if (!newType.isEmpty())
				sql += String.format("%sType = '%s'", sql.isEmpty() ? "" : ", ", newType);
			if (!newActiveStatus.isEmpty())
				sql += String.format("%sActive = %s", sql.isEmpty() ? "" : ", ", newActiveStatus);
			
			// update if necessary
			if (!sql.isEmpty()) {
				db.update(String.format("Update Employees SET %s WHERE EmpID = %d;", sql, employeeID));
				// print results
				result = db.query(String.format("SELECT * FROM Employees WHERE EmpID = %d;", employeeID));
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

	public void deleteEditor() {
		try {
			result = db.query("SELECT * FROM Employees WHERE EmpID IN (SELECT * FROM Editors);");
			System.out.println("Editor list:");
			DBTablePrinter.printResultSet(result);

			System.out.print("Enter employee ID to delete: ");
			int employeeID = scanner.nextInt(); scanner.nextLine();
			
			String sql = String.format("DELETE FROM Employees WHERE EmpID = %d;", employeeID);
			db.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addAuthor() {
		try {
			// Only Part that needs to be changed between each method.
			//************************************************

			//************************************************
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateAuthor() {
		try {
			// Only Part that needs to be changed between each method.
			//************************************************

			//************************************************
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteAuthor() {
		try {
			// Only Part that needs to be changed between each method.
			//************************************************

			//************************************************
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void helper() {
		
		System.out.println("\nCommand Code | Command Description             | Arguments it needs");
		System.out.println("-------------|---------------------------------|-------------------------------");
		
		String[][] help = {	
		         { "  R1         | assign editor to publication    | ", "Employee ID, Publication ID" },
		         { "  R2         | view editor responsibilities    | ", "Employee ID" },
		         { "  R3         | add editor                      | ", "name, type" },
		         { "  R4         | update editor                   | ", "Employee ID, name, type, active" },
		         { "  R5         | delete editor                   | ", "Employee ID" },
		         { "  R6         | add author                      | ", "Employee ID, name, type" },
		         { "  R7         | update author                   | ", "Employee ID, name, type, active" },
		         { "  R8         | delete author                   | ", "Employee ID" }
		      };
		
		
		for (int i = 0; i < help.length; i++) {
			System.out.println(help[i][0] + help[i][1]);
		}
		System.out.println();
	}
	
	public void command(String com) {
		System.out.println("Your Command: " + com + "\n");
		
		switch(com.toLowerCase()) {
		
		case "r1": assignEditorToPublication();
			break;
		
		case "r2": viewEditorResponsibilities();
			break;
			
		case "r3": addEditor();
			break;
			
		case "r4": updateEditor();
			break;
			
		case "r5": deleteEditor();
			break;
			
		case "r6": addAuthor();
			break;
			
		case "r7": updateAuthor();
			break;
			
		case "r8": deleteAuthor();
			break;
			
		default: System.out.println("Here are the Valid Command Codes, and their required information");
			helper();
				break;
		}
		
	}
	

}
