package sharehome.com.androidsharehome2;
import sharehome.com.androidsharehome2.backend.*;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import sharehome.com.androidsharehome2.backend.Post;

import static com.facebook.FacebookSdk.getApplicationContext;
import static sharehome.com.androidsharehome2.R.id.fab;
import static sharehome.com.androidsharehome2.R.id.upvotes;


/**
 * Created by zhigong on 4/16/2017.
 */

public class PostAdapter extends ArrayAdapter<Post> {
    public PostAdapter(Context context, List<Post> posts){
        super(context, 0, posts);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.post_list_item, parent, false);
        }
        final Post currentPost = getItem(position);
        TextView titleView = (TextView) listItemView.findViewById(R.id.PostTitle);
        titleView.setText(currentPost.getPostTitle());
        TextView contentView = (TextView) listItemView.findViewById(R.id.PostContent);
        contentView.setText(currentPost.getContent());
        final Button upvoteView = (Button) listItemView.findViewById(upvotes);
        upvoteView.setText(Integer.toString(currentPost.getUpVote()) + "\u2191");
        upvoteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:only local changes yet, not transmitting to server
                currentPost.setUpVote(9);
                upvoteView.setText(Integer.toString(currentPost.getUpVote()));
            }
        });

        Button downvoteView = (Button) listItemView.findViewById(R.id.downvotes);
        downvoteView.setText(Integer.toString(currentPost.getDownVote()) + " \u2193");
        return listItemView;
    }
}
