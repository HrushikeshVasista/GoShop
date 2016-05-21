package com.cs442.Team14;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Displays the home page of the app.
 * @author Hrushikesh Vasista, Prashanth Molakala
 */
public class HomeFragment extends Fragment {

    private FragmentTabHost tabHost;

    private final int tabs[] = {R.layout.fragment_inventory_list, R.layout.fragment_inventory_list, R.layout.fragment_inventory_list};

    private int tabnames[] = {R.string.cartFragmentTab1, R.string.cartFragmentTab2, R.string.cartFragmentTab3};
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.homeFragmentPager);

        ItemListPagerAdapter adapter = new ItemListPagerAdapter(getFragmentManager());
        SlidingTabLayout mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);

        viewPager.setAdapter(adapter);

        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setCustomTabView(R.layout.tab_text_view, R.id.tabTextViewId);
        mSlidingTabLayout.setViewPager(viewPager);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.title_activity_main);
    }


    class ItemListPagerAdapter extends FragmentStatePagerAdapter {

        public ItemListPagerAdapter(FragmentManager fm) {
            super(getChildFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch(position){
                case 0:
                    fragment = new BakeryFragment();
                    break;
                case 1:
                    fragment = new BeveragesFragment();
                    break;
                case 2:
                    fragment = new ProduceFragment();
                    break;
            }
            return fragment;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return getString(tabnames[position]);
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

    }
}
