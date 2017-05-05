package brookies.iut.com.brookies;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import brookies.iut.com.brookies.model.User;

public class EditProfile2Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "EditProfileActivity2";
    private String userId;
    private DatabaseReference mDatabase;
    private EditText editTextDescription, editTextHobbies;
    private User user;
    private ImageView checkProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile2);
        editTextDescription = (EditText) findViewById(R.id.editText_description);
        editTextHobbies = (EditText) findViewById(R.id.editText_hobbies);
        checkProfile = (ImageView) findViewById(R.id.icon_checkprofile);
        userId = getIntent().getStringExtra("userId");
        mAuth = FirebaseAuth.getInstance();
        final FirebaseStorage storage = FirebaseStorage.getInstance();
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
                user = snapshot.getValue(User.class);
                if (user.getDescription() != null && !user.getDescription().equals("")){
                    editTextDescription.setText(user.getDescription());
                }
                if (user.getHobbies() != null && !user.getHobbies().equals("")){
                    editTextHobbies.setText(user.getHobbies());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        checkProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextDescription.getText().toString() == null && editTextDescription.getText().toString().equals("") || editTextHobbies.getText().toString() == null && editTextHobbies.getText().toString().equals("")){
                    if (editTextDescription.getText().toString().equals("")){
                        editTextDescription.setError("Need a description");
                    }
                    else {
                        editTextDescription.setError("");
                    }

                    if (editTextHobbies.getText().toString().equals("")){
                        editTextHobbies.setError("Need a hobbies");
                    }
                    else {
                        editTextHobbies.setError("");
                    }
                }
                else {
                    user.setDescription(editTextDescription.getText().toString());
                    user.setHobbies(editTextHobbies.getText().toString());
                    mDatabase.child("user").child(userId).setValue(user);
                    Intent intent = new Intent(EditProfile2Activity.this,ProfileActivity.class);
                    intent.putExtra("userId",userId);
                    startActivity(intent);
                }
            }
        });


    }
}
