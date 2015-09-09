package madelyntav.c4q.nyc.chipchop.Payments;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.fragment.BuyButtonText;
import com.google.android.gms.wallet.fragment.Dimension;
import com.google.android.gms.wallet.fragment.SupportWalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;
import com.google.android.gms.wallet.fragment.WalletFragmentMode;
import com.google.android.gms.wallet.fragment.WalletFragmentOptions;
import com.google.android.gms.wallet.fragment.WalletFragmentStyle;

import madelyntav.c4q.nyc.chipchop.R;

public class PaymentsActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private SupportWalletFragment mSupportWalletFragment;
    private SupportWalletFragment mXmSupportWalletFragment;
    private FullWallet mFullWallet;
    private MaskedWalletRequest mMaskedWalletRequest;
    private IabHelper mHelper;
    GoogleApiClient mGoogleApiClient;
    private  ServiceConnection mServiceConn;
    private IInAppBillingService mService;
    private String sBase64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnm2MI3Jx8wAILS5ybiOGVKSmVub0JpPlpSFrazSJSRJI6koNMSCzZyYWMPv3gKHZT7PeDTM6UeBJLH4kplOhFj20+CSUXW+3cRXzw8gh5FFHKWN1UQOevx0YxK+WStcKzK3EtlVPo0nRTp+ADjSN0vq8KGsslaUHGf9F8ZrExLH98InW+Ulnk7vIH86Sl6gftf1/WRNn/Rg1ODPSjLSgSztnD56pDCAEEyu0nF5mZPYUehCztlA494XYiGzm80pw7pAhn1mNVsAsSc+Rw1aeC1kHHFZd3/Mf3IB+i0D8GjXn7MOTfYORCk4+AfyC5ClQmRfUDyFlZ5HoI/mDBEa5ZQIDAQAB";

    public static final int MASKED_WALLET_REQUEST_CODE = 888;
    public static final int FULL_WALLET_REQUEST_CODE = 889;

    public static final String WALLET_FRAGMENT_ID = "wallet_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new IabHelper(this, sBase64EncodedPublicKey);

        setUpBilling();

        mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
            }

            @Override
            public void onServiceConnected(ComponentName name,
                                           IBinder service) {
                mService = IInAppBillingService.Stub.asInterface(service);
            }
        };

        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);


        mSupportWalletFragment= (SupportWalletFragment) getSupportFragmentManager().findFragmentByTag(WALLET_FRAGMENT_ID);

        WalletFragmentInitParams walletFragmentInitParams;

        WalletFragmentInitParams.Builder startParamsBuilder= WalletFragmentInitParams.newBuilder()
                .setMaskedWalletRequest(generateMaskedWalletRequest()).setMaskedWalletRequestCode(MASKED_WALLET_REQUEST_CODE);

        walletFragmentInitParams = startParamsBuilder.build();

        if(mFullWallet == null){
            WalletFragmentStyle walletFragmentStyle= new WalletFragmentStyle()
                    .setBuyButtonText(BuyButtonText.BUY_WITH_GOOGLE)
                    .setBuyButtonWidth(Dimension.MATCH_PARENT);

            WalletFragmentOptions walletFragmentOptions= WalletFragmentOptions.newBuilder().
                    setEnvironment(WalletConstants.ENVIRONMENT_SANDBOX).
                    setFragmentStyle(walletFragmentStyle).
                    setTheme(WalletConstants.THEME_HOLO_LIGHT).
                    setMode(WalletFragmentMode.BUY_BUTTON).build();

            mSupportWalletFragment= SupportWalletFragment.newInstance(walletFragmentOptions);
            mSupportWalletFragment.initialize(walletFragmentInitParams);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.wallet_fragment, mSupportWalletFragment,WALLET_FRAGMENT_ID)
                    .commit();

            mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).addApi(Wallet.API, new Wallet.WalletOptions.Builder()
                            .setEnvironment(WalletConstants.ENVIRONMENT_SANDBOX)
                            .setTheme(WalletConstants.THEME_HOLO_LIGHT).build()).build();
        }

        setContentView(R.layout.activity_payments);

    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case MASKED_WALLET_REQUEST_CODE:
                switch(resultCode) {
                    case Activity.RESULT_OK:
                        mMaskedWalletRequest=data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
                                break;

                }
        }
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

        if (mService != null) {
            unbindService(mServiceConn);
        }
    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
