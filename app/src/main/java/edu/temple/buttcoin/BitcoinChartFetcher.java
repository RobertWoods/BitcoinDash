package edu.temple.buttcoin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Message;
import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by rober_000 on 11/12/2016.
 */

public class BitcoinChartFetcher extends AsyncTask<URL, Integer, Drawable> {

    ResponseListener listener;

    public BitcoinChartFetcher(ResponseListener listener) {
        this.listener = listener;
    }

    @Override
    protected Drawable doInBackground(URL... urls) {
        try {
            return getDrawableFromURL(urls[0]);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Drawable d) {
        listener.respondToResult(d);
    }


    Drawable getDrawableFromURL(URL url) throws IOException {
        InputStream is = (InputStream) url.getContent();
        return Drawable.createFromStream(is, "");
    }


}


