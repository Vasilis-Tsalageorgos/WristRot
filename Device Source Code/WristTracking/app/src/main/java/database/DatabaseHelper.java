package database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import model.DataModel;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "wristwrotDatabse";
    public static final String TABLE_NAME = "wristdata";
    public static final String TABLE_COLUMN_NAME_ID = "id";
    public static final String TABLE_COLUMN_NAME_DATE = "date";
    public static final String TABLE_COLUMN_NAME_TIME = "time";
    public static final String TABLE_COLUMN_NAME_ACTIVITY = "activity";
    public static final String TABLE_COLUMN_NAME_ROTATION = "rotation";

    public DataModel model;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);


    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + TABLE_COLUMN_NAME_DATE + " TEXT, "
                + TABLE_COLUMN_NAME_TIME + " TEXT,"
                + TABLE_COLUMN_NAME_ACTIVITY + " TEXT,"
                +TABLE_COLUMN_NAME_ID+"TEXT,"
                + TABLE_COLUMN_NAME_ROTATION + " TEXT)";

        // at last we are calling a exec sql
        // method to execute above sql query
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(sqLiteDatabase);
    }

    public boolean insertRecoed(String date, String time, String activityName, String rotation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TABLE_COLUMN_NAME_DATE, date);
        values.put(TABLE_COLUMN_NAME_TIME, time);
        values.put(TABLE_COLUMN_NAME_ACTIVITY, activityName);
        values.put(TABLE_COLUMN_NAME_ROTATION, rotation);

        long rowInserted = db.insert(TABLE_NAME, null, values);
        if(rowInserted != -1)
            return true;
        else
        return false;
    }


    @SuppressLint("Range")
    public List<DataModel> getData() {
        List<DataModel> dataList=null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor != null) {
            dataList = new ArrayList<>();
            if (cursor.moveToFirst()) {

                do {
                       model = new DataModel();
                       model.setActivityName(cursor.getString(cursor.getColumnIndex(TABLE_COLUMN_NAME_ACTIVITY)));
                       model.setDate(cursor.getString(cursor.getColumnIndex(TABLE_COLUMN_NAME_DATE)));
                       model.setTime(cursor.getString(cursor.getColumnIndex(TABLE_COLUMN_NAME_TIME)));
                       model.setRotation(cursor.getString(cursor.getColumnIndex(TABLE_COLUMN_NAME_ROTATION)));
                       System.out.println("cursor data::"+cursor.getString(0)+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3));
                       dataList.add(model);


                }
                while (cursor.moveToNext());

            }
        }System.out.println("Cursor Count List Size::"+dataList.size());
        return dataList;
    }

    public boolean deleteRecord(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, TABLE_COLUMN_NAME_TIME  + "='" + id+"'", null) > 0;

    }
}





