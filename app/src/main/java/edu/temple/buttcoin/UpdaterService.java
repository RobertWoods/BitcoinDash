package edu.temple.buttcoin;

import android.app.AlarmManager;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.util.JsonReader;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;



public class UpdaterService extends IntentService implements ResponseListener<Drawable[]> {

    private final String API_URL = "https://chart.yahoo.com/z?s=BTCUSD=X&t=";

    public UpdaterService() {
        super("UpdaterService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        handleChartUpdate();
    }

    private void handleChartUpdate() {
        BitcoinDbHelper dbHelper = new BitcoinDbHelper(this);
        dbHelper.getWritableDatabase();
        startChartFetcher();
    }

    private void startChartFetcher() {

        Fetcher<Drawable[]> fetcher = new Fetcher<Drawable[]>(this) {
            @Override
            protected Drawable[] getDataFromReader(JsonReader reader) throws IOException {
                return null;
            }

            @Override
            protected Drawable[] doInBackground(URL... urls) {
                Drawable[] data = new Drawable[4];
                String[] key = new String[] {"1d", "7d", "14d", "30d"};
                try {
                    for(int i=0; i<data.length; i++) {
                        data[i] = Drawable.createFromStream((InputStream)
                                new URL(API_URL+key[i]).getContent(), "");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
                return data;
            }
        };
        fetcher.execute();
    }


    @Override
    public void respondToResult(Drawable[] results) {
        Log.d("UpdaterService", "We fetched it so good daddy");
        BitcoinDbHelper dbHelper = new BitcoinDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        boolean hasData = db.query(BitcoinDbHelper.StocksContract.TABLE_NAME,
                new String[] {BitcoinDbHelper.StocksContract.COLUMN_NAME_CHART1D},
                null, null, null, null, null).getCount() != 0;
        int i = 1;
        String[] args = {"1", "2", "3", "4"};
        for(Drawable d : results) {
            ContentValues values = new ContentValues();
            values.put(BitcoinDbHelper.StocksContract.COLUMN_NAME_CHART1D,
                    getBlobFromDrawable(d));
            if(hasData){
                String where = BitcoinDbHelper.StocksContract.COLUMN_NAME_ID + "= ?";
                db.update(BitcoinDbHelper.StocksContract.TABLE_NAME, values, where,
                        new String[]{ args[i-1] } );
            } else {
                values.put(BitcoinDbHelper.StocksContract.COLUMN_NAME_ID, i);
                db.insert(BitcoinDbHelper.StocksContract.TABLE_NAME, null, values);
            }
            i++;
        }
        db.close();
    }

    private byte[] getBlobFromDrawable(Drawable image) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ((BitmapDrawable) image).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, out);
        return out.toByteArray();
    }

}
