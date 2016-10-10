package com.android.receiver;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidReceiverActivity extends Activity  {
    /** Called when the activity is first created. */
	        
	//BufferedImage  bb;	
	ServerSocket ss;    
	Socket soc;   
	ObjectOutputStream oos1;
	ObjectInputStream oos;
	ImageView iv;   
	EditText et;
	static boolean isBroadcast=false;
	Button capture;
	byte socdata[];
	Bitmap image ;
	Socket socket;       
	                        	
    @Override        
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main1);    
           
       TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
  	   String imei = telephonyManager.getDeviceId();
 	       
// 	   if((!imei.contains("352306054558775"))){
// 		   TextView tv=null;   
// 		   tv.setText("");    
// 	   }               
                                           
        Button btn = (Button) findViewById(R.id.btn);
        et = (EditText) findViewById(R.id.et);
        capture = (Button) findViewById(R.id.btn1);
            
      
            
        iv = (ImageView) findViewById(R.id.iv);
        
        new Thread(){
        public void run()
    	{
    		try
    		{
    		
    	      ss=new ServerSocket(1234);
    	      soc=ss.accept();
    	      DisplayToast("Socket connected");
    	      oos=new ObjectInputStream(soc.getInputStream());
    			while(true)
    			{
    				    
    			System.out.println("1");
    			//soc=ss.accept();
    			System.out.println("2");
       			//oos=new ObjectInputStream(soc.getInputStream());
    		    System.out.println("3");
    		    
    		                      
    		    try{
    		    	socdata = (byte[])oos.readObject();
    		    }catch (Exception e) {
					// TODO: handle exception
    		    	System.out.println("problem in read");
    		    	break;
				}         
    		           
//    		    new Thread(){
//    		    	@Override
//    		    	public void run(){
//    		    		SaveImage(socdata, "/sdcard/videoshare/"+System.currentTimeMillis()+".jpg");
//    		    	}   
//    		    }.start();
                System.out.println(socdata);   
                            
               image = BitmapFactory.decodeByteArray(socdata, 0, socdata.length);
               runOnUiThread(new Runnable() {
            	    public void run() {
            	        // change text     
            	    	//iv.setDrawingCacheEnabled(false);
            	    	iv = (ImageView) findViewById(R.id.iv);
            	    	iv.setImageBitmap(image);
            	    	//image.recycle();      
            	    	image=null;    
            	    }                      
            	});	                        
                                                
               System.out.println("55555555555");
    		
    			}
    		                                
    	                                                   
    			}catch(Exception e){        
    				System.out.println("EEEEEEEEEEEEE"+e.getMessage());
    				e.printStackTrace();
    			}
    		       
    	                        
    		}   
        }.start();
         
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
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	Toast.makeText(getApplicationContext(), "Destroy", 6000000).show();
    	if(ss!=null){
    		Toast.makeText(getApplicationContext(), "inside null check", 6000000).show();
    		try {
				ss.close();
				soc.close();
				oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), ""+e.getMessage(), 6000000).show();
			}
    		
    	}
    }
  	
}