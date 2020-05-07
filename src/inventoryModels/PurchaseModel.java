package inventoryModels;

public class PurchaseModel {
	private int id;
	private int product_id;
	private int quantity;
	private String shortMessage;
	private int shortCode;
	private int total;
	
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
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public int getTotal() {
		return total;
	}
	
	public String getShortMessage() {
		return shortMessage;
	}

	public void setShortMessage(String shortMessage) {
		this.shortMessage = shortMessage;
	}

	public int getShortCode() {
		return shortCode;
	}

	public void setShortCode(int shortCode) {
		this.shortCode = shortCode;
	}
		
	
	public void setTotal(int total) {
		this.total = total;
	}
}
