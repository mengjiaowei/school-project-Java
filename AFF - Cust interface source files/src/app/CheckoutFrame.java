package app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CheckoutFrame extends JFrame{

	
	
	private JButton back, finish;
	private JPanel personinfo, name, email, phone, address;
	private JLabel personinfo1, name1, email1, phone1, address1;
	private JTextField name2, email2, phone2;
	private JTextArea address2;
	private JScrollPane scroll;
	private App app;
	public static String namestr,emailstr,phonestr,addstr;

	CheckoutFrame(App a){
		app = a;

		/*----------------------personal info--------------------------------------------------*/
//		setTitle("Asian Food Finder");
		setSize(600,500);
		setResizable(false);
		setLayout(null);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//create the shopping cart
		personinfo = new JPanel();
		personinfo.setBounds(10,50,600,50);
		personinfo1 = new JLabel("Please enter your personal information.");
//		personinfo1.setFont(new java.awt.Font("Monospaced",1,20));
		personinfo.add(personinfo1);
		personinfo.setVisible(true);

		name = new JPanel();
		name.setSize(100,10);
		name.setBounds(5,100,300,100);	
		name1 = new JLabel("                Name:               ");
		name1.setFont(new java.awt.Font("Dialog",1,15));
		name2 = new JTextField(15);
		name2.setEditable(true);
		name.add(name1);
		name.add(name2);
		name.setVisible(true);

		email = new JPanel();
		email.setSize(100,10);
		email.setBounds(300,100,300,100);	
		email1 = new JLabel("Email Address:");
		email1.setFont(new java.awt.Font("Dialog",1,15));
		email2 = new JTextField(20);
		email2.setEditable(true);
		email.add(email1);
		email.add(email2);
		email.setVisible(true);

		phone = new JPanel();
		phone.setSize(100,100);
		phone.setBounds(5,300,300,100);	
		phone1 = new JLabel("          Phone Number:          ");
		phone1.setFont(new java.awt.Font("Dialog",1,15));
		phone2 = new JTextField(15);
		phone2.setEditable(true);
		phone.add(phone1);
		phone.add(phone2);
		phone.setVisible(true);

		address = new JPanel();
		address.setSize(100,100);
		address.setBounds(300,300,300,100);	
		address1 = new JLabel("                 Address:                ");
		address1.setFont(new java.awt.Font("Dialog",1,15));
	
		address2 = new JTextArea();
		scroll = new JScrollPane(address2);
		address2.setPreferredSize(new Dimension(250,60));
		
		
		address2.setEditable(true);
		address.add(address1);
		address.add(address2);
		address.add(scroll);
		address.setVisible(true);
		
		back = new JButton("Back");
		back.setBounds(10,400,500,400);
		back.setSize(75,35);
		back.setVisible(true);
		back.addActionListener(new ActionListener(){


			public void actionPerformed(ActionEvent e) {
				System.out.println("go back!!!!");
				app.checkoutFrame.setVisible(false);
				app.restaurantFrame.setVisible(true);
				
			}

		});

		finish = new JButton("Finish");
		finish.setBounds(450,400,100,100);
		finish.setSize(75,35);
		finish.setVisible(true);
		finish.addActionListener(new ActionListener(){


			public void actionPerformed(ActionEvent e) {
				namestr = name2.getText();
				System.out.println(namestr);
				addstr = address2.getText();
				System.out.println(addstr);
				phonestr = phone2.getText();
				System.out.println(phonestr);
				emailstr = email2.getText();
				System.out.println(emailstr);
				
				
				JOptionPane.showMessageDialog(rootPane,
						"Thank you for your order. Once it is processed, and email wil be sent to you.");
				app.orderTest = new orderTest(app);
				System.exit(0);
			}

		});
		
		add(personinfo);
		add(name);
		add(email);
		add(phone);
		add(address);
		add(scroll);
		add(back);
		add(finish);
		setVisible(true);

	}
	
}