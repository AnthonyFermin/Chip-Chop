package madelyntav.c4q.nyc.chipchop;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View root = inflater.inflate(R.layout.fragment_dialog_terms, container, false);


        checkBox = (CheckBox) root.findViewById(R.id.check_box);
        confirmButton = (Button) root.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()) {
                    getDialog().dismiss();
                    // TODO: move back to getting information and create user!
                }
            }
        });

        return root;

    }
}
