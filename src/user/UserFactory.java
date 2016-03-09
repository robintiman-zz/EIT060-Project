package user;

public class UserFactory {

	public static User createPatient(String password, int ID) {
		return new Patient(password, ID);
	}

	public static User createNurse(String password, int ID, String division) {
		return new Nurse(password, ID, division);
	}

	public static User createDoctor(String password, int ID, String division) {
		return new Doctor(password, ID, division);
	}

	public static User createGov(String password, int ID) {
		return new Gov(password, ID);
	}
}
