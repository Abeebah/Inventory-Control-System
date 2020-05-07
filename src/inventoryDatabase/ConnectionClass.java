package inventoryDatabase;
import java.sql.*;
import javax.swing.*;


public class ConnectionClass 
{
	public static Connection dbconnect()
	{
		try
		{   
			Class.forName("oracle.jdbc.driver.OracleDriver");  
			
			//Connection conn = DriverManager.getConnection("jdbc:oracle:thin@localhost:1521:orcl","abibat","abibat");
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","abibat","abibat");
			//JOptionPane.showMessageDialog(null, "Connection Success");
			return conn;
			
			
		}catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, e);
			return null;
		}
		
		
	}

}



