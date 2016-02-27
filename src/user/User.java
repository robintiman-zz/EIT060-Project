package user;

public abstract class User {
	public static final String NURSE = "nurse";
	public static final String DOCTOR = "doctor";
	public static final String GOV = "government";
	public static final String PATIENT = "patient";
	public final String role;
	public final int ID;

	public User(String role, int ID) {
		this.role = role;
		this.ID = ID;
	}

	public void authenticate() {

	}

	public boolean isAuthenticated() {
		return true;
	}
}
