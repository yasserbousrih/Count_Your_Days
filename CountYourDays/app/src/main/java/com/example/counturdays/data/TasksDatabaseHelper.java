package com.example.counturdays.data;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.counturdays.data.Task;
public class TasksDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Count_your_days_DB";
    private static final String TABLE_NAME = "TASKS";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "USER_ID";
    private static final String COL_3 = "TITLE";
    private static final String COL_4 = "DESCRIPTION";
    private static final String COL_5 = "COMPLETED";

    public TasksDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT , USER_ID INTEGER, TITLE TEXT, DESCRIPTION TEXT, COMPLETED INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertTask(int userId, String title, String description, int completed){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2 , userId);
        values.put(COL_3 , title);
        values.put(COL_4 , description);
        values.put(COL_5 , completed);

        long var = db.insert(TABLE_NAME , null , values);
        return var != -1;
    }

    public Cursor getTask(int taskId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COL_1 + "=?", new String[]{String.valueOf(taskId)}, null, null, null);
        return cursor;
    }

    public boolean updateTask(int taskId, int userId, String title, String description, int completed){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2 , userId);
        contentValues.put(COL_3 , title);
        contentValues.put(COL_4 , description);
        contentValues.put(COL_5 , completed);

        int rowsAffected = db.update(TABLE_NAME , contentValues , "ID=?" , new  String[]{String.valueOf(taskId)});
        return rowsAffected > 0;
    }

    public boolean deleteTask(int taskId){
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_NAME , "ID=?" , new String[]{String.valueOf(taskId)});
        return rowsAffected > 0;
    }

    public Cursor getAllTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME , null);
        return cursor;
    }

    public Cursor getTasksByUser(int userId){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COL_2 + "=?", new String[]{String.valueOf(userId)}, null, null, null);
        return cursor;
    }

    public Cursor getCompletedTasks(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COL_5 + "=?", new String[]{"1"}, null, null, null);
        return cursor;
    }

    public Cursor getIncompleteTasks(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COL_5 + "=?", new String[]{"0"}, null, null, null);
        return cursor;
    }

    public Integer deleteAllTasks(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME , null , null);
    }
}
