package brookies.iut.com.brookies;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

import brookies.iut.com.brookies.adapter.ChatAdapter;
import brookies.iut.com.brookies.model.Chat;
import brookies.iut.com.brookies.model.Message;

public class ChatActivity extends AppCompatActivity {


    private static final String TAG = "ChatActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance(); // Connexion FireBase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        DatabaseReference chat = mDatabase.child("room_messages").child("idchat1");


        // Listener De connexion
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

        FloatingActionButton fab =
                (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText) findViewById(R.id.input);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                DatabaseReference chat = mDatabase.child("room_messages").child("idchat1");

                chat.push()
                        .setValue(new Message(FirebaseAuth.getInstance().getCurrentUser().getUid().toString(),
                                input.getText().toString(),
                                String.valueOf(System.currentTimeMillis() / 1000))
                        );

                // Clear the input
                input.setText("");
            }
        });
        mDatabase.child("room_messages/idchat1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Message chat1 = dataSnapshot.getValue(Message.class);
                chat1.getContent();
                Log.d("msg",(chat1.getContent()).toString());
            }
            //ChatAdapter chatAdapter = new ChatAdapter(ChatActivity.this, chat.getMessages());
             //  chatListView.setAdapter(chatAdapter);

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });




    }
}
