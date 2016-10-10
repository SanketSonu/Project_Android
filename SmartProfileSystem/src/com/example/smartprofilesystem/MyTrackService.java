package com.example.smartprofilesystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.widget.Toast;

public class MyTrackService extends Service {

	MyTrackListener MTListener;
	LocationManager locationMgr;
	static String state = "idle";
	static boolean mBluezoneflag = false, mRedzoneflag = false;
	private TextToSpeech mTts;
	Context mContext;
	ArrayList<String> allLocation = new ArrayList<String>();
	ArrayList<String> allCommand = new ArrayList<String>();

	public MyTrackService() {

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
       
	@Override
	public void onCreate() {
		Toast.makeText(this, "The new TrackService was Created",
				Toast.LENGTH_LONG).show();
		mContext = this;

		Database dat = new Database(this);
		SQLiteDatabase db = dat.getWritableDatabase();
		Cursor c = db.rawQuery("select * from Locations", null);

		if (c.getCount() != 0) {
			c.moveToFirst();

			do {

				allLocation.add(c.getString(c.getColumnIndex("Location")));
				allCommand.add(c.getString(c.getColumnIndex("Command")));

			} while (c.moveToNext());
		}

		db.close();

	}

	@Override
	public void onStart(Intent intent, int startId) {
		// For time consuming an long tasks you can launch a new thread here...
		Toast.makeText(this, " Service Started", Toast.LENGTH_LONG).show();
		mTts = new TextToSpeech(this, new OnInitListener() {

			@Override
			public void onInit(int status) {
				// TODO Auto-generated method stub
				if (status == TextToSpeech.SUCCESS) {

					int result = mTts.setLanguage(Locale.US);
					if (result != TextToSpeech.LANG_MISSING_DATA
							&& result != TextToSpeech.LANG_NOT_SUPPORTED) {

					}
				}
			}
		});

		final Criteria criteria = new Criteria();

		state = "Active";
		locationMgr = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		MTListener = new MyTrackListener();
		final String bestProvider = locationMgr.getBestProvider(criteria, true);
		Toast.makeText(getApplicationContext(),
				"Best Provider " + bestProvider, 6000000).show();
		locationMgr.requestLocationUpdates(bestProvider, 0, 0, MTListener);
		// }

	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
		if (locationMgr != null && MTListener != null) {
			locationMgr.removeUpdates(MTListener);
			locationMgr = null;
			MTListener = null;
		}
	}

	private class MyTrackListener implements LocationListener {

		public void onLocationChanged(Location location) {

			Toast.makeText(getApplicationContext(), "got loc", 600).show();

			checkLocation(location);
		}

		public void onStatusChanged(String s, int i, Bundle b) {

		}

		public void onProviderDisabled(String s) {

		}

		public void onProviderEnabled(String s) {

		}

	}

	public boolean isSet(String cmd){
		if(cmd.equalsIgnoreCase("true")){
			return true;
		}else{
			return false;
		}
	}
	
	public void checkLocation(Location location) {
		for (int i = 0; i < allCommand.size(); i++) {
			Location mStationLocation = new Location(
					LocationManager.NETWORK_PROVIDER);

			String[] setlocation = allLocation.get(i).split("\\,");
			String[] commands = allCommand.get(i).split(" ");
			
			mStationLocation.setLatitude(Double.parseDouble(setlocation[0]));
			mStationLocation.setLongitude(Double.parseDouble(setlocation[1]));

			float dist = mStationLocation.distanceTo(location);
			Toast.makeText(getApplicationContext(), "dist: " + dist + " "+allCommand.get(i), 60000000)
					.show();
				
			if (dist <= 10.0) {	
				Utils util = new Utils(mContext);
				util.setAudioProfile(isSet(commands[0]));
				util.setBluetoothStatus(isSet(commands[1]));
				util.setWifiStatus(isSet(commands[2]));
				break;        
			} else {           
				Utils util = new Utils(mContext);
				util.setAudioProfile(false);
				util.setBluetoothStatus(false);
				util.setWifiStatus(false);
			}
		}
	}
	
	public void PlayAudio(String mAudioPath) {

		final MediaPlayer mp = new MediaPlayer();
        
		try {   
			mp.setDataSource(mAudioPath);
			mp.prepare();
			mp.start();
			mp.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp1) {
					// TODO Auto-generated method stub
					mp1.release();
					mp.release();
				}
			});
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
		
	}

}
