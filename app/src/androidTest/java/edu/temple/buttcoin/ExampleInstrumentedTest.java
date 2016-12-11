package edu.temple.buttcoin;

import android.app.Instrumentation;
import android.content.Context;
import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.util.JsonReader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest implements ResponseListener<String> {

    Instrumentation instrumentation;

    @Before
    public void setup(){
        instrumentation = InstrumentationRegistry.getInstrumentation();
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("edu.temple.buttcoin", appContext.getPackageName());
        ChartFragment fragment = new ChartFragment();

    }

    @Test
    public void testSpanishTranslation(){
        Locale.setDefault(new Locale("es", "ES"));
        Resources res = getInstrumentation().getTargetContext().getResources();
        String s = res.getString(R.string.back_button);
        assertEquals(s, "Regresar");
    }

    @Test
    public void testBalanceFetcher(){
        final Fetcher<String> fetcher = new Fetcher<String>(this) {
            @Override
            protected String getDataFromReader(JsonReader reader) throws IOException {
                reader.beginObject();
                reader.nextName();
                reader.nextString();
                reader.nextName();
                reader.beginObject();
                reader.nextName();
                reader.nextString();
                reader.nextName();
                return reader.nextString();
            }
        };
        try {
            fetcher.execute(new URL("http://btc.blockr.io/api/v1/address/balance/"+"hello_my_dude"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        runTestOnUIThread(new Runnable(){

            @Override
            public void run() {
                fetcher.execute();
            }
        });
    }

    @Override
    public void respondToResult(String result) {

    }
}
