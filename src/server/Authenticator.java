package server;

import java.util.ArrayList;
import java.util.HashMap;

import org.mindrot.jbcrypt.BCrypt;

import user.User;
import user.UserFactory;

public class Authenticator {
	
	//username:password
	private static HashMap<String, User> users = new HashMap<String, User>();
	private static Authenticator authenticator;
	
	private Authenticator() {
		
		String hashedPw = BCrypt.hashpw("hejsan", BCrypt.gensalt());
		// Hard coded dummy data
		// Doctors
		users.put("doktor1", UserFactory.createDoctor(BCrypt.hashpw("hejsan", BCrypt.gensalt()), 1, "division1"));
		users.put("doktor2", UserFactory.createDoctor(BCrypt.hashpw("hejsan", BCrypt.gensalt()), 2, "division2"));
		users.put("doktor3", UserFactory.createDoctor(BCrypt.hashpw("hejsan", BCrypt.gensalt()), 3, "division1"));
		
		// Nurses
		users.put("nurse1", UserFactory.createNurse(BCrypt.hashpw("hejsan", BCrypt.gensalt()), 4, "division1"));
		users.put("nurse2", UserFactory.createNurse(BCrypt.hashpw("hejsan", BCrypt.gensalt()), 5, "division2"));
		users.put("nurse3", UserFactory.createNurse(BCrypt.hashpw("hejsan", BCrypt.gensalt()), 6, "division1"));
		
		// Patients
		users.put("patient1", UserFactory.createPatient(BCrypt.hashpw("hejsan", BCrypt.gensalt()), 7));
		users.put("patient2", UserFactory.createPatient(BCrypt.hashpw("hejsan", BCrypt.gensalt()), 9));
		users.put("patient3", UserFactory.createPatient(BCrypt.hashpw("hejsan", BCrypt.gensalt()), 10));
		users.put("patient4", UserFactory.createPatient(BCrypt.hashpw("hejsan", BCrypt.gensalt()), 11));
		
		// Government
		users.put("gov", UserFactory.createGov(BCrypt.hashpw("hejsan", BCrypt.gensalt()), 0));
	}
	
	public User authenticate(String username, String password) {
		User user = users.get(username);
		
		if (user == null) return null;
		
		String hash = user.getPassword();
		
		if (BCrypt.checkpw(password, hash)) {
			return user;
		} else {
			return null;
		}
		
	}
	
	public static Authenticator getInstance() {
		if (authenticator == null) {
			authenticator = new Authenticator();
		} 
		
		return authenticator;
	}
	
	public User getUser(int id) {
		ArrayList<User> userList = new ArrayList<User>(users.values());
		
		for (User user : userList) {
			if (user.ID == id) {
				return user;
			}
		}
		
		return null;
	}
	
	public String getUsers(User user) {
		StringBuilder sb = new StringBuilder();
		ArrayList<User> userList = new ArrayList<User>(users.values());
		System.out.println("Number of users: " + userList.size());
		for (User u : userList) {
			sb.append(u.role + " with ID " + u.ID + "\\n");
		}
		Logger.log(user.role + " with ID " + user.ID + " listed all users");
		return sb.toString();
	}
}
