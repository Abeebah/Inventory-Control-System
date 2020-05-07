package inventoryModels;
//import oracle.sql.NUMBER;

public class CategoryModel {
	private int id;
	private String name;
	private String description;
	private int shortCode;
	private String shortMessage;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
