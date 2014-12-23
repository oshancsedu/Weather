package com.example.weather;

import java.text.DecimalFormat;

import android.util.Log;

public class CollectedData {

	double temp = 0.0;
	String city = null, country = null, uptime = null, humidity = null,
			sunrise = null, sunset = null, weather = null;

	public String getSunrise() {
		return sunrise;
	}

	public void setSunrise(String sunrise) {
		this.sunrise = sunrise;
	}

	public String getSunset() {
		return sunset;
	}

	public void setSunset(String sunset) {
		this.sunset = sunset;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getUptime() {
		return uptime;
	}

	public void setUptime(String uptime) {
		this.uptime = uptime;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setCity(String c) {
		this.city = c;
		Log.i("city_collect", this.city);
	}

	public void setTemp(double t) {
		this.temp = t;
		temp -= 273.0;
	}

	public String getCity() {
		Log.i("cityget", city);
		return city;
	}

	public String getTemp() {
		DecimalFormat df = new DecimalFormat("#.00");
		String sTemp = df.format(temp).toString();
		return sTemp;
	}

	public String dataToString() {
		DecimalFormat df = new DecimalFormat("#.00");
		return "In " + city + " " + "the current Temperature in C is "
				+ df.format(temp) + " degrees";
	}
}