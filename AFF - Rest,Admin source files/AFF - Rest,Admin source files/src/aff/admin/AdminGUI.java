package aff.admin;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import aff.admin.Database.QueryTableModel;

public class AdminGUI 
{
	private JTable table;
	private QueryTableModel qtm;
	protected String currentRestID;
	protected DefaultTableModel dtm = new DefaultTableModel(1,8);
	protected JTable insertTable;
	protected ArrayList<String> insertValues;
	protected Database qdb;
	private boolean connOK;
	
	public AdminGUI(QueryTableModel qtm) // Takes QueryTableModel qtm as argument for initalizing
	{
		this.qtm = qtm;
		createAndShowGUI();
	}


	private void createAndShowGUI() {
		JFrame frame = new JFrame("Asian FoodFinder Admin 1.0");
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame.setContentPane(createContentPane());
		frame.setJMenuBar(createMenuBar());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		
		
	}

	//creates the menu bar
	private JMenuBar createMenuBar() 
	{
		JMenuBar menuBar = new JMenuBar();
		JMenu actionMenu = new JMenu("Actions");
		
		JMenuItem addRestaurant = new JMenuItem("Add new restaurant");
		JMenuItem refreshList = new JMenuItem("Refresh restaurant list");
		
		addRestaurant.addActionListener(new AdminListener());
		addRestaurant.setActionCommand("addRest");
		refreshList.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				qtm = qdb.new QueryTableModel();
				qtm.setQuery("Select * from restinfo order by RestID");
				
			}
			
		});
		
		actionMenu.add(addRestaurant);
		actionMenu.add(refreshList);
		menuBar.add(actionMenu);
		return menuBar;
	
	}

	//and this creates our content panel
	private JPanel createContentPane() 
	{
		JPanel panel = new JPanel(new GridBagLayout());
		JLabel headline1 = new JLabel("To add a new restaurant use the Actions menu."); 
		JLabel headline2 = new JLabel("To delete/edit a restaurant - first select one from the table and click appropriate button");
		
		java.lang.Object[] colIdentifiers = {"RestID", "Name", "Area", "PhoneNo", "Open hours", "Street", "Postal", "City"};
		dtm.setColumnIdentifiers(colIdentifiers);
		
		insertTable = new JTable(dtm);
		TableColumnModel tcm = insertTable.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(45);
		tcm.getColumn(1).setPreferredWidth(130);
		tcm.getColumn(2).setPreferredWidth(45);
		tcm.getColumn(3).setPreferredWidth(70);
		tcm.getColumn(4).setPreferredWidth(70);
		tcm.getColumn(5).setPreferredWidth(70);
		tcm.getColumn(6).setPreferredWidth(70);
		tcm.getColumn(7).setPreferredWidth(40);
		
		JTableHeader header = insertTable.getTableHeader();
				
		JPanel buttonPanel = new JPanel();
		JButton edit = new JButton("Edit restaurant info");
		JButton delete = new JButton("Delete restaurant from DB");
		delete.setActionCommand("deleteRest");
		buttonPanel.add(delete);
		buttonPanel.add(edit);
		
		final JTable table = new JTable(qtm);
		TableColumnModel tcm2 = table.getColumnModel();
		tcm2.getColumn(0).setPreferredWidth(45);
		tcm2.getColumn(1).setPreferredWidth(130);
		tcm2.getColumn(2).setPreferredWidth(45);
		tcm2.getColumn(3).setPreferredWidth(70);
		tcm2.getColumn(4).setPreferredWidth(70);
		tcm2.getColumn(5).setPreferredWidth(70);
		tcm2.getColumn(6).setPreferredWidth(70);
		tcm2.getColumn(7).setPreferredWidth(40);
		
		
		
		// when we click a row in the table it should appear in the InsertTable up top of the GUI
		
		table.addMouseListener(new MouseAdapter(){
			
			public void mouseClicked(MouseEvent e)
			{
				
				currentRestID = (table.getValueAt(table.getSelectedRow(), 0).toString());
				ArrayList<String> copyingValues = new ArrayList<String>();
					for (int i = 0; i < 8; i++)
					{	
						if (table.getValueAt(table.getSelectedRow(), i) != null)
						{
							copyingValues.add(table.getValueAt(table.getSelectedRow(), i).toString());
							insertTable.setValueAt(copyingValues.get(i).toString(), 0, i);
						}
						else
						{
							insertTable.setValueAt("", 0, i);
						}
					}
				}
				
			});  
		JScrollPane scrollPane = new JScrollPane(table);
		//add actionListeners
		edit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				
				insertValues = new ArrayList<String>();
				
				for (int i = 1; i < 8; i++)
				{
					if (insertTable.getValueAt(0, i).toString() == null)
					{
						insertValues.add("null");
					}
					insertValues.add((insertTable.getValueAt(0, i).toString()));
				}
				String sqlString = "update restinfo set RestName = '?', Area = '?', PhoneNumber = '?', OpeningHours = '?', " +
						"StreetAddress = '?', PostalNumber = '?', City = '?' where RestID = '" + currentRestID + "'";
				
				for (int i = 0; i < 7; i++)
				{
					sqlString = sqlString.replaceFirst("\\?", insertValues.get(i));
				}
				AdminListener al = new AdminListener(sqlString);
				
				
  
			}
		});
		delete.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you wish to delete Restaurant \"" + 
				(insertTable.getValueAt(0, 1).toString()) + "\" with RestID " + currentRestID, "Sure about that?", dialogButton );
				if (dialogResult == JOptionPane.YES_OPTION)
				{
					String sqlString = "delete from restinfo where RestID = '" + currentRestID + "'";
					AdminListener al = new AdminListener(sqlString);
					for (int i = 0; i < insertTable.getColumnCount(); i++)
					{
						insertTable.setValueAt(null, 0, i);
					}
				}
				else {return;}
				
			}
			
		});
		
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10,10,10,10);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(headline1, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(headline2, gbc);
		
		gbc.insets = new Insets(0,0,0,0);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.VERTICAL;
		panel.add(header, gbc);
		
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(insertTable, gbc);
		
		gbc.insets = new Insets(10,10,10,10);
		gbc.gridx = 0;
		gbc.gridy = 4;
		panel.add(buttonPanel, gbc);
		
		
		gbc.gridx = 0;
		gbc.gridy = 5;
		panel.add(scrollPane, gbc);
		
		return panel;
	}
	
	
	
	public static void main(String[] args)
	{
		Database db = new Database();
		Boolean connOK = db.connect();
		
		
		QueryTableModel qtm = db.new QueryTableModel();
		if (connOK)
			{
				qtm.setQuery("Select * from restinfo order by RestID;");
			}
		
		AdminGUI admingui = new AdminGUI(qtm);

	}
	
	 class AdminListener implements ActionListener
	{
		Database qdb = new Database();
		boolean connOK = (qdb.connect());
		ArrayList<String> editValues;
		String sqlString = "";
		String lastGeneratedKey = "";
				
		AdminListener(String s) {
			super();
			sendData(s);
		}

		public AdminListener() {
			// TODO Auto-generated constructor stub
		}

		public void actionPerformed(ActionEvent c) 
		{
			if (c.getActionCommand() == "addRest")
			{
				
				//adding a new restaurant requires the user to enter restaurant name, user name for the owner and a password
				JTextField restField = new JTextField(20);
				JTextField userField = new JTextField(20);
				JTextField passField = new JTextField(20);
				
				JPanel newPanel = new JPanel();
				newPanel.setLayout(new GridLayout(3,2));
				newPanel.add(new JLabel("Restaurant name"));
				newPanel.add(restField);
				newPanel.add(new JLabel("User name"));
				newPanel.add(userField);
				newPanel.add(new JLabel("Password"));
				newPanel.add(passField);
				
				int result = JOptionPane.showConfirmDialog(null, newPanel, "Please enter information of new user", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION)
				{
					System.out.println(restField.getText() + userField.getText() + passField.getText());
				}
				
				String sqlString1 = "insert into restinfo (RestName) values ('" + restField.getText() + "');" ;
				String sqlString2 = "insert into Users (UserName,Password,Role,RestID) values ('" + userField.getText() +
						"', '" + passField.getText() + "', 'Restaurant', ";
				sendData(sqlString1, sqlString2);
				
			}
		}
		
		
		public void sendData(String sqlString)
		{
			if (connOK)
			{
				try 
				{
					qdb.updateDb(sqlString);
					qtm.setQuery("select * from restinfo order by RestID;");
					
				}  
				catch (SQLException e) 
				{
					e.printStackTrace();
				}
			}
			
		}
		//this method generates the user of the newest restaurant by checking which restaurant ID the last inserted restaurant has
		public void sendData(String sqlString1,String sqlString2)
		{
			StringBuilder sb = new StringBuilder(sqlString2);
			if (connOK)
			{
				try
				{
					lastGeneratedKey = (qdb.updateAndgetKey(sqlString1));
					qtm.setQuery("select * from restinfo order by RestID;");
					sb.append(lastGeneratedKey + ")");
					System.out.println("Key generated: " + lastGeneratedKey + "sb is: " + (sb.toString()));
					qdb.updateDb(sb.toString());
				}
				
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
			
	}    
		
}
	
