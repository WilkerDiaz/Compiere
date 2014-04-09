package compiere.model.cds.processes;

public class SaleCustomer {

	int customerId;
	String customerValue;
	int customerLocationId;
	String customerName;
	
	public SaleCustomer() {
		super();
	}
	
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getCustomerValue() {
		return customerValue;
	}
	public void setCustomerValue(String customerValue) {
		this.customerValue = customerValue;
	}
	public int getCustomerLocationId() {
		return customerLocationId;
	}
	public void setCustomerLocationId(int customerLocationId) {
		this.customerLocationId = customerLocationId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
}
