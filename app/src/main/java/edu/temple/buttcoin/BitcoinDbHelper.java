package edu.temple.buttcoin;

/**
 * Created by rober_000 on 11/14/2016.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by tuf08373 on 11/14/2016.
 */
public class BitcoinDbHelper extends SQLiteOpenHelper implements BaseColumns {

    public static final String TABLE_NAME = "addresses";
    public static final String COLUMN_NAME_ID = "addresses_id";
    public static final String COLUMN_NAME_NUMBER = "addresses_number";
    public static final String COLUMN_NAME_BALANCE = "addresses_balance";
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_NUMBER + " TEXT," +
                    COLUMN_NAME_BALANCE + " REAL);";
    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

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