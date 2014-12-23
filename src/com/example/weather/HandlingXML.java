package com.example.weather;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import android.util.Log;


public class HandlingXML extends DefaultHandler{

CollectedData info= new CollectedData();
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		
		if (localName.equals("city")){
			String city = attributes.getValue("name");
			info.setCity(city);
			Log.i("City", city);
		}else if(localName.equals("temperature")){
			String t = attributes.getValue("value");
			
			double temp=Double.parseDouble(t);
			info.setTemp(temp);
		}else if(localName.equals("lastupdate")){
			String Uptime = attributes.getValue("value");
			info.setUptime(Uptime);
		}else if(localName.equals("humidity")){
			String humidity = attributes.getValue("value");
			humidity += attributes.getValue("unit");
			info.setHumidity(humidity);
		}else if(localName.equals("weather")){
			String weather = attributes.getValue("value");
			info.setWeather(weather);
		}else if(localName.equals("sun")){
			String sun = attributes.getValue("rise");
			info.setSunrise(sun);
			sun = attributes.getValue("set");
			info.setSunset(sun);
		}
	}

	public String getCity(){
		return info.getCity();
	}
	
	public String getUptime(){
		return info.getUptime();
	}
	
	public String getTemp() {
		return info.getTemp();
	}

	public String getHumidity() {
		// TODO Auto-generated method stub
		return info.getHumidity();
	}

	public String getWeather() {
		// TODO Auto-generated method stub
		return info.getWeather();
	}
	
	public String getSunrise() {
		// TODO Auto-generated method stub
		return info.getSunrise();
	}
	
	public String getSunset() {
		// TODO Auto-generated method stub
		return info.getSunset();
	}
}