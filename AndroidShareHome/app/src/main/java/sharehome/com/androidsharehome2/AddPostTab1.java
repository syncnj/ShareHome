package sharehome.com.androidsharehome2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;

import butterknife.BindView;
import sharehome.com.androidsharehome2.model.PostResponse;
import sharehome.com.androidsharehome2.model.Task;
import sharehome.com.androidsharehome2.model.TaskList;

public class AddPostTab1 extends Fragment {
    private static final String TAG = "AddPostTab1";
    private Button btnTEST;
    private TextView goBack;
    private SwitchCompat urgentSwitch;
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

        btnTEST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog  progressDialog = new ProgressDialog(getContext(),
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Posting tasks...");
                progressDialog.show();
                new Thread(new Runnable() {
                    taskSubmitHandler handler = new taskSubmitHandler(getActivity().getMainLooper());
                    public void run() {
                        ApiClientFactory factory = new ApiClientFactory();
                        final AwscodestarsharehomelambdaClient client =
                                factory.build(AwscodestarsharehomelambdaClient.class);
                        Task newTask = new Task();
                        newTask.setGroupName("testGroupName3");
                        String title = _titleEditText.getText().toString();
                        Log.d(TAG, "title: " + title);

                        newTask.setTaskTitle(title);
                        String content = _contextEditText.getText().toString();
                        Log.d(TAG, "content: " + content);
                        newTask.setTaskContent(content);
                        newTask.setTaskDuration(502);
                        newTask.setTaskUser(AppHelper.getCurrUser());
                        newTask.setTaskSolved(false);
                       final PostResponse response = client.taskPost(newTask, "add");
                        newTaskID = response.getTaskID();
                        Log.d(TAG, "new Task ID: " + newTaskID.toString());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "post tasks successful",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
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
class taskSubmitHandler extends Handler {
    public taskSubmitHandler(Looper myLooper) {
        super(myLooper);
    }
}


