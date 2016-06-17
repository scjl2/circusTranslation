package hijac.tools.tightrope.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JTextField;

class BrowseAction implements ActionListener
	{
		Component parent;
		JTextField textField;

		public BrowseAction(Component parent, JTextField textField)
		{
			this.parent = parent;
			this.textField = textField;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFileChooser chooser = new JFileChooser();
			int returnVal = chooser.showOpenDialog(parent);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				System.out.println("You chose to open this file: "
						+ chooser.getSelectedFile().getName());

				textField.setText(chooser.getSelectedFile().getName());
			}
		}
	}