package madelyntav.c4q.nyc.chipchop.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.text.DecimalFormat;
import java.util.List;

import madelyntav.c4q.nyc.chipchop.DBCallback;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.SellActivity;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Seller_CreateItem;

/**
 * Created by c4q-anthonyf on 8/14/15.
 */
public class SellerItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private DBCallback itemRemovalCallback;
    private List<Item> sellerItems;
    private Context context;
    private int lastPosition = -1;
    private DBHelper dbHelper;
    private SellActivity activity;

    public SellerItemsAdapter(final Context context, final List<Item> sellerItems) {
        this.context = context;
        this.sellerItems = sellerItems;
        dbHelper = DBHelper.getDbHelper(context);
        activity = (SellActivity) context;
    }

    private class SellersViewHolder extends RecyclerView.ViewHolder {

        Button removeItemButton;
        CardView container;
        ImageView image;
        TextView name;
        TextView price;
        TextView quantity;
        TextView description;

        public SellersViewHolder(View itemView) {
            super(itemView);

            container = (CardView) itemView.findViewById(R.id.card_view);
            image = (ImageView) itemView.findViewById(R.id.food_image);
            name = (TextView) itemView.findViewById(R.id.food_name_tv);
            price = (TextView) itemView.findViewById(R.id.food_price_tv);
            quantity = (TextView) itemView.findViewById(R.id.food_quantity_tv);
            description = (TextView) itemView.findViewById(R.id.food_description_tv);

            itemRemovalCallback = new DBCallback() {
                @Override
                public void runOnSuccess() {
                    Toast.makeText(context,"Item removed successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void runOnFail() {
                    Toast.makeText(context,"Unable to remove item", Toast.LENGTH_SHORT).show();
                }
            };

            removeItemButton = (Button) itemView.findViewById(R.id.remove_item_button);
            removeItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: add popup dialog asking user to confirm deletion
                    if(activity.isCurrentlyCooking()) {
                        dbHelper.removeItemFromSale(sellerItems.get(getAdapterPosition()), itemRemovalCallback);
                    }else {
                        //TODO: Madelyn: are these the correct methods to remove items from the seller profile in the DB?
                        dbHelper.removeItemFromSellerProfile(sellerItems.get(getAdapterPosition()), itemRemovalCallback);
                    }
                    sellerItems.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            });

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Item itemToEdit = sellerItems.get(getAdapterPosition());

                    activity.setItemToEdit(itemToEdit);
                    activity.replaceSellerFragment(new Fragment_Seller_CreateItem());
                }
            });
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.selleritem_list_item, parent, false);
        return new SellersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        Item sellerItem = sellerItems.get(position);
        SellersViewHolder vh = (SellersViewHolder) viewHolder;
        vh.name.setText(sellerItem.getNameOfItem());
        DecimalFormat df = new DecimalFormat("#.00");
        String price = df.format(sellerItem.getPrice());
        vh.price.setText("$" + price);
        vh.quantity.setText(sellerItem.getQuantity() + "");
        vh.description.setText(sellerItem.getDescriptionOfItem());
        if(!sellerItem.getImageLink().isEmpty())
            Picasso.with(context).load(sellerItem.getImageLink()).fit().into(vh.image);

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
        return sellerItems.size();
    }
}


