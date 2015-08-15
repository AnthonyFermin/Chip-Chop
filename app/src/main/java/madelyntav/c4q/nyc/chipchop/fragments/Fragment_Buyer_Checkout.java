package madelyntav.c4q.nyc.chipchop.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.SignupActivity1;


public class Fragment_Buyer_Checkout extends Fragment {

    Button checkoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_buyer_checkout, container, false);
        checkoutButton = (Button) root.findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: check if signed in, if not then go to signupactivity
                Intent signupIntent = new Intent(getActivity(), SignupActivity1.class);
                startActivity(signupIntent);
            }
        });


        return root;

    }


    public interface OnBuyerCheckoutFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
