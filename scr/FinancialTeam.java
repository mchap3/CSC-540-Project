import java.util.Scanner;

/**
 * Financial team User profile contains commands useful for financial team
 * 
 * @author Chaitanya Patel
 *
 */
public class FinancialTeam {

	/** Database manager object */
	private DBManager db = null;
	/** Scanner object for reading input */
	private Scanner scanner = null;

	// private Publishers publisher = null;

	/** Production object */
	private Production production = null;
	/** Distribution object */
	private Distribution distribution = null;
	/** Reports object */
	private Reports report = null;

	/**
	 * constructor, creates the production, distribbution report objects
	 * 
	 * @param dbM database
	 * @param s   scanner
	 */
	public FinancialTeam(DBManager dbM, Scanner s) {
		db = dbM;
		scanner = s;

//		publisher = new Publishers(db, scanner);
		production = new Production(db, scanner);
		distribution = new Distribution(db, scanner);
		report = new Reports(db, scanner);
	}

	/**
	 * helper prints all commands that the financial team can use
	 */
	private static void helper() {
		System.out.printf("%49s\n", "FINANCIAL TEAM MENU");
		System.out.println("\nCommand Code | Command Description                              | Arguments it needs");
		System.out.println("-------------|--------------------------------------------------|-------------------");

		String[][] help = {
				{ "  F1         | generate report                                  | ", "Start Date, End Date" },
				{ "  F2         | total publications sold                          | ",
						"Publication ID, Start Date, End Date" },
				{ "  F3         | total publication revenue                        | ", "Start Date, End Date" },
				{ "  F4         | total expenses                                   | ", "Start Date, End Date" },
				{ "  F5         | total distributors                               | ", "None" },
				{ "  F6         | total city revenue                               | ", "City Name" },
				{ "  F7         | total distributor revenue                        | ", "Distributor Account Number" },
				{ "  F8         | total address revenue                            | ", "Address" },
				{ "  F9         | total payments by time and type of employee      | ", "Start Date, End Date" },
				{ "  F10        | total payments by type of employee and work type | ", "Start Date, End Date" },
				{ "  F11        | List of all Distributors                         | ", "None" },
				{ "  F12        | List of all Employees                            | ", "None" },
				{ "  F13        | bill distributor (new invoice)                   | ",
						"Distributor ID, invoice year-month" },
				{ "  F14        | update invoice payment status                    | ", "Invoice ID, payment date" },
				{ "  F15        | make employee payment                            | ",
						"employee ID, amount, payment date" },
				{ "  F16        | update employee payment                          | ", "check number, claim date" },
				{ "  F17        | track employee payment                           | ", "check number" },
				{ "  F18        | print book catalog                               | ", "" },
				{ "  F19        | print issue catalog                              | ", "" },
				{ "  logout     | return to user selection                         | ", "" } };

		for (int i = 0; i < help.length; i++) {
			System.out.println(help[i][0] + help[i][1]);
		}
		System.out.println();
	}

	/**
	 * sends commands to its objects
	 * 
	 * @param com command string
	 */
	public void command(String com) {
		System.out.println("Your Command: " + com + "\n");

		switch (com.toLowerCase()) {

		case "f1":
			report.generateReport();
			break;

		case "f2":
			report.totalPublicationsSold();
			break;

		case "f3":
			report.totalRevenue();
			break;

		case "f4":
			report.totalExpenses();
			break;

		case "f5":
			report.totalDistributors();
			break;

		case "f6":
			report.totalCityRevenue();
			break;

		case "f7":
			report.totalDistributorRevenue();
			break;

		case "f8":
			report.totalAddressRevenue();
			break;

		case "f9":
			report.totalPaymentsByTimeAndTypeofEmployee();
			break;

		case "f10":
			report.totalPaymentsByTypeOfEmployeeAndWorkType();
			break;

		case "f11":
			report.listOfAllDistributors();
			break;

		case "f12":
			report.listOfAllEmployees();
			break;

		case "f13":
			distribution.newInvoice();
			break;

		case "f14":
			distribution.paymentInvoice();
			break;

		case "f15":
			production.makePayment();
			break;

		case "f16":
			production.updatePayment();
			break;

		case "f17":
			production.trackPayment();
			break;
			
		case "f18":
			production.printBookCatalog();
			break;
			
		case "f19":
			production.printIssueCatalog();
			break;

		default:
//			System.out.println("Here are the Valid Command Codes, and their required information");
			helper();
			break;
		}

	}
}
