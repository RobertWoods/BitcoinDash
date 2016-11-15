package edu.temple.buttcoin;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rober_000 on 11/13/2016.
 */

public class BitcoinBlockFetcher extends AsyncTask<URL, Void, String[][]> {

    ResponseListener listener;

    public BitcoinBlockFetcher(ResponseListener listener){
        this.listener = listener;
    }

    @Override
    protected String[][] doInBackground(URL... urls) {
        try {
            return getBlockInformationFromReader(getReaderFromURL(urls[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String[][] result) {
        listener.respondToResult(result);
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

    String[][] getBlockInformationFromReader(JsonReader reader) throws IOException {
        String[][] data = new String[5][17];
        reader.beginObject();
        reader.nextName();
        if(!reader.nextString().equals("success"))
            return null;
        reader.nextName();
        reader.beginArray();
        for(int i=0; i<data.length; i++){
            reader.beginObject();
            int j = 0;
            while(reader.hasNext()){
                reader.nextName();
                if(reader.peek()!= JsonToken.NULL) {
                    data[i][j] = reader.nextString();
                } else {
                    data[i][j] = "None";
                    reader.skipValue();
                }
                j++;
            }
            reader.endObject();
        }
        reader.close();
        return data;
    }

}

