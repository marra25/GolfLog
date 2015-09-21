package golfpackage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * The AddRoundListener class of GolfLog calls on the add score method from the actions class.
 * 
 * @author Bryce
 *
 */

public class AddRoundListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			Actions.addScore();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

}
