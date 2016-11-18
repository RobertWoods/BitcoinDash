package edu.temple.buttcoin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class BalanceFragment extends Fragment implements ResponseListener<String> {


    private final String API_URL = "http://btc.blockr.io/api/v1/address/balance/";

    public BalanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_wallet, container, false);
        v.findViewById(R.id.getWalletButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String walletId = ((EditText) getView().findViewById(R.id.walletInput)).getText().toString();
                startWalletFetcher(walletId);
            }
        });
        return v;
    }

    public void startWalletFetcher(String walletId){

        Fetcher<Void, String> fetcher = new Fetcher<Void, String>(this) {
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
            fetcher.execute(new URL(API_URL+walletId));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void respondToResult(String result) {
        ((TextView) getView().findViewById(R.id.walletInfo)).setText("Wallet balance: " + result);
    }
}
