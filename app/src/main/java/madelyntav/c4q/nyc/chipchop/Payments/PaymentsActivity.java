package madelyntav.c4q.nyc.chipchop.Payments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import madelyntav.c4q.nyc.chipchop.R;

public class PaymentsActivity extends AppCompatActivity {

    IabHelper mHelper;
    String base64EncodedPublicKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnm2MI3Jx8wAILS5ybiOGVKSmVub0JpPlpSFrazSJSRJI6koNMSCzZyYWMPv3gKHZT7PeDTM6UeBJLH4kplOhFj20+CSUXW+3cRXzw8gh5FFHKWN1UQOevx0YxK+WStcKzK3EtlVPo0nRTp+ADjSN0vq8KGsslaUHGf9F8ZrExLH98InW+Ulnk7vIH86Sl6gftf1/WRNn/Rg1ODPSjLSgSztnD56pDCAEEyu0nF5mZPYUehCztlA494XYiGzm80pw7pAhn1mNVsAsSc+Rw1aeC1kHHFZd3/Mf3IB+i0D8GjXn7MOTfYORCk4+AfyC5ClQmRfUDyFlZ5HoI/mDBEa5ZQIDAQAB";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        mHelper = new IabHelper(this, base64EncodedPublicKey);
    }

    private void setUpBilling(){
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    Log.d("Error", "Problem setting up In-app Billing: " + result);
                }
                // Hooray, IAB is fully set up!
            }
        });
    }





    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }
}
