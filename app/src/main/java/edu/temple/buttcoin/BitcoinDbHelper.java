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




    private static final String DATABASE_NAME = "owo.db";
    private static final int DB_VERSION = 3;

    public BitcoinDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(AddressesContract.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(StocksContract.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(AddressesContract.SQL_DELETE_TABLE);
        sqLiteDatabase.execSQL(AddressesContract.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(StocksContract.SQL_DELETE_TABLE);
        sqLiteDatabase.execSQL(StocksContract.SQL_CREATE_TABLE);
    }

    public static class AddressesContract {
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
    }

    public static class StocksContract {
        public static final String TABLE_NAME = "stocks";
        public static final String COLUMN_NAME_ID = "stocks_id";
        public static final String COLUMN_NAME_CHART1D = "stocks_chart_day";
        public static final String COLUMN_NAME_CHART1W = "stocks_chart_week";
        public static final String COLUMN_NAME_CHART2W = "stocks_chart_two_weeks";
        public static final String COLUMN_NAME_CHART1M = "stocks_chart_month";
        private static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_CHART1W + " BLOB," +
                        COLUMN_NAME_CHART1M + " BLOB," +
                        COLUMN_NAME_CHART2W + " BLOB," +
                        COLUMN_NAME_CHART1D + " BLOB);";
        private static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}