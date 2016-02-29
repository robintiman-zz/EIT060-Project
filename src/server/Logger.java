package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

	public static File log = new File("log.txt");

	public static void log(String data) {
		try {
			FileWriter fr = new FileWriter(log, true);
			BufferedWriter br = new BufferedWriter(fr);
			br.write("\r\n" + data);
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
