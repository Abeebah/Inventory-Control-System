package inventoryServices;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import inventoryDatabase.ConnectionClass;
import inventoryModels.CategoryModel;
import inventoryModels.UsersLoginModel;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

public class CategoryServices {
	UsersLoginModel usersLoginModel = new UsersLoginModel();
	Connection conn = null;
	OraclePreparedStatement pst = null;
	OracleResultSet rs = null;
	OracleResultSet rs1 = null;
	OracleResultSet rs2 = null;
	
	CategoryModel categoryModel = new CategoryModel();
	public CategoryModel addCategoryData(int ids, String name, String description, int user_id) {
		conn = ConnectionClass.dbconnect();
		
		try {
			
			String query = "INSERT INTO CATEGORY (id, cat_name, description,created_by) VALUES (?, ?, ?, ?)";
			pst = (OraclePreparedStatement) conn.prepareStatement(query);
			pst.setInt(1, ids);
			pst.setString(2, name);
			pst.setString(3, description);
			pst.setInt(4, user_id);
			
			int ret = pst.executeUpdate();
			if(ret > 0){
				categoryModel.setShortCode(0);
				categoryModel.setShortMessage("Category Added Successfully");
			}
		}catch(Exception e){
			categoryModel.setShortCode(-1000);
			categoryModel.setShortMessage("Connection Error");
		}
		return categoryModel;
	}
	
	public int getRows() {
		conn = ConnectionClass.dbconnect();
		int rows = 0;
		try {
			String query = "SELECT * FROM category";
			pst = (OraclePreparedStatement) conn.prepareStatement(query);
			rs1 = (OracleResultSet) pst.executeQuery();
			while(rs1.next()) {
				rows++;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return rows;
	}
	
	
	public int generateID() {
		conn = ConnectionClass.dbconnect();
		int[] id;
		int max = 0;
		try {
			String query = "SELECT * FROM category";
			pst = (OraclePreparedStatement) conn.prepareStatement(query);
			rs = (OracleResultSet) pst.executeQuery();
			int leng = getRows();
			id = new int[leng];
			int j = 0;
			while(rs.next()) {
				id[j] = rs.getInt("id");
				j++;
			}
			max = getMaxValue(id);
			max++;
		}catch(Exception e){
			e.printStackTrace();
		}
		return max;
	}

	public static int getMaxValue(int[] numbers){
		  int maxValue = numbers[0];
		  for(int i=1; i < numbers.length;i++){
		    if(numbers[i] > maxValue){
			  maxValue = numbers[i];
			} 
		  }
		  return maxValue;
}
	
	public CategoryModel updateCategoryData(int id, String name, String description, int user_id) {
		conn = ConnectionClass.dbconnect();
		String date_modified;
		Date date = new Date();
		date_modified =date.toString();
		try {
			String query = "UPDATE category SET cat_name=?, description=?, modified_by=?, date_modified=? WHERE id=?";
			pst = (OraclePreparedStatement) conn.prepareStatement(query);
			pst.setString(1, name);
			pst.setString(2, description);
			pst.setInt(3, user_id);
			pst.setString(4,date_modified);
			pst.setInt(5, id);
			int ret = pst.executeUpdate();
			if(ret > 0){
				categoryModel.setShortCode(0);
				categoryModel.setShortMessage("Category Updated Successfully");
			}
		}catch(Exception e){
			categoryModel.setShortCode(-1000);
			categoryModel.setShortMessage("Connection Error");
		}
		
		return categoryModel;
	}
	
	
	public CategoryModel deleteCategoryData(int id) {
		conn = ConnectionClass.dbconnect();
		try {
			String query = "DELETE FROM category WHERE id=?";
			pst = (OraclePreparedStatement) conn.prepareStatement(query);
			pst.setInt(1, id);
			int ret = pst.executeUpdate();
			if(ret > 0){
				categoryModel.setShortCode(0);
				categoryModel.setShortMessage("Category deleted Successfully");
			}
		}catch(Exception e){
			categoryModel.setShortCode(-1000);
			categoryModel.setShortMessage("Connection Error");
		}
		
		return categoryModel;
			
	}
		
		 public void showCategoryData(JTable table, String name) {
		    	try {
		    		conn = ConnectionClass.dbconnect();
		    		DefaultTableModel model = new DefaultTableModel();
		    		model.addColumn("ID");
		    		model.addColumn("Date Created");
		    		model.addColumn("Name");
		    		model.addColumn("Description");
		    		model.addColumn("Created By");
		    		model.addColumn("Modified By");
		    		model.addColumn("Date Modified");
		    		String query = "SELECT category.*, users.firstname, users.lastname FROM category INNER JOIN USERS ON USERS.ID = category.CREATED_BY";
		    		pst = (OraclePreparedStatement) conn.prepareStatement(query);
		    		rs = (OracleResultSet) pst.executeQuery();
		    		String fullName = "";
		    		while (rs.next()) {
		    			fullName = rs.getString("firstname") + " " + rs.getString("lastname");
		    			int mID = rs.getInt("modified_by");
		    			String mName = getModifiedName(mID);
	    				model.addRow(new Object[] {
	    					rs.getInt("id"), 
	    					rs.getString("date_created"),
	    					rs.getString("cat_name"),
	    					rs.getString("description"),
	    					fullName,
	    					mName,
	    					rs.getString("date_modified"),
	    			});
	    		}
		    		rs.close();
		    		pst.close();
		    		table.setModel(model);
		    		table.setAutoResizeMode(0);
		    		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		    		table.getColumnModel().getColumn(1).setPreferredWidth(120);
		    		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		    		table.getColumnModel().getColumn(3).setPreferredWidth(120);
		    		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		    		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		    		table.getColumnModel().getColumn(6).setPreferredWidth(210);
		    		
		    	}catch(Exception e) {
		    		categoryModel.setShortCode(-1000);
					categoryModel.setShortMessage("Connection Error");
		    	}
		 }

		private String getModifiedName(int mID) {
			String mName = "";
			try {
	    		conn = ConnectionClass.dbconnect();
	    		String query = "SELECT * FROM users WHERE id=?";
	    		pst = (OraclePreparedStatement) conn.prepareStatement(query);
	    		pst.setInt(1, mID);
	    		ResultSet rs1 = (OracleResultSet) pst.executeQuery();
	    		while (rs1.next()) {
	    			mName = rs1.getString("firstname") + " " + rs1.getString("lastname");	
    			}
	    		rs1.close();
	    		pst.close();
	    		
	    	}catch(Exception e) {
	    		e.printStackTrace();
	    	}
			return mName;
		}   		
		    		
}		
		    		
		
