package golfpackage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * The SearchListener class of GolfLog calls on the search method from the actions class.
 * 
 * @author Bryce
 *
 */

public class SearchListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			Actions.search();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

}
