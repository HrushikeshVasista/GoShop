package com.cs442.Team14;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cs442.Team14.dummy.CartFragmentStuff;
import com.cs442.Team14.dummy.OrderHistoryStuff;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.paypal.android.sdk.payments.ShippingAddress;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
/**
 * Payment activity for users to checkout.
 *
 * @author Vasudev Gawde
 * @since 2-May-2016
 */
public class Goshop_PaymentActivity extends AppCompatActivity {


    private static final String TAG = "GoShopPayment";
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    private static final String CONFIG_CLIENT_ID = "ASBJ1ZQ1SL_t7Q7_cLC1Rbues_LzIq9afWdMUKSZ0YDN6zYPVznMROpKWhByspWGWTc4Ubv8q5HeJd_c";
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;
    private Button showCartBut;
    private TextView txt_Order;
    private TextView txt_total;
    private DBOperations dbOperations;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            .acceptCreditCards(true)
                    // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Example Merchant")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        txt_Order = (TextView) findViewById(R.id.payment_txt_order);
        txt_total = (TextView) findViewById(R.id.payment_txt_total);
        txt_total.setText("$"+Double.toString(CartFragmentStuff.getCartTotalAmount()));
        Double tempDouble = CartFragmentStuff.getCartTotalAmount()+ 0.00;
        txt_Order.setText("$"+Double.toString(tempDouble));
        dbOperations = new DBOperations(this);
        Intent intent = new Intent(Goshop_PaymentActivity.this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
    }

    public void onBuyPressed(View pressed) {
        PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(CartFragmentStuff.getCartTotalAmount()), "USD", "Total : ",
                PayPalPayment.PAYMENT_INTENT_SALE);
        // Created intent object to call payment activity
        Intent intent = new Intent(Goshop_PaymentActivity.this, PaymentActivity.class);
        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }
    //Add shipment addresss by calling this method
    private void addAppProvidedShippingAddress(PayPalPayment paypalPayment) {
        ShippingAddress shippingAddress =
                new ShippingAddress().recipientName("Mom Parker").line1("52 North Main St.")
                        .city("Austin").state("TX").postalCode("78729").countryCode("US");
        paypalPayment.providedShippingAddress(shippingAddress);
    }

    /*
     * Enable retrieval of shipping addresses from buyer's PayPal account
     */
    private void enableShippingAddressRetrieval(PayPalPayment paypalPayment, boolean enable) {
        paypalPayment.enablePayPalShippingAddressesRetrieval(enable);
    }

    public void onFuturePaymentPressed(View pressed) {
        Intent intent = new Intent(Goshop_PaymentActivity.this, PayPalFuturePaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                // Successful
                if (confirm != null) {
                    try {
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                        String itemList="";
                        Double subtotal = 0.0;
                        Random rn = new Random();
                        int  oid = rn.nextInt(1000 - 0 + 1) + 0;
                        if(CartFragmentStuff.ITEMS.size()!=0)
                        {
                            for (int i = 0; i < CartFragmentStuff.ITEMS.size(); i++) {
                                System.out.println(CartFragmentStuff.ITEMS.get(i));
                                //subtotal+= CartFragmentStuff.ITEMS.get(i).getSubtotal();
                                if(i==0)
                                    itemList=CartFragmentStuff.ITEMS.get(i).toString();
                                else
                                    itemList=itemList+"\n"+CartFragmentStuff.ITEMS.get(i).toString();
                            }

                            OrderHistoryStuff.OrderItem order = new OrderHistoryStuff.OrderItem(oid, itemList, CartFragmentStuff.getCartTotalAmount(), sdf.format(date));
                            OrderHistoryStuff.ITEMS.add(order);
                            dbOperations.insertData(GoShopApplicationData.OrderHistoryTableInfo.TableName, order);


                        }
                        CartFragmentStuff.ITEMS.clear();
                        Intent intent = new Intent();
                        intent.putExtra("OrderId", oid);
                        intent.putExtra("OrderItems", itemList);
                        intent.putExtra("OrderAmount", CartFragmentStuff.getCartTotalAmount());
                        setResult(GoShopApplicationData.PAYMENT_SUCCESSFUL, intent);
                        finish();
                    } catch (Exception e) {

                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                setResult(GoShopApplicationData.PAYMENT_FAILURE);

            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {

            }
        }
    }

    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

}
