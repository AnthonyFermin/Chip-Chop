package madelyntav.c4q.nyc.chipchop.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import madelyntav.c4q.nyc.chipchop.BuyActivity;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.R;

/**
 * Created by alvin2 on 9/3/15.
 */
public class Fragment_Buyer_Review extends Fragment {

    public static final String TAG = "fragment_buyer_review";
    DBHelper dbHelper;
    BuyActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_buyer_review, container, false);
        initializeData();
        bindViews(root);


        return root;
    }

    private void bindViews(View root) {

    }

    private void initializeData() {
        activity = (BuyActivity) getActivity();
        dbHelper = DBHelper.getDbHelper(activity);

        activity.setCurrentFragment(TAG);
    }
}
