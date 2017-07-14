package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public class Reports {

	public Reports() {

	}

	public Map<String, String> GetAllCountries(Connection conn) {
		Map<String, String> countries = new HashMap<String, String>();
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			String query = "SELECT code, name FROM countries";
			pst = conn.prepareStatement(query);

			rs = pst.executeQuery();

			countries.clear();
			while (rs.next()) {
				countries.put(rs.getString(1), rs.getString(2));
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
		return countries;
	}

	public List<String> CountAirports(Connection conn, Map<String, String> countries) {
		List<String> airCount = new ArrayList<String>();
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {

			String query = "SELECT iso_country, COUNT(iso_country) FROM airports GROUP BY iso_country ORDER BY COUNT(iso_country) DESC";
			pst = conn.prepareStatement(query);

			rs = pst.executeQuery();

			while (rs.next()) {
				String insertVal = countries.get(rs.getString(1)) + " " + rs.getString(2);
				airCount.add(insertVal);
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
		return airCount;
	}

	public Map<String, List<String>> RunwayTypes(Connection conn, Map<String, String> countries) {
		Map<String, List<String>> countryRun = new HashMap<String, List<String>>();
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			// for each country
			for (Map.Entry<String, String> code : countries.entrySet()) {
				String query = "SELECT DISTINCT surface FROM runways WHERE airport_ref IN (SELECT id FROM airports WHERE iso_country = '"
						+ code.getKey() + "')";
				pst = conn.prepareStatement(query);

				rs = pst.executeQuery();

				// collect all types of runways
				List<String> runTypes = new ArrayList<String>();
				while (rs.next()) {
					// collect types
					runTypes.add(rs.getString(1));
				}

				// for each country put list of types
				countryRun.put(code.getValue(), runTypes);

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
		return countryRun;
	}

	public List<String> TopRunways(Connection conn) {
		
		List<String> runCount = new ArrayList<String>();
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			String query = "SELECT le_ident, COUNT(le_ident) FROM runways GROUP BY le_ident ORDER BY COUNT(le_ident) DESC LIMIT 10";
			pst = conn.prepareStatement(query);

			rs = pst.executeQuery();

			while (rs.next()) 
			{
				runCount.add(rs.getString(1) + " " + rs.getString(2));
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
		return runCount;
	}
}
