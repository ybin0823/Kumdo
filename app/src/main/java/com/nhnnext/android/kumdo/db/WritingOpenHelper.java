package com.nhnnext.android.kumdo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WritingOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    public static final String WRITING_TABLE_NAME = "writings";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_SENTENCE = "sentence";
    public static final String KEY_WORDS = "words";
    public static final String KEY_IMAGE_URL = "imageUrl";
    public static final String KEY_DATE = "date";
    private final String CREATE_WRITING_TABLE =
            "CREATE TABLE IF NOT EXISTS writings(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT, " +
                    "email TEXT, " +
                    "sentence TEXT," +
                    "words TEXT," +
                    "imageUrl TEXT," +
                    "category NUM," +
                    "date TEXT" +
                    ");";

    public WritingOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public WritingOpenHelper(Context context) {
        super(context, WRITING_TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WRITING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
