package com.example.todo20.DatabaseManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelperClass extends SQLiteOpenHelper {
    private static final String TABLE_NAME="TODO_TABLE";
    private static final String DATABASE_NAME="TODO_DATABASE";

    public DbHelperClass(@Nullable Context context) {
        super(context,DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableQuery="CREATE TABLE "+TABLE_NAME+"( ID integer primary key autoincrement , TASK TEXT ,STATUS INTEGER ) ";
        db.execSQL(tableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropQuery="DROP TABLE IF EXISTS "+TABLE_NAME;
        db.execSQL(dropQuery);
        onCreate(db);
    }

    //---------------ADDING FUNCTION IN SQL---------------//
    public boolean addDataToSQL(String task,int status){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("TASK",task);
        cv.put("STATUS",status);
        float isExecuted = db.insert(TABLE_NAME,null,cv);
        if(isExecuted==-1){
            return false;
        }else {
            return true;
        }
    }//end of add Function
    //-------------------------------------------//
    //---------RETRIEVING DATA FUNCTION----------//
    public Cursor getAllData(){
        SQLiteDatabase db=this.getWritableDatabase();
        String selectingAllQuery="select * from "+TABLE_NAME +" order by ID desc";
        Cursor cursor=db.rawQuery(selectingAllQuery,null);
        return cursor;
    }//end of retrieving function
    //-------------------------------------------//


}
