package banking;

public class Company extends AccountHolder {
	private String companyName;

	public Company(String companyName, int idNumber) {
		super(idNumber);
		this.companyName = companyName;
	}

	public String getCompanyName() {
		return companyName;
	}
}