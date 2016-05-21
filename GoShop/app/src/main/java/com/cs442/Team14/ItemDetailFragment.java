package com.cs442.Team14;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs442.Team14.dummy.CartFragmentStuff;
import com.cs442.Team14.dummy.FavFragmentStuff;
import com.cs442.Team14.dummy.MenuActivityStuff;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    private MenuActivityStuff.DummyItem mItem;

    private DBOperations dbOperations;

    public ItemDetailFragment() {
        // Required empty public constructor
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbOperations = new DBOperations(getActivity());

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            int id = Integer.parseInt(getArguments().getString(ARG_ITEM_ID));
            mItem = MenuActivityStuff.ITEM_MAP.get(id);

            /*Activity activity = this.getActivity();
            Toolbar appBarLayout = (Toolbar) activity.findViewById(R.id.detail_toolbar);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.name+" ($"+mItem.price+")");
            }*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        final EditText quantity = (EditText)rootView.findViewById(R.id.qnt);
        Button cart = (Button) rootView.findViewById(R.id.cartbtn);
        Button fav = (Button) rootView.findViewById(R.id.favbtn);

        if (mItem != null) {
            //((TextView) rootView.findViewById(R.id.headTitle)).setText(mItem.name+"($"+mItem.price+")");
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.details);
            ((ImageView)rootView.findViewById(R.id.image_view)).setImageResource(mItem.image);
        }

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qnt = quantity.getText().toString();
                int quantity = 0;
                if(!qnt.equals("")) {
                    quantity = Integer.parseInt(qnt);
                }
                if (quantity != 0) {
                    double subtotal = (double) Math.round(Double.parseDouble(qnt) * mItem.price * 100) / 100;
                    CartFragmentStuff.CartItem cItem = new CartFragmentStuff.CartItem(Integer.parseInt(qnt), mItem.name, subtotal, mItem.image);
                    if (CartFragmentStuff.ITEM_MAP.containsKey(mItem.name)) {
                        Toast.makeText(getActivity(), mItem.name + " "+getString(R.string.alreadycart), Toast.LENGTH_SHORT).show();
                    } else {
                        CartFragmentStuff.addItem(cItem);
                        dbOperations.insertData(GoShopApplicationData.CartTableInfo.TableName, cItem);
                        Toast.makeText(getActivity(), mItem.name + " "+getString(R.string.addedcart), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), getString(R.string.qntg0), Toast.LENGTH_SHORT).show();
                }
                }
        });

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavFragmentStuff.FavItem favItem = new FavFragmentStuff.FavItem(mItem.name, mItem.price, mItem.image);
                if(FavFragmentStuff.ITEM_MAP.containsKey(mItem.name)){
                    Toast.makeText(getActivity(), mItem.name+" "+getString(R.string.alreadyfav), Toast.LENGTH_SHORT).show();
                } else {
                    FavFragmentStuff.addItem(favItem);
                    dbOperations.insertData(GoShopApplicationData.FavTableInfo.TableName, favItem);
                    Toast.makeText(getActivity(), mItem.name+" "+getString(R.string.addedfav), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

}
