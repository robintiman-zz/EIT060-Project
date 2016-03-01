package server;

import java.util.ArrayList;
import java.util.HashMap;

import user.User;
import user.UserFactory;

public class Authenticator {
	
	//username:password
	private static HashMap<String, User> users = new HashMap<String, User>();
	private static Authenticator authenticator;
	
	private Authenticator() {
		
		// Hard coded dummy data
		// Doctors
		users.put("doktor1:hejsan", UserFactory.createDoctor(1, "division1"));
		users.put("doktor2:hejsan", UserFactory.createDoctor(2, "division2"));
		users.put("doktor3:hejsan", UserFactory.createDoctor(3, "division1"));
		
		// Nurses
		users.put("nurse1:hejsan", UserFactory.createNurse(4, "division1"));
		users.put("nurse2:hejsan", UserFactory.createNurse(5, "division2"));
		users.put("nurse3:hejsan", UserFactory.createNurse(6, "division1"));
		
		// Patients
		users.put("patient1:hejsan", UserFactory.createPatient(7));
		users.put("patient2:hejsan", UserFactory.createPatient(9));
		users.put("patient3:hejsan", UserFactory.createPatient(10));
		users.put("patient4:hejsan", UserFactory.createPatient(11));
		
		// Government
		users.put("gov:hejsan", UserFactory.createGov(0));
	}
	
	public User authenticate(String username, String password) {
		String key = username + ":" + password;
		// TODO bcrypt?
		return users.get(key);
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
	
	public String getUsers() {
		StringBuilder sb = new StringBuilder();
		ArrayList<User> userList = new ArrayList<User>(users.values());
		System.out.println("Number of users: " + userList.size());
		for (User u : userList) {
			System.out.println("s");
			sb.append(u.role + " with ID " + u.ID + "\\n");
		}
		return sb.toString();
	}
}
