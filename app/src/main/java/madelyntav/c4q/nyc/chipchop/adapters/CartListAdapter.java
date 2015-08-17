package madelyntav.c4q.nyc.chipchop.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import madelyntav.c4q.nyc.chipchop.BuyActivity;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.R;

/**
 * Created by alvin2 on 8/16/15.
 */
public class CartListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Button removeItemButton;
    private List<Item> checkoutItems;
    private Context context;
    private int lastPosition = -1;

    public CartListAdapter(Context context, List<Item> checkoutItems) {
        this.context = context;
        this.checkoutItems = checkoutItems;
    }

    private class CheckoutViewHolder extends RecyclerView.ViewHolder {

        CardView container;
        ImageView image;
        TextView name;
        TextView price;
        TextView quantity;


        public CheckoutViewHolder(View itemView) {
            super(itemView);

            container = (CardView) itemView.findViewById(R.id.card_view);
            image = (ImageView) itemView.findViewById(R.id.food_image);
            name = (TextView) itemView.findViewById(R.id.food_name_tv);
            price = (TextView) itemView.findViewById(R.id.food_price_tv);
            quantity = (TextView) itemView.findViewById(R.id.food_quantity_tv);
            removeItemButton = (Button) itemView.findViewById(R.id.remove_item_button);

        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_item, parent, false);
        return new CheckoutViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        final Item checkoutItem = checkoutItems.get(position);
        CheckoutViewHolder vh = (CheckoutViewHolder) viewHolder;
        vh.name.setText(checkoutItem.getNameOfItem());
        vh.price.setText("$ N/A");
        vh.quantity.setText(checkoutItem.getQuantityAvailable());
        Picasso.with(context).load(checkoutItem.getImageLink()).fit().into(vh.image);


        vh.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: put the listener for the specific ITEM selected with the quantity
                removeItemButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkoutItems.remove(position);
                    }
                });
            }
        });

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
        return checkoutItems.size();
    }
}

