package user;

public class Nurse extends Worker {

	public Nurse(String password, int ID, String division) {
		super(password, User.NURSE, ID, division);
	}
	
}
