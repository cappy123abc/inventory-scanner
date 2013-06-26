package com.douglasinventory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InventoryDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;

    InventoryDatabaseHelper(Context context) {
        super(context, "inventory", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	
        db.execSQL("CREATE TABLE cycle_count (_id INTEGER PRIMARY KEY, item_id TEXT, qty INTEGER, location TEXT);");
    }


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	

}
