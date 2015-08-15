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

/**
 * Created by c4q-anthonyf on 8/14/15.
 */
public class SellersListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Item> foodItems;
    private Context context;
    private int lastPosition = -1;

    public SellersListAdapter(Context context, List<Item> foodItems){
        this.context = context;
        this.foodItems = foodItems;
    }

    private class FoodItemViewHolder extends RecyclerView.ViewHolder{

        CardView container;
        ImageView image;
        TextView name;
        TextView description;

        public FoodItemViewHolder(View itemView) {
            super(itemView);

            container = (CardView) itemView.findViewById(R.id.card_view);
            image = (ImageView) itemView.findViewById(R.id.seller_image);
            name = (TextView) itemView.findViewById(R.id.seller_name);
            description = (TextView) itemView.findViewById(R.id.seller_description);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.results_search_list_item, parent, false);
        return new FoodItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        Item foodItem = foodItems.get(position);
        FoodItemViewHolder vh = (FoodItemViewHolder) viewHolder;
        vh.name.setText(foodItem.getNameOfItem());
        vh.description.setText(foodItem.getDescriptionOfItem());
//        Picasso.with(context).load(foodItem.getImageLink()).fit().into(vh.image);
        vh.image.setImageDrawable(context.getResources().getDrawable(R.drawable.github));

        setAnimation(vh.container, position);


    }

    private void setAnimation(View viewToAnimate, int position){
        // only animates the view if it was not already displayed on the screen
        if(position > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.abc_slide_in_bottom); //can make a custom animation here
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }
}
