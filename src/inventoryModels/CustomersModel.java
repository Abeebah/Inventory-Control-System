package inventoryModels;

public class CustomersModel {
	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String address;
	private int shortCode;
	private String shortMessage;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
		
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
