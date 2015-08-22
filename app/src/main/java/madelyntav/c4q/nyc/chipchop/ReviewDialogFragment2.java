package madelyntav.c4q.nyc.chipchop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by alvin2 on 8/22/15.
 */
public class ReviewDialogFragment2 extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    EditText textReview;
    Button submitButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View root = inflater.inflate(R.layout.fragment_dialog_review2, container, false);

        textReview = (EditText) root.findViewById(R.id.text_review);
        submitButton = (Button) root.findViewById(R.id.submit_review_button);

        return root;
    }

    @Override
    public void onClick(View view) {

    }
}
