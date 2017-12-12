package sharehome.com.androidsharehome2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;

import sharehome.com.androidsharehome2.model.Post;
import sharehome.com.androidsharehome2.model.ResultStringResponse;

public class AddPostTab1 extends Fragment {
    private static final String TAG = "AddPostTab1";
    private Button btnTEST;
    private TextView goBack;
    private SwitchCompat urgentSwitch;
    private boolean isUrgent = false;
    EditText _titleEditText;
    EditText _contextEditText;
    Integer newTaskID;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_post_tab1,container,false);
        btnTEST = (Button) view.findViewById(R.id.submitPost);
        goBack = (TextView) view.findViewById(R.id.goback);
        urgentSwitch = (SwitchCompat) view.findViewById(R.id.switchUrgent);
        _titleEditText = (EditText) view.findViewById(R.id.PostTitleInput);
        _contextEditText = (EditText) view.findViewById(R.id.PostContentInput);

       // urgentSwitch.setChecked(false);
        urgentSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                     isUrgent =! isUrgent;
            }
        });

        btnTEST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_titleEditText.getText().toString().equals(""))
                {
                    Toast.makeText(getActivity(), "Post should have a title",Toast.LENGTH_SHORT).show();
                }
                else
                {
                final ProgressDialog  progressDialog = new ProgressDialog(getContext(),
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Posting...");
                progressDialog.show();
                new Thread(new Runnable() {
                    postSubmitHanlder handler = new postSubmitHanlder(getActivity().getMainLooper());
                    public void run() {
                        if (AppHelper.getCurrgroupName() == null){
                            return;
                        }

                        Post newPost = new Post();
                        newPost.setGroupName(AppHelper.getCurrgroupName());
                        String title = _titleEditText.getText().toString();
                        Log.d(TAG, "title: " + title);
                        Log.d(TAG, "postUrgent: " + urgentSwitch.getShowText());

                        newPost.setPostTitle(title);
                        String content = _contextEditText.getText().toString();
                        Log.d(TAG, "content: " + content);
                        newPost.setPostContent(content);
                      //  boolean isUrgent = urgentSwitch.getShowText();
                        newPost.setPostUrgent(isUrgent);

                        final ResultStringResponse response = UserActivity.client.postPost(newPost, "add");
                        String newPostID = response.getResult();
                        Log.d(TAG, "Post ID: " + newPostID.toString());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Post successful!",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
                }
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Cancelled",Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });

        return view;
    }
    public Integer getNewTaskID(){
        return newTaskID;
    }
}
class postSubmitHanlder extends Handler {
    public postSubmitHanlder(Looper myLooper) {
        super(myLooper);
    }
}


