package madelyntav.c4q.nyc.chipchop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;

import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;

/**
 * Created by alvin2 on 9/11/15.
 */
public class TermsDialog extends android.support.v4.app.DialogFragment {

    Button confirmButton;
    CheckBox checkBox;
    SignupActivity1 activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View root = inflater.inflate(R.layout.fragment_dialog_terms, container, false);

        activity = (SignupActivity1) getActivity();


        checkBox = (CheckBox) root.findViewById(R.id.check_box);
        confirmButton = (Button) root.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()) {

                    activity.dbHelper.createUserAndCallback(activity.getEmail(), activity.getPassword(), new DBCallback() {
                        @Override
                        public void runOnSuccess() {
                            activity.startActivity(activity.getNewUserIntent());
                            activity.finish();
                        }

                        @Override
                        public void runOnFail() {

                        }
                    });
                    getDialog().dismiss();
                    // TODO: move back to getting information and create user!
                }
            }
        });

        return root;

    }
}
