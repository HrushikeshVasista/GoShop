package com.cs442.Team14;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cs442.Team14.dummy.FavFragmentStuff;


/**
 * Displays the favorite items
 * @author Prashanth Molakala.
 */
public class FavoritesFragment extends Fragment {

    /**
     * Adapter for cart item list
     */
    private FavFragmentAdapter favItemListAdapter;

    private DBOperations dbOperations;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        getActivity().setTitle(R.string.title_fragment_favorites);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = null;
        if (FavFragmentStuff.ITEMS.size() == 0){
            view =  inflater.inflate(R.layout.empty_items, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_favorites, container, false);
            ListView listView = (ListView) view.findViewById(R.id.cartFragmentListNew);
            favItemListAdapter = new FavFragmentAdapter(getActivity(), FavFragmentStuff.ITEMS);
            listView.setAdapter(favItemListAdapter);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
