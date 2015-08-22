package madelyntav.c4q.nyc.chipchop;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

/**
 * Created by alvin2 on 8/22/15.
 */
public class ReviewDialogFragment extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    Button submitButton;
    RatingBar ratingBar;
    EditText textReview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View root = inflater.inflate(R.layout.fragment_dialog_review, container, false);

        submitButton = (Button) root.findViewById(R.id.submit_review_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();

                FragmentActivity activity = getActivity();
                FragmentManager fm = activity.getSupportFragmentManager();
                ReviewDialogFragment2 alertDialog = new ReviewDialogFragment2();
                alertDialog.show(fm, "fragment_alert");
            }
        });

        ratingBar = (RatingBar) root.findViewById(R.id.ratingBar);

        return root;
    }

    @Override
    public void onClick(View view) {

    }
}
