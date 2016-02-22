package server;

import java.util.HashMap;
import java.util.LinkedList;

import user.User;

public class JournalHandler {
	private HashMap<User, LinkedList<Journal>> database;
	
	public JournalHandler() {
		database = new HashMap<User, LinkedList<Journal>>();
	}
	
	public LinkedList<Journal> getJournal(User user) {
		return database.get(user);
	}
}
