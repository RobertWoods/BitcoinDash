package edu.temple.buttcoin;

import android.content.Context;
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

public class BitcoinExchangeFetcher extends AsyncTask<URL, Integer, ArrayList<Double>> {

    ResponseListener listener;

    public BitcoinExchangeFetcher(ResponseListener listener) {
        this.listener = listener;
    }

    @Override
    protected ArrayList<Double> doInBackground(URL... urls) {
        ArrayList<Double> result = null;
        try {
            JsonReader reader = getReaderFromURL(urls[0]);
            result = getPriceFromReader(reader);
        } catch (IOException e){

        }

        return result;
    }

    protected void onPostExecute(ArrayList<Double> result) {
        listener.respondToResult(result);
    }

    ArrayList<Double> getPriceFromReader(JsonReader reader) throws IOException {
        ArrayList<Double> values = new ArrayList<Double>();
        reader.beginObject();
        reader.nextName();
        reader.beginObject();
        while (reader.hasNext()) {
            if (!reader.nextName().equals("symbol")) {
                values.add(reader.nextDouble());
            } else {
                reader.nextString();
            }
        }
        return values;
    }

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


