package madelyntav.c4q.nyc.chipchop.Payments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wallet.Cart;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.LineItem;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.PaymentMethodTokenizationType;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.fragment.SupportWalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.AuthenticationException;

import madelyntav.c4q.nyc.chipchop.R;

public class PaymentsActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    // Unique identifiers for asynchronous requests:
    private static final int LOAD_MASKED_WALLET_REQUEST_CODE = 1000;
    private static final int LOAD_FULL_WALLET_REQUEST_CODE = 1001;
    private SupportWalletFragment walletFragment;
    GoogleApiClient googleApiClient;
    private String secretPublishableTestKey = "pk_test_AzaPoEV6yirFsSW63owmbRh9";
    String cardNum;
    int cardMonth;
    int cardYear;
    String streetAddress="570 West 189 Street";
    String addressLineTwo="Apt 5E";
    String city= "New York";
    String state="NY";
    String zipCode="10040";
    String country="United States";
    String name="Madelyn Tavarez";
    String cardCVC;
    EditText cardNumView;
    EditText cardMonthView;
    EditText cardYearView;
    EditText cardCVCView;
    EditText nameView;
    Button confirmPaymentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payments);

        nameView=(EditText) findViewById(R.id.Name);
        nameView.setText(name);
        cardNumView=(EditText) findViewById(R.id.cardNum);
        cardMonthView=(EditText) findViewById(R.id.expirationMonth);
        cardYearView=(EditText) findViewById(R.id.expirationYear);
        cardCVCView=(EditText) findViewById(R.id.cardCVC);
        confirmPaymentButton=(Button) findViewById(R.id.confirm_payment);

        walletFragment =
                (SupportWalletFragment) getSupportFragmentManager().findFragmentById(R.id.wallet_fragment);

        MaskedWalletRequest maskedWalletRequest = MaskedWalletRequest.newBuilder()

                // Request credit card tokenization with Stripe by specifying tokenization parameters:
                .setPaymentMethodTokenizationParameters(PaymentMethodTokenizationParameters.newBuilder()
                        .setPaymentMethodTokenizationType(PaymentMethodTokenizationType.PAYMENT_GATEWAY)
                        .addParameter("gateway", "stripe")
                        .addParameter("stripe:publishableKey", secretPublishableTestKey)
                        .addParameter("stripe:version", com.stripe.Stripe.VERSION)
                        .build())

                        // You want the shipping address:
                .setShippingAddressRequired(true)

                        // Price set as a decimal:
                //TODO: transfer price
                .setEstimatedTotalPrice("20.00")
                .setCurrencyCode("USD")
                .build();

        // Set the parameters:
        WalletFragmentInitParams initParams = WalletFragmentInitParams.newBuilder()
                .setMaskedWalletRequest(maskedWalletRequest)
                .setMaskedWalletRequestCode(LOAD_MASKED_WALLET_REQUEST_CODE)
                .build();

        // Initialize the fragment:
        walletFragment.initialize(initParams);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wallet.API, new Wallet.WalletOptions.Builder()
                        .setEnvironment(WalletConstants.ENVIRONMENT_SANDBOX)
                        .setTheme(WalletConstants.THEME_HOLO_LIGHT)
                        .build())
                .build();


        confirmPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardNum=cardNumView.getText().toString();
                cardMonth= Integer.parseInt(cardMonthView.getText().toString());
                cardYear= Integer.parseInt(cardYearView.getText().toString());
                cardCVC=cardCVCView.getText().toString();

                onClickConfirmPayment(cardNum, cardMonth, cardYear, cardCVC, name, streetAddress, addressLineTwo, city, state, zipCode,country);
            }
        });
    }


    public void onClickConfirmPayment(String cardNum, int cardMonth, int cardYear, String cardCVC, String name, String streetAddress, String apt, String city, String state, String zipCode, String Country ){

//        com.stripe.android.model.Card card = new com.stripe.android.model.Card(cardNum, cardMonth, cardYear, cardCVC);

        Card card= new Card(cardNum, cardMonth, cardYear, cardCVC, name, streetAddress, apt, city, state, zipCode,country);


        card.validateNumber();
        card.validateCVC();

        if ( !card.validateCard() ) {
            // Show errors
            Toast.makeText(this,"Invalid Payment Information",Toast.LENGTH_SHORT).show();
        }
        try {
        Stripe stripe = new Stripe(secretPublishableTestKey);

        stripe.createToken(
                card,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        Toast.makeText(PaymentsActivity.this, "Payment Submitted Successfully",Toast.LENGTH_LONG).show();
                        Log.d("TokenIS",token.toString());
                    }

                    public void onError(Exception error) {
                        // Show localized error message
                        Toast.makeText(PaymentsActivity.this,"Error Retrieving Token",Toast.LENGTH_SHORT).show();
                    }
                });
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

    }

    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOAD_MASKED_WALLET_REQUEST_CODE) { // Unique, identifying constant
            if (resultCode == Activity.RESULT_OK) {
                MaskedWallet maskedWallet = data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
                FullWalletRequest fullWalletRequest = FullWalletRequest.newBuilder()
                        .setCart(Cart.newBuilder()
                                .setCurrencyCode("USD")
                                .setTotalPrice("20.00")
                                .addLineItem(LineItem.newBuilder() // Identify item being purchased
                                        .setCurrencyCode("USD")
                                        .setQuantity("1")
                                        .setDescription("Premium Llama Food")
                                        .setTotalPrice("20.00")
                                        .setUnitPrice("20.00")
                                        .build())
                                .build())
                        .setGoogleTransactionId(maskedWallet.getGoogleTransactionId())
                        .build();
                Wallet.Payments.loadFullWallet(googleApiClient, fullWalletRequest, LOAD_FULL_WALLET_REQUEST_CODE);
            }
        } else if (requestCode == LOAD_FULL_WALLET_REQUEST_CODE) {
            // Unique, identifying constant
            FullWallet fullWallet = data.getParcelableExtra(WalletConstants.EXTRA_FULL_WALLET);
            String tokenJSON = fullWallet.getPaymentMethodToken().getToken();

            com.stripe.model.Token token = com.stripe.model.Token.GSON.fromJson(tokenJSON, com.stripe.model.Token.class);

            // TODO: send token to your server

    } else {
        super.onActivityResult(requestCode, resultCode, data);
    }
}


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

}