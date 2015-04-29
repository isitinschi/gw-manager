package com.guesswhat.manager.view.pane;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.guesswhat.manager.view.panel.BackupPanel;
import com.guesswhat.manager.view.panel.CreatePanel;
import com.guesswhat.manager.view.panel.UpdatePanel;

@SuppressWarnings("serial")
public class ManagerViewTabbedPane extends JTabbedPane implements ChangeListener {
	
	private UpdatePanel updatePanel = null;

	public ManagerViewTabbedPane(CreatePanel createPanel, UpdatePanel updatePanel, BackupPanel backupPanel) {
		this.updatePanel = updatePanel;
		
		addTab("Create", createPanel);
		addTab("Update", updatePanel);
		addTab("Backup", backupPanel);
		
		addChangeListener(this);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (getSelectedIndex() == 1) { // update tab
			updatePanel.updateQuestionsList();
		}
	}
	
}
