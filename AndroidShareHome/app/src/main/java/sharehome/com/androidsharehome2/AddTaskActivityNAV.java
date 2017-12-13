package sharehome.com.androidsharehome2;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Base64;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import sharehome.com.androidsharehome2.model.ListOfString;
import sharehome.com.androidsharehome2.model.Post;

import sharehome.com.androidsharehome2.model.ResultStringResponse;
import sharehome.com.androidsharehome2.model.Task;

import static sharehome.com.androidsharehome2.AppHelper.PROFILE_IMAGE_HEIGHT;
import static sharehome.com.androidsharehome2.AppHelper.PROFILE_IMAGE_WIDTH;
import static sharehome.com.androidsharehome2.AppHelper.ProfileImgFN;
import static sharehome.com.androidsharehome2.AppHelper.getCurrUser;
import static sharehome.com.androidsharehome2.AppHelper.profile_img_bitmap;
import static sharehome.com.androidsharehome2.UserActivity.client;
import static sharehome.com.androidsharehome2.AppHelper.*;
import static sharehome.com.androidsharehome2.UserActivity.client;
public class AddTaskActivityNAV extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "AddTaskActivityNAV";
    String[] Roommates = {};
    String[] RoommatesName;

    AlertDialog ad;
    Button openRoommateList;
    Button submitTask;
    Spinner DailyBaseSpinner;
    Spinner RoommateSpinner;
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
    private android.app.AlertDialog userDialog;

    public LinearLayout layoutHeader;
    public ImageView profileImage;
    ArrayAdapter<String> dataAdapter;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static int UPLOADIMAGE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        byte[] b = {};
        String members = "";
//        try {
//            // This code gets the user objectIds of the members of the CurrentUser's group, this is written to the file in TasksActivity
//            BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput("members")));
//            members = reader.readLine();
//        }catch(Exception e){
//            System.out.println("AddTaskActivityNAV EXCEPTION when reading members");
//        }
//        System.out.println("\n Members: " + members.toString());
//        Roommates = members.split(",");
//        //Todo: create a list with all groupmembers name from its groupId.
//        RoommatesName = new String[Roommates.length];
        if (AppHelper.Roommates == null){

        fetchRoomatesFromServer();
        }
        else {
            RoommateSpinner = (Spinner)findViewById(R.id.openRoommateList);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_spinner_item, AppHelper.Roommates);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            RoommateSpinner.setAdapter(dataAdapter);
        }
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

//        TaskNameInput = (EditText) findViewById(R.id.input_task_name);
        submitTask = (Button) findViewById(R.id.submitTask);
        DailyBaseSpinner = (Spinner)findViewById(R.id.spinner_scheduling);
        RoommateSpinner = (Spinner)findViewById(R.id.openRoommateList);
        custom = (TextView)findViewById(R.id.custom);

        //myDialog = new Dialog(this);
//        getRoomates();
        /*
        openRoommateList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ad != null) {
                    ad.show();
                }
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
//                        ApiClientFactory factory = new ApiClientFactory();
//                        final AwscodestarsharehomelambdaClient client =
//                                factory.build(AwscodestarsharehomelambdaClient.class);

                        final ListOfString response = UserActivity.client.groupGet(AppHelper.getCurrUser(),
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
        });*/
        submitTask.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(AddTaskActivityNAV.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Submitting tasks...");
                progressDialog.show();
                final String taskTitle = gettaskTitlefromCards();
                if (taskTitle == "-1"){
                    progressDialog.dismiss();
                    showDialogMessage("Failed to submit the task:".toUpperCase(),
                            "Please select a task"
                            );
                    return;
                }
                new Thread(new Runnable() {
                    Handler handler = new Handler(getMainLooper());
                    public void run() {
                        if (AppHelper.getCurrgroupName() == null){
                            return;
                        }
                        ApiClientFactory factory = new ApiClientFactory();
                        final AwscodestarsharehomelambdaClient client =
                                factory.build(AwscodestarsharehomelambdaClient.class);
                        Task task = new Task();
                        task.setGroupName(AppHelper.groupName);
                        task.setTaskTitle(taskTitle);
                        String formattedDate =
                                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Calendar.getInstance().getTime());
                        Log.d(TAG, "generated formattedDate:\n" + formattedDate);
                        task.setLastRotated(formattedDate);
                        String duratingStr = DailyBaseSpinner.getSelectedItem().toString();
                        Integer duration = 100;
                        switch(duratingStr){
                            case "Day": duration=  24*60;
                                break;
                            case "Week": duration= 7*24*60;
                                break;
                            default: {
                                Calendar c = Calendar.getInstance();
                                int monthMaxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                                duration = monthMaxDays * 24 * 60; //"Month"
                            }
                                break;
                        }

                        task.setTaskDuration(duration);
//                        TODO
//                        String taskUser = "ttzztt";
                        String taskUser = RoommateSpinner.getSelectedItem().toString();
                        task.setTaskUser(taskUser);
                        SimpleDateFormat simpleDateFormat =
                                new SimpleDateFormat("yyyy-M-dd hh:mm:ss", Locale.US);
                        String CurrentTimeFormatted = simpleDateFormat.format(Calendar.getInstance().getTime());
                        task.setLastRotated(CurrentTimeFormatted);
                        task.setTaskSolved(false);
                        try {
                            final ResultStringResponse response = client.taskPost(task, AppHelper.getCurrUser(),"add"
                                    );
                            // TODO: put results to UI here ( where roommates is a list of roommate names)
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "successfully submitted the task: " + taskTitle,
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to submit the task:" + taskTitle,
                                    Toast.LENGTH_LONG).show();
                            showDialogMessage("Failed to submit the task:", "task: " + taskTitle
                            + " was not successfully added due to the server error");
                        }
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
        layoutHeader = (LinearLayout) navigationView.getHeaderView(0);
        TextView welcomeText = (TextView) layoutHeader.findViewById(R.id.WelcomeText);
        String text = welcomeText.getText().toString() + " " +
                AppHelper.getCurrUser();
        welcomeText.setText(text);
        profileImage = (ImageView) layoutHeader.findViewById(R.id.profileImage);
        RoommateSpinner = (Spinner)findViewById(R.id.openRoommateList);
        setImageView();
        loadProfileImage();
    }

    private void getRoomates() {
        new Thread(new Runnable() {
            Handler handler = new Handler(getMainLooper());
            public void run() {
                if (AppHelper.getCurrgroupName() == null){
                    return;
                }
                final ListOfString response = UserActivity.client.groupGet(AppHelper.getCurrUser(),
                        "listMembers");

                // TODO: put results to UI here ( where roommates is a list of roommate names)
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        addItemsOnRoommateSpinner(response);
                    }
                });
            }
        }).start();
    }

    /*
    public void showPopup(View v) {
        TextView txtclose;
        final Button roommate1;
        final Button roommate2;
        final Button roommate3;
        final Button roommate4;
        final Button roommate5;
        myDialog.setContentView(R.layout.popup);
        LinearLayout l = (LinearLayout) this.findViewById(android.R.id.content);
        txtclose = (TextView) l.findViewById(R.id.txtclose);
        roommate1 = (Button) findViewById(R.id.roommate1);
        roommate2 = (Button) findViewById(R.id.roommate2);
        roommate3 = (Button) findViewById(R.id.roommate3);
        roommate4 = (Button) findViewById(R.id.roommate4);
        roommate5 = (Button) findViewById(R.id.roommate5);
        txtclose.setOnClickListener(new View.OnClickListener(){
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
        });
        myDialog.show();
    }*/
    public void addItemsOnRoommateSpinner(ListOfString Roommates)
    {

        List<String> RoommateNames = new ArrayList<>();
//        RoommateNames.clear();
        //here add members to the spinner
        for (String Roommate: Roommates){
            RoommateNames.add(Roommate);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, RoommateNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        RoommateSpinner.setAdapter(dataAdapter);
    }
    public void addItemsOnRoommateSpinner()
    {
        RoommateSpinner = (Spinner)findViewById(R.id.openRoommateList);
        List<String> RoommateNames = new ArrayList<>();
//        RoommateNames.clear();
        //here add members to the spinner
        for (String Roommate: AppHelper.Roommates){
            RoommateNames.add(Roommate);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, RoommateNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        RoommateSpinner.setAdapter(dataAdapter);
    }
    public void fetchRoomatesFromServer(){
        new Thread(new Runnable() {
            Handler handler = new Handler(getMainLooper());
            @Override
            public void run() {
                final ListOfString Roommates = UserActivity.client.groupGet(AppHelper.getCurrUser(),
                        "listMembers");
                RoommateSpinner = (Spinner)findViewById(R.id.openRoommateList);
                AppHelper.Roommates = new ArrayList<>();
//        RoommateNames.clear();
                //here add members to the spinner
                for (String Roommate: Roommates){
                    AppHelper.Roommates.add(Roommate);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
                                android.R.layout.simple_spinner_item, AppHelper.Roommates);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        RoommateSpinner.setAdapter(dataAdapter);
                    }
                });
            }
        }).start();

        // TODO save UserInfo into SharedPreference:

    }


    private void showDialogMessage(String title, String body){
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddTaskActivityNAV.this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();
                } catch (Exception e) {
                    // Log failure
                    Log.e(TAG,"Dialog dismiss failed");
                }
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }
    private String gettaskTitlefromCards() {
        if(clicked1){
            return  ((TextView) findViewById(R.id.sweep_floor_title)).getText().toString();
        }
        else if(clicked2){
            return ((TextView) findViewById(R.id.wash_dishes_title)).getText().toString();
        }
        else if(clicked3){
            return  ((TextView) findViewById(R.id.throw_trash_title)).getText().toString();
        }
        else if(clicked4){
            return ((TextView) findViewById(R.id.clean_table_title)).getText().toString();
        }
        else if(clicked5){
            return ((TextView) findViewById(R.id.wipe_window_title)).getText().toString();
        }
        else if(clicked6){
            return ((TextView) findViewById(R.id.do_laundry_title)).getText().toString();
        }
        // return "Do laundry";
        return "-1";
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
            signOut();
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        AppHelper.getPool().getUser(AppHelper.getCurrUser()).signOut();
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
    private void setImageView() {
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asktoUploadImage();
            }
        });
    }
    private void asktoUploadImage() {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(AddTaskActivityNAV.this).create();
        alertDialog.setTitle("Change your profile image");
        alertDialog.setMessage("sure to change your profile image?");
        alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Please select your favorite profile" +
                                        " image",
                                Toast.LENGTH_LONG).show();
                        uploadProfileImage();
                    }
                });
        alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, "cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
        alertDialog.getButton(Dialog.BUTTON_NEGATIVE).
                setTextColor(Color.parseColor("#3399ff"));
        alertDialog.getButton(Dialog.BUTTON_POSITIVE).
                setTextColor(Color.parseColor("#3399ff"));
    }
    private void uploadProfileImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, UPLOADIMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UPLOADIMAGE) {
            profileImage.setImageDrawable(getPicture(data.getData()));
            saveProfileImgs();
        }
    }
    private void saveProfileImgs() {
        ActivityCompat.requestPermissions(AddTaskActivityNAV.this,
                new String[]{"android.permission.WRITE_EXTERNAL_STORAGE",
                        "android.permission.READ_EXTERNAL_STORAGE"
                },
                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
    }
    public Drawable getPicture(Uri selectedImage) {
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(selectedImage);
        } catch (FileNotFoundException e) {
            return getResources().getDrawable(R.drawable.logo);
        }
        Drawable source = Drawable.createFromStream(inputStream, selectedImage.toString());
        source.setBounds(0,72,0,72);
        profile_img_bitmap = ((BitmapDrawable) source).getBitmap();
        Drawable scaled = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(profile_img_bitmap,
                PROFILE_IMAGE_WIDTH, PROFILE_IMAGE_HEIGHT, true));
        return scaled;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
//                    #####################################################
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    profile_img_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] b = baos.toByteArray();
//
                    final String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                    // Open the file.
                    try {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory()+ "//" + ProfileImgFN);
                                    fos.write(encodedImage.getBytes());
                                    fos.close();
                                    AppHelper.setUploadedProfileImgs(true);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //                    #####################################################
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    private void loadProfileImage() {
        final Handler handler = new Handler(getMainLooper());
        Thread loadImgs = new Thread(new Runnable() {
            @Override
            public void run() {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(Environment.getExternalStorageDirectory()+ "//" +
                            ProfileImgFN);
                    byte[] reader = new byte[fis.available ()];
                    while(fis.read(reader)!=-1){}
                    String previouslyEncodedImage = (new String(reader));
                    Log.i("Data", ProfileImgFN);
                    fis.close();
                    if( !previouslyEncodedImage.equalsIgnoreCase("") ){
                        if (!AppHelper.getUploadedProfileImgs()){
                            byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
                            profile_img_bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                            AppHelper.setUploadedProfileImgs(true);
                        }
//            scale it to proper dimension
                        final Drawable scaled = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(profile_img_bitmap,
                                PROFILE_IMAGE_WIDTH, PROFILE_IMAGE_HEIGHT, true));
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                profileImage.setImageDrawable(scaled);
                            }
                        });
                    }
                    else{
                        // look for server information
                        try {
                            String imgData = client.profileGet(getCurrUser()).getResult();
                            if (!AppHelper.getUploadedProfileImgs()){
                                byte[] b = Base64.decode(imgData, Base64.DEFAULT);
                                profile_img_bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                                AppHelper.setUploadedProfileImgs(true);
                            }
                            final Drawable scaled = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(profile_img_bitmap,
                                    PROFILE_IMAGE_WIDTH, PROFILE_IMAGE_HEIGHT, true));
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    profileImage.setImageDrawable(scaled);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }catch (FileNotFoundException fileNotFound){
                    // look for server information
                    try {
                        String imgData = client.profileGet(getCurrUser()).getResult();
                        if (!AppHelper.getUploadedProfileImgs()){
                            byte[] b = Base64.decode(imgData, Base64.DEFAULT);
                            profile_img_bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                            AppHelper.setUploadedProfileImgs(true);
                        }
                        final Drawable scaled = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(profile_img_bitmap,
                                PROFILE_IMAGE_WIDTH, PROFILE_IMAGE_HEIGHT, true));
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                profileImage.setImageDrawable(scaled);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        loadImgs.start();
    }
}
