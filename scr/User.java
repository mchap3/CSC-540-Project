import java.sql.Connection;
import java.util.Scanner;

public class User {
	
	private String currUser = null;
	private Scanner scanner = null;
	private Connection connection = null;
	
	private Reports report = null;
	// add your objects here
	
	
	public User(String user, Connection c, Scanner s) {
		scanner = s;
		currUser = user;
		connection = c;

		if (user.equals("financial team")) {
			report = new Reports(connection, scanner);
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
