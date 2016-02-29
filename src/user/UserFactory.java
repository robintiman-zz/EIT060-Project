package user;

public class UserFactory {

	public static User createPatient(int ID) {
		return new Patient(ID);
	}

	public static User createNurse(int ID, String division) {
		return new Nurse(ID, division);
	}

	public static User createDoctor(int ID, String division) {
		return new Doctor(ID, division);
	}

	public static User createGov(int ID) {
		return new Gov(ID);
	}
}
