import java.sql.*;
import java.util.Scanner;

//javac database_setup.java DBManager.java DBTablePrinter.java User.java Reports.java Distribution.java Production.java Publishing.java Publisher.java DistributionTeam.java Editor.java FinancialTeam.java
//java database_setup
/**
 * Production API class containing all methods for Production/Editing related
 * operations.
 *
 * @author Morgan Chapman
 */
public class Production {

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
	public Production(DBManager dbM, Scanner s) {
		db = dbM;
		scanner = s;
	}

	/**
	 * Creates a new book and inserts into database. User inputs book information.
	 */
	public void createBook() {
		try {
			System.out.println("CREATING BOOK...");

			// start transaction
			db.disableAutocommit();

			// user input for book attributes
			System.out.print("Enter book title: ");
			String title = scanner.nextLine();
			String type = "Book";
			System.out.print("Enter book topic: ");
			String topic = scanner.nextLine();
			System.out.print("Enter book edition number: ");
			String editionNum = scanner.nextLine();
			System.out.print("Enter ISBN: ");
			String isbn = scanner.nextLine();
			System.out.print("Enter publication date (YYYY-MM-DD): ");
			String pubDate = scanner.nextLine();

			// insert into Publication table
			String sql1 = String.format("insert into Publication values(NULL, '%s', '%s', '%s');", title, type, topic);
			db.update(sql1);

			// get publication ID
			result = db.query("select last_insert_id();");
			result.next();
			int pubID = result.getInt(1);

			// insert into Books table
			String sql2 = String.format("insert into Books values(%d, '%s', '%s', '%s');", pubID, editionNum, isbn,
					pubDate);
			db.update(sql2);

			// add optional author information
			System.out.print("Add optional author information (y/n): ");
			if (scanner.nextLine().toLowerCase().startsWith("y")) {
				// display author list
				result = db.query("select * from Employees where EmpID in (select * from Authors);");
				System.out.println("Author List:");
				DBTablePrinter.printResultSet(result);

				// user input author information - multiple authors allowed
				while (true) {
					int empID;
					System.out.print("Enter Employee ID (enter \"new\" to add new author or leave blank to finish): ");
					String input = scanner.nextLine();
					if (input.isEmpty())
						break;
					if (input.toLowerCase().equals("new")) {
						Publishing pub = new Publishing(db, scanner);
						empID = pub.addAuthor();
					} else
						empID = Integer.valueOf(input);
					String sql3 = String.format("insert into WritesBook values(%d, %d);", pubID, empID);
					db.update(sql3);
				}
			}

			// commit transaction and print results
			String sql4 = String.format("select * from Publication natural join Books where PublicationID = %d;",
					pubID);
			String sql5 = String.format(
					"select Name as 'Author(s)' from WritesBook natural join Employees where PublicationID = %d;",
					pubID);
			result = db.query(sql4);
			ResultSet result2 = db.query(sql5);

			if (db.commit()) {
				System.out.println("\nSuccessfully Added Following Record:");
				DBTablePrinter.printResultSet(result);
				DBTablePrinter.printResultSet(result2);
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			// enable auto-commit after transaction
			db.enableAutocommit();
		}

	}

	/**
	 * Modifies a book present in the database. User inputs new values for
	 * attributes being modified.
	 */
	public void editBook() {
		try {
			System.out.println("EDITING BOOK...");

			// display book catalog
			printBookCatalog();

			// get pubID to edit
			System.out.print("Enter publication ID to edit: ");
			int pubID = scanner.nextInt();
			scanner.nextLine();

			// get book information
			String sql1 = String.format("select * from Publication natural join Books where PublicationID = %d;",
					pubID);
			String sql2 = String.format(
					"select EmpID as AuthorID, Name from WritesBook natural join Employees where PublicationID = %d;",
					pubID);

			result = db.query(sql1);
			ResultSet result2 = db.query(sql2);

			// display current book information
			System.out.printf("Current information for PublicationID: %d\n", pubID);
			DBTablePrinter.printResultSet(result);

			// prompt user to enter changes
			System.out.print("Enter new publication ID (or 0 to keep current): ");
			int newPubID = scanner.nextInt();
			scanner.nextLine();
			System.out.print("Enter new title (or blank to keep current): ");
			String newTitle = scanner.nextLine();
			System.out.print("Enter new topic (or blank to keep current): ");
			String newTopic = scanner.nextLine();
			System.out.print("Enter new edition number (or blank to keep current): ");
			String newEditionNum = scanner.nextLine();
			System.out.print("Enter new ISBN (or blank to keep current): ");
			String newIsbn = scanner.nextLine();
			System.out.print("Enter new publication date in YYYY-MM-DD format (or blank to keep current): ");
			String newPubDate = scanner.nextLine();

			// build UPDATE Publication SQL statement
			String sql3 = "";
			if (newPubID != 0)
				sql3 += "PublicationID = " + newPubID;
			if (!newTitle.isEmpty())
				sql3 += String.format("%sTitle = '%s'", sql3.isEmpty() ? "" : ", ", newTitle);
			if (!newTopic.isEmpty())
				sql3 += String.format("%sTopic = '%s'", sql3.isEmpty() ? "" : ", ", newTopic);

			// build UPDATE Books SQL statement
			String sql5 = "";
			if (!newEditionNum.isEmpty())
				sql5 += String.format("EditionNumber = '%s'", newEditionNum);
			if (!newIsbn.isEmpty())
				sql5 += String.format("%sISBN = '%s'", sql5.isEmpty() ? "" : ", ", newIsbn);
			if (!newPubDate.isEmpty())
				sql5 += String.format("%sPublicationDate = '%s'", sql5.isEmpty() ? "" : ", ", newPubDate);

			// start transaction
			db.disableAutocommit();

			// apply SQL statements
			if (!sql5.isEmpty()) {
				String sql6 = String.format("update Books set %s where PublicationID = %d;", sql5, pubID);
				db.update(sql6);
			}
			if (!sql3.isEmpty()) {
				String sql4 = String.format("update Publication set %s where PublicationID = %d;", sql3, pubID);
				db.update(sql4);
			}

			// display current author information
			DBTablePrinter.printResultSet(result2);

			// prompt user for authorship changes
			System.out.print("Edit author information (y/n)? ");
			if (scanner.nextLine().toLowerCase().startsWith("y")) {
				int empID;
				while (true) {
					System.out.print(
							"1. Remove Author\n" + "2. Add author\n" + "Choose an action (or blank to finish): ");
					String input = scanner.nextLine();
					if (input.isEmpty())
						// end input
						break;
					if (input.equals("1")) {
						// remove author
						System.out.print("Enter AuthorID to remove: ");
						empID = scanner.nextInt();
						scanner.nextLine();
						String sql7 = String.format("delete from WritesBook where PublicationID = %d and EmpID = %d;",
								newPubID == 0 ? pubID : newPubID, empID);
						db.update(sql7);
					} else if (input.equals("2")) {
						// display available authors to add
						result = db.query("select * from Employees where EmpID in (select * from Authors);");
						System.out.println("Author List:");
						DBTablePrinter.printResultSet(result);

						// user input author information - multiple authors allowed
						while (true) {
							System.out.print(
									"Enter Employee ID (enter \"new\" to add new author or leave blank to finish): ");
							input = scanner.nextLine();
							if (input.isEmpty())
								break;
							if (input.toLowerCase().equals("new")) {
								Publishing pub = new Publishing(db, scanner);
								empID = pub.addAuthor();
							} else
								empID = Integer.valueOf(input);
							String sql8 = String.format("insert into WritesBook values(%d, %d);",
									newPubID == 0 ? pubID : newPubID, empID);
							db.update(sql8);
						}

					}
				}

			}

			// commit transaction and display confirmation
			String sql9 = String.format("select * from Publication natural join Books where PublicationID = %d;",
					newPubID == 0 ? pubID : newPubID);
			String sql10 = String.format(
					"select Name as 'Author(s)' from WritesBook natural join Employees where PublicationID = %d;",
					newPubID == 0 ? pubID : newPubID);
			result = db.query(sql9);
			result2 = db.query(sql10);

			if (db.commit()) {
				System.out.println("\nSuccessfully Updated Following Record:");
				DBTablePrinter.printResultSet(result);
				DBTablePrinter.printResultSet(result2);
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			// enable auto-commit after transaction
			db.enableAutocommit();
		}

	}

	/**
	 * Removes a book from database
	 */
	public void deleteBook() {
		try {
			System.out.println("DELETING BOOK...");

			// display book catalog
			printBookCatalog();

			// get PublicationID
			System.out.print("Enter publication ID to delete: ");
			int pubID = scanner.nextInt();
			scanner.nextLine();

			// get book information
			String sql2 = String.format("select * from Publication natural join Books where PublicationID = %d;",
					pubID);
			result = db.query(sql2);

			// delete from DB
			String sql1 = String.format("delete from Publication where PublicationID = %d;", pubID);
			db.update(sql1);

			// display confirmation
			System.out.println("Successfully Removed Following Record:");
			DBTablePrinter.printResultSet(result);

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	/**
	 * Inserts new chapter for a book into database. User inputs book and chapter
	 * information.
	 */
	public void addChapter() {
		try {
			System.out.println("ADDING CHAPTER...");

			// display book catalog
			printBookCatalog();

			// get Publication ID from user
			System.out.print("Enter publication ID: ");
			int pubID = scanner.nextInt();
			scanner.nextLine();

			// display current book chapter information
			String sql1 = String.format("select * from Chapters where PublicationID = %d;", pubID);
			result = db.query(sql1);
			DBTablePrinter.printResultSet(result);

			// get chapter title from user
			System.out.print("Enter title of new chapter: ");
			String chTitle = scanner.nextLine();

			// INSERT into Chapters
			String sql = String.format("insert into Chapters values(%d, '%s');", pubID, chTitle);
			db.update(sql);

			// confirmation
			System.out.println("Successfully Added Following Record:");
			String sql2 = String.format("select * from Chapters where PublicationID = %d and ChapterTitle = '%s';",
					pubID, chTitle);
			result = db.query(sql2);
			DBTablePrinter.printResultSet(result);

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	/**
	 * Modifies a book chapter in the database. User inputs new chapter information
	 */
	public void editChapter() {
		try {
			System.out.println("UPDATING CHAPTER...");

			// display book catalog
			printBookCatalog();

			// get publication ID from user
			System.out.print("Enter publication ID: ");
			int pubID = scanner.nextInt();
			scanner.nextLine();

			// display current book chapter information
			String sql1 = String.format("select * from Chapters where PublicationID = %d;", pubID);
			result = db.query(sql1);
			DBTablePrinter.printResultSet(result);

			// select which chapter to update
			System.out.print("Enter the chapter title you want to edit: ");
			String chTitle = scanner.nextLine();

			// get new chapter title
			System.out.print("Enter the new chapter title: ");
			String newChTitle = scanner.nextLine();

			// perform UPDATE Chapters
			String sql2 = String.format(
					"update Chapters set ChapterTitle = '%s' where PublicationID = %d and ChapterTitle = '%s';",
					newChTitle, pubID, chTitle);
			db.update(sql2);

			// confirmation
			System.out.println("Successfully Updated Following Record:");
			String sql3 = String.format("select * from Chapters where PublicationID = %d and ChapterTitle = '%s';",
					pubID, newChTitle);
			result = db.query(sql3);
			DBTablePrinter.printResultSet(result);

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	/**
	 * Removes a book chapter from database
	 */
	public void deleteChapter() {
		try {
			System.out.println("DELETING CHAPTER...");

			// display book catalog
			printBookCatalog();

			// get publication ID from user
			System.out.print("Enter publication ID: ");
			int pubID = scanner.nextInt();
			scanner.nextLine();

			// display current book chapter information
			String sql1 = String.format("select * from Chapters where PublicationID = %d;", pubID);
			result = db.query(sql1);
			DBTablePrinter.printResultSet(result);

			// select which chapter to delete
			System.out.print("Enter the chapter title you want to delete: ");
			String chTitle = scanner.nextLine();

			// get info and DELETE from Chapters
			String sql3 = String.format("select * from Chapters where PublicationID = %d and ChapterTitle = '%s';",
					pubID, chTitle);
			result = db.query(sql3);
			String sql2 = String.format("delete from Chapters where PublicationID = %d and ChapterTitle = '%s';", pubID,
					chTitle);
			db.update(sql2);

			// confirmation
			System.out.println("Successfully Removed Following Record:");
			DBTablePrinter.printResultSet(result);

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	/**
	 * Creates and inserts new Issue publication into database. User inputs all
	 * issue information
	 */
	public void createIssue() {
		try {
			// start transaction
			db.disableAutocommit();
			System.out.println("CREATING ISSUE...");

			// get issue information from user
			System.out.print("Enter periodical title: ");
			String title = scanner.nextLine();
			System.out.print("Enter periodical type (Magazine or Journal): ");
			String type = scanner.nextLine();
			System.out.print("Enter issue topic: ");
			String topic = scanner.nextLine();
			System.out.print("Enter issue title: ");
			String issueTitle = scanner.nextLine();
			System.out.print("Enter issue date (YYYY-MM-DD): ");
			String issueDate = scanner.nextLine();
			System.out.print("Enter issue periodicity (Weekly/Monthly): ");
			String periodicity = scanner.nextLine();

			// INSERT INTO Publication
			String sql1 = String.format("insert into Publication values(NULL, '%s', '%s', '%s');", title, type, topic);
			db.update(sql1);

			// get auto-generated PublicationID
			result = db.query("select last_insert_id();");
			result.next();
			int pubID = result.getInt(1);

			// INSERT INTO Issues
			String sql2 = String.format("insert into Issues values(%d, '%s', '%s', '%s');", pubID, issueTitle,
					issueDate, periodicity);
			db.update(sql2);

			// commit and display confirmation
			if (db.commit()) {
				System.out.println("Successfully Added Following Record:");
				String sql3 = String.format("select * from Publication natural join Issues where PublicationID = %d;",
						pubID);
				result = db.query(sql3);
				DBTablePrinter.printResultSet(result);
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			// enable auto-commit
			db.enableAutocommit();
		}

	}

	/**
	 * Modifies information for an issue in database. User inputs new issue
	 * information to be modified.
	 */
	public void editIssue() {
		try {
			System.out.println("EDITING ISSUE...");

			// display Issues catalog
			printIssueCatalog();

			// get PublicationID to edit
			System.out.print("Enter publication ID to edit: ");
			int pubID = scanner.nextInt();
			scanner.nextLine();

			// display current issue information
			String sql1 = String.format("select * from Publication natural join Issues where PublicationID = %d;",
					pubID);
			result = db.query(sql1);
			System.out.printf("Current information for PublicationID: %d\n", pubID);
			DBTablePrinter.printResultSet(result);

			// get information to update from user
			System.out.print("Enter new publication ID (or 0 to keep current): ");
			int newPubID = scanner.nextInt();
			scanner.nextLine();
			System.out.print("Enter new periodical title (or blank to keep current): ");
			String newTitle = scanner.nextLine();
			System.out.print("Enter new periodical type (or blank to keep current): ");
			String newType = scanner.nextLine();
			System.out.print("Enter new topic (or blank to keep current): ");
			String newTopic = scanner.nextLine();
			System.out.print("Enter new issue title (or blank to keep current): ");
			String newIssueTitle = scanner.nextLine();
			System.out.print("Enter new issue date in YYYY-MM-DD format (or blank to keep current): ");
			String newIssueDate = scanner.nextLine();
			System.out.print("Enter new issue periodicity (or blank to keep current): ");
			String newPeriodicity = scanner.nextLine();

			// start update transaction
			db.disableAutocommit();

			// build UPDATE Publication SQL
			String sql3 = "";
			if (newPubID != 0)
				sql3 += "PublicationID = " + newPubID;
			if (!newTitle.isEmpty())
				sql3 += String.format("%sTitle = '%s'", sql3.isEmpty() ? "" : ", ", newTitle);
			if (!newType.isEmpty())
				sql3 += String.format("%sType = '%s'", sql3.isEmpty() ? "" : ", ", newType);
			if (!newTopic.isEmpty())
				sql3 += String.format("%sTopic = '%s'", sql3.isEmpty() ? "" : ", ", newTopic);

			// build UPDATE Issues SQL
			String sql5 = "";
			if (!newIssueTitle.isEmpty())
				sql5 += String.format("IssueTitle = '%s'", newIssueTitle);
			if (!newIssueDate.isEmpty())
				sql5 += String.format("%sIssueDate = '%s'", sql5.isEmpty() ? "" : ", ", newIssueDate);
			if (!newPeriodicity.isEmpty())
				sql5 += String.format("%sPeriodicity = '%s'", sql5.isEmpty() ? "" : ", ", newPeriodicity);

			// UPDATE Issues
			if (!sql5.isEmpty()) {
				String sql6 = String.format("update Issues set %s where PublicationID = %d;", sql5, pubID);
				db.update(sql6);
			}

			// UPDATE Publication
			if (!sql3.isEmpty()) {
				String sql4 = String.format("update Publication set %s where PublicationID = %d;", sql3, pubID);
				db.update(sql4);
			}

			// commit transaction and display confirmation
			String sql7 = String.format("select * from Publication natural join Issues where PublicationID = %d;",
					newPubID == 0 ? pubID : newPubID);
			result = db.query(sql7);

			if (db.commit()) {
				System.out.println("\nSuccessfully Updated Following Record:");
				DBTablePrinter.printResultSet(result);
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			// enable auto-commit
			db.enableAutocommit();
		}

	}

	/**
	 * Removes an issue publication from the database
	 */
	public void deleteIssue() {
		try {
			System.out.println("DELETING ISSUE...");

			// display Issues catalog
			printIssueCatalog();

			// get PublicationID to delete
			System.out.print("Enter publication ID to delete: ");
			int pubID = scanner.nextInt();
			scanner.nextLine();

			// get publication information
			String sql1 = String.format("select * from Publication natural join Issues where PublicationID = %d;",
					pubID);
			result = db.query(sql1);

			// delete record
			String sql2 = String.format("delete from Publication where PublicationID = %d;", pubID);
			db.update(sql2);

			// confirmation
			System.out.println("Successfully Removed Following Record:");
			DBTablePrinter.printResultSet(result);

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	/**
	 * Inserts a new issue article into database. User inputs article information.
	 */
	public void addArticle() {
		try {
			System.out.println("ADDING ARTICLE...");

			// display Issues catalog
			printIssueCatalog();

			// get PublicationID from user
			System.out.print("Enter publication ID: ");
			int pubID = scanner.nextInt();
			scanner.nextLine();

			// display current article list
			result = db.query(String.format("select * from Articles where PublicationID = %d;", pubID));
			DBTablePrinter.printResultSet(result);

			// get article information from user
			System.out.print("Enter article title: ");
			String articleTitle = scanner.nextLine();
			System.out.print("Enter article topic: ");
			String articleTopic = scanner.nextLine();

			// INSERT INTO Articles
			String sql1 = String.format("insert into Articles values(%d, '%s', '%s', NULL);", pubID, articleTitle,
					articleTopic);
			db.update(sql1);

			System.out.print("Add optional author information (y/n): ");
			if (scanner.nextLine().toLowerCase().startsWith("y")) {
				// display author list
				result = db.query("select * from Employees where EmpID in (select * from Authors);");
				System.out.println("Author List:");
				DBTablePrinter.printResultSet(result);

				// user input author information - multiple authors allowed
				while (true) {
					int empID;
					System.out.print("Enter Employee ID (enter \"new\" to add new author or leave blank to finish): ");
					String input = scanner.nextLine();
					if (input.isEmpty())
						break;
					if (input.toLowerCase().equals("new")) {
						Publishing pub = new Publishing(db, scanner);
						empID = pub.addAuthor();
					} else
						empID = Integer.valueOf(input);
					String sql2 = String.format("insert into WritesArticle values(%d, '%s', %d);", pubID, articleTitle,
							empID);
					db.update(sql2);
				}
			}

			// confirmation
			String sql3 = String.format("select * from Articles where PublicationID = %d and ArticleTitle = '%s';",
					pubID, articleTitle);
			String sql4 = String.format(
					"select Name as 'Author(s)' from WritesArticle natural join Employees where PublicationID = %d and ArticleTitle = '%s';",
					pubID, articleTitle);
			result = db.query(sql3);
			ResultSet result2 = db.query(sql4);
			System.out.println("Successfully Added Following Record:");
			DBTablePrinter.printResultSet(result);
			DBTablePrinter.printResultSet(result2);

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	/**
	 * Modifies issue article information in database. User inputs new article
	 * information for modification.
	 */
	public void editArticle() {
		try {
			System.out.println("EDITING ARTICLE...");

			// display Issue catalog and get PublicationID from user
			printIssueCatalog();
			System.out.print("Enter publication ID: ");
			int pubID = scanner.nextInt();
			scanner.nextLine();

			// display Article list and get Article Title from user
			result = db.query(String.format("select * from Articles where PublicationID = %d;", pubID));
			DBTablePrinter.printResultSet(result);
			System.out.print("Enter the article title you want to edit: ");
			String articleTitle = scanner.nextLine();

			// get updated article information from user
			System.out.print("Enter the new article title (or blank to keep current): ");
			String newArticleTitle = scanner.nextLine();
			System.out.print("Enter the new article topic (or blank to keep current): ");
			String newArticleTopic = scanner.nextLine();

			// build and commit UPDATE SQL
			String sql1 = "";
			if (!newArticleTitle.isEmpty())
				sql1 += String.format("ArticleTitle = '%s'", newArticleTitle);
			if (!newArticleTopic.isEmpty())
				sql1 += String.format("%sArticleTopic = '%s'", sql1.isEmpty() ? "" : ", ", newArticleTopic);

			String sql2 = String.format("update Articles set %s where PublicationID = %d and ArticleTitle = '%s';",
					sql1, pubID, articleTitle);
			db.update(sql2);

			// confirmation
			System.out.println("Successfully Updated Following Record:");
			String sql3 = String.format("select * from Articles where PublicationID = %d and ArticleTitle = '%s';",
					pubID, newArticleTitle);
			result = db.query(sql3);
			DBTablePrinter.printResultSet(result);

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	/**
	 * Removes issue article from database
	 */
	public void deleteArticle() {
		try {
			System.out.println("DELETING ARTICLE...");

			// display Issue catalog and get PublicationID from user
			printIssueCatalog();
			System.out.print("Enter publication ID: ");
			int pubID = scanner.nextInt();
			scanner.nextLine();

			// display Article list and get Article Title from user
			result = db.query(String.format("select * from Articles where PublicationID = %d;", pubID));
			DBTablePrinter.printResultSet(result);
			System.out.print("Enter the article title you want to delete: ");
			String articleTitle = scanner.nextLine();

			// get info then DELETE from Articles
			String sql = String.format("select * from Articles where PublicationID = %d and ArticleTitle = '%s';",
					pubID, articleTitle);
			result = db.query(sql);
			String sql2 = String.format("delete from Articles where PublicationID = %d and ArticleTitle = '%s';", pubID,
					articleTitle);
			db.update(sql2);

			// confirmation
			System.out.println("Successfully Removed Following Record:");
			DBTablePrinter.printResultSet(result);

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	/**
	 * Modifies issue article to add article text into database
	 */
	public void addArticleText() {
		try {
			System.out.println("ADDING ARTICLE TEXT...");

			// display Issue catalog and get PublicationID from user
			printIssueCatalog();
			System.out.print("Enter publication ID: ");
			int pubID = scanner.nextInt();
			scanner.nextLine();

			// display Article list and get Article Title from user
			result = db.query(String.format("select * from Articles where PublicationID = %d;", pubID));
			DBTablePrinter.printResultSet(result);
			System.out.print("Enter the article title you want to edit: ");
			String articleTitle = scanner.nextLine();

			// get article text from user
			System.out.print("Enter article text filename: ");
			String articleText = scanner.nextLine();

			// UPDATE Articles table in DB
			String sql = String.format(
					"update Articles set ArticleText = '%s' where PublicationID = %d and ArticleTitle = '%s';",
					articleText, pubID, articleTitle);
			db.update(sql);

			// confirmation
			System.out.println("Successfully Updated Following Record:");
			String sql2 = String.format("select * from Articles where PublicationID = %d and ArticleTitle = '%s';",
					pubID, articleTitle);
			result = db.query(sql2);
			DBTablePrinter.printResultSet(result);

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	/**
	 * Modify article text attribute of an issue article in database
	 */
	public void editArticleText() {
		try {
			System.out.println("UPDATING ARTICLE TEXT...");

			// display Issue catalog and get PublicationID from user
			printIssueCatalog();
			System.out.print("Enter publication ID: ");
			int pubID = scanner.nextInt();
			scanner.nextLine();

			// display Article list and get Article Title from user
			result = db.query(String.format("select * from Articles where PublicationID = %d;", pubID));
			DBTablePrinter.printResultSet(result);
			System.out.print("Enter the article title you want to edit: ");
			String articleTitle = scanner.nextLine();

			// get new article text information from user
			System.out.print("Enter the new article text filename: ");
			String newArticleText = scanner.nextLine();

			// UPDATE Articles table in DB
			String sql = String.format(
					"update Articles set ArticleText = '%s' where PublicationID = %d and ArticleTitle = '%s';",
					newArticleText, pubID, articleTitle);
			db.update(sql);

			// confirmation
			System.out.println("Successfully Updated Following Record:");
			String sql2 = String.format("select * from Articles where PublicationID = %d and ArticleTitle = '%s';",
					pubID, articleTitle);
			result = db.query(sql2);
			DBTablePrinter.printResultSet(result);

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	/**
	 * Prints book information in database that matches user-input values for
	 * author, publication date, and/or topic.
	 */
	public void getBookInfo() {
		try {
			System.out.println("SEARCH BOOK CATALOG...");

			// get search details from user
			System.out.print("Enter author name (or blank to skip): ");
			String author = scanner.nextLine();
			System.out.print("Enter publication date as YYYY-MM-DD (or blank to skip): ");
			String pubDate = scanner.nextLine();
			System.out.print("Enter book topic (or blank to skip): ");
			String topic = scanner.nextLine();

			// build SQL SELECT with search criteria
			String sql1 = "";
			if (!author.isEmpty())
				sql1 += String.format(
						"PublicationID in (select PublicationID from WritesBook where EmpID in (select EmpID from Employees where name = '%s'))",
						author);
			if (!pubDate.isEmpty())
				sql1 += String.format("%sPublicationDate = '%s'", sql1.isEmpty() ? "" : " AND ", pubDate);
			if (!topic.isEmpty())
				sql1 += String.format("%sTopic = '%s'", sql1.isEmpty() ? "" : " AND ", topic);

			String sql2 = String.format("select * from Publication natural join Books%s;",
					sql1.isEmpty() ? "" : " where " + sql1);

			// display results
			result = db.query(sql2);
			DBTablePrinter.printResultSet(result);

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	/**
	 * Prints article information in database that matches user-input values for
	 * author, creation date (date of issue), and/or topic.
	 */
	public void getArticleInfo() {
		try {
			System.out.println("SEARCH ARTICLE CATALOG...");

			// get search details from user
			System.out.print("Enter author name (or blank to skip): ");
			String author = scanner.nextLine();
			System.out.print("Enter issue date as YYYY-MM-DD (or blank to skip): ");
			String issueDate = scanner.nextLine();
			System.out.print("Enter article topic (or blank to skip): ");
			String topic = scanner.nextLine();

			// build SQL SELECT with search criteria
			String sql1 = "";
			if (!author.isEmpty())
				sql1 += String.format(
						"(PublicationID, ArticleTitle) in (select PublicationID, ArticleTitle from WritesArticle where EmpID in (select EmpID from Employees where name = '%s'))",
						author);
			if (!issueDate.isEmpty())
				sql1 += String.format("%sIssueDate = '%s'", sql1.isEmpty() ? "" : " AND ", issueDate);
			if (!topic.isEmpty())
				sql1 += String.format("%sArticleTopic = '%s'", sql1.isEmpty() ? "" : " AND ", topic);

			String sql2 = String.format("select * from Publication natural join Issues natural join Articles%s;",
					sql1.isEmpty() ? "" : " where " + sql1);

			// display results
			result = db.query(sql2);
			DBTablePrinter.printResultSet(result);

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	/**
	 * Insert payment to employee into the database. User inputs payment information
	 */
	public void makePayment() {
		try {
			System.out.println("ENTERING EMPLOYEE PAYMENT...");

			// print Employee information table
			result = db.query("select * from Employees;");
			DBTablePrinter.printResultSet(result);

			// get payment information from user
			System.out.print("Enter Employee ID number: ");
			int empID = scanner.nextInt();
			scanner.nextLine();
			System.out.print("Enter payment amount (leave blank to calculate automatically): ");
			String amount = scanner.nextLine();
			System.out.print("Enter payment date as YYYY-MM-DD (leave blank to use current date): ");
			String payDate = scanner.nextLine();

			// determine attribute values if needed
			if (payDate.isEmpty())
				payDate = java.time.LocalDate.now().toString();
			double amt;
			if (amount.isEmpty()) {
				// get employee type (staff/invited)
				result = db.query(String.format("select Type from Employees where EmpID = %d;", empID));
				result.next();
				String empType = result.getString(1);

				// if staff employee pay based on default salary
				if (empType.equals("Staff"))
					amt = 2000;

				// get most recent payment date
				result = db.query(String.format("select max(SubmitDate) from Payments where EmpID = %d;", empID));
				result.next();
				String lastPayDate = result.getString(1);

				// get work done since last payment
				int pubEdits = 0, bookWrites = 0, artWrites = 0;
				ResultSet result2;
				String sql2;

				// if an editor count publications edited
				result = db.query(String.format("select * from Editors where EmpID = %d;", empID));
				if (result.next()) {
					// count books edited
					sql2 = String.format(
							"select count(*) from Publication natural join Books natural join Edits where EmpID = %d and PublicationDate > '%s' and PublicationDate <= '%s';",
							empID, lastPayDate, payDate);
					result2 = db.query(sql2);
					result2.next();
					pubEdits += result2.getInt(1);

					// count issues edited
					sql2 = String.format(
							"select count(*) from Publication natural join Issues natural join Edits where EmpID = %d and IssueDate > '%s' and IssueDate <= '%s';",
							empID, lastPayDate, payDate);
					result2 = db.query(sql2);
					result2.next();
					pubEdits += result2.getInt(1);
				}

				// if an author count publications written
				result = db.query(String.format("select * from Authors where EmpID = %d;", empID));
				if (result.next()) {
					// count books written
					sql2 = String.format(
							"select count(*) from Publication natural join Books natural join WritesBook where EmpID = %d and PublicationDate > '%s' and PublicationDate <= '%s';",
							empID, lastPayDate, payDate);
					result2 = db.query(sql2);
					result2.next();
					bookWrites += result2.getInt(1);

					// count articles written
					sql2 = String.format(
							"select count(*) from Publication natural join Issues natural join Articles natural join WritesArticle where EmpID = %d and IssueDate > '%s' and IssueDate <= '%s';",
							empID, lastPayDate, payDate);
					result2 = db.query(sql2);
					result2.next();
					artWrites += result2.getInt(1);
				}

				// set default pay rates
				final int PER_PUB_EDIT = 500;
				final int PER_BOOK_WRITE = 1000;
				final int PER_ARTICLE_WRITE = 200;

				// calculate payment amount
				amt = pubEdits * PER_PUB_EDIT + bookWrites * PER_BOOK_WRITE + artWrites * PER_ARTICLE_WRITE;

			} else
				amt = Double.valueOf(amount);

			// exit if no payment required
			if (amt == 0) {
				System.out.println("No payment required to employee.");
				return;
			}

			// start transaction
			db.disableAutocommit();

			// INSERT INTO Payments table in db
			String sql = String.format("insert into Payments values(NULL, %d, %.2f, '%s', NULL);", empID, amt, payDate);
			db.update(sql);

			// get auto-generated check number
			result = db.query("select last_insert_id();");
			result.next();
			int checkNum = result.getInt(1);

			// commit and confirmation
			String sql3 = String.format("select * from Payments where CheckNumber = %d;", checkNum);
			ResultSet result2 = db.query(sql3);
			if (db.commit()) {
				System.out.println("Successfully Entered Following Record:");
				DBTablePrinter.printResultSet(result2);
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			// enable auto-commit
			db.enableAutocommit();
		}

	}

	/**
	 * Update existing payment to an employee in the database. User inputs claim
	 * date of employee check upon receipt.
	 */
	public void updatePayment() {
		try {
			System.out.println("UPDATING EMPLOYEE PAYMENT...");

			// display Payments table
			result = db.query("select * from Payments;");
			DBTablePrinter.printResultSet(result);

			// get Payment information from user
			System.out.print("Enter Check Number: ");
			int checkNum = scanner.nextInt();
			scanner.nextLine();
//			System.out.print("Enter Employee ID number: ");
//			int empID = scanner.nextInt(); scanner.nextLine();
			System.out.print("Enter claim date for payment as YYYY-MM-DD (leave blank to use current date): ");
			String claimDate = scanner.nextLine();

			if (claimDate.isEmpty())
				claimDate = java.time.LocalDate.now().toString();

			// UPDATE Payments table
			String sql = String.format("update Payments set ClaimDate = '%s' where CheckNumber = %d;", claimDate,
					checkNum);
			db.update(sql);

			// confirmation
			String sql2 = String.format("select * from Payments where CheckNumber = %d;", checkNum);
			result = db.query(sql2);
			System.out.println("Successfully Entered Following Record:");
			DBTablePrinter.printResultSet(result);

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	/**
	 * Prints payment information for a given check number as input by the user.
	 */
	public void trackPayment() {
		try {
			System.out.println("TRACKING EMPLOYEE PAYMENT...");

//			// display Payments table
//			result = db.query("select * from Payments;");
//			DBTablePrinter.printResultSet(result);

			// get Payment information from user
			System.out.print("Enter Check Number: ");
			int checkNum = scanner.nextInt();
			scanner.nextLine();

			// query and print results
			String sql = String.format("select * from Payments where CheckNumber = %d;", checkNum);
			result = db.query(sql);
			DBTablePrinter.printResultSet(result);

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
				{ "  P12        | update article text                                 | ", "" },
				{ "  P13        | search book catalog                                 | ", "" },
				{ "  P14        | search article catalog                              | ", "" },
				{ "  P15        | make employee payment                               | ", "" },
				{ "  P16        | update employee payment                             | ", "" },
				{ "  P17        | track employee payment                              | ", "" } };

		for (int i = 0; i < help.length; i++) {
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

		case "p13":
			getBookInfo();
			break;

		case "p14":
			getArticleInfo();
			break;

		case "p15":
			makePayment();
			break;

		case "p16":
			updatePayment();
			break;

		case "p17":
			trackPayment();
			break;

		default:
			System.out.println("Here are the Valid Command Codes, and their required information");
			helper();
			break;
		}
	}

	/**
	 * Prints all Issues in database
	 */
	public void printIssueCatalog() {
		result = db.query("select * from Publication natural join Issues;");
		System.out.println("Issue catalog:");
		DBTablePrinter.printResultSet(result);
	}

	/**
	 * Prints all Books in database
	 */
	public void printBookCatalog() {
		result = db.query("select * from Publication natural join Books;");
		System.out.println("Book catalog:");
		DBTablePrinter.printResultSet(result);
	}
}
