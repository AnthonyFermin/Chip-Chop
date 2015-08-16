package madelyntav.c4q.nyc.chipchop.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import madelyntav.c4q.nyc.chipchop.R;

/**
 * Created by alvin2 on 8/16/15.
 */
public class Fragment_Seller_CreateItem extends Fragment {

    Button addButton, cancelButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_seller__create_item, container, false);

        cancelButton = (Button) root.findViewById(R.id.cancel_item_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: destroy current fragment, show last on stack!
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        addButton = (Button) root.findViewById(R.id.add_item_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: add item to sellers items in db and arraylist displayed in profile/items fragment recycler views
            }
        });


        return root;
    }
}
