import java.util.Scanner;

public class Publisher {
	private DBManager db = null;
	private Scanner scanner = null;

	private Publishing publisher = null;
	private Production production = null;
	private Distribution distributor = null;
	private Reports report = null;

	public Publisher(DBManager dbM, Scanner s) {
		db = dbM;
		scanner = s;

		publisher = new Publishing(db, scanner);
		production = new Production(db, scanner);
		distributor = new Distribution(db, scanner);
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
				{ "  P8         | delete author                   | ", "employee ID" },
				{ "  E4         | search book catalog             | ", "" },
				{ "  E5         | search article catalog          | ", "" },
				{ "  B3         | create book                     | ", "" },
				{ "  B4         | edit book                       | ", "" },
				{ "  B5         | delete book                     | ", "" },
				{ "  B6         | add book chapter                | ", "" },
				{ "  B7         | edit book chapter               | ", "" },
				{ "  I3         | create periodical issue         | ", "" },
				{ "  I4         | edit periodical issue           | ", "" },
				{ "  I5         | delete periodical issue         | ", "" },
				{ "  I6         | add article to issue            | ", "" },
				{ "  I7         | edit article in issue           | ", "" },
				{ "  I8         | add article text                | ", "" },
				{ "  I9         | update article text             | ", "" },
				{ "  D1         | print distributor                     | ", "Distributor ID" },
		        { "  D2         | add distributor                       | ", "Distributor ID, Name, Type, Street Address, City Address, Phone, Contact, Balance" },
		        { "  D3         | delete distributor                    | ", "Distributor ID" },
		        { "  D4         | update distributor        	     | ", "Selection Attribute/Value, Update Attribute/Value" },
		        { "  D5         | update distributor balance            | ", "Distributor ID" },
		        { "  D6         | place publication order               | ", "Order ID, Dist ID, Pub ID, #Copies, Production Date, Price, Shipping" },
		        { "  D7         | bill distributor (new invoice)        | ", "Distributor ID, invoice year-month" },
		        { "  D8         | update invoice payment status         | ", "Invoice ID, payment date" },
		        { "  R1         | generate report                                     | ", "Start Date, End Date" },
		        { "  R2         | total publications sold                             | ", "Publication ID, Start Date, End Date" },
		        { "  R3         | total publication revenue                           | ", "Start Date, End Date" },
		        { "  R4         | total expenses                                      | ", "Start Date, End Date" },
		        { "  R5         | total distributors                                  | ", "None" },
		        { "  R6         | total city revenue                                  | ", "City Name" },
		        { "  R7         | total distributor revenue                           | ", "Distributor Account Number" },
		        { "  R8         | total address revenue                               | ", "Address" },
		        { "  R9         | total payments dy time and type of employee         | ", "Start Date, End Date" },
		        { "  R10        | total payments by type of employee and work type    | ", "Start Date, End Date" },
		        { "  R11        | List of all Distributors                            | ", "None" },
		        { "  R12        | List of all Employees                               | ", "None" },
		        { "  D7         | bill distributor (new invoice)                      | ", "Distributor ID, invoice year-month" },
		        { "  D8         | update invoice payment status                       | ", "Invoice ID, payment date" },
		        { "  P15        | make employee payment                               | ", "" },
		        { "  P16        | update employee payment                             | ", "" },
		        { "  P17        | track employee payment                              | ", "" }
			};

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
