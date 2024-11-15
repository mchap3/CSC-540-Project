import java.util.Scanner;
//javac database_setup.java DBManager.java DBTablePrinter.java User.java Reports.java Distribution.java Production.java Publishing.java Publisher.java DistributionTeam.java Editor.java FinancialTeam.java
//java database_setup

/**
 * User class creates user objects and starts the corresponding class for each
 * user type
 * 
 * @author Morgan Chapman
 */
public class User {

	private String currUser = null;
	private Scanner scanner = null;

	private DBManager db = null;

	private FinancialTeam financial = null;
	private Editor editor = null;
	// add your objects here
	private Publisher publisher = null;
	private DistributionTeam distr_team = null;

	/**
	 * Constructor with DBManager for database connection and Scanner for user input
	 * and user type string.
	 * 
	 * @param user String
	 * @param dbM  DBManager object
	 * @param s    Scanner object
	 */
	public User(String user, Scanner s, DBManager dbM) {

		db = dbM;
		scanner = s;
		currUser = user;

		if (user.equals("4")) { // Financial Team
			financial = new FinancialTeam(db, scanner);
		} else if (user.equals("1")) { // publisher
			publisher = new Publisher(db, scanner);
		} else if (user.equals("2")) { // editor
			// editor object
			editor = new Editor(db, scanner);
		} else if (user.equals("3")) { // distribution team
			distr_team = new DistributionTeam(db, scanner);
		}

	}

	/**
	 * Directs command string from user input to call the appropriate user type
	 * class
	 * 
	 * @param command command string
	 */
	public void command(String command) {

		if (currUser.equals("4")) { // Financial Team
			financial.command(command);
		} else if (currUser.equals("1")) { // publisher
			publisher.command(command);
		} else if (currUser.equals("2")) { // editor
			editor.command(command);
		} else if (currUser.equals("3")) { // distribution team
			distr_team.command(command);
		}
	}
}