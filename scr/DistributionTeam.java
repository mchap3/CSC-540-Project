import java.util.Scanner;

public class DistributionTeam {
	    /**
* choise menu for Distributor APIs
*/
	
	private DBManager db = null;
	private Scanner scanner = null;
	
	private Distribution distribution = null;
	
	public DistributionTeam(DBManager dbM, Scanner s) {
		db = dbM;
		scanner = s;
		
		distribution = new Distribution(db, scanner);
	}
	
private static void helper() {
		    /**
* print menu for Distributor APIs
* promt command input
*/
		System.out.printf("%40s\n", "DISTRIBUTION TEAM MENU");
		System.out.println("\nCommand Code | Command Description             | Arguments it needs");
		System.out.println("-------------|---------------------------------|-------------------");
		
		String[][] help = {	
		         { "  D1         | print distributor               | ", "Distributor ID" },
		         { "  D2         | add distributor                 | ", "Distributor ID, Name, Type, Street Address, City Address, Phone, Contact, Balance" },
		         { "  D3         | delete distributor              | ", "Distributor ID" },
		         { "  D4         | update distributor              | ", "Selection Attribute/Value, Update Attribute/Value" },
		         { "  D5         | update distributor balance      | ", "Distributor ID" },
		         { "  D6         | place publication order         | ", "Order ID, Dist ID, Pub ID, #Copies, Production Date, Price, Shipping" },
		         { "  logout     | return to user selection        | ", "" }
		      };
		
		
		for (int i = 0; i < help.length; i++) {
			System.out.println(help[i][0] + help[i][1]);
		}
		System.out.println();
	}
	
	public void command(String com) {
			    /**
* call API based on user prompt
*/
		System.out.println("Your Command: " + com + "\n");
		
		switch(com.toLowerCase()) {
		
		case "d1": distribution.printDistributor();
		break;
		
		case "d2": distribution.addDistributor();
			break;
		
		case "d3": distribution.delDistributor();
			break;
			
		case "d4": distribution.updateDistributor();
			break;
			
		case "d5": distribution.balanceDistributor();
			break;
			
		case "d6": distribution.placeOrder();
			break;
			
		default: 
//			System.out.println("Here are the Valid Command Codes, and their required information");
			helper();
				break;
		}
		
	}
}
