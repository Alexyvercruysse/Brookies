package brookies.iut.com.brookies;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import brookies.iut.com.brookies.model.User;

/**
 * Created by iem on 05/04/2017.
 */

public class CustomList extends ArrayAdapter<User> {

    private final Activity context;
    ArrayList<User> userList;
    public CustomList(@NonNull Activity context,  @NonNull List<User> users) {
        super(context, R.layout.list_single, users);
        this.context = context;
        this.userList = (ArrayList<User>) users;


        System.out.println("USERS: "+users.size());
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView=null;
        if(userList.size()>0) {
            if(userList.get(0).getPictures()!=null)
            System.out.println("PICS: " + userList.get(0).getPictures().size());
        }
        System.out.println("POSITION: "+position);
        rowView = inflater.inflate(R.layout.list_single, parent, false);

        CircularImageView imageView = (CircularImageView) rowView.findViewById(R.id.imgProfilePicture);

        TextView txtUserName = (TextView) rowView.findViewById(R.id.txtUserName);
        TextView txtUserDescrition = (TextView) rowView.findViewById(R.id.txtDescription);

       // imageView.setImageResource(R.drawable.profile);
        //imageView.setImageURI(Uri.parse(userList.get(position).getPictures().get(0).getUrl()));
        Picasso.with(context).load(userList.get(position).getPictures().get(0).getUrl()).into(imageView);
        txtUserName.setText(userList.get(position).getFirstname() +" " + userList.get(position).getLastname() );
        txtUserDescrition.setText(userList.get(position).getDescription());




        return rowView;
    }

}
