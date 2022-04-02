import java.sql.*;
import java.util.Scanner;

public class Production {

	private DBManager db = null;
	private ResultSet result = null;

	private Scanner scanner = null;

	public Production(DBManager dbM, Scanner s) {
		db = dbM;
		scanner = s;

	}

	public void createBook() {
		try {
			int pubID;
			String title;
			String type;
			String topic;
			String editionNum;
			String isbn;
			String pubDate;

			System.out.println("Creating book...");
//			System.out.print("Enter a publication ID: ");
//			pubID = scanner.nextInt(); scanner.nextLine();
			System.out.print("Enter book title: ");
			title = scanner.nextLine();
//			System.out.print("Enter publication type (book, magazine, journal): ");
			type = "Book";
			System.out.print("Enter book topic: ");
			topic = scanner.nextLine();
			System.out.print("Enter book edition number: ");
			editionNum = scanner.nextLine();
			System.out.print("Enter ISBN: ");
			isbn = scanner.nextLine();
			System.out.print("Enter publication date (YYYY-MM-DD): ");
			pubDate = scanner.nextLine();

			String sql1 = String.format("insert into Publication values(NULL, '%s', '%s', '%s');", title, type, topic);
			db.update(sql1);
			result = db.query("select last_insert_id();");
			result.next();
			pubID = result.getInt(1);

			String sql2 = String.format("insert into Books values(%d, '%s', '%s', '%s');", pubID, editionNum, isbn, pubDate);

			db.update(sql2);

//			TODO: collect and add author information

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void editBook() {
		try {
			int pubID;
			String newTitle;
//			String newType;
			String newTopic;
			String newEditionNum;
			String newIsbn;
			String newPubDate;
			
			System.out.println("Editing book...");
			result = db.query("select * from Publication natural join Books;");
			if (db.commit()) {
				System.out.println("Book catalog:");
				DBTablePrinter.printResultSet(result);
			}
			System.out.print("Enter publication ID to edit: ");
			pubID = scanner.nextInt(); scanner.nextLine();
			
			String sql1 = String.format("select * from Publication natural join Books where PublicationID = %d;", pubID);
			String sql2 = String.format("select EmpID as AuthorID, Name from WritesBook natural join Employees where PublicationID = %d;", pubID);
			
			result = db.query(sql1);
			ResultSet result2 = db.query(sql2);
			
			if (db.commit()) {
				System.out.printf("Current information for PublicationID: %d\n", pubID);
				DBTablePrinter.printResultSet(result);
				DBTablePrinter.printResultSet(result2);
			}
			
			System.out.print("Enter new publication ID (or 0 to keep current): ");
			int newPubID = scanner.nextInt(); scanner.nextLine();
			System.out.print("Enter new title (or blank to keep current): ");
			newTitle = scanner.nextLine();
			System.out.print("Enter new topic (or blank to keep current): ");
			newTopic = scanner.nextLine();
			System.out.print("Enter new edition number (or blank to keep current): ");
			newEditionNum = scanner.nextLine();
			System.out.print("Enter new ISBN (or blank to keep current): ");
			newIsbn = scanner.nextLine();
			System.out.print("Enter new publication date in YYYY-MM-DD format (or blank to keep current): ");
			newPubDate = scanner.nextLine();
			
			String sql3 = "";
			if (newPubID != 0)
				sql3 += "PublicationID = " + newPubID;
			if (!newTitle.isEmpty())
				sql3 += String.format("%sTitle = '%s'", sql3.isEmpty() ? "" : ", ", newTitle);
			if (!newTopic.isEmpty())
				sql3 += String.format("%sTopic = '%s'", sql3.isEmpty() ? "" : ", ", newTopic);
			
			String sql5 = "";
			if (!newEditionNum.isEmpty())
				sql5 += "EditionNumber = " + newEditionNum;
			if (!newIsbn.isEmpty())
				sql5 += String.format("%sISBN = '%s'", sql5.isEmpty() ? "" : ", ", newIsbn);
			if (!newPubDate.isEmpty())
				sql5 += String.format("%sPublicationDate = '%s'", sql5.isEmpty() ? "" : ", ", newPubDate);
			
			if (!sql5.isEmpty()) {
				String sql6 = String.format("update Books set %s where PublicationID = %d;", sql5, pubID);
				db.update(sql6);
			}
			
			if (!sql3.isEmpty()) {
				String sql4 = String.format("update Publication set %s where PublicationID = %d;", sql3, pubID);
				db.update(sql4);
			}
			
//			TODO: Allow changing author information
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	
	public void deleteBook() {
		try {
			int pubID;
			
			result = db.query("select * from Publication natural join Books;");
			if (db.commit()) {
				System.out.println("Book catalog:");
				DBTablePrinter.printResultSet(result);
			}
			System.out.print("Enter publication ID to delete: ");
			pubID = scanner.nextInt(); scanner.nextLine();
			
			String sql = String.format("delete from Publication where PublicationID = %d;", pubID);
			db.update(sql);
			
			
		} catch (Exception e) {
			e.printStackTrace();

		} 
		
	}
	
	public void addChapter() {
		try {
			int pubID;
			String chTitle;
			
			System.out.println("Adding chapter...");
			
			result = db.query("select * from Publication natural join Books;");
			if (db.commit()) {
				System.out.println("Book catalog:");
				DBTablePrinter.printResultSet(result);
			}
			System.out.print("Enter publication ID: ");
			pubID = scanner.nextInt(); scanner.nextLine();
			
			System.out.print("Enter title of new chapter: ");
			chTitle = scanner.nextLine();
			
			String sql = String.format("insert into Chapters values(%d, '%s');", pubID, chTitle);
			db.update(sql);
			
		} catch (Exception e) {
			e.printStackTrace();

		} 
		
	}
	
	public void editChapter() {
		try {
			int pubID;
			String chTitle;
			String newChTitle;
			
			System.out.print("Enter publication ID: ");
			pubID = scanner.nextInt(); scanner.nextLine();
			
			System.out.print("Enter the chapter title you want to edit: ");
			chTitle = scanner.nextLine();
			
			System.out.print("Enter the new chapter title: ");
			newChTitle = scanner.nextLine();
			
			String sql = String.format("update Chapters set ChapterTitle = '%s' where PublicationID = %d and ChapterTitle = '%s';", newChTitle, pubID, chTitle);
			db.update(sql);
			
		} catch (Exception e) {
			e.printStackTrace();

		} 
		
	}
	
	public void createIssue() {
		try {
			int pubID;
			String title;
			String type;
			String topic;
			String issueTitle;
			String issueDate;
			
			System.out.println("Creating issue...");
//			System.out.print("Enter a publication ID: ");
//			pubID = scanner.nextInt(); scanner.nextLine();
			System.out.print("Enter periodical title: ");
			title = scanner.nextLine();
			System.out.print("Enter periodical type (Magazine or Journal): ");
			type = scanner.nextLine();
			System.out.print("Enter issue topic: ");
			topic = scanner.nextLine();
			System.out.print("Enter issue title: ");
			issueTitle = scanner.nextLine();
			System.out.print("Enter issue date (YYYY-MM-DD): ");
			issueDate = scanner.nextLine();
			
			String sql1 = String.format("insert into Publication values(NULL, '%s', '%s', '%s');", title, type, topic);
			db.update(sql1);
			result = db.query("select last_insert_id();");
			result.next();
			pubID = result.getInt(1);

			String sql2 = String.format("insert into Issues values(%d, '%s', '%s');", pubID, issueTitle, issueDate);

			db.update(sql2);
			
			
		} catch (Exception e) {
			e.printStackTrace();

		} 
		
	}
	
	public void editIssue() {
		try {
			int pubID;
			String newTitle;
			String newType;
			String newTopic;
			String newIssueTitle;
			String newIssueDate;
			
			System.out.println("Editing issue...");
			result = db.query("select * from Publication natural join Issues;");
			if (db.commit()) {
				System.out.println("Issue catalog:");
				DBTablePrinter.printResultSet(result);
			}
			System.out.print("Enter publication ID to edit: ");
			pubID = scanner.nextInt(); scanner.nextLine();
			
			String sql1 = String.format("select * from Publication natural join Issues where PublicationID = %d;", pubID);
			
			result = db.query(sql1);
			if (db.commit()) {
				System.out.printf("Current information for PublicationID: %d\n", pubID);
				DBTablePrinter.printResultSet(result);
			}
			
			System.out.print("Enter new publication ID (or 0 to keep current): ");
			int newPubID = scanner.nextInt(); scanner.nextLine();
			System.out.print("Enter new periodical title (or blank to keep current): ");
			newTitle = scanner.nextLine();
			System.out.print("Enter new periodical type (or blank to keep current): ");
			newType = scanner.nextLine();
			System.out.print("Enter new topic (or blank to keep current): ");
			newTopic = scanner.nextLine();
			System.out.print("Enter new issue title (or blank to keep current): ");
			newIssueTitle = scanner.nextLine();
			System.out.print("Enter new issue date in YYYY-MM-DD format (or blank to keep current): ");
			newIssueDate = scanner.nextLine();
			
			String sql3 = "";
			if (newPubID != 0)
				sql3 += "PublicationID = " + newPubID;
			if (!newTitle.isEmpty())
				sql3 += String.format("%sTitle = '%s'", sql3.isEmpty() ? "" : ", ", newTitle);
			if (!newType.isEmpty())
				sql3 += String.format("%sType = '%s'", sql3.isEmpty() ? "" : ", ", newType);
			if (!newTopic.isEmpty())
				sql3 += String.format("%sTopic = '%s'", sql3.isEmpty() ? "" : ", ", newTopic);
			
			String sql5 = "";
			if (!newIssueTitle.isEmpty())
				sql5 += String.format("IssueTitle = '%s'", newIssueTitle);
			if (!newIssueDate.isEmpty())
				sql5 += String.format("%sIssueDate = '%s'", sql5.isEmpty() ? "" : ", ", newIssueDate);
			
			if (!sql5.isEmpty()) {
				String sql6 = String.format("update Issues set %s where PublicationID = %d;", sql5, pubID);
				db.update(sql6);
			}
			
			if (!sql3.isEmpty()) {
				String sql4 = String.format("update Publication set %s where PublicationID = %d;", sql3, pubID);
				db.update(sql4);
			}
			
		} catch (Exception e) {
			e.printStackTrace();

		} 
		
	}
	
	public void deleteIssue() {
		try {
			int pubID;
			
			result = db.query("select * from Publication natural join Issues;");
			if (db.commit()) {
				System.out.println("Issue catalog:");
				DBTablePrinter.printResultSet(result);
			}
			System.out.print("Enter publication ID to delete: ");
			pubID = scanner.nextInt(); scanner.nextLine();
			
			String sql = String.format("delete from Publication where PublicationID = %d;", pubID);
			db.update(sql);
			
			
		} catch (Exception e) {
			e.printStackTrace();

		} 
		
	}
	
	public void addArticle() {
		try {
			int pubID;
			String articleTitle;
			String articleTopic;
			
			System.out.println("Adding article...");
			
			result = db.query("select * from Publication natural join Issues;");
			if (db.commit()) {
				System.out.println("Issue catalog:");
				DBTablePrinter.printResultSet(result);
			}
			System.out.print("Enter publication ID: ");
			pubID = scanner.nextInt(); scanner.nextLine();
			
			System.out.print("Enter article title: ");
			articleTitle = scanner.nextLine();
			System.out.print("Enter article topic: ");
			articleTopic = scanner.nextLine();
			
			String sql = String.format("insert into Articles values(%d, '%s', '%s', NULL);", pubID, articleTitle, articleTopic);
			db.update(sql);
			
//			TODO: deal with article authors
		} catch (Exception e) {
			e.printStackTrace();

		} 
		
	}
	
	public void editArticle() {
		try {
			int pubID;
			String articleTitle;
			String newArticleTitle;
			String newArticleTopic;
			
			System.out.print("Enter publication ID: ");
			pubID = scanner.nextInt(); scanner.nextLine();
			
			System.out.print("Enter the article title you want to edit: ");
			articleTitle = scanner.nextLine();
			
			System.out.print("Enter the new article title (or blank to keep current): ");
			newArticleTitle = scanner.nextLine();
			System.out.print("Enter the new article topic (or blank to keep current): ");
			newArticleTopic = scanner.nextLine();
			
			String sql1 = "";
			if (!newArticleTitle.isEmpty())
				sql1 += String.format("ArticleTitle = '%s'", newArticleTitle);
			if (!newArticleTopic.isEmpty())
				sql1 += String.format("%sArticleTopic = '%s'", sql1.isEmpty() ? "" : ", ", newArticleTopic);
			
			String sql2 = String.format("update Articles set %s where PublicationID = %d and ArticleTitle = '%s';", sql1, pubID, articleTitle);
			db.update(sql2);
			
		} catch (Exception e) {
			e.printStackTrace();

		} 
		
	}
	
	public void addArticleText() {
		try {
			int pubID;
			String articleTitle;
			String articleText;
			
			System.out.println("Adding article text...");
			System.out.print("Enter publication ID: ");
			pubID = scanner.nextInt(); scanner.nextLine();
			
			System.out.print("Enter the article title: ");
			articleTitle = scanner.nextLine();
			
			System.out.print("Enter article text filename: ");
			articleText = scanner.nextLine();
			
			String sql = String.format("update Articles set ArticleText = '%s' where PublicationID = %d and ArticleTitle = '%s';", articleText, pubID, articleTitle);
			db.update(sql);
			
		} catch (Exception e) {
			e.printStackTrace();

		} 
		
	}
	
	public void editArticleText() {
		try {
			int pubID;
			String articleTitle;
			String newArticleText;
			
			System.out.println("Adding article text...");
			System.out.print("Enter publication ID: ");
			pubID = scanner.nextInt(); scanner.nextLine();
			
			System.out.print("Enter the article title: ");
			articleTitle = scanner.nextLine();
			
			System.out.print("Enter the new article text filename: ");
			newArticleText = scanner.nextLine();
			
			String sql = String.format("update Articles set ArticleText = '%s' where PublicationID = %d and ArticleTitle = '%s';", newArticleText, pubID, articleTitle);
			db.update(sql);
			
		} catch (Exception e) {
			e.printStackTrace();

		} 
		
	}

	private static void helper() {

		System.out.println("\nCommand Code | Command Description                                 | Arguments it needs");
		System.out.println("-------------|-----------------------------------------------------|-------------------");

		String[][] help = { { "  P1         | create book                                         | ", "" },
		         { "  P2         | edit book                                           | ", "" },
		         { "  P3         | delete book                                         | ", "" },
		         { "  P4         | add book chapter                                    | ", "" },
		         { "  P5         | edit book chapter                                   | ", "" },
		         { "  P6         | create periodical issue                             | ", "" },
		         { "  P7         | edit periodical issue                               | ", "" },
		         { "  P8         | delete periodical issue                             | ", "" },
		         { "  P9         | add article to issue                                | ", "" },
		         { "  P10        | edit article in issue                               | ", "" },
		         { "  P11        | add article text                                    | ", "" },
		         { "  P12        | update article text                                 | ", "" }
		};

		for (int i = 0; i < 12; i++) {
			System.out.println(help[i][0] + help[i][1]);
		}
		System.out.println();
	}

	public void command(String com) {
		System.out.println("Your Command: " + com + "\n");

		switch (com.toLowerCase()) {

		case "p1":
			createBook();
			break;
			
		case "p2":
			editBook();
			break;
			
		case "p3":
			deleteBook();
			break;
			
		case "p4":
			addChapter();
			break;
			
		case "p5":
			editChapter();
			break;
			
		case "p6":
			createIssue();
			break;
			
		case "p7":
			editIssue();
			break;
			
		case "p8":
			deleteIssue();
			break;
			
		case "p9":
			addArticle();
			break;
			
		case "p10":
			editArticle();
			break;
			
		case "p11":
			addArticleText();
			break;
			
		case "p12":
			editArticleText();
			break;

		default:
			System.out.println("Here are the Valid Command Codes, and their required information");
			helper();
			break;
		}
	}
}
