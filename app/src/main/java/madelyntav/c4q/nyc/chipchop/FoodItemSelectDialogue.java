package madelyntav.c4q.nyc.chipchop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by alvin2 on 8/16/15.
 */
public class FoodItemSelectDialogue extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    public static FoodItemSelectDialogue newInstance() { FoodItemSelectDialogue f = new FoodItemSelectDialogue(); return f; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        View root = inflater.inflate(R.layout.fragment_dialog, container, false);

        return root;
    }

    @Override
    public void onClick(View view) {

    }
}
