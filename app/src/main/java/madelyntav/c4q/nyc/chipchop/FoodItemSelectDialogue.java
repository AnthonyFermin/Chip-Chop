package madelyntav.c4q.nyc.chipchop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by alvin2 on 8/16/15.
 */
public class FoodItemSelectDialogue extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    int quantity = 0;
    TextView quantityDisplay;
    Button addButton, removeButton, addToCartButton;

    public static FoodItemSelectDialogue newInstance() { FoodItemSelectDialogue f = new FoodItemSelectDialogue(); return f; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View root = inflater.inflate(R.layout.fragment_dialog, container, false);

        quantityDisplay = (TextView) root.findViewById(R.id.quantity_tv);

        addButton = (Button) root.findViewById(R.id.upButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity += 1;
                quantityDisplay.setText(String.valueOf(quantity));
            }
        });

        removeButton = (Button) root.findViewById(R.id.downButton);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity > 0) {
                    quantity -= 1;
                    quantityDisplay.setText(String.valueOf(quantity));
                }
            }
        });

        addToCartButton = (Button) root.findViewById(R.id.add_cart_button);
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: add code to add to the cart arraylist??
            }
        });

        return root;
    }

    @Override
    public void onClick(View view) {

    }
}
