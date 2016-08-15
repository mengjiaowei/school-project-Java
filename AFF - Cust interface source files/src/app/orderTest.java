package app;

import java.sql.*;
import java.util.Scanner;

public class orderTest {
	static int custIDGet,orderIDGet,restID,dishNumber,qty,dishPrice;
		
		
	orderTest(App a){
		try
		{
			
			//creates new row in curtana.Customer using the customer information
			String CustomerQuery = "insert into Customer (CustomerName, CustomerPhone, CustomerEmail, CustomerAddress) values ('" + CheckoutFrame.namestr + "', '" + CheckoutFrame.phonestr + "', '" + CheckoutFrame.emailstr + "', '" + CheckoutFrame.addstr + "')";
			System.out.println(CustomerQuery);
			//executes, and pulls the auto-incremented 'custID'
			custIDGet = Database.statement.executeUpdate(CustomerQuery, Statement.RETURN_GENERATED_KEYS);
			Database.rs = Database.statement.getGeneratedKeys();
			if (Database.rs.next()){
				custIDGet = Database.rs.getInt(1);
			}
			
			//Uses custID to create a new row in curtana.Order
			String OrderQuery = "insert into curtana.Orders (CustomerID) value (" + custIDGet + ")";
			System.out.println(OrderQuery);
			//executes and pulls auto-incremented orderID
			orderIDGet = Database.statement.executeUpdate(OrderQuery, Statement.RETURN_GENERATED_KEYS);
			Database.rs = Database.statement.getGeneratedKeys();
			if (Database.rs.next()){
				orderIDGet = Database.rs.getInt(1);
			}
			System.out.println("OrderID is: " + orderIDGet);
			
			//Joins Customer and Order, get all info into program
			String joinQuery = "select * from curtana.Orders natural join curtana.Customer "
					+ "where curtana.Orders.OrderID = " + orderIDGet + " and curtana.Customer.CustomerID = " + custIDGet;
			Database.rs = Database.statement.executeQuery(joinQuery);
			while (Database.rs.next()){
				String stuff = Database.rs.getString(3) + ", " + Database.rs.getString(1) + ", " + Database.rs.getString(2) + ", " + Database.rs.getString(4) + ", " + Database.rs.getString(5) + ", " + Database.rs.getString(6) + ", " + Database.rs.getString(7);
				System.out.println(stuff);
			}
			
			//Scans RestaurantFrame.oCInfo, reads each dish's information and sends the appropriate number of OCQuery queries
			Scanner sc = new Scanner(RestaurantFrame.oCInfo);
				
			while (sc.hasNext()){
				
				dishNumber = sc.nextInt();
				dishPrice = sc.nextInt();
				qty = sc.nextInt();
				String OCQuery = "insert into OrderContents (OrderID, RestID, DishNumber, Quantity, DishPrice) values ('" 
						+ orderIDGet + "', '" + SearchFrame.restID + "', '" + dishNumber + "', '" + qty + "', '" + dishPrice + "')";
				Database.statement.executeUpdate(OCQuery);
				System.out.println(OCQuery);
			
				
			}
			
			
			Database.rs.close();
			Database.statement.close();
			
			
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
}
