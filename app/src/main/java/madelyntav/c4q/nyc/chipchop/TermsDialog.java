package madelyntav.c4q.nyc.chipchop;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;

/**
 * Created by alvin2 on 9/11/15.
 */
public class TermsDialog extends android.support.v4.app.DialogFragment {

    Button confirmButton;
    CheckBox checkBox;
    SignUpFirstActivity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        View root = inflater.inflate(R.layout.fragment_dialog_terms, container, false);

        activity = (SignUpFirstActivity) getActivity();


        checkBox = (CheckBox) root.findViewById(R.id.check_box);
        confirmButton = (Button) root.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()) {

                    activity.dbHelper.createUserAndCallback(activity.getEmail(), activity.getPassword(), new DBCallback() {
                        @Override
                        public void runOnSuccess() {

                            SharedPreferences sharedPreferences = activity.getSharedPreferences(Constants.USER_INFO_KEY, activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Constants.EMAIL_KEY, activity.getEmail());
                            editor.putString(Constants.PASSWORD_KEY, activity.getPassword());
                            editor.putBoolean(Constants.IS_LOGGED_IN_KEY, true);
                            editor.apply();

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
