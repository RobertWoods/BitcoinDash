package edu.temple.buttcoin;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.test.ServiceTestCase;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by rober_000 on 12/11/2016.
 */
//Using the deprecated class because ServiceRule doesn't work with IntentService
public class UpdaterServiceTest extends ServiceTestCase<UpdaterService> {


    public UpdaterServiceTest(Class<UpdaterService> serviceClass) {
        super(serviceClass);
    }

    public UpdaterServiceTest(){
        super(UpdaterService.class);
    }


    @Test
    public void testServiceEffectsDatabase() throws InterruptedException {
        BitcoinDbHelper dbHelper = new BitcoinDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.onUpgrade(db, 1, 1);

        //Check if database is empty first
        Cursor c = db.query(BitcoinDbHelper.StocksContract.TABLE_NAME,
                new String[] { BitcoinDbHelper.StocksContract.COLUMN_NAME_ID,
                        BitcoinDbHelper.StocksContract.COLUMN_NAME_CHART1D},
                null,
                null,
                null, null, null, null);
        assertEquals(c.getCount(), 0);


        Intent startIntent = new Intent();
        startIntent.setClass(getContext(), UpdaterService.class);
        startService(startIntent);
        assertNotNull(getService());

        Thread.sleep(1000);
        //Test if it modified the database
        c = db.query(BitcoinDbHelper.StocksContract.TABLE_NAME,
                new String[] { BitcoinDbHelper.StocksContract.COLUMN_NAME_ID,
                        BitcoinDbHelper.StocksContract.COLUMN_NAME_CHART1D},
                null,
                null,
                null, null, null, null);
        assertEquals(c.getCount(), 4);
        db.close();
    }
}
