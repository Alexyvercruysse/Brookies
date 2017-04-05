package brookies.iut.com.brookies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

import brookies.iut.com.brookies.model.User;

public class UserListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        ArrayList<User> users = new ArrayList<>();
        users.add(new User("Jean","Pierre","Coucou"));
        users.add(new User("Martin","Bernard","Hello"));

        CustomList adapter = new CustomList(this,users);
        ListView list =(ListView)findViewById(R.id.listViewUsers);
        list.setAdapter(adapter);


    }
}
