package inventoryServices;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartUtilities;
//import org.jfree.chart.JFreeChart;
//import org.jfree.data.general.DefaultPieDataset;

import inventoryDatabase.ConnectionClass;
import inventoryModels.SalesModel;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

public class SalesServices {
	Connection conn = null;
	OraclePreparedStatement pst,pst9 = null;
	PreparedStatement pst5 = null;
	PreparedStatement pst6 = null;
	PreparedStatement pst3 = null;
	PreparedStatement pst2 = null;
	PreparedStatement pst1 = null;
	PreparedStatement pst7, pst8 = null;
	ResultSet rs8 = null;
	ResultSet rs5 = null;
	ResultSet rs4 = null;
	ResultSet rs6 = null;
	ResultSet rs3 = null;
	ResultSet rs2 = null;
	ResultSet rs1 = null;
	ResultSet rs = null;
	// ResultSet rs2 = null;
	// ResultSet rs3 = null;
	ResultSet rs7;

	HashMap<String, Integer> customerMap = new HashMap<String, Integer>();
	HashMap<String, Integer> customerProductMap = new HashMap<String, Integer>();

	SalesModel salesModel = new SalesModel();

	public SalesModel addSaleData(int product_id, int customer_id, int quantity,int user_id) {
		conn = ConnectionClass.dbconnect();
		int qty = getPreviousQuantity(product_id);
		int newQty = qty - quantity;
		if(quantity > qty) {
			salesModel.setShortCode(20);
			salesModel.setShortMessage("Product out of stock");
		}else {
		try {
			String query = "INSERT INTO SALLES (product_id, customer_id, sale_quantity, created_by) VALUES (?,?,?,?)";
			pst1 = conn.prepareStatement(query);
			pst1.setInt(1, product_id);
			pst1.setInt(2, customer_id);
			pst1.setInt(3, quantity);
			pst1.setInt(4, user_id);
			int ret = pst1.executeUpdate();
			
			String query1 = "UPDATE products SET quantity=? WHERE id=?";
			pst2 = conn.prepareStatement(query1);
			pst2.setInt(1, newQty);
			pst2.setInt(2, product_id);
			int ret2 = pst2.executeUpdate();
			
			if((ret>0) && (ret2>0)) {
				salesModel.setShortCode(0);
				salesModel.setShortMessage("Sale added successfully");
			}
			
		} catch (Exception e) {
			salesModel.setShortCode(-1000);
			salesModel.setShortMessage("Connection error");
		}
		}
		return salesModel;
	}
	
	
	public SalesModel updatSaleData(int id,int product_id, int quantity, int user_id) {
		conn = ConnectionClass.dbconnect();
		String date_modified;
		Date date = new Date();
		date_modified =date.toString();
		int newSaleQuantity = 0;
		int newProductQuantity = 0;
		int productQty = getPreviousQuantity(product_id);
		int saleQty = getPreviousSaleQuantity(id);
		try {
			String query = "UPDATE salles SET sale_quantity=?, modified_by=?, date_modified=? WHERE id=?";
			String query1 ="UPDATE products SET quantity=? WHERE id=?";
			if(quantity > saleQty) {
				newSaleQuantity = quantity - saleQty;
				newProductQuantity = productQty - newSaleQuantity;
			}else {
				newSaleQuantity = saleQty - quantity;
				newProductQuantity = productQty + newSaleQuantity;
			}
			pst7 = conn.prepareStatement(query);
			pst7.setInt(1, quantity);
			pst7.setInt(2, user_id);
			pst7.setString(3, date_modified);
			pst7.setInt(4, id);
			
			pst8 = conn.prepareStatement(query1);
			pst8.setInt(1, newProductQuantity);
			pst8.setInt(2, product_id);
			
			int ret = pst7.executeUpdate();
			int ret2 = pst8.executeUpdate();
			
			if((ret>0) && (ret2 > 0)) {
				salesModel.setShortCode(0);
				salesModel.setShortMessage("Sale Updated Successfully");
			}
		}catch(Exception e) {
			salesModel.setShortCode(-1000);
			salesModel.setShortMessage("Connection Error");
		}
		return salesModel;
	}
	
	public SalesModel deleteSaleData(int id) {
		conn = ConnectionClass.dbconnect();
		try {
			String query = "DELETE FROM salles WHERE id=?";
			pst9 = (OraclePreparedStatement) conn.prepareStatement(query);
			pst9.setInt(1, id);
			int ret = pst9.executeUpdate();
			if(ret > 0) {
				salesModel.setShortCode(0);
				salesModel.setShortMessage("Sale deleted successfully");
			}
		}catch(Exception e) {
			salesModel.setShortCode(10);
			salesModel.setShortMessage("Problem deleting sale");
		}
		return salesModel;
	}
	
	
	private int getPreviousSaleQuantity(int id) {
		int qtty =0;
		try {
			String query = "SELECT * FROM salles WHERE id=?";
			pst7 = conn.prepareStatement(query);
			pst7.setInt(1, id);
			rs7 = pst7.executeQuery();
			while(rs7.next()) {
				qtty = rs7.getInt("sale_quantity");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return qtty;
	}


	private int getPreviousQuantity(int product_id) {
		int qtty = 0;
		try {
			String query = "SELECT * FROM products WHERE id=?";
			pst2 = conn.prepareStatement(query);
			pst2.setInt(1, product_id);
			rs2 = pst2.executeQuery();
			while(rs2.next()) {
				qtty = rs2.getInt("quantity");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return qtty;
	}
	
	private int getTotalQuantity(int product_id) {
		int qtty = 0;
		try {
			String query = "SELECT * FROM salles WHERE product_id=?";
			pst =(OraclePreparedStatement) conn.prepareStatement(query);
			pst2.setInt(1, product_id);
			rs2 = pst2.executeQuery();
			//int sum = rs2.getInt("SUM(SALE_QUANTITY)");
			while(rs2.next()) {
				qtty += rs2.getInt("sale_quantity");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return qtty;
		
	}
	
	private int getProductId(String name) {
		PreparedStatement statement5 = null;
		ResultSet resultSet5 = null;
		int productId =0;
		try {
			String query = "SELECT * FROM products where name=?";
			statement5 = (PreparedStatement) conn.prepareStatement(query);
			statement5.setString(1, name);
			resultSet5 = statement5.executeQuery();
			while(resultSet5.next()) {
				productId = resultSet5.getInt("id");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return productId;
		}

	 //check if value is an integer
    public int checkIfInt(String q) {
    	int r = 0;
    	try {
    		int x = Integer.parseInt(q);
    		r = 0;
    	}catch(Exception e) {
    		r = 1;
    	}
    	return r;
    }
    
    /*public void showchartt() {
    	conn = ConnectionClass.dbconnect();
    	try {
    		
    	//	DefaultPieDataset dataset = new DefaultPieDataset();
    		String querys = "SELECT DISTINCT Product_id, products.name from salles INNER JOIN PRODUCTS ON SALLES.PRODUCT_ID = PRODUCTS.ID";
    		pst = (OraclePreparedStatement)conn.prepareStatement(querys);
    		rs8 = pst.executeQuery();
    		
    		while(rs8.next()) {
    			int totalQtty = 0;
    			int pid = rs8.getInt("product_id");
    			
    			totalQtty = getTotalQuantity(pid);
    			
    			
    			dataset.setValue(
    					rs.getString("name"),
    					totalQtty
    			);
    			
    		}
    		
    		
    		 JFreeChart chart = ChartFactory.createPieChart(
    		         "Product Sales",         
    		         dataset,             
    		         true,                    
    		         true,           
    		         false );
    		 int width = 560;   
    	      int height = 370;  
    	      File pieChart = new File( "Pie_Chart.jpeg" );
    	      ChartUtilities.saveChartAsJPEG( pieChart , chart , width , height );
    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}
		
    }*/
    private String getModifiedName(int mID) {
		String modifiedUser = "";
		try {
    		conn = ConnectionClass.dbconnect();
    		String query = "SELECT * FROM users WHERE id=?";
    		pst = (OraclePreparedStatement) conn.prepareStatement(query);
    		pst.setInt(1, mID);
    		ResultSet rs4 = (OracleResultSet) pst.executeQuery();
    		while (rs4.next()) {
    			modifiedUser = rs4.getString("firstname") + " " + rs4.getString("lastname");	
			}
    		rs4.close();
    		pst.close();
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
		return modifiedUser;
	}   	
    
    public SalesModel showSaleData(JTable sales_table) {
    	conn = ConnectionClass.dbconnect();
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("ID");
		model.addColumn("Date Created");
		model.addColumn("Product");
		model.addColumn("Quantity");
		model.addColumn("Price");
		model.addColumn("Total");
		model.addColumn("Created By");
		model.addColumn("Modified By");
		model.addColumn("Date Modified");

		String query = "SELECT salles.*, PRODUCTS.NAME, PRODUCTS.PRICE, USERS.FIRSTNAME, USERS.LASTNAME FROM salles INNER JOIN PRODUCTS ON SALLES.PRODUCT_ID = PRODUCTS.ID INNER JOIN USERS ON SALLES.CREATED_BY=USERS.ID ORDER BY  2,1"; 
		try {
			pst6 = conn.prepareStatement(query);
			rs6 = pst6.executeQuery();
			double total = 0.0;
			String fullName;
			while(rs6.next()) {
				int mID = rs6.getInt("modified_by");
				String modifiedUser = getModifiedName(mID);
				fullName = rs6.getString("firstname") +" " + rs6.getString("lastname");
				total = rs6.getDouble("price") * rs6.getInt("sale_quantity");
				model.addRow(new Object[] {
						rs6.getInt("id"),
						rs6.getString("date_created"),
						rs6.getString("name"),
						rs6.getInt("sale_quantity"),
						rs6.getDouble("price"),
						total,
						fullName,
						modifiedUser,
						rs6.getString("date_modified")

				});
			}
			rs6.close();
			pst6.close();
			sales_table.setModel(model);
			sales_table.setAutoResizeMode(0);
			sales_table.getColumnModel().getColumn(0).setPreferredWidth(20);
			sales_table.getColumnModel().getColumn(1).setPreferredWidth(100);
			sales_table.getColumnModel().getColumn(2).setPreferredWidth(100);
			sales_table.getColumnModel().getColumn(3).setPreferredWidth(50);
			sales_table.getColumnModel().getColumn(4).setPreferredWidth(70);
			sales_table.getColumnModel().getColumn(5).setPreferredWidth(70);
			sales_table.getColumnModel().getColumn(6).setPreferredWidth(100);
			sales_table.getColumnModel().getColumn(7).setPreferredWidth(100);
			sales_table.getColumnModel().getColumn(8).setPreferredWidth(210);
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return salesModel;
    }

	public HashMap<String, Integer> populateCustomers(JComboBox<String> salesCustomersComboBox) {

	conn = ConnectionClass.dbconnect();
		try {
			String querys = "SELECT * FROM customers";
			pst5 = conn.prepareStatement(querys);
			rs5 = pst5.executeQuery();
			salesCustomersComboBox.addItem("Select Customer");
			while (rs5.next()) {
				salesCustomersComboBox.addItem(rs5.getString("firstname") + " " + rs5.getString("lastname"));
				String firstname = rs5.getString("firstname");
				String lastname = rs5.getString("lastname");
				int id = rs5.getInt("id");

				customerMap.put(firstname + " " + lastname, id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customerMap;
	}

	public int getKeyForSelectedCustomer(String customer, HashMap<String, Integer> customerMap) {
		return customerMap.get(customer);
	}

	public HashMap<String, Integer> populateSaleProduct(JComboBox<String> salesProductsComboBox) {
		conn = ConnectionClass.dbconnect();
		try {
			String query = "SELECT * FROM products";
			pst3 = conn.prepareStatement(query);
			rs3 = pst3.executeQuery();
			salesProductsComboBox.addItem("Select Product");
			while (rs3.next()) {
				salesProductsComboBox.addItem(rs3.getString("name"));
				String name = rs3.getString("name");
				int id = rs3.getInt("id");
				customerProductMap.put(name, id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customerProductMap;
	}

	public int getKeyForSelectedProduct(String product, HashMap<String, Integer> customerProductMap) {
		return customerProductMap.get(product);
	}

}
