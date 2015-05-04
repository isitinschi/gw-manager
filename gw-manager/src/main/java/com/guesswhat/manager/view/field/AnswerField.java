package com.guesswhat.manager.view.field;

import java.awt.FlowLayout;

import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

@SuppressWarnings("serial")
public class AnswerField extends JRadioButton {
	
	private JTextField textFiled = null;

	public AnswerField() {
		textFiled = new JTextField("", 10);
		setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
		
		textFiled.setDocument(new JTextFieldLimit(20));
		
		add(textFiled);
	}

	public String getAnswer() {
		return textFiled.getText();
	}

	public void setAnswer(String text) {
		textFiled.setText(text);
	}

	private class JTextFieldLimit extends PlainDocument {
		private int limit;

		JTextFieldLimit(int limit) {
			super();
			this.limit = limit;
		}

		public void insertString(int offset, String str, AttributeSet attr)
				throws BadLocationException {
			if (str == null) {
				return;
			}

			if ((getLength() + str.length()) <= limit) {
				super.insertString(offset, str, attr);
			}
		}
	}
}
