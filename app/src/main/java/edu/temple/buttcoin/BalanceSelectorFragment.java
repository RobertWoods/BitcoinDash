package edu.temple.buttcoin;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class BalanceSelectorFragment extends Fragment {

    SelectionListener listener;
    SQLiteDatabase db;
    HashMap<String, String> wallets;

    public BalanceSelectorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_balance_selector, container, false);
        BitcoinDbHelper dbHelper = new BitcoinDbHelper(getContext());
        db = dbHelper.getWritableDatabase();
        wallets = getWallets();
        insertNewWallet("", "");
        insertNewWallet("12", "1F1tAaz5x1HUXrCNLbtMDqcw6o5GNn4xqX");
        wallets.keySet();
        final BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return wallets.size()+1;
            }

            @Override
            public Object getItem(int i) {
                if(i == 0) return "Please Select a meme";
                return wallets.keySet().toArray()[i-1];
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view != null) {
                    //TODO get view reuse working here
                }
//                    if (((LinearLayout) view).getChildAt(0) != null) {
//                        ((TextView) ((LinearLayout) view).getChildAt(0))
//                                .setText(getItem(i).toString());
//                    }
//                } else {
                    view = new LinearLayout(getContext());
//                }
                TextView textView = new TextView(getContext());
                textView.setText(getItem(i).toString());
                ((LinearLayout) view).addView(textView);
                return view;
            }
        };
        ((Spinner) v.findViewById(R.id.walletSelector)).setAdapter(adapter);
        ((Spinner) v.findViewById(R.id.walletSelector)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0)
                    listener.showWalletAddress(adapter.getItem(i).toString(), wallets.get(adapter.getItem(i)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            //TODO change MainActivity to implement interface, because this attaches to the Activity not the fragment
            listener = (SelectionListener) context;
        } catch (ClassCastException e) {
            Log.d("really", "makes you think:" + context.getClass());
        }
    }

    private void insertNewWallet(String balance, String walletId) {
        if (!wallets.containsKey(walletId)) {
            ContentValues values = new ContentValues();
            values.put(BitcoinDbHelper.COLUMN_NAME_BALANCE, balance);
            values.put(BitcoinDbHelper.COLUMN_NAME_NUMBER, walletId);
            db.insert(BitcoinDbHelper.TABLE_NAME, null, values);
            wallets.put(walletId, balance);
        }
    }

    private HashMap<String, String> getWallets() {
        HashMap<String, String> wallets = new HashMap<>();
        Cursor c = db.query(
                BitcoinDbHelper.TABLE_NAME,
                new String[]{BitcoinDbHelper.COLUMN_NAME_NUMBER, BitcoinDbHelper.COLUMN_NAME_BALANCE},
                null, null, null, null, null
        );
        c.moveToFirst();
        while (c.moveToNext()) {
            wallets.put(c.getString(c.getColumnIndex(BitcoinDbHelper.COLUMN_NAME_NUMBER)),
                    c.getString(c.getColumnIndex(BitcoinDbHelper.COLUMN_NAME_BALANCE)));
        }
        return wallets;
    }

    public interface SelectionListener {
        void showWalletAddress(String walletId, String balance);
    }

}
