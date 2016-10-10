package com.example.smartprofilesystem;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.media.AudioManager;
import android.net.wifi.WifiManager;

public class Utils {

	Context mContext;

	public Utils(Context mContext) {
		this.mContext = mContext;
	}

	public void setAudioProfile(boolean isSilent) {
		AudioManager myAudioManager = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);

		if (isSilent) {
			myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
			//myAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		} else {
			myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		}
	}   

	public void setBluetoothStatus(boolean status) {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		
		if (status == true && !mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.enable();
		} else if(status == false && mBluetoothAdapter.isEnabled()){   
			mBluetoothAdapter.disable();
		}  
	}
	
	public void setWifiStatus(boolean status) {
		WifiManager wifiManager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		if (status == true && !wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		} else if (status == false && wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(false);
		}
	}

}
