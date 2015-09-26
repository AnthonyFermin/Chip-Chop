package madelyntav.c4q.nyc.chipchop;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import madelyntav.c4q.nyc.chipchop.DBObjects.Order;
import madelyntav.c4q.nyc.chipchop.DBObjects.Seller;

/**
 * Created by alvin2 on 9/12/15.
 */
public class DeliveryDialog extends android.support.v4.app.DialogFragment {

    Button deliverButton, pickupButton;
    Seller seller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View root = inflater.inflate(R.layout.fragment_dialog_delivery, container, false);

        initializeData();
        bindViews(root);
        initializeViews();
        setListeners();

        return root;
    }

    private void initializeViews() {
        if(seller.isDeliveryAvailable()){
            deliverButton.setEnabled(true);
        }

        if(!seller.isPickUpAvailable()){
            deliverButton.setEnabled(true);
            pickupButton.setEnabled(false);
        }
    }

    private void setListeners() {
        deliverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelperMethods.getCurrentOrder().setToDeliver(true);
                HelperMethods.getCurrentOrder().setIsPickup(false);
                HelperMethods.getCurrentOrder().setBuyerAddress(HelperMethods.getUser().getAddressString());
                Log.d("DELIVERY SELECTED", "BUYER ADDRESS: " + HelperMethods.getCurrentOrder().getBuyerAddress());
                getDialog().dismiss();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                PaymentDialog alertDialog = new PaymentDialog();
                alertDialog.show(fm, "fragment_alert");
            }
        });


        pickupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelperMethods.getCurrentOrder().setIsPickup(true);
                HelperMethods.getCurrentOrder().setToDeliver(false);
                HelperMethods.getCurrentOrder().setSellerAddress(HelperMethods.getSellerToView().getAddressString());
                Log.d("PICKUP SELECTED","SELLER ADDRESS: " + HelperMethods.getCurrentOrder().getSellerAddress());

                getDialog().dismiss();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                PaymentDialog alertDialog = new PaymentDialog();
                alertDialog.show(fm, "fragment_alert");
            }
        });

    }

    private void bindViews(View root) {

        pickupButton = (Button) root.findViewById(R.id.pickup_button);
        deliverButton = (Button) root.findViewById(R.id.delivery_button);
    }

    private void initializeData() {
        seller = HelperMethods.getSellerToView();
    }
}
