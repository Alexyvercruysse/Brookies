package brookies.iut.com.brookies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import brookies.iut.com.brookies.model.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private String userId;
    private TextView tvNameProfile;
    private CircleImageView profileImage;
    private SwitchCompat manSwitch;
    private SwitchCompat womanSwitch;
    private User user;
    private FloatingActionButton editProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userId = getIntent().getStringExtra("userId");
        manSwitch = (SwitchCompat) findViewById(R.id.man_switch);
        womanSwitch = (SwitchCompat) findViewById(R.id.woman_switch);
        tvNameProfile = (TextView) findViewById(R.id.tvName);
        mAuth = FirebaseAuth.getInstance(); // Connexion FireBase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        editProfileButton = (FloatingActionButton) findViewById(R.id.editProfileFloatingButton);
        profileImage = (CircleImageView) findViewById(R.id.profile_image);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    userId = user.getUid();
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };


        mDatabase.child("user/"+userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                tvNameProfile.setText(user.getFirstname() + " " + user.getLastname());
                String urlPictureProfilePath = user.getPictures().get(0).getUrl();
                manSwitch.setChecked(user.getLikeMen());
                womanSwitch.setChecked(user.getLikeWomen());
                new LoadProfilePictureTask().execute(urlPictureProfilePath);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfile = new Intent(ProfileActivity.this,EditProfileActivity.class);
                editProfile.putExtra("userId",userId);
                startActivity(editProfile);
            }
        });

    }


    class LoadProfilePictureTask extends AsyncTask<String, Void, Bitmap> {
        protected void onPreExecute() {
        }

        protected Bitmap doInBackground(String... urlPath) {
            Bitmap myBitmap = null;
            try {
                URL url = new URL(urlPath[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return myBitmap = BitmapFactory.decodeStream(input);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return myBitmap;
        }


        protected void onPostExecute(Bitmap result) {
            profileImage.setImageBitmap(result);
        }
    }

}