package com.example.counturdays.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UserDatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "Count_your_days_DB";
        private static final String TABLE_NAME = "USER";
        private static final String COL_1 = "ID";
        private static final String COL_2 = "USERNAME";
        private static final String COL_3 = "EMAIL";
        private static final String COL_4 = "PASSWORD_HASH";

    public UserDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME , null, 1);
    }


    @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT , USERNAME TEXT , EMAIL TEXT, PASSWORD_HASH TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

    public long insertUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2 , user.getUsername());
        values.put(COL_3 , user.getEmail());
        values.put(COL_4 , user.getPasswordHash());

        long result = db.insert(TABLE_NAME , null , values);
        return result;
    }


    public User getUserByUsername(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COL_2 + "=?", new String[]{username}, null, null, null);
        User user = null;
        if(cursor != null && cursor.moveToFirst()){
            int id = cursor.getInt(cursor.getColumnIndex(COL_1));
            String email = cursor.getString(cursor.getColumnIndex(COL_3));
            String passwordHash = cursor.getString(cursor.getColumnIndex(COL_4));
            user = new User(id, username, email, passwordHash);
        }
        if(cursor != null) {
            cursor.close();
        }
        return user;
    }


    public boolean updateUser(User user){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_2 , user.getUsername());
            contentValues.put(COL_3 , user.getEmail());
            contentValues.put(COL_4 , user.getPasswordHash());

            db.update(TABLE_NAME , contentValues , "ID=?" , new  String[]{String.valueOf(user.getId())});
            return true;
        }

        public Integer deleteUser(User user){
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(TABLE_NAME , "ID=?" , new String[]{String.valueOf(user.getId())});
        }
    }


