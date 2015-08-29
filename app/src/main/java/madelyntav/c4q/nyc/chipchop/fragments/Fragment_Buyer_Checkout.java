package madelyntav.c4q.nyc.chipchop.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import madelyntav.c4q.nyc.chipchop.BuyActivity;
import madelyntav.c4q.nyc.chipchop.DBCallback;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.DBObjects.Order;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.ReviewDialogFragment;
import madelyntav.c4q.nyc.chipchop.adapters.CheckoutListAdapter;


public class Fragment_Buyer_Checkout extends Fragment {

    Button confirmOrder;
    private ArrayList<Item> cartItems;
    private RecyclerView cartRView;
    private TextView totalPrice;

    private BuyActivity activity;
    private Order order;
    private DBHelper dbHelper;

    public static final String TAG = "fragment_buyer_checkout";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_buyer_checkout, container, false);

        activity = (BuyActivity) getActivity();
        dbHelper = DBHelper.getDbHelper(activity);

        order = activity.getCurrentOrder();
        cartItems = order.getItemsOrdered();

        totalPrice = (TextView) root.findViewById(R.id.total_price_tv);
        double total = 0;
        for(Item item: cartItems){
            total = total + (item.getPrice() * item.getQuantityWanted());
        }
        totalPrice.setText(total + "");

        cartRView = (RecyclerView) root.findViewById(R.id.checkout_items_list);
        cartRView.setLayoutManager(new LinearLayoutManager(getActivity()));

        CheckoutListAdapter checkoutListAdapter = new CheckoutListAdapter(getActivity(), cartItems);
        cartRView.setAdapter(checkoutListAdapter);

        confirmOrder = (Button) root.findViewById(R.id.confirmOrderButton);
        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Madelyn this the right method? I see multiple methods that can be used to send this to the DB
                //TODO: Madelyn this can be for next weeks sprint if you like: is there a way to check the quanities wanted vs the quantities actually available on the DB inside a DBHelper method before accepting the transaction? https://www.firebase.com/docs/web/api/firebase/transaction.html
                dbHelper.addCurrentOrderToSellerDB(order,new DBCallback() {
                    @Override
                    public void runOnSuccess() {
                        activity.replaceFragment(new Fragment_Buyer_Orders());
                    }

                    @Override
                    public void runOnFail() {
                        Toast.makeText(activity,"Items are no longer available",Toast.LENGTH_SHORT).show();
                    }
                });
                //TODO: Check if Signed in, else go into signup activity - Sign in should just be a pop up dialog
            }
        });


        return root;

    }


}
