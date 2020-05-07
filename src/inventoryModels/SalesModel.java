package inventoryModels;

public class SalesModel {
	 private int id;
	 private int product_id;
	 private int customer_id;
	 public int shortCode;
	 public String shortMessage;
	 
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getProduct_id(){
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	
	public int getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}
	
	public int getShortCode() {
		return shortCode;
	}
	public void setShortCode(int shortcode) {
		this.shortCode = shortcode;
	}
	
	public String getShortMessage() {
		return shortMessage;
	}
	
	public void setShortMessage(String shortMessage) {
		this.shortMessage = shortMessage;
	}	
}
