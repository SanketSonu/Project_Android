package com.android.receiver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class PhotoReceive extends Activity {

	ServerSocket ss;
	Socket soc;
	ObjectOutputStream oos1;
	ObjectInputStream oos;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photoreceive);

		ReceiveData();

	}

	public void DisplayToast(final String message) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), message, 600000000)
						.show();
			}
		});
	}

	public void ReceiveData() {

		new Thread() {

			@Override
			public void run() {
				try {

					ss = new ServerSocket(6002);
					soc = ss.accept();
					DisplayToast("Socket connected");
					oos = new ObjectInputStream(soc.getInputStream());
					byte[] data = (byte[]) oos.readObject();
					String filename = (String) oos.readObject();
					savefile(filename, data);
					ss.close();
					soc.close();
					oos.close();

				} catch (Exception e) {
					System.out.println("EEEEEEEEEEEEE" + e.getMessage());
					e.printStackTrace();
					DisplayToast("Photo " + e.getMessage());
				}

			}
		}.start();
	}

	public void onPause() {
		super.onPause();
		try {
			if (ss != null || soc != null) {
				ss.close();
				soc.close();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
