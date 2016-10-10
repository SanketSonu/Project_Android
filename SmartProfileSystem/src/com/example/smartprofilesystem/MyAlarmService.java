package com.example.smartprofilesystem;
import java.util.Locale;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class MyAlarmService extends Activity implements TextToSpeech.OnInitListener, OnUtteranceCompletedListener {
	
	
    private TextToSpeech mTts;
    private String spokenText= "";
    Intent intent;
	 
	
    @Override
    public void onCreate(Bundle savedInstance) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstance);
    	Toast.makeText(this, "MyAlarmService.onCreate()", Toast.LENGTH_LONG).show();
    	Log.d("this is daya","daya alaram");
    	
    	intent = getIntent();
 	
    }

 
    @Override
    public void onDestroy() {
	
    	// TODO Auto-generated method stub
	
    	if (mTts != null) {
    		mTts.stop();
    		mTts.shutdown();
    	}
	         
    	super.onDestroy();
    	Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG).show();
    }
    
    @Override
    public void onStart() {
		super.onStart();
    	//Notification(intent.getStringExtra("text"));
    	spokenText = "";
    	spokenText += intent.getStringExtra("dist");
    	mTts = new TextToSpeech(this, this);
    	Toast.makeText(this, "MyAlarmService.onStart()"+spokenText, Toast.LENGTH_LONG).show();
    }

 
   
    @Override
    public void onUtteranceCompleted(String utteranceId) {
    	
    }
    
    @Override
    public void onInit(int status) {
	// TODO Auto-generated method stub
	   
	if (status == TextToSpeech.SUCCESS) {
		   
        int result = mTts.setLanguage(Locale.US);
        if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
        	spokenText += spokenText+""+spokenText+"";
        	
            mTts.speak(spokenText, TextToSpeech.QUEUE_FLUSH, null);
        }   
    }   
	             
}



}