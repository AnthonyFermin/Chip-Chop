package madelyntav.c4q.nyc.chipchop.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import madelyntav.c4q.nyc.chipchop.R;

public class Fragment_Seller_Orders extends Fragment {

    OnHeadlineSelectedListener mCallback;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seller__orders, container, false);
    }


    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onSellerOrderSelected(int position);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnBuyerOrderSelectedListener");
        }
    }





}
