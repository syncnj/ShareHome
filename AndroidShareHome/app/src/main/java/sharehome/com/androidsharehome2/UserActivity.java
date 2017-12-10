package sharehome.com.androidsharehome2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Layout;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.apigateway.*;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;

import sharehome.com.androidsharehome2.model.ListOfString;
import sharehome.com.androidsharehome2.model.PostList;
import sharehome.com.androidsharehome2.model.PostListItem;

import com.amazonaws.mobileconnectors.pinpoint.*;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSMobileClient.*;
import com.amazonaws.regions.Regions;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;



public class UserActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "UserActivity";
    private CognitoUser user_aws;
    private String username;
    private ProgressDialog waitDialog;
    private AlertDialog userDialog;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> posts;
    private ListView postListView;
    private ImageView profileImage;
    public static PinpointManager pinpointManager;

    @Override
    protected void onPause() {
        super.onPause();

        // unregister notification receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(notificationReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register notification receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(notificationReceiver,
                new IntentFilter(ShareHomePushListenerService.ACTION_PUSH_NOTIFICATION));
    }

    private final BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Received notification from local broadcast. Display it in a dialog.");

            Bundle data = intent.getBundleExtra(ShareHomePushListenerService.INTENT_SNS_NOTIFICATION_DATA);

            String title = ShareHomePushListenerService.getTitle(data);
            String message = ShareHomePushListenerService.getMessage(data);

            new android.app.AlertDialog.Builder(UserActivity.this)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Exit if coming from different activity logging out
        if(getIntent().getBooleanExtra("EXIT", false)){
            finish();
            return;
        }
        init();
        AWSMobileClient.getInstance().initialize(this).execute();
        initializeAWSPinpoint();

        //for pinpoint endpoint

         /* get login info*/
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        NavigationView f0 = (NavigationView)findViewById(R.id.nav_view);
//
//        LinearLayout f1  = (LinearLayout)findViewById(R.id.nav_header_main_layout);
//        profileImage = (ImageView)f1.findViewById(R.id.profileImage);
//        setImageView();
//
        getCurrentGroupName();
        posts = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,
                R.layout.task_item, posts);
        postListView = (ListView) findViewById(R.id.post_list);
        getPostResponseFromLambda();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddDifferentPostActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void setImageView() {
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserActivity.this,
                        "The favorite list would appear on clicking this icon",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getCurrentGroupName() {
//        if (AppHelper.getCurrgroupName() == null){
            findCurrentGroupName();
            // blocking here to enforce we get the groupName before doing something else
//            while(AppHelper.groupName == null){
////                hard coded here ...
////                return "HelloKitty";
//            }
//            return AppHelper.getCurrgroupName();
//        }
//        Log.d(TAG,AppHelper.groupName);
        return AppHelper.getCurrgroupName();
    }

    private void findCurrentGroupName() {
        Thread taskThread = new Thread(new Runnable() {
            public void run() {
                Handler handler = new postSubmitHanlder(getMainLooper());
                ApiClientFactory factory = new ApiClientFactory();
                final AwscodestarsharehomelambdaClient client =
                        factory.build(AwscodestarsharehomelambdaClient.class);
                final ListOfString response = client.groupGet(AppHelper.getCurrUser(), "getGroupName");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        AppHelper.groupName = (response.get(0));
                        registerEndpoint();
                    }
                });
            }
        });
        taskThread.start();
    }
    private void registerEndpoint(){

//        CognitoCachingCredentialsProvider cognitoCachingCredentialsProvider = new CognitoCachingCredentialsProvider(this,"IDENTITY_POOL_ID", Regions.US_EAST_1);
//
//        PinpointConfiguration config = new PinpointConfiguration(this, "APP_ID", Regions.US_EAST_1, cognitoCachingCredentialsProvider);
//
//        this.pinpointManager = new PinpointManager(config);
        if(AppHelper.getCurrgroupName() == null){
            return;
        }
       pinpointManager.getTargetingClient().addAttribute("GroupName",Arrays.asList(AppHelper.getCurrgroupName()));
        pinpointManager.getTargetingClient().addAttribute("UserName",Arrays.asList(AppHelper.getCurrUser()));
       pinpointManager.getTargetingClient().updateEndpointProfile();
    }
    private void initializeAWSPinpoint() {

        if (pinpointManager == null) {
            PinpointConfiguration pinpointConfig = new PinpointConfiguration(
                    getApplicationContext(),
                    AWSMobileClient.getInstance().getCredentialsProvider(),
                    AWSMobileClient.getInstance().getConfiguration());

            pinpointManager = new PinpointManager(pinpointConfig);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String deviceToken =
                                InstanceID.getInstance(UserActivity.this).getToken(
                                        "824984416620",
                                        GoogleCloudMessaging.INSTANCE_ID_SCOPE);
                        Log.e("NotError", deviceToken);
                        pinpointManager.getNotificationClient()
                                .registerGCMDeviceToken(deviceToken);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }

    private void getPostResponseFromLambda() {
        if(getCurrentGroupName() ==null){
            return;
        }
        postListView = (ListView) findViewById(R.id.post_list);
        Thread taskThread = new Thread(new Runnable() {
            public void run() {
                if(AppHelper.getCurrgroupName() == null){
                    return;
                }
                ApiClientFactory factory = new ApiClientFactory();
                final AwscodestarsharehomelambdaClient client =
                        factory.build(AwscodestarsharehomelambdaClient.class);
                PostList postList = client.postGet(getCurrentGroupName());
                posts.clear();
                for (PostListItem post : postList){
                    posts.add(post.getPostTitle());
                }
                 UserActivity.this.runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         postListView.setAdapter(adapter);
                     }
                 });
            }
        });
        taskThread.start();
    }

       protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult( requestCode, resultCode, data );
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
        if (id == R.id.action_refresh ) {// Create Display of everything!
            try {
                getPostResponseFromLambda();
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
    public void launchAddPostActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), AddDifferentPostActivity.class);
        startActivity(intent);
    }
    public void LogoutFromFacebook(View v){
        Snackbar.make(v, "Replace with your own action ", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
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
        Intent intent = new Intent();
        /*pass in username and password info to MainActivity*/
        if (username == null) {
            username = "";
        }
        intent.putExtra("username", username);
        setResult(RESULT_OK, intent);
        finish();
    }

}
