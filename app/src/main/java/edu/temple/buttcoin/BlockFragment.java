package edu.temple.buttcoin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.JsonReader;
import android.util.JsonToken;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlockFragment extends Fragment implements ResponseListener<String[][]> {

    private static String API_URL = "http://btc.blockr.io/api/v1/block/info/";
    private String[][] currentBlocks = new String[0][0];
    private FragmentStatePagerAdapter pagerAdapter;
    private ViewPager pager;

    public BlockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_block, container, false);
        pager = (ViewPager) v.findViewById(R.id.blockViewer);
        pagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()){
            @Override
            public Fragment getItem(int position) {
                BlockPageFragment blockPageFragment = new BlockPageFragment();
                Bundle bundle = new Bundle();
                bundle.putStringArray(BlockPageFragment.DATA, currentBlocks[position]);
                blockPageFragment.setArguments(bundle);
                return blockPageFragment;
            }

            @Override
            public int getCount() {
                return currentBlocks.length;
            }
        };
        pager.setAdapter(pagerAdapter);
        v.findViewById(R.id.getBlockButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String blockId = ((EditText) getView().findViewById(R.id.blockInput)).getText().toString();
                startBlockFetcher(blockId);
            }
        });
        
        return v;
    }

    private void startBlockFetcher(String blockId) {
        String s = "";
        int id;
        try {
            id = Integer.parseInt(blockId);
        } catch (NumberFormatException e){
            return;
        }
        for(int i=-2;i<=2;i++){
            s+=id+i+",";
        }
        s = s.substring(0, s.length()-1);

        Fetcher<String[][]> fetcher = new Fetcher<String[][]>(this) {

            protected String[][] getDataFromReader(JsonReader reader) throws IOException {
                String[][] data = new String[5][17];
                reader.beginObject();
                reader.nextName();
                if(!reader.nextString().equals("success"))
                    return null;
                reader.nextName();
                reader.beginArray();
                for(int i=0; i<data.length; i++){
                    reader.beginObject();
                    int j = 0;
                    while(reader.hasNext()){
                        reader.nextName();
                        if(reader.peek()!= JsonToken.NULL) {
                            data[i][j] = reader.nextString();
                        } else {
                            data[i][j] = "None";
                            reader.skipValue();
                        }
                        j++;
                    }
                    reader.endObject();
                }
                reader.close();
                return data;
            }
        };
        try {
            fetcher.execute(new URL(API_URL+s));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void respondToResult(String[][] result) {
        currentBlocks = result;
        pagerAdapter.notifyDataSetChanged();
        pager.setCurrentItem(2);
    }

}
