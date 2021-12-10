package com.example.finale_weather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
    public static final String databaseName="Data.db";

    public MyDBHelper(Context context)
    {
        super(context,"Data.db",null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE users(username TEXT primary key,password TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS users");
    }

    public boolean insertData(String userName,String password)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("username",userName);
        contentValues.put("password",password);
        long check=database.insert("users",null,contentValues);
        if(check==-1)return false;
        return true;
    }

    public boolean checkUsername(String username)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery("SELECT * FROM users WHERE username=?",new String[]{username});
        if(cursor.getCount()>0)
            return true;

        return false;

    }
    public boolean checkBothField(String username,String password)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery("SELECT * FROM users WHERE username=? and password=?",new String[]{username,password});
        if(cursor.getCount()>0)
            return true;

        return false;

    }
}
