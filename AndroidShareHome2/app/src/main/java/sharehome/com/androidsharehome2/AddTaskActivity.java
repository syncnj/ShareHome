package sharehome.com.androidsharehome2;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


/**
 * Created by zhigong on 2017/4/5.
 */

public class AddTaskActivity extends Activity{

    static String[] Roommates = {"Tom", "Richard", "Cassie", "Leo"};
    boolean[] checkedItems = new boolean[Roommates.length];
    AlertDialog ad;
    Button openRoommateList;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        openRoommateList = (Button)findViewById(R.id.openRoommateList);
        openRoommateList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.show();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("select your Room mate");


        builder.setNegativeButton("Cancel", null);
        builder.setMultiChoiceItems(Roommates, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {

                Toast.makeText(getApplicationContext(), Roommates[which], Toast.LENGTH_SHORT).show();
            }
        });
        ad = builder.create();

    }
}
