package sharehome.com.androidsharehome2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;

import sharehome.com.androidsharehome2.model.TaskList;

public class AddPostTab1 extends Fragment {
    private static final String TAG = "AddPostTab1";
    private Button btnTEST;
    private TextView goBack;
    private SwitchCompat urgentSwitch;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_post_tab1,container,false);
        btnTEST = (Button) view.findViewById(R.id.submitPost);
        goBack = (TextView) view.findViewById(R.id.goback);
        urgentSwitch = (SwitchCompat) view.findViewById(R.id.switchUrgent);
        btnTEST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    public void run() {
                        ApiClientFactory factory = new ApiClientFactory();
                        final AwscodestarsharehomelambdaClient client =
                                factory.build(AwscodestarsharehomelambdaClient.class);

                       TaskList taskList = client.taskGet("addTask", "sampleLambdagroup");
                        Toast.makeText(getActivity(), taskList.get(0).getGroupName(), Toast.LENGTH_LONG).show();
                        Log.d(TAG,taskList.get(0).getGroupName());
                    }
                }).start();


                //Toast.makeText(getActivity(), "adsfjsfsfasd"+ urgentSwitch.isChecked(),Toast.LENGTH_SHORT).show();
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
}
