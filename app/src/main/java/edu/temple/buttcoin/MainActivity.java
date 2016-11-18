package edu.temple.buttcoin;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Fragment[] fragments = new Fragment[]{
                new BlockFragment(),
                new BalanceFragment(),
                new ExchangeFragment(),
                new ChartFragment()
        };
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.length;
            }

            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }
        };
        ViewPager vp = (ViewPager) findViewById(R.id.screenFrame);
        vp.setAdapter(adapter);

//        BarcodeDetector detector = new BarcodeDetector.Builder(getApplicationContext())
//                .setBarcodeFormats(Barcode.QR_CODE).build();
//        Barcode QRs have raw string format: 'bitcoin:<address>'
//        Barcode
    }




}
