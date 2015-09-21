package golfpackage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * The ByCourseListener class of GolfLog calls on the display holes method from the actions class.
 * 
 * @author Bryce
 *
 */

public class ByCourseListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			Actions.displayHoles();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

}
