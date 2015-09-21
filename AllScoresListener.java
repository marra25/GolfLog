package golfpackage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * The AllScoresListener class of GolfLog calls on the display all scores method from the actions class.
 * 
 * @author Bryce
 *
 */

public class AllScoresListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			Actions.displayAllScores();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

}
