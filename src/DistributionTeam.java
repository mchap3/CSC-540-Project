import java.util.Scanner;

/**
 * User profile for Distribution Team with menus and commands for operations on
 * distributor info.
 *
 * @author Ilya Arakelyan
 */
public class DistributionTeam {

	private DBManager db = null;
	private Scanner scanner = null;
	private Distribution distribution = null;
	private Production production = null;

	/**
	 * Constructs DistribtuionTeam object with distribution object and menu flag
	 * 
	 * @param dbM DBManager object
	 * @param s   scanner object
	 */
	public DistributionTeam(DBManager dbM, Scanner s) {
		db = dbM;
		scanner = s;

		distribution = new Distribution(db, scanner);
		production = new Production(db, scanner);
	}

	/**
	 * print menu for Distributor APIs prompt command input
	 */
	private static void helper() {
		System.out.printf("%40s\n", "DISTRIBUTION TEAM MENU");
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
						"Order ID, Dist ID, Pub ID, #Copies, Production Date, Price, Shipping" },
				{ "  D7         | print book catalog              | ", "" },
				{ "  D8         | print issue catalog             | ", "" },
				{ "  logout     | return to user selection        | ", "" } };

		for (int i = 0; i < help.length; i++) {
			System.out.println(help[i][0] + help[i][1]);
		}
		System.out.println();
	}

	/**
	 * Directs command string from user input to call the appropriate Distributor
	 * API
	 * 
	 * @param com command string
	 */
	public void command(String com) {
		/**
		 * call API based on user prompt
		 */
		System.out.println("Your Command: " + com + "\n");

		switch (com.toLowerCase()) {

		case "d1":
			distribution.printDistributor();
			break;

		case "d2":
			distribution.addDistributor();
			break;

		case "d3":
			distribution.delDistributor();
			break;

		case "d4":
			distribution.updateDistributor();
			break;

		case "d5":
			distribution.balanceDistributor();
			break;

		case "d6":
			distribution.placeOrder();
			break;
			
		case "d7":
			production.printBookCatalog();
			break;
			
		case "d8":
			production.printIssueCatalog();
			break;

		default:
//			System.out.println("Here are the Valid Command Codes, and their required information");
			helper();
			break;
		}

	}
}
