package inventoryServices;

import java.sql.Connection;

import java.sql.ResultSet;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import inventoryDatabase.ConnectionClass;

import inventoryModels.CustomersModel;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CustomerServices {
	Connection conn = null;
	OraclePreparedStatement pst = null;
	ResultSet rs = null;
	CustomersModel customersModel = new CustomersModel();
	
	public CustomersModel addCustomerData(String firstName, String lastName, String email, String phone, String address, int user_id) {
		conn = ConnectionClass.dbconnect();
		try {
			String query = "INSERT INTO CUSTOMERS (firstName, lastName, email, phone, address, created_by) VALUES (?, ?, ?, ?, ?, ?)";
			pst = (OraclePreparedStatement) conn.prepareStatement(query);
			pst.setString(1, firstName);
			pst.setString(2, lastName);
			pst.setString(3, email);
			pst.setString(4, phone);
			pst.setString(5, address);
			pst.setInt(6, user_id);
			int ret = pst.executeUpdate();
			if(ret > 0) {
				customersModel.setShortCode(0);
				customersModel.setShortMessage("Customer Added Successfully");
			}
		} catch (Exception e) {
			customersModel.setShortCode(-1000);
			customersModel.setShortMessage("Connection Error");
		}
		
		return customersModel;
		
	}
	
	public CustomersModel updateCustomeryData(int id, String firstName, String lastName,String email, String phone, String address, int user_id) {
		conn = ConnectionClass.dbconnect();
		String date_modified;
		Date date = new Date();
		date_modified =date.toString();
		try {
			String query = "UPDATE customers SET firstname=?,lastname=?, email=?, phone=?, address=?, modified_by=?, date_modified=? WHERE id=?";
			pst = (OraclePreparedStatement) conn.prepareStatement(query);
			pst.setString(1, firstName);
			pst.setString(2, lastName);
			pst.setString(3, email);
			pst.setString(4, phone);
			pst.setString(5, address);
			pst.setInt(6, user_id);
			pst.setString(7, date_modified);
			pst.setInt(8, id);
			int ret = pst.executeUpdate();
			if(ret > 0){
				customersModel.setShortCode(0);
				customersModel.setShortMessage("Customer Updated Successfully");
			}
		}catch(Exception e){
			customersModel.setShortCode(-1000);
			customersModel.setShortMessage("Connection Error");
		}
		return customersModel;	
	}
	
	public CustomersModel deleteCustomerData(int id) {
		conn = ConnectionClass.dbconnect();
		try {
			String query = "DELETE FROM customers WHERE id=?";
			pst = (OraclePreparedStatement) conn.prepareStatement(query);
			pst.setInt(1, id);
			int ret = pst.executeUpdate();
			if(ret > 0){
				customersModel.setShortCode(0);
				customersModel.setShortMessage("Customer deleted Successfully");
			}
		}catch(Exception e){
			customersModel.setShortCode(-1000);
			customersModel.setShortMessage("Connection Error");
		}
		
		return customersModel;
			
	}
	
	 public int validateEmail(String emailStr)
	    {
		 int e = 0;
	        Matcher matcher = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(emailStr);
	        try {
		        if(matcher.find())
		        {
		           e= 0;
		        }
		        else
		        {
		           e = 1;
		        }
	        }catch(Exception exx) {
	        	JOptionPane.showMessageDialog(null, e);
	    }
			return e;
	    }
	
		//check if value is an integer
	    public int checkIfInt(String q) {
	    	int pl = q.length();
	    	int r = 0;
	    	for(int i = 0; i<pl; i++) {
	    		try {
		    		String a = "" + q.charAt(i);
		    		int b =  Integer.parseInt(a);
		    		r = 0;
	    		}catch(Exception e) {
		    		r = 1;
		    		break;
		    	}
	    	}
	    	return r;
	    }
	    
	    private String getModifiedName(int mID) {
			String modifiedUser = "";
			try {
	    		conn = ConnectionClass.dbconnect();
	    		String query = "SELECT * FROM users WHERE id=?";
	    		pst = (OraclePreparedStatement) conn.prepareStatement(query);
	    		pst.setInt(1, mID);
	    		ResultSet rs7 = (OracleResultSet) pst.executeQuery();
	    		while (rs7.next()) {
	    			modifiedUser = rs7.getString("firstname") + " " + rs7.getString("lastname");	
				}
	    		rs7.close();
	    		pst.close();
	    		
	    	}catch(Exception e) {
	    		e.printStackTrace();
	    	}
			return modifiedUser;
		}   	
	 
	public void showCustomerData(JTable customers_table) {
		conn = ConnectionClass.dbconnect();
		try {
			DefaultTableModel model = new DefaultTableModel();
			model.addColumn("ID");
			model.addColumn("Date Created");
			model.addColumn("First Name");
			model.addColumn("Last Name");
			model.addColumn("Email");
			model.addColumn("Phone Number");
			model.addColumn("Address");
			model.addColumn("Created by");
			model.addColumn("Modified By");
			model.addColumn("Date Modified");
			
			String query = "SELECT CUSTOMERS .*,users.firstname as name, users.lastname as last FROM CUSTOMERS INNER JOIN USERS ON CUSTOMERS.CREATED_BY=USERS.ID";
			pst = (OraclePreparedStatement) conn.prepareStatement(query);
			rs = (OracleResultSet) pst.executeQuery();
			String fullName;
			while(rs.next()){
				int mID = rs.getInt("modified_by");
				String modifiedUser = getModifiedName(mID);
				fullName = rs.getString("name") +" " + rs.getString("last");
				model.addRow(new Object[] {
						rs.getInt("id"),
						rs.getString("date_created"),
						rs.getString("firstName"),
						rs.getString("lastName"),
						rs.getString("email"),
						rs.getString("phone"),
						rs.getString("address"),
						fullName,
						modifiedUser,
						rs.getString("date_modified")
				});
			}
				rs.close();
				pst.close();
				
				customers_table.setModel(model);
				customers_table.setAutoResizeMode(0);
				customers_table.getColumnModel().getColumn(0).setPreferredWidth(20);
				customers_table.getColumnModel().getColumn(1).setPreferredWidth(100);
				customers_table.getColumnModel().getColumn(2).setPreferredWidth(100);
				customers_table.getColumnModel().getColumn(3).setPreferredWidth(100);
				customers_table.getColumnModel().getColumn(4).setPreferredWidth(200);
				customers_table.getColumnModel().getColumn(5).setPreferredWidth(100);
				customers_table.getColumnModel().getColumn(6).setPreferredWidth(100);
				customers_table.getColumnModel().getColumn(7).setPreferredWidth(100);
				customers_table.getColumnModel().getColumn(8).setPreferredWidth(100);
				customers_table.getColumnModel().getColumn(9).setPreferredWidth(210);
			
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, e);
	}
			
		
	}	

	}
