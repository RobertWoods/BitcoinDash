package edu.temple.buttcoin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlockPageFragment extends Fragment {
    public static String DATA = "Dr Morris Wears Nice Sweaters";

    public BlockPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_block_page, container, false);
        if(getArguments()!=null){
            String[] titles = getResources().getStringArray(R.array.block_titles);
            String[] data = getArguments().getStringArray(DATA);
            String s = "";
            for(int i=0;i<titles.length;i++){
                s = s + titles[i] + " " + data[i] + "\n";
            }
            ((TextView) v.findViewById(R.id.blockInfo)).setText(s);
        }
        return v;
    }

}
