import javax.swing.*;  
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;  

import java.awt.*;  
import java.awt.event.*;  
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class NewOrder 
{  
  
  static JTree tree;
  JScrollPane treeScrollPane;
  
  //these two nodes are used for building the tree of active orders in the "orderlist" class
  DefaultMutableTreeNode activeOrderNode, activeDishNode;
  
  guiinterface guiinterface;
  private JPanel p;
  private JSplitPane spt;
  private JLabel sign;
  private JTextArea text;
  JButton AcpBtn, DecBtn;
  String emailAddress;
  
  
  public NewOrder(String abo3)
  {
	 buildTree();
  }
  
  
  public void buildTree() 
  { 
	  
	
	tree = new JTree(); 	// (root)
	System.out.println("Row count of the tree is " + tree.getRowCount());
	
    
    //Adding Panel and Buttons on the right side of the SplitPane
    p = new JPanel();
    p.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	
	sign = new JLabel("Customer information:");
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridwidth = 2;
	c.gridx = 5;
	c.gridy = 2;
	c.insets = new Insets(10,10,10,10);
	p.add(sign, c);
	
	text = new JTextArea(0, 2);
	text.setPreferredSize(null);
	c.gridwidth = 2;
	c.gridheight = 1;
	//c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 5;
	c.gridy = 3;
	c.insets = new Insets(10,10,10,10);
	p.add(text, c);
	text.setEditable(false);
	
	
	
	AcpBtn = new JButton("Accept");
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 5;
	c.gridy = 6;
	c.insets = new Insets(10,10,10,10);
	p.add(AcpBtn, c);
	
	DecBtn = new JButton("Decline");
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 5;
	c.gridy = 7;
	c.insets = new Insets(10,10,10,10);
	p.add(DecBtn, c);
	
	
	  
    
   
    
    //trying out a tree selection listener
	
    tree.addTreeSelectionListener(new TreeSelectionListener() {
        public void valueChanged(TreeSelectionEvent e) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                               tree.getLastSelectedPathComponent();

        // if nothing is selected 
            if (node == null || node.isRoot()) {
            	
            	//this fixes a bug where the program would throw a NullPointer exception when you tried to click accept the same moment the order tree was updating
            	AcpBtn.setEnabled(false);
            	DecBtn.setEnabled(false);
            	return;
            	}
            AcpBtn.setEnabled(true);
            DecBtn.setEnabled(true);
        // retrieve the node that was selected 
            Object nodeInfo = node.getUserObject();
            System.out.println("Nodeinfo as string representation: " + nodeInfo.toString());
            System.out.println("Just nodeinfo: " + nodeInfo);
            
            
            //the customer information must be stored in a new LinkedHashMap in orderlist, otherwise synchronization issues occur
            String newKey = nodeInfo.toString();
            ArrayList<String> customerInfo = PassWordDialog.customerMap.get(nodeInfo);
             
            orderlist.waitingCustomerMap.put(newKey, customerInfo);
            
        // TODO: React to the node selection. Perhaps we can also store the customers e-mail as a string from here?
            emailAddress = new String(customerInfo.get(1)); 
            text.setText("Name: " +customerInfo.get(0) +"\n" + "Mail :" + emailAddress +"\n" + "Time of order: " + customerInfo.get(2));
            
        }
    });
    
    //Accept Button Function for accepting an order
    AcpBtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            DefaultTreeModel model = (DefaultTreeModel) tree.getModel();

            TreePath[] paths = tree.getSelectionPaths();
            
            
            
            for (TreePath path : paths) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                if (node.getParent() != null && !node.isLeaf()) {
                    
                	String time = JOptionPane.showInputDialog(null, "How many minutes will this order take to prepare?");
                	
                	Pattern p = Pattern.compile("[a-ö]");
            		Matcher m = p.matcher(time);
            		if (m.find()){
                   JOptionPane.showMessageDialog(null, "Please enter only numbers");
            		return;
            		}
                	model.removeNodeFromParent(node);
                    
                    Object nodeToBeRemoved = node.getUserObject();
                    //the order that is removed from new orders is placed under active orders in class "orderlist"
                    //perhaps all of this should be done once we know the e-mail is on the way though?
                    
                    activeOrderNode = new DefaultMutableTreeNode(node);
                    orderlist.activeOrderTreeModel.insertNodeInto(activeOrderNode, orderlist.activeRoot, orderlist.activeRoot.getChildCount());
                    for (int i = 0; i < node.getChildCount(); i++)
                    {
                    	activeDishNode = new DefaultMutableTreeNode(node.getChildAt(i));
                    	orderlist.activeOrderTreeModel.insertNodeInto(activeDishNode, activeOrderNode, activeOrderNode.getChildCount());
                    }
                    orderlist.activeOrdersTree.setModel(orderlist.activeOrderTreeModel);
                    PassWordDialog.orderMap.removeAll(nodeToBeRemoved);
                    
                    String mailString = "Thank you for your order. Your food will be ready for pick up in\n approximately " + time + " minutes. Kind regards. /The Aff crew";
                    sendMail(mailString);
                }
               }
            } 
         });  
                 
                    
                    

                
                
            
       
    
    
    
	//Pressing this button declines the order and generates an e-mail with an apology to the customer
    DecBtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            DefaultTreeModel model = (DefaultTreeModel) tree.getModel();

            TreePath[] paths = tree.getSelectionPaths();
            for (TreePath path : paths) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                if (node.getParent() != null && !node.isLeaf()) {
                    model.removeNodeFromParent(node);
                    
                    String declineString = "We are busy at the moment and can't process your order this time. Our sincerest apologies. /The AFF crew";
                    sendMail(declineString);
                }
            }
        }
    });
    
    
    
	//Just Layout
    treeScrollPane = new JScrollPane(tree);
    treeScrollPane.setViewportView(tree);
    spt = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,treeScrollPane,p);  
    int size = spt.getDividerSize();
    size = 2;
    spt.setDividerSize(size);
    spt.setResizeWeight(.5);  
    spt.setDividerLocation(200);
    
      
    p.setVisible(true);
    
    tree.getSelectionModel().setSelectionMode
    (TreeSelectionModel.SINGLE_TREE_SELECTION);
    
  }

protected void sendMail(String s) {
	// Recipient's email ID
    String to = emailAddress;

    // Sender's email ID 
    final String from = "asianfoodfinder222@gmail.com";
    final String password = "hejduhejdu";

    // Assuming you are sending email from localhost
    String host = "localhost";

    // Get system properties
    Properties properties = new Properties();

    // Setup mail server
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");
    properties.put("mail.smtp.host", "smtp.gmail.com");
    properties.put("mail.smtp.port", "587");
    // Get the default Session object.
    Session session = Session.getInstance(properties,
    		new javax.mail.Authenticator() {
    	protected PasswordAuthentication getPasswordAuthentication(){
    		return new PasswordAuthentication(from, password);
    	}
    });

    try{
       // Create a default MimeMessage object.
       Message message = new MimeMessage(session);

       // Set From: header field of the header.
       message.setFrom(new InternetAddress("asianfoodfinder222@gmail.com"));

       // Set To: header field of the header.
       message.addRecipient(Message.RecipientType.TO,
                                new InternetAddress(to));

       // Set Subject: header field
       message.setSubject("Dear " + text.getText() + "!");

       // Now set the actual message
       message.setText(s);

       // Send message
       Transport.send(message);
       JOptionPane.showMessageDialog(null,
    		    "Confirmation sent to " + text.getText());
       System.out.println("Sent message successfully....");
    }catch (MessagingException mex) {
       mex.printStackTrace();
    }
	
}


public JSplitPane getJSplitPanel() { 
	System.out.println("3");
return spt;

	
	}
      
}       

  
  