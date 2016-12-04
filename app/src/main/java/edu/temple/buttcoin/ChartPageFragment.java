package edu.temple.buttcoin;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChartPageFragment extends Fragment {


    public static final String CHART = "355x4";

    public ChartPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_chart_page, container, false);
        if(getArguments()!=null) {
            ImageView image = (ImageView) v.findViewById(R.id.chartInfo);
            SQLiteDatabase db = new BitcoinDbHelper(getContext()).getWritableDatabase();
            Cursor c = db.query(BitcoinDbHelper.StocksContract.TABLE_NAME,
                    new String[]{BitcoinDbHelper.StocksContract.COLUMN_NAME_CHART1D},
                    null, null, null, null, null);
            c.moveToFirst();
            for(int i=0; i < getArguments().getInt(CHART); i++) c.moveToNext();
            image.setImageBitmap(BitmapFactory.decodeByteArray(c.getBlob(0), 0, c.getBlob(0).length));
            db.close();
        }
        return v;
    }

    private String getColumnName(int pos){
        String s = "";
        switch(pos){
            case 0:
                s = BitcoinDbHelper.StocksContract.COLUMN_NAME_CHART1D;
                break;
            case 1:
                s = BitcoinDbHelper.StocksContract.COLUMN_NAME_CHART1W;
                break;
            case 2:
                s = BitcoinDbHelper.StocksContract.COLUMN_NAME_CHART2W;
                break;
            case 3:
                s = BitcoinDbHelper.StocksContract.COLUMN_NAME_CHART1M;
                break;
        }
        return s;
    }

}
