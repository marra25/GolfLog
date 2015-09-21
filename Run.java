package golfpackage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

/**
 * The Run class of GolfLog creates the main frame and adds the logo, background,
 * and buttons to the frame.
 * 
 * @author Bryce
 *
 */

public class Run {

	public static void run(Main args) throws SQLException, InterruptedException
	{
		JFrame main = new JFrame("GolfTracker");
		main.setSize(1200, 580);
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel label1 = new JLabel();
		label1.setSize(210, 250);
		label1.setLocation(10, 80);
		
		
		JLabel im = new JLabel(new ImageIcon("golflog.jpg"));
		im.setSize(960, 550);
		im.setLocation(220, 0);
		
		JLabel logo = new JLabel(new ImageIcon("logo.png"));
		logo.setSize(210, 150);
		logo.setLocation(5, 25);
		
		main.add(im);
		main.add(logo);
		main.add(label1);
		
		
		main.setVisible(true);
		label1.setVisible(true);
		im.setVisible(true);
		logo.setVisible(true);
		
		for (int i=0; i<8; i++){
			JButton btn = new JButton();
			btn.setSize(200,40);
			if (i == 0){
				btn.setText("Initialize");
				btn.addActionListener(new InitializeListener());
				btn.setLocation(10, 5);
				label1.add(btn);
				btn.setVisible(false);
				}
			if (i == 1){
				btn.setText("Display All Rounds");
				btn.addActionListener(new AllScoresListener());
				btn.setLocation(10, 170);
				label1.add(btn);
				}
			if (i == 2){
				btn.setText("Add/Modify Round");
				btn.addActionListener(new AddRoundListener());
				btn.setLocation(10, 220);
				label1.add(btn);
			}
			if (i == 3){
				btn.setText("Add/Modify Golfer");
				btn.addActionListener(new AddGolferListener());
				btn.setLocation(10, 270);
				label1.add(btn);
			}
			if (i == 4){
				btn.setText("Display Rounds by Course");
				btn.addActionListener(new ByCourseListener());
				btn.setLocation(10, 320);
				label1.add(btn);
			}
			if (i == 5){
				btn.setText("View Golfers");
				btn.addActionListener(new AllGolfersListener());
				btn.setLocation(10, 370);
				label1.add(btn);
			}
			if (i == 6){
				btn.setText("Search");
				btn.addActionListener(new SearchListener());
				btn.setLocation(10, 420);
				label1.add(btn);
			}
			if (i == 7){
				btn.setText("Quit");
				btn.addActionListener(new QuitListener());
				btn.setLocation(10, 470);
				label1.add(btn);
			}
			label1.repaint();
			label1.revalidate();

		}
	} // End of run method
}
