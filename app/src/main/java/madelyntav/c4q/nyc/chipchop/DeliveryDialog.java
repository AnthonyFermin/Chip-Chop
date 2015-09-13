package madelyntav.c4q.nyc.chipchop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;

/**
 * Created by alvin2 on 9/12/15.
 */
public class DeliveryDialog extends android.support.v4.app.DialogFragment {

    Button deliverButton, pickupButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View root = inflater.inflate(R.layout.fragment_dialog_delivery, container, false);


        deliverButton = (Button) root.findViewById(R.id.delivery_button);
        deliverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelperMethods.getCurrentOrder().setToDeliver(true);
                getDialog().dismiss();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                PaymentDialog alertDialog = new PaymentDialog();
                alertDialog.show(fm, "fragment_alert");
            }
        });


        pickupButton = (Button) root.findViewById(R.id.pickup_button);
        pickupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelperMethods.getCurrentOrder().setIsPickup(true);
                getDialog().dismiss();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                PaymentDialog alertDialog = new PaymentDialog();
                alertDialog.show(fm, "fragment_alert");
            }
        });

        return root;
    }
}
