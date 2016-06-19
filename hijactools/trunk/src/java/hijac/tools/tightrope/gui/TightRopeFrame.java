package hijac.tools.tightrope.gui;

import hijac.tools.application.TightRope;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class TightRopeFrame extends JFrame
{
	/**
	 * Default Serial Id
	 */
	private static final long serialVersionUID = -5887203895415977539L;
	
	private final JTextField rtsjLib = new JTextField();
	private final JTextField scjLib = new JTextField();
	private final JTextField programSource = new JTextField();
	

	public TightRopeFrame()
	{
		// ***************************
		// Create And Set Up Window
		// ***************************
		super("Tight Rope - SCJ to Circus translator " + TightRope.getVersion());

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 500);
		setLayout(new BorderLayout());

		// ***************************
		// Create Components
		// ***************************

		JButton generateButton = new JButton("Generate");

		generateButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				TightRopeFrame.this.generate();
			}

		});

		GridLayout settingsGrid = new GridLayout();
		settingsGrid.setRows(3);
		settingsGrid.setColumns(3);

		JPanel settingsPanel = new JPanel(settingsGrid);
		settingsPanel.add(new JLabel("RTSJ Library:", SwingConstants.RIGHT));
		
		settingsPanel.add(rtsjLib);
		JButton rtsjBrowse = new JButton("Browse");
		rtsjBrowse.addActionListener(new BrowseAction(this, rtsjLib));
		settingsPanel.add(rtsjBrowse);

		settingsPanel.add(new JLabel("SCJ Library:", SwingConstants.RIGHT));
		
		settingsPanel.add(scjLib);
		JButton scjBrowse = new JButton("Browse");
		scjBrowse.addActionListener(new BrowseAction(this, scjLib));
		settingsPanel.add(scjBrowse);

		settingsPanel.add(new JLabel("Program Source:", SwingConstants.RIGHT));
		
		settingsPanel.add(programSource);
		JButton programBrowse = new JButton("Browse");
		programBrowse.addActionListener(new BrowseAction(this, programSource));
		settingsPanel.add(programBrowse);

//		populateTextFeilds("rtsj Location","scj Location","program Location");

		// ***************************
		// Add Components To Window
		// ***************************
		// frame.add(new JLabel(), BorderLayout.NORTH);
		this.add(generateButton, BorderLayout.EAST);
		this.add(settingsPanel, BorderLayout.CENTER);
		// frame.add(new JLabel("WEST"), BorderLayout.WEST);

		// frame.add(new
		// JLabel("Matt Luckcuck - University of York, UK - 2016"),
		// BorderLayout.SOUTH);
		// frame.getContentPane().add(label);

		// ***************************
		// Display the window.
		// ***************************

		// TightRope.main(new String[] {});
	}

	protected void generate()
	{

		JOptionPane.showMessageDialog(TightRopeFrame.this, "GENERATE");

	}

	public void populateTextFeilds(String rtsjLibText, String scjLibText,
			String programSourceText)
	{
		rtsjLib.setText(rtsjLibText);

		scjLib.setText(scjLibText);

		programSource.setText(programSourceText);

	}

	public void displayFrame()
	{
		pack();
		setVisible(true);
	}
	
	public String getRtsjLibText()
	{
		return rtsjLib.getText();
	}
	
	public String getScjLibText()
	{
		return scjLib.getText();
	}
	
	public String getProgramSourceText()
	{
		return programSource.getText();
	}
}