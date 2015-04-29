package com.guesswhat.manager.view.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel implements MouseListener {

	private JFileChooser chooser = null;

	private String imagePath = null;
	private Image img = null;

	public ImagePanel() {
		setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
		setPreferredSize(new Dimension(640, 480));
		
		chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG file", "png");
		chooser.setFileFilter(filter);
		
		addMouseListener(this);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (img != null) {
			g.drawImage(img, 0, 0, null);
		}
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImage(String imagePath) {
		if (imagePath == null || imagePath.equals("")) {
			clearImage();
			return;
		}
		
		this.imagePath = imagePath;
		try {
			img = ImageIO.read(new File(imagePath));
		} catch (IOException e) {
			e.printStackTrace();
			clearImage();
			return;
		}
		fitPanelSize(new Dimension(img.getWidth(null), img.getHeight(null)));
	}
	
	public void setImage(BufferedImage image) {
		img = image;
		if (img != null) {
			fitPanelSize(new Dimension(img.getWidth(null), img.getHeight(null)));
		}
	}
	
	private void clearImage() {
		img = null;
		fitPanelSize(new Dimension(640, 480));
		repaint();
	}
	
	private void fitPanelSize(Dimension size) {
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			setImage(chooser.getSelectedFile().getAbsolutePath());
		}
	}

	public Image getImage() {
		return img;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	
}
