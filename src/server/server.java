package server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.security.KeyStore;
import java.util.LinkedList;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;
import javax.security.cert.X509Certificate;

import user.Gov;
import user.User;

public class server implements Runnable {
	private ServerSocket serverSocket = null;
	private static int numConnectedClients = 0;

	public server(ServerSocket ss) throws IOException {
		serverSocket = ss;
		newListener();
	}

	public void run() {
		try {

			User authUser = null;
			SSLSocket socket = (SSLSocket) serverSocket.accept();
			newListener();
			SSLSession session = socket.getSession();
			X509Certificate cert = (X509Certificate) session.getPeerCertificateChain()[0];
			String subject = cert.getSubjectDN().getName();
			numConnectedClients++;
			System.out.println("client connected");
			System.out.println("client name (cert subject DN field): " + subject);
			System.out.println(cert.getIssuerDN());
			System.out.println(numConnectedClients + " concurrent connection(s)\n");

			PrintWriter out = null;
			BufferedReader in = null;
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String clientMsg = null;
			while ((clientMsg = in.readLine()) != null) {
				System.out.println("received '" + clientMsg + "' from client");

				String[] split = clientMsg.split(" ");
				String cmd = "";
				String param1 = "";
				String param2 = "";
				String param3 = "";

				if (split.length > 0) {
					cmd = split[0];
				}

				if (split.length > 1) {
					param1 = split[1];
				}

				if (split.length > 2) {
					param2 = split[2];
				}

				if (split.length > 3) {
					param3 = split[3];
				}

				switch (cmd) {
				// Usage: login username password
				case "login":
					if (authUser != null) {
						out.println("Cannot authenticate as another user when logged in");
						break;
					}
					User user = Authenticator.getInstance().authenticate(param1, param2);
					if (user == null) {
						out.println("Invalid username or password");
					} else {
						authUser = user;
						out.println("Successfully authenticated as " + user.role + " " + user.ID);
					}
					break;

				// Usage: logout
				case "logout":
					if (authUser == null) {
						out.println("Not logged in.");
						break;
					}

					authUser = null;
					out.println("Successfully logged out.");
					break;

				// Usage: getjournal journalId
				case "getjournal":
					if (authUser == null) {
						out.println("Not authenticated");
					} else {
						try {
							int journalId = Integer.parseInt(param1);
							LinkedList<Journal> journals = JournalHandler.getInstance().getJournals(authUser);
							Journal sought = null;
							for(Journal e : journals){
								if(e.getID() == journalId){
									sought = e;
									break;
								}
							}
							if(sought == null){
								out.println("The journal with that ID either does not exist or you are not authenticated to read it.");
							}else{
								out.println(sought.toString());
							}
						} catch (NumberFormatException e) {
							out.println("The IDs can only be numbers.");
						}
					}
					break;
					
				// Usage: createjournal nurseId patientId
				case "createjournal":
					if (authUser == null) {
						out.println("Not authenticated");
					} else {
						try {
							int nurseId = Integer.parseInt(param1);
							int patientId = Integer.parseInt(param2);
							User nurse = Authenticator.getInstance().getUser(nurseId);
							User patient = Authenticator.getInstance().getUser(patientId);
							boolean success = JournalHandler.getInstance().createJournal(authUser, nurse, patient);
							out.println(success ? "Successfully created journal" : "Could not create journal.");
						} catch (NumberFormatException e) {
							out.println("The IDs can only be numbers.");
						}
					}
					break;
					
				// deletejournal patientId journalId
				case "deletejournal":
					if (authUser == null) {
						out.println("Not authenticated");
					} else {
						try {
							int patientId = Integer.parseInt(param1);
							int journalID = Integer.parseInt(param2);
							User patient = Authenticator.getInstance().getUser(patientId);
							boolean success = JournalHandler.getInstance().deleteJournal(authUser, patient, journalID);
							if (success) {
								out.println("Journal with ID " + journalID + " was successfully deleted");
							} else {
								out.println("Journal with ID " + journalID + " could not be deleted");
							}
						} catch (NumberFormatException e) {
							out.println("Journal ID can only be digits. Please obey");
							e.printStackTrace();
						}
					}
					break;
				
				// Can only be used by government
				// Usage: listjournals
				case "listjournals":
					if (authUser == null || !(authUser instanceof Gov)) {
						out.println("Not authenticated");
					} else {
						LinkedList<Journal> journals = JournalHandler.getInstance().getJournals(authUser);
						StringBuilder sb = new StringBuilder();
						for (Journal journal : journals) {
							sb.append(journal.toString() + "\\n");
						}
						out.println(sb.toString());
					}
					break;
					
				// Can only be used by government
				// Usage: listusers
				case "listusers":
					if (authUser == null) {
						out.println("Not Authenticated");
					} else {
						out.println(Authenticator.getInstance().getUsers());
					}
					break;
				}

				out.flush();
			}

			in.close();
			out.close();
			socket.close();
			numConnectedClients--;
			System.out.println("client disconnected");
			System.out.println(numConnectedClients + " concurrent connection(s)\n");
		} catch (IOException e) {
			System.out.println("Client died: " + e.getMessage());
			e.printStackTrace();
			return;
		}
	}

	private void newListener() {
		(new Thread(this)).start();
	} // calls run()

	public static void main(String args[]) {
		System.out.println("\nServer Started\n");
		int port = -1;
		if (args.length >= 1) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 3000;
		}
		String type = "TLS";
		try {
			ServerSocketFactory ssf = getServerSocketFactory(type);
			ServerSocket ss = ssf.createServerSocket(port);
			((SSLServerSocket) ss).setNeedClientAuth(true); // enables client
			// authentication
			new server(ss);
		} catch (IOException e) {
			System.out.println("Unable to start Server: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static ServerSocketFactory getServerSocketFactory(String type) {
		if (type.equals("TLS")) {
			SSLServerSocketFactory ssf = null;
			try { // set up key manager to perform server authentication
				SSLContext ctx = SSLContext.getInstance("TLS");
				KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
				TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
				KeyStore ks = KeyStore.getInstance("JKS");
				KeyStore ts = KeyStore.getInstance("JKS");
				char[] password = "password".toCharArray();

				ks.load(new FileInputStream("certs/serverkeystore"), password); // keystore
				// password
				// (storepass)
				ts.load(new FileInputStream("certs/servertruststore"), password); // truststore
				// password
				// (storepass)
				kmf.init(ks, password); // certificate password (keypass)
				tmf.init(ts); // possible to use keystore as truststore here
				ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
				ssf = ctx.getServerSocketFactory();
				return ssf;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return ServerSocketFactory.getDefault();
		}
		return null;
	}
}
