package com.android.receiver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class Main extends Activity{
	
	EditText mUsername,mPwd,mIP;  
	static boolean islogged=false;  
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);   
	    
	    Intent service = new Intent(this,MyService.class);
	    startService(service);   
	    
	}
	
}
