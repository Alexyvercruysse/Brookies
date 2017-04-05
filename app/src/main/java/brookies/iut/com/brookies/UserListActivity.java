package brookies.iut.com.brookies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import brookies.iut.com.brookies.model.User;
import brookies.iut.com.brookies.model.UsersRoom;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class UserListActivity extends AppCompatActivity {


    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        mAuth = FirebaseAuth.getInstance(); // Connexion FireBase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    userId = user.getUid();

                    updateUI(user.getProviderData().get(1).getProviderId());

                } else {

                }
            }
        };






        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                UsersRoom room = dataSnapshot.getValue(UsersRoom.class);
                System.out.println("ROOM CHAT SIZE: "+room.getUsersChats().size());
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("A", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.addValueEventListener(postListener);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setImageBitmap(textAsBitmap("+", 40, Color.WHITE));

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        ArrayList<User> users = new ArrayList<>();
        users.add(new User("Jean","Pierre","Coucou"));
        users.add(new User("Martin","Bernard","Hell oMMM MMMWW WWW WWWWHell oMMM MMMMMMMMMW WWWWWWW"));
        users.add(new User("Jean","Pierre","Coucou"));

        CustomList adapter = new CustomList(this,users);
        ListView list =(ListView)findViewById(R.id.listViewUsers);
        list.setAdapter(adapter);


    }
    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    private void updateUI(String providerId) {
        if (providerId.equals("google.com")){

        }
        if (providerId.equals("facebook.com")){

        }
        else {

        }
    }
}
