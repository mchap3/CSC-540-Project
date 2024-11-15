import java.sql.*;
import java.util.Scanner;
//javac database_setup.java DBManager.java DBTablePrinter.java User.java Reports.java Distribution.java Production.java Publishing.java Publisher.java DistributionTeam.java Editor.java FinancialTeam.java
//java database_setup

/**
 * Distribution API class containing methods printDistributor() - print
 * distributor info addDistributor() - add distributor delDistributor() - delete
 * distributor balanceDistributor() - change distributor balance placeOrder() -
 * place publication order newInvoice() - bill distributor (create new invocie)
 * paymentInvoice() - update invoice payment status
 * 
 * @author Ilya Arakelyan
 */
public class Distribution {

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
	public Distribution(DBManager dbM, Scanner s) {
		db = dbM;
		scanner = s;

	}

	/**
	 * add distributor API. Prompt user for distributor info and update Database
	 */
	public void addDistributor() {
		try {

			// start transaction
			db.disableAutocommit();

			// Get the highest ID from the Distributor table
			int maxID = 0;
			result = db.query("select max(DistAccountNum) as MaxID from Distributors;");
			if (db.commit()) {
				if (result.next()) {
					maxID = result.getInt("MaxID");
				}
			}
			// user prompt
			System.out.println("\nAdd New Distributor");
			System.out.println("Enter New Account ID(current max ID is " + maxID + ", use 0 for auto increment");
			String newID_str = scanner.nextLine();
			int newID = Integer.parseInt(newID_str);
			if (newID == 0) {
				newID_str = "NULL";
			}
			if (newID <= maxID) {
				newID = maxID + 1;
				newID_str = Integer.toString(newID);
			}
			System.out.println("Enter New Account Name:");
			String d_name = "'" + scanner.nextLine() + "'";
			System.out.println("Enter New Account Type:");
			String d_type = "'" + scanner.nextLine() + "'";
			System.out.println("Enter New Account Street Address:");
			String d_street = "'" + scanner.nextLine() + "'";
			System.out.println("Enter New Account City:");
			String d_city = "'" + scanner.nextLine() + "'";
			System.out.println("Enter New Account Phone:");
			String d_phone = "'" + scanner.nextLine() + "'";
			System.out.println("Enter New Account Contact Name:");
			String d_contact = "'" + scanner.nextLine() + "'";
			System.out.println("Enter New Account Balance:");
			String d_balance = scanner.nextLine();
			// update string
			String sql = "INSERT INTO Distributors VALUES (" + newID_str + "," + d_name + "," + d_type + "," + d_street
					+ "," + d_city + "," + d_phone + "," + d_contact + "," + d_balance + ");";
			// update database and commit
			db.update(sql);
			result = db.query("SELECT * FROM Distributors WHERE DistAccountNum =" + newID_str + " ;");
			if (db.commit()) {
				System.out.println("\nSuccessfully Added Following Record");
				DBTablePrinter.printResultSet(result);
				System.out.println();
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			// enable auto-commit after transaction
			db.enableAutocommit();
		}

	}

	/**
	 * Delete distributor API. Prompt user for Distributor ID, delete the
	 * Distributor, update Database
	 */
	public void delDistributor() {
		try {

			// start transaction
			db.disableAutocommit();

			// user prompt
			System.out.println("\nDelete Distributor");
			System.out.println("Enter Account ID to delete:");
			int newID = Integer.parseInt(scanner.nextLine());
			// pull the record and ask for confirmation
			result = db.query("SELECT * FROM Distributors WHERE DistAccountNum =" + newID + " ;");
			System.out.println("\nThe following record will be deleted:");
			DBTablePrinter.printResultSet(result);
			System.out.println("Confirm (yes/no)?");
			String userType = scanner.nextLine().toLowerCase();
			if (userType.equals("yes")) {
				String sql = "DELETE FROM Distributors WHERE DistAccountNum = " + newID + ";";
				// update DB and commit
				db.update(sql);
				if (db.commit()) {
					System.out.println("\nRecord Deleted");
					System.out.println();
				}
			} else {
				System.out.println("No changes made");
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			// enable auto-commit after transaction
			db.enableAutocommit();
		}

	}

	/**
	 * Print distributor info API. Prompt user for Distributor ID, print Distributor
	 * info
	 */
	public void printDistributor() {
		try {

			// start transaction
			db.disableAutocommit();

			// user prompt
			String sql = null;
			System.out.println("\nPrint Distributor");
			System.out.println("Enter Account ID to print (* for all):");
			String userType = scanner.nextLine();
			if (userType.equals("*")) {
				sql = "SELECT * FROM Distributors;";
			} else {
				sql = "SELECT * FROM Distributors WHERE DistAccountNum =" + userType + " ;";
			}
			// pull the record and print
			result = db.query(sql);
			if (db.commit()) {
				DBTablePrinter.printResultSet(result);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// enable auto-commit after transaction
			db.enableAutocommit();
		}

	}

	/**
	 * update distributor API pick rows for with a given value for selected
	 * attribute, modify one colums for those rows e.g., Change the Contact
	 * attribute (from 'John' to 'Jane') of a distributor specified by Name
	 * ('Library').
	 */
	public void updateDistributor() {
		try {

			// start transaction
			db.disableAutocommit();

			// user prompt for what to update
			String sql = null;
			System.out.println("\nUpdate Distributor");
			System.out.println("Enter selection attribute (e.g., DistAccountNum):");
			String sel_attr = scanner.nextLine();
			System.out.println("Enter selection attribute value (e.g., 4):");
			String sel_value = "'" + scanner.nextLine() + "'";
			sql = "SELECT * FROM Distributors WHERE " + sel_attr + " = " + sel_value + ";";

			// pull the record
			result = db.query(sql);
			DBTablePrinter.printResultSet(result);

			// user prompt for new values
			System.out.println("Enter attribute to update:");
			String up_attr = scanner.nextLine();
			System.out.println("Enter new value:");
			String up_value = "'" + scanner.nextLine() + "'";
			String sql1 = "UPDATE Distributors SET " + up_attr + " = " + up_value + " WHERE " + sel_attr + " = "
					+ sel_value + ";";

			// confirm the change and pull updated record
			System.out.println("Confirm changes? (yes/no)");
			String userType = scanner.nextLine().toLowerCase();
			if (userType.equals("yes")) {
				db.update(sql1);
				System.out.println("Updated records:");
				result = db.query("SELECT * FROM Distributors WHERE " + up_attr + " = " + up_value + ";");
				if (db.commit()) {
					DBTablePrinter.printResultSet(result);
				}
			} else {
				// do nothing
				System.out.println("No changes made");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// enable auto-commit after transaction
			db.enableAutocommit();
		}

	}

	/**
	 * Change distributor balance API. Prompt user for Distributor ID, change
	 * Distributor balance
	 */
	public void balanceDistributor() {
		try {

			// start transaction
			db.disableAutocommit();

			// user prompt for which account to update
			String sql = null;
			System.out.println("\nUpdate Distributor Balance");
			System.out.println("Enter Distributor ID:");
			String sel_id = scanner.nextLine();
			System.out.println("Current Balance");
			sql = "SELECT DistAccountNum, Balance FROM Distributors WHERE DistAccountNum = " + sel_id + ";";
			// pull the record
			result = db.query(sql);
			DBTablePrinter.printResultSet(result);
			// user prompt for new values
			System.out.println("Enter New Balance:");
			String up_value = scanner.nextLine();

			// confirm the change and pull updated record
			System.out.println("Confirm changes? (yes/no)");
			String userType = scanner.nextLine().toLowerCase();
			if (userType.equals("yes")) {
				String sql1 = "UPDATE Distributors SET Balance = " + up_value + " WHERE DistAccountNum = " + sel_id
						+ ";";
				db.update(sql1);
				result = db.query(sql);
				if (db.commit()) {
					System.out.println("Updated records:");
					DBTablePrinter.printResultSet(result);
				}
			} else {
				// do nothing
				System.out.println("No changes made");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// enable auto-commit after transaction
			db.enableAutocommit();
		}

	}

	/**
	 * Place publication order API. Prompt user for order info, create a new
	 * database record in table Orders.
	 */
	public void placeOrder() {
		try {

			// start transaction
			db.disableAutocommit();

			// Get the highest ID from the Orders table
			int maxID = 0;
			result = db.query("select max(OrderID) as MaxID from Orders;");
			if (db.commit()) {
				if (result.next()) {
					maxID = result.getInt("MaxID");
				}
			}
			// user prompt
			System.out.println("\nAdd New Order");
			System.out.println("Enter New Order ID (current max ID is " + maxID + ", use 0 for auto increment):");
			String newID_str = scanner.nextLine();
			int newID = Integer.parseInt(newID_str);
			if (newID == 0) {
				newID_str = "NULL";
			}
			if (newID <= maxID) {
				newID = maxID + 1;
				newID_str = Integer.toString(newID);
			}
			System.out.println("Enter Distributor Account:");
			String d_id = scanner.nextLine();
			System.out.println("Enter Publication ID:");
			String pub_ID = scanner.nextLine();
			System.out.println("Enter Number of Copies:");
			String num_copies = scanner.nextLine();
			System.out.println("Enter Order Date (year-month-date) or leave blank for today's date:");
			String order_date = scanner.nextLine();
			if (order_date.isEmpty())
				order_date = java.time.LocalDate.now().toString();
			order_date = "'" + order_date + "'";
			System.out.println("Enter Production Date (year-month-date):");
			String prod_date = "'" + scanner.nextLine() + "'";
			System.out.println("Enter Price");
			String order_price = scanner.nextLine();
			System.out.println("Enter Shipping Cost:");
			String ship_cost = scanner.nextLine();
			float total_cost = Integer.parseInt(num_copies) * Float.parseFloat(order_price)
					+ Float.parseFloat(ship_cost);
			String total_cost_str = String.valueOf(total_cost);
			// update string
			String sql = "INSERT INTO Orders VALUES (" + newID_str + "," + d_id + "," + pub_ID + "," + num_copies + ","
					+ order_date + "," + prod_date + "," + order_price + "," + ship_cost + "," + total_cost_str + ");";
			// System.out.println(sql);
			// update database and commit
			db.update(sql);
			result = db.query("SELECT * FROM Orders WHERE OrderID =" + newID + ";");
			if (db.commit()) {
				System.out.println("\nSuccessfully Added Following Record");
				DBTablePrinter.printResultSet(result);
				System.out.println();
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			// enable auto-commit after transaction
			db.enableAutocommit();
		}
	}

	/**
	 * Bill distributor (create new invocie) API. Prompt user for new invoice info,
	 * create a new database record in table Invoices the invoice start date is the
	 * first of the entered month (more or equal to) end date - the first of the next month (less)
	 */
	public void newInvoice() {
		try {

			// start transaction
			db.disableAutocommit();

			// Get the highest ID from the Invoice table
			int maxID = 0;
			result = db.query("select max(InvoiceID) as MaxID from Invoices;");
			if (db.commit()) {
				if (result.next()) {
					maxID = result.getInt("MaxID");
					System.out.println(maxID);
				}
			}
			// user prompt
			System.out.println("\nGenerate New Invoice");
			System.out.println("Enter New Invoice ID (current max ID is " + maxID + ", use 0 for auto increment):");
			String newID_str = scanner.nextLine();
			int newID = Integer.parseInt(newID_str);
			if (newID == 0) {
				newID_str = "NULL";
			}
			if (newID <= maxID) {
				newID = maxID + 1;
				newID_str = Integer.toString(newID);
			}

			System.out.println("Enter Distributor Account:");
			String d_id = scanner.nextLine();
			result = db.query("SELECT * FROM Orders WHERE DistAccountNum =" + d_id + ";");
			if (db.commit()) {
				System.out.println("Distributor Orders");
				DBTablePrinter.printResultSet(result);
				// System.out.println();
			}
			System.out.println("Enter Invoice month and year (year-month):");
			String start_date = "'" + scanner.nextLine() + "-01'";
			String[] split_date = start_date.split("-");
			int month_int = Integer.parseInt(split_date[1]);
			String end_date = split_date[0] + "-" + Integer.toString(month_int + 1) + "-" + split_date[2];

			// calculate total from Orders: NumCopies*Price + Shipping
			String sql5 = "(SELECT NumCopies, Price, ShippingCosts as ShC FROM Orders WHERE DistAccountNum = " + d_id
					+ " AND ProduceByDate >= " + start_date + " AND ProduceByDate < " + end_date + ");";
			result = db.query(sql5);
			float invoice_total = 0;
			if (db.commit()) {
				while (result.next()) {
					invoice_total = invoice_total + result.getInt("NumCopies") * result.getFloat("Price")
							+ result.getFloat("ShC");
				}
			}
			// update database and commit
			String sql = "INSERT INTO Invoices VALUES (" + newID_str + "," + d_id + "," + invoice_total + "," + end_date
					+ ", NULL);";
			// System.out.println(sql);
			db.update(sql);
			result = db.query("SELECT * FROM Invoices WHERE InvoiceID =" + newID + ";");
			if (db.commit()) {
				System.out.println("\nSuccessfully Added Following Record");
				DBTablePrinter.printResultSet(result);
				System.out.println();
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			// enable auto-commit after transaction
			db.enableAutocommit();
		}
	}

	/**
	 * Update invoice payment status API. Change payment date from NULL to user
	 * specified
	 */
	public void paymentInvoice() {
		try {

			// start transaction
			db.disableAutocommit();

			// user prompt
			System.out.println("\nUpdate Invoice Payment Status");
			System.out.println("Enter Invoice ID to update:");
			String inID = scanner.nextLine();
			System.out.println("Enter Invoice Payment Date (year-month-date):");
			String inDate = "'" + scanner.nextLine() + "'";
			String sql = "UPDATE Invoices SET PaymentDate =" + inDate + " WHERE InvoiceID = " + inID + ";";
			// update database and commit
			db.update(sql);
			result = db.query("SELECT * FROM Invoices WHERE InvoiceID =" + inID + ";");
			if (db.commit()) {
				System.out.println("\nSuccessfully Updated Following Record");
				DBTablePrinter.printResultSet(result);
				System.out.println();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// enable auto-commit after transaction
			db.enableAutocommit();
		}

	}

	private static void helper() {

		System.out.println("\nCommand Code | Command Description             | Arguments it needs");
		System.out.println("-------------|---------------------------------|-------------------");

		String[][] help = { { "  D1         | print distributor               | ", "Distributor ID" },
				{ "  D2         | add distributor                 | ",
						"Distributor ID, Name, Type, Street Address, City Address, Phone, Contact, Balance" },
				{ "  D3         | delete distributor              | ", "Distributor ID" },
				{ "  D4         | update distributor              | ",
						"Selection Attribute/Value, Update Attribute/Value" },
				{ "  D5         | update distributor balance      | ", "Distributor ID" },
				{ "  D6         | place publication order         | ",
						"Order ID, Dist ID, Pub ID, #Copies, Order Date, Production Date, Price, Shipping" },
				{ "  D7         | bill distributor (new invoice)  | ", "Distributor ID, invoice year-month" },
				{ "  D8         | update invoice payment status   | ", "Invoice ID, payment date" } };

		for (int i = 0; i < help.length; i++) {
			System.out.println(help[i][0] + help[i][1]);
		}
		System.out.println();
	}

	public void command(String com) {
		System.out.println("Your Command: " + com + "\n");

		switch (com.toLowerCase()) {

		case "d2":
			addDistributor();
			break;

		case "d3":
			delDistributor();
			break;

		case "d1":
			printDistributor();
			break;

		case "d4":
			updateDistributor();
			break;

		case "d5":
			balanceDistributor();
			break;

		case "d6":
			placeOrder();
			break;

		case "d7":
			newInvoice();
			break;

		case "d8":
			paymentInvoice();
			break;

		default:
			System.out.println("Here are the Valid Command Codes, and their required information");
			helper();
			break;
		}

	}

}
