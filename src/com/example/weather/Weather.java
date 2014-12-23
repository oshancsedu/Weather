package com.example.weather;

import java.io.IOException;
import java.net.URL;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.preference.Preference;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Weather extends Activity implements OnClickListener {

	static final String baseURL = "http://api.openweathermap.org/data/2.5/weather?q=";
	TextView tvWeather, tvCity, tvTemp, tvUptime, tvHumidity, tvTempUnit,
			tvSunrise, tvSunset,tvLableCity,tvLableCountry;
	EditText city, country;
	Button getWeather;
	HandlingXML doingWork;
	String fullUrl, s, c;
	LinearLayout linearParent;
	Date localTime;
	boolean night;
	String unit, timeFormate;
	ListPreference LPtemp_unit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.weather);
		// onResume1();
		initialize();
		setNightMode();
		SharedPreferences getData = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		unit = getData.getString("list_unit", "3");

	}

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater blowup = getMenuInflater();
		blowup.inflate(R.menu.weather, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.settings:
			Intent p = new Intent(Weather.this, Prefs.class);
			startActivity(p);
			break;

		case R.id.exit:
			finish();
			break;
		}

		return false;
	}

	// @Override
	protected void setNightMode() {
		// TODO Auto-generated method stub
		localTime = new Date();
		String time = localTime.toString();
		char ch1 = time.charAt(11);
		char ch2 = time.charAt(12);
		time = String.valueOf(ch1) + String.valueOf(ch2);
		night = false;
		int x = Integer.valueOf(time);
		if (x < 6 || x >= 18) {
			night = true;
			linearParent.setBackgroundResource(R.drawable.clear_sky_night2);
			tvLableCity.setTextColor(Color.rgb(255, 255, 255));
			tvLableCountry.setTextColor(Color.rgb(255, 255, 255));
			tvCity.setTextColor(Color.rgb(255, 255, 255));
			tvSunrise.setTextColor(Color.rgb(255, 255, 255));
			tvSunset.setTextColor(Color.rgb(255, 255, 255));
			tvWeather.setTextColor(Color.rgb(255, 255, 255));
			tvTemp.setTextColor(Color.rgb(255, 255, 255));
			tvHumidity.setTextColor(Color.rgb(255, 255, 255));
			tvTempUnit.setTextColor(Color.rgb(255, 255, 255));
			tvUptime.setTextColor(Color.rgb(255, 255, 255));
			city.setTextColor(Color.rgb(160, 160, 160));
			country.setTextColor(Color.rgb(160, 160, 160));
		}

	}

	private void initialize() {
		// TODO Auto-generated method stub
		getWeather = (Button) findViewById(R.id.bWeather);
		tvCity = (TextView) findViewById(R.id.tvCity);
		tvWeather = (TextView) findViewById(R.id.tvWeather);
		tvSunrise = (TextView) findViewById(R.id.tvsunrise);
		tvSunset = (TextView) findViewById(R.id.tvsunset);
		tvTemp = (TextView) findViewById(R.id.tvTemp);
		tvHumidity = (TextView) findViewById(R.id.tvHumidity);
		tvTempUnit = (TextView) findViewById(R.id.tvtempUnit);
		tvUptime = (TextView) findViewById(R.id.tvUptime);
		city = (EditText) findViewById(R.id.etCity);
		country = (EditText) findViewById(R.id.etCountry);
		tvLableCountry=(TextView)findViewById(R.id.tvLableCountry);
		tvLableCity=(TextView) findViewById(R.id.tvLableCity);
		// LPtemp_unit= (ListPreference) Weather.this.findPreference
		// ("list_unit");
		linearParent = (LinearLayout) findViewById(R.id.llParent);
		getWeather.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		s = null;
		c = null;
		s = city.getText().toString();
		c = country.getText().toString();

		if (s.equals("") || c.equals(""))
			Toast.makeText(this, "Enter Country & City name...",
					Toast.LENGTH_SHORT).show();
		else {
			//ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			//NetworkInfo ni= NetworkInfo.getActiveNetworkInfo ();
			
			ConnectivityManager cm =
			        (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
			 
			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
			boolean isConnected = activeNetwork != null &&
			                      activeNetwork.isConnectedOrConnecting();
			Process process=null;
			try {
				process = Runtime.getRuntime().exec(
				        "/system/bin/ping -c 1 " + "https://www.facebook.com");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Log.i("Process", process.toString());
			
			new PostAsync().execute();
			
			/*if(isConnected==true)
			{
				new PostAsync().execute();

			} else  {
				Toast.makeText(this, "Not connected to Internet",
						Toast.LENGTH_LONG).show();

			}*/
			
			/*if (//conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED||
					conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED
					//||conMgr.getNetworkInfo(2).getState() == NetworkInfo.State.CONNECTED
					//||conMgr.getNetworkInfo(3).getState() == NetworkInfo.State.CONNECTED
					//||conMgr.getNetworkInfo(4).getState() == NetworkInfo.State.CONNECTED
					//||conMgr.getNetworkInfo(5).getState() == NetworkInfo.State.CONNECTED
					//||conMgr.getNetworkInfo(6).getState() == NetworkInfo.State.CONNECTED
					//||conMgr.getNetworkInfo(7).getState() == NetworkInfo.State.CONNECTED
					//||conMgr.getNetworkInfo(8).getState() == NetworkInfo.State.CONNECTED
					//||conMgr.getNetworkInfo(9).getState() == NetworkInfo.State.CONNECTED
					) {

				new PostAsync().execute();

			} else  {
				Toast.makeText(this, "Not connected to Internet",
						Toast.LENGTH_LONG).show();

			}*/
		}
	}

	class PostAsync extends AsyncTask<Void, Void, Void> {

		ProgressDialog pd;
		HandlingXML helper;

		@Override
		protected void onPreExecute() {

			StringBuilder URL = new StringBuilder(baseURL);
			URL.append(s + "." + c);
			fullUrl = URL.toString() + "&mode=xml";
			pd = ProgressDialog.show(Weather.this, "Temperature",
					"Updating Temperature...", true, false);
		}

		@Override
		protected Void doInBackground(Void... params) {

			helper = new HandlingXML();
			try {
				URL website = new URL(fullUrl);

				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();
				XMLReader xr = sp.getXMLReader();

				doingWork = new HandlingXML();
				xr.setContentHandler(doingWork);

				Log.i("Site", fullUrl);

				xr.parse(new InputSource(website.openStream()));

			} catch (Exception e) {
				tvWeather.setText("Error");
				e.printStackTrace();
				Log.i("error", "errooooooooooooooooooor");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			String sunrise, sunset, lastupdate, weather;

			SharedPreferences getData = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());

			unit = getData.getString("list_unit", "1");
			timeFormate = getData.getString("time_format", "1");

			DecimalFormat df = new DecimalFormat("#.00");
			double temp = Double.valueOf(doingWork.getTemp());
			if (unit.equals("2")) {
				temp += 273.0;
				tvTemp.setText(String.valueOf(df.format(temp)));
				tvTempUnit.setText("K");
			} else if (unit.equals("1")) {
				temp = (9.0 / 5.0) * temp + 32.0;
				tvTemp.setText(String.valueOf(df.format(temp)));
				tvTempUnit.setText("°F");
			} else {
				tvTempUnit.setText("°C");
				tvTemp.setText(String.valueOf(df.format(temp)));
			}

			tvTempUnit.setVisibility(View.VISIBLE);

			tvUptime.setText(doingWork.getUptime());
			tvHumidity.setText("Humidity: " + doingWork.getHumidity());
			tvWeather.setText(doingWork.getWeather());
			Log.i("cityfinal", doingWork.getSunrise());

			StringBuffer sb = new StringBuffer(doingWork.getSunrise());
			sb.setCharAt(10, ':');
			sunrise = sb.toString();

			sb = new StringBuffer(doingWork.getSunset());
			sb.setCharAt(10, ':');
			sunset = sb.toString();

			sb = new StringBuffer(doingWork.getUptime());
			sb.setCharAt(10, ':');
			lastupdate = sb.toString();

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd:HH:mm:ss");
			TimeZone tz = Calendar.getInstance().getTimeZone();
			simpleDateFormat.setTimeZone(tz);
			try {
				Date gmtRise = simpleDateFormat.parse(sunrise);

				SimpleDateFormat converter = new SimpleDateFormat(
						"yyyy/MM/dd HH:mm:ss");

				converter.setTimeZone(TimeZone.getDefault());
				Date localTime2 = new Date(gmtRise.getTime()
						+ TimeZone.getDefault().getOffset(localTime.getTime()));

				sunrise = converter.format(localTime2).toString();

				if (timeFormate.equals("2"))
					sunrise = TimeFormate(sunrise);

				// Log.i("hour", hour);
				tvSunrise.setText("Sunrise: " + sunrise + " Local time");

				Date gmtSet = simpleDateFormat.parse(sunset);

				converter.setTimeZone(TimeZone.getDefault());
				localTime2 = new Date(gmtSet.getTime()
						+ TimeZone.getDefault().getOffset(localTime.getTime()));

				sunset = converter.format(localTime2).toString();

				if (timeFormate.equals("2"))
					sunset = TimeFormate(sunset);
				tvSunset.setText("Sunset: " + sunset + " Local time");

				Date gmtUpTime = simpleDateFormat.parse(lastupdate);

				converter.setTimeZone(TimeZone.getDefault());
				localTime2 = new Date(gmtUpTime.getTime()
						+ TimeZone.getDefault().getOffset(localTime.getTime()));

				lastupdate = converter.format(localTime2).toString();

				if (timeFormate.equals("2"))
					lastupdate = TimeFormate(lastupdate);

				sb = new StringBuffer(lastupdate);
				if (timeFormate.equals("2"))
					lastupdate = sb.substring(11, 22);
				else
					lastupdate = sb.substring(11, 19);
				tvUptime.setText("Last Update: " + lastupdate);

			} catch (ParseException ex) {
				System.out.println("Exception " + ex);
			}

			tvCity.setText(doingWork.getCity());

			weather = doingWork.getWeather();
			int rain = -1, cloud = -1, snow = -1;

			rain = weather.indexOf("rain");
			cloud = weather.indexOf("cloud");
			snow = weather.indexOf("snow");
			if (weather.equals("mist") || weather.equals("haze"))
				linearParent.setBackgroundResource(R.drawable.mist);
			else if (cloud != -1)
				if (night == true)
					linearParent.setBackgroundResource(R.drawable.cloud_night_copy);
				else
					linearParent.setBackgroundResource(R.drawable.cloud2_2);
			else if (rain != -1)
				linearParent.setBackgroundResource(R.drawable.rain2);
			else if (snow != -1)
				linearParent.setBackgroundResource(R.drawable.snow_copy);

			else {
				if (night == true)
					linearParent
							.setBackgroundResource(R.drawable.clear_sky_night2);
				else
					linearParent
							.setBackgroundResource(R.drawable.clear_sky_day_copy);
			}

			pd.dismiss();
		}

		String TimeFormate(String sun) {
			StringBuffer sb = new StringBuffer(sun);
			String hour;
			hour = sb.substring(11, 13);
			int h = Integer.valueOf(hour);
			if (h > 11) {
				h -= 12;
				hour = String.valueOf(h);
				if (h < 10)
					hour = "0" + hour;
				sb.replace(11, 13, hour);
				sun = sb.toString() + " pm";
			} else
				sun = sun + " am";
			return sun;
		}
	}
}