package madelyntav.c4q.nyc.chipchop.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.adapters.SellerItemsAdapter;

/**
 * Created by c4q-anthonyf on 9/11/15.
 */
public class Fragment_Seller_Items_Active extends Fragment {

    RecyclerView activeList;
    Fragment_Seller_Items fragment;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.active_seller_items, container, false);


        fragment = (Fragment_Seller_Items) getParentFragment();
        activeList = (RecyclerView) rootView.findViewById(R.id.seller_items_list_ACTIVE);
        activeList.setLayoutManager(new LinearLayoutManager(getActivity()));
        activeList.setAdapter(new SellerItemsAdapter(fragment.getActivity(),fragment, fragment.getActiveItems(),true));

        fragment.setActiveList(activeList);

        return rootView;
    }


}
