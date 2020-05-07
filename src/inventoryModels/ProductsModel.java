package inventoryModels;

public class ProductsModel {
	private int id;
	private String name;
	private int price;
	private int quantity;
	private String category;
	private int cost;
	private String shortMessage;
	private int shortCode;
	private int total;
	private int totalCost;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id ;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getPrice() {
		return price;
	}
	public void setPrice(int price){
		this.price = price;
	}
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	

	public int gettotalCost() {
		return totalCost;
	}
	public void settotalCost(int totalCost) {
		this.totalCost = totalCost;
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
		
		public int getTotal() {
			return total;
		}
		public void setTotal(int total) {
			this.total = total;
		}
}
