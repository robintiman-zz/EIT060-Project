package client;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginScreen extends JFrame {
	private boolean success = false;
	private GUI gui;

	public LoginScreen(GUI gui) {
		this.gui = gui;
		this.setTitle("Login");
		this.setVisible(true);
		JButton logButt = new JButton("Click to login");
		logButt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}

		});
	}

	private class LoginDialog extends JDialog {

		private LoginDialog(Frame parent) {
			super(parent, "Login", true);
			JPanel panel = new JPanel(new GridBagLayout());
			GridBagConstraints gs = new GridBagConstraints();
			gs.fill = GridBagConstraints.HORIZONTAL;

			// Adding the components
			JLabel unameLabel = new JLabel("Username: ");
			gs.gridx = 0;
			gs.gridy = 1;
			panel.add(unameLabel, gs);
			
			JTextField unameField = new JTextField(20);
			gs.gridx = 1;
			gs.gridy = 0;
			panel.add(unameField, gs);
			
			JLabel pwdLabel = new JLabel("Password: ");
			gs.gridx = 0;
			gs.gridy = 1;
			panel.add(pwdLabel, gs);
			
			JPasswordField pwdField = new JPasswordField(20);
			gs.gridx = 1;
			gs.gridy = 1;
			panel.add(pwdField, gs);
			
			// Buttons for login and cancel
			JButton buttLog = new JButton("Login");
			buttLog.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// Only for testing purposes
					String actualPwd = "lösenlösen";
					String user = "Robin";
					
					// Checks if username and password is correct
					char[] typedPwd = pwdField.getPassword();
					if (!user.equals(unameField.getText())) return;
					for (int i = 0; i < typedPwd.length; i++) {
						if (typedPwd[i] != actualPwd.charAt(i)) return;
					}
					success = true;
					if (success) {
						success = false;
						gui.activate();
						dispose();
					} else {
						JOptionPane.showMessageDialog(LoginDialog.this, "Invalid username or password!");
					}
				}
				
			});
		}
	}
}
