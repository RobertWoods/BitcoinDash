package edu.temple.buttcoin;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment implements ResponseListener<Drawable>, SetupListener {

    private final String API_URL = "https://chart.yahoo.com/z?s=BTCUSD=X&t=";
    FragmentStatePagerAdapter adapter;

    public ChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chart, container, false);
        adapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                ChartPageFragment chartPageFragment = new ChartPageFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(ChartPageFragment.CHART, position);
                chartPageFragment.setArguments(bundle);
                return chartPageFragment;
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
        ((ViewPager) v.findViewById(R.id.chartGraph)).setAdapter(adapter);
        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        getCharts();
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

    public void getCharts(){
        BitcoinDbHelper dbHelper = new BitcoinDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db.query(BitcoinDbHelper.StocksContract.TABLE_NAME,
                new String[] {BitcoinDbHelper.StocksContract.COLUMN_NAME_CHART1D},
                null, null, null, null, null).getCount() == 0){
            for(String s : new String[]{"1d", "7d", "14d", "30d"}){
                startChartFetcher(s);
            }
        }
    }

    @Override
    public void respondToResult(Drawable result) {
        BitcoinDbHelper dbHelper = new BitcoinDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BitcoinDbHelper.StocksContract.COLUMN_NAME_CHART1D,
                getBlobFromDrawable(result));
        db.insert(BitcoinDbHelper.StocksContract.TABLE_NAME, null, values);
        db.close();
    }

    private byte[] getBlobFromDrawable(Drawable image) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ((BitmapDrawable) image).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, out);
        return out.toByteArray();
    }

    private void firstTimeSetup(){

    }

    @Override
    public void respondToSetup() {

    }

    private class SetupTask extends AsyncTask<Void, Void, Void> {
        SetupListener listener;

        public SetupTask(SetupListener listener){
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            listener.respondToSetup();
        }
    }

}
