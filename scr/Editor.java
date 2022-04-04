import java.util.Scanner;

public class Editor {
	private DBManager db = null;
	private Scanner scanner = null;
	
	
	private Production production = null;
	private Publishers publisher = null;
	
	public Editor(DBManager dbM, Scanner s) {
		db = dbM;
		scanner = s;
		
		production = new Production(db, scanner);
		publisher = new Publishers(db, scanner);
	}

	
	private static void helper() {

		System.out.println("\nCommand Code | Command Description             | Arguments it needs");
		System.out.println("-------------|---------------------------------|-------------------");

		String[][] help = { { "  P1         | create book                     | ", "" },
		         { "  P2         | edit book                       | ", "" },
		         { "  P3         | delete book                     | ", "" },
		         { "  P4         | add book chapter                | ", "" },
		         { "  P5         | edit book chapter               | ", "" },
		         { "  P6         | create periodical issue         | ", "" },
		         { "  P7         | edit periodical issue           | ", "" },
		         { "  P8         | delete periodical issue         | ", "" },
		         { "  P9         | add article to issue            | ", "" },
		         { "  P10        | edit article in issue           | ", "" },
		         { "  P11        | add article text                | ", "" },
		         { "  P12        | update article text             | ", "" },
		         { "  P13        | search book catalog             | ", "" },
		         { "  P14        | search article catalog          | ", "" },
		         { "  P15        | view editor responsibilities    | ", "employee ID" },
		         { "  P16        | add author                      | ", "employee ID, name, type" },
		         { "  P17        | update author                   | ", "employee ID, name, type, active" },
		         { "  P18        | delete author                   | ", "employee ID" }
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
			production.createBook();
			break;
			
		case "p2":
			production.editBook();
			break;
			
		case "p3":
			production.deleteBook();
			break;
			
		case "p4":
			production.addChapter();
			break;
			
		case "p5":
			production.editChapter();
			break;
			
		case "p6":
			production.createIssue();
			break;
			
		case "p7":
			production.editIssue();
			break;
			
		case "p8":
			production.deleteIssue();
			break;
			
		case "p9":
			production.addArticle();
			break;
			
		case "p10":
			production.editArticle();
			break;
			
		case "p11":
			production.addArticleText();
			break;
			
		case "p12":
			production.editArticleText();
			break;
			
		case "p13":
			production.getBookInfo();
			break;
			
		case "p14":
			production.getArticleInfo();
			break;
			
		case "p15":
			publisher.viewEditorResponsibilities();
			break;
			
		case "p16":
			publisher.addAuthor();
			break;

		case "p17":
			publisher.updateAuthor();
			break;

		case "p18":
			publisher.deleteAuthor();
			break;

		default:
			System.out.println("Here are the Valid Command Codes, and their required information");
			helper();
			break;
		}
	}
	
}
