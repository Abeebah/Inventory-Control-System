package inventoryViews;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.util.HashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import inventoryDatabase.ConnectionClass;
import inventoryModels.CategoryModel;
import inventoryModels.CustomersModel;
import inventoryModels.ProductsModel;
import inventoryModels.PurchaseModel;
import inventoryModels.SalesModel;
import inventoryModels.UsersLoginModel;
import inventoryServices.CategoryServices;
import inventoryServices.CustomerServices;
import inventoryServices.ProductServices;
import inventoryServices.PurchaseServices;
import inventoryServices.SalesServices;
import inventoryServices.UsersLoginServices;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
//import org.jfree.chart.ChartUtilities;
//import org.jfree.chart.ChartFactory; 
//import org.jfree.chart.JFreeChart; 
//import org.jfree.data.general.DefaultPieDataset;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class UsersDashboard extends JFrame {
	Connection conn = null;
	private JPanel contentPane;
	private JTable table;
	private JTable products_table;
	JLabel welcome;
	JLabel lblCompanyname;
	private JTable category_table;
	private JTextField catNameField;
	JComboBox categoryComboBox;
	JComboBox productsComboBox;
	JComboBox salesProductsComboBox;
	JComboBox<String> salesCustomersComboBox;
	CategoryServices categoryServices = new CategoryServices();
	CategoryModel categoryModel = new CategoryModel();
	ProductServices productServices = new ProductServices();
	CustomerServices customerServices = new CustomerServices();
	SalesServices salesServices = new SalesServices();
	ProductsModel productsModel = new ProductsModel();
	PurchaseModel purchaseModel = new PurchaseModel();
	CustomersModel customersModel = new CustomersModel();
	SalesModel salesModel = new SalesModel();
	UsersLoginModel usersLoginModel = new UsersLoginModel();
	UsersLoginServices usersLoginServices = new UsersLoginServices();
	private JTextField productNameField;
	private JTextField priceTextField;
	private JTextField costTextField;
	JTextPane catDescriptionField;
	JLabel totalProducts;
	int productTotal;
	OraclePreparedStatement pst = null;
	OracleResultSet rs = null;
	HashMap<String, Integer> catMap = new HashMap<String, Integer>();
	HashMap<String, Integer> customerMap = new HashMap<String, Integer>();
	HashMap<String, Integer> productMap = new HashMap<String, Integer>();
	HashMap<String, Integer> customerProductMap = new HashMap<String, Integer>();

	private JTable purchase_table;
	private JTextField purchaseQuantityField;
	private JTextField customerFirstNameField;
	private JTextField customerLastNameField;
	private JTextField customerEmailField;
	private JTextField customerPhoneField;
	private JTable customers_table;
	private JTable sales_table;
	private JTextField salesQuantityTextField;
	private JTable stock_table;
	private JTextField userFirstNameTextField;
	private JTextField userLastNameEmailTextField;
	private JTextField userEmailTextField;
	private JTextField companyTextField;
	private JTable users_table;
	private JTextField textFieldLogin;
	private JTextField phoneTextField;
	private JPasswordField passwordFieldLogin;
	JButton productDeleteButton;
	JButton catDeleteButton;
	JButton salesDeleteButton;
	JButton btnDeleteCustomer;
	JPanel purchasestab;
	JComboBox roleComboBox;
	JTabbedPane tabbedPane;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public UsersDashboard(String firstName, String lastName, String company, String role, int id) {

		addWindowListener(new WindowAdapter() {

			@SuppressWarnings("unchecked")
			@Override
			public void windowOpened(WindowEvent arg0) {
				catMap = new ProductServices().populateCategory(categoryComboBox);
				customerMap = new SalesServices().populateCustomers(salesCustomersComboBox);
				productMap = new PurchaseServices().populateProduct(productsComboBox);
				new ProductServices().totalProducts(totalProducts);
				customerProductMap = new SalesServices().populateSaleProduct(salesProductsComboBox);
			//	salesServices.showchartt();
				String name = firstName + " " + lastName;
				categoryServices.showCategoryData(category_table, name);
			}
		});
		conn = ConnectionClass.dbconnect();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1144, 904);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		lblCompanyname = new JLabel("Company");
		lblCompanyname.setFont(new Font("Utsaah", Font.BOLD, 45));
		lblCompanyname.setBounds(21, 11, 341, 42);
		contentPane.add(lblCompanyname);
		lblCompanyname.setText("" + company);

		welcome = new JLabel("New label");
		welcome.setFont(new Font("Utsaah", Font.BOLD, 22));
		welcome.setBounds(930, 0, 246, 44);
		contentPane.add(welcome);
		welcome.setText("Welcome " + firstName + " " + lastName);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setForeground(new Color(0, 0, 51));
		tabbedPane.setBackground(Color.WHITE);
		tabbedPane.setFont(new Font("Utsaah", Font.PLAIN, 25));
		tabbedPane.setBounds(10, 60, 1108, 746);
		contentPane.add(tabbedPane);

		JPanel dashboardtab = new JPanel();
		dashboardtab.setForeground(new Color(0, 0, 153));
		dashboardtab.setBackground(Color.WHITE);
		tabbedPane.addTab("Dashboard", null, dashboardtab, null);
		tabbedPane.setForegroundAt(0, new Color(204, 204, 204));
		tabbedPane.setBackgroundAt(0, new Color(0, 0, 51));
		dashboardtab.setLayout(null);

		JButton button = new JButton("Total Sales");
		button.setBounds(71, 32, 297, 59);
		button.setFont(new Font("Utsaah", Font.PLAIN, 45));
		dashboardtab.add(button);

		JButton button_1 = new JButton("Total Purchases");
		button_1.setBounds(426, 32, 308, 59);
		button_1.setFont(new Font("Utsaah", Font.PLAIN, 45));
		dashboardtab.add(button_1);

		JButton button_2 = new JButton("Total Products");
		button_2.setBounds(776, 32, 275, 59);
		button_2.setFont(new Font("Utsaah", Font.PLAIN, 45));
		dashboardtab.add(button_2);

		JSeparator separator = new JSeparator();
		separator.setBounds(985, 709, -912, -78);
		dashboardtab.add(separator);

		JButton newSaleButton = new JButton("New Sale");
		newSaleButton.setBounds(125, 638, 201, 40);
		newSaleButton.setFont(new Font("Utsaah", Font.PLAIN, 22));
		newSaleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				salesServices.showSaleData(sales_table);
			}
		});
		dashboardtab.add(newSaleButton);
		JButton newPurchaseButton = new JButton("New Purchase");
		newPurchaseButton.setBounds(478, 638, 193, 40);
		newPurchaseButton.setFont(new Font("Utsaah", Font.PLAIN, 22));
		dashboardtab.add(newPurchaseButton);

		JButton newProductButton = new JButton("New Product");
		newProductButton.setBounds(855, 638, 172, 40);
		newProductButton.setFont(new Font("Utsaah", Font.PLAIN, 22));
		dashboardtab.add(newProductButton);

		table = new JTable();
		table.setBounds(10, 614, 1083, 84);
		table.setBackground(new Color(0, 0, 51));
		dashboardtab.add(table);

		JLabel totalSales = new JLabel("Total Sales");
		totalSales.setBounds(91, 153, 259, 51);
		totalSales.setFont(new Font("Utsaah", Font.PLAIN, 45));
		dashboardtab.add(totalSales);

		JLabel totalPurchase = new JLabel("Total Purchase");
		totalPurchase.setBounds(454, 163, 275, 41);
		totalPurchase.setFont(new Font("Utsaah", Font.PLAIN, 45));
		dashboardtab.add(totalPurchase);

		totalProducts = new JLabel("");
		totalProducts.setBounds(776, 163, 259, 41);
		totalProducts.setFont(new Font("Utsaah", Font.PLAIN, 45));
		dashboardtab.add(totalProducts);

		JButton btnPieChart = new JButton("Pie Chart");
		btnPieChart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
		//		salesServices.showchartt();
			}
		});
		btnPieChart.setBounds(439, 358, 269, 59);
		dashboardtab.add(btnPieChart);
		JPanel productstab = new JPanel();
		productstab.setForeground(new Color(204, 204, 204));
		tabbedPane.addTab("Products", null, productstab, null);
		productstab.setLayout(null);

		JScrollPane productsScrollPane = new JScrollPane();
		productsScrollPane.setBounds(300, 11, 777, 647);
		productstab.add(productsScrollPane);

		products_table = new JTable();
		products_table.setFont(new Font("Utsaah", Font.PLAIN, 18));
		products_table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DefaultTableModel model = (DefaultTableModel) products_table.getModel();
				int selectedRowIndex = products_table.getSelectedRow();
				productsModel.setId(Integer.parseInt(model.getValueAt(selectedRowIndex, 0).toString()));
				productNameField.setText(model.getValueAt(selectedRowIndex, 2).toString());
				categoryComboBox.setSelectedItem(model.getValueAt(selectedRowIndex, 5).toString());
				priceTextField.setText(model.getValueAt(selectedRowIndex, 3).toString());
				costTextField.setText(model.getValueAt(selectedRowIndex, 5).toString());

			}
		});

		productsScrollPane.setViewportView(products_table);
		productServices.showProductsData(products_table);

		JButton productaddButton = new JButton("Add Product");
		productaddButton.setForeground(Color.WHITE);
		productaddButton.setBackground(new Color(0, 0, 51));
		productaddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (productNameField.getText().equals("") || priceTextField.getText().equals("")
							|| costTextField.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "All fields are required", "Login Error",
								JOptionPane.OK_OPTION);
					} else {
						int pricevalidity = new ProductServices().checkIfIntDouble(priceTextField.getText());
						int costvalidity = new ProductServices().checkIfIntDouble(costTextField.getText());
						if (pricevalidity == 1) {
							JOptionPane.showMessageDialog(null, "Enter a valid price", "Login Error",
									JOptionPane.OK_OPTION);
						} else if (costvalidity == 1) {
							JOptionPane.showMessageDialog(null, "Enter a valid cost", "Login Error",
									JOptionPane.OK_OPTION);
						} else {

							int catID = new ProductServices()
									.getKeyForSelectedCategory(categoryComboBox.getSelectedItem().toString(), catMap);

							ProductsModel productsModel1 = new ProductServices().addProductData(
									productNameField.getText(), catID, Double.parseDouble(priceTextField.getText()),
									Double.parseDouble(costTextField.getText()), id);
							if (productsModel1.getShortCode() == 0) {
								JOptionPane.showMessageDialog(null, productsModel1.getShortMessage());
								productServices.showProductsData(products_table);
								productNameField.setText("");
								categoryComboBox.setSelectedItem("=Select Category");
								priceTextField.setText("");
								costTextField.setText("");
							} else {
								JOptionPane.showMessageDialog(null, productsModel1.getShortMessage());
							}

						}

					}

				} catch (Exception e) {

				}

			}
		});
		productaddButton.setFont(new Font("Utsaah", Font.PLAIN, 28));
		productaddButton.setBounds(10, 334, 258, 41);
		productstab.add(productaddButton);

		JLabel productlblName = new JLabel("Name");
		productlblName.setFont(new Font("Utsaah", Font.PLAIN, 23));
		productlblName.setBounds(10, 68, 105, 41);
		productstab.add(productlblName);

		productNameField = new JTextField();
		productNameField.setFont(new Font("Utsaah", Font.PLAIN, 22));
		productNameField.setBounds(79, 72, 211, 32);
		productstab.add(productNameField);
		productNameField.setColumns(10);

		JLabel productlblCategory = new JLabel("Category");
		productlblCategory.setFont(new Font("Utsaah", Font.PLAIN, 23));
		productlblCategory.setBounds(10, 116, 105, 41);
		productstab.add(productlblCategory);

		JLabel productlblPrice = new JLabel("Price");
		productlblPrice.setFont(new Font("Utsaah", Font.PLAIN, 23));
		productlblPrice.setBounds(10, 155, 105, 41);
		productstab.add(productlblPrice);

		JLabel productlblCost = new JLabel("Cost");
		productlblCost.setFont(new Font("Utsaah", Font.PLAIN, 23));
		productlblCost.setBounds(10, 207, 105, 41);
		productstab.add(productlblCost);

		categoryComboBox = new JComboBox();
		categoryComboBox.setFont(new Font("Utsaah", Font.PLAIN, 22));
		categoryComboBox.setBounds(79, 120, 211, 32);
		productstab.add(categoryComboBox);

		priceTextField = new JTextField();
		priceTextField.setFont(new Font("Utsaah", Font.PLAIN, 22));
		priceTextField.setColumns(10);
		priceTextField.setBounds(79, 159, 211, 32);
		productstab.add(priceTextField);

		costTextField = new JTextField();
		costTextField.setFont(new Font("Utsaah", Font.PLAIN, 22));
		costTextField.setColumns(10);
		costTextField.setBounds(79, 207, 211, 32);
		productstab.add(costTextField);

		JButton productUpdateButton = new JButton("Edit Product");
		productUpdateButton.setForeground(Color.WHITE);
		productUpdateButton.setBackground(new Color(0, 0, 51));
		productUpdateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (productNameField.getText().equals("") || priceTextField.getText().equals("")
						|| costTextField.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "All fields are required", "Login Error",
							JOptionPane.OK_OPTION);

				} else {
					int pricevalidity = new ProductServices().checkIfIntDouble(priceTextField.getText());
					int costvalidity = new ProductServices().checkIfIntDouble(costTextField.getText());
					if (pricevalidity == 1) {
						JOptionPane.showMessageDialog(null, "Enter a valid price", "Login Error",
								JOptionPane.OK_OPTION);
					} else if (costvalidity == 1) {
						JOptionPane.showMessageDialog(null, "Enter a valid cost", "Login Error", JOptionPane.OK_OPTION);
					} else {
						int pid = productsModel.getId();
						int catID = new ProductServices()
								.getKeyForSelectedCategory(categoryComboBox.getSelectedItem().toString(), catMap);
						ProductsModel productsModel2 = new ProductServices().updateProductData(pid,
								productNameField.getText(), catID, Double.parseDouble(priceTextField.getText()),
								Double.parseDouble(costTextField.getText()), id);
						if (productsModel2.getShortCode() == 0) {
							JOptionPane.showMessageDialog(null, productsModel2.getShortMessage());
							productServices.showProductsData(products_table);
							productNameField.setText("");
							priceTextField.setText("");
							categoryComboBox.setSelectedIndex(-1);
							costTextField.setText("");
						} else {
							JOptionPane.showMessageDialog(null, "Problem updating product");
						}
					}
				}
			}
		});
		productUpdateButton.setFont(new Font("Utsaah", Font.PLAIN, 28));
		productUpdateButton.setBounds(10, 390, 258, 41);
		productstab.add(productUpdateButton);

		JButton productDeleteButton = new JButton("Delete Product");
		productDeleteButton.setForeground(Color.WHITE);
		productDeleteButton.setBackground(new Color(0, 0, 51));
		productDeleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int prid = productsModel.getId();
				ProductsModel productModel = new ProductServices().deleteProductData(prid);
				if (productModel.getShortCode() == 0) {
					JOptionPane.showMessageDialog(null, productModel.getShortMessage());
					new ProductServices().showProductsData(products_table);
					productNameField.setText("");
					priceTextField.setText("");
					costTextField.setText("");
				} else {
					JOptionPane.showMessageDialog(null, "Problem deleting product");
				}
			}
		});
		productDeleteButton.setFont(new Font("Utsaah", Font.PLAIN, 28));
		productDeleteButton.setBounds(10, 442, 258, 41);
		productstab.add(productDeleteButton);
		tabbedPane.setBackgroundAt(1, new Color(0, 0, 51));
		tabbedPane.setForegroundAt(1, new Color(204, 204, 204));

		JPanel categorytabs = new JPanel();
		tabbedPane.addTab("Category", null, categorytabs, null);
		categorytabs.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(317, 11, 776, 599);
		categorytabs.add(scrollPane);

		category_table = new JTable();
		category_table.setFont(new Font("Utsaah", Font.PLAIN, 18));
		category_table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DefaultTableModel model = (DefaultTableModel) category_table.getModel();
				int selectedRowIndex = category_table.getSelectedRow();
				categoryModel.setId(Integer.parseInt(model.getValueAt(selectedRowIndex, 0).toString()));
				catNameField.setText(model.getValueAt(selectedRowIndex, 2).toString());
				catDescriptionField.setText(model.getValueAt(selectedRowIndex, 3).toString());
			}
		});
		scrollPane.setViewportView(category_table);
		String name = firstName + " " + lastName;
		categoryServices.showCategoryData(category_table, name);

		JLabel lblCatName = new JLabel("Name");
		lblCatName.setFont(new Font("Utsaah", Font.PLAIN, 25));
		lblCatName.setBounds(10, 87, 152, 36);
		categorytabs.add(lblCatName);

		JLabel lblCatDescription = new JLabel("Description");
		lblCatDescription.setFont(new Font("Utsaah", Font.PLAIN, 25));
		lblCatDescription.setBounds(10, 156, 120, 43);
		categorytabs.add(lblCatDescription);

		catNameField = new JTextField();
		catNameField.setColumns(10);
		catNameField.setBounds(112, 93, 195, 27);
		categorytabs.add(catNameField);

		JButton catInsertButton = new JButton("Add Category");
		catInsertButton.setForeground(Color.WHITE);
		catInsertButton.setBackground(new Color(0, 0, 51));
		catInsertButton.setFont(new Font("Utsaah", Font.PLAIN, 28));
		catInsertButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (catNameField.getText().equals("") || catDescriptionField.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Enter category name and description", "Error",
							JOptionPane.OK_OPTION);
				} else {
					int ids = categoryServices.generateID();
					CategoryModel categoryModel = new CategoryServices().addCategoryData(ids, catNameField.getText(),
							catDescriptionField.getText(), id);
					if (categoryModel.getShortCode() == 0) {
						JOptionPane.showMessageDialog(null, categoryModel.getShortMessage());
						new CategoryServices().showCategoryData(category_table, name);
						catNameField.setText("");
						catDescriptionField.setText("");
					} else {
						JOptionPane.showMessageDialog(null, "Problem adding category");
					}
				}
			}
		});
		catInsertButton.setBounds(10, 302, 248, 36);
		categorytabs.add(catInsertButton);

		JButton catUpdateButton = new JButton("Edit Category");
		catUpdateButton.setForeground(Color.WHITE);
		catUpdateButton.setBackground(new Color(0, 0, 51));
		catUpdateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int cid = categoryModel.getId();
				CategoryModel categoryModel2 = new CategoryServices().updateCategoryData(cid, catNameField.getText(),
						catDescriptionField.getText(), id);
				if (categoryModel2.getShortCode() == 0) {
					JOptionPane.showMessageDialog(null, categoryModel2.getShortMessage());
					new CategoryServices().showCategoryData(category_table, name);
					catNameField.setText("");
					catDescriptionField.setText("");
				} else {
					JOptionPane.showMessageDialog(null, "Problem updating category");
				}

			}
		});
		catUpdateButton.setFont(new Font("Utsaah", Font.PLAIN, 28));
		catUpdateButton.setBounds(10, 349, 248, 36);
		categorytabs.add(catUpdateButton);

		JButton catDeleteButton = new JButton("Delete Category");
		catDeleteButton.setBackground(new Color(0, 0, 51));
		catDeleteButton.setForeground(Color.WHITE);
		catDeleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int cid = categoryModel.getId();
				CategoryModel categoryModel = new CategoryServices().deleteCategoryData(cid);
				if (categoryModel.getShortCode() == 0) {
					JOptionPane.showMessageDialog(null, categoryModel.getShortMessage());
					new CategoryServices().showCategoryData(category_table, name);
					catNameField.setText("");
					catDescriptionField.setText("");
				} else {
					JOptionPane.showMessageDialog(null, "Problem deleting category");
				}

			}
		});
		catDeleteButton.setFont(new Font("Utsaah", Font.PLAIN, 28));
		catDeleteButton.setBounds(10, 396, 248, 36);
		categorytabs.add(catDeleteButton);
		tabbedPane.setForegroundAt(2, new Color(204, 204, 204));
		tabbedPane.setBackgroundAt(2, new Color(0, 0, 51));

		purchasestab = new JPanel();
		purchasestab.setForeground(new Color(0, 0, 0));
		purchasestab.setBackground(new Color(255, 255, 255));
		tabbedPane.addTab("Purchases", null, purchasestab, null);
		tabbedPane.setBackgroundAt(3, new Color(0, 0, 51));
		tabbedPane.setForegroundAt(3, new Color(204, 204, 204));
		purchasestab.setLayout(null);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(297, 11, 796, 611);
		purchasestab.add(scrollPane_1);

		purchase_table = new JTable();
		purchase_table.setFont(new Font("Utsaah", Font.PLAIN, 18));
		purchase_table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DefaultTableModel model = (DefaultTableModel) purchase_table.getModel();
				int selectedRowIndex = purchase_table.getSelectedRow();
				purchaseModel.setId(Integer.parseInt(model.getValueAt(selectedRowIndex, 0).toString()));
				productsComboBox.setSelectedItem(model.getValueAt(selectedRowIndex, 2).toString());
				purchaseQuantityField.setText(model.getValueAt(selectedRowIndex, 3).toString());
			}
		});

		scrollPane.setViewportView(category_table);

		catDescriptionField = new JTextPane();
		catDescriptionField.setBounds(112, 146, 195, 63);
		categorytabs.add(catDescriptionField);
		PurchaseServices purchaseServices = new PurchaseServices();
		purchaseServices.showPurchaseData(purchase_table);
		scrollPane_1.setViewportView(purchase_table);

		JLabel purchaseProductLabel = new JLabel("Product");
		purchaseProductLabel.setBounds(10, 94, 133, 46);
		purchaseProductLabel.setFont(new Font("Utsaah", Font.PLAIN, 25));
		purchasestab.add(purchaseProductLabel);

		productsComboBox = new JComboBox();
		productsComboBox.setFont(new Font("Utsaah", Font.PLAIN, 20));
		productsComboBox.setBounds(89, 100, 189, 37);
		purchasestab.add(productsComboBox);

		JLabel PurchaseQuantitylbl = new JLabel("Quantity");
		PurchaseQuantitylbl.setFont(new Font("Utsaah", Font.PLAIN, 25));
		PurchaseQuantitylbl.setBounds(10, 172, 133, 46);
		purchasestab.add(PurchaseQuantitylbl);

		purchaseQuantityField = new JTextField();
		purchaseQuantityField.setBounds(89, 172, 189, 37);
		purchasestab.add(purchaseQuantityField);
		purchaseQuantityField.setColumns(10);

		JButton purchaseAddBtn = new JButton("Add Purchase");
		purchaseAddBtn.setForeground(Color.WHITE);
		purchaseAddBtn.setBackground(new Color(0, 0, 51));
		purchaseAddBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (purchaseQuantityField.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "field is required", "Login Error", JOptionPane.OK_OPTION);

					} else {
						int quantityvalidity = new ProductServices().checkIfIntDouble(purchaseQuantityField.getText());

						if (quantityvalidity == 1) {
							JOptionPane.showMessageDialog(null, "Enter a valid quantity", "Login Error",
									JOptionPane.OK_OPTION);
						}

						else {

							int productID = new PurchaseServices().getKeyForSelectedProduct(
									productsComboBox.getSelectedItem().toString(), productMap);
							PurchaseModel purchaseModel1 = new PurchaseServices().addPurchaseData(productID,
									Integer.parseInt(purchaseQuantityField.getText()), id);
							if (purchaseModel1.getShortCode() == 0) {
								JOptionPane.showMessageDialog(null, purchaseModel1.getShortMessage());
								purchaseServices.showPurchaseData(purchase_table);
								productsComboBox.setSelectedItem("Select Product");
								purchaseQuantityField.setText("");
							} else {
								JOptionPane.showMessageDialog(null, "Problem updating purchase");
							}

						}

					}
				} catch (Exception eex) {

				}

			}

		});
		purchaseAddBtn.setFont(new Font("Utsaah", Font.PLAIN, 28));
		purchaseAddBtn.setBounds(10, 295, 248, 37);
		purchasestab.add(purchaseAddBtn);

		JButton purchaseUpdateBtn = new JButton("Edit Purchase");
		purchaseUpdateBtn.setForeground(Color.WHITE);
		purchaseUpdateBtn.setBackground(new Color(0, 0, 51));
		purchaseUpdateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int purID = purchaseModel.getId();
				int productID = new PurchaseServices()
						.getKeyForSelectedProduct(productsComboBox.getSelectedItem().toString(), productMap);
				PurchaseModel purchaseModel1 = purchaseServices.updatePurchaseData(purID, productID,
						Integer.parseInt(purchaseQuantityField.getText()), id);
				if (purchaseModel.getShortCode() == 0) {
					JOptionPane.showMessageDialog(null, purchaseModel1.getShortMessage());
					new PurchaseServices().showPurchaseData(purchase_table);
					productsComboBox.setSelectedItem("=Select Item=");
					purchaseQuantityField.setText("");
				} else {
					JOptionPane.showMessageDialog(null, purchaseModel1.getShortMessage());
				}
			}
		});
		purchaseUpdateBtn.setFont(new Font("Utsaah", Font.PLAIN, 28));
		purchaseUpdateBtn.setBounds(10, 343, 249, 37);
		purchasestab.add(purchaseUpdateBtn);

		JButton purchaseDeleteBtn = new JButton("Delete Purchase");
		purchaseDeleteBtn.setForeground(Color.WHITE);
		purchaseDeleteBtn.setBackground(new Color(0, 0, 51));
		purchaseDeleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int id = purchaseModel.getId();
				PurchaseModel purchaseModel = new PurchaseServices().deletePurchaseData(id);
				if (purchaseModel.getShortCode() == 0) {
					JOptionPane.showMessageDialog(null, purchaseModel.getShortMessage());
					new PurchaseServices().showPurchaseData(purchase_table);
					productsComboBox.setSelectedItem("=Select Item=");
					purchaseQuantityField.setText("");
				} else {
					JOptionPane.showMessageDialog(null, purchaseModel.getShortMessage());
				}
			}
		});
		purchaseDeleteBtn.setFont(new Font("Utsaah", Font.PLAIN, 28));
		purchaseDeleteBtn.setBounds(10, 391, 248, 37);
		purchasestab.add(purchaseDeleteBtn);

		JPanel sales = new JPanel();
		tabbedPane.addTab("Sales", null, sales, null);
		sales.setLayout(null);

		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DefaultTableModel model = (DefaultTableModel) sales_table.getModel();
				int selectedRowIndex = sales_table.getSelectedRow();
				salesModel.setId(Integer.parseInt(model.getValueAt(selectedRowIndex, 0).toString()));
				salesProductsComboBox.setSelectedItem(model.getValueAt(selectedRowIndex, 2).toString());
				salesQuantityTextField.setText(model.getValueAt(selectedRowIndex, 3).toString());
			}
		});
		scrollPane_3.setBounds(341, 11, 734, 681);
		sales.add(scrollPane_3);

		sales_table = new JTable();
		scrollPane_3.setViewportView(sales_table);
		salesServices.showSaleData(sales_table);
		sales_table.setFont(new Font("Utsaah", Font.PLAIN, 18));

		JLabel salesProductLabel = new JLabel("Product Name");
		salesProductLabel.setFont(new Font("Utsaah", Font.PLAIN, 24));
		salesProductLabel.setBounds(0, 89, 149, 38);
		sales.add(salesProductLabel);

		salesProductsComboBox = new JComboBox();
		salesProductsComboBox.setFont(new Font("Utsaah", Font.PLAIN, 18));
		salesProductsComboBox.setBounds(135, 92, 196, 33);
		sales.add(salesProductsComboBox);

		JLabel lblCustomerName = new JLabel("Customer Name");
		lblCustomerName.setFont(new Font("Utsaah", Font.PLAIN, 24));
		lblCustomerName.setBounds(0, 151, 134, 38);
		sales.add(lblCustomerName);

		salesCustomersComboBox = new JComboBox();
		salesCustomersComboBox.setFont(new Font("Utsaah", Font.PLAIN, 18));
		salesCustomersComboBox.setBounds(135, 154, 196, 33);
		sales.add(salesCustomersComboBox);
//		for (int i= 0; i < custList.size(); i++) {
//			salesCustomersComboBox.addItem((String) custList.get(i));
//		}

		JLabel lblSalesQuantity = new JLabel("Quantity");
		lblSalesQuantity.setFont(new Font("Utsaah", Font.PLAIN, 24));
		lblSalesQuantity.setBounds(10, 211, 149, 38);
		sales.add(lblSalesQuantity);

		salesQuantityTextField = new JTextField();
		salesQuantityTextField.setFont(new Font("Utsaah", Font.PLAIN, 20));
		salesQuantityTextField.setBounds(135, 214, 196, 33);
		sales.add(salesQuantityTextField);
		salesQuantityTextField.setColumns(10);

		JButton salesAddButton = new JButton("Add Sale");
		salesAddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int saleQuantityValidity = new SalesServices().checkIfInt(salesQuantityTextField.getText());
				if (salesQuantityTextField.getText().isEmpty() || salesCustomersComboBox.getSelectedItem().equals("")
						|| salesProductsComboBox.getSelectedItem().equals("")) {
					JOptionPane.showMessageDialog(null, "All field required");
				} else if (saleQuantityValidity == 1) {
					JOptionPane.showMessageDialog(null, "Enter a valid quantity");
				} else {
					int customerID = new SalesServices().getKeyForSelectedCustomer(
							salesCustomersComboBox.getSelectedItem().toString(), customerMap);
					int salesProID = new SalesServices().getKeyForSelectedProduct(
							salesProductsComboBox.getSelectedItem().toString(), customerProductMap);
					SalesModel salesModel = new SalesServices().addSaleData(salesProID, customerID,
							Integer.parseInt(salesQuantityTextField.getText()), id);
					if (salesModel.getShortCode() == 0) {
						JOptionPane.showMessageDialog(null, salesModel.getShortMessage());
						salesServices.showSaleData(sales_table);
						salesQuantityTextField.setText("");

					} else if (salesModel.getShortCode() == 20) {
						JOptionPane.showMessageDialog(null, "Product out of stock");
					} else {
						JOptionPane.showMessageDialog(null, "Problem adding sale");
					}
				}
			}
		});

		salesAddButton.setForeground(Color.WHITE);
		salesAddButton.setBackground(new Color(0, 0, 51));
		salesAddButton.setFont(new Font("Utsaah", Font.PLAIN, 26));
		salesAddButton.setBounds(10, 324, 251, 38);
		sales.add(salesAddButton);

		JButton salesUpdateButton = new JButton("Edit Sale");
		salesUpdateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int saleQuantityValidity = new SalesServices().checkIfInt(salesQuantityTextField.getText());
				if (salesQuantityTextField.getText().isEmpty() || salesCustomersComboBox.getSelectedItem().equals("")
						|| salesProductsComboBox.getSelectedItem().equals("")) {
					JOptionPane.showMessageDialog(null, "All field required");
				} else if (saleQuantityValidity == 1) {
					JOptionPane.showMessageDialog(null, "Enter a valid quantity");
				} else {
					int saleID = salesModel.getId();
					int salesProID = new SalesServices().getKeyForSelectedProduct(
							salesProductsComboBox.getSelectedItem().toString(), customerProductMap);
					SalesModel salesModel = new SalesServices().updatSaleData(saleID, salesProID,
							Integer.parseInt(salesQuantityTextField.getText()), id);
					if (salesModel.getShortCode() == 0) {
						JOptionPane.showMessageDialog(null, salesModel.getShortMessage());
						salesServices.showSaleData(sales_table);
						// salesQuantityTextField.setText("");

					} else {
						JOptionPane.showMessageDialog(null, "Problem updating sale");
					}
				}
			}

		});
		salesUpdateButton.setForeground(Color.WHITE);
		salesUpdateButton.setFont(new Font("Utsaah", Font.PLAIN, 26));
		salesUpdateButton.setBackground(new Color(0, 0, 51));
		salesUpdateButton.setBounds(10, 373, 251, 38);
		sales.add(salesUpdateButton);

		JButton salesDeleteButton = new JButton("Delete Sale");
		salesDeleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int saleID = salesModel.getId();
				SalesModel salesModel = new SalesServices().deleteSaleData(saleID);
				if (salesModel.getShortCode() == 0) {
					JOptionPane.showMessageDialog(null, salesModel.getShortMessage());
					salesServices.showSaleData(sales_table);
				} else {
					JOptionPane.showMessageDialog(null, "Problem deleting sale");
				}
			}
		});
		salesDeleteButton.setForeground(Color.WHITE);
		salesDeleteButton.setFont(new Font("Utsaah", Font.PLAIN, 26));
		salesDeleteButton.setBackground(new Color(0, 0, 51));
		salesDeleteButton.setBounds(10, 422, 253, 38);
		sales.add(salesDeleteButton);
		tabbedPane.setForegroundAt(4, new Color(204, 204, 204));
		tabbedPane.setBackgroundAt(4, new Color(0, 0, 51));

		JPanel customerstab = new JPanel();
		tabbedPane.addTab("Customers", null, customerstab, null);
		tabbedPane.setForegroundAt(5, new Color(204, 204, 204));
		tabbedPane.setBackgroundAt(5, new Color(0, 0, 51));
		customerstab.setLayout(null);

		JLabel customerFirstName = new JLabel("First Name");
		customerFirstName.setFont(new Font("Utsaah", Font.PLAIN, 24));
		customerFirstName.setBounds(10, 66, 111, 52);
		customerstab.add(customerFirstName);

		JLabel customerEmail = new JLabel("Email");
		customerEmail.setFont(new Font("Utsaah", Font.PLAIN, 24));
		customerEmail.setBounds(10, 158, 111, 52);
		customerstab.add(customerEmail);

		JLabel CustomerPhone = new JLabel("Phone");
		CustomerPhone.setFont(new Font("Utsaah", Font.PLAIN, 24));
		CustomerPhone.setBounds(10, 215, 111, 52);
		customerstab.add(CustomerPhone);

		JLabel customerAddress = new JLabel("Address");
		customerAddress.setFont(new Font("Utsaah", Font.PLAIN, 24));
		customerAddress.setBounds(10, 278, 111, 52);
		customerstab.add(customerAddress);

		customerFirstNameField = new JTextField();
		customerFirstNameField.setFont(new Font("Utsaah", Font.PLAIN, 22));
		customerFirstNameField.setBounds(106, 75, 204, 34);
		customerstab.add(customerFirstNameField);
		customerFirstNameField.setColumns(10);

		JLabel customerLastName = new JLabel("Last Name");
		customerLastName.setFont(new Font("Utsaah", Font.PLAIN, 24));
		customerLastName.setBounds(10, 121, 111, 52);
		customerstab.add(customerLastName);

		customerLastNameField = new JTextField();
		customerLastNameField.setFont(new Font("Utsaah", Font.PLAIN, 22));
		customerLastNameField.setColumns(10);
		customerLastNameField.setBounds(106, 129, 204, 34);
		customerstab.add(customerLastNameField);

		customerEmailField = new JTextField();
		customerEmailField.setFont(new Font("Utsaah", Font.PLAIN, 22));
		customerEmailField.setColumns(10);
		customerEmailField.setBounds(106, 176, 204, 34);
		customerstab.add(customerEmailField);

		customerPhoneField = new JTextField();
		customerPhoneField.setFont(new Font("Utsaah", Font.PLAIN, 22));
		customerPhoneField.setColumns(10);
		customerPhoneField.setBounds(106, 224, 204, 34);
		customerstab.add(customerPhoneField);

		JTextArea customerAddressField = new JTextArea();
		customerAddressField.setBounds(106, 278, 209, 52);
		customerstab.add(customerAddressField);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DefaultTableModel model = (DefaultTableModel) customers_table.getModel();
				int selectedRowIndex = customers_table.getSelectedRow();
				customersModel.setId(Integer.parseInt(model.getValueAt(selectedRowIndex, 0).toString()));
				customerFirstNameField.setText(model.getValueAt(selectedRowIndex, 2).toString());
				customerLastNameField.setText(model.getValueAt(selectedRowIndex, 3).toString());
				customerEmailField.setText(model.getValueAt(selectedRowIndex, 4).toString());
				customerPhoneField.setText(model.getValueAt(selectedRowIndex, 5).toString());
				customerAddressField.setText(model.getValueAt(selectedRowIndex, 6).toString());
			}

		});
		scrollPane_2.setBounds(366, 11, 727, 653);
		customerstab.add(scrollPane_2);

		customers_table = new JTable();
		scrollPane_2.setViewportView(customers_table);
		customers_table.setFont(new Font("Utsaah", Font.PLAIN, 18));
		customerServices.showCustomerData(customers_table);

		JButton customerAddBtn = new JButton("Add Customer");
		customerAddBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int phoneValidity = new CustomerServices().checkIfInt(customerPhoneField.getText());
				int emailValidity = new CustomerServices().validateEmail(customerEmailField.getText());
				if (customerFirstNameField.getText().isEmpty() || customerLastNameField.getText().isEmpty()
						|| customerEmailField.getText().isEmpty() || customerPhoneField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "All fields are required", "Login Error",
							JOptionPane.OK_OPTION);
				} else if (emailValidity == 1) {
					JOptionPane.showMessageDialog(null, "Enter a valid email");
				} else if (phoneValidity == 1) {
					JOptionPane.showMessageDialog(null, "Enter 11 digit phone number");
				} else if (customerPhoneField.getText().length() != 11) {
					JOptionPane.showMessageDialog(null, "Enter 11 digit number");
				} else {
					CustomersModel customersModel1 = new CustomerServices().addCustomerData(
							customerFirstNameField.getText(), customerLastNameField.getText(),
							customerEmailField.getText(), customerPhoneField.getText(), customerAddressField.getText(),
							id);
					if (customersModel.getShortCode() == 0) {
						JOptionPane.showMessageDialog(null, customersModel1.getShortMessage());
						customerServices.showCustomerData(customers_table);
						customerFirstNameField.setText("");
						customerLastNameField.setText("");
						customerEmailField.setText("");
						customerPhoneField.setText("");
						customerAddressField.setText("");
					} else {
						JOptionPane.showMessageDialog(null, "Problem updating customer");
					}
				}

			}
		});

		customerAddBtn.setBackground(new Color(0, 0, 51));
		customerAddBtn.setForeground(Color.WHITE);
		customerAddBtn.setFont(new Font("Utsaah", Font.PLAIN, 28));
		customerAddBtn.setBounds(10, 386, 252, 34);
		customerstab.add(customerAddBtn);

		JButton btnUpdateCustomer = new JButton("Update Customer");
		btnUpdateCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int cusId = customersModel.getId();
				int phoneValidity = new CustomerServices().checkIfInt(customerPhoneField.getText());
				int emailValidity = new CustomerServices().validateEmail(customerEmailField.getText());
				if (customerFirstNameField.getText().isEmpty() || customerLastNameField.getText().isEmpty()
						|| customerEmailField.getText().isEmpty() || customerPhoneField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "All fields are required", "Login Error",
							JOptionPane.OK_OPTION);
				} else if (emailValidity == 1) {
					JOptionPane.showMessageDialog(null, "Enter a valid email");
				} else if (phoneValidity == 1) {
					JOptionPane.showMessageDialog(null, "Enter a valid phone number");
				} else if (customerPhoneField.getText().length() != 11) {
					JOptionPane.showMessageDialog(null, "Enter 11 digit number");
				} else {
					CustomersModel customersModels = new CustomerServices().updateCustomeryData(cusId,
							customerFirstNameField.getText(), customerLastNameField.getText(),
							customerEmailField.getText(), customerPhoneField.getText(), customerAddressField.getText(),
							id);
					if (customersModel.getShortCode() == 0) {
						JOptionPane.showMessageDialog(null, customersModels.getShortMessage());
						new CustomerServices().showCustomerData(customers_table);
						customerFirstNameField.setText("");
						customerLastNameField.setText("");
						customerEmailField.setText("");
						customerPhoneField.setText("");
						customerAddressField.setText("");
					} else {
						JOptionPane.showMessageDialog(null, "Problem updating customers");
					}
				}
			}
		});
		btnUpdateCustomer.setForeground(Color.WHITE);
		btnUpdateCustomer.setBackground(new Color(0, 0, 51));
		btnUpdateCustomer.setFont(new Font("Utsaah", Font.PLAIN, 28));
		btnUpdateCustomer.setBounds(10, 431, 252, 34);
		customerstab.add(btnUpdateCustomer);

		btnDeleteCustomer = new JButton("Delete Customer");
		btnDeleteCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int id = customersModel.getId();
				CustomersModel customersModel2 = new CustomerServices().deleteCustomerData(id);
				if (customersModel.getShortCode() == 0) {
					JOptionPane.showMessageDialog(null, purchaseModel.getShortMessage());
					new CustomerServices().showCustomerData(customers_table);
					customerFirstNameField.setText("");
					customerLastNameField.setText("");
					customerEmailField.setText("");
					customerPhoneField.setText("");
					customerAddressField.setText("");
				} else {
					JOptionPane.showMessageDialog(null, customersModel2.getShortMessage());
				}
			}
		});
		btnDeleteCustomer.setBackground(new Color(0, 0, 51));
		btnDeleteCustomer.setForeground(Color.WHITE);
		btnDeleteCustomer.setFont(new Font("Utsaah", Font.PLAIN, 28));
		btnDeleteCustomer.setBounds(10, 476, 252, 34);
		customerstab.add(btnDeleteCustomer);

		JPanel stocktab = new JPanel();
		tabbedPane.addTab("Stock", null, stocktab, null);
		stocktab.setLayout(null);

		JScrollPane scrollPane_4 = new JScrollPane();
		scrollPane_4.setBounds(321, 34, 509, 611);
		stocktab.add(scrollPane_4);

		stock_table = new JTable();
		scrollPane_4.setViewportView(stock_table);
		new ProductServices().showStockData(stock_table);
		stock_table.setFont(new Font("Utsaah", Font.PLAIN, 18));
		tabbedPane.setForegroundAt(6, new Color(204, 204, 204));
		tabbedPane.setBackgroundAt(6, new Color(0, 0, 51));

		JPanel userstab = new JPanel();
		tabbedPane.addTab("Users", null, userstab, null);
		userstab.setLayout(null);

		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setFont(new Font("Utsaah", Font.PLAIN, 23));
		lblFirstName.setBounds(10, 64, 139, 34);
		userstab.add(lblFirstName);

		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setFont(new Font("Utsaah", Font.PLAIN, 23));
		lblLastName.setBounds(10, 104, 139, 34);
		userstab.add(lblLastName);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("Utsaah", Font.PLAIN, 23));
		lblEmail.setBounds(10, 149, 139, 34);
		userstab.add(lblEmail);

		JLabel lblRole = new JLabel("Role");
		lblRole.setFont(new Font("Utsaah", Font.PLAIN, 23));
		lblRole.setBounds(10, 329, 139, 34);
		userstab.add(lblRole);

		JLabel lblCompany = new JLabel("Company");
		lblCompany.setFont(new Font("Utsaah", Font.PLAIN, 23));
		lblCompany.setBounds(10, 374, 139, 34);
		userstab.add(lblCompany);

		userFirstNameTextField = new JTextField();
		userFirstNameTextField.setFont(new Font("Utsaah", Font.PLAIN, 20));
		userFirstNameTextField.setBounds(105, 68, 174, 27);
		userstab.add(userFirstNameTextField);
		userFirstNameTextField.setColumns(10);

		userLastNameEmailTextField = new JTextField();
		userLastNameEmailTextField.setFont(new Font("Utsaah", Font.PLAIN, 20));
		userLastNameEmailTextField.setColumns(10);
		userLastNameEmailTextField.setBounds(105, 109, 174, 27);
		userstab.add(userLastNameEmailTextField);

		userEmailTextField = new JTextField();
		userEmailTextField.setFont(new Font("Utsaah", Font.PLAIN, 20));
		userEmailTextField.setColumns(10);
		userEmailTextField.setBounds(105, 149, 174, 27);
		userstab.add(userEmailTextField);

		companyTextField = new JTextField();
		companyTextField.setFont(new Font("Utsaah", Font.PLAIN, 20));
		companyTextField.setColumns(10);
		companyTextField.setBounds(105, 374, 174, 27);
		userstab.add(companyTextField);

		roleComboBox = new JComboBox();
		roleComboBox.setModel(new DefaultComboBoxModel(new String[] { "*Select Role*", "admin", "user" }));
		roleComboBox.setFont(new Font("Utsaah", Font.PLAIN, 18));
		roleComboBox.setBounds(105, 333, 174, 27);
		userstab.add(roleComboBox);

		JScrollPane scrollPane_5 = new JScrollPane();
		scrollPane_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DefaultTableModel model = (DefaultTableModel) users_table.getModel();
				int selectedRowIndex = users_table.getSelectedRow();
				usersLoginModel.setId(Integer.parseInt(model.getValueAt(selectedRowIndex, 0).toString()));
				userFirstNameTextField.setText(model.getValueAt(selectedRowIndex, 1).toString());
				userLastNameEmailTextField.setText(model.getValueAt(selectedRowIndex, 2).toString());
				userEmailTextField.setText(model.getValueAt(selectedRowIndex, 3).toString());
				phoneTextField.setText(model.getValueAt(selectedRowIndex, 4).toString());
				roleComboBox.setSelectedItem(model.getValueAt(selectedRowIndex, 5).toString());
				companyTextField.setText(model.getValueAt(selectedRowIndex, 6).toString());
			}
		});
		scrollPane_5.setBounds(315, 53, 778, 603);
		userstab.add(scrollPane_5);

		users_table = new JTable();
		scrollPane_5.setViewportView(users_table);
		users_table.setFont(new Font("Utsaah", Font.PLAIN, 18));

		usersLoginServices.showUsersData(users_table);

		productServices.showProductsData(products_table);

		JButton addUserButton = new JButton("Add New User");
		addUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UsersLoginModel usersloginModels = new UsersLoginServices().addUserData(
						userFirstNameTextField.getText(), userLastNameEmailTextField.getText(),
						userEmailTextField.getText(), phoneTextField.getText(), textFieldLogin.getText(),
						passwordFieldLogin.getText(), roleComboBox.getSelectedItem(), companyTextField.getText(), id);
				if (usersloginModels.getShortCode() == 0) {
					JOptionPane.showMessageDialog(null, usersloginModels.getShortMessage());
					usersLoginServices.showUsersData(users_table);
					userFirstNameTextField.setText("");
					userLastNameEmailTextField.setText("");
					userEmailTextField.setText("");
					phoneTextField.setText("");
					companyTextField.setText("");
				} else {
					JOptionPane.showMessageDialog(null, usersloginModels.getShortMessage());
				}
			}
		});
		addUserButton.setBackground(new Color(0, 0, 51));
		addUserButton.setForeground(Color.WHITE);
		addUserButton.setFont(new Font("Utsaah", Font.PLAIN, 22));
		addUserButton.setBounds(10, 466, 232, 34);
		userstab.add(addUserButton);

		JButton editUserButton = new JButton("Update User");
		editUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int userID = usersLoginModel.getId();
				UsersLoginModel userloginModel = new UsersLoginServices().updateUserData(
						userFirstNameTextField.getText(), userLastNameEmailTextField.getText(),
						userEmailTextField.getText(), phoneTextField.getText(), textFieldLogin.getText(),
						passwordFieldLogin.getText(), roleComboBox.getSelectedItem(), companyTextField.getText(),
						userID, id);
				if (userloginModel.getShortCode() == 0) {
					JOptionPane.showMessageDialog(null, userloginModel.getShortMessage());
					usersLoginServices.showUsersData(users_table);
					userFirstNameTextField.setText("");
					userLastNameEmailTextField.setText("");
					userEmailTextField.setText("");
					phoneTextField.setText("");
					companyTextField.setText("");
				} else {
					JOptionPane.showMessageDialog(null, usersLoginModel.getShortMessage());
				}
			}
		});
		editUserButton.setForeground(Color.WHITE);
		editUserButton.setFont(new Font("Utsaah", Font.PLAIN, 22));
		editUserButton.setBackground(new Color(0, 0, 51));
		editUserButton.setBounds(10, 511, 232, 34);
		userstab.add(editUserButton);

		JButton deleteUserButton = new JButton("Delete User");
		deleteUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int userID = usersLoginModel.getId();
				UsersLoginModel usersLoginModel = new UsersLoginServices().deleteUserData(userID);
				if (usersLoginModel.getShortCode() == 0) {
					JOptionPane.showMessageDialog(null, usersLoginModel.getShortMessage());
					usersLoginServices.showUsersData(users_table);
					userFirstNameTextField.setText("");
					userLastNameEmailTextField.setText("");
					userEmailTextField.setText("");
					phoneTextField.setText("");
					companyTextField.setText("");
				} else {
					JOptionPane.showMessageDialog(null, usersLoginModel.getShortMessage());
				}
			}

		});
		deleteUserButton.setForeground(Color.WHITE);
		deleteUserButton.setFont(new Font("Utsaah", Font.PLAIN, 22));
		deleteUserButton.setBackground(new Color(0, 0, 51));
		deleteUserButton.setBounds(10, 556, 232, 34);
		userstab.add(deleteUserButton);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Utsaah", Font.PLAIN, 23));
		lblUsername.setBounds(10, 239, 139, 34);
		userstab.add(lblUsername);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Utsaah", Font.PLAIN, 23));
		lblPassword.setBounds(10, 284, 139, 34);
		userstab.add(lblPassword);

		textFieldLogin = new JTextField();
		textFieldLogin.setFont(new Font("Utsaah", Font.PLAIN, 20));
		textFieldLogin.setColumns(10);
		textFieldLogin.setBounds(105, 239, 174, 27);
		userstab.add(textFieldLogin);

		JLabel lblPhone = new JLabel("Phone");
		lblPhone.setFont(new Font("Utsaah", Font.PLAIN, 23));
		lblPhone.setBounds(10, 194, 139, 34);
		userstab.add(lblPhone);

		phoneTextField = new JTextField();
		phoneTextField.setFont(new Font("Utsaah", Font.PLAIN, 20));
		phoneTextField.setColumns(10);
		phoneTextField.setBounds(105, 190, 174, 27);
		userstab.add(phoneTextField);

		passwordFieldLogin = new JPasswordField();
		passwordFieldLogin.setFont(new Font("Utsaah", Font.PLAIN, 20));
		passwordFieldLogin.setBounds(105, 291, 174, 27);
		userstab.add(passwordFieldLogin);
		tabbedPane.setForegroundAt(7, new Color(204, 204, 204));
		tabbedPane.setBackgroundAt(7, new Color(0, 0, 51));

		JButton logoutButton = new JButton("Logout");
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				new UsersLogin().setVisible(true);
			}
		});
		logoutButton.setFont(new Font("Utsaah", Font.PLAIN, 14));
		logoutButton.setBounds(984, 45, 134, 23);
		contentPane.add(logoutButton);

		if (role.equals("user")) {
			productDeleteButton.hide();
			catDeleteButton.hide();

			tabbedPane.setEnabledAt(7, false);
			salesDeleteButton.hide();
			btnDeleteCustomer.hide();

		}

	}
}
