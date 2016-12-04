package edu.temple.buttcoin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements BalanceSelectorFragment.SelectionListener, BalanceViewerFragment.BalanceViewerListener {

    ActionBarDrawerToggle drawerToggle;
    DrawerLayout dl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] fragments = getResources().getStringArray(R.array.fragment_descriptors);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fragments);
        ListView lv = (ListView) findViewById(R.id.drawer);
        dl = (DrawerLayout) findViewById(R.id.drawerLayout);
        lv.setAdapter(adapter);
        drawerToggle = new ActionBarDrawerToggle(this, dl, toolbar, R.string.go_button, R.string.hello_blank_fragment);
        dl.addDrawerListener(drawerToggle);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fragment fragToLoad = null;
                switch(i){
                    case 0:
                        fragToLoad = new BlockFragment();
                        break;
                    case 1:
                        fragToLoad = new BalanceFragment();
                        break;
                    case 2:
                        fragToLoad = new ExchangeFragment();
                        break;
                    case 3:
                        fragToLoad = new ChartFragment();
                        break;
                    default:
                        return;
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.screenFrame, fragToLoad)
                        .commit();
                dl.closeDrawers();
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.screenFrame, new BlockFragment()).commit();
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void showWalletAddress(String walletId, String balance) {
        try {
            ((BalanceFragment) getSupportFragmentManager().findFragmentById(R.id.screenFrame))
                    .showWalletAddress(walletId, balance);
        } catch (Exception e) {} //TODO make it catch actual exception
    }

    @Override
    public void removeFromBackStack() {
        try {
            ((BalanceFragment) getSupportFragmentManager().findFragmentById(R.id.screenFrame))
                    .removeFromBackStack();
        } catch (Exception e) {} //TODO make it catch actual exception
    }

}
