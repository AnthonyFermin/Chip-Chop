package madelyntav.c4q.nyc.chipchop.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import madelyntav.c4q.nyc.chipchop.BuyActivity;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.DBObjects.Order;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.SellActivity;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Seller_OrderDetails;

/**
 * Created by alvin2 on 8/26/15.
 */

public class BuyerOrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Order> orderItems;
    private Context context;
    private int lastPosition = -1;

    public BuyerOrdersAdapter(Context context, List<Order> orderItems) {
        this.context = context;
        this.orderItems = orderItems;
    }

    private class BuyerOrdersViewHolder extends RecyclerView.ViewHolder {

        CardView container;
        TextView orderID;
        TextView total;
        TextView nameOfSeller;


        public BuyerOrdersViewHolder(View itemView) {
            super(itemView);

            total = (TextView) itemView.findViewById(R.id.order_cost_tv);
            container = (CardView) itemView.findViewById(R.id.card_view);
            nameOfSeller = (TextView) itemView.findViewById(R.id.seller_name_tv);
            orderID = (TextView) itemView.findViewById(R.id.order_id_tv);

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BuyActivity activity = (BuyActivity) context;
                    activity.replaceFragment(new Fragment_Seller_OrderDetails());
                }
            });



        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.buyerorder_list_item, parent, false);
        return new BuyerOrdersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        Order order = orderItems.get(position);
        BuyerOrdersViewHolder vh = (BuyerOrdersViewHolder) viewHolder;
        vh.nameOfSeller.setText("Seller Name: " + order.getSellerID());
        vh.orderID.setText("Order ID: " + order.getOrderID());
        vh.total.setText("Total: $" + order.getPrice());

        setAnimation(vh.container, position);

    }

    private void setAnimation(View viewToAnimate, int position) {
        // only animates the view if it was not already displayed on the screen
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.abc_slide_in_bottom); //can make a custom animation here
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }
}