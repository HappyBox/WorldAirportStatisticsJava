package Control;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Model.DBconn;
import Model.Query;
import Model.Reports;

/**
 * Servlet implementation class MainCtr
 */
@WebServlet("/MainCtr")
public class MainCtr extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainCtr() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		//filtering submitted forms
		if(request.getParameter("name").equals("Query"))
		{
			if(request.getParameter("country") != "")
			{
				
				//filter for illegal input
				String name = Filter(request.getParameter("country"));

				//Open db connection for future task
				DBconn db = new DBconn();
				Connection conn = db.Open();
				
				//if connection established
				if(conn != null)
				{
					//start query tasks
					Query query = new Query();
					List<String> countries = query.SelectCountry(conn, name);
					
					//check how many countries match, in case of 1 continue
					if (countries.size() == 1)
					{
						//get airports and count of runways
						String[] splited = countries.get(0).split(",");
						Map<String, Integer> airRail = query.GetAirports(conn, splited[0]);
						
						//print results 
						for(Map.Entry<String, Integer> part : airRail.entrySet())
						{
							response.getWriter().println(part.getKey() + " " + part.getValue() + " runways");
						}
					}
					else if (countries.size() > 1)
					{
						response.getWriter().println("Too many countries mach given input.");
						response.getWriter().println("Did you thought about one of these?");
						response.getWriter().println();
						for(String code : countries) 
						{
							code = code.replaceAll(",", " ");
							response.getWriter().println(code);
						}
					}
					else
					{
						response.getWriter().println("Sorry, no countries found maching your input...");
					}
				}
				else
					response.getWriter().println("Sorry, no countries found maching your input...");
				db.Close();
			}
			System.out.println(request.getParameter("country"));
			
			
		}
		else if(request.getParameter("name").equals("Reports"))
		{
			//Open db connection for future task
			DBconn db = new DBconn();
			Connection conn = db.Open();
			
			//if connection established
			if(conn != null)
			{
				//2.1
				//Count all airports for each country
				Reports reports = new Reports();
				Map<String, String> countries = reports.GetAllCountries(conn);
				List<String> airports = reports.CountAirports(conn, countries);
				
				//print Top 10
				response.getWriter().println("Top 10 countries with highest number of airports");
				response.getWriter().println("");
				for(int i = 0; i < 10; i++)
				{
					response.getWriter().println((i+1) + " " + airports.get(i).replaceAll(",", " "));
				}
				
				//Print Low 10
				response.getWriter().println("");
				response.getWriter().println("");
				response.getWriter().println("Top 10 countries with lowest number of airports");
				response.getWriter().println("");
				for(int i = airports.size()-1; i > airports.size() - 11; i--)
				{
					response.getWriter().println((i+1) + " " + airports.get(i).replaceAll(",", " "));
				}
				
				//2.2
				//Get all possible runways in each country
				Map<String, List<String>> countryRun = reports.RunwayTypes(conn, countries);
				
				//Print Runway types in each country
				response.getWriter().println("");
				response.getWriter().println("");
				response.getWriter().println("Runway types in each country");
				response.getWriter().println("");
				for(Map.Entry<String, List<String>> country : countryRun.entrySet())
				{
					response.getWriter().println(country.getKey());
					for(String type : country.getValue())
					{
						response.getWriter().println("	"+type);
					}
				}
				
				//2.3
				List<String> topRunId = reports.TopRunways(conn);
				
				response.getWriter().println("");
				response.getWriter().println("");
				response.getWriter().println("Top 10 most common runway identifications");
				response.getWriter().println("");
				response.getWriter().println("Num id amount");
				for(int i = 0; i < topRunId.size(); i++)
				{
					response.getWriter().println((i+1) + " " + topRunId.get(i));
				}
			}
			db.Close();
			
		}
		else
		{
			//Somebody is trying to access class that do not exist
			response.getWriter().append("Nothing here...");

		}
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private String Filter(String input)
	{
		return input.replaceAll("[^a-zA-Z ]", "");
	}
}
