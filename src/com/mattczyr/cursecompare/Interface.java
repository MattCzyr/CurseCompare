package com.mattczyr.cursecompare;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class Interface {

	private static Frame frame;

	public static OperatorThread operatorThread;

	public static String status;

	public static void openInterface() {
		frame = new Frame();
		setStatus("Idle");
	}

	public static void output(String s, int index) {
		if (frame != null) {
			if (index == 0) {
				String currText = frame.outputArea0.getText();
				frame.outputArea0.setText(currText.isEmpty() ? s : (currText + "\n" + s));
			} else if (index == 1) {
				String currText = frame.outputArea1.getText();
				frame.outputArea1.setText(currText.isEmpty() ? s : (currText + "\n" + s));
			} else if (index == 2) {
				String currText = frame.outputArea2.getText();
				frame.outputArea2.setText(currText.isEmpty() ? s : (currText + "\n" + s));
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void stop(boolean kill) {
		if (operatorThread != null && operatorThread.isAlive() && kill) {
			operatorThread.interrupt();
			operatorThread.stop();
			operatorThread = null;
		}

		if (frame != null) {
			frame.compareButton.setText("Compare");
		}

		CurseCompare.working = false;
	}

	public static void error() {
		stop(false);
	}

	public static void setStatus(String newStatus) {
		status = newStatus;
		updateLabel();
	}

	public static void setNames(String name1, String name2) {
		if (frame != null) {
			frame.setTitle("Curse Compare - " + name1 + ", " + name2);
		}
	}

	public static void setNum(int num, int index) {
		if (frame != null) {
			if (index == 0) {
				frame.outputLabel0.setText("Both contain " + String.valueOf(num) + " mods...");
			} else if (index == 1) {
				frame.outputLabel1.setText("#1 contains " + String.valueOf(num) + " unique mods...");
			} else if (index == 2) {
				frame.outputLabel2.setText("#2 contains " + String.valueOf(num) + " unique mods...");
			}
		}
	}

	private static void updateLabel() {
		if (frame != null) {
			frame.currentStatus.setText(status);
		}
	}

	private static void reset() {
		if (frame != null) {
			frame.outputArea0.setText("");
			frame.outputArea1.setText("");
			frame.outputArea2.setText("");

			frame.outputLabel0.setText("Both contain...");
			frame.outputLabel1.setText("#1 contains...");
			frame.outputLabel2.setText("#2 contains...");

			frame.setTitle("Curse Compare");
		}
	}

	static final class Frame extends JFrame implements ActionListener, KeyListener {

		private static final long serialVersionUID = 0L;

		JPanel panel;
		JPanel urlPanel;
		JPanel urlPanel0;
		JPanel urlPanel1;
		JPanel outputPanel;
		JPanel outputPanel0;
		JPanel outputPanel1;
		JPanel outputPanel2;
		JPanel statusPanel;
		JScrollPane scrollPane0;
		JScrollPane scrollPane1;
		JScrollPane scrollPane2;
		JTextField urlField0;
		JTextField urlField1;
		JTextArea outputArea0;
		JTextArea outputArea1;
		JTextArea outputArea2;
		JLabel urlLabel0;
		JLabel urlLabel1;
		JLabel outputLabel0;
		JLabel outputLabel1;
		JLabel outputLabel2;
		JLabel currentStatus;
		JButton compareButton;

		public Frame() {
			setSize(800, 640);
			setTitle("Curse Compare");

			panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
			
			urlPanel = new JPanel();
			urlPanel.setLayout(new BoxLayout(urlPanel, BoxLayout.LINE_AXIS));
			urlPanel.setMaximumSize(new Dimension(1000, 100));
			
			urlPanel0 = new JPanel();
			urlPanel0.setLayout(new BoxLayout(urlPanel0, BoxLayout.PAGE_AXIS));
			
			urlPanel1 = new JPanel();
			urlPanel1.setLayout(new BoxLayout(urlPanel1, BoxLayout.PAGE_AXIS));
			
			outputPanel = new JPanel();
			outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.LINE_AXIS));
			
			outputPanel0 = new JPanel();
			outputPanel0.setLayout(new BoxLayout(outputPanel0, BoxLayout.PAGE_AXIS));
			
			outputPanel1 = new JPanel();
			outputPanel1.setLayout(new BoxLayout(outputPanel1, BoxLayout.PAGE_AXIS));
			
			outputPanel2 = new JPanel();
			outputPanel2.setLayout(new BoxLayout(outputPanel2, BoxLayout.PAGE_AXIS));
			
			statusPanel = new JPanel();
			statusPanel.setLayout(new BorderLayout());
			statusPanel.setMaximumSize(new Dimension(1000, 200));
			
			urlLabel0 = new JLabel("Modpack URL #1:");
			urlLabel1 = new JLabel("Modpack URL #2:");
			outputLabel0 = new JLabel("Both contain...");
			outputLabel1 = new JLabel("#1 contains...");
			outputLabel2 = new JLabel("#2 contains...");
			currentStatus = new JLabel("");
			
			urlField0 = new JTextField("", 54);
			urlField1 = new JTextField("", 54);
			
			compareButton = new JButton("Compare");
			compareButton.setAlignmentX(CENTER_ALIGNMENT);

			outputArea0 = new JTextArea();
			outputArea0.setFont(new JLabel().getFont());
			outputArea0.setEditable(false);

			outputArea1 = new JTextArea();
			outputArea1.setFont(new JLabel().getFont());
			outputArea1.setEditable(false);

			outputArea2 = new JTextArea();
			outputArea2.setFont(new JLabel().getFont());
			outputArea2.setEditable(false);

			outputPanel0.add(outputLabel0);
			outputPanel1.add(outputLabel1);
			outputPanel2.add(outputLabel2);

			scrollPane0 = new JScrollPane(outputArea0);
			scrollPane1 = new JScrollPane(outputArea1);
			scrollPane2 = new JScrollPane(outputArea2);

			outputPanel0.add(scrollPane0);
			outputPanel1.add(scrollPane1);
			outputPanel2.add(scrollPane2);

			Border standardBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
			outputPanel0.setBorder(standardBorder);
			outputPanel1.setBorder(standardBorder);
			outputPanel2.setBorder(standardBorder);

			urlPanel0.setBorder(standardBorder);
			urlPanel1.setBorder(standardBorder);

			statusPanel.setBorder(standardBorder);

			urlPanel0.add(urlLabel0);
			urlPanel0.add(urlField0);
			urlPanel1.add(urlLabel1);
			urlPanel1.add(urlField1);
			urlPanel.add(urlPanel0);
			urlPanel.add(urlPanel1);
			outputPanel.add(outputPanel0);
			outputPanel.add(outputPanel1);
			outputPanel.add(outputPanel2);
			statusPanel.add(currentStatus);
			panel.add(urlPanel);
			panel.add(outputPanel);
			panel.add(compareButton);
			panel.add(statusPanel);
			add(panel);

			compareButton.requestFocus();
			compareButton.addActionListener(this);
			urlField0.addKeyListener(this);
			urlField1.addKeyListener(this);

			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					System.exit(1);
				}
			});

			setResizable(true);
			setVisible(true);
		}

		@Override
		public void keyTyped(KeyEvent e) {
			if (e.getKeyChar() == '\n') {
				actionPerformed(null);
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e != null && e.getSource() == compareButton) {
				if (CurseCompare.working && operatorThread != null) {
					Interface.stop(true);
					Interface.setStatus("Stopped");
				} else if (!CurseCompare.working) {
					operatorThread = new OperatorThread(urlField0.getText(), urlField1.getText());
					((JButton) e.getSource()).setText("Stop");
					reset();
				}
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}
	}

}
