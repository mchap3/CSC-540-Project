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
			
			System.out.print("Creating book...");
			System.out.print("Enter a publication ID: ");
			pubID = scanner.nextInt(); scanner.nextLine();
			System.out.print("Enter book title: ");
			title = scanner.nextLine();
//			System.out.print("Enter publication type (book, magazine, journal): ");
			type = "book";
			System.out.print("Enter book topic: ");
			topic = scanner.nextLine();
			System.out.print("Enter book edition number: ");
			editionNum = scanner.nextLine();
			System.out.print("Enter ISBN: ");
			isbn = scanner.nextLine();
			System.out.print("Enter publication date: ");
			pubDate = scanner.nextLine();
			
			String str1 = String.format("insert into Publication values(%d, '%s', '%s', '%s');",
										pubID, title, type, topic);
			String str2 = String.format("insert into Books values(%d, '%s', '%s', '%s')",
										pubID, editionNum, isbn, pubDate);

			db.update(str1);
			db.update(str2);
			
		} catch (Exception e) {
			e.printStackTrace();

		} 
		
	}
}
