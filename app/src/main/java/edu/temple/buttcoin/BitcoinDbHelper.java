package edu.temple.buttcoin;

/**
 * Created by rober_000 on 11/14/2016.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tuf08373 on 11/14/2016.
 */
public class BitcoinDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE addresses (" +
                    "addresses_id INTEGER PRIMARY KEY" +
                    "addresses_number TEXT" +
                    "addresses balance REAL);";
    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS addresses";

    private static final String DATABASE_NAME = "owo.db";
    private static final int DB_VERSION = 1;

    public BitcoinDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }
}