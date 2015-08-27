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

import com.squareup.picasso.Picasso;

import java.util.List;

import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.SellActivity;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Seller_OrderDetails;

/**
 * Created by alvin2 on 8/16/15.
 */
public class SellerOrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Item> orderItems;
    private Context context;
    private int lastPosition = -1;

    public SellerOrdersAdapter(Context context, List<Item> orderItems) {
        this.context = context;
        this.orderItems = orderItems;
    }

    private class SellerOrdersViewHolder extends RecyclerView.ViewHolder {

        CardView container;
        ImageView image;
        TextView name;
        TextView price;
        TextView quantity;


        public SellerOrdersViewHolder(View itemView) {
            super(itemView);

            container = (CardView) itemView.findViewById(R.id.card_view);
            image = (ImageView) itemView.findViewById(R.id.food_image);
            name = (TextView) itemView.findViewById(R.id.food_name_tv);
            quantity = (TextView) itemView.findViewById(R.id.food_quantity_tv);

        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sellerorder_list_item, parent, false);
        return new SellerOrdersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        Item checkoutItem = orderItems.get(position);
        SellerOrdersViewHolder vh = (SellerOrdersViewHolder) viewHolder;
//        vh.name.setText(checkoutItem.getNameOfItem());
//        vh.quantity.setText(String.valueOf(checkoutItem.getQuantityWanted()));
//        Picasso.with(context).load(checkoutItem.getImageLink()).fit().into(vh.image);

        vh.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SellActivity activity = (SellActivity) context;
                activity.replaceSellerFragment(new Fragment_Seller_OrderDetails());
            }
        });

//            setAnimation(vh.container, position);


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




