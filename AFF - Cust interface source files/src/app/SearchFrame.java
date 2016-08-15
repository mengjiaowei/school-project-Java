package app;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class SearchFrame extends JFrame {

	private JButton back, restSelect;
	private	 JPanel infoPanel;
	private JLabel infoLabel;
	private App app;
	private JScrollPane scrollPane;
	private JTable table;
	public static String restname, columnName, areastr,restID;
	private ResultSet rs;
	public DefaultTableModel dd;

	SearchFrame(App a, final DefaultTableModel mm){
		app = a;
		 
		//create a JFrame to show information about the restaurant;
		/*---------------------RESTAURANT information window-------------------------*/
		//		resInfo = new JFrame("Restaurant information");
		setSize(600,500);
		setLayout(null);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Asian Food Finder");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//the information show out in the window
		infoPanel = new JPanel();
		infoPanel.setBounds(10, 10, 600, 350);
		infoLabel = new JLabel("We have found these restaurants:");
		infoPanel.add(infoLabel);
		table = new JTable(mm){  
			public Component prepareRenderer(TableCellRenderer renderer,int row, int col) {
				  Component comp = super.prepareRenderer(renderer, row, col);
				  JComponent jcomp = (JComponent)comp;
				  if (comp == jcomp) {
				  jcomp.setToolTipText((String)getValueAt(row, col));
				  }
				  return comp;
				  }
				  };
		
		
		scrollPane = new JScrollPane(table);
		infoPanel.add(scrollPane);
		table.changeSelection(0, 0, false, false);
		restID = table.getValueAt(0, 0).toString();
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			
			public void valueChanged(ListSelectionEvent event){
				restID=(table.getValueAt(table.getSelectedRow(), 0).toString());
				System.out.println(restID.toString());
			}
			
		});

		//back button
		back = new JButton("Back");
		back.setBounds(10,400,500,400);
		back.setSize(75,35);
//		back.setBackground(Color.RED);
		back.setVisible(true);
		back.addActionListener(new ActionListener(){


			public void actionPerformed(ActionEvent e) {
				System.out.println("go back!!!!");
				
				app.searchFrame.setVisible(false);
				app.selectFrame.setVisible(true);
			}

		});

		//create a button which can search information about restaurant
		restSelect = new JButton("Menu");
		restSelect.setBounds(480,400,500,400);
		restSelect.setSize(75,35);
//		search.setBackground(Color.RED);
		restSelect.setVisible(true);
		restSelect.addActionListener(new ActionListener(){


			public void actionPerformed(ActionEvent e) {
				System.out.println("Search the menu");
				dd = new DefaultTableModel();

				try {
//			 		app.database.executeQuery("SELECT * FROM restinfo WHERE RestID = '"+ restID +"'");
	   				app.database.executeQuery("SELECT DishNumber, DishName, Ingredients, DishPrice FROM Menu WHERE RestID = '" + restID + "'");
					ResultSet rs = app.database.getResultSet();
					ResultSetMetaData rsmd = rs.getMetaData();
					int colnum = rsmd.getColumnCount();
					String[] columName = new String[colnum];
					System.out.println(columName);

					for (int i = 1; i <= colnum; i ++) {
						columName[i-1] = rsmd.getColumnName(i);

					}
					dd.setColumnIdentifiers(columName);
					dd.setColumnIdentifiers(new Object[]{"#","Dish","Ingredients", "Price"});
					dd.addRow(new Object[]{"#","Dish","Ingredients","Price"});

					while (rs.next()){
						String[] rows = new String[colnum];
						for (int i = 1; i <= colnum; i ++){
							rows[i - 1] = rs.getString(i);
						}
						dd.addRow(rows);
					}
					rs.close();

				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				app.restaurantFrame = new RestaurantFrame(app,dd);
				app.searchFrame.setVisible(false);
			}

		});

		add(back);
		add(restSelect);
		add(infoPanel);
		add(infoLabel);
		setVisible(true);
		
	}

}

