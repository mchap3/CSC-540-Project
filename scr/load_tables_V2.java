// This example is created by Seokyong Hong
// modified by Shrikanth N C to support MySQL(MariaDB)

// Relpace all $USER$ with your unity id and $PASSWORD$ with your 9 digit student id or updated password (if changed)

import java.sql.*;

public class load_tables_V2 {


// Update your user info alone here
private static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/iarakel"; // Using SERVICE_NAME

// Update your user and password info here!

private static final String user = "iarakel";
private static final String password = "Cehtycehty00";

public static void main(String[] args) {
try {
// Loading the driver. This creates an instance of the driver
// and calls the registerDriver method to make MySql(MariaDB) Thin available to clients.


Class.forName("org.mariadb.jdbc.Driver");

Connection connection = null;
            Statement statement = null;
            ResultSet result = null;

            try {
            // Get a connection instance from the first driver in the
            // DriverManager list that recognizes the URL jdbcURL
            connection = DriverManager.getConnection(jdbcURL, user, password);

            // Create a statement instance that will be sending
            // your SQL statements to the DBMS
            statement = connection.createStatement();
		try {
			//statement.executeUpdate("DROP TABLE Distributors");                      
			statement.executeUpdate("DROP TABLE Invoices");
            statement.executeUpdate("DROP TABLE Orders");
            statement.executeUpdate("DROP TABLE Distributors");
            statement.executeUpdate("DROP TABLE Publication");			
		} catch (SQLException e) {
		}

            // Create Publication table
            String p1 = "CREATE TABLE Publication( PublicationID INTEGER not NULL," +
                "Title VARCHAR(128) NOT NULL,Type VARCHAR(128) NOT NULL," +
                "Topic VARCHAR(128) NOT NULL, PRIMARY KEY(PublicationID) )";
            statement.executeUpdate(p1);
            //Populate Publication
            statement.executeUpdate("INSERT INTO Publication VALUES (1, 'Don Quixote','Novel', 'Adventure')");
            statement.executeUpdate("INSERT INTO Publication VALUES (2, 'People ','Magazine', 'Celebrity News')");
            statement.executeUpdate("INSERT INTO Publication VALUES (3, 'People ','Magazine', 'Celebrity News')");
            statement.executeUpdate("INSERT INTO Publication VALUES (4, 'The Count of Monte Cristo','Novel', 'Adventure')");
           
            // Create Distributor tables
            String d1 = "CREATE TABLE Distributors (DistAccountNum INTEGER not NULL," + 
                " Name VARCHAR(128) not NULL, Type VARCHAR(128) not NULL," + 
                " Address VARCHAR(128) not NULL, City VARCHAR(128) not NULL," +
                " PhoneNumber VARCHAR(16) not NULL, Contact VARCHAR(128) not NULL,"+
                " Balance DECIMAL(8,2) not NULL, PRIMARY KEY(DistAccountNum) )";
            String d2 = "CREATE TABLE Orders (OrderID INTEGER not NULL," +
                " DistAccountNum INTEGER, PublicationID INTEGER not NULL," +
                " NumCopies INTEGER not NULL, ProduceByDate DATE not NULL," +
                " Price DECIMAL(8,2) not NULL, ShippingCosts DECIMAL(8,2) not NULL," + 
                " PRIMARY KEY(OrderID)," +
                " FOREIGN KEY (PublicationID) REFERENCES Publication(PublicationID) ON UPDATE CASCADE," +
                " FOREIGN KEY (DistAccountNum) REFERENCES Distributors(DistAccountNum) ON UPDATE CASCADE ON DELETE SET NULL )"; 
            String d3 = "CREATE TABLE Invoices (InvoiceID INTEGER not NULL,"+
                " DistAccountNum INTEGER, Amount DECIMAL(8,2) not NULL," + 
                " BillingDate DATE not NULL, PaymentDate DATE," +
                " PRIMARY KEY(InvoiceID), FOREIGN KEY (DistAccountNum) REFERENCES Distributors(DistAccountNum) ON DELETE SET NULL ON UPDATE CASCADE)";
            // execute
            statement.executeUpdate(d1);
            statement.executeUpdate(d2);
            statement.executeUpdate(d3);

            // check the foreign key status for "Orders"
            // copied from http://www.java2s.com/Code/Java/Database-SQL-JDBC/Createtablewithforeignkey.htm
            DatabaseMetaData dbmd = connection.getMetaData();  
            ResultSet rs = dbmd.getImportedKeys(null, null, "Orders");
            while (rs.next()) {
                String pkTable = rs.getString("PKTABLE_NAME"); // get the foreign key reference table
                short updateRule = rs.getShort("UPDATE_RULE"); // get the update rule 1 - default, 0 - CASCADE
                System.out.println("update rule:  " + updateRule);
                System.out.println("primary key table name :  " + pkTable);
            }
            rs.close();

            //Populate Distributors tables
            //Distributors
            statement.executeUpdate("INSERT INTO Distributors VALUES (1, 'Library1','Library', '100 New Street', 'Raleigh', '919-xxx-xxxx', 'John', 0)");
	        statement.executeUpdate("INSERT INTO Distributors VALUES (2, 'Library2','Library', '200 New Street', 'Durham','919-xxx-xxxx', 'John', 0.00)");
	        statement.executeUpdate("INSERT INTO Distributors VALUES (3, 'Books','Store', '300 New Street', 'Cary','919-xxx-xxxx', 'John', 0.00)");
	        statement.executeUpdate("INSERT INTO Distributors VALUES (4, 'BooksEtc','Store', '400 New Street', 'Durham','919-xxx-xxxx', 'John', 0.00)");
            // Orders
            statement.executeUpdate("INSERT INTO Orders VALUES (1, 1, 2, 1, '2022-5-10', 5.15, 1.00)");
            statement.executeUpdate("INSERT INTO Orders VALUES (2, 4, 2, 2, '2022-5-12', 5.15, 1.00)");
            statement.executeUpdate("INSERT INTO Orders VALUES (3, 4, 1, 3, '2022-5-23', 5.15, 1.00)");
            statement.executeUpdate("INSERT INTO Orders VALUES (4, 3, 4, 4, '2022-5-25', 5.15, 1.00)");
            //Invoices
	        statement.executeUpdate("INSERT INTO Invoices VALUES (1, 3, 5.15, '2022-5-25', NULL)");
	        statement.executeUpdate("INSERT INTO Invoices VALUES (2, 3, 5.15, '2022-5-25', '2022-8-25')");
	        statement.executeUpdate("INSERT INTO Invoices VALUES (3, 3, 5.15, '2022-5-25', '2022-8-25')");
	        statement.executeUpdate("INSERT INTO Invoices VALUES (4, 3, 5.15, '2022-5-25', NULL)");
       
            } finally {
                close(result);
                close(statement);
                close(connection);
            }
} catch(Throwable oops) {
            oops.printStackTrace();
        }
}
static void close(Connection connection) {
        if(connection != null) {
            try {
            connection.close();
            } catch(Throwable whatever) {}
        }
    }
    static void close(Statement statement) {
        if(statement != null) {
            try {
            statement.close();
            } catch(Throwable whatever) {}
        }
    }
    static void close(ResultSet result) {
        if(result != null) {
            try {
            result.close();
            } catch(Throwable whatever) {}
        }
    }
}