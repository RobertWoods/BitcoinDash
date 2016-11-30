package edu.temple.buttcoin;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class BalanceViewerFragment extends Fragment implements ResponseListener<String>{

    private final int GET_QR_IMAGE = 1314;
    private final String API_URL = "http://btc.blockr.io/api/v1/address/balance/";


    public BalanceViewerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_balance_viewer, container, false);

        v.findViewById(R.id.getWalletButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String walletId = ((EditText) getView().findViewById(R.id.walletInput)).getText().toString();
                startWalletFetcher(walletId);
            }
        });

        v.findViewById(R.id.getWalletFromQRButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext())
                        != ConnectionResult.SUCCESS){
                    Toast.makeText(getContext(), "Play Services is not available :("
                            , Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, GET_QR_IMAGE);
            }
        });

        return v;
    }

    public void startWalletFetcher(String walletId){

        Fetcher<String> fetcher = new Fetcher<String>(this) {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GET_QR_IMAGE && resultCode == Activity.RESULT_OK){
            Bitmap qr = (Bitmap) data.getExtras().get("data");
            ((ImageView) getView().findViewById(R.id.imageView)).setImageBitmap(qr);
            getRawFromQR(qr);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    void getRawFromQR(Bitmap qr){
        BarcodeDetector detector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS).build();
        if(!detector.isOperational()) {
            Toast.makeText(getContext(), "Something went wrong with the detector :(", Toast.LENGTH_SHORT).show();
            return;
        }

        Frame frame = new Frame.Builder().setBitmap(qr).build();

        SparseArray<Barcode> a = detector.detect(frame);
        EditText t = ((EditText) getView().findViewById(R.id.walletInput));
        if(a.size() < 1){
            Toast.makeText(getContext(), "Something went wrong with the detector 8(", Toast.LENGTH_SHORT).show();
            return;
        }
        Barcode b = a.valueAt(0);
        t.setText(b.rawValue);
    }

    @Override
    public void respondToResult(String result) {
        ((TextView) getView().findViewById(R.id.walletInfo)).setText("Wallet balance: " + result + " BTC");
    }

    public void showWalletAddress(String walletId, String balance) {
        ((EditText) getView().findViewById(R.id.walletInput)).setText(walletId);
        ((TextView) getView().findViewById(R.id.walletInfo)).setText("Wallet balance: " + balance + " BTC");
    }
}
