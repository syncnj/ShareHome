package sharehome.com.androidsharehome2;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.UserService;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.facebook.login.LoginManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.sql.Time;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import sharehome.com.androidsharehome2.backend.GroupService;
import sharehome.com.androidsharehome2.backend.TaskService;

public class AddTaskActivityNAV extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LoginManager loginManager;
    String[] Roommates = {};
    String[] RoommatesName;

    AlertDialog ad;
    Button openRoommateList;
    Button submitTask;
    Spinner DailyBaseSpinner;
    String TimePeriod;
    EditText TaskNameInput;
    String TaskNameString;
    GroupService groupService;
    boolean[] checkedItems;
    int TimetoHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        byte[] b = {};
        String members = "";
        try {
            // This code gets the user objectIds of the members of the CurrentUser's group, this is written to the file in TasksActivity
            BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput("members")));
            members = reader.readLine();
        }catch(Exception e){
            System.out.println("AddTaskActivityNAV EXCEPTION when reading members");
        }
        System.out.println("\n Members: " + members.toString());
        Roommates = members.split(",");
        //Todo: create a list with all groupmembers name from its groupId.
        RoommatesName = new String[Roommates.length];
        for(int j =0; j<Roommates.length;j++) {
            final int a = j;
            Backendless.UserService.findById(Roommates[j], new AsyncCallback<BackendlessUser>() {
                @Override
                public void handleResponse(BackendlessUser response) {

                    RoommatesName[a] = response.getProperty("name").toString();
                }

                @Override
                public void handleFault(BackendlessFault fault) {

                }
            });

        }

        Log.i("roommateId List", Roommates.toString());
        System.out.println(Roommates.length);
        checkedItems = new boolean[Roommates.length];

        loginManager = LoginManager.getInstance();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

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
                if(ad != null) {
                    ad.show();
                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("select your Room mate");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Toast.makeText(getApplicationContext(),"Canceled roommate selection",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //get user input on task name and time period
                TimePeriod= DailyBaseSpinner.getSelectedItem().toString();
                if (TimePeriod.equals("Day")){
                    TimetoHour = 24;
                }
                else if(TimePeriod.equals("Week")){
                    TimetoHour = 7*24;
                }
                else if(TimePeriod.equals("Month")){
                    TimetoHour = 30*24;
                }
                else if(TimePeriod.equals("Year")){
                    TimetoHour = 365*24;
                }
                else {
                    System.out.println("The spinner has broken");
                }
                TaskNameString = TaskNameInput.getText().toString();
                TaskService.getInstance().createNewTaskAsync(TaskNameInput.getText().toString(),
                        findCheckedRoommates(Roommates, checkedItems),
                        Backendless.UserService.CurrentUser().getProperty("groupId").toString(), TimetoHour, new Date(System.currentTimeMillis()), new AsyncCallback<String>() {
                            @Override
                            public void handleResponse(String response) {
                                Toast.makeText(getApplicationContext(), "Successfully created "+ TaskNameString, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(getApplicationContext(), "Please select roommates", Toast.LENGTH_LONG).show();
                            }
                        })

            ;
//                Toast.makeText(getApplicationContext(),findCheckedRoommates(Roommates,checkedItems)+ " will  work base on " + TimePeriod + " doing "+TaskNameString,Toast.LENGTH_SHORT).show();
            }
        });

        builder.setMultiChoiceItems(RoommatesName, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {

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
        getMenuInflater().inflate(R.menu.shared, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            return true;
        } else if(id == R.id.action_logout){
            loginManager.logOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_transactions) {
            Intent intent = new Intent(getApplicationContext(), TransactionActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_groceries) {
            Intent intent = new Intent(getApplicationContext(), GroceryListActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_tasks) {
            Intent intent = new Intent(getApplicationContext(), TasksActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_main) {
            Intent intent = new Intent(getApplicationContext(), UserActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            // TODO: This doesn't exit the app
            loginManager.logOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public String findCheckedRoommates(String[] allRoommates, boolean[] checkedRoommates){
        String returnNames = "";
        if(checkedRoommates[0]){
            returnNames += allRoommates[0];
        }
        for(int j =1; j<allRoommates.length;j++){
            if(checkedRoommates[j]){
                returnNames += "," + allRoommates[j];
            }
        }
        return returnNames;
    }

    public void submitMessageHandler(View view){

        Toast.makeText(getApplicationContext(),"need to replace to actual submittion process \nSuccessful",Toast.LENGTH_SHORT).show();
    }

}
