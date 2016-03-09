package user;

public class Doctor extends Worker {
	
	public Doctor(String password, int ID, String division) {
		super(password, User.DOCTOR, ID, division);
	}
	
}
