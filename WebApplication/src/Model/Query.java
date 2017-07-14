package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author macbook
 *
 */
public class Query {

	private List<String> countries;
	private Map<String, Integer> airRun;

	public Query() {
		countries = new ArrayList<String>();
		airRun = new HashMap<String, Integer>();
	}

	public Map<String, Integer> GetAirports(Connection conn, String countryCode)
	{
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try 
		{
			String query = "SELECT id, ident, name FROM airports WHERE iso_country = '" + countryCode +"'";
			pst = conn.prepareStatement(query);
		
			rs = pst.executeQuery();

			while (rs.next()) {
				// get ready data to print: ident, name, runways count
				airRun.put(rs.getString(2)+" "+rs.getString(3), CountRunways(conn, rs.getString(1)));
				System.out.println(rs.getString(2)+" "+rs.getString(3));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			

		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
				
			}
		}
		return airRun;
	}
	
	public Integer CountRunways(Connection conn, String airCode)
	{
		int runwaysCount = 0;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try 
		{
			String query = "SELECT COUNT(airport_ref) FROM runways WHERE airport_ref = '" + airCode +"'";
			pst = conn.prepareStatement(query);
		
			rs = pst.executeQuery();

			//
			rs.next();
			runwaysCount = Integer.parseInt(rs.getString(1));
			

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			

		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
				
			}
		}
		return runwaysCount;
	}
	
public List<String> SelectCountry(Connection conn, String name) {
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<String> export = new ArrayList<String>();
		
		try {
			
			//check for code or name
			if(name.length() > 2)
			{
				String query = "SELECT code, name FROM countries WHERE LOWER(name) LIKE '%" + name.toLowerCase() +"%'";
				pst = conn.prepareStatement(query);
				
			}else if(name.length() == 2)
			{
				String query = "SELECT code, name FROM countries WHERE code = '" + name.toUpperCase() +"'";
				pst = conn.prepareStatement(query);
			}

			rs = pst.executeQuery();

			while (rs.next()) {
				export.add(rs.getString(1)+","+rs.getString(2));
				System.out.println(rs.getString(2));
			}
			

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			

		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
				
			}
		}
		return export;
	}

}



