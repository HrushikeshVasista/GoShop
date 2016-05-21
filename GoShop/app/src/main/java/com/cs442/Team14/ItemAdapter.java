package com.cs442.Team14;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs442.Team14.dummy.MenuActivityStuff;

import java.util.ArrayList;

/**
 * Adapter for displaying the grocery items
 * @author Prashanth Molakala
 */
public class ItemAdapter extends BaseAdapter {
    Context CTX;
    ArrayList<MenuActivityStuff.DummyItem> mItems = new ArrayList<>();
    //int start;

    public ItemAdapter(Context ctx, ArrayList<MenuActivityStuff.DummyItem> mItems){
        this.CTX = ctx;
        this.mItems = mItems;
    }

    /*public ItemAdapter(Context ctx, int start){
        this.CTX = ctx;
        //this.mItems = mItems;
        this.start = start;
    }*/

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder{
        ImageView item_imgv;
        TextView item_namev;
        TextView item_pricev;
        ViewHolder(View v){
            item_imgv = (ImageView) v.findViewById(R.id.itm_img);
            item_namev = (TextView) v.findViewById(R.id.itm_name);
            item_pricev = (TextView) v.findViewById(R.id.itm_price);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        if(row == null) {
            LayoutInflater inflater = (LayoutInflater) CTX.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_item, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        holder.item_imgv.setImageResource(mItems.get(position).image);
        holder.item_namev.setText(mItems.get(position).name);
        holder.item_pricev.setText("$"+ mItems.get(position).price+"");
        return row;
    }
}
