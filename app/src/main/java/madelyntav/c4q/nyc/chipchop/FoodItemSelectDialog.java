package madelyntav.c4q.nyc.chipchop;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.DBObjects.Order;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Buyer_Checkout;

/**
 * Created by alvin2 on 8/16/15.
 */
public class FoodItemSelectDialog extends android.support.v4.app.DialogFragment {

    private int quantity = 0;
    private CircleImageView foodImage;
    private TextView quantityDisplay, foodDescription, dishName;
    private ImageButton upButton, downButton;
    private Button addToCartButton;
    private Item itemForCart;
    private BuyActivity activity;
    private Order order;

    private DBHelper dbHelper;

    public static FoodItemSelectDialog newInstance() {
        FoodItemSelectDialog f = new FoodItemSelectDialog();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View root = inflater.inflate(R.layout.fragment_dialog_food_item, container, false);

        bindViews(root);
        initializeData();
        initializeViews();
        setListeners();

        return root;
    }

    private void setListeners() {
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity < itemForCart.getQuantity()) {
                    quantity += 1;
                    quantityDisplay.setText(String.valueOf(quantity));
                }
            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity > 0) {
                    quantity -= 1;
                    quantityDisplay.setText(String.valueOf(quantity));
                }
            }
        });

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item item = itemForCart.clone();
                item.setQuantityWanted(quantity);
                item.setBuyerID(dbHelper.getUserID());
                ArrayList<Item> cart = order.getItemsOrdered();
                boolean replacedItem = false;
                for (int i = 0; i < cart.size(); i++) {
                    if (cart.get(i).getNameOfItem().equals(item.getNameOfItem())) {
                        cart.remove(i);
                        cart.add(i, item);
                        replacedItem = true;
                        break;
                    }
                }
                if (!replacedItem) {
                    cart.add(item);
                }
                order.setItemsOrdered(cart);
                activity.setCurrentOrder(order);
                FoodItemSelectDialog.this.dismiss();
            }
        });
    }

    private void initializeData() {
        activity = (BuyActivity) getActivity();
        dbHelper = DBHelper.getDbHelper(activity);
        itemForCart = activity.getItemToCart();
        order = activity.getCurrentOrder();
    }

    private void bindViews(View root) {
        foodImage = (CircleImageView) root.findViewById(R.id.food_image);
        dishName = (TextView) root.findViewById(R.id.dish_name);
        foodDescription = (TextView) root.findViewById(R.id.food_description_tv);
        quantityDisplay = (TextView) root.findViewById(R.id.quantity_tv);
        upButton = (ImageButton) root.findViewById(R.id.upButton);
        downButton = (ImageButton) root.findViewById(R.id.downButton);
        addToCartButton = (Button) root.findViewById(R.id.add_cart_button);
    }

    private void initializeViews() {
        Picasso.with(activity).load(itemForCart.getImageLink()).fit().into(foodImage);
        dishName.setText(itemForCart.getNameOfItem());
        foodDescription.setText(itemForCart.getDescriptionOfItem());
    }

}
