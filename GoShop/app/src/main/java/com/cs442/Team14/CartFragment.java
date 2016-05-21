package com.cs442.Team14;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs442.Team14.dummy.CartFragmentStuff;

import java.util.List;

/**
 * This class implements the Cart page
 *
 * @author Hrushikesh Vasista
 * @since 20-Apr-2016
 */
public class CartFragment extends Fragment {


    /**
     * Adapter for cart item list
     */
    private CartFragmentAdapter cartItemListAdapter;

    /**
     * Instance of the calling activity
     */
    private OnCartFragmentInteractionListener mListener;

    /**
     * List of cart items
     */
    private ListView cartListView;

    /**
     * Check out button
     */
    private Button checkoutButton;

    private boolean isCartListUpdated = false;

    private TextView cartListAmount;

    private View layout1;
    private View layout2;

    private DBOperations dbOperations;

    public CartFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbOperations = new DBOperations(getActivity());
        if (getArguments() != null) {

        }
    }

    @Override
    public void onResume() {
        getActivity().setTitle(R.string.title_fragment_cart);

        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_cart_list, container, false);
        layout1 =  view.findViewById(R.id.cartFragmentLayout1);
        layout2 =  view.findViewById(R.id.cartFragmentLayout2);
        cartListView = (ListView) layout1.findViewById(R.id.cartFragmentListNew);
        cartListAmount = (TextView) layout1.findViewById(R.id.cartFragmentTotal);
        checkoutButton = (Button) layout1.findViewById(R.id.buttonCartFragChkout);

        cartListAmount.setText(""+CartFragmentStuff.getCartTotalAmount());
        cartItemListAdapter = new CartFragmentAdapter(getActivity(), CartFragmentStuff.ITEMS);
        cartListView.setAdapter(cartItemListAdapter);

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Goshop_PaymentActivity.class);
                startActivityForResult(intent, GoShopApplicationData.PAYMENT_SUCCESSFUL);
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCartFragmentInteractionListener) {
            mListener = (OnCartFragmentInteractionListener) context;
        } /*else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //If payment was successful reset cart item list, amount and go to home page
        //If payment remain in Cart page

        FragmentManager fragmentManager = getActivity().getFragmentManager();

        if(resultCode == GoShopApplicationData.PAYMENT_SUCCESSFUL){

            //Clear the DB for cart items
            CartFragmentStuff.ITEMS.clear();
            CartFragmentStuff.ITEM_MAP.clear();
            CartFragmentStuff.resetCartTotalAmount();
            cartItemListAdapter.notifyDataSetChanged();
            dbOperations.clearData(GoShopApplicationData.CartTableInfo.TableName);

            Toast.makeText(getActivity(), R.string.cartFragmentMsg1, Toast.LENGTH_SHORT).show();

            //Inform activity to display home page
            mListener.PaymentSuccessful(data.getIntExtra("OrderId", 0), data.getStringExtra("OrderItems"), data.getDoubleExtra("OrderAmount", 0));

        }
        else {

            //remain in Cart page
        }

    }

    private void manageLayouts(View toHide, View toShow){
        toHide.setVisibility(View.INVISIBLE);
        toShow.setVisibility(View.VISIBLE);

    }
    /**
     * Interface to allow interaction between this fragment and the calling activity
     * activity
     */
    public interface OnCartFragmentInteractionListener {

        /**
         * Indicates payment was succesful to the calling Activity.         *
         */
        public void PaymentSuccessful(int orderId, String orderItems, double orderAmt);

    }

    /**
     *Custom-adapter class to display list of cart items
     *
     * @author Hrushikesh Vasista
     * @since 19-Apr-2016
     */
    public class CartFragmentAdapter extends BaseAdapter {

        /**
         * Holds the context of the calling Activity
         */
        private Context mContext;

        /**
         * List of items to be displayed
         */
        private List<CartFragmentStuff.CartItem> mList;

        /**
         * Views of Cart items
         */
        private final int[] pagerViewLayouts = {R.layout.cart_item_normal, R.layout.cart_item_swiped};

        private TextView qtyInNormalView;
        private TextView qtyInSwipedView;

        public CartFragmentAdapter(Context context, List<CartFragmentStuff.CartItem> list){

            mContext = context;
            mList = list;

            if(mList.isEmpty()){
                manageLayouts(layout1, layout2);
            }
            else{
                manageLayouts(layout2, layout1);
            }
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

        private class ViewHolder{

            ViewPager vp;

            //Normal view
            TextView normalItemName;
            TextView normalQty;
            ImageView normalImage;
            TextView normalSubtotal;

            //Swiped view
            ImageView swipedInc;
            TextView swipedQty;
            ImageView swipedDec;
            ImageView swipedDelete;


        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View normal;
            View swiped;
            ViewPager viewPager;
            View cartItemView;

            ViewHolder holder = new ViewHolder();

            //If inflating for the first time...
            if(convertView == null){

                cartItemView = inflater.inflate(R.layout.cart_item, null, false);

                //Inflate and add the normal and swiped views here, not in instantiateItem()
                normal = inflater.inflate(pagerViewLayouts[0], null, false);
                swiped = inflater.inflate(pagerViewLayouts[1], null, false);

                viewPager = (ViewPager) cartItemView.findViewById(R.id.cartItemPager);
                viewPager.addView(normal);
                viewPager.addView(swiped);

                holder.vp = (ViewPager) cartItemView.findViewById(R.id.cartItemPager);
                holder.normalItemName = (TextView) normal.findViewById(R.id.cartItemName);
                holder.normalQty = (TextView) normal.findViewById(R.id.cartItemQty);
                holder.normalImage = (ImageView) normal.findViewById(R.id.cartImg);
                holder.normalSubtotal = (TextView) normal.findViewById(R.id.cartItemSubtotal);
                holder.swipedQty = (TextView) swiped.findViewById(R.id.cartItemQuantity);
                holder.swipedInc = (ImageView) swiped.findViewById(R.id.cartItemIncQty);
                holder.swipedDec = (ImageView) swiped.findViewById(R.id.cartItemDecQty);
                holder.swipedDelete = (ImageView) swiped.findViewById(R.id.cartItemDelete);

                cartItemView.setTag(holder);

                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter();
                viewPager.setAdapter(viewPagerAdapter);
            }
            else{

                cartItemView = convertView;
                holder = (ViewHolder) convertView.getTag();
                holder.vp.setCurrentItem(0, false);
                normal = holder.vp.getChildAt(0);
                swiped = holder.vp.getChildAt(1);

            }

            //Reading from DB
            CartFragmentStuff.CartItem cartItem = mList.get(position);

            //Display cart item details as in DB
            holder.normalQty.setText("" + cartItem.qty);
            holder.swipedQty.setText("" + cartItem.qty);
            holder.normalItemName.setText(cartItem.name);
            holder.normalSubtotal.setText("$" + cartItem.subtotal);
            holder.normalImage.setImageResource(cartItem.image);

/*
            //Until changed, display the quantity in swiped view as per DB
            holder.swipedQty.setText(cartItem.qty + "");
*/

            normal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, R.string.cartFragmentMsg6, Toast.LENGTH_SHORT).show();
                }
            });

            final ViewHolder finalHolder = holder;
            holder.swipedInc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Get item from adater list
                    CartFragmentStuff.CartItem cartItem = CartFragmentStuff.ITEMS.get(position);

                    double perItemCost = Math.round((cartItem.subtotal / cartItem.qty)*100)/100;
                    CartFragmentStuff.updateCartTotalAmount(perItemCost, true);

                    cartItem.subtotal = (double) Math.round((perItemCost + cartItem.subtotal) * 100) / 100;
                    int newQty = (++cartItem.qty);

                    finalHolder.swipedQty.setText(("" + newQty));
                    finalHolder.normalQty.setText(("" + newQty));
                    finalHolder.normalSubtotal.setText(("$" + cartItem.subtotal));

                    cartListAmount.setText("" + CartFragmentStuff.getCartTotalAmount());
                    dbOperations.updateCart(cartItem);
                    isCartListUpdated = true;

                }
            });


            holder.swipedDec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mList.get(position).qty>1){

                        CartFragmentStuff.CartItem cartItem = CartFragmentStuff.ITEMS.get(position);

                        double perItemCost = Math.round((cartItem.subtotal / cartItem.qty)*100)/100;
                        CartFragmentStuff.updateCartTotalAmount(perItemCost, false);

                        cartItem.subtotal = (double) Math.round((cartItem.subtotal - perItemCost) * 100) / 100;
                        int newQty = (--cartItem.qty);

                        finalHolder.swipedQty.setText(("" + newQty));
                        finalHolder.normalQty.setText(("" + newQty));
                        finalHolder.normalSubtotal.setText(("$" + cartItem.subtotal));

                        cartListAmount.setText("" + CartFragmentStuff.getCartTotalAmount());

                        dbOperations.updateCart(cartItem);
                        isCartListUpdated = true;


                    }
                    else{
                        Toast.makeText(mContext, "Click on delete button to remove", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            holder.swipedDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(mContext, mList.get(position).name + " " + getString(R.string.cartFragmentMsg4), Toast.LENGTH_SHORT).show();

                    CartFragmentStuff.updateCartTotalAmount(CartFragmentStuff.ITEMS.get(position).subtotal, false);
                    dbOperations.deleteData(GoShopApplicationData.CartTableInfo.TableName, CartFragmentStuff.ITEMS.get(position));
                    CartFragmentStuff.ITEM_MAP.remove(CartFragmentStuff.ITEMS.get(position).name);
                    CartFragmentStuff.ITEMS.remove(position);
                    mList = CartFragmentStuff.ITEMS;
                    cartListAmount.setText("" + CartFragmentStuff.getCartTotalAmount());
                    notifyDataSetChanged();

                    //If no items in cart, hide all fields and display a message
                    if (mList.isEmpty()) {
                        manageLayouts(layout1, layout2);
                    }
                }
            });

            return cartItemView;
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

}
