package madelyntav.c4q.nyc.chipchop.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.adapters.SellerItemsAdapter;

/**
 * Created by c4q-anthonyf on 9/11/15.
 */
public class Fragment_Seller_Items_Inactive extends Fragment {

    RecyclerView inactiveList;
    Fragment_Seller_Items fragment;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.inactive_seller_items, container, false);
        fragment = (Fragment_Seller_Items) getParentFragment();

        inactiveList = (RecyclerView) rootView.findViewById(R.id.seller_items_list_INACTIVE);
        inactiveList.setLayoutManager(new LinearLayoutManager(getActivity()));
        inactiveList.setAdapter(new SellerItemsAdapter(fragment.getActivity(), fragment.getInActiveItems(), false));

        fragment.setInactiveList(inactiveList);

        return rootView;
    }

}
