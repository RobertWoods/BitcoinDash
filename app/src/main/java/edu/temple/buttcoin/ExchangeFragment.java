package edu.temple.buttcoin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangeFragment extends Fragment implements ResponseListener<ArrayList<Double>> {

    private final String API_URL = "https://blockchain.info/ticker";

    public ExchangeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_exchange, container, false);
        return v;
    }
    @Override
    public void onResume(){
        super.onResume();
        startExchangeFetcher();
    }

    void startExchangeFetcher() {
        Fetcher<ArrayList<Double>> fetcher = new Fetcher<ArrayList<Double>>(this) {
            @Override
            protected ArrayList<Double> getDataFromReader(JsonReader reader) throws IOException {
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
        };
        try {
            fetcher.execute(new URL(API_URL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void respondToResult(ArrayList<Double> result) {
        String s = "";
        int i = 0;
        String[] titles = getResources().getStringArray(R.array.ticker_titles);
        if(result == null || result.size() == 0) {
            ((ViewGroup) getView()).removeView(getView().findViewById(R.id.exchangeInfo));
            return;
        }
        for(Double d : result)  s+=titles[i++]+d+"\n";
        ((TextView) getView().findViewById(R.id.exchangeInfo)).setText(s);
    }
}
