package edu.temple.buttcoin;

import android.content.Context;
import android.text.Editable;
import android.util.JsonReader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest implements ResponseListener {

    @Mock
    Editable t;
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
        when(t.toString())
                .thenReturn("Hey");
        String x = t.toString();
        assertEquals(x, "Hey");
    }

    public void testBalanceFetcher(){
        Fetcher fetcher = new Fetcher(this) {
            @Override
            protected Object getDataFromReader(JsonReader reader) throws IOException {
                return null;
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                return null;
            }
        };
    }


    @Override
    public void respondToResult(Object result) {

    }
}