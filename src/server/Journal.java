package server;

import java.util.ArrayList;

import user.Doctor;
import user.Nurse;
import user.Patient;
import user.User;

public class Journal {
	private String ID;
	private User patient, doc, nurse;
	private ArrayList<String> dataList;
	
	public Journal(Patient patient, Doctor doc, Nurse nurse) {
		this.patient = patient;
		dataList = new ArrayList<String>();
		this.doc = doc;
		this.nurse = nurse;
	}
	
	public User getNurse() {
		return nurse;
	}
	
	public User getDoc() {
		return doc;
	}
	
	public User getPatient() {
		return patient;
	}
	
	public void appendMedicalStuff(String data) {
		dataList.add(data);
	}
}
