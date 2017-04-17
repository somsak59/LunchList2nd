package com.boupha.scdev.lunchlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by scdev on 17/4/2560.
 */

public class RestaurantHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "lunchlist.db";
    private static final int SCHEMA_VERSION = 1;
    public RestaurantHelper(Context context){
        super(context, DATABASE_NAME,null, SCHEMA_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE restaurant (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, address TEXT, type TEXT, notes TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    //no-op, since will not be called until 2 nd schema
    //version exists
    }
    public void insert(String name, String address, String type, String notes){
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("address", address);
        cv.put("type", type);
        cv.put("notes", notes);

        getWritableDatabase().insert("restaurant","name", cv);
    }
    public Cursor getAll(){
        return (getReadableDatabase().rawQuery("SELECT _id, name, address, type, notes FROM restaurant ORDER BY name", null));

    }
    public String getName(Cursor c){
        return (c.getString(1));
    }
    public String getAddress(Cursor c){
        return (c.getString(2));
    }
    public String getType(Cursor c){
        return (c.getString(3));
    }
    public String getNotes(Cursor c){
        return (c.getString(4));
    }
}
