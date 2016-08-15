package app;

import java.sql.Statement;

public class App {
	Statement database;

	SelectFrame selectFrame;
	SearchFrame searchFrame;
	RestaurantFrame restaurantFrame;
	CheckoutFrame checkoutFrame;
	orderTest orderTest;

	public static void main(String[] args) throws Exception {
		new App();
	}

	App() throws Exception{
		database = new Database().statement;
		selectFrame = new SelectFrame(this);
	}

}
