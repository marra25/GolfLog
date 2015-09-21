package golfpackage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * The AddGolferListener class of GolfLog calls on the add golfer method from the actions class.
 * 
 * @author Bryce
 *
 */

public class AddGolferListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			Actions.addGolfer();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

}
