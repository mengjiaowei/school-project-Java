package app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public class RestaurantFrame extends JFrame{

	private JButton back, checkout;
	public JButton addButton, removeButton;
	public JPanel pnlActions,totalPanel;
	public JLabel totalLabel;
	public JTable MenuTable, CheckoutTable;
	private App app;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane1;
	public static String menulist,oCInfo;
	private Object dishNumber,dishName,dishPrice;
	public int qty,totalPrice=0;
	public DefaultTableModel tablemodel;
	private Vector menuList;
	private TableColumnModel columnNames;
	private DefaultTableModel model;
	
	
	

	
	

	RestaurantFrame(App a,final DefaultTableModel dd){
		app = a;

		setSize(600,500);
		setLayout(null);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Asian Food Finder");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		

		//create dual tables for menu items and shopping cart items
		//also create add/remove buttons for dishes
		//add new panel(pnlActions) to frame
		
		pnlActions = new JPanel();
		MenuTable = new JTable(dd){  
			public Component prepareRenderer(TableCellRenderer renderer,int row, int col) {
				  Component comp = super.prepareRenderer(renderer, row, col);
				  JComponent jcomp = (JComponent)comp;
				  if (comp == jcomp) {
				  jcomp.setToolTipText((String)getValueAt(row, col));
				  }
				  return comp;
				  }
				  };
		final DefaultTableModel model = new DefaultTableModel();
		CheckoutTable = new JTable(model);
		model.setColumnCount(4);
		model.setColumnIdentifiers(new Object[]{"#","Dish","Price", "Quantity"});
		
		JTableHeader header2 = MenuTable.getTableHeader();
		JTableHeader header = CheckoutTable.getTableHeader();
		
		model.addRow(new Object[]{"#","Dish","Price", "Quantity"});
		MenuTable.setBounds(10, 10, 230, 350);
		MenuTable.changeSelection(0, 0, false, false);	
		CheckoutTable.setBounds(350, 10, 230, 350);
		scrollPane = new JScrollPane(MenuTable);
		scrollPane1 = new JScrollPane(CheckoutTable);
		
		scrollPane.add(header2);
		scrollPane1.add(header);
		totalPanel = new JPanel();
		addButton = new JButton("ADD");
		addButton.setBounds(10,360,80,20);
		removeButton = new JButton("REMOVE");
		removeButton.setBounds(500,360,80,20);
		removeButton.setEnabled(false);
		totalLabel = new JLabel();
		MenuTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent event){
				ListSelectionModel lsm = (ListSelectionModel)event.getSource();
				
	            addButton.setEnabled(!lsm.isSelectionEmpty());
	            
	            dishNumber=(MenuTable.getValueAt(MenuTable.getSelectedRow(), 0));
				dishName=(MenuTable.getValueAt(MenuTable.getSelectedRow(), 1));
				dishPrice=(MenuTable.getValueAt(MenuTable.getSelectedRow(), 3));
				qty=1;
				System.out.println(dishName);
				
			}
			
		});
		
		
		CheckoutTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent event) {
				ListSelectionModel lsm = (ListSelectionModel)event.getSource();
				if (lsm.isSelectionEmpty()){return;}
				removeButton.setEnabled(!lsm.isSelectionEmpty());
			}
			
		});
		
		addButton.addActionListener(new ActionListener(){
			
			

			public void actionPerformed(ActionEvent e){
				
				
				if (MenuTable.getSelectedRow() == 0){	
	            	return;
	            }
				
				
				for (int i = 1; i < model.getRowCount(); i++)
				{
					if (model.getValueAt(i, 1).equals(dishName))
					{
						
						qty = new Integer(((Integer) model.getValueAt(i, 3)).intValue() + 1);
						
						model.setValueAt( qty, i, 3);
						
						int totePrice = (Integer.valueOf((String) dishPrice).intValue());
						newTotalPrice(totePrice);
						return;
					}
					
				}
				
				model.addRow(new Object[]{dishNumber,dishName,dishPrice,qty});
				int totePrice = (Integer.valueOf((String) dishPrice).intValue());
				newTotalPrice(totePrice);
				
			}
		});
		
		removeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				System.out.println("Nr of rows: " + CheckoutTable.getRowCount());
				
				if ((CheckoutTable.getRowCount() == 1))
				{
					return;
				}
				int i = (CheckoutTable.getSelectedRow());
				
				dishPrice = model.getValueAt(i, 2);
				qty = new Integer(((Integer) model.getValueAt(i, 3)).intValue());
				
				if (qty > 1)
				{
					qty = (qty - 1);
					model.setValueAt(qty, i, 3);
					int totePrice = (Integer.valueOf((String) dishPrice).intValue());
					newTotalPrice(-totePrice);
					return;
				}
				
				model.removeRow(CheckoutTable.getSelectedRow());
				int totePrice = (Integer.valueOf((String) dishPrice).intValue());
				newTotalPrice(-totePrice);
				CheckoutTable.changeSelection(1, 1, false, false);
			}
		});		
		
     
		//gbc = new GridBagConstraints();
		//add objects to the panel
		String totalLabelText = ("Total: " + totalPrice + "kr");
		totalLabel.setText(totalLabelText);
		totalPanel.add(totalLabel);
		totalLabel.setVisible(true);
		totalPanel.setVisible(true);
		totalPanel.setBounds(340,360,500,400);
		totalPanel.setSize(100,50);
		pnlActions.setLayout(new BorderLayout());
		pnlActions.add(scrollPane);
		// pnlActions.add(header);
		pnlActions.add(scrollPane1);
		pnlActions.add(addButton);
		pnlActions.add(removeButton);
		pnlActions.add(totalPanel);
		pnlActions.setVisible(true);
		System.out.println("panel is here");
		

		//back button
		back = new JButton("Back");
		back.setBounds(10,400,500,400);
		back.setSize(75,35);
		back.setVisible(true);
		back.addActionListener(new ActionListener(){


			public void actionPerformed(ActionEvent e) {
				System.out.println("go back!!!!");
				app.restaurantFrame.setVisible(false);
				app.searchFrame.setVisible(true);
				
			}

		});
		//check out button
		checkout = new JButton("Check out");
		checkout.setBounds(450,400,500,400);
		checkout.setSize(100,35);
//		checkout.setBackground(Color.RED);
		checkout.setVisible(true);

		checkout.addActionListener(new ActionListener(){


			public void actionPerformed(ActionEvent e) {
				
				//creates ArrayList containing all the relevant information for OrderContents, goes to checkout frame
				ArrayList al = new ArrayList();
				menuList = model.getDataVector();
				int total = menuList.size();
				for(int i=0;i < total;i++)
					al.add(menuList.get(i).toString());
					oCInfo = al.toString().replaceAll("[^0-9]", " ");
					
				System.out.println(oCInfo);
				System.out.println("check out");
				app.checkoutFrame = new CheckoutFrame(app);
				app.restaurantFrame.setVisible(false);
			}

		});
		add(back);
		add(checkout);
//		setupTable(MenuTable);
//		setupTable(CheckoutTable);
		add(pnlActions);
		add(totalPanel);
		add(MenuTable);
		add(CheckoutTable);
		add(addButton);
		add(removeButton);
		setVisible(true);
	}
	
	public void newTotalPrice(int x){
		
		totalPrice = totalPrice + x;
		String totalLabelText = ("Total: " + totalPrice + "kr");
		totalLabel.setText(totalLabelText);
		
	}
}