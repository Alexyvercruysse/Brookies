package brookies.iut.com.brookies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

import brookies.iut.com.brookies.model.User;
import brookies.iut.com.brookies.model.UserMatch;
import brookies.iut.com.brookies.model.UserMatchs;

public class MainMatch extends AppCompatActivity {

    private static final String TAG = "MainMatchActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private String userId;
    private User user;
    private FrameLayout progressBarHolder;
    //HashMap<String,Boolean> usersMatch;
    HashMap<String, Boolean> userMatchs;
    HashMap<String,User> users;
    HashMap<String, User> usersBySexeForCurrent;
    HashMap<String, User> usersBySexeForUsersSelected;
    HashMap<String, User> usersBySexeByMatchForCurrent;
    HashMap<String, User> usersBySexeByMatchForUserSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_match);
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
                user = snapshot.getValue(User.class);
                loadMatch();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        progressBarHolder.setVisibility(View.VISIBLE);




    }




    private void formatUsersPreference(){
        if (user.getLikeMen() && user.getLikeWomen()){

        }
        else if (user.getLikeMen()){
            sortByGenderForCurrent("male");

        }
        else {
            sortByGenderForCurrent("female");
        }

    }

    private void sortByGenderForCurrent(String sexe) {
        Log.d(TAG,sexe);
        usersBySexeForCurrent = new HashMap<>();
        for(Map.Entry<String, User> entry : users.entrySet()) {
            String key = entry.getKey();
            User value = entry.getValue();

            if (value.getSexe().equals(sexe)){
                usersBySexeForCurrent.put(key, value);
            }
        }

        sortByGenderForSelectedUsers();


    }

    private void sortByGenderForSelectedUsers() {
        usersBySexeByMatchForUserSelected = new HashMap<>();
        for(Map.Entry<String, User> entry : usersBySexeForCurrent.entrySet()) {
            String key = entry.getKey();
            User value = entry.getValue();

            if (value.getLikeMen() && user.getSexe().equals("male")){
                usersBySexeByMatchForUserSelected.put(key, value);
            }
            if (value.getLikeWomen() && user.getSexe().equals("female")){
                usersBySexeByMatchForUserSelected.put(key,value);
            }


        }


        loadUserMatch();

    }

    private void sortByMatchForCurrent() {
        usersBySexeByMatchForCurrent = new HashMap<>();
        for (Map.Entry<String, User> entry : usersBySexeByMatchForUserSelected.entrySet()){
            for (Map.Entry<String, Boolean> entry2 : userMatchs.entrySet()){
                if (entry.getKey().equals(entry2.getKey())){
                    if (entry2.getValue()){
                        usersBySexeByMatchForCurrent.put(entry.getKey(),entry.getValue());
                    }
                }
            }
        }

        for (Map.Entry<String, User> entry : usersBySexeByMatchForCurrent.entrySet()){
            Log.d(TAG, entry.getValue().getFirstname());
        }

        updateUI();
    }

    private void updateUI() {
    }

    private void loadUserMatch() {
        userMatchs = new HashMap<String, Boolean>();
        mDatabase.child("usermatchs").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot userssnapShot : snapshot.getChildren()) {

                    userMatchs.put(userssnapShot.getKey(), (Boolean) userssnapShot.getValue(Boolean.class));

                }
               sortByMatchForCurrent();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private void loadMatch() {
        users = new HashMap<>();
        mDatabase.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot userSnapshot :snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    users.put(userSnapshot.getKey(),user);
                    Log.d(TAG,"Size "+users.size()+" count : "+snapshot.getChildrenCount());
                    if (users.size() == snapshot.getChildrenCount()){
                        formatUsersPreference();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }
}
