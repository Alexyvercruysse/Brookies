package brookies.iut.com.brookies;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginEmailActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button inscription;
    private EditText email,password;
    private static final String TAG = "LoginEmailActivity";
    private String mail,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

        inscription = (Button) findViewById(R.id.buttonInscription);
        email = (EditText) findViewById(R.id.editTextEmail);
        password = (EditText) findViewById(R.id.editTextPassword);

        mAuth = FirebaseAuth.getInstance(); // Connexion FireBase

        // Créer un User et le connecte  -- MAIL --
        inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().equals("")){
                    email.setError("Vous devez rentrer un Email");
                }
                else {
                    mail = email.getText().toString();
                }
                if (password.getText().toString().equals("")){
                    password.setError("Vous devez rentrer un mot de passe");
                }
                else {
                    pass = password.getText().toString();
                }
                if (!email.getText().toString().equals("") && !password.getText().toString().equals("")){
                    createUser(mail,pass);
                }

            }
        });

        // Listener De la connexion si la personne se connecte ou se deconnecte
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    // Create User on Firebase with mail mdp. Generate ID
    public void createUser(String email,String motDePasse){
        mAuth.createUserWithEmailAndPassword(email, motDePasse)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginEmailActivity.this, "Auth Failed",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }

                });
    }



    @Override
    public void onStart() {
        super.onStart();
        // Calcul l'état de connexion
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
