import javax.swing.*;
import java.awt.GridLayout;


public class PopUp {
public JPanel panel, mainpanel;	
JScrollPane Pane;
guiinterface guiinterface;
JLabel MenuItem, qty;
JButton accept, decline;
	
public PopUp(guiinterface guiinterface) { // intended to popup when an order is made so the restaurant owner can confirm
		this.guiinterface = guiinterface; // then it will be placed under the "orderlist" class, no functions yet.
				
		panel = new JPanel();
		panel.setLayout(new GridLayout(0,4));
		Pane = new JScrollPane(panel);
		Pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    Pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
						
	    	mainpanel = new JPanel();

				JLabel label1 = new JLabel("New Order!");
				panel.add(label1);
				JLabel label25 = new JLabel("");
				panel.add(label25);
				JLabel label26 = new JLabel("");
				panel.add(label26);
				JLabel label27 = new JLabel("");
				panel.add(label27);
				
				JLabel label2 = new JLabel("Menu Item:");
				panel.add(label2);
				JTextField label3 = new JTextField(10);
				panel.add(label3);
				label3.setEditable(false);
				
				JLabel label4 = new JLabel("QTY:");
				panel.add(label4);
				JTextField label5 = new JTextField(10);
				panel.add(label5);
				label5.setEditable(false);
				
				JLabel label6 = new JLabel("Menu Item:");
				panel.add(label6);
				JTextField label7 = new JTextField(10);
				panel.add(label7);
				label7.setEditable(false);
				
				JLabel label8 = new JLabel("QTY:");
				panel.add(label8);				
				JTextField label9 = new JTextField(10);
				panel.add(label9);
				label9.setEditable(false);
				
				JLabel label10 = new JLabel("Menu Item:");
				panel.add(label10);				
				JTextField label11 = new JTextField(10);
				panel.add(label11);
				label11.setEditable(false);
				
				JLabel label12 = new JLabel("QTY:");
				panel.add(label12);				
				JTextField label13 = new JTextField(10);
				panel.add(label13);
				label13.setEditable(false);
				
				JLabel label14 = new JLabel("Menu Item:");
				panel.add(label14);				
				JTextField label15 = new JTextField(10);
				panel.add(label15);
				label15.setEditable(false);
				
				JLabel label16 = new JLabel("QTY:");
				panel.add(label16);				
				JTextField label17 = new JTextField(10);
				panel.add(label17);
				label17.setEditable(false);
				
				JLabel label18 = new JLabel("Menu Item:");
				panel.add(label18);				
				JTextField label19 = new JTextField(10);
				panel.add(label19);
				label19.setEditable(false);
				
				JLabel label20 = new JLabel("QTY:");
				panel.add(label20);				
				JTextField label21 = new JTextField(10);
				panel.add(label21);
				label21.setEditable(false);
				

			    
			    mainpanel.add(Pane);
				panel.setVisible(true);
}
			/*	public JScrollPane getPopUp() {
					return Pane;
				}*/
					
					
				}