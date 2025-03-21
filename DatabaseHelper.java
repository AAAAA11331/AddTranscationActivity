package com.example.project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "budget_app.db";
    public static final int DB_VERSION = 4;  // Increment to 4

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE transactions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "type TEXT, " +
                "category TEXT, " +
                "amount REAL, " +
                "date TEXT, " +
                "note TEXT, " +
                "frequency TEXT, " +
                "frequency_value TEXT, " +
                "frequency_unit TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE transactions ADD COLUMN frequency TEXT");
            db.execSQL("ALTER TABLE transactions ADD COLUMN frequency_value TEXT");
        }
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE transactions ADD COLUMN amount REAL");
        }
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE transactions ADD COLUMN frequency_unit TEXT");
        }
    }
}