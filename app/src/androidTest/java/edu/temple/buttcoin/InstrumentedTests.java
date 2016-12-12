package edu.temple.buttcoin;

import android.app.Instrumentation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.core.deps.guava.util.concurrent.Service;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.LocalBroadcastManager;
import android.test.RenamingDelegatingContext;
import android.test.ServiceTestCase;
import android.util.JsonReader;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTests implements ResponseListener<JsonReader> {


    private Instrumentation instrumentation;
    private SQLiteDatabase db;
    private JsonReader testReader;

    @Before
    public void setup(){
        instrumentation = InstrumentationRegistry.getInstrumentation();
        BitcoinDbHelper dbHelper = new BitcoinDbHelper(instrumentation.getTargetContext());
        db = dbHelper.getWritableDatabase();
        dbHelper.onUpgrade(db, 0, 0);
    }


    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("edu.temple.buttcoin", appContext.getPackageName());
    }

    @Test
    public void testSpanishTranslation(){
        Locale locale = new Locale("es", "ES");
        Locale.setDefault(locale);
        Resources res = getInstrumentation().getTargetContext().getResources();
        Configuration config = res.getConfiguration();
        config.setLocale(locale);
        Context c = getInstrumentation().getTargetContext().createConfigurationContext(config);
        res = c.getResources();
        String s = res.getString(R.string.back_button);
        assertEquals(s, "Regresar");
    }

    @Test
    public void testEnglishTranslation(){
        Locale locale = new Locale("en", "US");
        Locale.setDefault(locale);
        Resources res = instrumentation.getTargetContext().getResources();
        Configuration config = res.getConfiguration();
        config.setLocale(locale);
        Context c = instrumentation.getTargetContext().createConfigurationContext(config);
        res = c.getResources();
        String s = res.getString(R.string.back_button);
        assertEquals(s, "Back");
    }

    @Test
    public void testFetcherGetsJsonReader() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        final Fetcher<JsonReader> fetcher = new Fetcher<JsonReader>(this) {
            @Override
            protected JsonReader getDataFromReader(JsonReader reader) throws IOException {
                return reader;
            }

            protected void onPostExecute(JsonReader result){
                super.onPostExecute(result);
                signal.countDown();
            }
        };
        instrumentation.runOnMainSync(new Runnable(){

            @Override
            public void run() {
                try {
                    fetcher.execute(new URL("http://btc.blockr.io/api/v1/address/balance/"+"hello_my_dude"));
                } catch (MalformedURLException e) {
                    fail();
                }
            }
        });

        signal.await(10, TimeUnit.SECONDS);
        assertNotNull(testReader);
        testReader = null;
    }

    @Override
    public void respondToResult(JsonReader result) {
        testReader = result;
    }

    @Test
    public void testAddressesInsertAndQuery(){
        ContentValues values = new ContentValues();
        values.put(BitcoinDbHelper.AddressesContract.COLUMN_NAME_ID, 1);
        values.put(BitcoinDbHelper.AddressesContract.COLUMN_NAME_NUMBER, 12345);
        values.put(BitcoinDbHelper.AddressesContract.COLUMN_NAME_BALANCE, 123);
        db.insert(BitcoinDbHelper.AddressesContract.TABLE_NAME, null, values);

        Cursor c = db.query(BitcoinDbHelper.AddressesContract.TABLE_NAME,
                new String[] { BitcoinDbHelper.AddressesContract.COLUMN_NAME_ID,
                        BitcoinDbHelper.AddressesContract.COLUMN_NAME_BALANCE,
                        BitcoinDbHelper.AddressesContract.COLUMN_NAME_NUMBER},
                BitcoinDbHelper.AddressesContract.COLUMN_NAME_ID + " = ?",
                new String[] { "1" },
                null, null, null, null);
        assertEquals(c.getCount(), 1);
        c.moveToFirst();
        assertEquals(c.getInt(0), 1);
        assertEquals(c.getString(1), "123");
        assertEquals(c.getString(2), "12345");

        db.close();
    }

    @Test
    public void testStocksInsertAndQuery(){
        ContentValues values = new ContentValues();
        values.put(BitcoinDbHelper.StocksContract.COLUMN_NAME_ID, 1);
        values.put(BitcoinDbHelper.StocksContract.COLUMN_NAME_CHART1D, new byte[] {1,2,3,4});
        db.insert(BitcoinDbHelper.StocksContract.TABLE_NAME, null, values);

        Cursor c = db.query(BitcoinDbHelper.StocksContract.TABLE_NAME,
                new String[] { BitcoinDbHelper.StocksContract.COLUMN_NAME_ID,
                        BitcoinDbHelper.StocksContract.COLUMN_NAME_CHART1D},
                BitcoinDbHelper.StocksContract.COLUMN_NAME_ID + " = ?",
                new String[] { "1" },
                null, null, null, null);
        assertEquals(c.getCount(), 1);
        c.moveToFirst();
        assertEquals(c.getInt(0), 1);
        assertArrayEquals(c.getBlob(1), new byte[] {1,2,3,4});

        db.close();
    }

    @Test
    public void testStocksUpdate(){
        ContentValues values = new ContentValues();
        values.put(BitcoinDbHelper.StocksContract.COLUMN_NAME_ID, 1);
        values.put(BitcoinDbHelper.StocksContract.COLUMN_NAME_CHART1D, new byte[] {1,2,3,4});
        db.insert(BitcoinDbHelper.StocksContract.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(BitcoinDbHelper.StocksContract.COLUMN_NAME_ID, 1);
        values.put(BitcoinDbHelper.StocksContract.COLUMN_NAME_CHART1D, new byte[] {1,2,3,4,5,6});
        int x = db.update(BitcoinDbHelper.StocksContract.TABLE_NAME,
                values,
                BitcoinDbHelper.StocksContract.COLUMN_NAME_ID + " = ?",
                new String[] { "1" });
        assertEquals(x, 1);

        Cursor c = db.query(BitcoinDbHelper.StocksContract.TABLE_NAME,
                new String[] { BitcoinDbHelper.StocksContract.COLUMN_NAME_ID,
                        BitcoinDbHelper.StocksContract.COLUMN_NAME_CHART1D},
                BitcoinDbHelper.StocksContract.COLUMN_NAME_ID + " = ?",
                new String[] { "1" },
                null, null, null, null);

        c.moveToFirst();
        assertEquals(c.getInt(0), 1);
        assertArrayEquals(c.getBlob(1), new byte[] {1,2,3,4,5,6});
    }

    @Test
    public void testAddressesUpdate(){
        ContentValues values = new ContentValues();
        values.put(BitcoinDbHelper.AddressesContract.COLUMN_NAME_ID, 1);
        values.put(BitcoinDbHelper.AddressesContract.COLUMN_NAME_NUMBER, "I'm on my fifth brew");
        values.put(BitcoinDbHelper.AddressesContract.COLUMN_NAME_BALANCE, "and my rents due");
        db.insert(BitcoinDbHelper.AddressesContract.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(BitcoinDbHelper.AddressesContract.COLUMN_NAME_ID, 1);
        values.put(BitcoinDbHelper.AddressesContract.COLUMN_NAME_NUMBER, "Don't stop now");
        values.put(BitcoinDbHelper.AddressesContract.COLUMN_NAME_BALANCE, "Keep dreaming");
        int x = db.update(BitcoinDbHelper.AddressesContract.TABLE_NAME,
                values,
                BitcoinDbHelper.AddressesContract.COLUMN_NAME_ID + " = ?",
                new String[] { "1" });
        assertEquals(x, 1);

        Cursor c = db.query(BitcoinDbHelper.AddressesContract.TABLE_NAME,
                new String[] { BitcoinDbHelper.AddressesContract.COLUMN_NAME_ID,
                        BitcoinDbHelper.AddressesContract.COLUMN_NAME_NUMBER,
                        BitcoinDbHelper.AddressesContract.COLUMN_NAME_BALANCE},
                BitcoinDbHelper.AddressesContract.COLUMN_NAME_ID + " = ?",
                new String[] { "1" },
                null, null, null, null);

        c.moveToFirst();
        assertEquals(c.getInt(0), 1);
        assertEquals(c.getString(1), "Don't stop now");
        assertEquals(c.getString(2), "Keep dreaming");
    }

    @Test
    public void testStartupReceiverReceivesBootAndStartsAlarm() throws InterruptedException {
        //instrumentation.getContext() hasn't been working and idk why so I'm doing this
        Context context = new RenamingDelegatingContext(InstrumentationRegistry.getInstrumentation().getTargetContext(), "test_");
        StartupReceiver startupReceiver = new StartupReceiver();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(context);
        lbm.registerReceiver(startupReceiver, new IntentFilter(Intent.ACTION_BOOT_COMPLETED));
        Intent intent = new Intent(Intent.ACTION_BOOT_COMPLETED);
        LocalBroadcastManager.getInstance(instrumentation.getContext()).sendBroadcast(intent);
        Thread.sleep(1000);
        //Flag is field in StartupReceiver and is set to true after all code to set alarm is finished
        assertEquals(startupReceiver.testFlag, true);
        LocalBroadcastManager.getInstance(instrumentation.getContext()).unregisterReceiver(startupReceiver);
    }


}
