package com.example.smartprofilesystem;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	Button getLocation;
	MyTrackListener MTListener;
	LocationManager locationMgr;
	static String setLocation = "";
	CheckBox mAudio, mBluetooth, mWifi;
	EditText mName;
	Button mProfiles, mSender, mReceive;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mReceive = (Button) findViewById(R.id.openreceive);
		mSender = (Button) findViewById(R.id.opensender);
		mProfiles = (Button) findViewById(R.id.listprofile);
		mName = (EditText) findViewById(R.id.editText1);
		getLocation = (Button) findViewById(R.id.getlocation);
		mAudio = (CheckBox) findViewById(R.id.checkBox1);
		mBluetooth = (CheckBox) findViewById(R.id.checkBox2);
		mWifi = (CheckBox) findViewById(R.id.checkBox3);

		
		mSender.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent LaunchIntent = MainActivity.this.getPackageManager()
						.getLaunchIntentForPackage(
								"com.example.disastermanagementsender");
				startActivity(LaunchIntent);
			}  
		});
		      
		mReceive.setOnClickListener(new OnClickListener() {
			
			@Override  
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent LaunchIntent = MainActivity.this.getPackageManager()
						.getLaunchIntentForPackage(
								"com.android.receiver");
				startActivity(LaunchIntent);
			}
		});
		
		mProfiles.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				

				Intent intent = new Intent(MainActivity.this, List.class);
				startActivity(intent);
			}
		});
		
		getLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				fetchLocation();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void fetchLocation() {
		locationMgr = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		MTListener = new MyTrackListener();
		final String bestProvider = locationMgr.getBestProvider(new Criteria(),
				true);
		Toast.makeText(getApplicationContext(),
				"Best Provider " + bestProvider, 6000000).show();
		locationMgr.requestLocationUpdates(bestProvider, 0, 0, MTListener);
	}

	private class MyTrackListener implements LocationListener {

		public void onLocationChanged(Location location) {

			Toast.makeText(getApplicationContext(), "Got location", 600).show();
			locationMgr.removeUpdates(MTListener);
			setLocation = location.getLatitude() + ","
					+ location.getLongitude();

			Intent service = new Intent(MainActivity.this, MyTrackService.class);
			startService(service);

			insertToDB(location);

		}

		public void onStatusChanged(String s, int i, Bundle b) {

		}

		public void onProviderDisabled(String s) {

		}

		public void onProviderEnabled(String s) {

		}

	}

	public void insertToDB(Location location) {
		Database database = new Database(getBaseContext());
		SQLiteDatabase db = database.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put("ProfileName", mName.getText().toString());
		cv.put("Command",
				"" + mAudio.isChecked() + " " + mBluetooth.isChecked() + " "
						+ mWifi.isChecked());
		cv.put("Location",
				location.getLatitude() + "," + location.getLongitude());
		db.insert("Locations", null, cv);

		db.close();

		Toast.makeText(getApplicationContext(), "Profile added successfully",
				600000).show();
	}

}
