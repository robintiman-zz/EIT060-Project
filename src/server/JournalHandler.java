package server;

import java.util.HashMap;
import java.util.LinkedList;

import user.Doctor;
import user.Nurse;
import user.Patient;
import user.User;

public class JournalHandler {
	private HashMap<User, LinkedList<Journal>> database;

	public JournalHandler() {
		database = new HashMap<User, LinkedList<Journal>>();
	}

	public boolean createJournal(User doctor, User nurse, User patient) {
		if (doctor.role.equals(User.DOCTOR) && nurse.role.equals(User.NURSE) && patient.role.equals(User.PATIENT)) {
			LinkedList<Journal> journals = database.get(patient);
			Journal journal = new Journal((Patient) patient, (Doctor) doctor, (Nurse) nurse, journals.getLast().ID + 1);
			journals.add(journal);
			return true;
		} else {
			return false;
		}
	}

	public boolean deleteJournal(User user, User patient, int journalID) {
		if (user.role.equals(User.GOV)) {
			LinkedList<Journal> journals = database.get(patient);
			Journal delete = null;
			for (Journal a : journals) {
				if (a.ID == journalID) {
					delete = a;
					break;
				}
			}
			if (delete != null) {
				return journals.remove(delete);
			}
		}
		return false;
	}

	public LinkedList<Journal> getJournals(User user) {
		LinkedList<Journal> send = new LinkedList<Journal>();
		for (Journal a : database.get(user)) {
			send.add(a);
		}
		return send;
	}
}
