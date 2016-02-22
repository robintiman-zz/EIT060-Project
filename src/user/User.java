package user;

public abstract class User {
	private static String role;
	private static int ID;

	public void authenticate() {
		
	}
	
	public boolean isAuthenticated() {
		return true;
	}

	public String getRole() {
		return role;
	}
}
