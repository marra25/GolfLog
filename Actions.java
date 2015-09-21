package golfpackage;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


/**
 * The Actions class of GolfLog executes the actions requested by the user.
 * Initialization is required for the first time running the program.
 * The Initialization button is hidden.  Set to Visible within Run class to initialize.
 * 
 * @author Bryce
 *
 */

public class Actions {
	static String font = "Font";
	static ImageIcon taylormade = new ImageIcon("taylormade.png");
	static Image sized = taylormade.getImage().getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH);
	static ImageIcon newIcon = new ImageIcon(sized);
	public static void initializeDatabase() throws SQLException  {
		Statement stat = Main.conn.createStatement();
		try
		{
			try
			{
				stat.execute("DROP TABLE Golfers");
			}
			catch (SQLException e)
			{
				System.out.println("Notice: Exception during DROP TABLE Golfers: " + e.getMessage());
			}
			try
			{
				stat.execute("DROP TABLE Scores");
			}
			catch (SQLException e)
			{
				System.out.println("Notice: Exception during DROP TABLE Scores: " + e.getMessage());
			}
			// Golfers table: Golfer ID, Name
			stat.execute("CREATE TABLE Golfers (Golfer_ID INTEGER, Golfer_Name VARCHAR(64))");
			stat.execute("CREATE TABLE Scores (Golfer_ID INTEGER, Round_ID INTEGER, Date VARCHAR(10), Course_Name VARCHAR(64), Course_Par INTEGER, "
					+ "Hole_One DOUBLE, Hole_Two DOUBLE, Hole_Three DOUBLE, Hole_Four DOUBLE, Hole_Five DOUBLE, "
					+ "Hole_Six DOUBLE, Hole_Seven DOUBLE, Hole_Eight DOUBLE, Hole_Nine DOUBLE, Total DOUBLE)");
			// Add default list of golfers, courses, and scores
			final String[] golfers =
				{
					"1, 'Doe, John'"
				};
			for (String g : golfers)
			{
				stat.execute("INSERT INTO Golfers (Golfer_ID, Golfer_Name) VALUES (" + g + ")");
				System.out.println("Notice: Inserted golfer " + g);
			}
			final String[] scores =
				{
					"1, 1, '06/12/1992', 'DEFAULT COURSE NAME', 36, 1, 2, 3, 4, 5, 6, 7, 8, 9, 45"
				};
			for (String s : scores)
			{
				stat.execute("INSERT INTO Scores (Golfer_ID, Round_ID, Date, Course_Name, Course_Par, Hole_One, Hole_Two, Hole_Three, "
						+ "Hole_Four, Hole_Five, Hole_Six, Hole_Seven, Hole_Eight, Hole_Nine, Total) VALUES (" + s + ")");
				System.out.println("Notice: Inserted score " + s);
			}
		}
		finally
		{
			stat.close();
		}
	} // End of initializeDatabase method
	
	/**
	 * Display all the scores for a golfer.
	 */
	public static void displayAllScores() throws SQLException
	{
		String inputValue = (String) JOptionPane.showInputDialog(null, "Please Enter Golfer ID Number", "Display All Rounds", 2, newIcon, null, null);
		int gid = Integer.parseInt(inputValue);
		
		String name = getGolferName(gid);
		
		if (name == null)
		{
			JOptionPane.showMessageDialog(null, "Golfer ID does not exist", null, JOptionPane.CLOSED_OPTION);
			return;
		}

		// Prepare and execute the query to get the course name and score
		// from the Scores table for the specified golfer ID.
		PreparedStatement stat = Main.conn.prepareStatement("SELECT Round_ID, Date, Course_Name, Course_Par, Total FROM Scores WHERE Golfer_ID = ?");
		PreparedStatement stat2 = Main.conn.prepareStatement("SELECT Count(Round_ID) AS Rounds_Played, CAST(Avg(Total) AS DECIMAL (12,2)) AS Average FROM Scores WHERE Golfer_Id = ?");
		try
		{
			JFrame allScores = new JFrame("Scores for: " + name);
			allScores.setSize(1000, 1000);
			allScores.setLocation(200, 250);
			allScores.setVisible(false);
			stat.setInt(1, gid);
			stat2.setInt(1, gid);
			ResultSet result = stat.executeQuery();
			ResultSet result2 = stat2.executeQuery();
			JTable table = new JTable(buildTableModel(result));
			JTable table2 = new JTable(buildTableModel(result2));
			table.setShowGrid(false);
			table.setBackground(new java.awt.Color(8, 176, 8));
			table.setRowHeight(50);
			JScrollPane upperData = new JScrollPane(table);
			allScores.add(upperData, BorderLayout.CENTER);
			table2.setBackground(new java.awt.Color(8, 176, 8));
			table2.setFont(new Font(font, 60, 60));
			table2.setRowHeight(200);
			table2.setSize(1000, 200);
			JScrollPane bottomRow = new JScrollPane(table2);
			allScores.add(bottomRow, BorderLayout.PAGE_END);
			allScores.validate();
			allScores.setVisible(true);
		}
		finally
		{
			stat.close();
			stat2.close();
		}
	} // End of displayAllScores method
	
	
	public static DefaultTableModel buildTableModel(ResultSet rs)
			throws SQLException {
		 ResultSetMetaData metaData = rs.getMetaData();

		    // names of columns
		    Vector<String> columnNames = new Vector<String>();
		    int columnCount = metaData.getColumnCount();
		    for (int column = 1; column <= columnCount; column++) {
		        columnNames.add(metaData.getColumnName(column));
		    }

		    // data of the table
		    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		    while (rs.next()) {
		        Vector<Object> vector = new Vector<Object>();
		        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
		            vector.add(rs.getObject(columnIndex));
		        }
		        data.add(vector);
		        
		    }
		    return new DefaultTableModel(data, columnNames);
	}

	/**
    Add/modify a score.
	 */
	public static void addScore() throws SQLException
	{
		JFrame addScore = new JFrame("Add/Modify a Round -- Golf Tracker");
		addScore.setSize(1000, 1000);
		addScore.setVisible(false);
		addScore.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		String inputValue = (String) JOptionPane.showInputDialog(null, "Please Enter Golfer ID Number", "Add/Modify a Round", 2, newIcon, null, null);
		int gid = Integer.parseInt(inputValue);
	
		String name = getGolferName(gid);

		if (name == null)
		{
			JOptionPane.showMessageDialog(null, "Unknown Golfer ID", null, JOptionPane.CLOSED_OPTION);
		}
		
		String roundNumber = (String) JOptionPane.showInputDialog(null, "Please Enter Round Number", "Display All Rounds", 2, newIcon, null, null);
		int rid = Integer.parseInt(roundNumber);
		
		String date = JOptionPane.showInputDialog("Please Enter Date as xx/xx/xxxx");
		
		String course_Name = JOptionPane.showInputDialog("Enter Course Name").toUpperCase();

		String coursePar = JOptionPane.showInputDialog("Enter Course Par");
		int par = Integer.parseInt(coursePar);
		
		String oneString = JOptionPane.showInputDialog("Hole One:");
		double one = Double.parseDouble(oneString);
		
		String twoString = JOptionPane.showInputDialog("Hole Two:");
		double two = Double.parseDouble(twoString);
		
		String threeString = JOptionPane.showInputDialog("Hole Three:");
		double three = Double.parseDouble(threeString);
		
		String fourString = JOptionPane.showInputDialog("Hole Four:");
		double four = Double.parseDouble(fourString);
		
		String fiveString = JOptionPane.showInputDialog("Hole Five:");
		double five = Double.parseDouble(fiveString);
		
		String sixString = JOptionPane.showInputDialog("Hole Six:");
		double six = Double.parseDouble(sixString);
		
		String sevenString = JOptionPane.showInputDialog("Hole Seven:");
		double seven = Double.parseDouble(sevenString);
		
		String eightString = JOptionPane.showInputDialog("Hole Eight:");
		double eight = Double.parseDouble(eightString);
		
		String nineString = JOptionPane.showInputDialog("Hole Nine:");
		double nine = Double.parseDouble(nineString);
		
	
		double total = one + two + three + four + five + six + seven + eight + nine;
	

		PreparedStatement stat = Main.conn.prepareStatement(
			"UPDATE Scores SET Course_Par = ?, Date = ?, Course_Name = ?, Hole_One = ?, Hole_Two = ?, Hole_Three = ?, Hole_Four = ?, Hole_Five = ?, "
						+ "Hole_Six = ?, Hole_Seven = ?, Hole_Eight = ?, Hole_Nine = ?, Total = ? "
						+ "WHERE Golfer_ID = ? AND Round_ID = ?");
		
		stat.setInt(1, par);
		stat.setString(2, date);
		stat.setString(3, course_Name);
		stat.setDouble(4, one);
		stat.setDouble(5, two);
		stat.setDouble(6, three);
		stat.setDouble(7, four);
		stat.setDouble(8, five);
		stat.setDouble(9, six);
		stat.setDouble(10, seven);
		stat.setDouble(11, eight);
		stat.setDouble(12, nine);
		stat.setDouble(13, total);
		stat.setInt(14, gid);
		stat.setInt(15, rid);
	
		if (stat.executeUpdate() == 1)
			JOptionPane.showMessageDialog(null, "Score Modified", null, JOptionPane.CLOSED_OPTION);
		else
		{
			stat = Main.conn.prepareStatement(
				"INSERT INTO Scores VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			stat.setInt(1, gid);
			stat.setInt(2, rid);
			stat.setString(3, date);
			stat.setString(4, course_Name);
			stat.setInt(5, par);
			stat.setDouble(6, one);
			stat.setDouble(7, two);
			stat.setDouble(8, three);
			stat.setDouble(9, four);
			stat.setDouble(10, five);
			stat.setDouble(11, six);
			stat.setDouble(12, seven);
			stat.setDouble(13, eight);
			stat.setDouble(14, nine);
			stat.setDouble(15, total);
			
			stat.executeUpdate();
			JOptionPane.showMessageDialog(null, "Score Added", null, JOptionPane.CLOSED_OPTION);
		}
	} // End of addScore method

	/**
	 * Add/modify a Golfer.
	 */
	public static void addGolfer() throws SQLException
	{
		boolean insert = false;
		JFrame addScore = new JFrame("Add/Modify a Golfer -- Golf Tracker");
		addScore.setSize(1000, 1000);
		addScore.setVisible(false);
		addScore.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		String inputValue = (String) JOptionPane.showInputDialog(null, "Please Enter Golfer ID Number", "Add/Modify a Golfer", 2, newIcon, null, null);
		int gid = Integer.parseInt(inputValue);
	
		String name = getGolferName(gid);
		String newName = "s";
		
		if (name == null)
		{
			insert = true;
		}
		
		if (!insert){
			newName = ((String) JOptionPane.showInputDialog(null, "Enter Golfer Name (Was \"" + name + "\")", "Modify Golfer", 2, newIcon, null, null)).toUpperCase();
		}
		else if (insert)
			newName = ((String) JOptionPane.showInputDialog(null, "Enter Golfer Name: ", "Add Golfer", 2, newIcon, null, null)).toUpperCase();

		if (!insert)
		{
			PreparedStatement stat = Main.conn.prepareStatement(
					"UPDATE Golfers SET Golfer_Name = ? WHERE Golfer_ID = ?");
			stat.setString(1, newName);
			stat.setInt(2, gid);
			if (stat.executeUpdate() == 1)
			{
				JOptionPane.showMessageDialog(null, "Golfer Modified");
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Golfer Was Not Modified");
			}
		}
		else /* Insert */
		{
			PreparedStatement stat = Main.conn.prepareStatement(
					"INSERT INTO Golfers VALUES(?, ?)");
			stat.setInt(1, gid);
			stat.setString(2, newName);
			stat.executeUpdate();
			JOptionPane.showMessageDialog(null, "Golfer Added");
		}
	} // End of addGolfer method

	/**
	      Gets the name of a golfer in the database.
	      @param id the golfer ID
	      @return the name, or null if there is no golfer with the given ID
	 */
	public static String getGolferName(int id) throws SQLException
	{
		PreparedStatement stat = Main.conn.prepareStatement(
				"SELECT Golfer_Name FROM Golfers WHERE Golfer_ID = ?");
		stat.setInt(1, id);
		ResultSet result = stat.executeQuery();
		if (result.next())
			return result.getString(1).trim();
		else
			return null;
	} // End of getGolferName method

	/**
    Gets the name of a golfer in the database.
    @param id the golfer ID
    @return the name, or null if there is no golfer with the given ID
	 */
	public static int getGolferID(String name) throws SQLException
	{
		PreparedStatement stat = Main.conn.prepareStatement(
				"SELECT Golfer_ID FROM Golfers WHERE Golfer_Name = ?");
		stat.setString(1, name);
		ResultSet result = stat.executeQuery();
		if (result.next())
			return result.getInt(1);
		else
			return 0000;
	} // End of getGolferName method
	
	public static void displayHoles() throws SQLException
	{
		String inputValue = (String) JOptionPane.showInputDialog(null, "Please Enter Golfer ID Number", "Display Rounds by Course", 2, newIcon, null, null);
		int gid = Integer.parseInt(inputValue);
		
		String name = getGolferName(gid);
		
		if (name == null)
		{
			JOptionPane.showMessageDialog(null, "Unknown Golfer ID", null, JOptionPane.CLOSED_OPTION);
			return;
		}
		
		String c_Name = ((String) JOptionPane.showInputDialog(null, "Please Enter Course Name", "Display Rounds by Course", 2, newIcon, null, null)).toUpperCase();
		
		// Prepare and execute the query to get the course name and score
		// from the Scores table for the specified golfer ID.
		PreparedStatement stat = Main.conn.prepareStatement("SELECT Round_ID, Date, Course_Par, Hole_One, Hole_Two, "
				+ "Hole_Three, Hole_Four, Hole_Five, Hole_Six, Hole_Seven, Hole_Eight, Hole_Nine, Total "
				+ "FROM Scores WHERE Golfer_ID = ? AND Course_Name = ?");
		PreparedStatement stat2 = Main.conn.prepareStatement("SELECT Count(Round_ID) as Rounds, Avg(Hole_One) as One, "
				+ "Avg(Hole_Two) as Two, Avg(Hole_Three) as Three, Avg(Hole_Four) as Four, Avg(Hole_Five) as Five, "
				+ "Avg(Hole_Six) as Six, Avg(Hole_Seven) as Seven, Avg(Hole_Eight) as Eight, Avg(Hole_Nine) as Nine, "
				+ "Avg(Total) as Total FROM Scores WHERE Golfer_ID = ? AND Course_Name = ?");
		try
		{
			JFrame displayScores = new JFrame(name + ": @" + c_Name);
			displayScores.setSize(1000, 1000);
			displayScores.setLocation(200, 250);
			displayScores.setVisible(false);
			stat.setInt(1, gid);
			stat.setString(2, c_Name);
			stat2.setInt(1, gid);
			stat2.setString(2, c_Name);
			ResultSet result = stat.executeQuery();
			ResultSet result2 = stat2.executeQuery();
			JTable table = new JTable(buildTableModel(result));
			JTable table2 = new JTable(buildTableModel(result2));
			table.setShowGrid(false);
			table.setBackground(new java.awt.Color(8, 176, 8));
			table.setRowHeight(50);
			JScrollPane upperData = new JScrollPane(table);
			displayScores.add(upperData, BorderLayout.CENTER);
			table2.setBackground(new java.awt.Color(8, 176, 8));
			table2.setRowHeight(200);
			table2.setSize(1250, 100);
			table2.setFont(new Font(font, 60, 30));
			JScrollPane bottomRow = new JScrollPane(table2);
			displayScores.add(bottomRow, BorderLayout.PAGE_END);
			displayScores.validate();
			displayScores.setVisible(true);
		}
		finally{
			stat.close();
			stat2.close();
		}
	}

	public static void search() throws SQLException
	{
		String name = ((String) JOptionPane.showInputDialog(null, "Enter Golfer Name (Doe, John):", "Search For Golfer ID", 2, newIcon, null, null)).toUpperCase();
		int gid = 0;
		gid = getGolferID(name);
		if (gid == 0){
			JOptionPane.showMessageDialog(null, "Unknown Golfer", null, 2, newIcon);
		}
		else{
			JOptionPane.showMessageDialog(null, "Golfer ID: " + gid, name, 2, newIcon);
		}
}
	
	public static void displayAllGolfers() throws SQLException
	{
		JFrame displayGolfers = new JFrame("Display All Golfers -- Golf Tracker");
		displayGolfers.setSize(1250, 1000);
		displayGolfers.setVisible(false);
		
		PreparedStatement stat = Main.conn.prepareStatement("SELECT * FROM Golfers");

		try
		{
			ResultSet result = stat.executeQuery();
			JTable table = new JTable(buildTableModel(result));
			table.setShowGrid(true);
			table.setBackground(new java.awt.Color(8, 176, 8));
			table.setRowHeight(50);
			JScrollPane leftData = new JScrollPane(table);
			displayGolfers.add(leftData, BorderLayout.WEST);
			displayGolfers.pack();
			displayGolfers.validate();
			displayGolfers.setVisible(true);
		}
		finally{
			stat.close();
		}
	}
}
