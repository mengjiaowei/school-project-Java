package app;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class SelectFrame extends JFrame {

	private ButtonGroup areaButtonGroup;
	private JRadioButton centralButton, eastButton, westButton, northButton,southButton,chinesefoodButton, thaifoodButton, indianfoodButton, sushifoodButton;
	private AbstractButton textfree;
	private JButton search;
	public JButton addButton, removeButton, Store2, Store1;

	protected JFrame menuInfo;
	private JPanel postalNumber, freeSearch, select;
	protected JPanel shoppingCart;
	public JPanel restaurant, infoPanel;
	private JLabel postalNumber1, freeSearch1;
	public JLabel weFind2, select1;
	private JTextField postalNumber2, freeSearch2;
	public JTable MenuTable, CheckoutTable;
	private JComboBox jcb;
	private App app;
	public String areastr, restname, area, categoryText;
	public int colnum;
	public DefaultTableModel mm;
	private String text;
	private JButton search1;
	private AbstractButton search2;

	SelectFrame(App a){
		app = a;
		setSize(600,500);
		setLayout(null);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Asian Food Finder");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//area option radio buttons
		centralButton = new JRadioButton("CENTRAL Gothenburg",true);
		centralButton.setBounds(100, 150,200,100);
		centralButton.setVisible(false);
		areastr = ("Central");

		eastButton = new JRadioButton("EAST Gothenburg",true);
		eastButton.setBounds(100, 210, 200, 100);
		eastButton.setVisible(false);

		westButton = new JRadioButton("WEST Gothenburg",true);
		westButton.setBounds(100, 270, 200, 100);
		westButton.setVisible(false);

		northButton = new JRadioButton("NORTH Gothenburg",true);
		northButton.setBounds(100, 330, 200, 100);
		northButton.setVisible(false);
		
		southButton = new JRadioButton("SOUTH Gothenburg",true);
		southButton.setBounds(100, 390, 200, 100);
		southButton.setVisible(false);
		
		centralButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				areastr=("Central");
			}
		});

		eastButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				areastr=("East");
			}
		});


		westButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				areastr=("West");

			}

		});
		northButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				areastr=("North");

			}

		});
		southButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				areastr=("South");

			}

		});
		areaButtonGroup = new ButtonGroup();
		areaButtonGroup.add(centralButton);
		areaButtonGroup.add(northButton);
		areaButtonGroup.add(southButton);
		areaButtonGroup.add(eastButton);
		areaButtonGroup.add(westButton);
	;
		
		//category option radio buttons
		chinesefoodButton = new JRadioButton("Chinese",true);
		chinesefoodButton.setBounds(100, 150,200,100);
		chinesefoodButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				categoryText = ("Chinese");
				//freeSearch.setVisible(false);

			}
		});
		chinesefoodButton.setVisible(false);

		thaifoodButton = new JRadioButton("Thai",true);
		thaifoodButton.setBounds(100, 210, 200, 100);
		thaifoodButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				categoryText = ("Thai");
				System.out.println("thai food!!!");
				//freeSearch.setVisible(false);

			}
		});
		thaifoodButton.setVisible(false);
		
		indianfoodButton = new JRadioButton("Indian",true);
		indianfoodButton.setBounds(100, 270, 200, 100);
		indianfoodButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				categoryText = ("Indian");
				System.out.println("Indian food!!!");
				//freeSearch.setVisible(false);
			}
		});
	indianfoodButton.setVisible(false);
		
		sushifoodButton = new JRadioButton("Sushi",true);
		sushifoodButton.setBounds(100, 330, 200, 100);
		sushifoodButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				categoryText = ("Sushi");
				System.out.println("sushi food!!!");
				//freeSearch.setVisible(false);
			}
		});
		sushifoodButton.setVisible(false);
		/*freeSearch = new JPanel();
		freeSearch.setSize(100,200);
		freeSearch.setBounds(100, 340,200,300);	
		freeSearch1 = new JLabel("Text Search:");
		freeSearch2 = new JTextField(10);
		freeSearch2.setEditable(true);
		freeSearch.add(freeSearch1);
		freeSearch.add(freeSearch2)
		freeSearch.setVisible(false);


		textfree = new JRadioButton("Or use free text search",true);
		textfree.setBounds(100, 270,200,100);
		textfree.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				freeSearch.setVisible(true);
				search.setVisible(false);
				search2.setVisible(true);
			}
		});
		textfree.setVisible(false);*/

		final ButtonGroup foodButtonGroup = new ButtonGroup();
		foodButtonGroup.add(chinesefoodButton);
		foodButtonGroup.add(thaifoodButton);
		foodButtonGroup.add(sushifoodButton);
		foodButtonGroup.add(indianfoodButton);
		//foodButtonGroup.add(textfree);
		//this is a postal number search panel

		postalNumber = new JPanel();
		postalNumber.setBounds(50, 200,500,200);	
		postalNumber1 = new JLabel("Postal Number:");
		postalNumber2 = new JTextField(10);
		postalNumber2.setEditable(true);
		postalNumber.add(postalNumber1);
		postalNumber.add(postalNumber2);
		postalNumber.setVisible(false);

		//creat a jcombobox which can select the menthod of search
		select = new JPanel();
		String []selectOptions = {"Choose your search method","General Area","Postal Number","Category"};
		jcb = new JComboBox(selectOptions);
		jcb.setForeground(Color.black);
		jcb.setBackground(Color.white);
		jcb.setSize(20, 3);
		jcb.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {

				if(e.getStateChange() == ItemEvent.SELECTED){
					if(e.getItem().equals("General Area")){
						centralButton.setVisible(true);
						eastButton.setVisible(true);
						westButton.setVisible(true);
						northButton.setVisible(true);
						southButton.setVisible(true);
					}else if(e.getItem().equals("Postal Number")){
						postalNumber.setVisible(true);
						search.setVisible(false);
						search1.setVisible(true);
						System.out.println("postal");
					}else if(e.getItem().equals("Category")){
						System.out.println("category");
						foodButtonGroup.clearSelection();
						search.setVisible(false);
						search1.setVisible(false);
						search2.setVisible(true);
						postalNumber.setVisible(false);
						centralButton.setVisible(false);
						eastButton.setVisible(false);
						westButton.setVisible(false);
						northButton.setVisible(false);
						southButton.setVisible(false);
						chinesefoodButton.setVisible(true);
						thaifoodButton.setVisible(true);
						indianfoodButton.setVisible(true);
						sushifoodButton.setVisible(true);
						//textfree.setVisible(true);	
					
					}
				}else{
					//this is the action that just one group of radio option appear in the window
					centralButton.setVisible(false);
					eastButton.setVisible(false);
					westButton.setVisible(false);
					northButton.setVisible(false);
					southButton.setVisible(false);
					chinesefoodButton.setVisible(false);
					indianfoodButton.setVisible(false);
					sushifoodButton.setVisible(false);
					thaifoodButton.setVisible(false);
					//textfree.setVisible(false);	
					//freeSearch.setVisible(false);
					postalNumber.setVisible(false);
					search1.setVisible(false);
					search.setVisible(true);
					search2.setVisible(false);

				}
			}

		});
		select.add(select1  =new JLabel("Asian Food Finder"));
		select.add(jcb);
		select.setBounds(200, 100, 250, 50);
		//create a button which can search information about restaurant
		search = new JButton("Search");
		search.setBounds(480,400,500,400);
		search.setSize(75,35);
		//search.setBackground(Color.RED);
		search.setVisible(true);
		search.addActionListener(new ActionListener(){


			public void actionPerformed(ActionEvent e) {
				mm = new DefaultTableModel();
				

				try {
					app.database.executeQuery("SELECT * FROM restinfo WHERE Area = '"+ areastr +"'");
					ResultSet rs = app.database.getResultSet();
					ResultSetMetaData rsmd = rs.getMetaData();
					int colnum = rsmd.getColumnCount();
					String[] columName = new String[colnum];

					for (int i = 1; i <= colnum; i ++) {
						columName[i - 1] = rsmd.getColumnName(i);

					}
					mm.setColumnIdentifiers(columName);

					if (!rs.next()){
						JOptionPane.showMessageDialog(rootPane,
								"Sorry, we couldn't find any such restaurants.");
					}
				else{
					rs.previous();
					while (rs.next()){
						String[] rows = new String[colnum];
						for (int i = 1; i <= colnum; i ++)
						{
							rows[i - 1] = rs.getString(i);
						}
						mm.addRow(rows);
						}
						
						app.searchFrame = new SearchFrame(app, mm);
						app.selectFrame.setVisible(false);
					
				}
					rs.close();

				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			
			}

		});
		search1 = new JButton("Search");
		search1.setBounds(480,400,500,400);
		search1.setSize(75,35);
		//search1.setBackground(Color.RED);
		search1.setVisible(true);
		search1.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				text = postalNumber2.getText();
				mm = new DefaultTableModel();

				try {
					app.database.executeQuery("SELECT * FROM restinfo WHERE PostalNumber like '%"+ text+"%'");
					ResultSet rs = app.database.getResultSet();
					ResultSetMetaData rsmd = rs.getMetaData();
				
					int colnum = rsmd.getColumnCount();
					String[] columName = new String[colnum];
					
					for (int i = 1; i <= colnum; i ++) {
						columName[i - 1] = rsmd.getColumnName(i);

					}
					mm.setColumnIdentifiers(columName);

					if (rs.next()){
						String[] rows = new String[colnum];
						for (int i = 1; i <= colnum; i ++){
							rows[i - 1] = rs.getString(i);
						}
						mm.addRow(rows);
						app.searchFrame = new SearchFrame(app, mm);
						app.selectFrame.setVisible(false);
					}
				else{
					JOptionPane.showMessageDialog(rootPane,
							"Sorry, we could not find any restaurants for you.");
					
				}
					rs.close();
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
			}
			
			
			});
		search2 = new JButton("Search");
		search2.setBounds(480,400,500,400);
		search2.setSize(75,35);
//		search2.setBackground(Color.RED);
		search2.setVisible(true);
		search2.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				
				
				System.out.println(categoryText + "BAJS");
				mm = new DefaultTableModel();

				try {
					app.database.executeQuery("SELECT * FROM restinfo WHERE Category like '%"+ categoryText+"%'");
					ResultSet rs = app.database.getResultSet();
					ResultSetMetaData rsmd = rs.getMetaData();
					int colnum = rsmd.getColumnCount();
					String[] columName = new String[colnum];

					for (int i = 1; i <= colnum; i ++) {
						columName[i - 1] = rsmd.getColumnName(i);

					}
					mm.setColumnIdentifiers(columName);

					if (!rs.next()){
						JOptionPane.showMessageDialog(rootPane,
								"Sorry, we couldn't find any such restaurants.");
					}
				else{
					rs.previous();
					while (rs.next()){
						String[] rows = new String[colnum];
						for (int i = 1; i <= colnum; i ++)
						{
							rows[i - 1] = rs.getString(i);
						}
						mm.addRow(rows);
						}
						
						app.searchFrame = new SearchFrame(app, mm);
						app.selectFrame.setVisible(false);
					
				}
					rs.close();

				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			
			}
			
			
			});
		
		add(select);
		add(postalNumber);
		add(centralButton);
		add(eastButton);
		add(westButton);
		add(southButton);
		add(northButton);
		add(postalNumber);
		add(chinesefoodButton);
		add(thaifoodButton);
		add(indianfoodButton);
		add(sushifoodButton);
		//add(textfree);
		//add(freeSearch);
		add(search);
		add(search1);
		add(search2);
		setVisible(true);
	}
}
