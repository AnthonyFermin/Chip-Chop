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
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.SellActivity;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Seller_OrderDetails;

/**
 * Created by alvin2 on 8/26/15.
 */

public class BuyerOrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    RecyclerView recycler;
    private List<Item> orderItems;
    private Context context;
    private int lastPosition = -1;

    public BuyerOrdersAdapter(Context context, List<Item> orderItems) {
        this.context = context;
        this.orderItems = orderItems;
    }

    private class BuyerOrdersViewHolder extends RecyclerView.ViewHolder {

        CardView container;
        TextView orderID;
        TextView sellerName;
        TextView date;
        TextView totalCost;
        TextView quantity;


        public BuyerOrdersViewHolder(View itemView) {
            super(itemView);

            recycler = (RecyclerView) itemView.findViewById(R.id.buyers_orders_list);
            container = (CardView) itemView.findViewById(R.id.card_view);
            orderID = (TextView) itemView.findViewById(R.id.order_id_tv);
            sellerName = (TextView) itemView.findViewById(R.id.seller_name_tv);
            date = (TextView) itemView.findViewById(R.id.order_time_tv);
            totalCost = (TextView) itemView.findViewById(R.id.order_cost_tv);
            quantity = (TextView) itemView.findViewById(R.id.total_item_tv);



        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.buyerorder_list_item, parent, false);
        return new BuyerOrdersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        Item checkoutItem = orderItems.get(position);
        BuyerOrdersViewHolder vh = (BuyerOrdersViewHolder) viewHolder;
//        vh.name.setText(checkoutItem.getNameOfItem());
//        vh.quantity.setText(String.valueOf(checkoutItem.getQuantityWanted()));
//        Picasso.with(context).load(checkoutItem.getImageLink()).fit().into(vh.image);


            setAnimation(vh.container, position);

//        CartListAdapter cartListAdapter = new CartListAdapter(context, orderDetails);
//        recycler.setAdapter(cartListAdapter);

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