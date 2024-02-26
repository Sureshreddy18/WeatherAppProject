package org.own;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@WebServlet("/Weather")
public class Weather extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.sendRedirect("index.html");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String apiKey="318b0e53453526b505df2509b1a928a1";
		
		String city=request.getParameter("city");
		
		String apiUrl="https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+apiKey;
		
		try {
		
		URL url=new URL(apiUrl);
		
		HttpURLConnection con=(HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		
		InputStream inpStream=con.getInputStream();
		InputStreamReader reader=new InputStreamReader(inpStream);
		
		StringBuilder resultContent=new StringBuilder();
		
		Scanner sc=new Scanner(reader);
		
		while(sc.hasNext())
		{
			resultContent.append(sc.nextLine());
		}
		
		sc.close();
		System.out.println(resultContent);
		
		Gson gson=new Gson();
		 JsonObject jsonObject = gson.fromJson(resultContent.toString(), JsonObject.class);
         
         //Date & Time
         long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
         String date = new Date(dateTimestamp).toString();
         
         //Temperature
         double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
         int temperatureCelsius = (int) (temperatureKelvin - 273.15);
        
         //Humidity
         int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
         
         //Wind Speed
         double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
         
         //Weather Condition
         String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
         
         // Set the data as request attributes (for sending to the jsp page)
         request.setAttribute("date", date);
         request.setAttribute("city", city);
         request.setAttribute("temperature", temperatureCelsius);
         request.setAttribute("weatherCondition", weatherCondition); 
         request.setAttribute("humidity", humidity);    
         request.setAttribute("windSpeed", windSpeed);
         request.setAttribute("weatherData", resultContent.toString());
         con.disconnect();
		}catch(IOException e) {
            e.printStackTrace();
        }
		
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

}
