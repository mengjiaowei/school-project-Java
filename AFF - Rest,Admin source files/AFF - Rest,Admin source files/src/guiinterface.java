import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import aff.admin.Database;
import aff.admin.Database.QueryTableModel;
//contains main method, starts by loading up the PasswordDialog class which allows the entering of password/username
public class guiinterface extends JFrame {

    private PassWordDialog passDialog;
    

    public guiinterface() {
        passDialog = new PassWordDialog(this, true);
        passDialog.setTitle("Welcome to AFF Order system");
        
        passDialog.setVisible(true);
        }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new guiinterface();
                
            }
        });
    }
}


// handles the login. Also contains the frame/tabbed panel of the program (the subpanels are accessed by their own methods in menulist, orderlist etc.)
class PassWordDialog extends JDialog {
	private JFrame face = new JFrame();
	private JTabbedPane tabbedPane = new JTabbedPane();
	private restinfo ri;
	private menulist ml;
	private orderlist ol;
	private NewOrder no;
	private Database db;
    private JSplitPane panel1;
	
    private JPanel panel3;
    private JSplitPane panel2;
	JScrollPane panel4;
 	public static String restID = null;
 	public static String role = null;
    private final JLabel userNameLabel;
    private final JLabel passWordLabel;
    private final JTextField userNameInput;
    private final JPasswordField passWordInput;
    private final JButton okButton;
    private final JButton cancelButton;
    private final JLabel status;
    static Timer timer;
    
    
    static DefaultMutableTreeNode orderNode, dishNode, root;  
    static DefaultTreeModel dtm;
    
    
    //this LinkedListMultiMap keeps track of what food is in the different orders
    public static LinkedListMultimap<Integer, String> orderMap = LinkedListMultimap.create();
    //this LinkedHashMap tracks which customer placed a specific order 
    public static LinkedHashMap<Integer, ArrayList<String>> customerMap;
    
    //and an integer representing the "newest order received", that is the order with highest orderID
    public static int lastOrderReceived;
    
    //TODO: this is a timer counter, it can be removed
    public static int timerCounter = 0;
    
    public PassWordDialog() {
        this(null, true);
    }
  
    
    public PassWordDialog(final JFrame parent, boolean modal) {
        super(parent, modal);
        
    
        JPanel p3 = new JPanel(new GridLayout(2, 1));
        userNameLabel  = new JLabel("Username");
        p3.add(userNameLabel);
        passWordLabel = new JLabel("Password");
        p3.add(passWordLabel);

        JPanel p4 = new JPanel(new GridLayout(2, 1));
        userNameInput = new JTextField(15);
        p4.add(userNameInput);
        passWordInput = new JPasswordField();
        p4.add(passWordInput);

        JPanel p1 = new JPanel();
        p1.add(p3);
        p1.add(p4);

        JPanel p2 = new JPanel();
        okButton = new JButton("Login");
        p2.add(okButton);
        cancelButton = new JButton("Cancel");
        p2.add(cancelButton);

        JPanel p5 = new JPanel(new BorderLayout());
        status = new JLabel(" ");
        p5.add(p2, BorderLayout.CENTER);
        p5.add(status, BorderLayout.NORTH);
        status.setForeground(Color.RED);
        status.setHorizontalAlignment(SwingConstants.CENTER);

        setLayout(new BorderLayout());
        add(p1, BorderLayout.CENTER);
        add(p5, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {  
            @Override
            public void windowClosing(WindowEvent e) {
            	
                System.exit(0);  
            }  
        });


        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	char[] s = passWordInput.getPassword();
            	String a = new String(s);
            	String abo2 = null;
            	String abo = null;
           
            	
            	db = new Database();
            	boolean conOK = (db.connect());
            	if(conOK)
            		try{
            			
            		String usQuery = "SELECT * FROM Users WHERE UserName='" +userNameInput.getText()+"' and Password ='" + a + "'";
            		
            		ResultSet rs = (db.queryDb(usQuery));
            		            		
            		while(rs.next()){
            			
            			abo = rs.getString("UserName");
                		abo2 = rs.getString("Password");
                		restID = rs.getString("RestID");
                		role = rs.getString("Role");
            		}
            		 if (abo.equals(userNameInput.getText()) && a.equals(abo2) && role.equals("Admin")) {
                       
                      	Database db = new Database();
                  		Boolean connOK = db.connect();
                  		
                  		
                  		QueryTableModel qtm = db.new QueryTableModel();
                  		if (connOK)
                  			
                  			{
                  				qtm.setQuery("Select * from restinfo order by RestID;");
                  			}
                  		
                      	@SuppressWarnings("unused")
 						aff.admin.AdminGUI admingui = new aff.admin.AdminGUI(qtm);
                      
                      setVisible(false);
                      face.setVisible(false);
                      
                      }
            		
                 		
            		//do we have the correct log-in name and password? if so...
            		 else if (abo.equals(userNameInput.getText()) && a.equals(abo2)) {
                	
                	// we start by checking what number the last order had
                	try {
						Scanner scanner = new Scanner(new File("LastOrder.txt"));
						lastOrderReceived = scanner.nextInt();
						scanner.close();
						
					} catch (FileNotFoundException e2) {
						
						e2.printStackTrace();
					}
                	 
                	//we initialize a LinkedHashMap that store the information about the customers
                	customerMap = new LinkedHashMap<Integer, ArrayList<String>>();
                	
                	//We need a timed event that constantly checks for new orders. this ActionListener does the trick
                	ActionListener checkForOrders = new ActionListener(){
                	

              			@Override
              			public void actionPerformed(ActionEvent e) {
              				db = new Database();
                          	boolean conOK = (db.connect());
                          	if(conOK)
                          	{
                          		try {	
									ResultSet rs = db.queryDb("SELECT Orders.OrderID, Timestamp, DishName, CAST(Quantity as CHAR(5)), CustomerName, CustomerEmail "
															+ "FROM (Menu, Orders, OrderContents, Customer) "
															+ "WHERE Orders.OrderID > '" + lastOrderReceived + "' AND Customer.CustomerID = Orders.CustomerID AND Orders.OrderID = OrderContents.OrderID "
															+ "AND OrderContents.RestID = '" + restID + "' AND OrderContents.RestID = Menu.RestID AND OrderContents.DishNumber = Menu.DishNumber "
															+ "ORDER BY Orders.Timestamp");
									int justRead = 0;
									
									
									/*the following checks if the ResultSet is empty  
									if (!rs.isBeforeFirst())
									{
										System.out.println("No new orders at this time");
										return;
									}  */
									
									
									while (rs.next())
									{
										
										int orderNo = rs.getInt("OrderID");
										
										// when we start reading an order (the first or not the one we were just reading)...
										
										if (rs.isFirst() || justRead != orderNo){
											//... we create an ArrayList of strings for the HashMap with customer information
											ArrayList<String> customerArrayList = new ArrayList<String>();
											customerArrayList.add(rs.getString("CustomerName"));
											customerArrayList.add(rs.getString("CustomerEmail"));
											customerArrayList.add(rs.getString("Timestamp"));
											customerMap.put(orderNo, customerArrayList);
										}
										
											StringBuilder foodQty = new StringBuilder(); 
											foodQty.append(rs.getString("DishName"));
											foodQty.append(" (");
											foodQty.append(rs.getString("CAST(Quantity as CHAR(5))"));
											foodQty.append(")");
											String food = foodQty.toString();
											orderMap.put(orderNo, food);
										
											
										//we tell Java which order number we were just reading
										justRead = orderNo;
										
										if (rs.isLast())
										{
											//we must store integer lastOrder, which is the highest OrderID of the last ResultSet
											lastOrderReceived = (rs.getInt("OrderID"));
											System.out.println("Last order received was " + lastOrderReceived + ".");
											//we also save this integer to a file, so our program knows the last order the next time we run it
											
											try {
												File file = new File("LastOrder.txt");
												if (!file.exists())
												{
													file.createNewFile();
												}
												
												FileWriter fw = new FileWriter(file.getAbsoluteFile());
												BufferedWriter bw = new BufferedWriter(fw);
												bw.write(String.valueOf(lastOrderReceived));
												bw.close();
												
											}
											catch (IOException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
										}
									}
									
								} catch (SQLException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
                          	
                          		
                          		
                          		//system messages for checking that stuff is working
                          		System.out.println("Database retrieval was done " + timerCounter + " times.");
                      			timerCounter++;
                      			System.out.println("Last order was: " + lastOrderReceived);
                      			
                      			System.out.println("(GuiInterface calling) Size of map is: " + PassWordDialog.orderMap.size());
                      			
                      			
                      		//root and TreeModel of the JTree presented in NewOrder are initialized here	
                      			root = new DefaultMutableTreeNode("Incoming orders");
                      		    dtm = new DefaultTreeModel(root); 
                      		  
                      		   
                      		   Set<Integer> keySet = orderMap.keySet();
                      			    Iterator<Integer> keyIterator = keySet.iterator();
                      			    while (keyIterator.hasNext()  ) {			//&& keyIterator.next().intValue() > lastOrderReceived
                      			        Integer key = (Integer) keyIterator.next();
                      			        
                      			        orderNode = new DefaultMutableTreeNode(key);
                      			        System.out.println("Key: " + key);
                      			        
                      			        dtm.insertNodeInto(orderNode, root, root.getChildCount());
                      			        System.out.println("Lets confirm that orderNode \"" + orderNode + "\" has been added to it's parent: " + orderNode.getParent());
                      		        	
                      			        
                      			        Collection<String> values = orderMap.get( key );
                      			        Object[] dishArray = values.toArray();
                      			        for (int i = 0; i < dishArray.length; i++)
                      			        {
                      			        	dishNode = new DefaultMutableTreeNode(dishArray[i].toString());
                      			        	
                      			        	dtm.insertNodeInto(dishNode, orderNode, orderNode.getChildCount());
                      			        	System.out.println("And this confirms that dishNode \"" + dishNode + "\" has been added to order node " + orderNode.toString());
                      			        	System.out.println(dishNode.getParent() + "is the parent of " + dishNode +".");
                      			        	
                      			        }
                      			        
                          	
                          	}
              			
              			
              			
              			
              			
              			System.out.println("Panel index is " + tabbedPane.getSelectedIndex());
              			
              			NewOrder.tree.setModel(dtm);
              			
              		    
              		  
              		}
                  }
               };
                	
                  	//the actual timer that fires up the database updates and builds the order tree
                	timer = new Timer(0, checkForOrders);
                	timer.setDelay(10000);
                	timer.start();
                	
                	
                	// calling the different panels in the TabbedPane
                	ml = new menulist(restID);
                	ri = new restinfo(restID);
                	ol = new orderlist();
                	no = new NewOrder(restID);
                	
                	face.add(tabbedPane);
                	panel1 = no.getJSplitPanel();
                	panel2 = ol.getOrderList();
                	           	
                	panel3 = ri.restinfo(); 
                	panel4 = ml.getPanel();
                	
                	tabbedPane.addTab("New Orders", panel1);               
                	tabbedPane.addMouseListener(null);	
                	tabbedPane.addTab("Order list", panel2);               
                	tabbedPane.addMouseListener(null);	
                	tabbedPane.addTab("Restaurant info", panel3);
                	tabbedPane.addMouseListener(null);
                	tabbedPane.addTab("Menu", panel4);
                	tabbedPane.addMouseListener(null);
                	
                	//face.setLocationRelativeTo(null);  not working properly for some stupid reason
                	face.getContentPane().add(tabbedPane);
                	face.setTitle("AFF delivery system");
                	face.setSize(600,500);
                	face.setDefaultCloseOperation(EXIT_ON_CLOSE);
                	          	
                	
                	
                	face.pack();
                	
                	
                	face.setVisible(true);
                    setVisible(false);
                   
                                            	
                } else {
                    status.setText("Invalid username or password");
                }
                rs.close();
            } catch(SQLException e1) {
            	e1.printStackTrace();
            	}
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                
                System.exit(0);
            }
        });
      
    }
}
	