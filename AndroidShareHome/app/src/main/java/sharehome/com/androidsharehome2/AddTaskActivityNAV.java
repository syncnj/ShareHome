package sharehome.com.androidsharehome2;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
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
import android.widget.TextView;
import android.widget.Toast;


import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import sharehome.com.androidsharehome2.model.ListOfString;
import sharehome.com.androidsharehome2.model.Post;
import sharehome.com.androidsharehome2.model.ResultStringResponse;

public class AddTaskActivityNAV extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "AddTaskActivityNAV";
    String[] Roommates = {};
    String[] RoommatesName;

    AlertDialog ad;
    Button openRoommateList;
    Button submitTask;
    Spinner DailyBaseSpinner;
    String TimePeriod;
    EditText TaskNameInput;
    String TaskNameString;
    CardView card1;
    CardView card2;
    CardView card3;
    CardView card4;
    CardView card5;
    CardView card6;
    private TextView custom;
    boolean[] checkedItems;
    boolean clicked1 = false;
    boolean clicked2 = false;
    boolean clicked3 = false;
    boolean clicked4 = false;
    boolean clicked5 = false;
    boolean clicked6 = false;
    int TimetoHour;
    Dialog myDialog;
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
        /*for(int j =0; j<Roommates.length;j++) {
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

        }*/

        Log.i("roommateId List", Roommates.toString());
        System.out.println(Roommates.length);
        checkedItems = new boolean[Roommates.length];
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        card1 = (CardView) findViewById(R.id.card1);
        card2 = (CardView) findViewById(R.id.card2);
        card3 = (CardView) findViewById(R.id.card3);
        card4 = (CardView) findViewById(R.id.card4);
        card5 = (CardView) findViewById(R.id.card5);
        card6 = (CardView) findViewById(R.id.card6);
        submitTask = (Button) findViewById(R.id.submitTask);
        openRoommateList = (Button)findViewById(R.id.openRoommateList);
        DailyBaseSpinner = (Spinner)findViewById(R.id.spinner_scheduling);
        custom = (TextView)findViewById(R.id.custom);
        myDialog = new Dialog(this);
        openRoommateList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(AddTaskActivityNAV.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Getting your Roommates...");
                progressDialog.show();
                new Thread(new Runnable() {
                    Handler handler = new Handler(getMainLooper());
                    public void run() {
                        if (AppHelper.getCurrgroupName() == null){
                            return;
                        }
                        ApiClientFactory factory = new ApiClientFactory();
                        final AwscodestarsharehomelambdaClient client =
                                factory.build(AwscodestarsharehomelambdaClient.class);

                        final ListOfString response = client.groupGet(AppHelper.getCurrUser(),
                                "listMembers");


                        final List<String> roommates = new ArrayList<>();
                        for (String roommate : response){
                            roommates.add(roommate);
                        }

                        // TODO: put results to UI here ( where roommates is a list of roommate names)
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                  progressDialog.dismiss();
                                  showPopup(null);

                                  Toast.makeText(getApplicationContext(), response.get(0),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).start();
            }
        });

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!clicked1 && !(clicked2||clicked3||clicked4||clicked5||clicked6))
                {
                    card1.setCardBackgroundColor(Color.parseColor("#009688"));
                    clicked1 = true;
                }
                else if(!clicked1 && (clicked2||clicked3||clicked4||clicked5||clicked6))
                {
                    card2.setCardBackgroundColor(Color.WHITE);
                    card3.setCardBackgroundColor(Color.WHITE);
                    card4.setCardBackgroundColor(Color.WHITE);
                    card5.setCardBackgroundColor(Color.WHITE);
                    card6.setCardBackgroundColor(Color.WHITE);
                    clicked2 = false;
                    clicked3 = false;
                    clicked4 = false;
                    clicked5 = false;
                    clicked6 = false;
                }
                else if(clicked1)
                {
                    card1.setCardBackgroundColor(Color.WHITE);
                    clicked1 = false;
                }

            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!clicked2 && !(clicked1||clicked3||clicked4||clicked5||clicked6))
                {
                    card2.setCardBackgroundColor(Color.parseColor("#009688"));
                    clicked2 = true;
                }
                else if(!clicked2 && (clicked1||clicked3||clicked4||clicked5||clicked6))
                {
                    card1.setCardBackgroundColor(Color.WHITE);
                    card3.setCardBackgroundColor(Color.WHITE);
                    card4.setCardBackgroundColor(Color.WHITE);
                    card5.setCardBackgroundColor(Color.WHITE);
                    card6.setCardBackgroundColor(Color.WHITE);
                    clicked1 = false;
                    clicked3 = false;
                    clicked4 = false;
                    clicked5 = false;
                    clicked6 = false;
                }
                else if(clicked2)
                {
                    card2.setCardBackgroundColor(Color.WHITE);
                    clicked2 = false;
                }
            }
        });
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!clicked3 && !(clicked1||clicked2||clicked4||clicked5||clicked6))
                {
                    card3.setCardBackgroundColor(Color.parseColor("#009688"));
                    clicked3 = true;
                }
                else if(!clicked3 && (clicked1||clicked2||clicked4||clicked5||clicked6))
                {
                    card1.setCardBackgroundColor(Color.WHITE);
                    card2.setCardBackgroundColor(Color.WHITE);
                    card4.setCardBackgroundColor(Color.WHITE);
                    card5.setCardBackgroundColor(Color.WHITE);
                    card6.setCardBackgroundColor(Color.WHITE);
                    clicked1 = false;
                    clicked2 = false;
                    clicked4 = false;
                    clicked5 = false;
                    clicked6 = false;
                }
                else if(clicked3)
                {
                    card3.setCardBackgroundColor(Color.WHITE);
                    clicked3 = false;
                }
            }
        });
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!clicked4 && !(clicked1||clicked3||clicked2||clicked5||clicked6))
                {
                    card4.setCardBackgroundColor(Color.parseColor("#009688"));
                    clicked4 = true;
                }
                else if(!clicked4 && (clicked1||clicked3||clicked2||clicked5||clicked6))
                {
                    card1.setCardBackgroundColor(Color.WHITE);
                    card3.setCardBackgroundColor(Color.WHITE);
                    card2.setCardBackgroundColor(Color.WHITE);
                    card5.setCardBackgroundColor(Color.WHITE);
                    card6.setCardBackgroundColor(Color.WHITE);
                    clicked1 = false;
                    clicked3 = false;
                    clicked2 = false;
                    clicked5 = false;
                    clicked6 = false;
                }
                else if(clicked4)
                {
                    card4.setCardBackgroundColor(Color.WHITE);
                    clicked4 = false;
                }
            }
        });
        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!clicked5 && !(clicked1||clicked3||clicked4||clicked2||clicked6))
                {
                    card5.setCardBackgroundColor(Color.parseColor("#009688"));
                    clicked5 = true;
                }
                else if(!clicked5 && (clicked1||clicked3||clicked4||clicked2||clicked6))
                {
                    card1.setCardBackgroundColor(Color.WHITE);
                    card3.setCardBackgroundColor(Color.WHITE);
                    card4.setCardBackgroundColor(Color.WHITE);
                    card2.setCardBackgroundColor(Color.WHITE);
                    card6.setCardBackgroundColor(Color.WHITE);
                    clicked1 = false;
                    clicked3 = false;
                    clicked4 = false;
                    clicked2 = false;
                    clicked6 = false;
                }
                else if(clicked5)
                {
                    card5.setCardBackgroundColor(Color.WHITE);
                    clicked5 = false;
                }
            }
        });
        card6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!clicked6 && !(clicked1||clicked3||clicked4||clicked5||clicked2))
                {
                    card6.setCardBackgroundColor(Color.parseColor("#009688"));
                    clicked6 = true;
                }
                else if(!clicked6 && (clicked1||clicked3||clicked4||clicked5||clicked2))
                {
                    card1.setCardBackgroundColor(Color.WHITE);
                    card3.setCardBackgroundColor(Color.WHITE);
                    card4.setCardBackgroundColor(Color.WHITE);
                    card5.setCardBackgroundColor(Color.WHITE);
                    card2.setCardBackgroundColor(Color.WHITE);
                    clicked1 = false;
                    clicked3 = false;
                    clicked4 = false;
                    clicked5 = false;
                    clicked2 = false;
                }
                else if(clicked6)
                {
                    card6.setCardBackgroundColor(Color.WHITE);
                    clicked6 = false;
                }
            }
        });
        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddCustomTaskActivityNAV.class);
                startActivity(intent);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        ad = builder.create();

    }
    public void showPopup(View v) {
        TextView txtclose;
        final Button roommate1;
        final Button roommate2;
        final Button roommate3;
        final Button roommate4;
        final Button roommate5;
        myDialog.setContentView(R.layout.popup);
        txtclose = (TextView) findViewById(R.id.txtclose);
        roommate1 = (Button) findViewById(R.id.roommate1);
        roommate2 = (Button) findViewById(R.id.roommate2);
        roommate3 = (Button) findViewById(R.id.roommate3);
        roommate4 = (Button) findViewById(R.id.roommate4);
        roommate5 = (Button) findViewById(R.id.roommate5);
        /*txtclose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        roommate1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                roommate1.setBackgroundColor(Color.WHITE);
            }
        });*/
        myDialog.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            Intent intent = new Intent(this, TasksActivity.class);
//            startActivity(intent);
//            finish();
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
//            loginManager.logOut();
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
