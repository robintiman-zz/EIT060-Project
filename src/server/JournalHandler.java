package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import user.Doctor;
import user.Gov;
import user.Nurse;
import user.Patient;
import user.User;

public class JournalHandler {
	private HashMap<User, LinkedList<Journal>> database;
	private static JournalHandler journalHandler;
	
	private JournalHandler() {
		database = new HashMap<User, LinkedList<Journal>>();
	}
	
	public static JournalHandler getInstance() {
		if (journalHandler == null) {
			journalHandler = new JournalHandler();
		}
		
		return journalHandler;
	}

	public boolean createJournal(User doctor, User nurse, User patient) {
		if (doctor.role.equals(User.DOCTOR) 
				&& nurse.role.equals(User.NURSE) 
				&& patient.role.equals(User.PATIENT)) {
			
			LinkedList<Journal> journals = database.get(patient);
			int newJournalId = 0;
			if (journals != null) {
				newJournalId = journals.getLast().ID + 1;
			} else {
				journals = new LinkedList<Journal>();
			}
			System.out.println("Added journal id: " + newJournalId);
			Journal journal = new Journal((Patient) patient, (Doctor) doctor, (Nurse) nurse, newJournalId);
			journals.add(journal);
			database.put(patient, journals);
			Logger.log(
					"Doctor " + doctor.ID + " created journal for patient " + patient.ID + " with nurse " + nurse.ID);
			return true;
		} else {
			return false;
		}
	}

	public boolean deleteJournal(User user, User patient, int journalID) {
		boolean successful = false;
		if (user.role.equals(User.GOV)) {
			LinkedList<Journal> journals = database.get(patient);
			if (journals == null) {
				return false;
			}
			Journal delete = null;
			for (Journal a : journals) {
				if (a.ID == journalID) {
					delete = a;
					break;
				}
			}
			if (delete != null) {
				successful = journals.remove(delete);
			}
		}
		if (successful) {
			Logger.log("Government agent " + user.ID + " deleted journal " + journalID + " for patient " + patient.ID);
		}
		return successful;
	}

	public LinkedList<Journal> getJournals(User user) {
		LinkedList<Journal> send = new LinkedList<Journal>();
		
		
		if (!(user instanceof Gov)) {	
			LinkedList<Journal> journals = database.get(user);
			if (user == null || journals == null) {
				return new LinkedList<Journal>();
			}
			for (Journal a : database.get(user)) {
				send.add(a);
			}
		} else {
			// If government, list all journals
			LinkedList<LinkedList<Journal>> journalsList = new LinkedList<LinkedList<Journal>>(database.values());
			LinkedList<Journal> journals = new LinkedList<Journal>();
			for (LinkedList<Journal> list : journalsList) {
				journals.addAll(list);
			}
			send = journals;
		}
		return send;
	}
}
