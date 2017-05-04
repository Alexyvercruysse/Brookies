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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_detail);

        sliderShow = (SliderLayout) findViewById(R.id.slider);
        tvName = (TextView) findViewById(R.id.tvName);
        tvAge = (TextView) findViewById(R.id.tvAge);
        tvDescription = (TextView) findViewById(R.id.tvDescription);



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

        mDatabase.child("user/PSgOMpkputeiPpRp4ppWsSAJjcM2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                tvName.setText(user.getFirstname() + " " + user.getLastname() +", ");

                Calendar calendar = Calendar.getInstance();
                calendar.get(Calendar.YEAR);
                String age = user.getBirthdate();
                //tvAge.setText(user.get);

           //     Picasso.with(getApplication()).load(user.getPictures().get(0).getUrl()).into(profileImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });




        DefaultSliderView defaultSliderView = new DefaultSliderView(this);
        defaultSliderView
                .image("http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        DefaultSliderView defaultSliderView2 = new DefaultSliderView(this);
        defaultSliderView2
                .image("https://lh6.googleusercontent.com/-CkxB35ot8bI/AAAAAAAAAAI/AAAAAAAAAAA/AHalGhqYUjgdy-6umTwVV1chIiPi7W2YCg/s96-c/photo.jpg");


        sliderShow.addSlider(defaultSliderView);
        sliderShow.addSlider(defaultSliderView2);

    }

    @Override
    protected void onStop() {
        sliderShow.stopAutoCycle();
        super.onStop();
    }


    private String getAge(int year, int month, int day){
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

