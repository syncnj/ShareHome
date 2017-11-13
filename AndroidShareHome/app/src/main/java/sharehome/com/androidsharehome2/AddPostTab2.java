package sharehome.com.androidsharehome2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import java.util.Calendar;

public class AddPostTab2 extends Fragment {
    private static final String TAG = "AddPostTab1";
    private Button btnTEST;
    private TextView goBack;
    private SwitchCompat urgentSwitch;
    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_post_tab2,container,false);
        btnTEST = (Button) view.findViewById(R.id.submitPost);
        goBack = (TextView) view.findViewById(R.id.goback);
        urgentSwitch = (SwitchCompat) view.findViewById(R.id.switchUrgent);
        btnDatePicker=(Button) view.findViewById(R.id.btn_date);
        btnTimePicker=(Button) view.findViewById(R.id.btn_time);
        txtDate=(EditText) view.findViewById(R.id.in_date);
        txtTime=(EditText) view.findViewById(R.id.in_time);

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "TESTING BUTTON CLICK 2",Toast.LENGTH_SHORT).show();
            }
        });
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "TESTING BUTTON CLICK 2",Toast.LENGTH_SHORT).show();
            }
        });
        btnTEST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "TESTING BUTTON CLICK 2",Toast.LENGTH_SHORT).show();
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
