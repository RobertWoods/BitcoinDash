package edu.temple.buttcoin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class BalanceFragment extends Fragment {


    private final int GET_QR_IMAGE = 1623;
    private boolean twoPanes = false;

    public BalanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_wallet, container, false);


        twoPanes = v.findViewById(R.id.detailsPane) != null;
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction()
                .replace(R.id.selectFrame, new BalanceSelectorFragment());
        if(twoPanes) ft = ft.add(R.id.detailsPane, new Fragment());
        ft.addToBackStack(null).commit();
        return v;
    }


    public void showWalletAddress(String walletId, String balance) {
        BalanceViewerFragment bvFrag = new BalanceViewerFragment();
        int layoutId = twoPanes ? R.id.detailsPane : R.id.selectFrame;
        getChildFragmentManager()
                .beginTransaction()
                .replace(layoutId, bvFrag)
                .addToBackStack(null)
                .commit();
        getChildFragmentManager().executePendingTransactions();
        bvFrag.showWalletAddress(walletId, balance);
    }

    public void removeFromBackStack() {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.selectFrame, new BalanceSelectorFragment())
                .commit();
    }

}
