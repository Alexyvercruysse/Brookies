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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import brookies.iut.com.brookies.model.RoomMetadata;
import brookies.iut.com.brookies.model.User;

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

        userId = getIntent().getStringExtra("userId");

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
        final HashMap<Map.Entry<String,RoomMetadata>, User> usersByRoom = new HashMap<Map.Entry<String,RoomMetadata>, User>();
        final ArrayList<User> users = new ArrayList<>();
        final CustomList adapter = new CustomList(UserListActivity.this, users);
        ListView list = (ListView) findViewById(R.id.listViewUsers);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int i = 0;
                for (Map.Entry<Map.Entry<String,RoomMetadata>, User> e : usersByRoom.entrySet()) {
                    if (i == position) {
                       //System.out.println("ROOM: " + e.getKey().getKey());
                        Intent intent = new Intent(UserListActivity.this,ChatActivity.class);
                        intent.putExtra("idchat",e.getKey().getKey());
                        startActivity(intent);
                    }
                    i++;
                }


            }
        });

        final List<String> myRooms = new ArrayList<>();
       //final List<UsersRoom> usersRooms = new ArrayList<UsersRoom>();
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


               Iterable<DataSnapshot> usersRooms = dataSnapshot.child("users_room/"+userId).getChildren();
        myRooms.clear();
               // System.out.println("CHAT1: "+usersRooms.size());
                for(DataSnapshot userRoom : usersRooms)
                {
                        System.out.println("add "+ userRoom.getValue());
                        myRooms.add((String)userRoom.getValue());

                }
                System.out.println("CHAT: "+myRooms.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        final  ArrayList<ValueEventListener> events = new ArrayList<ValueEventListener>();


        final HashMap<String,RoomMetadata> roomList = new HashMap<String,RoomMetadata>();

        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count ", "" + snapshot.child("room_metadata").getChildrenCount());
                users.clear();
                adapter.clear();
                roomList.clear();
                usersByRoom.clear();


                Log.e("DEBUG:",users.size()+", "+adapter.userList.size()+", "+roomList.size()+", "+myRooms.size());

                for(String roomId : myRooms)
                {
                    RoomMetadata roomMeta = snapshot.child("room_metadata/"+roomId).getValue(RoomMetadata.class);
                    roomList.put(roomId,roomMeta);

                }




                //  List<String> otherUserIdList = new ArrayList<String>();

              if(events.size()>0)
              {

                  for(ValueEventListener event : events)
                  {
                      FirebaseDatabase.getInstance().getReference().removeEventListener(event);
                  }

              }

                for (final Map.Entry<String,RoomMetadata> r : roomList.entrySet()) {
                    System.out.println("ROOM USERS: "+r.getValue().getUsers().size());
                    for (final String id : r.getValue().getUsers()) {
                        Log.e(userId, id);
                        if (!id.equals(userId)) {


                          ValueEventListener event =  FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User user = dataSnapshot.child("user/" + id).getValue(User.class);
                                    users.clear();

                                    adapter.clear();
                                    roomList.clear();
                                    Log.e("DEBUG2:",users.size()+", "+adapter.userList.size()+", "+roomList.size());
                                    Log.e(user.getFirstname(), r.getValue().getLastmessage());

                                    usersByRoom.put(r, user);


                                    // users = new ArrayList<>();


                                    Iterator<Map.Entry<Map.Entry<String,RoomMetadata>, User>> usersIterator = usersByRoom.entrySet().iterator();

                                    System.out.println("MAp SIZE: " + usersByRoom.size());
                                    users.clear();
                                    adapter.clear();
                                    while (usersIterator.hasNext()) {
                                        Map.Entry<Map.Entry<String,RoomMetadata>, User> entry = usersIterator.next();
                                        users.add(new User(entry.getValue().getFirstname(), entry.getValue().getLastname(), entry.getKey().getValue().getLastmessage(),entry.getValue().getPictures()));
                                    }

                                    adapter.notifyDataSetChanged();

       /* users.add(new User("Jean", "Pierre", "Coucou"));
        users.add(new User("Martin", "Bernard", "Hell oMMM MMMWW WWW WWWWHell oMMM MMMMMMMMMW WWWWWWW"));
        users.add(new User("Jean", "Pierre", "Coucou"));*/


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            events.add(event);
                           //
                        }
                    }
                }


                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: ", firebaseError.getMessage());
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageBitmap(textAsBitmap("+", 40, Color.WHITE));

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();


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
        if (providerId.equals("google.com")) {

        }
        if (providerId.equals("facebook.com")) {

        } else {

        }
    }
}
