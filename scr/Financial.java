import java.util.Scanner;

public class Financial {
	
	private DBManager db = null;
	private Scanner scanner = null;
	
	private Publishers publisher = null;
	private Production production = null;
	private Distributors distributor = null;
	private Reports report = null;
	
	public Financial(DBManager dbM, Scanner s) {
		db = dbM;
		scanner = s;
		
		publisher = new Publishers(db, scanner);
		production = new Production(db, scanner);
		distributor = new Distributors(db, scanner);
		report = new Reports(db, scanner);
	}
	
private static void helper() {
		
		System.out.println("\nCommand Code | Command Description                                 | Arguments it needs");
		System.out.println("-------------|-----------------------------------------------------|-------------------");
		
		String[][] help = {	
		         { "  R1         | generate report                                     | ", "Start Date, End Date" },
		         { "  R2         | total publications sold                             | ", "Publication ID, Start Date, End Date" },
		         { "  R3         | total publication revenue                           | ", "Start Date, End Date" },
		         { "  R4         | total expenses                                      | ", "Start Date, End Date" },
		         { "  R5         | total distributors                                  | ", "None" },
		         { "  R6         | total city revenue                                  | ", "City Name" },
		         { "  R7         | total distributor revenue                           | ", "Distributor Account Number" },
		         { "  R8         | total address revenue                               | ", "Address" },
		         { "  R9         | total payments dy time and type of employee         | ", "Start Date, End Date" },
		         { "  R10        | total payments by type of employee and work type    | ", "Start Date, End Date" },
		         { "  R11        | List of all Distributors                            | ", "None" },
		         { "  R12        | List of all Employees                               | ", "None" }
		      };
		
		
		for (int i = 0; i < 12; i++) {
			System.out.println(help[i][0] + help[i][1]);
		}
		System.out.println();
	}
	
	public void command(String com) {
		System.out.println("Your Command: " + com + "\n");
		
		switch(com.toLowerCase()) {
		
		case "r1": report.generateReport();
			break;
		
		case "r2": report.totalPublicationsSold();
			break;
			
		case "r3": report.totalRevenue();
			break;
			
		case "rR4": report.totalExpenses();
			break;
			
		case "r5": report.totalDistributors();
			break;
			
		case "r6": report.totalCityRevenue();
			break;
			
		case "r7": report.totalDistributorRevenue();
			break;
			
		case "r8": report.totalAddressRevenue();
			break;
			
		case "r9": report.totalPaymentsByTimeAndTypeofEmployee();
			break;
			
		case "r10": report.totalPaymentsByTypeOfEmployeeAndWorkType();
			break;
			
		case "r11": report.listOfAllDistributors();
			break;
			
		case "r12": report.listOfAllEmployees();
			break;
			
		default: System.out.println("Here are the Valid Command Codes, and their required information");
			helper();
				break;
		}
		
	}
}
