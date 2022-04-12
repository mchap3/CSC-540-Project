
/**
 * Create and populate all database tables
 */
import java.util.*;
//javac database_setup.java DBManager.java DBTablePrinter.java User.java Reports.java Distributors.java Production.java Publishers.java
//java database_setup
public class database_setup {

	public static void main(String[] args) {

		DBManager db = new DBManager();

		User currentUser = null;

		Scanner scanner = new Scanner(System.in);

		String check = null;

		while (check == null || check == "logout") {

			currentUser = createUser(scanner, db, currentUser);

			check = command(scanner, currentUser);
		}

		scanner.close();

		db.close();

	}

	// create User, returns the User object
	private static User createUser(Scanner scanner, DBManager db, User currentUser) {

		String userType = null;

		while (true) {

			System.out.println(
					"Enter a Valid User Type number (Example: \n 1 for Publisher, \n 2 for Editor, \n 3 for Distribution Team, \n 4 for Financial Team)\n"
							+ "User Type: ");
			userType = scanner.nextLine().toLowerCase();

			if (userType.equals("1") || userType.equals("2") || userType.equals("3") || userType.equals("4")) {
				currentUser = new User(userType, scanner, db);
				currentUser.command("");
				break;
			}
		}

		return currentUser;

	}

	private static String command(Scanner scanner, User currentUser) {

		String logout = null;

		// run commands depending on user type
		while (true) {

			System.out.println("Enter a Command:");
			String command = scanner.nextLine();

			if (command.toLowerCase().equals("exit") || command.toLowerCase().equals("quit")) {
				logout = "exit";
				break;

			} else if (command.toLowerCase().equals("logout")) {
				logout = "logout";
				break;

			} else {
				currentUser.command(command.toLowerCase());
			}
		}

		return logout;

	}

}
