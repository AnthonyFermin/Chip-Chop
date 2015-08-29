package madelyntav.c4q.nyc.chipchop.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import madelyntav.c4q.nyc.chipchop.BuyActivity;
import madelyntav.c4q.nyc.chipchop.DBObjects.Seller;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Buyer_SellerProfile;

/**
 * Created by alvin2 on 8/20/15.
 */
public class SellerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Seller> sellers;
    private Context context;
    private int lastPosition = -1;


    public SellerListAdapter(Context context, ArrayList<Seller> sellers) {
        this.context = context;
        this.sellers = sellers;
    }

    private class SellersViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout container;
        TextView name;
        TextView description;

        public SellersViewHolder(View itemView) {
            super(itemView);

            container = (RelativeLayout) itemView.findViewById(R.id.container);
            name = (TextView) itemView.findViewById(R.id.store_name);
            description = (TextView) itemView.findViewById(R.id.store_description);

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BuyActivity activity = (BuyActivity) context;
                    activity.setSellerToView(sellers.get(getAdapterPosition()));
                    activity.replaceFragment(new Fragment_Buyer_SellerProfile());
                }
            });

        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.results_search_list_item, parent, false);
        return new SellersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        Seller seller = sellers.get(position);
        final SellersViewHolder vh = (SellersViewHolder) viewHolder;
        vh.name.setText(seller.getStoreName());
        //TODO: CHANGE TO DESCRIPTION !!
        vh.description.setText(seller.getDescription());
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                BitmapDrawable bd = new BitmapDrawable(context.getResources(), bitmap);
                vh.container.setBackground(bd);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(context).load(R.drawable.food2).into(target);

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
        return sellers.size();
    }
}
