package brookies.iut.com.brookies;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import brookies.iut.com.brookies.model.Picture;
import brookies.iut.com.brookies.model.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class CandidateDetailActivity extends AppCompatActivity {

    private SliderLayout sliderShow;

    private static final String TAG = "CandidateDetailActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private User user;
    private String userId;
    private TextView tvName;
    private TextView tvAge;
    private TextView tvDescription;
    private TextView tvHobbies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_detail);
        userId = getIntent().getStringExtra("userId");

        sliderShow = (SliderLayout) findViewById(R.id.slider);
        tvName = (TextView) findViewById(R.id.tvName);
        tvAge = (TextView) findViewById(R.id.tvAge);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvHobbies = (TextView) findViewById(R.id.tvHobbies);

        sliderShow.setPresetTransformer(SliderLayout.Transformer.Default);

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
                tvName.setText(user.getFirstname() + " " + user.getLastname() +", ");
                tvAge.setText(getAge(user.getBirthdate()));
                tvDescription.setText(user.getDescription());
                tvHobbies.setText(user.getHobbies());

                for(Picture picture : user.getPictures()){
                    DefaultSliderView defaultSliderView = new DefaultSliderView(CandidateDetailActivity.this);
                    defaultSliderView.image(picture.getUrl());
                    sliderShow.addSlider(defaultSliderView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    @Override
    protected void onStop() {
        sliderShow.stopAutoCycle();
        super.onStop();
    }


    private String getAge(String birthDate){
        String[] birthDateSeparated = birthDate.split("/");
        
        int year = Integer.parseInt(birthDateSeparated[2]);
        int month= Integer.parseInt(birthDateSeparated[1]);
        int day= Integer.parseInt(birthDateSeparated[0]);
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

}

