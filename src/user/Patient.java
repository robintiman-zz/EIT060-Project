package user;

public class Patient extends User {
	
	public Patient(String password, int ID) {
		super(password, User.PATIENT, ID);
	}

}
