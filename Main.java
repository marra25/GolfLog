package golfpackage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * The Main class of GolfLog contains the basic setup for the application.
 * It contains the run method to run the program.
 * 
 * @author Bryce
 *
 */

public class Main {
	public static Connection conn;
	public Scanner in;
	public static void main(String args[])
			throws IOException, SQLException, ClassNotFoundException, InterruptedException
		{
		if (args.length == 0)
		{   
			System.out.println("Usage: java GolferScores propertiesFile");
			System.exit(0);
		}
	
		Main db = new Main(args[0]);
		Run.run(db);
	}
	
	public Main(String propfile) throws IOException, SQLException, ClassNotFoundException
	{
		SimpleDataSource.init(propfile);
		conn = SimpleDataSource.getConnection();
		in = new Scanner(System.in);
	} 
}
