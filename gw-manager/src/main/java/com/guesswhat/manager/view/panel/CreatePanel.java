package com.guesswhat.manager.view.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.springframework.beans.factory.annotation.Autowired;

import com.guesswhat.manager.controller.ManagerController;
import com.guesswhat.manager.dto.QuestionDTO;
import com.guesswhat.manager.view.field.AnswerField;

@SuppressWarnings("serial")
public class CreatePanel extends JPanel implements ActionListener {

	@Autowired
	private ManagerController managerController;
	
	private ImagePanel imageQuestionPanel = null;
	private ImagePanel imageAnswerPanel = null;
	
	private JPanel northPanel = new JPanel(new FlowLayout());
	private JPanel centerPanel = new JPanel(new FlowLayout());
	private JPanel southPanel = new JPanel(new FlowLayout());

	private AnswerField answerField1 = null, answerField2 = null,
			answerField3 = null, answerField4 = null;

	private JButton resetButton = null;
	private JButton uploadButton = null;

	public CreatePanel() {
		imageQuestionPanel = new ImagePanel();
		imageAnswerPanel = new ImagePanel();
		answerField1 = new AnswerField();
		answerField2 = new AnswerField();
		answerField3 = new AnswerField();
		answerField4 = new AnswerField();
		ButtonGroup group = new ButtonGroup();
		group.add(answerField1);
		group.add(answerField2);
		group.add(answerField3);
		group.add(answerField4);
		resetButton = new JButton("Reset");
		uploadButton = new JButton("Upload");

		setLayout(new BorderLayout());
		northPanel.add(answerField1);
		northPanel.add(answerField2);
		northPanel.add(answerField3);
		northPanel.add(answerField4);
		add(northPanel, BorderLayout.NORTH);
		JScrollPane imageBeforePane = new JScrollPane(imageQuestionPanel);
		JScrollPane imageAfterPane = new JScrollPane(imageAnswerPanel);
		centerPanel.add(imageBeforePane);
		centerPanel.add(imageAfterPane);
		add(centerPanel, BorderLayout.CENTER);
		southPanel.add(resetButton);
		southPanel.add(uploadButton);
		add(southPanel, BorderLayout.SOUTH);
		
		resetButton.addActionListener(this);
		uploadButton.addActionListener(this);

		setVisible(true);
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource().equals(resetButton)) {
			clearQuestion();			
		} else if (ae.getSource().equals(uploadButton)) {
			uploadQuestion();
		}
	}

	private void uploadQuestion() {
		String correctAnswer = null;
		if (answerField1.isSelected()) {
			correctAnswer = answerField1.getAnswer();
		} else if (answerField2.isSelected()) {
			correctAnswer = answerField2.getAnswer();
		} else if (answerField3.isSelected()) {
			correctAnswer = answerField3.getAnswer();
		} else if (answerField4.isSelected()) {
			correctAnswer = answerField4.getAnswer();
		}
		
		QuestionDTO questionDTO = new QuestionDTO();
		questionDTO.setAnswer1(answerField1.getAnswer());
		questionDTO.setAnswer2(answerField2.getAnswer());
		questionDTO.setAnswer3(answerField3.getAnswer());
		questionDTO.setAnswer4(answerField4.getAnswer());
		questionDTO.setCorrectAnswer(correctAnswer);
		managerController.uploadQuestion(questionDTO, imageQuestionPanel.getImagePath(), imageAnswerPanel.getImagePath());
	}

	private void clearQuestion() {
		imageQuestionPanel.setImage("");
		imageAnswerPanel.setImage("");
		answerField1.setAnswer("");
		answerField2.setAnswer("");
		answerField3.setAnswer("");
		answerField4.setAnswer("");
	}

	public void setManagerController(ManagerController managerController) {
		this.managerController = managerController;
	}
	
}
