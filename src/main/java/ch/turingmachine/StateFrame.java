package ch.turingmachine;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class StateFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private String machine;
	private DisplayImage panel;
	private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	
	public StateFrame(String machine, String state) {
		this.machine = machine;
		
		String path = "./img/" + machine + "/0.png";
		
		panel = new DisplayImage(path);
		this.add(panel);
		this.setSize(640, 240);
		this.setLocation(dim.width/2 - getWidth()/2, (int) (dim.height*0.8 - getHeight()/2));
		this.setUndecorated(true);
		this.setFocusableWindowState(false);
	}
	
	public void setStateImage(String state) {
		String path = "./img/" + machine + "/" + state + ".png";
		panel.setImage(path);
	}
}
