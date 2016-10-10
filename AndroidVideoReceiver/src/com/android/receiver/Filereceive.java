package com.android.receiver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class Filereceive extends Activity  {
    /** Called when the activity is first created. */
	        
	//BufferedImage  bb;	
	ServerSocket ss;    
	Socket soc;   
	ObjectOutputStream oos1;
	ObjectInputStream oos;
	byte socdata[];
	Socket socket;       
	String fname;                
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
                                           
       
       
           
       
            
     
  	 
        new Thread(){
        public void run()
    	{
    		try
    		{
    		    
    	      ss=new ServerSocket(6002);
    	      soc=ss.accept();
    	      oos=new ObjectInputStream(soc.getInputStream());
    			   
    				           
    			System.out.println("1");
    			//soc=ss.accept();
    			System.out.println("2");
       			//oos=new ObjectInputStream(soc.getInputStream());
    		    System.out.println("3");
    		                  
    		             
    		    try{
    		    	fname = (String)oos.readObject();
    		    	socdata = (byte[])oos.readObject();
    		    }catch (Exception e) {
					// TODO: handle exception
    		    	System.out.println("problem in read");
//    		    	break;
    		    	      
				}         
    		             
    		    new Thread(){
    		    	@Override
    		    	public void run(){
    		    		SaveImage(socdata, "/sdcard/"+fname);
    		    		runOnUiThread(new Runnable() {
							
							@Override   
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(getApplicationContext(), "file saved" + fname, 6000000).show();
							}          
						});
    		    	}        
    		    }.start();
              
                              
                           
               System.out.println("55555555555");
    		    
               ss.close();
               soc.close();
               oos.close();
                                                             
    		}catch(Exception e){        
    				System.out.println("EEEEEEEEEEEEE"+e.getMessage());
    				e.printStackTrace();
    				
    		}   
    		   	   
    	                           
    		}   
        }.start();
            
                     
    }       
     
    
                     
             
   
      public void SaveImage(byte[] data,String filename){
    	      
    		try {                
				           
				    
    			System.out.println("the data is to be "+data);
    			File file = new File(filename);
				if(file.exists()){
					file.delete();         
				}            
			    FileOutputStream outStream1 = new FileOutputStream(filename);
			    outStream1.write(data);    
				outStream1.close();        
				       
				Log.d("Camera",  "onPictureTaken - wrote bytes: " + data.length);
				
			} catch (FileNotFoundException e) {    
				
				e.printStackTrace();              
				              
			} catch (IOException e) {
				
				e.printStackTrace();
				          
				                   
			} finally {}    
      }    
                            
                         
    @Override
    public void onResume(){
    	super.onResume();
    	//Thread t = new Thread(this);
    	//t.start();
    }
  	
}