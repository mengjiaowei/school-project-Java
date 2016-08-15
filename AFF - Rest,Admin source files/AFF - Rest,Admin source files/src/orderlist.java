import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;


public class orderlist  {

public JSplitPane splitOrderPanel;
public static JTree activeOrdersTree;
public static DefaultMutableTreeNode activeRoot;  
public static DefaultTreeModel activeOrderTreeModel;
// had to create a new linked HashMap for the customers, the first one is not synchronized and problems are caused when the timer accesses it
public static LinkedHashMap<String, ArrayList<String>> waitingCustomerMap = new LinkedHashMap<String, ArrayList<String>>();

JPanel rightSidePanel;
JScrollPane leftSidePanel;

JTextArea customerTextArea;
JLabel customerLabel;
JButton doneBtn;
PassWordDialog pwd;
	

public orderlist() {
	buildOrderList();
}
//creates a JTree with the accepted orders. (The nodes of the actual tree are built from the NewOrder Class, when pressing "Accept")
void buildOrderList()
{
	
	activeRoot = new DefaultMutableTreeNode("Active orders:");
	activeOrderTreeModel = new DefaultTreeModel(activeRoot);
	activeOrdersTree = new JTree(activeRoot); // TODO remove activeRoot if necessary (it will be added to tree via NewOrder AcceptButton listener method)
	
	leftSidePanel = new JScrollPane(activeOrdersTree);
	
	rightSidePanel = new JPanel(new GridBagLayout());
	GridBagConstraints gbc = new GridBagConstraints();
	
	
	customerLabel = new JLabel("Customer information:");
	gbc.insets = new Insets(10, 10, 10, 10);
	gbc.gridx = 1;
	gbc.gridy = 1;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	rightSidePanel.add(customerLabel, gbc);
	
	customerTextArea = new JTextArea("Select an order\n from the list\n to display\n customer information");
	gbc.gridy = 2;
	gbc.gridx = 1;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	rightSidePanel.add(customerTextArea, gbc);
	
	doneBtn = new JButton("Done");
	gbc.gridy = 3;
	gbc.gridx = 1;
	rightSidePanel.add(doneBtn, gbc);
	
	/*TODO:
	 * 1 Selectionlistener displays customer information on rightsidepanel
	 * 2 "finish button" removes order from list + customer info from customer linkedhashmap
	 * 3 ...? 
	 * 
	 */
	
	
	//finally we initialize the SplitPanel by inserting left and right side panels to it
	splitOrderPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftSidePanel,rightSidePanel);  
	int size = splitOrderPanel.getDividerSize();
	size = 2;
	splitOrderPanel.setDividerSize(size);
	splitOrderPanel.setResizeWeight(.5);  
	splitOrderPanel.setDividerLocation(200);
	
	//the tree with active orders needs a selection listener
	activeOrdersTree.addTreeSelectionListener(new TreeSelectionListener() {
        public void valueChanged(TreeSelectionEvent e) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
            		activeOrdersTree.getLastSelectedPathComponent();

        // if nothing is selected 
            if (node == null || node.isLeaf()) return;

        // retrieve the node that was selected 
           
            Object nodeInfo = node.getUserObject();
            
                
            ArrayList<String> customerInfo = waitingCustomerMap.get(nodeInfo.toString());
            
            
        // React to the node selection. Show customer information on right side of this panel.
            customerTextArea.setText("Name: " +customerInfo.get(0) +"\n" + "Mail :" + customerInfo.get(1) +"\n" + "Time of order: " + customerInfo.get(2));
            
        }
    });
		//when the user clicks "done", the order is removed from the system
	 doneBtn.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            DefaultTreeModel model = (DefaultTreeModel) activeOrdersTree.getModel();

	            TreePath[] paths = activeOrdersTree.getSelectionPaths();
	            for (TreePath path : paths) {
	                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
	                if (node.getParent() != null && !node.isLeaf()) {
	                    model.removeNodeFromParent(node);
	                
	                }
	            }
	        }
	    });
	
	
	
	
}

		
		public JSplitPane getOrderList() 
		{
			return splitOrderPanel;
		}

	
}		
	
	

