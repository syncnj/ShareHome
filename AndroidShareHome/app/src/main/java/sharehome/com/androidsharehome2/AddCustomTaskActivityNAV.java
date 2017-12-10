package sharehome.com.androidsharehome2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddCustomTaskActivityNAV extends AppCompatActivity {
    private TextView goBack;
    String TimePeriod;
    EditText TaskNameInput;
    String TaskNameString;
    Button openRoommateList;
    Button submitTask;
    Spinner DailyBaseSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_custom_task_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        submitTask = (Button) findViewById(R.id.submitTask);
        openRoommateList = (Button)findViewById(R.id.openRoommateList);
        TaskNameInput = (EditText) findViewById(R.id.input_task_name);
        DailyBaseSpinner = (Spinner)findViewById(R.id.spinner_scheduling);
        goBack = (TextView) findViewById(R.id.goback);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TasksActivity.class);
                startActivity(intent);
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_LONG).show();
            }
        });

        submitTask.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });
    }

}
