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
    BaseAdapter adapter;

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
//        insertNewWallet(12, "1JEiV9CiJmhfYhE7MzeSdmH82xRYrbYrtb");
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return wallets.size()+2;
            }

            @Override
            public Object getItem(int i) {
                if(i == 0) return getResources().getString(R.string.select_option);
                if(i == 1) return getResources().getString(R.string.get_new_wallet);
                return wallets.keySet().toArray()[i-2];
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
                String walletId = i < 2 ? "" : adapter.getItem(i).toString();
                String balance  = i < 2 ? "" : wallets.get(wallets.keySet().toArray()[i - 2]);
                if(i!=0)
                    listener.showWalletAddress(walletId, balance);
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
            listener = (SelectionListener) context;
        } catch (ClassCastException e) {
            Log.d("really", "makes you think:" + context.getClass());
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        wallets = getWallets();
        adapter.notifyDataSetChanged();
    }

    private void insertNewWallet(int balance, String walletId) {
            if (!wallets.containsKey(walletId)) {
            ContentValues values = new ContentValues();
            values.put(BitcoinDbHelper.AddressesContract.COLUMN_NAME_BALANCE, balance);
            values.put(BitcoinDbHelper.AddressesContract.COLUMN_NAME_NUMBER, walletId);
            db.insertOrThrow(BitcoinDbHelper.AddressesContract.TABLE_NAME, null, values);
            wallets.put(walletId, String.valueOf(balance));
        }
    }

    private HashMap<String, String> getWallets() {
        HashMap<String, String> wallets = new HashMap<>();
        Cursor c = db.query(
                BitcoinDbHelper.AddressesContract.TABLE_NAME,
                new String[]{BitcoinDbHelper.AddressesContract.COLUMN_NAME_NUMBER,
                        BitcoinDbHelper.AddressesContract.COLUMN_NAME_BALANCE},
                null, null, null, null, null
        );
        if(c.getCount()==0) return wallets;
        c.moveToFirst();
        do  {
            wallets.put(c.getString(c.getColumnIndex(
                    BitcoinDbHelper.AddressesContract.COLUMN_NAME_NUMBER)),
                    c.getString(c.getColumnIndex(
                            BitcoinDbHelper.AddressesContract.COLUMN_NAME_BALANCE)));
        } while (c.moveToNext());

        return wallets;
    }

    public interface SelectionListener {
        void showWalletAddress(String walletId, String balance);
    }

}
