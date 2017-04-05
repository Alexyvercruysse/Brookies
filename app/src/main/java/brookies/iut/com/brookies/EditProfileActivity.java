package brookies.iut.com.brookies;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import brookies.iut.com.brookies.model.User;

public class EditProfileActivity extends AppCompatActivity {

    ImageButton icon_checkprofile;
    RadioButton radioButtonMale,radioButtonFemale;
    ImageView image_1,image_2,image_3;
    EditText firstName,lastName;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "EditProfileActivity";
    private String userId;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        userId = getIntent().getStringExtra("userId");
        mAuth = FirebaseAuth.getInstance(); // Connexion FireBase
        mDatabase = FirebaseDatabase.getInstance().getReference();
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
                User user = snapshot.getValue(User.class);
                lastName.setText(user.getLastname());
                firstName.setText(user.getFirstname());
                Picasso.with(getApplication()).load(user.getPictures().get(0).getUrl()).into(image_1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        icon_checkprofile = (ImageButton) findViewById(R.id.icon_checkprofile);
        radioButtonMale = (RadioButton) findViewById(R.id.radioButtonMale);
        radioButtonFemale = (RadioButton) findViewById(R.id.radioButtonFemale);
        image_1 = (ImageView) findViewById(R.id.image_1);
        image_2 = (ImageView) findViewById(R.id.image_2);
        image_3 = (ImageView) findViewById(R.id.image_3);
        firstName = (EditText) findViewById(R.id.editText_firstname);
        lastName = (EditText) findViewById(R.id.editText_lastname);

        radioButtonMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButtonMale.isChecked()){
                    radioButtonFemale.setChecked(false);
                }
                else {
                    radioButtonMale.setChecked(false);
                    radioButtonFemale.setChecked(true);
                }
            }
        });
        radioButtonFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButtonFemale.isChecked()){
                    radioButtonMale.setChecked(false);
                }
                else {
                    radioButtonFemale.setChecked(false);
                    radioButtonMale.setChecked(true);
                }
            }
        });
    }


}
