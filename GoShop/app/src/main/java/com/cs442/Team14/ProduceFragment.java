package com.cs442.Team14;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.cs442.Team14.dummy.MenuActivityStuff;


/**
 * Displays the grocery item list as a tabbed view.
 * @author Prashanth Molakala
 */
public class ProduceFragment extends Fragment {

    public ProduceFragment() {}

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inventory_list, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridV);
        gridView.setAdapter(new ItemAdapter(getActivity(), MenuActivityStuff.PRODUCE_ITEMS));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getActivity(), "Displaying product "+ MenuActivityStuff.ITEMS.get(position+MenuActivityStuff.BAKERY_ITEMS.size()+MenuActivityStuff.BEVERAGE_ITEMS.size()).name, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getActivity(), ItemDetailActivity.class);
                    i.putExtra(ItemDetailFragment.ARG_ITEM_ID, ""+ MenuActivityStuff.ITEMS.get(position+MenuActivityStuff.BAKERY_ITEMS.size()+MenuActivityStuff.BEVERAGE_ITEMS.size()).id);
                    startActivity(i);
                }
            });
        return rootView;
    }


    @Override
    public void onResume() {
        getActivity().setTitle(R.string.title_activity_main);
        super.onResume();
    }
}
