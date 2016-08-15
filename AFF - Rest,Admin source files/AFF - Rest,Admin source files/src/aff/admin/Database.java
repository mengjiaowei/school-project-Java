package aff.admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

//this class contains methods for connecting to, updating and querying the database

public class Database {
	protected Connection connection = null;
	protected ResultSet rs = null;
	// our connector, used by various classes, exception handling helps out with any database problems
	public boolean connect()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://90.231.28.63:3306/curtana?" + "user=Magnus&" + "password=j3dd4EGMXRTA9Zvu"); 
		}
		catch(ClassNotFoundException cnfex)
		{
			cnfex.printStackTrace();
			System.out.println("Driver not found\n" + cnfex.toString());
			return false;
		}
		catch(SQLException sqlex)
		{
			sqlex.printStackTrace();
			System.out.println("Connection unsuccessful\n" + sqlex.toString());
			return false;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.out.println(ex.toString());
			return false;
		}
		
		return true;
	}
	
	protected void updateDb(String sqlString) throws SQLException
	{
		Statement st = connection.createStatement();
		st.executeUpdate(sqlString);
	}
	
	public ResultSet queryDb(String sqlString) throws SQLException
	{
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery(sqlString);
		return rs;
	}
	
	protected String updateAndgetKey(String sqlString) throws SQLException
	{
		String theKey = "";
		Statement st = connection.createStatement();
		st.executeUpdate(sqlString, Statement.RETURN_GENERATED_KEYS);
		rs = st.getGeneratedKeys();
		if (rs.next()){theKey = Integer.toString(rs.getInt(1));}
		return theKey;
	}
	
	//this is a custom tablemodel that is premade to handle ResultSets we get from the MySQL server
	//it also notifies itself when there has been changes to the table and updates the table accordingly
	public class QueryTableModel extends AbstractTableModel
	{
		Vector cache;
		int colCount;
		String[] colNames;
		
		public QueryTableModel()
		{
			cache = new Vector();
		}
		
		public String getColumnName(int i) {return colNames[i];}
		@Override
		public int getColumnCount() { return colCount; }
		@Override
		public int getRowCount() { return cache.size();	}
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			
			return ((String[])cache.elementAt(rowIndex))[columnIndex];
		}
		
		//this method takes a String, queries the DB and uses a vector to populate our tableModel
		//firetablechanged() makes sure the table in the GUI notices the change
		public void setQuery(String q)
		{
			cache = new Vector();
			try {
				ResultSet rs = queryDb(q);
				ResultSetMetaData rsmd = rs.getMetaData();
				colCount = rsmd.getColumnCount();
				colNames = new String[colCount];
				
				for (int i = 1; i <= colCount; i++)
				{
					colNames[i - 1] = rsmd.getColumnName(i); 
				}
				
				while (rs.next())
				{
					String[] row = new String[colCount];
					for (int i = 0; i < colCount; i++)
					{
						row[i] = rs.getString(i + 1);
					}
					cache.addElement(row);
				}
				fireTableDataChanged();
				
			}
			catch(Exception e)
			{
				cache = new Vector();
				e.printStackTrace();
			}
		}
	}

}
