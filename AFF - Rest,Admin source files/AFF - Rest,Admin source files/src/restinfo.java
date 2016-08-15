import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class restinfo implements ActionListener {
	JLabel restName, restAddress, restPhone, restPostnr, restCity, restArea, edit, restOpen;
	public JTextField insertName,insertAddr, insertPhone, insertPostnr, insertCity, insertArea, insertOpen;
	JPanel rI, s, ca;
	JButton save, cancel;
	PassWordDialog pwd;
	JCheckBox box;
	JOptionPane optionPane;
	public String str, str2, str3, str4, str5, str6, str7, upStr1, upStr2, upStr3, upStr4, upStr5, upStr6, upStr7;
	Statement alpha = null;
	ResultSet rs = null;
	String abo3;
	public restinfo(String abo3){
		this.abo3 = abo3;
		getData();
		// System.out.println("bartha" + abo3);
		
		rI = new JPanel();
		rI.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
				
		restName = new JLabel("Restaurant name:");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(10,10,10,10);
		rI.add(restName, c);
		insertName = new JTextField(20);
		c.gridx = 2;
		c.gridy = 2;
		rI.add(insertName, c);
		insertName.setText(str);
		
		restPhone = new JLabel("Phone number:");
		c.gridx = 1;
		c.gridy = 3;
		rI.add(restPhone, c);
		insertPhone = new JTextField(str3);
		c.gridx =2;
		c.gridy = 3;
		rI.add(insertPhone, c);
		rI.revalidate();
		rI.repaint();
		rI.setVisible(true);
		
		restAddress = new JLabel("Address:");
		c.gridx = 1;
		c.gridy = 4;
		rI.add(restAddress, c);
		insertAddr = new JTextField(str5);
		c.gridx =2;
		c.gridy = 4;
		rI.add(insertAddr, c);
		
		restPostnr = new JLabel("Postal number:");
		c.gridx = 1;
		c.gridy = 5;
		rI.add(restPostnr, c);
		insertPostnr = new JTextField(str6);
		
		c.gridx =2;
		c.gridy = 5;
		rI.add(insertPostnr, c);
		
		restCity = new JLabel("City:");
		c.gridx = 1;
		c.gridy = 6;
		rI.add(restCity, c);
		insertCity = new JTextField(str7);
		c.gridx =2;
		c.gridy = 6;
		rI.add(insertCity, c);
		
		restArea = new JLabel("Area:");
		c.gridx = 1;
		c.gridy = 7;
		rI.add(restArea, c);
		insertArea = new JTextField(str2);
		c.gridx =2;
		c.gridy = 7;
		rI.add(insertArea, c);
		
		restOpen = new JLabel("Opening hours:");
		c.gridx = 1;
		c.gridy = 8;
		rI.add(restOpen, c);
		insertOpen = new JTextField(str4);
		c.gridx =2;
		c.gridy = 8;
		rI.add(insertOpen, c);
		
		s = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		save = new JButton("Save");
		s.add(save);
		c.gridx = 2;
		c.gridy = 9;
		rI.add(s, c);
		
		save.addActionListener(this);
				
		ca = new JPanel(new FlowLayout(FlowLayout.LEFT));
		cancel = new JButton("Cancel");
		c.gridx = 1;
		c.gridy = 9;
		cancel.addActionListener(this);
		ca.add(cancel);
		rI.add(ca, c);
		
		rI.revalidate();
		rI.repaint();
		rI.setVisible(true);
		
		
	}
	public JPanel restinfo() {
		return rI;
	}
	private void getData() { //retrieve information about the restaurant from the database
		
			try 
			{
				Class.forName("com.mysql.jdbc.Driver");
			
			
			Connection mysqlCon = DriverManager.getConnection("jdbc:mysql://90.231.28.63:3306/curtana?" + "user=Magnus&" + "password=j3dd4EGMXRTA9Zvu");
				
			alpha = mysqlCon.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE);
			alpha.executeQuery("SELECT * FROM restinfo where RestID =" +abo3); //restid should change depending on future login function
			//System.out.println("jhejhejhej");
			//System.out.println(abo3);
			rs = alpha.getResultSet();
			
			rs.next();
			{
				
				str = rs.getString("RestName");
				str2 = rs.getString("Area");
				str3 = rs.getString("PhoneNumber");
				str4 = rs.getString("OpeningHours");
				str5 = rs.getString("StreetAddress");
				str6 = rs.getString("PostalNumber");
				str7 = rs.getString("City");
				
				
			}
			
			}
			catch (Exception e) 
			{
			System.out.println(e);
			}
		
		}
	@SuppressWarnings("static-access")
	@Override
	public void actionPerformed(ActionEvent arg0) { //updating restaurant info in the database
		if(arg0.getActionCommand() == "Save"){
		 optionPane = new JOptionPane();
		
		 int selectedOption = optionPane.showConfirmDialog(box, "Are you sure?" ,"Confirm changes", JOptionPane.YES_NO_OPTION);
		
		upStr1 = insertName.getText();
		upStr2 = insertAddr.getText();
		upStr3 = insertPhone.getText();
		upStr4 = insertPostnr.getText();
		upStr5 = insertCity.getText();
		upStr6 = insertArea.getText();
		upStr7 = insertOpen.getText();
		if (selectedOption == JOptionPane.YES_OPTION) {
		  
		
		try {
			
			getData();
			rs.updateString("RestName", upStr1);			
			rs.updateString("PhoneNumber", upStr3);
			rs.updateString("OpeningHours", upStr7);
			rs.updateString("StreetAddress", upStr2);
			rs.updateString("PostalNumber", upStr4);
			rs.updateString("City", upStr5);
			rs.updateString("Area", upStr6);
			rs.updateRow();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} }
		else if (selectedOption == JOptionPane.NO_OPTION) { // should reset the restaurant info, not working.
			
			insertName.setText(str);
			insertAddr.setText(str5);
			insertPhone.setText(str3);
			insertPostnr.setText(str6);
			insertCity.setText(str7);
			insertArea.setText(str2);
			insertOpen.setText(str4);
		}}
		else if(arg0.getActionCommand() == "Cancel"){
			//System.out.println("hejkorv");
			insertName.setText(str);
			insertAddr.setText(str5);
			insertPhone.setText(str3);
			insertPostnr.setText(str6);
			insertCity.setText(str7);
			insertArea.setText(str2);
			insertOpen.setText(str4);
			}
		
		
		
	}
}


