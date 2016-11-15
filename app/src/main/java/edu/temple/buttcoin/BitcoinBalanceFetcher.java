package edu.temple.buttcoin;

/**
 * Created by rober_000 on 11/14/2016.
 */

        import android.os.AsyncTask;
        import android.util.JsonReader;

        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.URL;


public class BitcoinBalanceFetcher extends AsyncTask<URL, Void, String> {

    ResponseListener listener;

    //http://btc.blockr.io/api/v1/address/balance/<address#>,<address#>

    public BitcoinBalanceFetcher(ResponseListener listener){
        this.listener = listener;
    }

    @Override
    public String doInBackground(URL... urls){
        String result = null;
        try {
            JsonReader reader = getReaderFromURL(urls[0]);
            result = getBalanceFromReader(reader);
            reader.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onPostExecute(String result){
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

    String getBalanceFromReader(JsonReader reader) throws IOException {
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

}
