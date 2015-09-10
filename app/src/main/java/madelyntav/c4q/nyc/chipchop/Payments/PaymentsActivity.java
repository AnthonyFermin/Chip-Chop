package madelyntav.c4q.nyc.chipchop.Payments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;

import java.util.HashMap;
import java.util.Map;

import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Order;
import madelyntav.c4q.nyc.chipchop.DBObjects.User;
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
    DBHelper dbHelper;
    Charge charge;
    String stripeUserID;
    //TODO transfer all Order and userInfo
    Order order;
    User user;
    static Map<String, Object> defaultCardParams   = new HashMap<String, Object>();
    static Map<String, Object> defaultChargeParams = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        com.stripe.Stripe.apiKey = "sk_test_6hJykZdli4Tw6ALzNEyHSePR";

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wallet.API, new Wallet.WalletOptions.Builder()
                        .setEnvironment(WalletConstants.ENVIRONMENT_SANDBOX)
                        .setTheme(WalletConstants.THEME_HOLO_LIGHT)
                        .build())
                .build();

        nameView=(EditText) findViewById(R.id.Name);
        nameView.setText(name);
        cardNumView=(EditText) findViewById(R.id.cardNum);
        cardMonthView=(EditText) findViewById(R.id.expirationMonth);
        cardYearView=(EditText) findViewById(R.id.expirationYear);
        cardCVCView=(EditText) findViewById(R.id.cardCVC);
        confirmPaymentButton=(Button) findViewById(R.id.confirm_payment);



        //Button to confirm payment with credit card through stripe
        confirmPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardNum = cardNumView.getText().toString();
                cardMonth = Integer.parseInt(cardMonthView.getText().toString());
                cardYear = Integer.parseInt(cardYearView.getText().toString());
                cardCVC = cardCVCView.getText().toString();

                onClickConfirmPayment(cardNum, cardMonth, cardYear, cardCVC, name, streetAddress, addressLineTwo, city, state, zipCode, country);
            }
        });
    }


    public void onClickConfirmPayment(final String cardNum, final int cardMonth, final int cardYear, final String cardCVC, String name, String streetAddress, String apt, String city, String state, String zipCode, String Country ){

        final Card card= new Card(cardNum, cardMonth, cardYear, cardCVC, name, streetAddress, apt, city, state, zipCode,country);

        if ( !card.validateCard() ) {
            // Show errors
            Toast.makeText(this,"Invalid Payment Information",Toast.LENGTH_SHORT).show();
        }
        else {
            //TODO activate once we have Encrypted Info and SSL Certificate has been added and we have user Info transfered in (UID, NAME, FULL Address)
//            user.setCardNumber(cardNum);
//            user.setCardExpirationMonth(cardMonth);
//            user.setCardExpirationYear(cardYear);
//            user.setCardCVC(cardCVC);
//            dbHelper.addUserProfileInfoToDB(user);
        }

        card.validateNumber();
        card.validateCVC();

        try {
        Stripe stripe = new Stripe(secretPublishableTestKey);
            stripe.setDefaultPublishableKey(secretPublishableTestKey);

        stripe.createToken(
                card,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        Toast.makeText(PaymentsActivity.this, "Payment Info Submitted Successfully", Toast.LENGTH_LONG).show();
                        Log.d("TokenIS", token.toString());
                        createCharge("100", token, cardNum, cardMonth, cardYear, cardCVC);
                    }

                    public void onError(Exception error) {
                        // Show localized error message
                        Toast.makeText(PaymentsActivity.this, "Error Retrieving Token", Toast.LENGTH_SHORT).show();
                    }
                });
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

    }

    private void createCharge(String price, final Token token, String cardNum, int cardMonth, int cardYear, final String cardCVC) {

        final Map<String, Object> chargeMap = new HashMap<String, Object>();
        //amount charged is in cents!!!
        chargeMap.put("amount", price);
        chargeMap.put("currency", "usd");
        //  chargeMap.put("description", order.getStoreName());

        Map<String, Object> cardMap = new HashMap<String, Object>();
        cardMap.put("number", cardNum);
        cardMap.put("exp_month", cardMonth);
        cardMap.put("exp_year", cardYear);
        chargeMap.put("card", cardMap);
        Log.d("CreatedChargeMap", "1");






        new AsyncTask<Void, Void, Charge>() {
            String id;
            @Override
            protected Charge doInBackground(Void... params) {

                try {
                    charge= Charge.create(chargeMap);
                    Log.d("CHARGE",charge.toString());
                } catch (AuthenticationException e) {
                    e.printStackTrace();
                } catch (InvalidRequestException e) {
                    e.printStackTrace();
                } catch (APIConnectionException e) {
                    e.printStackTrace();
                } catch (CardException e) {
                    e.printStackTrace();
                } catch (APIException e) {
                    e.printStackTrace();
                    Log.e("stripe err" , e.getCause().toString());
                }

                return charge;
            }

            @Override
            protected void onPostExecute(Charge charge) {
                super.onPostExecute(charge);
                id = charge.getId();
                Log.d("ChargeID", id.toString());
                retrieveCharge(id);
            }
        }.execute();
}

    public void retrieveCharge(final String id){

        new AsyncTask<String, Void, Charge>() {
            @Override
            protected Charge doInBackground(String... params) {
                try{
                    charge=Charge.retrieve(id);
                    Log.d("Charge Retrieved","I");

                } catch (AuthenticationException e) {
                    e.printStackTrace();
                } catch (InvalidRequestException e) {
                    e.printStackTrace();
                } catch (APIConnectionException e) {
                    e.printStackTrace();
                } catch (CardException e) {
                    e.printStackTrace();
                } catch (APIException e) {
                    e.printStackTrace();
                }
                return charge;
            }
            @Override
            protected void onPostExecute(Charge charge) {
                super.onPostExecute(charge);
                captureCharge(charge);
            }
        }.execute(id);
    }

    public void captureCharge(final Charge charge){

        new AsyncTask<Charge, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Charge... params) {
                try {
                    charge.capture();
                } catch (AuthenticationException e) {
                    e.printStackTrace();
                } catch (InvalidRequestException e) {
                    e.printStackTrace();
                } catch (APIConnectionException e) {
                    e.printStackTrace();
                } catch (CardException e) {
                    e.printStackTrace();
                } catch (APIException e) {
                    e.printStackTrace();
                }
                Log.d("Charge Captured", "I");
                return charge.getCaptured();
            }

            @Override
            protected void onPostExecute(Boolean aVoid) {
                super.onPostExecute(aVoid);
                Log.d("Complete",aVoid.toString());
            }
        }.execute(charge);
    }


    public void onStart() {
        super.onStart();
        //TODO connect when adding AndroidPay
        //googleApiClient.connect();
    }

    public void onStop() {
        super.onStop();
        //TODO disconnect when adding AndroidPay
        googleApiClient.disconnect();
    }

    //Code Below For When Android Pay Is Released from Beta Mode
    public void androidPay(){
//        walletFragment =
//                (SupportWalletFragment) getSupportFragmentManager().findFragmentById(R.id.wallet_fragment);

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
                Log.d("UserToken",token.toString());
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