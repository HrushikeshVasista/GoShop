package com.cs442.Team14;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs442.Team14.dummy.FavFragmentStuff;

import java.util.List;

/**
 * Adapter for displaying the favorites items
 * @author Prashanth Molakala
 */
public class FavFragmentAdapter extends BaseAdapter {

    /**
     * Holds the context of the calling Activity
     */
    private Context mContext;

    /**
     * List of items to be displayed
     */
    private List<FavFragmentStuff.FavItem> mList;

    private DBOperations dbOperations;

    /**
     * Views of Cart items
     */
    private final int[] pagerViewLayouts = {R.layout.favorites_item_normal, R.layout.favorits_item_swiped};

    public FavFragmentAdapter(Context context, List<FavFragmentStuff.FavItem> list){

        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View normal;
        View swiped;
        ViewPager viewPager;
        View favItemView;

        dbOperations = new DBOperations(mContext);

        //If inflating for the first time...
        if(convertView == null){

            favItemView = inflater.inflate(R.layout.favorites_item, null, false);
            //Inflate and add the normal and swiped views here, not in instantiateItem()
            normal = inflater.inflate(pagerViewLayouts[0], null, false);
            swiped = inflater.inflate(pagerViewLayouts[1], null, false);
            viewPager = (ViewPager) favItemView.findViewById(R.id.cartItemPager);
            viewPager.addView(normal);
            viewPager.addView(swiped);
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter();
            viewPager.setAdapter(viewPagerAdapter);
        }
        else{
            favItemView = convertView;
            viewPager = (ViewPager) favItemView.findViewById(R.id.cartItemPager);
            viewPager.setCurrentItem(0,false);
            normal = ((ViewPager)favItemView.findViewById(R.id.cartItemPager)).getChildAt(0);
            swiped = ((ViewPager)favItemView.findViewById(R.id.cartItemPager)).getChildAt(1);
        }

        TextView posV = (TextView) normal.findViewById(R.id.favPosition);
        TextView nameView = (TextView) normal.findViewById(R.id.cartItemName);
        TextView priceV = (TextView) normal.findViewById(R.id.favPrice);
        ImageView favImg = (ImageView) normal.findViewById(R.id.favImage);

        FavFragmentStuff.FavItem favItem = new FavFragmentStuff.FavItem(mList.get(position));
        nameView.setText(favItem.name);
        posV.setText((position+1) + ". ");
        priceV.setText("$" + favItem.price);
        favImg.setImageResource(favItem.image);

        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, ((TextView) v.findViewById(R.id.cartItemName)).getText() +
                        " selected", Toast.LENGTH_SHORT).show();
            }
        });

        swiped.findViewById(R.id.cartItemDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, FavFragmentStuff.ITEMS.get(position).name+ " removed from favorites", Toast.LENGTH_SHORT).show();
                dbOperations.deleteData(GoShopApplicationData.FavTableInfo.TableName, FavFragmentStuff.ITEMS.get(position));
                FavFragmentStuff.ITEM_MAP.remove(FavFragmentStuff.ITEMS.get(position).name);
                FavFragmentStuff.ITEMS.remove(position);
                mList = FavFragmentStuff.ITEMS;
                notifyDataSetChanged();
            }
        });

        return favItemView;
    }

    /**
     * Custom adapter for cart item normal and swipe views
     *
     * @author Hrushikesh Vasista
     * @since 20-Apr-2016
     */
    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return container.getChildAt(position);
        }

        @Override
        public int getCount() {
            return pagerViewLayouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
