package com.cs442.Team14;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.cs442.Team14.dummy.OrderHistoryStuff;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderHistoryFragment extends Fragment {

    private OrderHistoryAdapter orderHistoryAdapter;

    public OrderHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        ListView listView = (ListView) view.findViewById(R.id.myOrdersLst);
        orderHistoryAdapter = new OrderHistoryAdapter(getActivity());
        listView.setAdapter(orderHistoryAdapter);

        view.findViewById(R.id.orderHistoryClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBOperations operations = new DBOperations(getActivity());
                operations.clearData(GoShopApplicationData.OrderHistoryTableInfo.TableName);
                OrderHistoryStuff.ITEMS.clear();
                orderHistoryAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), R.string.orderHistoryFragmentMsg1, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        getActivity().setTitle(R.string.title_fragment_orderHistory);
        super.onResume();
    }
}
