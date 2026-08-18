package com.example.itrproject_001.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "OpenLibrary.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";

    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_EMAIL + " TEXT UNIQUE NOT NULL, " +
                COL_PASSWORD + " TEXT NOT NULL)";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public boolean insertUser(
            String name,
            String email,
            String password) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_NAME, name);
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);

        long result = db.insert(
                TABLE_USERS,
                null,
                values
        );

        return result != -1;
    }

    public boolean checkUser(
            String email,
            String password) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS +
                        " WHERE " + COL_EMAIL + "=? AND " +
                        COL_PASSWORD + "=?",
                new String[]{email, password}
        );

        boolean exists = cursor.getCount() > 0;

        cursor.close();

        return exists;
    }


    public Cursor getUser(String email) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_USERS +
                        " WHERE " + COL_EMAIL + "=?",
                new String[]{email}
        );
    }

    public String getUserName(String email) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT " + COL_NAME +
                        " FROM " + TABLE_USERS +
                        " WHERE " + COL_EMAIL + "=?",
                new String[]{email}
        );

        String name = "";

        if (cursor.moveToFirst()) {
            name = cursor.getString(
                    cursor.getColumnIndexOrThrow(COL_NAME)
            );
        }

        cursor.close();

        return name;
    }

    public boolean updateUser(
            String oldEmail,
            String newName,
            String newEmail) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_NAME, newName);
        values.put(COL_EMAIL, newEmail);

        int result = db.update(
                TABLE_USERS,
                values,
                COL_EMAIL + "=?",
                new String[]{oldEmail}
        );

        return result > 0;
    }
}