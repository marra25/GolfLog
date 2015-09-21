package golfpackage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JOptionPane;

/**
 * The QuitListener class of GolfLog closes the program.
 * 
 * @author Bryce
 *
 */

public class QuitListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(null, "Thank You for using GolfTracker", null, JOptionPane.CLOSED_OPTION);
		System.exit(0);
		try {
			Main.conn.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

}
