package com.guesswhat.manager.utils;

import javax.swing.JOptionPane;

public class MessageDialog {
	
	public static void showErrorDialog(String action, String object) {
		JOptionPane.showMessageDialog(null,
				"Cannot " + action + " " + object,
				"Error",
				JOptionPane.ERROR_MESSAGE);
	}
	
}
