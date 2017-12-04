package sharehome.com.androidsharehome2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AddPostTab3 extends Fragment {
    private static final String TAG = "AddPostTab1";
    private Button btnTEST;
    private TextView goBack;
    private SwitchCompat urgentSwitch;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_post_tab3,container,false);
        btnTEST = (Button) view.findViewById(R.id.submitPost);
        goBack = (TextView) view.findViewById(R.id.goback);
        urgentSwitch = (SwitchCompat) view.findViewById(R.id.switchUrgent);
        btnTEST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "TESTING BUTTON CLICK 3",Toast.LENGTH_SHORT).show();
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
