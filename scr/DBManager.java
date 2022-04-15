import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//javac database_setup.java DBManager.java DBTablePrinter.java User.java Reports.java Distribution.java Production.java Publishing.java Publisher.java DistributionTeam.java Editor.java FinancialTeam.java
//java database_setup

/**
 * Database manager that provides methods to connect to database, operations
 * with database (table creation, table population, updates, transaction, etc.),
 * closes connection objects to database
 * 
 * @author Chaitanya Patel
 */
public class DBManager {

	// static final String jdbcURL =
	// "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/cpatel3";
	// static final String user = "cpatel3";
	// static final String password = "200048024";
	// static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/iarakel";
	// static final String user = "iarakel";
	// static final String password = "Cehtycehty00";
	static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/aabate";
	static final String user = "aabate";
	static final String password = "aabate";

	private Connection connection = null;
	private Statement statement = null;
	private ResultSet result = null;

	/**
	 * Calls methods to connect to DB, create and populate tables
	 */
	public DBManager() {
		connectToDatabase();
		createTables();
		populateTables();
	}

	/**
	 * Returns connection object
	 */
	public Connection getConn() {
		return connection;
	}

	/**
	 * Creates a Statement object for sending statements to database.
	 */
	public Statement createStatement() {
		try {

			statement = connection.createStatement();
			return statement;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		}
	}

	/**
	 * Returns result object
	 */
	public ResultSet getResult() {
		return result;
	}

	/**
	 * Disable auto commit mode
	 */
	public void disableAutocommit() {
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Enable auto commit mode
	 */
	public void enableAutocommit() {
		try {
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Returns result of database query
	 */
	public ResultSet query(String s) {
		try {
			createStatement();
			result = statement.executeQuery(s);

			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		return null;
	}

	/**
	 * Update database
	 */
	public void update(String s) {
		try {
			createStatement();
			statement.executeUpdate(s);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	/**
	 * Commit if successful, rollback otherwise
	 */
	public boolean commit() {

		try {
			connection.commit();

			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			try {
				if (connection != null)
					System.out.println("Error Commiting, Please Check Format");
				connection.rollback();
				return false;
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}

		return false;
	}

	/**
	 * Connect to database with specified user credentials create statement object
	 * Drop specified tables if they exist
	 */
	private void connectToDatabase() {

		try {

			Class.forName("org.mariadb.jdbc.Driver");

			connection = DriverManager.getConnection(jdbcURL, user, password);
			statement = connection.createStatement();

			statement.executeUpdate("SET FOREIGN_KEY_CHECKS=0;");
			statement.executeUpdate("DROP TABLE IF EXISTS WritesBook");
			statement.executeUpdate("DROP TABLE IF EXISTS WritesArticle");
			statement.executeUpdate("DROP TABLE IF EXISTS Edits");
			statement.executeUpdate("DROP TABLE IF EXISTS Articles");
			statement.executeUpdate("DROP TABLE IF EXISTS Chapters");
			statement.executeUpdate("DROP TABLE IF EXISTS Issues");
			statement.executeUpdate("DROP TABLE IF EXISTS Books");
			statement.executeUpdate("DROP TABLE IF EXISTS Editors");
			statement.executeUpdate("DROP TABLE IF EXISTS Authors");
			statement.executeUpdate("DROP TABLE IF EXISTS Payments");
			statement.executeUpdate("DROP TABLE IF EXISTS Employees");
			statement.executeUpdate("DROP TABLE IF EXISTS Invoices");
			statement.executeUpdate("DROP TABLE IF EXISTS Orders");
			statement.executeUpdate("DROP TABLE IF EXISTS Distributors");
			statement.executeUpdate("DROP TABLE IF EXISTS Publication");
			statement.executeUpdate("SET FOREIGN_KEY_CHECKS=1;");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Create database tables
	 */
	private void createTables() {
		try {
			statement.executeUpdate("CREATE TABLE Publication(PublicationID INT AUTO_INCREMENT, "
					+ "Title VARCHAR(128) NOT NULL, " + "Type VARCHAR(128) NOT NULL, " + "Topic VARCHAR(128) NOT NULL, "
					+ "PRIMARY KEY (PublicationID));");
			statement.executeUpdate("CREATE TABLE Issues(PublicationID INT, " + "IssueTitle VARCHAR(128) NOT NULL, "
					+ "IssueDate DATE NOT NULL, " + "Periodicity VARCHAR(128) NOT NULL,"
					+ "PRIMARY KEY (PublicationID), "
					+ "FOREIGN KEY (PublicationID) REFERENCES Publication(PublicationID) "
					+ "ON UPDATE CASCADE ON DELETE CASCADE);");
			statement.executeUpdate("CREATE TABLE Articles(PublicationID INT, " + "ArticleTitle VARCHAR(128) NOT NULL, "
					+ "ArticleTopic VARCHAR(128) NOT NULL, " + "ArticleText VARCHAR(128), "
					+ "PRIMARY KEY (PublicationID, ArticleTitle), "
					+ "FOREIGN KEY (PublicationID) REFERENCES Issues(PublicationID) "
					+ "ON UPDATE CASCADE ON DELETE CASCADE);");
			statement.executeUpdate("CREATE TABLE Books(PublicationID INT, " + "EditionNumber VARCHAR(16), "
					+ "ISBN VARCHAR(32), " + "PublicationDate DATE NOT NULL, " + "PRIMARY KEY (PublicationID), "
					+ "UNIQUE(ISBN), " + "FOREIGN KEY (PublicationID) REFERENCES Publication(PublicationID) "
					+ "ON UPDATE CASCADE ON DELETE CASCADE);");
			statement.executeUpdate("CREATE TABLE Chapters(PublicationID INT, " + "ChapterTitle VARCHAR(128), "
					+ "PRIMARY KEY (PublicationID, ChapterTitle), "
					+ "FOREIGN KEY (PublicationID) REFERENCES Books(PublicationID) "
					+ "ON UPDATE CASCADE ON DELETE CASCADE);");
			statement.executeUpdate("CREATE TABLE Employees(EmpID INT AUTO_INCREMENT, " + "Name VARCHAR(128) NOT NULL, "
			// + "Age INT NOT NULL, "
			// + "Gender VARCHAR(10) NOT NULL, "
					+ "Type VARCHAR(10) NOT NULL, " + "Phone VARCHAR(16) NOT NULL, " + "Email VARCHAR(128) NOT NULL, "
					+ "Address VARCHAR(128) NOT NULL, "
//                    + "Active BOOLEAN NOT NULL, "
					+ "PRIMARY KEY (EmpID));");
			statement.executeUpdate("CREATE TABLE Editors(EmpID INT, " + "PRIMARY KEY (EmpID), "
					+ "FOREIGN KEY (EmpID) REFERENCES Employees(EmpID) " + "ON UPDATE CASCADE ON DELETE CASCADE);");
			statement.executeUpdate("CREATE TABLE Authors(EmpID INT, " + "PRIMARY KEY (EmpID), "
					+ "FOREIGN KEY (EmpID) REFERENCES Employees(EmpID) " + "ON UPDATE CASCADE ON DELETE CASCADE);");
			statement.executeUpdate(
					"CREATE TABLE Edits(PublicationID INT, " + "EmpID INT, " + "PRIMARY KEY (PublicationID, EmpID), "
							+ "FOREIGN KEY (PublicationID) REFERENCES Publication(PublicationID) "
							+ "ON UPDATE CASCADE ON DELETE CASCADE, " + "FOREIGN KEY (EmpID) REFERENCES Editors(EmpID) "
							+ "ON UPDATE CASCADE ON DELETE CASCADE);");
			statement.executeUpdate("CREATE TABLE WritesBook(PublicationID INT, " + "EmpID INT, "
					+ "PRIMARY KEY (PublicationID, EmpID), "
					+ "FOREIGN KEY (PublicationID) REFERENCES Books(PublicationID) "
					+ "ON UPDATE CASCADE ON DELETE CASCADE, " + "FOREIGN KEY (EmpID) REFERENCES Authors(EmpID) "
					+ "ON UPDATE CASCADE ON DELETE CASCADE);");
			statement.executeUpdate("CREATE TABLE WritesArticle(PublicationID INT, " + "ArticleTitle VARCHAR(128), "
					+ "EmpID INT, " + "PRIMARY KEY (PublicationID, ArticleTitle, EmpID), "
					+ "FOREIGN KEY (PublicationID, ArticleTitle) REFERENCES Articles(PublicationID, ArticleTitle) "
					+ "ON UPDATE CASCADE ON DELETE CASCADE, " + "FOREIGN KEY (EmpID) REFERENCES Authors(EmpID) "
					+ "ON UPDATE CASCADE ON DELETE CASCADE);");
			statement.executeUpdate("CREATE TABLE Payments(CheckNumber INT AUTO_INCREMENT, " + "EmpID INT, "
					+ "Amount DECIMAL(8,2) NOT NULL, " + "SubmitDate DATE NOT NULL, " + "ClaimDate DATE, "
					+ "PRIMARY KEY (CheckNumber), " + "FOREIGN KEY (EmpID) REFERENCES Employees(EmpID) "
					+ "ON UPDATE CASCADE ON DELETE SET NULL);");
			statement.executeUpdate("CREATE TABLE Distributors (DistAccountNum INT AUTO_INCREMENT, "
					+ "Name VARCHAR(128) NOT NULL, " + "Type VARCHAR(128) NOT NULL, "
					+ "Address VARCHAR(128) NOT NULL, " + "City VARCHAR(128) NOT NULL, "
					+ "PhoneNumber VARCHAR(16) NOT NULL, " + "Contact VARCHAR(128) NOT NULL, "
					+ "Balance DECIMAL(8,2) NOT NULL, " + "PRIMARY KEY (DistAccountNum));");
			statement.executeUpdate("CREATE TABLE Orders (OrderID INT AUTO_INCREMENT, " + "DistAccountNum INT, "
					+ "PublicationID INT, " + "NumCopies INT NOT NULL, " + "OrderDate DATE NOT NULL, "
					+ "ProduceByDate DATE NOT NULL, " + "Price DECIMAL(8,2) NOT NULL, "
					+ "ShippingCosts DECIMAL(8,2) NOT NULL, " + "PRIMARY KEY (OrderID), "
					+ "TotalCost DECIMAL(8,2) NOT NULL, "
					+ "FOREIGN KEY (DistAccountNum) REFERENCES Distributors(DistAccountNum) "
					+ "ON UPDATE CASCADE ON DELETE SET NULL, "
					+ "FOREIGN KEY (PublicationID) REFERENCES Publication(PublicationID) "
					+ "ON UPDATE CASCADE ON DELETE SET NULL);");
			statement.executeUpdate("CREATE TABLE Invoices (InvoiceID INT AUTO_INCREMENT, " + "DistAccountNum INT, "
					+ "Amount DECIMAL(8,2) NOT NULL, " + "BillingDate DATE NOT NULL, " + "PaymentDate DATE, "
					+ "PRIMARY KEY (InvoiceID), "
					+ "FOREIGN KEY (DistAccountNum) REFERENCES Distributors(DistAccountNum) "
					+ "ON UPDATE CASCADE ON DELETE SET NULL);");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Populate tables with Demo Data
	 */
	private void populateTables() {
		try {
			// Populate Publication Tables
//			statement.executeUpdate("INSERT INTO Publication VALUES (NULL, 'Don Quixote', 'Book', 'Adventure');");
//            statement.executeUpdate("INSERT INTO Publication VALUES (NULL, 'People', 'Magazine', 'Celebrity News');");
//            statement.executeUpdate("INSERT INTO Publication VALUES (NULL, 'People', 'Magazine', 'Celebrity News');");
//            statement.executeUpdate("INSERT INTO Publication VALUES (NULL, 'The Count of Monte Cristo', 'Book', 'Adventure');");
//            statement.executeUpdate("INSERT INTO Publication VALUES (NULL, 'Science', 'Journal', 'Science');");
//            statement.executeUpdate("INSERT INTO Publication VALUES (NULL, 'Science', 'Journal', 'Science');");
//            statement.executeUpdate("INSERT INTO Publication VALUES (NULL, 'Database Systems', 'Book', 'Databases');");
//            statement.executeUpdate("INSERT INTO Publication VALUES (NULL, 'Database Systems', 'Book', 'Databases');");
			statement.executeUpdate(
					"INSERT INTO Publication VALUES (1001, 'Introduction to database', 'Book', 'technology');");
			statement.executeUpdate("INSERT INTO Publication VALUES (1002, 'Healthy Diet', 'Magazine', 'health');");
			statement.executeUpdate("INSERT INTO Publication VALUES (1003, 'Animal Science', 'Journal', 'science');");

//			statement.executeUpdate("INSERT INTO Issues VALUES (2, 'February-21', '2022-02-21', 'weekly');");
//			statement.executeUpdate("INSERT INTO Issues VALUES (3, 'February-14', '2022-02-14', 'weekly');");
//			statement.executeUpdate("INSERT INTO Issues VALUES (5, 'Volume 375 Issue 6584', '2022-03-04', 'monthly');");
//			statement.executeUpdate("INSERT INTO Issues VALUES (6, 'Volume 375 Issue 6583', '2022-02-25', 'monthly');");
			statement.executeUpdate("INSERT INTO Issues VALUES (1002, 'February-24', '2020-02-24', 'weekly');");
			statement.executeUpdate("INSERT INTO Issues VALUES (1003, 'Volume 3 Issue 1', '2020-03-01', 'monthly');");

//			statement.executeUpdate("INSERT INTO Books VALUES (1, '1', '0142437239', '2003-02-25');");
//			statement.executeUpdate("INSERT INTO Books VALUES (4, '1', '0140449264', '2003-05-27');");
//			statement.executeUpdate("INSERT INTO Books VALUES (7, '1', '0167120441', '2022-01-31');");
//			statement.executeUpdate("INSERT INTO Books VALUES (8, '2', '0335009913', '2022-02-26');");
			statement.executeUpdate("INSERT INTO Books VALUES (1001, '2ed', '12345', '2018-10-10');");

//			statement.executeUpdate(
//					"INSERT INTO Articles VALUES (2, 'Title1', 'Celebrity News', '/Issues/1/February-21/Title1.pdf');");
//			statement.executeUpdate(
//					"INSERT INTO Articles VALUES (2, 'Title2', 'Stories', '/Issues/1/February-21/Title2.pdf');");
//			statement.executeUpdate(
//					"INSERT INTO Articles VALUES (3, 'Title1', 'Stories', '/Issues/1/February-14/Title1.pdf');");
//			statement.executeUpdate(
//					"INSERT INTO Articles VALUES (6, 'Structure of Omicron', 'Coronavirus', 'structure_of_omicron.txt');");
//			statement.executeUpdate(
//					"INSERT INTO Articles VALUES (6, 'Fly Cell Atlas', 'Genetics', 'fly_cell_atlas.txt');");
			statement.executeUpdate("INSERT INTO Articles VALUES (1002, 'UnknownTitle', 'UnknownTopic', 'ABC');");
			statement.executeUpdate("INSERT INTO Articles VALUES (1003, 'UnknownTitle', 'UnknownTopic', 'AAA');");

//			statement.executeUpdate("INSERT INTO Chapters VALUES (1, 'Chapter 1');");
//			statement.executeUpdate("INSERT INTO Chapters VALUES (1, 'Chapter 2');");
//			statement.executeUpdate("INSERT INTO Chapters VALUES (4, 'Chapter 1');");
//			statement.executeUpdate("INSERT INTO Chapters VALUES (7, 'DBMS Overview');");
//			statement.executeUpdate("INSERT INTO Chapters VALUES (7, 'The Relational Model');");
//			statement.executeUpdate("INSERT INTO Chapters VALUES (8, 'DBMS Overview and More');");
//			statement.executeUpdate("INSERT INTO Chapters VALUES (8, 'The Relational Model - Expanded');");

			// Populate Staff Tables
//			statement.executeUpdate("INSERT INTO Employees VALUES (1, 'Sam T', 'Staff','919-xxx-xxxx', 'name@email.com', '400 New Street');");
//			statement.executeUpdate("INSERT INTO Employees VALUES (2, 'Mary S', 'Staff', '919-xxx-xxxx', 'name@email.com','400 New Street');");
//			statement.executeUpdate("INSERT INTO Employees VALUES (3, 'John D', 'Invited', '919-xxx-xxxx', 'name@email.com','400 New Street');");
//			statement.executeUpdate("INSERT INTO Employees VALUES (4, 'Pam L', 'Invited', '919-xxx-xxxx', 'name@email.com','400 New Street');");
//			statement.executeUpdate("INSERT INTO Employees VALUES (5, 'Don D', 'Staff', '919-xxx-xxxx', 'name@email.com','400 New Street');");
//			statement.executeUpdate("INSERT INTO Employees VALUES (6, 'Tom A', 'Invited', '919-xxx-xxxx', 'name@email.com','400 New Street');");
//			statement.executeUpdate("INSERT INTO Employees VALUES (7, 'Jan P', 'Staff', '919-xxx-xxxx', 'name@email.com','400 New Street');");
//			statement.executeUpdate("INSERT INTO Employees VALUES (8, 'Nina T', 'Invited', '919-xxx-xxxx', 'name@email.com','400 New Street');");
			statement.executeUpdate(
					"INSERT INTO Employees VALUES (3001, 'John', 'Staff', '9391234567', '3001@gmail.com','21 ABC St, NC 27');");
			statement.executeUpdate(
					"INSERT INTO Employees VALUES (3002, 'Ethen', 'Staff', '9491234567', '3002@gmail.com','21 ABC St, NC 27606');");
			statement.executeUpdate(
					"INSERT INTO Employees VALUES (3003, 'Cathy',  'Invited', '9591234567', '3003@gmail.com','3300 AAA St, NC 27606');");

//			statement.executeUpdate("INSERT INTO Authors VALUES (1);");
//			statement.executeUpdate("INSERT INTO Authors VALUES (2);");
//			statement.executeUpdate("INSERT INTO Authors VALUES (3);");
//			statement.executeUpdate("INSERT INTO Authors VALUES (4);");
			statement.executeUpdate("INSERT INTO Authors VALUES (3003);");

//			statement.executeUpdate("INSERT INTO Editors VALUES (5);");
//			statement.executeUpdate("INSERT INTO Editors VALUES (6);");
//			statement.executeUpdate("INSERT INTO Editors VALUES (7);");
//			statement.executeUpdate("INSERT INTO Editors VALUES (8);");
			statement.executeUpdate("INSERT INTO Editors VALUES (3001);");
			statement.executeUpdate("INSERT INTO Editors VALUES (3002);");

//			statement.executeUpdate("INSERT INTO Edits VALUES (1, 5);");
//			statement.executeUpdate("INSERT INTO Edits VALUES (2, 6);");
//			statement.executeUpdate("INSERT INTO Edits VALUES (5, 7);");
//			statement.executeUpdate("INSERT INTO Edits VALUES (8, 8);");
			statement.executeUpdate("INSERT INTO Edits VALUES (1001, 3001);");
			statement.executeUpdate("INSERT INTO Edits VALUES (1002, 3002);");

//			statement.executeUpdate("INSERT INTO WritesBook VALUES (7, 1);");
//			statement.executeUpdate("INSERT INTO WritesBook VALUES (7, 4);");
//			statement.executeUpdate("INSERT INTO WritesBook VALUES (8, 1);");
//			statement.executeUpdate("INSERT INTO WritesBook VALUES (8, 2);");
//			statement.executeUpdate("INSERT INTO WritesBook VALUES (8, 4);");

//			statement.executeUpdate("INSERT INTO WritesArticle VALUES (2, 'Title1', 1);");
//			statement.executeUpdate("INSERT INTO WritesArticle VALUES (2, 'Title2', 4);");
//			statement.executeUpdate("INSERT INTO WritesArticle VALUES (6, 'Structure of Omicron', 3);");
//			statement.executeUpdate("INSERT INTO WritesArticle VALUES (6, 'Fly Cell Atlas', 2);");

//			statement.executeUpdate("INSERT INTO Payments VALUES (1001, 1, 1000.00, '2022-01-31', '2022-02-05');");
//			statement.executeUpdate("INSERT INTO Payments VALUES (1002, 2, 1200.00, '2022-01-31', '2022-02-08');");
//			statement.executeUpdate("INSERT INTO Payments VALUES (1003, 4, 800.00, '2022-01-31', '2022-02-10');");
//			statement.executeUpdate("INSERT INTO Payments VALUES (1004, 1, 100.00, '2022-02-28', NULL);");
//			statement.executeUpdate("INSERT INTO Payments VALUES (1005, 2, 1200.00, '2022-02-28', NULL);");
//			statement.executeUpdate("INSERT INTO Payments VALUES (1006, 4, 800.00, '2022-02-28', NULL);");
//			statement.executeUpdate("INSERT INTO Payments VALUES (1007, 5, 100.00, '2022-02-28', NULL);");
//			statement.executeUpdate("INSERT INTO Payments VALUES (1008, 6, 1100.00, '2022-02-28', NULL);");
//			statement.executeUpdate("INSERT INTO Payments VALUES (1009, 7, 700.00, '2022-02-28', NULL);");
			statement.executeUpdate("INSERT INTO Payments VALUES (1001, 3001, 1000.00, '2020-04-01', NULL);");
			statement.executeUpdate("INSERT INTO Payments VALUES (1002, 3002, 1000.00, '2020-04-01', NULL);");
			statement.executeUpdate("INSERT INTO Payments VALUES (1003, 3003, 1200.00, '2020-04-01', NULL);");

			// These Dont work for some reason
			// statement.executeUpdate("INSERT INTO Payments VALUES (2008, 1, 1000.00,
			// '2022-04-31', '2022-05-05');");
			// statement.executeUpdate("INSERT INTO Payments VALUES (2009, 2, 1200.00,
			// '2022-04-31', '2022-05-08');");
			// statement.executeUpdate("INSERT INTO Payments VALUES (2010, 4, 800.00,
			// '2022-04-31', '2022-05-10');");
			// statement.executeUpdate("INSERT INTO Payments VALUES (2011, 1, 100.00,
			// '2022-05-28', NULL);");
			// statement.executeUpdate("INSERT INTO Payments VALUES (2012, 2, 1200.00,
			// '2022-05-28', NULL);");
			// statement.executeUpdate("INSERT INTO Payments VALUES (2013, 4, 800.00,
			// '2022-05-28', NULL);");

			// Populate Distribution Tables
//			statement.executeUpdate(
//					"INSERT INTO Distributors VALUES (1, 'Library1','Library', '100 New Street', 'Raleigh', '919-xxx-xxxx', 'John', 0)");
//			statement.executeUpdate(
//					"INSERT INTO Distributors VALUES (2, 'Library2','Library', '200 New Street', 'Durham','919-xxx-xxxx', 'John', 0.00)");
//			statement.executeUpdate(
//					"INSERT INTO Distributors VALUES (3, 'Books','Store', '300 New Street', 'Cary','919-xxx-xxxx', 'John', 0.00)");
//			statement.executeUpdate(
//					"INSERT INTO Distributors VALUES (4, 'BooksEtc','Store', '400 New Street', 'Durham','919-xxx-xxxx', 'John', 0.00)");
			statement.executeUpdate(
					"INSERT INTO Distributors VALUES (2001, 'BookSell','bookstore', '2200, A Street, NC', 'charlotte','9191234567', 'Jason', 215)");
			statement.executeUpdate(
					"INSERT INTO Distributors VALUES (2002, 'BooksDist','wholesaler', '2200, B Street, NC', 'Raleigh','9291234568', 'Alex', 0.00)");

//			statement.executeUpdate("INSERT INTO Orders VALUES (1, 1, 3, 1, '2022-3-25', '2022-4-10', 5.15, 1.00);");
//			statement.executeUpdate("INSERT INTO Orders VALUES (2, 4, 2, 2, '2022-3-25', '2022-4-12', 5.15, 1.00);");
//			statement.executeUpdate("INSERT INTO Orders VALUES (3, 4, 1, 3, '2022-3-25', '2022-4-23', 5.15, 1.00);");
//			statement.executeUpdate("INSERT INTO Orders VALUES (4, 3, 1, 4, '2022-3-25', '2022-4-23', 5.15, 1.00);");
//			statement.executeUpdate("INSERT INTO Orders VALUES (5, 3, 4, 4, '2022-3-25', '2022-4-25', 5.15, 1.00);");
//			statement.executeUpdate("INSERT INTO Orders VALUES (6, 1, 3, 1, '2022-4-25', '2022-5-10', 15.15, 2.00);");
//			statement.executeUpdate("INSERT INTO Orders VALUES (7, 2, 2, 2, '2022-4-25', '2022-5-12', 15.15, 2.00);");
//			statement.executeUpdate("INSERT INTO Orders VALUES (8, 2, 1, 3, '2022-4-25', '2022-5-23', 15.15, 2.00);");
//			statement.executeUpdate("INSERT INTO Orders VALUES (9, 3, 4, 4, '2022-4-25', '2022-5-25', 15.15, 2.00);");
			statement.executeUpdate(
					"INSERT INTO Orders VALUES (4001, 2001, 1001, 30, '2020-01-02', '2020-01-15', 20, 30, 630);");
			statement.executeUpdate(
					"INSERT INTO Orders VALUES (4002, 2001, 1001, 10, '2020-02-05', '2020-02-15', 20, 15, 215);");
			statement.executeUpdate(
					"INSERT INTO Orders VALUES (4003, 2002, 1003, 10, '2020-02-10', '2020-02-25', 10, 15, 115);");

//			statement.executeUpdate("INSERT INTO Invoices VALUES (1, 1, 5.15, '2022-4-25', NULL);");
//			statement.executeUpdate("INSERT INTO Invoices VALUES (2, 4, 10.30, '2022-4-25', '2022-5-05');");
//			statement.executeUpdate("INSERT INTO Invoices VALUES (4, 3, 10.30, '2022-4-25', NULL);");
//			statement.executeUpdate("INSERT INTO Invoices VALUES (5, 1, 15.15, '2022-5-25', NULL);");
//			statement.executeUpdate("INSERT INTO Invoices VALUES (6, 2, 30.30, '2022-5-25', '2022-6-05');");
//			statement.executeUpdate("INSERT INTO Invoices VALUES (8, 3, 15.15, '2022-5-25', NULL);");
			statement.executeUpdate("INSERT INTO Invoices VALUES (5001, 2001, 630.00, '2020-02-01', '2020-02-05');");
			statement.executeUpdate("INSERT INTO Invoices VALUES (5002, 2002, 115.00, '2020-03-01', '2020-03-05');");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Close connection to database, close objects connection, statement, result
	 */
	public void close() {

		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (result != null) {
			try {
				result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
