package user;

public abstract class Worker extends User {
	
	private String division;
	
	public Worker(String password, String role, int ID, String division) {
		super(password, role, ID);
		this.division = division;
	}
	
	public String getDivision() {
		return division;
	}
}
