import java.util.Scanner;

public class Publisher {
	private DBManager db = null;
	private Scanner scanner = null;

	private Publishers publisher = null;
	private Production production = null;
	private Distributors distributor = null;
	private Reports report = null;

	public Publisher(DBManager dbM, Scanner s) {
		db = dbM;
		scanner = s;

		publisher = new Publishers(db, scanner);
		production = new Production(db, scanner);
		distributor = new Distributors(db, scanner);
		report = new Reports(db, scanner);
	}

	private static void helper() {

		System.out.println("\nCommand Code | Command Description             | Arguments it needs");
		System.out.println("-------------|---------------------------------|-------------------------------");

		String[][] help = { { "  P1         | assign editor to publication    | ", "employee ID, publication ID" },
				{ "  P2         | view editor responsibilities    | ", "employee ID" },
				{ "  P3         | add editor                      | ", "name, type" },
				{ "  P4         | update editor                   | ", "employee ID, name, type, active" },
				{ "  P5         | delete editor                   | ", "employee ID" },
				{ "  P6         | add author                      | ", "employee ID, name, type" },
				{ "  P7         | update author                   | ", "employee ID, name, type, active" },
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
			publisher.assignEditorToPublication();
			break;

		case "p2":
			publisher.viewEditorResponsibilities();
			break;

		case "p3":
			publisher.addEditor();
			break;

		case "p4":
			publisher.updateEditor();
			break;

		case "p5":
			publisher.deleteEditor();
			break;

		case "p6":
			publisher.addAuthor();
			break;

		case "p7":
			publisher.updateAuthor();
			break;

		case "p8":
			publisher.deleteAuthor();
			break;

		default:
			System.out.println("Here are the Valid Command Codes, and their required information");
			helper();
			break;
		}

	}
}
