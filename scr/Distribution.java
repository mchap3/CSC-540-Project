import java.util.Scanner;

public class Distribution {
	
	private DBManager db = null;
	private Scanner scanner = null;
	
	private Distributors distributor = null;
	
	public Distribution(DBManager dbM, Scanner s) {
		db = dbM;
		scanner = s;
		
		distributor = new Distributors(db, scanner);
	}
	
private static void helper() {
		
		System.out.println("\nCommand Code | Command Description                   | Arguments it needs");
		System.out.println("-------------|---------------------------------------|-------------------");
		
		String[][] help = {	
		         { "  D1         | print distributor                     | ", "Distributor ID" },
		         { "  D2         | add distributor                       | ", "Distributor ID, Name, Type, Street Address, City Address, Phone, Contact, Balance" },
		         { "  D3         | delete distributor                    | ", "Distributor ID" },
		         { "  D4         | update distributor        	     | ", "Selection Attribute/Value, Update Attribute/Value" },
		         { "  D5         | update distributor balance            | ", "Distributor ID" },
		         { "  D6         | place publication order               | ", "Order ID, Dist ID, Pub ID, #Copies, Production Date, Price, Shipping" },
		         { "  D7         | bill distributor (new invoice)        | ", "Distributor ID, invoice year-month" },
		         { "  D8         | update invoice payment status         | ", "Invoice ID, payment date" }		         
		      };
		
		
		for (int i = 0; i < 8; i++) {
			System.out.println(help[i][0] + help[i][1]);
		}
		System.out.println();
	}
	
	public void command(String com) {
		System.out.println("Your Command: " + com + "\n");
		
		switch(com.toLowerCase()) {
		
		case "d2": distributor.addDistributor();
			break;
		
		case "d3": distributor.delDistributor();
			break;
			
		case "d1": distributor.printDistributor();
			break;
			
		case "d4": distributor.updateDistributor();
			break;
			
		case "d5": distributor.balanceDistributor();
			break;
			
		case "d6": distributor.placeOrder();
			break;
			
		case "d7": distributor.newInvoice();
			break;
			
		case "d8": distributor.paymentInvoice();
			break;		
			
		default: System.out.println("Here are the Valid Command Codes, and their required information");
			helper();
				break;
		}
		
	}
}
