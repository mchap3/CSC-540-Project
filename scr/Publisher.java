import java.util.Scanner;

/**
 * User profile for Publisher with menus and commands for operations on
 * publications, production, distribution, and financial menues.
 *
 * @author Andrew Abate
 */
public class Publisher {
	private DBManager db = null;
	private Scanner scanner = null;

	private Publishing publish = null;
	private Production production = null;
	private Distribution distribution = null;
	private Reports report = null;

	private enum Menu {
		MAIN, PUB, PROD, DIST, FIN
	}

	private Menu menu;

	/**
	 * Constructs Publisher object with production, publishing distribution, report
	 * objects and menu flag
	 * 
	 * @param dbM DBManager object
	 * @param s   scanner object
	 */
	public Publisher(DBManager dbM, Scanner s) {
		db = dbM;
		scanner = s;

		publish = new Publishing(db, scanner);
		production = new Production(db, scanner);
		distribution = new Distribution(db, scanner);
		report = new Reports(db, scanner);

		menu = Menu.MAIN;
	}

	/**
	 * Prints main-menu-level command options available to Publisher class
	 */
	private static void helper() {
		System.out.printf("%35s\n", "PUBLISHER MENU");
		System.out.println("\nCommand Code | Command Description             | Arguments it needs");
		System.out.println("-------------|---------------------------------|-------------------");

		String[][] help = { { "  M1         | publishing menu                 | ", "" },
				{ "  M2         | production/editing menu         | ", "" },
				{ "  M3         | distribution menu               | ", "" },
				{ "  M4         | financial menu                  | ", "" },
				{ "  logout     | return to user selection        | ", "" } };

		for (int i = 0; i < help.length; i++) {
			System.out.println(help[i][0] + help[i][1]);
		}
		System.out.println();
	}

	/**
	 * Directs command string from user input to call the appropriate production,
	 * publishing, distribution, and report APIs
	 * 
	 * @param com command string
	 */
	public void command(String com) {
		System.out.println("Your Command: " + com + "\n");

		switch (com.toLowerCase()) {

		case "m1":
			menu = Menu.PUB;
			pubHelper();
			break;

		case "m2":
			menu = Menu.PROD;
			prodHelper();
			break;

		case "m3":
			menu = Menu.DIST;
			distHelper();
			break;

		case "m4":
			menu = Menu.FIN;
			finHelper();
			break;

		case "p1":
			publish.assignEditorToPublication();
			break;

		case "p2":
			publish.viewEditorResponsibilities();
			break;

		case "p3":
			publish.addEditor();
			break;

		case "p4":
			publish.updateEditor();
			break;

		case "p5":
			publish.deleteEditor();
			break;

		case "p6":
			publish.addAuthor();
			break;

		case "p7":
			publish.updateAuthor();
			break;

		case "p8":
			publish.deleteAuthor();
			break;

		// book menu commands
		case "b1":
			production.getBookInfo();
			break;

		case "i1":
			production.getArticleInfo();
			break;

		case "b2":
			production.createBook();
			break;

		case "b3":
			production.editBook();
			break;

		case "b4":
			production.deleteBook();
			break;

		case "b5":
			production.addChapter();
			break;

		case "b6":
			production.editChapter();
			break;

		case "b7":
			production.deleteChapter();
			break;

		// issue menu commands
		case "i2":
			production.createIssue();
			break;

		case "i3":
			production.editIssue();
			break;

		case "i4":
			production.deleteIssue();
			break;

		case "i5":
			production.addArticle();
			break;

		case "i6":
			production.editArticle();
			break;

		case "i7":
			production.deleteArticle();
			break;

		case "i8":
			production.addArticleText();
			break;

		case "i9":
			production.editArticleText();
			break;

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
			distribution.newInvoice();
			break;

		case "d8":
			distribution.paymentInvoice();
			break;

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
			production.makePayment();
			break;

		case "f14":
			production.updatePayment();
			break;

		case "f15":
			production.trackPayment();
			break;

		case "back":
			menu = Menu.MAIN;
			helper();
			break;

		default:
//			System.out.println("Here are the Valid Command Codes, and their required information");
			switch (menu) {
			case PUB:
				pubHelper();
				break;
			case PROD:
				prodHelper();
				break;
			case DIST:
				distHelper();
				break;
			case FIN:
				finHelper();
				break;
			default:
				helper();
				break;
			}
			break;
		}

	}

	/**
	 * Prints publisher-menu command options available to Publisher class
	 */
	private static void pubHelper() {
		System.out.printf("%36s\n", "PUBLISHING MENU");
		System.out.println("\nCommand Code | Command Description             | Arguments it needs");
		System.out.println("-------------|---------------------------------|-------------------");

		String[][] help = { { "  P1         | assign editor to publication    | ", "employee ID, publication ID" },
				{ "  P2         | view editor responsibilities    | ", "employee ID" },
				{ "  P3         | add editor                      | ", "name, type" },
				{ "  P4         | update editor                   | ", "employee ID, name, type" },
				{ "  P5         | delete editor                   | ", "employee ID" },
				{ "  P6         | add author                      | ", "employee ID, name, type" },
				{ "  P7         | update author                   | ", "employee ID, name, type" },
				{ "  P8         | delete author                   | ", "employee ID" },
				{ "  back       | return to publisher menu        | ", "" } };

		for (int i = 0; i < help.length; i++) {
			System.out.println(help[i][0] + help[i][1]);
		}
		System.out.println();
	}

	/**
	 * Prints publication-menu command options available to Publisher class
	 */
	private static void prodHelper() {
		System.out.printf("%36s\n", "PRODUCTION MENU");
		System.out.println("\nCommand Code | Command Description             | Arguments it needs");
		System.out.println("-------------|---------------------------------|-------------------");

		String[][] help = { { "  B1         | search book catalog             | ", "author, publication date, topic" },
				{ "  B2         | create book                     | ",
						"title, topic, edition, ISBN, publication date, author(s)" },
				{ "  B3         | edit book                       | ",
						"publication ID, title, topic, edition, ISBN, publication date, author(s)" },
				{ "  B4         | delete book                     | ", "publication ID" },
				{ "  B5         | add book chapter                | ", "publication ID, chapter title" },
				{ "  B6         | edit book chapter               | ", "publication ID, chapter title" },
				{ "  B7         | delete book chapter             | ", "publication ID, chapter title" },
				{ "-------------|---------------------------------|-------------------", "" },
				{ "  I1         | search article catalog          | ", "author, issue date, topic" },
				{ "  I2         | create periodical issue         | ",
						"title, type, topic, issue title, issue date, periodicity" },
				{ "  I3         | edit periodical issue           | ",
						"publication ID, title, type, topic, issue title, issue date, periodicity" },
				{ "  I4         | delete periodical issue         | ", "publication ID" },
				{ "  I5         | add article to issue            | ",
						"publication ID, article title, article topic, author(s)" },
				{ "  I6         | edit article in issue           | ",
						"publication ID, article title, article topic, author(s)" },
				{ "  I7         | delete article in issue         | ", "publication ID, article title" },
				{ "  I8         | add article text                | ", "publication ID, article title, article text" },
				{ "  I9         | update article text             | ", "publication ID, article title, article text" },
				{ "  back       | return to publisher menu        | ", "" } };

		for (int i = 0; i < help.length; i++) {
			System.out.println(help[i][0] + help[i][1]);
		}
		System.out.println();
	}

	/**
	 * Prints distributor-menu command options available to Publisher class
	 */
	private static void distHelper() {
		System.out.printf("%36s\n", "DISTRIBUTION MENU");
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
				{ "  D7         | bill distributor (new invoice)  | ", "Distributor ID, invoice year-month" },
				{ "  D8         | update invoice payment status   | ", "Invoice ID, payment date" },
				{ "  back       | return to publisher menu        | ", "" } };

		for (int i = 0; i < help.length; i++) {
			System.out.println(help[i][0] + help[i][1]);
		}
		System.out.println();
	}

	/**
	 * Prints report-menu command options available to Publisher class
	 */
	private static void finHelper() {
		System.out.printf("%44s\n", "FINANCIAL MENU");
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
				{ "  F13        | make employee payment                            | ",
						"employee ID, amount, payment date" },
				{ "  F14        | update employee payment                          | ", "check number, claim date" },
				{ "  F15        | track employee payment                           | ", "check number" },
				{ "  back       | return to publisher menu                         | ", "" } };

		for (int i = 0; i < help.length; i++) {
			System.out.println(help[i][0] + help[i][1]);
		}
		System.out.println();
	}
}
