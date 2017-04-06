package sharehome.com.androidsharehome2;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;


/**
 * Created by zhigong on 2017/4/5.
 */

public class AddTaskActivity extends Activity{

    static String[] Roommates = {"Tom", "Richard", "Cassie", "Leo"};
    boolean[] checkedItems = new boolean[Roommates.length];
    AlertDialog ad;
    Button openRoommateList;
    Button submitTask;
    Spinner DailyBaseSpinner;
    String TimePeriod;
    EditText TaskNameInput;
    String TaskNameString;

public String findCheckedRoommates(String[] allRoommates, boolean[] checkedRoommates){
    String returnNames = "";
        for(int j =0; j<allRoommates.length;j++){
        if(checkedRoommates[j]==true){
             returnNames = returnNames + " " +allRoommates[j];
        }
    }
return returnNames;
    }
    public void submitMessageHandler(View view){

        Toast.makeText(getApplicationContext(),"need to replace to actual submittion process \nSuccessful",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);


        submitTask = (Button) findViewById(R.id.submitTask);
        openRoommateList = (Button)findViewById(R.id.openRoommateList);


        DailyBaseSpinner = (Spinner)findViewById(R.id.spinner_scheduling);
        TaskNameInput = (EditText)findViewById(R.id.input_task_name) ;




        openRoommateList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.show();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("select your Room mate");


       builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
               Toast.makeText(getApplicationContext(),"Canceled roommate selection",Toast.LENGTH_SHORT).show();
           }
       });
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //get user input on task name and time period
                TimePeriod= DailyBaseSpinner.getSelectedItem().toString();
                TaskNameString = TaskNameInput.getText().toString();
                Toast.makeText(getApplicationContext(),findCheckedRoommates(Roommates,checkedItems)+ " should work base on " + TimePeriod + "on doing "+TaskNameString,Toast.LENGTH_SHORT).show();
            }
        });


        builder.setMultiChoiceItems(Roommates, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {

                Toast.makeText(getApplicationContext(), Roommates[which], Toast.LENGTH_SHORT).show();
            }
        });
        ad = builder.create();



    }

}
