package inventoryViews;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import inventoryDatabase.ConnectionClass;
import inventoryModels.UsersLoginModel;
import inventoryServices.UsersLoginServices;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Image;
import java.sql.Connection;

import javax.swing.JPasswordField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UsersLogin extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldLogin;
	private JPasswordField passwordFieldLogin;
	
	public UsersLogin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 516, 377);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Utsaah", Font.PLAIN, 25));
		lblUsername.setBounds(24, 129, 127, 40);
		contentPane.add(lblUsername);
		
		textFieldLogin = new JTextField();
		textFieldLogin.setBounds(142, 134, 310, 32);
		contentPane.add(textFieldLogin);
		textFieldLogin.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Utsaah", Font.PLAIN, 25));
		lblPassword.setBounds(24, 206, 127, 32);
		contentPane.add(lblPassword);	
		passwordFieldLogin = new JPasswordField();
		passwordFieldLogin.setBounds(142, 207, 310, 32);
		contentPane.add(passwordFieldLogin);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try 
				{
					if(textFieldLogin.getText().equals("") || passwordFieldLogin.getText().equals("")){
						JOptionPane.showMessageDialog(null, "All fields are required", "Login Error", JOptionPane.OK_OPTION);
					}else{	
					UsersLoginModel usersLoginModel = new UsersLoginServices().processUserLogin(textFieldLogin.getText(), passwordFieldLogin.getText());
					
					if(usersLoginModel.getShortCode() == 0) {
						JOptionPane.showMessageDialog(null, usersLoginModel.getShortMessage());
						new UsersDashboard(usersLoginModel.getFirstName(), usersLoginModel.getLastName(), usersLoginModel.getCompany(),usersLoginModel.getRole(), usersLoginModel.getId()).setVisible(true);
						setVisible(false);
						
						
						}else{
						JOptionPane.showMessageDialog(null, usersLoginModel.getShortMessage(), "Login Error", JOptionPane.OK_OPTION);
						}
				}
					}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				}
				});
		
		btnLogin.setFont(new Font("Utsaah", Font.PLAIN, 25));
		btnLogin.setBounds(266, 273, 186, 40);
		Image img_1 = new ImageIcon(this.getClass().getResource("/ok-icon.png")).getImage();
		btnLogin.setIcon(new ImageIcon(img_1));
		contentPane.add(btnLogin);
		
		JLabel lblNewLabel = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/passs.png")).getImage();
		lblNewLabel.setIcon(new ImageIcon(img));
		lblNewLabel.setBounds(28, 11, 110, 116);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("System Login");
		lblNewLabel_1.setFont(new Font("Utsaah", Font.BOLD, 40));
		lblNewLabel_1.setBounds(196, 52, 207, 40);
		contentPane.add(lblNewLabel_1);
		
		JLabel label = new JLabel("");
		
		label.setBounds(84, 273, 67, 54);
		contentPane.add(label);
	}
}
