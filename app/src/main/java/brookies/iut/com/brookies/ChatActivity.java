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

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import brookies.iut.com.brookies.adapter.ChatAdapter;
import brookies.iut.com.brookies.model.Chat;
import brookies.iut.com.brookies.model.Message;

public class ChatActivity extends AppCompatActivity {


    private static final String TAG = "ChatActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private String userId;
    private FirebaseListAdapter<Message> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final String idchat = getIntent().getStringExtra("idchat");

        mAuth = FirebaseAuth.getInstance(); // Connexion FireBase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        DatabaseReference chat = mDatabase.child("room_messages").child(idchat);


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

                if (input.equals("")) {
                    return;
                }

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                DatabaseReference chat = mDatabase.child("room_messages").child(idchat);

                chat.push()
                        .setValue(new Message(FirebaseAuth.getInstance().getCurrentUser().getUid().toString(),FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toString(),
                                input.getText().toString(),System.currentTimeMillis())
                        );

                // Clear the input
                input.setText("");
            }
        });
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<Message>(this, Message.class,
                R.layout.message, mDatabase.child("room_messages/"+ idchat).orderByChild("date")) {

            @Override
            protected void populateView(View v, Message model, int position) {

                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                //TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                //TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getContent());
                //messageUser.setText(model.getAuthor());
                DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                Date netDate = (new Date());
                netDate.setTime(model.getDate());

                // Format the date before showing it
               //messageTime.setText(sdf.format(netDate));
            }
        };
        listOfMessages.setAdapter(adapter);
        listOfMessages.requestFocus();
    }
}
