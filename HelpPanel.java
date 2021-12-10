import java.awt.*;
import java.awt.event.*;
import java.io.*;

// swing:
import javax.swing.*;

// watsonnet

public class HelpPanel extends JFrame {

	public SudokuMainframe parent = null;
	
	// constructor
	public HelpPanel() {
		
		JPanel panelMain = new JPanel(new BorderLayout());
	
		JButton readyButton = new JButton("Ready");

		try {
			JEditorPane je = new JEditorPane (getClass().getResource("/help.html"));

			JScrollPane scrollHelpText = new JScrollPane (je);

			panelMain.add (scrollHelpText, BorderLayout.CENTER);

		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, "HelpText file not found.", "Read error", JOptionPane.WARNING_MESSAGE);
		} 
		
	
		// Create elements
		/*
		+---------------------------+
		| helpText               |
		+---------------------------+
		| readyButton          |
		+---------------------------+
		*/
		
		// ---------------------------------------------------------------------
		// Main content panel
		// ---------------------------------------------------------------------
		getContentPane().add(panelMain);
		// ---------------------------------------------------------------------
		
		// ---------------------------------------------------------------------
		// Title and icon
		// ---------------------------------------------------------------------
		setTitle("Help");
		// setIconImage(new ImageIcon(getClass().getResource("/images/icon.gif")).getImage());
		// ---------------------------------------------------------------------
		
		// ---------------------------------------------------------------------
		// Keywords/search button/location
		// ---------------------------------------------------------------------
		// Labels
		JPanel panelReadyButton = new JPanel();
		panelReadyButton.add(readyButton);
		
		readyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				closeWindow ();
			}
		});
		
		panelMain.add (panelReadyButton, BorderLayout.SOUTH);
		
		// ---------------------------------------------------------------------
		// Add window listener to trap closing event
		// ---------------------------------------------------------------------
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeWindow();
			}
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowOpened(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
		});
		// ---------------------------------------------------------------------
		
		// ---------------------------------------------------------------------
		// Make frame visible
		// ---------------------------------------------------------------------
		panelMain.validate();
		setVisible(true);
		// ---------------------------------------------------------------------
	}
	
	// Close window
	public void closeWindow() {
		// So we know to create a new search panel next time
		parent.helpPanelVisible = false;
		
		// Close window
		dispose();
	}
}

