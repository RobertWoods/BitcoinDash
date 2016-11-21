package edu.temple.buttcoin;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment implements ResponseListener<Drawable> {

    private final String API_URL = "https://chart.yahoo.com/z?s=BTCUSD=X&t=";

    public ChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chart, container, false);
        v.findViewById(R.id.getChartButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chartRange = ((EditText) getView().findViewById(R.id.chartinput)).getText()
                        .toString();
                startChartFetcher(chartRange);
            }
        });
        return v;
    }

    private void startChartFetcher(String chartRange) {

        Fetcher<Drawable> fetcher = new Fetcher<Drawable>(this) {
            @Override
            protected Drawable getDataFromReader(JsonReader reader) throws IOException {
                return null;
            }

            @Override
            protected Drawable doInBackground(URL... urls) {
                try {
                    return Drawable.createFromStream((InputStream) urls[0].getContent(), "");
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
        try {
            fetcher.execute(new URL(API_URL + chartRange));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void respondToResult(Drawable result) {
        ((ImageView) getView().findViewById(R.id.chartInfo)).setImageDrawable(result);
    }
}
