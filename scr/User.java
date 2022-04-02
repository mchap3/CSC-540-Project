import java.util.Scanner;
//javac database_setup.java DBManager.java DBTablePrinter.java Reports.java User.java
//java database_setup
public class User {
	
	private String currUser = null;
	private Scanner scanner = null;

	private DBManager db = null;
	
	private Reports report = null;
	private Production prod = null;
	// add your objects here
	
	
	public User(String user, Scanner s, DBManager dbM) {
		
		db = dbM;
		scanner = s;
		currUser = user;
		
		if (user.equals("4")) { // Financial Team
			report = new Reports(db, scanner);
		}
		else if (user.equals("1")) { // publisher
			// publisher object
		}
		else if (user.equals("2")) { // editor
			// editor object
			prod = new Production(db, scanner);
		}
		else if (user.equals("3")) { // distribution team
			// distribution team object
		}
		
	}
	
	public void command(String command) {

		if (currUser.equals("4")) { // Financial Team
			report.command(command);
		}
		else if (currUser.equals("1")) { // publisher
			
		}
		else if (currUser.equals("2")) { // editor
			prod.command(command);
		}
		else if (currUser.equals("3")) { // distribution team
			
		}
	}
}
