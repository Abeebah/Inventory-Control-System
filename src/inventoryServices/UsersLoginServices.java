package inventoryServices;

import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.Date;

import inventoryDatabase.ConnectionClass;
import inventoryModels.UsersLoginModel;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

public class UsersLoginServices {
	Connection conn = null;
	OraclePreparedStatement pst = null;
	OracleResultSet rs = null;
	ResultSet rs2,rs3 = null;
	
	UsersLoginModel usersLoginModel = new UsersLoginModel();
	public UsersLoginModel processUserLogin(String username, String password) {
	conn = ConnectionClass.dbconnect();
	try {
		String query = "SELECT * FROM USERS WHERE USERNAME=? and PASSWORD=?";
		pst = (OraclePreparedStatement) conn.prepareStatement(query);
		pst.setString(1, username);
		pst.setString(2, password);
		rs = (OracleResultSet) pst.executeQuery();
	
		if(rs.next()){
			usersLoginModel.setId(rs.getInt("id"));
			usersLoginModel.setFirstName(rs.getString("firstname"));
			usersLoginModel.setLastName(rs.getString("lastname"));
			usersLoginModel.setUserName(rs.getString("username"));
			usersLoginModel.setCompany(rs.getString("company"));
			usersLoginModel.setRole(rs.getString("role"));
			usersLoginModel.setShortCode(0);
			usersLoginModel.setShortMessage("Login Successful");
			}else{
			usersLoginModel.setShortCode(-1000);
			usersLoginModel.setShortMessage("Invalid Login Details");
			}
			}catch(Exception e) {
			usersLoginModel.setShortCode(-1001);
			usersLoginModel.setShortMessage("Connection Error");
			}
	return usersLoginModel;
	}
	
	public UsersLoginModel addUserData(String firstname, String lastname, String email, String phone, String username, String password, Object role, String company, int user_id) {
		conn = ConnectionClass.dbconnect();
		try {
			String query = "INSERT INTO users (firstname, lastname, email, phone, username, password,role, company, created_by) VALUES (?,?,?,?,?,?,?,?,?)";
			pst = (OraclePreparedStatement) conn.prepareStatement(query);
			pst.setString(1, firstname);
			pst.setString(2, lastname);
			pst.setString(3, email);
			pst.setString(4, phone);
			pst.setString(5, username);
			pst.setString(6, password);
			pst.setObject(7, role);
			pst.setString(8, company);
			pst.setInt(9, user_id);
			int ret = pst.executeUpdate();
			if(ret > 0) {
				usersLoginModel.setShortCode(0);
				usersLoginModel.setShortMessage("User added successfully");
			}
		}catch(Exception e) {
			usersLoginModel.setShortCode(20);
			usersLoginModel.setShortMessage("Problem adding user");
		}
		return usersLoginModel;
	}
	
	public UsersLoginModel updateUserData(String firstname, String lastname, String email,  String phone, String username, String password, Object role, String company,int user_id, int id) {
		conn = ConnectionClass.dbconnect();
		try {
			String date_modified;
			Date date = new Date();
			date_modified =date.toString();
			String query = "UPDATE users SET firstname=?, lastname=?, email=?,  phone=?, username=?, password=?, role=?, company=?,modified_by=?, date_modified=? WHERE id=?";
			pst = (OraclePreparedStatement) conn.prepareStatement(query);
			pst.setString(1, firstname);
			pst.setString(2, lastname);
			pst.setString(3, email);
			pst.setString(4, phone);
			pst.setString(5, username);
			pst.setString(6, password);
			pst.setObject(7, role);
			pst.setString(8, company);
			pst.setInt(9, user_id);
			pst.setString(10,date_modified );
			pst.setInt(11, id);
			int ret = pst.executeUpdate();
			if(ret > 0) {
				usersLoginModel.setShortCode(0);
				usersLoginModel.setShortMessage("User updated successfully");
			}
		}catch(Exception e) {
			usersLoginModel.setShortCode(20);
			usersLoginModel.setShortMessage("Problem updating user");
		}
		return usersLoginModel;
	}
 public UsersLoginModel deleteUserData(int id) {
	 conn = ConnectionClass.dbconnect();
	 try {
		 String query = "DELETE FROM users WHERE id=?";
		 pst = (OraclePreparedStatement) conn.prepareStatement(query);
		 pst.setInt(1, id);
		 int ret = pst.executeUpdate();
		 if(ret > 0) {
			 usersLoginModel.setShortCode(0);
			 usersLoginModel.setShortMessage("User deleted successfully");
		 }
	 }catch(Exception e) {
		 usersLoginModel.setShortCode(10);
		 usersLoginModel.setShortMessage("Problem deleting user");
	 }
	return usersLoginModel;
 }
 
 private String getModifiedName(int mID) {
		String modifiedUser = "";
		try {
 		conn = ConnectionClass.dbconnect();
 		String query = "SELECT * FROM users WHERE id=?";
 		pst = (OraclePreparedStatement) conn.prepareStatement(query);
 		pst.setInt(1, mID);
 		ResultSet rs2 = (OracleResultSet) pst.executeQuery();
 		while (rs2.next()) {
 			modifiedUser = rs2.getString("firstname") + " " + rs2.getString("lastname");	
			}
 		rs2.close();
 		pst.close();
 		
 	}catch(Exception e) {
 		e.printStackTrace();
 	}
		return modifiedUser;
	}   	
 
 private String getCreatedByName(int cID) {
		String createdByUser= "";
		try {
		conn = ConnectionClass.dbconnect();
		String query = "SELECT * FROM users WHERE id=?";
		pst = (OraclePreparedStatement) conn.prepareStatement(query);
		pst.setInt(1, cID);
		ResultSet rs3 = (OracleResultSet) pst.executeQuery();
		while (rs3.next()) {
			createdByUser = rs3.getString("firstname") + " " + rs3.getString("lastname");	
			}
		rs3.close();
		pst.close();
		
	}catch(Exception e) {
		e.printStackTrace();
	}
		return createdByUser;
	}   	
 
 public void showUsersData(JTable users_table) {
		try {
			conn = ConnectionClass.dbconnect();
			DefaultTableModel model = new DefaultTableModel();
			model.addColumn("ID");
			model.addColumn("First Name");
			model.addColumn("Last Name");
			model.addColumn("Email");
			model.addColumn("Phone");
			model.addColumn("Role");
			model.addColumn("Company");
			model.addColumn("Created By");
			model.addColumn("Modified By");
			model.addColumn("Date Modified");
			
			String query = "select * from users";
			pst = (OraclePreparedStatement) conn.prepareStatement(query);
			rs = (OracleResultSet) pst.executeQuery();
			while(rs.next()){
				int mID = rs.getInt("modified_by");
				int cID = rs.getInt("created_by");
				String modifiedUser = getModifiedName(mID);
				String createdByUser = getCreatedByName(cID);
				
				model.addRow(new Object[] {
						rs.getInt("id"), 
						rs.getString("firstname"),
						rs.getString("lastname"),
						rs.getString("email"),
						rs.getString("phone"),
						rs.getString("role"),
						rs.getString("company"),
						createdByUser,
						modifiedUser,
						
						rs.getString("date_modified")
						});
			}
			users_table.setModel(model);
			users_table.setAutoResizeMode(0);
			users_table.getColumnModel().getColumn(0).setPreferredWidth(20);
			users_table.getColumnModel().getColumn(1).setPreferredWidth(80);
			users_table.getColumnModel().getColumn(2).setPreferredWidth(80);
			users_table.getColumnModel().getColumn(3).setPreferredWidth(150);
			users_table.getColumnModel().getColumn(4).setPreferredWidth(80);
			users_table.getColumnModel().getColumn(5).setPreferredWidth(70);
			users_table.getColumnModel().getColumn(6).setPreferredWidth(100);
			users_table.getColumnModel().getColumn(7).setPreferredWidth(100);
			users_table.getColumnModel().getColumn(8).setPreferredWidth(100);
			users_table.getColumnModel().getColumn(9).setPreferredWidth(210);
			rs.close();
			pst.close();
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}


}
