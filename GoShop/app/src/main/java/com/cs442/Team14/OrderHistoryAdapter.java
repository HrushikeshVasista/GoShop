package com.cs442.Team14;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cs442.Team14.dummy.OrderHistoryStuff;

/**
 * Created by Prashanth Molakala on 5/1/2016.
 */
public class OrderHistoryAdapter extends BaseAdapter {

    private Context ctx;
    private Button clearHistoryButton;
    public OrderHistoryAdapter(Context ctx){
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return OrderHistoryStuff.ITEMS.size();
    }

    @Override
    public Object getItem(int position) {
        return OrderHistoryStuff.ITEMS.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder{
        TextView order_id;
        TextView items;
        TextView total;
        TextView date;
        ViewHolder(View v){
            order_id = (TextView) v.findViewById(R.id.orderId);
            items = (TextView) v.findViewById(R.id.items);
            total = (TextView) v.findViewById(R.id.total);
            date = (TextView) v.findViewById(R.id.date);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        if(row == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.order_history_item, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.order_id.setText(OrderHistoryStuff.ITEMS.get(position).oid+". ");
        holder.items.setText(OrderHistoryStuff.ITEMS.get(position).items);
        holder.total.setText("$"+ OrderHistoryStuff.ITEMS.get(position).total);
        holder.date.setText(OrderHistoryStuff.ITEMS.get(position).date);
        return row;
    }
}
