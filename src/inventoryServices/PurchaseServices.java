package inventoryServices;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import inventoryDatabase.ConnectionClass;
import inventoryModels.PurchaseModel;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

public class PurchaseServices {
	Connection conn = null;
	OraclePreparedStatement pst = null;
	OracleResultSet rs = null;
	OracleResultSet rs2 = null;
	OracleResultSet rs3 = null;
	ResultSet rs4 = null;
	ResultSet rs7 = null;
	OracleResultSet rs5 = null;
	//PreparedStatement pst2 =null;
	HashMap <String, Integer> productMap = new HashMap<String, Integer>();
	PurchaseModel purchaseModel = new PurchaseModel();

	public PurchaseModel addPurchaseData(int product, int quantity, int user_id) {
		conn = ConnectionClass.dbconnect();
		PreparedStatement pst1 = null;
		PreparedStatement pst2 = null;
		try {
			String query = "INSERT INTO purchase (product_id, quantity, created_by) VALUES (?, ?, ?)";

			int qty = getPreviousQuantity(product);
			int nQty = qty + quantity;

			String queryy = "update products set quantity=? where id=?";

			pst1 = conn.prepareStatement(query);
			pst1.setInt(1, product);
			pst1.setInt(2, quantity);
			pst1.setInt(3, user_id);
			int ret = pst1.executeUpdate();

			pst2 = conn.prepareStatement(queryy);
			pst2.setInt(1, nQty);
			pst2.setInt(2, product);	
			int ret2 = pst2.executeUpdate();

			if((ret > 0) && (ret2 > 0)){
				purchaseModel.setShortCode(0);
				purchaseModel.setShortMessage("Purchase Added Successfully");
			}
		}catch(Exception e){

			purchaseModel.setShortCode(-1000);
			purchaseModel.setShortMessage("Connection Error");
		}
		return purchaseModel;
	}

	private int getPreviousQuantity(int product) {
		PreparedStatement pst1 = null;
		ResultSet rs1 = null;
		int qtty = 0;
		try {
			String query = "SELECT * FROM products WHERE id=?";
			pst1 = conn.prepareStatement(query);
			pst1.setInt(1, product);
			rs1 = pst1.executeQuery();
			while(rs1.next()) {
				qtty = rs1.getInt("quantity");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return qtty;
	}


	public PurchaseModel updatePurchaseData(int id, int product, int quantity, int user_id) {
		conn = ConnectionClass.dbconnect();
		String date_modified;
		Date date = new Date();
		date_modified =date.toString();
		PreparedStatement pst3, pst4 = null;
		ResultSet rs3 = null;	
		try {
			int newPurchaseQuantity = 0;
			int newProductQuantity = 0;
			String query = "update purchase set quantity=?, modified_by=?, date_modified=? where id=?";
			String query1 = "update products set quantity=? where id=?";
			int purchaseQty = getPurchaseQuantity(id);
			int productQuantity = getPreviousQuantity(product);
			if(purchaseQty > quantity) {
				newPurchaseQuantity =  purchaseQty - quantity;
				newProductQuantity = productQuantity - newPurchaseQuantity;
			}else if(purchaseQty < quantity) {
				newPurchaseQuantity = quantity - purchaseQty;
				newProductQuantity = productQuantity + newPurchaseQuantity;
			}


			pst3 = conn.prepareStatement(query);
			pst3.setInt(1, quantity);
			pst3.setInt(2, user_id);
			pst3.setString(3, date_modified);
			pst3.setInt(4, id);


			pst4 = conn.prepareStatement(query1);
			pst4.setInt(1, newProductQuantity);
			pst4.setInt(2, product);


			int ret = pst3.executeUpdate();
			int ret2 = pst4.executeUpdate();

			if((ret > 0 ) && (ret2 > 0)){
				purchaseModel.setShortCode(0);
				purchaseModel.setShortMessage("Purchase Updated Successfully");
			}
		} catch (Exception e) {
			purchaseModel.setShortCode(-1000);
			purchaseModel.setShortMessage("Connection Error");
		}
		return purchaseModel;	
	}

	private int getPurchaseQuantity(int id){
		PreparedStatement pst3 = null;
		ResultSet rs3 = null;
		int previousQty = 0;
		try {
			String query = "SELECT * FROM PURCHASE WHERE id=?"; 
			pst3 = conn.prepareStatement(query);
			pst3.setInt(1, id);
			rs3 = pst3.executeQuery();
			while(rs3.next()){
				previousQty = rs3.getInt("quantity");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return previousQty;	
	}


	public PurchaseModel deletePurchaseData(int id) {
		conn = ConnectionClass.dbconnect();
		PreparedStatement pst5 = null;
		ResultSet rs5 = null;
		String query = "DELETE FROM PURCHASE WHERE id = ?";
		try {
			pst5 = conn.prepareStatement(query);
			pst5.setInt(1, id);
			int ret = pst5.executeUpdate();
			if(ret > 0) {
				purchaseModel.setShortCode(0);
				purchaseModel.setShortMessage("Purchase deleted succesfully");
			}
		} catch (Exception e) {
			purchaseModel.setShortCode(-1000);
			purchaseModel.setShortMessage("Connection Error");
		}
		return purchaseModel;
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

	public PurchaseModel showPurchaseData(JTable purchase_table) {
		conn = ConnectionClass.dbconnect();
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("ID");
		model.addColumn("Date Created");
		model.addColumn("Product");
		model.addColumn("Quantity");
		model.addColumn("Cost");
		model.addColumn("Total");
		model.addColumn("Created By");
		model.addColumn("Modified By");
		model.addColumn("Date Modified");

		String query = "SELECT PURCHASE.*, PRODUCTS.NAME, PRODUCTS.COST, USERS.FIRSTNAME, USERS.LASTNAME FROM PURCHASE INNER JOIN PRODUCTS ON PURCHASE.PRODUCT_ID = PRODUCTS.ID INNER JOIN USERS ON PURCHASE.CREATED_BY=USERS.ID"; 
		try {
			pst = (OraclePreparedStatement) conn.prepareStatement(query);
			rs4 = pst.executeQuery();
			double total = 0.0;
			String fullName = "";
			while(rs4.next()) {
				int mID = rs4.getInt("modified_by");
				String modifiedUser = getModifiedName(mID);
				fullName = rs4.getString("firstname") +" " + rs4.getString("lastname");
				total = rs4.getDouble("cost") * rs4.getInt("quantity");
				model.addRow(new Object[] {
						rs4.getInt("id"),
						rs4.getString("date_created"),
						rs4.getString("name"),
						rs4.getInt("quantity"),
						rs4.getDouble("cost"),
						total,
						fullName,
						modifiedUser,
						rs4.getString("date_modified")
				});
			}
			rs4.close();
			pst.close();
			purchase_table.setModel(model);
			purchase_table.setAutoResizeMode(0);
			purchase_table.getColumnModel().getColumn(0).setPreferredWidth(20);
			purchase_table.getColumnModel().getColumn(1).setPreferredWidth(100);
			purchase_table.getColumnModel().getColumn(2).setPreferredWidth(100);
			purchase_table.getColumnModel().getColumn(3).setPreferredWidth(50);
			purchase_table.getColumnModel().getColumn(4).setPreferredWidth(70);
			purchase_table.getColumnModel().getColumn(5).setPreferredWidth(70);
			purchase_table.getColumnModel().getColumn(6).setPreferredWidth(100);
			purchase_table.getColumnModel().getColumn(7).setPreferredWidth(100);
			purchase_table.getColumnModel().getColumn(8).setPreferredWidth(190);
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return purchaseModel;
	}

	public HashMap<String, Integer> populateProduct(JComboBox<String> productsComboBox) {
		conn = ConnectionClass.dbconnect();
		try {
			String query = "SELECT * FROM products";
			pst = (OraclePreparedStatement) conn.prepareStatement(query);
			rs2 = (OracleResultSet) pst.executeQuery();
			//productsComboBox.removeAllItems();
			productsComboBox.addItem("=Select Product=");
			while(rs2.next()){
				productsComboBox.addItem(rs2.getString("name"));
				String name = rs2.getString("name");
				int id = rs2.getInt("id");
				productMap.put(name, id);
			}
		}catch(Exception e){
			e.printStackTrace();
		}	
		return productMap;
	}

	public int getKeyForSelectedProduct(String product, HashMap <String, Integer> productMap) {
		return productMap.get(product);
	}

}
