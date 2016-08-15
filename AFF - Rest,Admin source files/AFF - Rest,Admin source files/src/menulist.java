import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;



public class menulist extends JTable implements ActionListener {


	private JPanel panel, mainpanel;
	private JScrollPane scrollPane;
	private DefaultTableModel dataModel;
	private Statement beta;
	private ResultSet rs;
	private JCheckBox box;
	private JTable table2;
	private String abo3;
	private JOptionPane optionPane2;
	public menulist(String abo3) {
		this.abo3 = abo3;
		
		dataModel = new DefaultTableModel();
		
			
		
		
		panel = new JPanel();
		table2 = new JTable(dataModel){  
			public Component prepareRenderer(TableCellRenderer renderer,int row, int col) {
			  Component comp = super.prepareRenderer(renderer, row, col);
			  JComponent jcomp = (JComponent)comp;
			  if (comp == jcomp) {
			  jcomp.setToolTipText((String)getValueAt(row, col));
			  }
			  return comp;
			  }
			  };
			
		
		scrollPane = new JScrollPane(panel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    // System.out.println("2");
	    panel.add(table2);
	    
	    mainpanel = new JPanel();
	    
	    mainpanel.add(scrollPane);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBackground(Color.WHITE);
		
		panel.add(table2);
		JPanel buttons = new JPanel(new FlowLayout());
		JButton knapp = new JButton("Add dish");
		buttons.add(knapp);
		JButton knapp3 = new JButton("Delete dish");
		buttons.add(knapp3);
		knapp3.addActionListener(this);
		JButton knapp2 = new JButton("Update menu");
		buttons.add(knapp2);
		knapp2.addActionListener(this);
		knapp.setAlignmentX(JPanel.RIGHT_ALIGNMENT);
		knapp.addActionListener(this);
		panel.add(buttons);
		
		menuList();
	}
		

		
	
	
	
	
	

	public JScrollPane getPanel() { 
		// System.out.println("3");
	return scrollPane;
	
		
	}
	
			
	
	@Override
	public void actionPerformed(ActionEvent e) { // set the class adder visible when pressing the add dishes button
		switch(e.getActionCommand()) {
		case "Add dish":
			
			dataModel.addRow(new Vector());
			// System.out.println("hej");
		break;
		
				
		
	
		case "Update menu": //poor way of adding dishes to the menu in database
			
		try {
				table2.getCellEditor().stopCellEditing();
		}
		catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.println("Not in focus");
		
		}
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection mysqlCon = DriverManager.getConnection("jdbc:mysql://90.231.28.63:3306/curtana?" + "user=Magnus&" + "password=j3dd4EGMXRTA9Zvu");
				
			
					beta.executeUpdate("DELETE FROM " + "Menu where RestID ="+ abo3 + ";");
					
					for(int i = 1; i< dataModel.getRowCount(); i++) {
						String temp = "";
						for (int j = 0; j < dataModel.getColumnCount()-1; j++) {
							temp += "'" + dataModel.getValueAt(i, j) + "',";
							// System.out.println(dataModel.getValueAt(i, j) + "" + i + j);
							
						}
						
						temp += "'" + dataModel.getValueAt(i, dataModel.getColumnCount()-1) + "'";
						// System.out.println("INSERT INTO " + "Menu" + " VALUES (" + temp + ");");
						beta.executeUpdate("INSERT INTO " + "Menu" + " VALUES (" + "'"+abo3+"'," + temp + ");");
						
					}
					optionPane2 = new JOptionPane();
					JOptionPane.showMessageDialog(box,
						    "Update successful.",
						    "Update",
						    JOptionPane.INFORMATION_MESSAGE);
					
				} catch (SQLException k) {
					optionPane2 = new JOptionPane();
					JOptionPane.showMessageDialog(box,
						    "Update failed.",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					k.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				
				}
			try {
				beta.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			menuList();
		
			break;
		case "Delete dish":	
		JOptionPane optionPane = new JOptionPane();
		// System.out.println("ss");
			String dialog = "Enter dish number";
			//JTextField textField = new JTextField();
			
				
			Object[] array = {dialog};
			
			String insert = JOptionPane.showInputDialog(array);
			
			
			for(int i = 0; i < dataModel.getRowCount(); i++){
				// System.out.println(dataModel.getValueAt(i, 0));
							if(dataModel.getValueAt(i, 0).equals(insert)) 
							{
								
								dataModel.removeRow(i);}
			
			}
			
	
			break;
		}
		}
	
		  
		
	private void menuList() {
		
		try 
		{
			
		Class.forName("com.mysql.jdbc.Driver");
		
		Connection mysqlCon = DriverManager.getConnection("jdbc:mysql://90.231.28.63:3306/curtana?" + "user=Magnus&" + "password=j3dd4EGMXRTA9Zvu");
		beta = mysqlCon.createStatement();
		beta.executeQuery("SELECT DishNumber, DishName, ingredients, DishPrice FROM Menu WHERE RestID =" + abo3);
		// System.out.println(abo3);
		rs = beta.getResultSet();
	
		
		ResultSetMetaData resultSetMetaData = rs.getMetaData();
		int colCount = resultSetMetaData.getColumnCount();
		String[] columnNames = new String[colCount];
		// System.out.println("10");
		for (int i = 0; i <= colCount - 1; i++)
		{
			columnNames[i] = resultSetMetaData.getColumnName(i+1);		
			
		}
		dataModel.setRowCount(0);
		dataModel.setColumnIdentifiers(columnNames);
		dataModel.addRow(columnNames);
		
		
		
	
	while (rs.next())
	{
		// System.out.println("13");
		String[] rowData = new String[colCount];
		for (int i = 0; i <= colCount- 1; i++)
		{
			rowData[i] = rs.getString(i + 1);
			System.out.println("12");
		}
		dataModel.addRow(rowData);
		// System.out.println(rowData.toString());
	} 
	

	}
		
	catch (Exception e) 
	{
	
		
		
	System.out.println(e);
	
	
	}	
	

}	

}	 
