package hijac.tools.tightrope.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

class BrowseAction implements ActionListener
	{
		private Component parent;
		private JTextField textField;
//		private String currentLoc;

		public BrowseAction(Component parent, JTextField textField)
		{
			this.parent = parent;
			this.textField = textField;
//			this.currentLoc = currentLoc;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			File currentLoc = currentLoc();
			JFileChooser chooser = new JFileChooser(currentLoc);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = chooser.showOpenDialog(parent);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				System.out.println("You chose to open this file: "
						+ chooser.getSelectedFile().getName());

				textField.setText(chooser.getCurrentDirectory().toString());
			}
		}
		
		public File currentLoc()
		{				
			return new File(textField.getText());
		}
	}