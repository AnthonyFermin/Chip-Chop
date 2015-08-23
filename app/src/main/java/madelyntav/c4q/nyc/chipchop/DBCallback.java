package madelyntav.c4q.nyc.chipchop;

import android.content.Context;

public interface DBCallback{

    public void runOnSuccess(Context context);
    public void runOnFail(Context context);

}