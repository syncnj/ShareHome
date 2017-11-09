package sharehome.com.androidsharehome2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;

public class AddPostTab1 extends Fragment {
    private static final String TAG = "AddPostTab1";
    @BindView(R.id.goback) TextView _cancel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_post_tab1,container,false);
        _cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               
            }
        });
        return view;
    }
}
