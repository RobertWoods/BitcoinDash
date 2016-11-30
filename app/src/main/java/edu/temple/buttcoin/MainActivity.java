package edu.temple.buttcoin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Fragment[] fragments = new Fragment[]{
                new BlockFragment(),
                new BalanceFragment(),
                new ExchangeFragment(),
                new ChartFragment(),
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

    }


}
