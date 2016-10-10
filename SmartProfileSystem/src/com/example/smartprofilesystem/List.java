package com.example.smartprofilesystem;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class List extends ListActivity {

	String key;
	ArrayList<String> lines;
	ListView lv;
	String[] items;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.percentage);

		lines = new ArrayList<String>();

		// DisplayALert();

		Database dat = new Database(getBaseContext());
		SQLiteDatabase db = dat.getWritableDatabase();

		Cursor c = db.rawQuery("select * from Locations", null);

		if (c.getCount() != 0) {
			c.moveToFirst();
			do {
				// String name = c.getString(c.getColumnIndex("Name"));
				// String att = c.getString(c.getColumnIndex("Attendance1"));
				String commands = c.getString(c.getColumnIndex("Command"));
				String[] cmds = commands.split(" ");
				lines.add("Name: " + c.getString(c.getColumnIndex("ProfileName"))
						+ "\n" + "Bluetooth:: " +cmds[1] +"\nWifi:" + cmds[2]
						+ "\nisSilent: "+ cmds[0] + "\nLocation: "+c.getString(c.getColumnIndex("Location")));

			} while (c.moveToNext());
			
		}

		db.close();
		items = lines.toArray(new String[lines.size()]);

		ArrayAdapter<String> aa = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.listview,
				items);
		setListAdapter(aa);

	}

	@Override
	protected void onListItemClick(ListView l, android.view.View v,
			int position, long id) {

	};

}
