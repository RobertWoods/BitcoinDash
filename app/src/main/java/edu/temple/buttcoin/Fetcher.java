package edu.temple.buttcoin;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

/**
 * Created by rober_000 on 11/18/2016.
 */

public abstract class Fetcher<E, T> extends AsyncTask<URL, E, T> {

    ResponseListener<T> listener;

    public Fetcher(ResponseListener<T> listener) {
        this.listener = listener;
    }

    @Override
    protected T doInBackground(URL... urls) {
        T result = null;
        try {
            JsonReader reader = getReaderFromURL(urls[0]);
            result = getDataFromReader(reader);
        } catch (IOException e) {

        }

        return result;
    }


    @Override
    protected void onPostExecute(T result) {
        listener.respondToResult(result);
    }

    protected abstract T getDataFromReader(JsonReader reader) throws IOException;

    JsonReader getReaderFromURL(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        InputStream in = conn.getInputStream();

        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        return reader;
    }

}
