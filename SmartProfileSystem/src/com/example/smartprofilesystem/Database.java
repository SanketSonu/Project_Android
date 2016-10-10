package com.example.smartprofilesystem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper{

	public static final int DATABASE_VERSION=1;
	public static final String DATABASE_NAME="Tracking.db";
	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override 
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		final String Location=
                "CREATE TABLE Locations" + 
                         "(_id integer primary key autoincrement"
                        + ", Location TEXT " +  
                        ",Command TEXT" +
                        ",ProfileName TEXT)";
        db.execSQL(Location);
        
      
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	

}
