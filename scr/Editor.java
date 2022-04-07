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
		System.out.printf("\n%35s\n", "EDITOR MENU");
		System.out.println("\nCommand Code | Command Description             | Arguments it needs");
		System.out.println("-------------|---------------------------------|-------------------");

		String[][] help = { { "  E1         | menu for books                  | ", "" },
		         { "  E2         | menu for issues                 | ", "" },
		         { "  E3         | view editor responsibilities    | ", "employee ID" },
		         { "  E4         | search book catalog             | ", "" },
		         { "  E5         | search article catalog          | ", "" },
		         { "  E6         | add author                      | ", "employee ID, name, type" },
		         { "  E7         | update author                   | ", "employee ID, name, type, active" },
		         { "  E8         | delete author                   | ", "employee ID" },
		         { "  logout     | return to user selection        | ", "" }
		};

		for (int i = 0; i < help.length; i++) {
			System.out.println(help[i][0] + help[i][1]);
		}
		System.out.println();
	}

	public void command(String com) {
		System.out.println("Your Command: " + com + "\n");

		switch (com.toLowerCase()) {

		// editor menu commands
		case "e1":
			bookHelper();
			break;
			
		case "e2":
			issueHelper();
			break;
			
		case "e3":
		case "b1":
		case "i1":
			publisher.viewEditorResponsibilities();
			break;
			
		case "e4":
		case "b2":
			production.getBookInfo();
			break;
			
		case "e5":
		case "i2":
			production.getArticleInfo();
			break;
			
		case "e6":
		case "b8":
		case "i10":
			publisher.addAuthor();
			break;

		case "e7":
		case "b9":
		case "i11":
			publisher.updateAuthor();
			break;

		case "e8":
		case "b10":
		case "i12":
			publisher.deleteAuthor();
			break;
			
		// book menu commands
		case "b3": 
			production.createBook();
			break;
			
		case "b4":
			production.editBook();
			break;
			
		case "b5":
			production.deleteBook();
			break;
			
		case "b6":
			production.addChapter();
			break;
			
		case "b7":
			production.editChapter();
			break;
			
		// issue menu commands
		case "i3": 
			production.createIssue();
			break;
			
		case "i4":
			production.editIssue();
			break;
			
		case "i5":
			production.deleteIssue();
			break;
			
		case "i6":
			production.addArticle();
			break;
			
		case "i7":
			production.editArticle();
			break;
			
		case "i8":
			production.addArticleText();
			break;
			
		case "i9":
			production.editArticleText();
			break;
			
		default:
			System.out.println("Here are the Valid Command Codes, and their required information");
			helper();
			break;
		}
	}
	
	private static void bookHelper() {
		System.out.printf("\n%35s\n", "BOOK MENU");
		System.out.println("\nCommand Code | Command Description             | Arguments it needs");
		System.out.println("-------------|---------------------------------|-------------------");

		String[][] help = { { "  B1         | view editor responsibilities    | ", "employee ID" },
				 { "  B2         | search book catalog             | ", "" },
				 { "  B3         | create book                     | ", "" },
		         { "  B4         | edit book                       | ", "" },
		         { "  B5         | delete book                     | ", "" },
		         { "  B6         | add book chapter                | ", "" },
		         { "  B7         | edit book chapter               | ", "" },
		         { "  B8         | add author                      | ", "employee ID, name, type" },
		         { "  B9         | update author                   | ", "employee ID, name, type, active" },
		         { "  B10        | delete author                   | ", "employee ID" },
		         { "  back       | return to editor menu           | ", "" }
		};

		for (int i = 0; i < help.length; i++) {
			System.out.println(help[i][0] + help[i][1]);
		}
		System.out.println();
	}
	
	private static void issueHelper() {
		System.out.printf("\n%35s\n", "ISSUE MENU");
		System.out.println("\nCommand Code | Command Description             | Arguments it needs");
		System.out.println("-------------|---------------------------------|-------------------");

		String[][] help = { { "  I1         | view editor responsibilities    | ", "employee ID" },
				 { "  I2         | search article catalog          | ", "" },
				 { "  I3         | create periodical issue         | ", "" },
		         { "  I4         | edit periodical issue           | ", "" },
		         { "  I5         | delete periodical issue         | ", "" },
		         { "  I6         | add article to issue            | ", "" },
		         { "  I7         | edit article in issue           | ", "" },
		         { "  I8         | add article text                | ", "" },
		         { "  I9         | update article text             | ", "" },
		         { "  I10        | add author                      | ", "employee ID, name, type" },
		         { "  I11        | update author                   | ", "employee ID, name, type, active" },
		         { "  I12        | delete author                   | ", "employee ID" },
		         { "  back       | return to editor menu           | ", "" }
		};

		for (int i = 0; i < help.length; i++) {
			System.out.println(help[i][0] + help[i][1]);
		}
		System.out.println();
	}
	
}
