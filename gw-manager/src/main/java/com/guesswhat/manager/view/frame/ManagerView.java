package com.guesswhat.manager.view.frame;

import javax.swing.JFrame;

import com.guesswhat.manager.view.pane.ManagerViewTabbedPane;

@SuppressWarnings("serial")
public class ManagerView extends JFrame {

	public ManagerView(ManagerViewTabbedPane tabbedPane) {
		super("Guess What Manager");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultLookAndFeelDecorated(true);
		
		add(tabbedPane);
		
		pack();
		setLocationRelativeTo(null);

		setVisible(true);
	}
	
}
