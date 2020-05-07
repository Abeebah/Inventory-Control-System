package inventoryViews;

import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JSeparator;
import java.awt.Color;

public class Home {

	private JFrame frmInventoryManagementSystem;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home window = new Home();
					window.frmInventoryManagementSystem.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Home() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmInventoryManagementSystem = new JFrame();
		frmInventoryManagementSystem.getContentPane().setBackground(Color.WHITE);
		frmInventoryManagementSystem.setTitle("Inventory Management System");
		frmInventoryManagementSystem.setBounds(100, 100, 614, 582);
		frmInventoryManagementSystem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmInventoryManagementSystem.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("User Login");
		btnNewButton.setFont(new Font("Utsaah", Font.BOLD, 24));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new UsersLogin().setVisible(true);
				frmInventoryManagementSystem.setVisible(false);
			}
		});
		
		btnNewButton.setBounds(136, 301, 290, 47);
		frmInventoryManagementSystem.getContentPane().add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("Inventory Management System");
		lblNewLabel.setFont(new Font("Utsaah", Font.BOLD, 40));
		lblNewLabel.setBounds(60, 0, 444, 99);
		frmInventoryManagementSystem.getContentPane().add(lblNewLabel);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(47, 66, 471, 24);
		frmInventoryManagementSystem.getContentPane().add(separator);
		
		JLabel lblNewLabel_1 = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/bg_design.jpg")).getImage();
		lblNewLabel_1.setIcon(new ImageIcon(img));
		lblNewLabel_1.setBounds(10, 359, 578, 184);
		frmInventoryManagementSystem.getContentPane().add(lblNewLabel_1);
		
		JLabel label = new JLabel("");
		Image img_1 = new ImageIcon(this.getClass().getResource("/Users-icon.png")).getImage();
		label.setIcon(new ImageIcon(img_1));
		label.setBounds(196, 115, 211, 175);
		frmInventoryManagementSystem.getContentPane().add(label);
	}
}
