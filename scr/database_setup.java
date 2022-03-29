
/**
 * Create and populate all database tables
 */
import java.util.*;
// javac database_setup.java DBManager.java DBTablePrinter.java Reports.java User.java
// java database_setup

public class database_setup {

	public static void main(String[] args) {
			
			DBManager db = null;
			User currentUser = null;
			
			Scanner scanner = new Scanner(System.in);
			
			db = new DBManager();
			
			
			// create user;
			while(true) {
				
				System.out.println("Enter a Valid User Type (Example: Publisher, Editor, Distribution Team, Financial Team)\n"
						+ "User Type: ");
				String userType = scanner.nextLine().toLowerCase();
				
				if (userType.equals("publisher")|| userType.equals("editor") || userType.equals("distribution team")|| userType.equals("financial team")) {
					currentUser = new User(userType, scanner, db);
					break;
				}
			}
			
			// run commands depending on user type, look at the bottom of the file to add you objects
			while(true) {
				System.out.println("Enter a Command:");
				String command = scanner.nextLine();
				
				if (command.toLowerCase().equals("exit") || command.toLowerCase().equals("quit")) {
					break;
				} else {
					currentUser.command(command.toLowerCase());
				}

			}

			scanner.close();
			
			db.close();
		
	}


}


