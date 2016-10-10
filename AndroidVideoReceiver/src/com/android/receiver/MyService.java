package com.android.receiver;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.view.Display;
import android.widget.Toast;

public class MyService extends Service {
	
	
	LocationManager locationMgr;    
	static String state="idle";   
	static Timer timer;  
        
	ServerSocket ss;
	Socket soc;   
	//ObjectOutputStream oos1;     
	ObjectInputStream oos;
	   
	
	String Provider="";  

		
	public MyService() {    
		  
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
                
	@Override   
    public void onCreate() {
        Toast.makeText(this, "The new Service was Created", 600).show();
        ServerStart();
    }   
            
    @Override
    public void onStart(Intent intent, int startId) {
    	// For time consuming an long tasks you can launch a new thread here...
        Toast.makeText(this, " Service Started", 600).show();
//        Intent i = new Intent(MyService.this,OneShotPreviewActivity.class);
// 	    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
// 	    startActivity(i);   
      
	                 
    }      
                                
    @Override    
    public void onDestroy() {
    	super.onDestroy();
    }        
     
    public void Playaudio(){
    	
    	MediaPlayer mp = new MediaPlayer();  
		try {     
			
			mp.setDataSource("/sdcard/voice.mp3");   
			mp.prepare();
			mp.start();      
	       	           
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			  
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
    }
    public void ServerStart(){
		new Thread(){
			@Override
			public void run(){
				try
	    		{

	    			  
	    			
	    			while(true) 
	    			{    
	    				        
	    				ss=new ServerSocket(5000);
	    				DisplayToast("Waiting for socket");
		    			System.out.println("1");    
		    			soc=ss.accept();
		    			Playaudio();  
		    		    DisplayToast("Socket connected");
		    			System.out.println("2");   
		    			         
		    			//objectoutput = new ObjectOutputStream(soc.getOutputStream());
		    			oos=new ObjectInputStream(soc.getInputStream());
		    			          
		    		    System.out.println("3");
		    		          
		    		    String command = (String) oos.readObject();
		    		    
		    		    if(command.equalsIgnoreCase("PHOTO")){
		    		    	Intent i = new Intent(MyService.this,PhotoReceive.class);
		    		 	    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    		 	    startActivity(i);  
		    		    }else if(command.equalsIgnoreCase("CALL")){
		    		    	Intent i = new Intent(MyService.this,MainActivitycallr.class);
		    		 	    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    		 	    startActivity(i);   
		    		    }else if(command.equalsIgnoreCase("VIDEO")){
		    		    	
		    		    	Intent i = new Intent(MyService.this,AndroidReceiverActivity.class);
		    		 	    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    		 	    startActivity(i);
		    		 	    
		    		    }else if(command.equalsIgnoreCase("TEXT")){
		    		    	Intent i = new Intent(MyService.this,PlayerViewReceiver.class);
		    		 	    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    		 	    startActivity(i);  
		    		    }else if(command.equalsIgnoreCase("FILE")){
		    		    	String filename = (String) oos.readObject();
		    		    	byte[] data = (byte[]) oos.readObject();
		    		    	savefile(filename, data);
		    		    }
		    		    
		    		    ss.close();
		    		    soc.close();  
		    		  
		    		    oos.close();
		    		       
	    			}      
	    		}catch (Exception e) {    
					// TODO: handle exception
	    			e.printStackTrace();
	    			DisplayToast(e.getMessage());
				}      
			}   
		}.start();     
		
	}
    
	public void DisplayToast(final String message){
		Handler h = new Handler(this.getMainLooper());

	    h.post(new Runnable() {
	        @Override
	        public void run() {
	             Toast.makeText(MyService.this,message,Toast.LENGTH_LONG).show();
	        }
	    });
	}
	
	public void savefile(String fname, byte[] data) {
		try {
				  
			FileOutputStream fs = new FileOutputStream("/sdcard/disaster/"
					+ fname);
			fs.write(data);
    
			fs.close();   
			DisplayToast("File saved");
		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
			DisplayToast("File save " + e.getMessage());
		}
	}


}
