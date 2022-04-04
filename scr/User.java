import java.util.Scanner;
//javac database_setup.java DBManager.java DBTablePrinter.java User.java Reports.java Distributors.java Production.java Publishers.java
//java database_setup
public class User {
	
	private String currUser = null;
	private Scanner scanner = null;

	private DBManager db = null;
	
	private Reports report = null;
	private Editor editor = null;
	// add your objects here
	private Publishers publisher = null;
	private Distributors distr_team = null;
	
	
	public User(String user, Scanner s, DBManager dbM) {
		
		db = dbM;
		scanner = s;
		currUser = user;
		
		if (user.equals("4")) { // Financial Team
			report = new Reports(db, scanner);
		}
		else if (user.equals("1")) { // publisher
			publisher = new Publishers(db, scanner);
		}
		else if (user.equals("2")) { // editor
			// editor object
			editor = new Editor(db, scanner);
		}
		else if (user.equals("3")) { // distribution team
			distr_team = new Distributors(db, scanner);
		}
		
	}
	
	public void command(String command) {

		if (currUser.equals("4")) { // Financial Team
			report.command(command);
		}
		else if (currUser.equals("1")) { // publisher
			publisher.command(command);
		}
		else if (currUser.equals("2")) { // editor
			editor.command(command);
		}
		else if (currUser.equals("3")) { // distribution team
			distr_team.command(command);			
		}
	}
}
