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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.apigateway.*;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;

import sharehome.com.androidsharehome2.model.ListOfString;
import sharehome.com.androidsharehome2.model.PostList;
import sharehome.com.androidsharehome2.model.PostListItem;
import sharehome.com.androidsharehome2.model.ResultStringResponse;
import sharehome.com.androidsharehome2.model.Task;
import sharehome.com.androidsharehome2.model.TaskList;
import sharehome.com.androidsharehome2.model.TaskListItem;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;
import static sharehome.com.androidsharehome2.AppHelper.*;
import static sharehome.com.androidsharehome2.UserActivity.client;

public class TasksActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "TaskActivity";
    private CognitoUser user_aws;
    private String username;
    private ProgressDialog waitDialog;
    private AlertDialog userDialog;
    private ArrayList<String> tasks;
    taskHandler taskHandler;
    private ListView tasklistView;
    private ExpandableListView taskExpandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> title;
    Map<String, List<String>> content;
    public  LinearLayout layoutHeader;
    public  ImageView profileImage;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static int UPLOADIMAGE = 0;

    public static final ApiClientFactory factory = new ApiClientFactory();
    private SwipeRefreshLayout mySwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Exit if coming from different activity logging out
        if(getIntent().getBooleanExtra("EXIT", false)){
            finish();
            return;
        }
        init();
         /* get login info*/
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tasks = new ArrayList<String>();
        title = new ArrayList<String>();
        content = new HashMap<>();

        taskExpandableListView = (ExpandableListView) findViewById(R.id.post_list);
//        taskExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                Integer TaskID = Integer.parseInt(expandableListAdapter.getChild(groupPosition,0).toString());
//                final Task task = new Task();
//                task.setTaskID(TaskID);
//                task.setTaskSolved(true);
//                task.setGroupName(getCurrentGroupName());
//                Thread taskSolved = new Thread(new Runnable() {
//                    Handler handler = new Handler(getApplicationContext().getMainLooper());
//                    @Override
//                    public void run() {
//                        try{
//                            final ResultStringResponse response = client.taskPost(task, AppHelper.getCurrUser(),"add");
//                        }
//                        catch (Exception e){
//                            Toast.makeText(getApplicationContext(), "Failed to submit the task:",
//                                    Toast.LENGTH_LONG).show();
//                            showDialogMessage("Failed to submit the task:", "task: ", false);
//                        }
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(getApplicationContext(), "Successfully submitted the task" ,
//                                        Toast.LENGTH_LONG).show();
//                            }
//                        });
//                    }
//                });
//                taskSolved.start();
//                return true;
//            }
//        });
        expandableListAdapter = new TaskAdapter(this, title, content);


        getTaskResponseFromLambda();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddTaskActivityNAV.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        getTaskResponseFromLambda();
                    }

                }
        );

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        layoutHeader = (LinearLayout) navigationView.getHeaderView(0);
        TextView welcomeText = (TextView) layoutHeader.findViewById(R.id.WelcomeText);
        String text = welcomeText.getText().toString() + " " +
                AppHelper.getCurrUser();
        welcomeText.setText(text);
        profileImage = (ImageView) layoutHeader.findViewById(R.id.profileImage);
        setImageView();
        loadProfileImage();
    }

    private void setImageView() {
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asktoUploadImage();
            }
        });
    }

    public void fillData(TaskList taskList) {
//        split posts into two groups : urgent and normal
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-M-dd hh:mm:ss", Locale.US);
        String CurrentTimeFormatted = simpleDateFormat.format(Calendar.getInstance().getTime());
        try {
            Date CurrDate = simpleDateFormat.parse(CurrentTimeFormatted);
            for (TaskListItem task : taskList) {
                title.add(task.getTaskTitle());
                Date StartDate = simpleDateFormat.parse(task.getLastRotated());
                Log.d(TAG, StartDate.toString());
                int days = Integer.valueOf(DateTimeUtils.getFormattedDateRecurrence(task.getTaskDuration().longValue()));

                List<String> contents = Arrays.asList( "TaskID: "+ task.getTaskID().toString(),
                        "Executor: "+ task.getTaskUser(),
                        "Rotation period: "+ DateTimeUtils.getFormattedDateRecurrence(task.getTaskDuration().longValue()) + " days",
                        "From: " + task.getLastRotated(),
                        "Until deadline: " + DateTimeUtils.getRemainingTime(CurrDate, DateTimeUtils.addDays(StartDate, days)));
                content.put(task.getTaskTitle(), contents);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i(TAG, "convert format failed");
        }


    }

    private void asktoUploadImage() {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(TasksActivity.this).create();
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
        ActivityCompat.requestPermissions(TasksActivity.this,
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
                                    // also upload string to server
                                    ResultStringResponse profileImg = new ResultStringResponse();
                                    profileImg.setResult(encodedImage);
                                    client.profilePost(getCurrUser(),profileImg);
                                    Log.d(TAG, "Uploaded ProfileImage");
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

    private void getTaskResponseFromLambda() {
        if(getCurrentGroupName() ==null){
            return;
        }

        Thread taskThread = new Thread(new Runnable() {
            public void run() {
                final Handler handler = new Handler(getMainLooper());
                TaskList tasklist = client.taskGet(getCurrentGroupName());
                tasks.clear();
                title.clear();
                content.clear();
                fillData(tasklist);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mySwipeRefreshLayout.setRefreshing(false);
                        taskExpandableListView.setAdapter(expandableListAdapter);

                    }
                });
            }
        });

        taskThread.start();

    }

    /**
     * copy method from UserActivity
     * @return
     */
    private String getCurrentGroupName() {
            findCurrentGroupName();
        return AppHelper.getCurrgroupName();
    }

    /**
     * copy method from UserActivity
     * @return
     */
    private void findCurrentGroupName() {
        Thread taskThread = new Thread(new Runnable() {
            public void run() {
                Handler handler = new postSubmitHanlder(getMainLooper());

                final ListOfString response = client.groupGet(AppHelper.getCurrUser(), "getGroupName");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        AppHelper.groupName = (response.get(0));
                    }
                });
            }
        });
        taskThread.start();
//        try{
//            taskThread.join();
//        }
//        catch (Exception e){
//        }
    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult( requestCode, resultCode, data );
//    }

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
        if (id == R.id.action_refresh ) {// Create Display of everything!
            try {
                getTaskResponseFromLambda();
                Toast.makeText(this, "refresh successful", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                System.out.println("cannot found a group");
            }

            return true;
        } else if(id == R.id.action_logout){
            try {
                user_aws.signOut();
                exit();
            } catch (Exception e) {
                System.out.println("Hello" + e);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(user_aws == null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        if (id == R.id.nav_profile) {
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
            try {
                user_aws.signOut();
                exit();
            } catch (Exception e) {
                System.out.println("Hello" + e);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Starts the ProfileActivity
     *
     * @param view The view of the button which called this method
     */
    public void launchProfileActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
    }
    public void launchAddTaskActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), AddTaskActivityNAV.class);
        startActivity(intent);
    }
//    public void LogoutFromFacebook(View v){
//        Snackbar.make(v, "Replace with your own action ", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
//    }
    // Initialize this activity
    private void init() {
        // Get the user name
        Bundle extras = getIntent().getExtras();
        username = AppHelper.getCurrUser();
        user_aws = AppHelper.getPool().getUser(username);

    }

    private void showWaitDialog(String message) {
        closeWaitDialog();
        waitDialog = new ProgressDialog(this);
        waitDialog.setTitle(message);
        waitDialog.show();
    }

    private void showDialogMessage(String title, String body, final boolean exit) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();
                    if(exit) {
                        exit();
                    }
                } catch (Exception e) {
                    // Log failure
                    Log.e(TAG,"Dialog dismiss failed");
                    if(exit) {
                        exit();
                    }
                }
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }

    private void closeWaitDialog() {
        try {
            waitDialog.dismiss();
        }
        catch (Exception e) {
            //
        }
    }
    private void exit() {
        Intent intent = new Intent(this, LoginActivity.class);
        /*pass in username and password info to MainActivity*/
        if (username == null) {
            username = "";
        }
        intent.putExtra("username", username);
        setResult(RESULT_OK, intent);
        startActivity(intent);
        finish();
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
