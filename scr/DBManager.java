import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//javac database_setup.java DBManager.java DBTablePrinter.java Reports.java User.java
//java database_setup
public class DBManager {

	static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/cpatel3";
	static final String user = "cpatel3";
	static final String password = "200048024";

	private Connection connection = null;
	private Statement statement = null;
	private ResultSet result = null;

	public DBManager() {
		connectToDatabase();
		createTables();
		populateTables();
	}

	public Connection getConn() {
		return connection;
	}

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

	public ResultSet getResult() {
		return result;
	}

	public void disableAutocommit() {
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void enableAutocommit() {
		try {
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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

	public void update(String s) {
		try {
			createStatement();
			statement.executeUpdate(s);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	public boolean commit() {
		
		try {
			connection.commit();
			
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			try {
				if (connection != null)
					System.out.println("Error Generating Report, Please Check Format");
				connection.rollback();
				return false;
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		
		return false;
	}

	private void connectToDatabase() {

		try {

			Class.forName("org.mariadb.jdbc.Driver");

			connection = DriverManager.getConnection(jdbcURL, user, password);
			statement = connection.createStatement();

			statement.executeUpdate("SET FOREIGN_KEY_CHECKS=0;");
			statement.executeUpdate("DROP TABLE WritesBook");
			statement.executeUpdate("DROP TABLE WritesArticle");
			statement.executeUpdate("DROP TABLE Edits");
			statement.executeUpdate("DROP TABLE Articles");
			statement.executeUpdate("DROP TABLE Chapters");
			statement.executeUpdate("DROP TABLE Issues");
			statement.executeUpdate("DROP TABLE Books");
			statement.executeUpdate("DROP TABLE Editors");
			statement.executeUpdate("DROP TABLE Authors");
			statement.executeUpdate("DROP TABLE IF EXISTS Payments");
			statement.executeUpdate("DROP TABLE Employees");
			statement.executeUpdate("DROP TABLE Invoices");
			statement.executeUpdate("DROP TABLE Orders");
			statement.executeUpdate("DROP TABLE Distributors");
			statement.executeUpdate("DROP TABLE Publication");
			statement.executeUpdate("SET FOREIGN_KEY_CHECKS=1;");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createTables() {
		try {
			statement.executeUpdate(
                    "CREATE TABLE Publication(PublicationID INT AUTO_INCREMENT, "
                    + "Title VARCHAR(128) NOT NULL, "
                    + "Type VARCHAR(128) NOT NULL, "
                    + "Topic VARCHAR(128) NOT NULL, "
                    + "PRIMARY KEY (PublicationID));");
            statement.executeUpdate(
                    "CREATE TABLE Issues(PublicationID INT, "
                    + "IssueTitle VARCHAR(128) NOT NULL, "
                    + "IssueDate DATE NOT NULL, "
                    + "PRIMARY KEY (PublicationID), "
                    + "FOREIGN KEY (PublicationID) REFERENCES Publication(PublicationID) "
                    + "ON UPDATE CASCADE ON DELETE CASCADE);");
            statement.executeUpdate(
                    "CREATE TABLE Articles(PublicationID INT, "
                    + "ArticleTitle VARCHAR(128) NOT NULL, "
                    + "ArticleTopic VARCHAR(128) NOT NULL, "
                    + "ArticleText VARCHAR(128), "
                    + "PRIMARY KEY (PublicationID, ArticleTitle), "
                    + "FOREIGN KEY (PublicationID) REFERENCES Issues(PublicationID) "
                    + "ON UPDATE CASCADE ON DELETE CASCADE);");
            statement.executeUpdate(
                    "CREATE TABLE Books(PublicationID INT, "
                    + "EditionNumber VARCHAR(16), "
                    + "ISBN VARCHAR(32), "
                    + "PublicationDate DATE NOT NULL, "
                    + "PRIMARY KEY (PublicationID), "
                    + "UNIQUE(ISBN), "
                    + "FOREIGN KEY (PublicationID) REFERENCES Publication(PublicationID) "
                    + "ON UPDATE CASCADE ON DELETE CASCADE);");
            statement.executeUpdate(
                    "CREATE TABLE Chapters(PublicationID INT, "
                    + "ChapterTitle VARCHAR(128), "
                    + "PRIMARY KEY (PublicationID, ChapterTitle), "
                    + "FOREIGN KEY (PublicationID) REFERENCES Books(PublicationID) "
                    + "ON UPDATE CASCADE ON DELETE CASCADE);");
            statement.executeUpdate(
                    "CREATE TABLE Employees(EmpID INT AUTO_INCREMENT, "
                    + "Name VARCHAR(128) NOT NULL, "
                    + "Type VARCHAR(10) NOT NULL, "
                    + "Active BOOLEAN NOT NULL, "
                    + "PRIMARY KEY (EmpID));");
            statement.executeUpdate(
                    "CREATE TABLE Editors(EmpID INT, "
                    + "PRIMARY KEY (EmpID), "
                    + "FOREIGN KEY (EmpID) REFERENCES Employees(EmpID) "
                    + "ON UPDATE CASCADE ON DELETE CASCADE);");
            statement.executeUpdate(
                    "CREATE TABLE Authors(EmpID INT, "
                    + "PRIMARY KEY (EmpID), "
                    + "FOREIGN KEY (EmpID) REFERENCES Employees(EmpID) "
                    + "ON UPDATE CASCADE ON DELETE CASCADE);");
            statement.executeUpdate(
                    "CREATE TABLE Edits(PublicationID INT, "
                    + "EmpID INT, "
                    + "PRIMARY KEY (PublicationID, EmpID), "
                    + "FOREIGN KEY (PublicationID) REFERENCES Publication(PublicationID) "
                    + "ON UPDATE CASCADE ON DELETE CASCADE, "
                    + "FOREIGN KEY (EmpID) REFERENCES Editors(EmpID) "
                    + "ON UPDATE CASCADE ON DELETE CASCADE);");
            statement.executeUpdate(
                    "CREATE TABLE WritesBook(PublicationID INT, "
                    + "EmpID INT, "
                    + "PRIMARY KEY (PublicationID, EmpID), "
                    + "FOREIGN KEY (PublicationID) REFERENCES Books(PublicationID) "
                    + "ON UPDATE CASCADE ON DELETE CASCADE, "
                    + "FOREIGN KEY (EmpID) REFERENCES Authors(EmpID) "
                    + "ON UPDATE CASCADE ON DELETE CASCADE);");
            statement.executeUpdate(
                    "CREATE TABLE WritesArticle(PublicationID INT, "
                    + "ArticleTitle VARCHAR(128), "
                    + "EmpID INT, "
                    + "PRIMARY KEY (PublicationID, ArticleTitle, EmpID), "
                    + "FOREIGN KEY (PublicationID, ArticleTitle) REFERENCES Articles(PublicationID, ArticleTitle) "
                    + "ON UPDATE CASCADE ON DELETE CASCADE, "
                    + "FOREIGN KEY (EmpID) REFERENCES Authors(EmpID) "
                    + "ON UPDATE CASCADE ON DELETE CASCADE);");
            statement.executeUpdate(
                    "CREATE TABLE Payments(CheckNumber INT AUTO_INCREMENT, "
                    + "EmpID INT, "
                    + "Amount DECIMAL(8,2) NOT NULL, "
                    + "SubmitDate DATE NOT NULL, "
                    + "ClaimDate DATE, "
                    + "PRIMARY KEY (CheckNumber), "
                    + "FOREIGN KEY (EmpID) REFERENCES Employees(EmpID) "
                    + "ON UPDATE CASCADE ON DELETE SET NULL);");
            statement.executeUpdate(
                    "CREATE TABLE Distributors (DistAccountNum INT AUTO_INCREMENT, "
                    + "Name VARCHAR(128) NOT NULL, "
                    + "Type VARCHAR(128) NOT NULL, "
                    + "Address VARCHAR(128) NOT NULL, "
                    + "City VARCHAR(128) NOT NULL, "
                    + "PhoneNumber VARCHAR(16) NOT NULL, "
                    + "Contact VARCHAR(128) NOT NULL, "
                    + "Balance DECIMAL(8,2) NOT NULL, "
                    + "PRIMARY KEY (DistAccountNum));");
            statement.executeUpdate(
                    "CREATE TABLE Orders (OrderID INT AUTO_INCREMENT, "
                    + "DistAccountNum INT, "
                    + "PublicationID INT NOT NULL, "
                    + "NumCopies INT NOT NULL, "
                    + "ProduceByDate DATE NOT NULL, "
                    + "Price DECIMAL(8,2) NOT NULL, "
                    + "ShippingCosts DECIMAL(8,2) NOT NULL, "
                    + "PRIMARY KEY (OrderID), "
                    + "FOREIGN KEY (DistAccountNum) REFERENCES Distributors(DistAccountNum) "
                    + "ON UPDATE CASCADE ON DELETE SET NULL, "
                    + "FOREIGN KEY (PublicationID) REFERENCES Publication(PublicationID) "
                    + "ON UPDATE CASCADE);");
            statement.executeUpdate(
                    "CREATE TABLE Invoices (InvoiceID INT AUTO_INCREMENT, "
                    + "DistAccountNum INT, "
                    + "Amount DECIMAL(8,2) NOT NULL, "
                    + "BillingDate DATE NOT NULL, "
                    + "PaymentDate DATE, "
                    + "PRIMARY KEY (InvoiceID), "
                    + "FOREIGN KEY (DistAccountNum) REFERENCES Distributors(DistAccountNum) "
                    + "ON UPDATE CASCADE ON DELETE SET NULL);");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void populateTables() {
		try {
			// Populate Publication Tables
			statement.executeUpdate("INSERT INTO Publication VALUES (NULL, 'Don Quixote', 'Book', 'Adventure');");
            statement.executeUpdate("INSERT INTO Publication VALUES (NULL, 'People', 'Magazine', 'Celebrity News');");
            statement.executeUpdate("INSERT INTO Publication VALUES (NULL, 'People', 'Magazine', 'Celebrity News');");
            statement.executeUpdate("INSERT INTO Publication VALUES (NULL, 'The Count of Monte Cristo', 'Book', 'Adventure');");
            statement.executeUpdate("INSERT INTO Publication VALUES (NULL, 'Science', 'Journal', 'Science');");
            statement.executeUpdate("INSERT INTO Publication VALUES (NULL, 'Science', 'Journal', 'Science');");
            statement.executeUpdate("INSERT INTO Publication VALUES (NULL, 'Database Systems', 'Book', 'Databases');");
            statement.executeUpdate("INSERT INTO Publication VALUES (NULL, 'Database Systems', 'Book', 'Databases');");

			statement.executeUpdate("INSERT INTO Issues VALUES (2, 'February-21', '2022-02-21');");
			statement.executeUpdate("INSERT INTO Issues VALUES (3, 'February-14', '2022-02-14');");
			statement.executeUpdate("INSERT INTO Issues VALUES (5, 'Volume 375 Issue 6584', '2022-03-04');");
			statement.executeUpdate("INSERT INTO Issues VALUES (6, 'Volume 375 Issue 6583', '2022-02-25');");

			statement.executeUpdate("INSERT INTO Books VALUES (1, '1', '0142437239', '2003-02-25');");
			statement.executeUpdate("INSERT INTO Books VALUES (4, '1', '0140449264', '2003-05-27');");
			statement.executeUpdate("INSERT INTO Books VALUES (7, '1', '0167120441', '2022-01-31');");
			statement.executeUpdate("INSERT INTO Books VALUES (8, '2', '0335009913', '2022-02-26');");

			statement.executeUpdate(
					"INSERT INTO Articles VALUES (2, 'Title1', 'Celebrity News', '/Issues/1/February-21/Title1.pdf');");
			statement.executeUpdate(
					"INSERT INTO Articles VALUES (2, 'Title2', 'Stories', '/Issues/1/February-21/Title2.pdf');");
			statement.executeUpdate(
					"INSERT INTO Articles VALUES (3, 'Title1', 'Stories', '/Issues/1/February-14/Title1.pdf');");
			statement.executeUpdate(
					"INSERT INTO Articles VALUES (6, 'Structure of Omicron', 'Coronavirus', 'structure_of_omicron.txt');");
			statement.executeUpdate(
					"INSERT INTO Articles VALUES (6, 'Fly Cell Atlas', 'Genetics', 'fly_cell_atlas.txt');");

			statement.executeUpdate("INSERT INTO Chapters VALUES (1, 'Chapter 1');");
			statement.executeUpdate("INSERT INTO Chapters VALUES (1, 'Chapter 2');");
			statement.executeUpdate("INSERT INTO Chapters VALUES (4, 'Chapter 1');");
			statement.executeUpdate("INSERT INTO Chapters VALUES (7, 'DBMS Overview');");
			statement.executeUpdate("INSERT INTO Chapters VALUES (7, 'The Relational Model');");
			statement.executeUpdate("INSERT INTO Chapters VALUES (8, 'DBMS Overview and More');");
			statement.executeUpdate("INSERT INTO Chapters VALUES (8, 'The Relational Model - Expanded');");

			// Populate Staff Tables
			statement.executeUpdate("INSERT INTO Employees VALUES (1, 'Sam T', 'Staff', TRUE);");
			statement.executeUpdate("INSERT INTO Employees VALUES (2, 'Mary S', 'Staff', TRUE);");
			statement.executeUpdate("INSERT INTO Employees VALUES (3, 'John D', 'Invited', TRUE);");
			statement.executeUpdate("INSERT INTO Employees VALUES (4, 'Pam L', 'Invited', TRUE);");
			statement.executeUpdate("INSERT INTO Employees VALUES (5, 'Don D', 'Staff', TRUE);");
			statement.executeUpdate("INSERT INTO Employees VALUES (6, 'Tom A', 'Invited', TRUE);");
			statement.executeUpdate("INSERT INTO Employees VALUES (7, 'Jan P', 'Staff', FALSE);");
			statement.executeUpdate("INSERT INTO Employees VALUES (8, 'Nina T', 'Invited', TRUE);");

			statement.executeUpdate("INSERT INTO Authors VALUES (1);");
			statement.executeUpdate("INSERT INTO Authors VALUES (2);");
			statement.executeUpdate("INSERT INTO Authors VALUES (3);");
			statement.executeUpdate("INSERT INTO Authors VALUES (4);");

			statement.executeUpdate("INSERT INTO Editors VALUES (5);");
			statement.executeUpdate("INSERT INTO Editors VALUES (6);");
			statement.executeUpdate("INSERT INTO Editors VALUES (7);");
			statement.executeUpdate("INSERT INTO Editors VALUES (8);");

			statement.executeUpdate("INSERT INTO Edits VALUES (1, 5);");
			statement.executeUpdate("INSERT INTO Edits VALUES (2, 6);");
			statement.executeUpdate("INSERT INTO Edits VALUES (5, 7);");
			statement.executeUpdate("INSERT INTO Edits VALUES (8, 8);");

			statement.executeUpdate("INSERT INTO WritesBook VALUES (7, 1);");
			statement.executeUpdate("INSERT INTO WritesBook VALUES (7, 4);");
			statement.executeUpdate("INSERT INTO WritesBook VALUES (8, 1);");
			statement.executeUpdate("INSERT INTO WritesBook VALUES (8, 2);");
			statement.executeUpdate("INSERT INTO WritesBook VALUES (8, 4);");

			statement.executeUpdate("INSERT INTO WritesArticle VALUES (2, 'Title1', 1);");
			statement.executeUpdate("INSERT INTO WritesArticle VALUES (2, 'Title2', 4);");
			statement.executeUpdate("INSERT INTO WritesArticle VALUES (6, 'Structure of Omicron', 3);");
			statement.executeUpdate("INSERT INTO WritesArticle VALUES (6, 'Fly Cell Atlas', 2);");

			statement.executeUpdate("INSERT INTO Payments VALUES (1001, 1, 1000.00, '2022-01-31', '2022-02-05');");
			statement.executeUpdate("INSERT INTO Payments VALUES (1002, 2, 1200.00, '2022-01-31', '2022-02-08');");
			statement.executeUpdate("INSERT INTO Payments VALUES (1003, 4, 800.00, '2022-01-31', '2022-02-10');");
			statement.executeUpdate("INSERT INTO Payments VALUES (1004, 1, 100.00, '2022-02-28', NULL);");
			statement.executeUpdate("INSERT INTO Payments VALUES (1005, 2, 1200.00, '2022-02-28', NULL);");
			statement.executeUpdate("INSERT INTO Payments VALUES (1006, 4, 800.00, '2022-02-28', NULL);");
			statement.executeUpdate("INSERT INTO Payments VALUES (1007, 5, 100.00, '2022-02-28', NULL);");
			statement.executeUpdate("INSERT INTO Payments VALUES (1008, 6, 1100.00, '2022-02-28', NULL);");
			statement.executeUpdate("INSERT INTO Payments VALUES (1009, 7, 700.00, '2022-02-28', NULL);");

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
			statement.executeUpdate(
					"INSERT INTO Distributors VALUES (1, 'Library1','Library', '100 New Street', 'Raleigh', '919-xxx-xxxx', 'John', 0)");
			statement.executeUpdate(
					"INSERT INTO Distributors VALUES (2, 'Library2','Library', '200 New Street', 'Durham','919-xxx-xxxx', 'John', 0.00)");
			statement.executeUpdate(
					"INSERT INTO Distributors VALUES (3, 'Books','Store', '300 New Street', 'Cary','919-xxx-xxxx', 'John', 0.00)");
			statement.executeUpdate(
					"INSERT INTO Distributors VALUES (4, 'BooksEtc','Store', '400 New Street', 'Durham','919-xxx-xxxx', 'John', 0.00)");

			statement.executeUpdate("INSERT INTO Orders VALUES (1, 1, 3, 1, '2022-4-10', 5.15, 1.00);");
			statement.executeUpdate("INSERT INTO Orders VALUES (2, 4, 2, 2, '2022-4-12', 5.15, 1.00);");
			statement.executeUpdate("INSERT INTO Orders VALUES (3, 4, 1, 3, '2022-4-23', 5.15, 1.00);");
			statement.executeUpdate("INSERT INTO Orders VALUES (4, 3, 1, 4, '2022-4-23', 5.15, 1.00);");
			statement.executeUpdate("INSERT INTO Orders VALUES (5, 3, 4, 4, '2022-4-25', 5.15, 1.00);");
			statement.executeUpdate("INSERT INTO Orders VALUES (6, 1, 3, 1, '2022-5-10', 15.15, 2.00);");
			statement.executeUpdate("INSERT INTO Orders VALUES (7, 2, 2, 2, '2022-5-12', 15.15, 2.00);");
			statement.executeUpdate("INSERT INTO Orders VALUES (8, 2, 1, 3, '2022-5-23', 15.15, 2.00);");
			statement.executeUpdate("INSERT INTO Orders VALUES (9, 3, 4, 4, '2022-5-25', 15.15, 2.00);");

			statement.executeUpdate("INSERT INTO Invoices VALUES (1, 1, 5.15, '2022-4-25', NULL);");
			statement.executeUpdate("INSERT INTO Invoices VALUES (2, 4, 10.30, '2022-4-25', '2022-5-05');");
			// statement.executeUpdate("INSERT INTO Invoices VALUES (3, 4, 5.15,
			// '2022-4-25', '2022-5-05');");
			statement.executeUpdate("INSERT INTO Invoices VALUES (4, 3, 10.30, '2022-4-25', NULL);");
			statement.executeUpdate("INSERT INTO Invoices VALUES (5, 1, 15.15, '2022-5-25', NULL);");
			statement.executeUpdate("INSERT INTO Invoices VALUES (6, 2, 30.30, '2022-5-25', '2022-6-05');");
			// statement.executeUpdate("INSERT INTO Invoices VALUES (7, 2, 5.15,
			// '2022-5-25', '2022-6-05');");
			statement.executeUpdate("INSERT INTO Invoices VALUES (8, 3, 15.15, '2022-5-25', NULL);");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

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
