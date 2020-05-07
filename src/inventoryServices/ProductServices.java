package inventoryServices;

import java.sql.Connection;
//import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
//import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import inventoryDatabase.ConnectionClass;
//import inventoryModels.CategoryModel;
import inventoryModels.ProductsModel;
//import inventoryViews.UsersDashboard;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

public class ProductServices {
	Connection conn = null;
	OraclePreparedStatement pst, pst1, pst5 = null;
	OracleResultSet rs = null;
	ResultSet rs3, rs6 = null;
	ResultSet rs4, rs5 = null;

	HashMap<String, Integer> categoryMap = new HashMap<String, Integer>();

	ProductsModel productsModel = new ProductsModel();

	public ProductsModel addProductData(String name, int category, double price, double cost, int user_id) {
		conn = ConnectionClass.dbconnect();
		int quantity = 0;
		try {
			String query = "INSERT INTO PRODUCTS (name, price, quantity, category_id, cost, created_by) VALUES (?, ?, ?, ?, ?, ?)";
			pst = (OraclePreparedStatement) conn.prepareStatement(query);
			pst.setString(1, name);
			pst.setDouble(2, price);
			pst.setInt(3, quantity);
			pst.setInt(4, category);
			pst.setDouble(5, cost);
			pst.setDouble(6, user_id);
			int ret = pst.executeUpdate();
			if (ret > 0) {
				productsModel.setShortCode(0);
				productsModel.setShortMessage("Product Added Successfully");
			}
		} catch (Exception e) {
			productsModel.setShortCode(-1000);
			productsModel.setShortMessage("Connection Error");
		}
		return productsModel;
	}

	public ProductsModel updateProductData(int id, String name, int category, double price, double cost, int user_id) {
		conn = ConnectionClass.dbconnect();
		String date_modified;
		Date date = new Date();
		date_modified = date.toString();
		try {

			String query = "UPDATE products SET name=?, price=?, category_id=?, cost=?, modified_by=?, date_modified=? WHERE id=?";
			pst = (OraclePreparedStatement) conn.prepareStatement(query);
			pst.setString(1, name);
			pst.setDouble(2, price);
			pst.setInt(3, category);
			pst.setDouble(4, cost);
			pst.setDouble(5, user_id);
			pst.setString(6, date_modified);
			pst.setInt(7, id);
			int ret = pst.executeUpdate();
			if (ret > 0) {
				productsModel.setShortCode(0);
				productsModel.setShortMessage("Product Updated Successfully");
			}
		} catch (Exception e) {
			productsModel.setShortCode(-1000);
			productsModel.setShortMessage("Connection Error");
		}

		return productsModel;

	}

	// check if a value is double
	public int checkIfIntDouble(String q) {
		int res = 0;
		try {
			double x = Double.parseDouble(q);
			res = 0;
		} catch (Exception e) {
			res = 1;
		}
		return res;
	}

	public void totalProducts(JLabel totalProducts) {
		conn = ConnectionClass.dbconnect();
		int i = 0;
		try {
			String query = "SELECT * FROM PRODUCTS";
			pst = (OraclePreparedStatement) conn.prepareStatement(query);
			rs3 = pst.executeQuery();
			while (rs3.next()) {
				i++;
			}
			totalProducts.setText("" + i);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ProductsModel deleteProductData(int id) {
		conn = ConnectionClass.dbconnect();
		try {
			String query = "DELETE FROM products WHERE id=?";
			pst = (OraclePreparedStatement) conn.prepareStatement(query);
			pst.setInt(1, id);
			int ret = pst.executeUpdate();
			if (ret > 0) {
				productsModel.setShortCode(0);
				productsModel.setShortMessage("Product deleted Successfully");
			}
		} catch (Exception e) {
			productsModel.setShortCode(-1000);
			productsModel.setShortMessage("Connection Error");
		}

		return productsModel;

	}

	public HashMap<String, Integer> populateCategory(JComboBox categoryComboBox) {
		conn = ConnectionClass.dbconnect();
		try {
			String query = "SELECT * FROM category";
			pst = (OraclePreparedStatement) conn.prepareStatement(query);
			rs = (OracleResultSet) pst.executeQuery();
			categoryComboBox.addItem("=Select Category=");
			while (rs.next()) {
				categoryComboBox.addItem(rs.getString("cat_name"));
				String name = rs.getString("cat_name");
				int id = rs.getInt("id");
				categoryMap.put(name, id);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return categoryMap;
	}

	public int getKeyForSelectedCategory(String category, HashMap<String, Integer> catMap) {
		return catMap.get(category);
	}

	ProductsModel productsModels = new ProductsModel();

	private String getModifiedName(int mID) {
		String modifiedUser = "";
		try {
			conn = ConnectionClass.dbconnect();
			String query = "SELECT * FROM users WHERE id=?";
			pst = (OraclePreparedStatement) conn.prepareStatement(query);
			pst.setInt(1, mID);
			ResultSet rs6 = (OracleResultSet) pst.executeQuery();
			while (rs6.next()) {
				modifiedUser = rs6.getString("firstname") + " " + rs6.getString("lastname");
			}
			rs6.close();
			pst.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return modifiedUser;
	}

	public void showProductsData(JTable products_table) {
		try {
			conn = ConnectionClass.dbconnect();
			DefaultTableModel model = new DefaultTableModel();
			model.addColumn("ID");
			model.addColumn("Date Created");
			model.addColumn("Product Name");
			model.addColumn("Price");

			model.addColumn("Category");
			model.addColumn("Cost");
			model.addColumn("Created By");
			model.addColumn("Modified By");
			model.addColumn("Date Modified");

			// String query = "SELECT * FROM PRODUCTS";
			String query = "SELECT PRODUCTS.*, CATEGORY.CAT_NAME,USERS.FIRSTNAME, USERS.LASTNAME FROM PRODUCTS INNER JOIN CATEGORY ON PRODUCTS.CATEGORY_ID = CATEGORY.ID INNER JOIN USERS ON PRODUCTS.CREATED_BY = USERS.ID ORDER BY  2,1";

			pst = (OraclePreparedStatement) conn.prepareStatement(query);
			rs = (OracleResultSet) pst.executeQuery();

			while (rs.next()) {

				int mID = rs.getInt("modified_by");
				String modifiedUser = getModifiedName(mID);
				String fullName;
				fullName = rs.getString("firstname") + " " + rs.getString("lastname");
				model.addRow(new Object[] { rs.getInt("id"), rs.getString("date_created"), rs.getString("name"),
						rs.getDouble("price"),

						rs.getString("cat_name"), rs.getDouble("cost"), fullName, modifiedUser,
						rs.getString("date_modified") });
			}
			rs.close();
			pst.close();
			products_table.setModel(model);
			products_table.setAutoResizeMode(0);
			products_table.getColumnModel().getColumn(0).setPreferredWidth(20);
			products_table.getColumnModel().getColumn(1).setPreferredWidth(100);
			products_table.getColumnModel().getColumn(2).setPreferredWidth(100);
			products_table.getColumnModel().getColumn(3).setPreferredWidth(70);
			products_table.getColumnModel().getColumn(4).setPreferredWidth(100);
			products_table.getColumnModel().getColumn(5).setPreferredWidth(70);
			products_table.getColumnModel().getColumn(6).setPreferredWidth(100);
			products_table.getColumnModel().getColumn(7).setPreferredWidth(100);
			products_table.getColumnModel().getColumn(8).setPreferredWidth(200);

		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, e);
		}
	}

	public void showStockData(JTable stock_table) {
		try {
			conn = ConnectionClass.dbconnect();
			DefaultTableModel model = new DefaultTableModel();
			model.addColumn("ID");
			model.addColumn("Product Name");
			model.addColumn("Quantity");
			String query = "SELECT id, name, quantity FROM products";
			pst = (OraclePreparedStatement) conn.prepareStatement(query);
			rs = (OracleResultSet) pst.executeQuery();
			while (rs.next()) {
				model.addRow(new Object[] { rs.getInt("id"), rs.getString("name"), rs.getInt("quantity") });
			}
			stock_table.setModel(model);
			stock_table.setAutoResizeMode(0);
			stock_table.getColumnModel().getColumn(0).setPreferredWidth(100);
			stock_table.getColumnModel().getColumn(1).setPreferredWidth(200);
			stock_table.getColumnModel().getColumn(2).setPreferredWidth(200);
			rs.close();
			pst.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}

	public ProductsModel showChart(String name, int quantity) {
		return productsModel;

	}

}
