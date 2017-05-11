package brookies.iut.com.brookies;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rethoughtsolutions.stripecardentry.StripeCardEntry;
import com.squareup.picasso.Picasso;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import brookies.iut.com.brookies.API.APIClient;
import brookies.iut.com.brookies.model.ParamsPayment;
import brookies.iut.com.brookies.model.ResponsePayment;
import brookies.iut.com.brookies.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {

    private static final String PREMIUM_PRICE = "4.99";

    private String userId;
    private User user;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        userId = getIntent().getStringExtra("userId");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("eee", "onAuthStateChanged:signed_in:" + user.getUid());
                    userId = user.getUid();
                } else {
                    Log.d("eee", "onAuthStateChanged:signed_out");
                }
            }
        };
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("user/"+userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        final StripeCardEntry stripeCardEntry = (StripeCardEntry) findViewById(R.id.act_payment_sce_cardinput);
        stripeCardEntry.setListener(new StripeCardEntry.Listener() {
            @Override
            public void onCardEntryCompleted(boolean completed) {
                Card cardToSave = new Card(stripeCardEntry.getNumber(), stripeCardEntry.getExpiryMonth(), stripeCardEntry.getExpiryYear(), stripeCardEntry.getCVC());
                if (cardToSave == null) {
                    Log.d("Erreur", "Invalid Card Data");
                    return;
                }

                Stripe stripe = new Stripe(PaymentActivity.this, "pk_test_duyer4UlMe33noGxo2jYayuY");

                stripe.createToken(
                        cardToSave,
                        new TokenCallback() {
                            public void onSuccess(Token token) {
                                Log.d("TOKEN", token.getId());
                                // Send token to your own web service
                                ParamsPayment paramsPayment = new ParamsPayment(token.getId(), PREMIUM_PRICE);
                                Call<ResponsePayment> callPayment = APIClient.getApiInterface().getPaymentValidation(paramsPayment);
                                callPayment.enqueue(new Callback<ResponsePayment>() {
                                    @Override
                                    public void onResponse(Call<ResponsePayment> call, Response<ResponsePayment> response) {
                                        if (response.isSuccessful()){
                                            Boolean paymentSuccess = response.body().isStatut();
                                            if (paymentSuccess){
                                                Toast.makeText(PaymentActivity.this,
                                                        "Paiement accepté",
                                                        Toast.LENGTH_LONG).show();

                                                // Modification de la base de données
                                                user.setIsPremium(true);
                                                mDatabase.child("user/"+userId).setValue(user);
                                                finish();

                                            } else {
                                                Toast.makeText(PaymentActivity.this,
                                                        "Paiement refusé",
                                                        Toast.LENGTH_LONG).show();
                                            }

                                        } else {
                                            Toast.makeText(PaymentActivity.this,
                                                    "Erreur réponse",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponsePayment> call, Throwable t) {
                                        Toast.makeText(PaymentActivity.this,
                                                "Erreur\nPayement annulé",
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                            public void onError(Exception error) {
                                Toast.makeText(PaymentActivity.this,
                                        error.getLocalizedMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                );
            }
        });

    }
}
