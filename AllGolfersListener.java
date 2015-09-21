package golfpackage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * The AllGolfersListener class of GolfLog calls on the display all golfers method from the actions class.
 * 
 * @author Bryce
 *
 */

public class AllGolfersListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			Actions.displayAllGolfers();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

}
