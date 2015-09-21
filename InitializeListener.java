package golfpackage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * The InitializeListener class of GolfLog calls on the initialize database method from the actions class.
 * 
 * @author Bryce
 *
 */

public class InitializeListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			Actions.initializeDatabase();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
}
