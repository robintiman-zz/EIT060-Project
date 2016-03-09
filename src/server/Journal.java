package server;

import java.util.ArrayList;

import user.Doctor;
import user.Nurse;
import user.Patient;
import user.User;

public class Journal {
	public final int ID;
	private String division;
	private User patient, doc, nurse;
	private ArrayList<String> dataList;

	public Journal(Patient patient, Doctor doc, Nurse nurse, int ID) {
		this.patient = patient;
		dataList = new ArrayList<String>();
		this.doc = doc;
		division = doc.getDivision();
		this.nurse = nurse;
		this.ID = ID;
	}

	public User getNurse() {
		return nurse;
	}

	public User getDoc() {
		return doc;
	}

	public String getDivision() {
		return division;
	}

	public int getID() {
		return ID;
	}

	public User getPatient() {
		return patient;
	}

	@Override
	public String toString() {
		return "JournalID: " + ID + ", DoctorID: " + doc.ID + ", NurseID: " + nurse.ID + ", PatientID: " + patient.ID;
	}

	public String[] getData(User user) {
		boolean accessRights = false;
		switch (user.role) {
		case User.DOCTOR:
			Doctor doc = (Doctor) user;
			if (doc.getDivision().equals(division))
				accessRights = true;
			break;
		case User.GOV:
			accessRights = true;
			break;
		case User.NURSE:
			Nurse nurse = (Nurse) user;
			if (nurse.getDivision().equals(division))
				accessRights = true;
			break;
		case User.PATIENT:
			if (user.ID == patient.ID)
				accessRights = true;
			break;
		}
		if (accessRights) {
			String[] send = new String[dataList.size()];
			for (int i = 0; i < dataList.size(); i++) {
				send[i] = dataList.get(i);
			}
			Logger.log(
					user.role + " " + user.ID + " fetched the data of journal " + ID + " from patient " + patient.ID);
			return send;
		} else {
			return null;
		}
	}

	public boolean appendMedicalStuff(User user, String data) {
		boolean canModify = false;
		if (user.role == User.NURSE && user.ID == nurse.ID) {
			canModify = true;
		} else if (user.role == User.DOCTOR && user.ID == doc.ID) {
			canModify = true;
		}
		if (canModify) {
			dataList.add(data);
			Logger.log(user.role + " " + user.ID + " wrote data in journal " + ID + " for patient " + patient.ID);
			return true;
		} else {
			return false;
		}
	}

}
