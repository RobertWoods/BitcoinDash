package edu.temple.buttcoin;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ResponseListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BitcoinExchangeFetcher fetcher = new BitcoinExchangeFetcher(this);
        try {
            fetcher.execute(new URL("https://blockchain.info/ticker"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BitcoinChartFetcher chartFetcher = new BitcoinChartFetcher(this);
        try {
            chartFetcher.execute(new URL("https://chart.yahoo.com/z?s=BTCUSD=X&t=1d"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BitcoinBlockFetcher blockFetcher = new BitcoinBlockFetcher(this);
        try {
            blockFetcher.execute(new URL("http://btc.blockr.io/api/v1/block/info/223213,223214,223215,223216,223217"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BitcoinBalanceFetcher balanceFetcher = new BitcoinBalanceFetcher(this);
        try {
            balanceFetcher.execute(new URL("http://btc.blockr.io/api/v1/address/balance/1F1tAaz5x1HUXrCNLbtMDqcw6o5GNn4xqX"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

//        BarcodeDetector detector = new BarcodeDetector.Builder(getApplicationContext())
//                .setBarcodeFormats(Barcode.QR_CODE).build();
//        Barcode QRs have raw string format: 'bitcoin:<address>'
//        Barcode
    }

    @Override
    public void respondToResult(ArrayList<Double> result) {
        String s = "";
        String[] titles = getResources().getStringArray(R.array.ticker_titles);
        for (int i = 0; i < result.size(); i++) {
            s = s + titles[i];
            s = s + result.get(i) + "\n";
        }
        ((TextView) findViewById(R.id.textCatcher)).setText(s);
    }

    @Override
    public void respondToResult(Drawable result) {
        ((ImageView) findViewById(R.id.imageView)).setImageDrawable(result);
    }

    @Override
    public void respondToResult(String[][] result) {
        String[] titles = getResources().getStringArray(R.array.block_titles);
        ((TextView) findViewById(R.id.textCatcher)).setText(result[0][16]);
    }

    @Override
    public void respondToResult(String result) {
        ((TextView) findViewById(R.id.textCatcher)).setText(result);
    }
}
