import java.util.Scanner;
//javac database_setup.java DBManager.java DBTablePrinter.java Reports.java User.java
//java database_setup
public class User {
	
	private String currUser = null;
	private Scanner scanner = null;

	private DBManager db = null;
	
	private Reports report = null;
	// add your objects here
	
	
	public User(String user, Scanner s, DBManager dbM) {
		
		db = dbM;
		scanner = s;
		currUser = user;
		
		if (user.equals("financial team")) {
			report = new Reports(db, scanner);
		}
		else if (user.equals("publisher")) {
			// publisher object
		}
		else if (user.equals("editor")) {
			// editor object
		}
		else if (user.equals("distribution team")) {
			// distribution team object
		}
		
	}
	
	public void command(String command) {

		if (currUser.equals("financial team")) {
			report.command(command);
		}
		else if (currUser.equals("publisher")) {
			
		}
		else if (currUser.equals("editor")) {
			
		}
		else if (currUser.equals("distribution team")) {
			
		}
	}
}
