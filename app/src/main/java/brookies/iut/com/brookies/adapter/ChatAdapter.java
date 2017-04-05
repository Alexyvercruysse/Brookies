package brookies.iut.com.brookies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import brookies.iut.com.brookies.R;
import brookies.iut.com.brookies.model.Message;

/**
 * Created by Clém on 05/04/2017.
 */

public class ChatAdapter extends ArrayAdapter<Message> {

    public ChatAdapter(Context context, List<Message> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message, parent, false);
        }

        ChatViewHolder chatViewHolder = (ChatViewHolder) convertView.getTag();

        if(chatViewHolder == null)
        {
            chatViewHolder = new ChatViewHolder();
            chatViewHolder.auteur = (TextView) convertView.findViewById(R.id.message_user);
            chatViewHolder.message = (TextView) convertView.findViewById(R.id.message_text);
            convertView.setTag(chatViewHolder);
        }

        Message chat = getItem(position);

        chatViewHolder.auteur.setText(chat.getAuthor());
        chatViewHolder.message.setText(chat.getContent());

        return convertView;
    }

    private class ChatViewHolder
    {
        public TextView auteur;
        public TextView message;
    }
}
