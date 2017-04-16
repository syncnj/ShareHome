package sharehome.com.androidsharehome2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddTaskActivityNAV extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static String[] Roommates = {"Tom", "Richard", "Cassie", "Leo"};
    boolean[] checkedItems = new boolean[Roommates.length];
    AlertDialog ad;
    Button openRoommateList;
    Button submitTask;
    Spinner DailyBaseSpinner;
    String TimePeriod;
    EditText TaskNameInput;
    String TaskNameString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
                Toast.makeText(getApplicationContext(),findCheckedRoommates(Roommates,checkedItems)+ " will  work base on " + TimePeriod + " doing "+TaskNameString,Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_task_activity_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

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

}