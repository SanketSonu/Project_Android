package com.android.receiver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivitycallr extends Activity {
	ServerSocket ss;    
	Socket soc;
	ObjectInputStream oin;
	int frequency = 11025;
	int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
	int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	AudioTrack audioTrack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.callreceive);
		int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 
				11025, 
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT, 
				bufferSize, 
				AudioTrack.MODE_STREAM);
		audioTrack.play();
		System.out.println(""+audioTrack);
		
		Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				new Thread(){
					@Override
					public void run(){
						 FileInputStream fis;
							try {
								fis = new FileInputStream("/sdcard/testing.pcm");
								 byte[] buf = new byte[fis.available()];
								 fis.read(buf);     
								 fis.close(); 
								 audioTrack.write(buf, 0, buf.length);
								 
								 
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					}
				}.start();
			
				     
			}
		});
		Receive();
	}   

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}  
	public void Displaytost(final String message){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), message, 60000000).show();
			}
		});
	}
	boolean isplaying=false;
	public void Receive(){
        new Thread(){
        public void run()
    	{
//        	int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
//        	audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 
//    				11025, 
//    				AudioFormat.CHANNEL_CONFIGURATION_MONO,
//    				AudioFormat.ENCODING_PCM_16BIT, 
//    				bufferSize, 
//    				AudioTrack.MODE_STREAM);
//    		System.out.println(""+audioTrack);
//    		isplaying=false;
    		
    		
    		try
    		{
    		
    	      ss=new ServerSocket(2000);
    	      Displaytost("Waiting for socket");
    	      System.out.println("waiting for socket!!!!");
    	      soc=ss.accept();
    	      Displaytost("Socket connected");
    	      System.out.println("socket connected!!!!");
    	      oin=new ObjectInputStream(soc.getInputStream());
    			while(true)
    			{  
    				       
    			System.out.println("1");  
    			//soc=ss.accept();
    			System.out.println("2");
       			//oos=new ObjectInputStream(soc.getInputStream());
    		    System.out.println("3");
    		                        
    		    byte[] data=null;
    		    try{  
    		    	data = (byte[])oin.readObject();  
    		    	audioTrack.write(data, 0, data.length);  
    		    	System.out.println(""+audioTrack);
    		    	Displaytost(""+data.length);
//    		    	if(!isplaying){   
//    		    		System.out.println("playing");
//    		    		audioTrack.write(data, 0, data.length);
//    		    		audioTrack.play();
//    		    		isplaying=true;
//    		    	}else{
//    		    		System.out.println("writing");
//    		    		audioTrack.write(data, 0, data.length);
//    		    	}
    		    	      
    		    	//storeinfile(data);
    		    }catch (Exception e) {
					// TODO: handle exception
    		    	System.out.println("problem in read "+e.getMessage());
				}      
    		             
                System.out.println(data+" "+data.length);      
                            
             
                           
               System.out.println("55555555555");
    		
    			}
    		                                
    	                                                   
    			}catch(Exception e){        
    				System.out.println("EEEEEEEEEEEEE"+e.getMessage());
    				e.printStackTrace();
    			}
    		   
    	                    
    		}   
        }.start();
	}
	
	public void storeinfile(byte[] bytedata){  
		try {
			   
 			FileOutputStream fs = new FileOutputStream("/sdcard/testing.pcm",true);
 			fs.write(bytedata); 
 			     
 			                   
			fs.close();  
			
			} catch (IOException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
			}      
	}

}
