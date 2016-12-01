package edu.temple.buttcoin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements BalanceSelectorFragment.SelectionListener {

    String[] fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragments = new String[]{
                "View Block Info",
                "View Wallet Balance",
                "View Exchange Rates",
                "View Price Chart",
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fragments);
        ListView lv = (ListView) findViewById(R.id.drawer);
        DrawerLayout dl = (DrawerLayout) findViewById(R.id.activity_main);
        lv.setAdapter(adapter);
        ActionBarDrawerToggle abdt = new ActionBarDrawerToggle(this, dl, R.drawable.ic_drawer, R.string.go_button, R.string.hello_blank_fragment);
        dl.addDrawerListener(abdt);

    }


    @Override
    public void showWalletAddress(String walletId, String balance) {
//        ((BalanceFragment)fragments[1]).showWalletAddress(walletId, balance);
    }
}
