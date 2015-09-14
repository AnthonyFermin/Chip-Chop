package madelyntav.c4q.nyc.chipchop;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;

import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Order;
import madelyntav.c4q.nyc.chipchop.DBObjects.Review;

/**
 * Created by alvin2 on 8/22/15.
 */
public class ReviewDialogFragment extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    Button submitButton;
    Order order;
    RatingBar ratingBar;
    DBHelper dbHelper;
    Review review;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dbHelper = DBHelper.getDbHelper(getActivity());

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View root = inflater.inflate(R.layout.fragment_dialog_review, container, false);
        ratingBar = (RatingBar) root.findViewById(R.id.ratingBar);
        review= new Review();
        order=HelperMethods.getCurrentOrder();
        review.setNumOfStars(ratingBar.getRating());

        submitButton = (Button) root.findViewById(R.id.submit_review_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(review.getNumOfStars()==0) {
                    ratingBar.setNumStars(0);
                }
                review.setNumOfStars(ratingBar.getNumStars());
                order.setReview(review);
                order.setIsReviewed(true);

                addReviewAndCalculateNewAvg();
                getDialog().dismiss();
            }
        });

        ratingBar = (RatingBar) root.findViewById(R.id.ratingBar);

        return root;
    }

    @Override
    public void onClick(View view) {

    }

    public void addReviewAndCalculateNewAvg(){
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                //dbHelper.addReviewToSellerProfile(order.getBuyerID(),order.getSellerID(),review.getNumOfStars());
                dbHelper.copyOrderToBuyerProfile(order);
                dbHelper.setReviewForOrderInSellerProfile(order);
                dbHelper.sendReviewedOrderToSellerDB(order, new DBCallback() {
                    @Override
                    public void runOnSuccess() {
                        dismiss();
                    }

                    @Override
                    public void runOnFail() {

                    }
                });
                return null;
            }
        }.execute();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                return null;
            }
        }.execute();
    }
}
