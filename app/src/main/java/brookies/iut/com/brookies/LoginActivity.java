package brookies.iut.com.brookies;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import brookies.iut.com.brookies.model.Picture;
import brookies.iut.com.brookies.model.User;


public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;
    CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private Button loginButtonGoogle;
    private LoginButton loginButtonFacebbok;
    private DatabaseReference mDatabase;
    private String userId;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        loginButtonGoogle = (Button) findViewById(R.id.login_button_google);
        loginButtonFacebbok = (LoginButton) findViewById(R.id.login_button_facebook);


        // Listener De connexion
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fireuser = firebaseAuth.getCurrentUser();
                if (fireuser != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + fireuser.getUid());
                    userId = fireuser.getUid();
                    if (user != null) {
                        writeNewUser(user);
                    }
                    else {
                        Intent intent = new Intent(LoginActivity.this,EditProfileActivity.class);
                        intent.putExtra("userId",userId);
                        startActivity(intent);
                    }
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };



        loginButtonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        // Connexion Facebook
        callbackManager = CallbackManager.Factory.create();

        loginButtonFacebbok.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        loginButtonFacebbok.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {

                                    user = new User();
                                    user.setBirthdate(object.getString("birthday"));
                                    user.setFirstname(object.getString("first_name"));
                                    user.setSexe(object.getString("gender"));
                                    user.setLastname(object.getString("last_name"));
                                    user.setEmail(object.getString("email"));
                                    user.addPicture(new Picture("facebookphoto","https://graph.facebook.com/" + object.getString("id") + "/picture?type=large"));
                                    user.setHobbies("");
                                    user.setDescription("");
                                    user.setLikeMen(false);
                                    user.setLikeWomen(false);
                                    user.setIsPremium(false);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,first_name,last_name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void writeNewUser(User user) {
        if (user.getPictures() == null){
            user.addPicture(new Picture("failed","http://ajdeguzman.x10.mx/blog/wp-content/uploads/2015/10/facebook-android.jpg"));
        }
        mDatabase.child("user").child(userId).setValue(user);
        Intent intent = new Intent(LoginActivity.this,EditProfileActivity.class);
        intent.putExtra("userId",userId);
        startActivity(intent);
    }



    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                user = new User();
                GoogleSignInAccount account = result.getSignInAccount();
                user.setBirthdate("");
                user.setDescription("");
                if (account.getEmail() != null) {
                    user.setEmail(account.getEmail());
                }
                if (account.getGivenName() != null){
                    user.setFirstname(account.getGivenName());
                }
                if (account.getFamilyName() != null){
                    user.setLastname(account.getFamilyName());
                }
                user.setHobbies("");
                user.setSexe("");
                user.setLikeMen(false);
                user.setLikeWomen(false);
                if (account.getPhotoUrl() != null){
                    user.addPicture(new Picture(account.getGivenName()+"_photo",account.getPhotoUrl().toString()));
                }
                firebaseAuthWithGoogle(account);


            } else {
                Toast.makeText(this,"Failed",Toast.LENGTH_LONG);
            }
        }
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
