package com.example.smartprofilesystem;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.widget.Toast;

public class smsReceiver extends BroadcastReceiver {

	static String incomingno;
	Context cntxt;
	SharedPreferences sp;
	SmsManager smanager;
	static MediaPlayer mp;
	NotificationManager notificationManager;
	static LocationManager locationManager;
	static MyLocationListener locationListener;
	static String location = "";
	static float distance = 0f;
	String body;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		cntxt = context;
		sp = PreferenceManager.getDefaultSharedPreferences(context);
		smanager = SmsManager.getDefault();
		notificationManager = (NotificationManager) context
				.getSystemService(context.NOTIFICATION_SERVICE);

		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		String str = "";
		mp = new MediaPlayer();
		if (bundle != null) {
			// ---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for (int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				incomingno = msgs[i].getOriginatingAddress();
				String body = msgs[i].getMessageBody().toString();
				
				this.body = body;
				
				if (body.contains("busloc1234")) {

					String[] temp = body.split("\\#");
					if (temp.length == 2) {
						
						location = temp[1];
						
						final Criteria criteria = new Criteria();
						locationManager = (LocationManager) context
								.getSystemService(Context.LOCATION_SERVICE);
						final String bestProvider = locationManager
								.getBestProvider(criteria, true);

						Toast.makeText(context, "" + bestProvider, 600000)
								.show();
							
						locationListener = new MyLocationListener();
						locationManager.requestLocationUpdates(bestProvider, 0,
								0, locationListener);
						
					}

				}else if(body.equalsIgnoreCase("busloc")){
					final Criteria criteria = new Criteria();
					locationManager = (LocationManager) context
							.getSystemService(Context.LOCATION_SERVICE);
					final String bestProvider = locationManager
							.getBestProvider(criteria, true);

					Toast.makeText(context, "" + bestProvider, 600000)
							.show();
						
					locationListener = new MyLocationListener();
					locationManager.requestLocationUpdates(bestProvider, 0,
							0, locationListener);
				}
				
				
			}
		}

	}
	
	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {

			
			stopListening(locationManager);
			
			if(body.equalsIgnoreCase("busloc")){
				SmsManager sms = SmsManager.getDefault();
				sms.sendTextMessage(incomingno, null, "busloc1234#"+location.getLatitude()+","+location.getLongitude(), null, null);
			}else{
				
				Location loc = new Location(LocationManager.NETWORK_PROVIDER);
				String[] temp = smsReceiver.location.split("\\,");
				System.out.println("" + smsReceiver.location);
					   
				loc.setLatitude(Double.parseDouble(temp[0]));
				loc.setLongitude(Double.parseDouble(temp[1]));
				distance = location.distanceTo(loc);
				int dist = (int)distance;
				Intent it = new Intent(cntxt, MyAlarmService.class);
				it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				it.putExtra("dist", "The bus is in "+dist + " meter distance");
				cntxt.startActivity(it);	
			}   
			
			
		}

		public void onStatusChanged(String s, int i, Bundle b) {

		}

		public void onProviderDisabled(String s) {

		}

		public void onProviderEnabled(String s) {

		}

	}

	public void stopListening(LocationManager locationManager) {
		try {
			if (locationManager != null && locationListener != null) {
				locationManager.removeUpdates(locationListener);
			}

			locationManager = null;
		} catch (final Exception ex) {

		}
	}

}
