package com.guesswhat.manager.view.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.springframework.beans.factory.annotation.Autowired;

import com.guesswhat.manager.dto.QuestionDTO;
import com.guesswhat.manager.service.face.ImageService;
import com.guesswhat.manager.service.face.QuestionService;
import com.guesswhat.manager.view.field.AnswerField;

@SuppressWarnings("serial")
public class UpdatePanel extends JPanel implements ActionListener {

	@Autowired
	private ImageService imageService;
	@Autowired
	private QuestionService questionService;
	
	private JComboBox<String> questionList = null;
	
	private ImagePanel imageQuestionPanel = null;
	private ImagePanel imageAnswerPanel = null;

	private AnswerField answerField1 = null;
	private AnswerField answerField2 = null;
	private AnswerField answerField3 = null;
	private AnswerField answerField4 = null;

	private JButton loadButton = null;
	private JButton removeButton = null;

	public UpdatePanel() {		
		questionList = new JComboBox<String>(new String []{"Empty"});

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
		loadButton = new JButton("Load");
		removeButton = new JButton("Remove");
		
		JPanel northPanel = new JPanel(new GridLayout(2,1));
		JPanel answerPanel = new JPanel(new FlowLayout());
		JPanel centerPanel = new JPanel(new FlowLayout());
		JPanel southPanel = new JPanel(new FlowLayout());
		
		setLayout(new BorderLayout());
		answerPanel.add(answerField1);
		answerPanel.add(answerField2);
		answerPanel.add(answerField3);
		answerPanel.add(answerField4);
		northPanel.add(questionList);
		northPanel.add(answerPanel);
		add(northPanel, BorderLayout.NORTH);
		JScrollPane imageBeforePane = new JScrollPane(imageQuestionPanel);
		JScrollPane imageAfterPane = new JScrollPane(imageAnswerPanel);
		centerPanel.add(imageBeforePane);
		centerPanel.add(imageAfterPane);
		add(centerPanel, BorderLayout.CENTER);
		southPanel.add(loadButton);
		southPanel.add(removeButton);
		add(southPanel, BorderLayout.SOUTH);
		
		loadButton.addActionListener(this);
		removeButton.addActionListener(this);

		setVisible(true);
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource().equals(removeButton)) {
			removeQuestion();
		} else if (ae.getSource().equals(loadButton)) {
			loadQuestion();
		}
	}

	private void loadQuestion() {
		String selectedItem = (String) questionList.getSelectedItem();
		if (!selectedItem.equals("Empty")) {
			String questionId = selectedItem.split("id=")[1].split(",")[0];
			String answer1 = selectedItem.split("answer1=")[1].split(",")[0];
			String answer2 = selectedItem.split("answer2=")[1].split(",")[0];
			String answer3 = selectedItem.split("answer3=")[1].split(",")[0];
			String answer4 = selectedItem.split("answer4=")[1].split(",")[0];
			String correctAnswer = selectedItem.split("correctAnswer=")[1].split("]")[0];
			
			answerField1.setAnswer(answer1);
			answerField2.setAnswer(answer2);
			answerField3.setAnswer(answer3);
			answerField4.setAnswer(answer4);
			
			if (correctAnswer.equals(answer1)) {
				answerField1.setSelected(true);
			} else if (correctAnswer.equals(answer2)) {
				answerField2.setSelected(true);
			} else if (correctAnswer.equals(answer3)) {
				answerField3.setSelected(true);
			} else if (correctAnswer.equals(answer4)) {
				answerField4.setSelected(true);
			}
			
			BufferedImage image = imageService.downloadQuestionImage(questionId);
			imageQuestionPanel.setImage(image);
			
			image = imageService.downloadAnswerImage(questionId);
			imageAnswerPanel.setImage(image);			
		}
	}
	
	private void removeQuestion() {
		String selectedItem = (String) questionList.getSelectedItem();
		if (!selectedItem.equals("Empty")) {
			String questionId = selectedItem.split("id=")[1].split(",")[0];
			questionService.deleteQuestion(questionId);
			
			clearQuestion();
			updateQuestionsList();
		}
	}

	public void updateQuestionsList() {
		questionList.removeAllItems();
		List<QuestionDTO> questionDTOList = questionService.findQuestions();
		if (questionDTOList == null || questionDTOList.isEmpty()) {
			questionList.addItem("Empty");
		} else {
			for (Object value : questionDTOList) {
				questionList.addItem(value.toString());
			}
		}
	}
	
	private void clearQuestion() {
		imageQuestionPanel.setImage("");
		imageAnswerPanel.setImage("");
		answerField1.setAnswer("");
		answerField2.setAnswer("");
		answerField3.setAnswer("");
		answerField4.setAnswer("");
	}
	
}
