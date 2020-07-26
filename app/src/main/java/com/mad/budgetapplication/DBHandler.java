package com.mad.budgetapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper{
    private static final String FILENAME = "DBHandler.java";
    private static final String TAG = "MoneyAssistant";
    public static String DATABASE_NAME = "RTransactionHist.db";
    public static int DATABASE_VERSION = 1;
    public  static String RTRANSACTIONHIST = "RTransactions";
    public static String COLUMN_DATE = "Date";
    public static String COLUMN_NAME = "Name";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RTRANSACTION_TABLE = "CREATE TABLE "+ RTRANSACTIONHIST+ "("+ COLUMN_NAME + " TEXT," + COLUMN_DATE + " TEXT" + ")";
        db.execSQL(CREATE_RTRANSACTION_TABLE);
        Log.v(TAG, "DB created "+CREATE_RTRANSACTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RTRANSACTIONHIST);
        onCreate(db);
    }

    public void addRTransactionHist(Transaction transaction, String date){
        //this is to record Rtransaction that has created a transaction
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        values.put(COLUMN_NAME, transaction.getName());
        values.put(COLUMN_DATE, date);

        db.insert(RTRANSACTIONHIST, null, values);
        Log.v(TAG, FILENAME + ": Adding data to database: "+ values.toString());

        db.close();
    }

    //date parsed in should be today's date
    public boolean findHist(String name, String date){
        // this function to check whether Hist exists
        String query = "SELECT * FROM "+ RTRANSACTIONHIST+" WHERE "+ COLUMN_NAME + " = \""+name+"\"" + " AND "+ COLUMN_DATE +" = \""+date+"\"";
        Log.v(TAG, FILENAME + " findHist: "+ query);

        SQLiteDatabase db =this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            Log.v(TAG, FILENAME+": Name: "+ cursor.getString(0)+ "Date: "+cursor.getString(1));
            return false;
        }
        else {
            return true;
        }
    }
}
