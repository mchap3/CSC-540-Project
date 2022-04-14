import java.util.Scanner;

/**
 * User profile for Editors with menus and commands for operations on
 * publications.
 *
 * @author Morgan Chapman
 */
public class Editor {

	private DBManager db = null;
	private Scanner scanner = null;

	private Production production = null;
	private Publishing publish = null;

	private enum Menu {
		MAIN, BOOK, ISSUE
	}

	private Menu menu;

	/**
	 * Constructs editor object with production and publishing objects and menu flag
	 * 
	 * @param dbM DBManager object
	 * @param s   scanner object
	 */
	public Editor(DBManager dbM, Scanner s) {
		db = dbM;
		scanner = s;

		production = new Production(db, scanner);
		publish = new Publishing(db, scanner);

		menu = Menu.MAIN;
	}

	/**
	 * Prints main-menu-level command options available to Editor class
	 */
	private static void helper() {
		System.out.printf("%35s\n", "EDITOR MENU");
		System.out.println("\nCommand Code | Command Description             | Arguments it needs");
		System.out.println("-------------|---------------------------------|-------------------");

		String[][] help = { { "  E1         | menu for books                  | ", "" },
				{ "  E2         | menu for issues                 | ", "" },
				{ "  E3         | view editor responsibilities    | ", "employee ID" },
				{ "  E4         | search book catalog             | ", "author, publication date, topic" },
				{ "  E5         | search article catalog          | ", "author, issue date, topic" },
				{ "  E6         | add author                      | ", "employee ID, name, type" },
				{ "  E7         | update author                   | ", "employee ID, name, type" },
				{ "  E8         | delete author                   | ", "employee ID" },
				{ "  logout     | return to user selection        | ", "" } };

		for (int i = 0; i < help.length; i++) {
			System.out.println(help[i][0] + help[i][1]);
		}
		System.out.println();
	}

	/**
	 * Directs command string from user input to call the appropriate Production or
	 * Publishing API
	 * 
	 * @param com command string
	 */
	public void command(String com) {
		System.out.println("Your Command: " + com + "\n");

		switch (com.toLowerCase()) {

		// editor menu commands
		case "e1":
			menu = Menu.BOOK;
			bookHelper();
			break;

		case "e2":
			menu = Menu.ISSUE;
			issueHelper();
			break;

		case "e3":
		case "b1":
		case "i1":
			publish.viewEditorResponsibilities();
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
			publish.addAuthor();
			break;

		case "e7":
			publish.updateAuthor();
			break;

		case "e8":
			publish.deleteAuthor();
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

		case "b8":
			production.deleteChapter();
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
			production.deleteArticle();
			break;

		case "i9":
			production.addArticleText();
			break;

		case "i10":
			production.editArticleText();
			break;

		case "back":
			menu = Menu.MAIN;
			helper();
			break;

		default:
//			System.out.println("Here are the Valid Command Codes, and their required information");
			if (menu == Menu.BOOK)
				bookHelper();
			else if (menu == Menu.ISSUE)
				issueHelper();
			else
				helper();
			break;
		}
	}

	/**
	 * Prints book-menu command options available to Editor class
	 */
	private static void bookHelper() {
		System.out.printf("%35s\n", "BOOK MENU");
		System.out.println("\nCommand Code | Command Description             | Arguments it needs");
		System.out.println("-------------|---------------------------------|-------------------");

		String[][] help = { { "  B1         | view editor responsibilities    | ", "employee ID" },
				{ "  B2         | search book catalog             | ", "author, publication date, topic" },
				{ "  B3         | create book                     | ",
						"title, topic, edition, ISBN, publication date, author(s)" },
				{ "  B4         | edit book                       | ",
						"publication ID, title, topic, edition, ISBN, publication date, author(s)" },
				{ "  B5         | delete book                     | ", "publication ID" },
				{ "  B6         | add book chapter                | ", "publication ID, chapter title" },
				{ "  B7         | edit book chapter               | ", "publication ID, chapter title" },
				{ "  B8         | delete book chapter             | ", "publication ID, chapter title" },
				{ "  back       | return to editor menu           | ", "" } };

		for (int i = 0; i < help.length; i++) {
			System.out.println(help[i][0] + help[i][1]);
		}
		System.out.println();
	}

	/**
	 * Prints issue-menu command options available to Editor class
	 */
	private static void issueHelper() {
		System.out.printf("%35s\n", "ISSUE MENU");
		System.out.println("\nCommand Code | Command Description             | Arguments it needs");
		System.out.println("-------------|---------------------------------|-------------------");

		String[][] help = { { "  I1         | view editor responsibilities    | ", "employee ID" },
				{ "  I2         | search article catalog          | ", "author, issue date, topic" },
				{ "  I3         | create periodical issue         | ",
						"title, type, topic, issue title, issue date, periodicity" },
				{ "  I4         | edit periodical issue           | ",
						"publication ID, title, type, topic, issue title, issue date, periodicity" },
				{ "  I5         | delete periodical issue         | ", "publication ID" },
				{ "  I6         | add article to issue            | ",
						"publication ID, article title, article topic, author(s)" },
				{ "  I7         | edit article in issue           | ",
						"publication ID, article title, article topic, author(s)" },
				{ "  I8         | delete article in issue         | ", "publication ID, article title" },
				{ "  I9         | add article text                | ", "publication ID, article title, article text" },
				{ "  I10        | update article text             | ", "publication ID, article title, article text" },
				{ "  back       | return to editor menu           | ", "" } };

		for (int i = 0; i < help.length; i++) {
			System.out.println(help[i][0] + help[i][1]);
		}
		System.out.println();
	}

}
