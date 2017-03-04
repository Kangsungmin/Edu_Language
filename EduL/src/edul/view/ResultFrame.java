package edul.view;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ResultFrame extends JFrame{
	private JLabel jlabel;
	private JTextArea textarea;
	private JScrollPane scrollpane;
	public ResultFrame(String result){
		super("Result print");
		setLayout(new FlowLayout());
		
		textarea = new JTextArea("",25,45);
		textarea.setText(result);
		textarea.setEditable(false);
		scrollpane = new JScrollPane(textarea);
		add(scrollpane);
	}
}
