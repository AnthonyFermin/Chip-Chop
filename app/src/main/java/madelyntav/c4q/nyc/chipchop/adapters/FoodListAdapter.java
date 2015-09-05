package madelyntav.c4q.nyc.chipchop.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
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

import madelyntav.c4q.nyc.chipchop.BuyActivity;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.FoodItemSelectDialog;
import madelyntav.c4q.nyc.chipchop.R;

/**
 * Created by c4q-anthonyf on 8/14/15.
 */
public class FoodListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Item> foodItems;
    private Context context;
    private int lastPosition = -1;

    public FoodListAdapter(Context context, List<Item> foodItems){
        this.context = context;
        this.foodItems = foodItems;
    }

    private class FoodItemViewHolder extends RecyclerView.ViewHolder{

        CardView container;
        ImageView image;
        TextView name;
        TextView description;
        TextView price;
        TextView quantity;

        public FoodItemViewHolder(View itemView) {
            super(itemView);

            container = (CardView) itemView.findViewById(R.id.card_view);
            image = (ImageView) itemView.findViewById(R.id.food_image);
            name = (TextView) itemView.findViewById(R.id.food_name_tv);
            description = (TextView) itemView.findViewById(R.id.food_description_tv);
            price = (TextView) itemView.findViewById(R.id.food_price_tv);
            quantity = (TextView) itemView.findViewById(R.id.food_quantity_tv);

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BuyActivity activity = (BuyActivity)(context);
                    FragmentManager fm = activity.getSupportFragmentManager();
                    activity.setItemToCart(foodItems.get(getAdapterPosition()));
                    FoodItemSelectDialog alertDialog = new FoodItemSelectDialog();
                    alertDialog.show(fm, "fragment_alert");
                }
            });

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_search_list_item, parent, false);
        return new FoodItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        Item foodItem = foodItems.get(position);
        final FoodItemViewHolder vh = (FoodItemViewHolder) viewHolder;
        vh.name.setText(foodItem.getNameOfItem());
        vh.description.setText(foodItem.getDescriptionOfItem());
        vh.price.setText("$ " + foodItem.getPrice() + "");
        vh.quantity.setText(foodItem.getQuantity() + "");
        if(foodItem.getImageLink() != null && !foodItem.getImageLink().isEmpty() && foodItem.getImageLink().length() > 200) {
            final String imageLink = foodItem.getImageLink();
            new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... voids) {
                    byte[] decoded = org.apache.commons.codec.binary.Base64.decodeBase64(imageLink.getBytes());
                    BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
                    return BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);
                    vh.image.setImageBitmap(bitmap);
                }
            }.execute();
        }

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


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}


