package com.guesswhat.manager.view.panel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.springframework.beans.factory.annotation.Autowired;

import com.guesswhat.manager.service.face.BackupService;

@SuppressWarnings("serial")
public class BackupPanel extends JPanel implements ActionListener {

	@Autowired
	private BackupService backupService;
	
	private JButton saveBackupButton = null;
	private JButton loadBackupButton = null;
	private JFileChooser fc = null;

	public BackupPanel() {
		fc = new JFileChooser();
		saveBackupButton = new JButton("Save backup...");
		loadBackupButton = new JButton("Load backup...");
		
		setLayout(new FlowLayout());
		add(saveBackupButton);
		add(loadBackupButton);

		saveBackupButton.addActionListener(this);
		loadBackupButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource().equals(saveBackupButton)) {
			int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
            	backupService.downloadBackup(fc.getSelectedFile().getAbsolutePath());
            } 
		} else if (ae.getSource().equals(loadBackupButton)) {
			int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
            	backupService.uploadBackup(fc.getSelectedFile().getAbsolutePath());
            }
		}
	}

	
}
