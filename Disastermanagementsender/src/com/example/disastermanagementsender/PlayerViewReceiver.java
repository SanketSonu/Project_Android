

package com.example.disastermanagementsender;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



  
public class PlayerViewReceiver extends Activity {
	
  
  ServerSocket ss;       
  Socket soc;
  ObjectOutputStream oos1;
  ObjectInputStream oos;
  String mVideoid="";	     
  TextView mStatus;
  TextView mChatinfo,mSendertext;
  EditText textdata,mIP;    
  Button sendt;
   
  SharedPreferences sp;
 
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.playerview_demo);
          
    
//    TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//	   String imei = telephonyManager.getDeviceId();
//	            
////	   if((!imei.contains("356262056467927"))){
////		   TextView tv=null;
////		   tv.setText("");  
////	   }    
//	                                    
	sp = PreferenceManager.getDefaultSharedPreferences(this);
	  
    mStatus = (TextView) findViewById(R.id.status);
    mChatinfo = (TextView) findViewById(R.id.chatinfo);
    mSendertext = (TextView) findViewById(R.id.senderchat);
    textdata = (EditText) findViewById(R.id.textdata);
    mIP = (EditText) findViewById(R.id.ip);  
    sendt = (Button) findViewById(R.id.button);   
    
    ReceiveDataforchar();
    
       
    sendt.setOnClickListener(new OnClickListener() {
		   
		@Override   
		public void onClick(View v) {
			// TODO Auto-generated method stub   
			   
			final String ip = mIP.getText().toString();
			Toast.makeText(getApplicationContext(), MainActivity.senderIP, 6000000).show();
			new Thread(){
				@Override
				public void run(){
					
					ConnectsocketChat(MainActivity.senderIP);
				}   
			}.start();
		}   
	});          
  }               
  String[] temp;     

  
  String chatdata="";
  
  ServerSocket sschat;
  ObjectInputStream oinchat;
  Socket socketchat;
  
  public void ReceiveDataforchar(){
	  
      new Thread(){
    	  
	      @Override
	      public void run()
	  	  {      
	  		try   
	  		{              
	  		           

	  			while(true)       
	  			{
	  				
	  				DisplayToast("waiting for textdata");
	  				  
	  				sschat=new ServerSocket(7777);   
		  			socketchat=sschat.accept();   
		  			DisplayToast("Socket connected");
		  			oinchat=new ObjectInputStream(socketchat.getInputStream());
		  			InetAddress ia = socketchat.getInetAddress();
		  			byte[] ipbyte = ia.getAddress();
		  			
		  			String ip = new String(ipbyte);
		  			//DisplayToast(ip);   
	  			System.out.println("1");
	  			//soc=ss.accept();
	  			System.out.println("2");
	     			//oos=new ObjectInputStream(soc.getInputStream());
	  		    System.out.println("3");    
	  		                                                                       
	  		    try{   
	  		    	DisplayToast("Reading data");               
	  		    	chatdata = (String)oinchat.readObject();
	  		    	System.out.println("Received data"+chatdata); 
	  		    	DisplayToast("Received data:: "+chatdata);   
	  		    	runOnUiThread(new Runnable() {
						                                        
						@Override      
						public void run() {
							// TODO Auto-generated method stub
						      	
							DisplayToast("inside ui thread");
						//	mChatinfo = (TextView) findViewById(R.id.chatinfo);
							mChatinfo.setText(mChatinfo.getText().toString()+"\n"+chatdata);
							                                     
						}         
					});
	  		    	            
	  		    }catch (Exception e) {
						// TODO: handle exception
	  		    	System.out.println("problem in read");
	  		    	e.printStackTrace();
	  		    	break;
				}     

	  		  sschat.close();
	  		  socketchat.close();
	  		  oinchat.close();
	                         
	             System.out.println("55555555555");
	  		        
	  			}    
	  		                                                    
	  			}catch(Exception e){        
	  				System.out.println("EEEEEEEEEEEEE"+e.getMessage());
	  				e.printStackTrace();
	  			}
	  		        
	  		}   
      }.start();
  }
  
  
  public void onDestroy(){
	  super.onDestroy();
	  
	  if(ss!=null){
		  
		  try {   
			sschat.close();
			socketchat.close();
	  		oinchat.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			DisplayToast("chat close:: "+e.getMessage());
		}
		  
	  }
	
  }
  
  public void DisplayToast(final String message){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), message, 600000000).show();
			}
		});
	}      
       
  public void ConnectsocketChat(String ip){
               
		try {        
			  
			Socket socket = new Socket(ip, 4444);
			DisplayToast("Socket connected");
			//ObjectInputStream oin = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream o1 = new ObjectOutputStream(socket.getOutputStream());
			DisplayToast("Writing data");                         
			o1.writeObject(textdata.getText().toString());         
			DisplayToast("Writen data");                        
			
			o1.close();        
			socket.close();    
			
			runOnUiThread(new Runnable() {
				   
				@Override
				public void run() {
					// TODO Auto-generated method stub  
					     
				//	insertorupdatedb(Totalipregistration.mip,true,textdata.getText().toString());
					mSendertext.setText(mSendertext.getText().toString()+"\n"+textdata.getText().toString());
				}
			});
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		                
	} 
  
 
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
  	menu.add(Menu.NONE, 0, 0, "Show settings");
 // 	menu.add(Menu.NONE, 1, 1, "Facebook settings");
  	return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
  	switch (item.getItemId()) {
  		case 0:
  		
  			return true;
  		case 1:
  		//	startActivity(new Intent(this, TestConnect.class));
  	}
  	return false;
  }
  
}
