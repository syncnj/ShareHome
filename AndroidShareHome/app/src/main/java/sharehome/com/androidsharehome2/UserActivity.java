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

import com.amazonaws.mobileconnectors.pinpoint.*;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import static sharehome.com.androidsharehome2.AppHelper.*;

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
//    private ImageView profileImage;
    public static PinpointManager pinpointManager;

    public  LinearLayout layoutHeader;
    public  ImageView profileImage;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static int UPLOADIMAGE = 0;
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
         /* get login info*/
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        layoutHeader = (LinearLayout) navigationView.getHeaderView(0);
        TextView welcomeText = (TextView) layoutHeader.findViewById(R.id.WelcomeText);
        String text = welcomeText.getText().toString() + " " +
                AppHelper.getCurrUser();
        welcomeText.setText(text);
        profileImage = (ImageView) layoutHeader.findViewById(R.id.profileImage);

        setImageView();
        findCurrentGroupName();
        posts = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,
                R.layout.task_item, posts);
        postListView = (ListView) findViewById(R.id.post_list);
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

//        if(AppHelper.getUploadedProfileImgs()) {
//            loadProfileImage();
//        }
        loadProfileImage();
    }


    private void loadProfileImage() {
//        SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(this);
//        String previouslyEncodedImage = shre.getString("image_data", "");
//        if( !previouslyEncodedImage.equalsIgnoreCase("") ){
//            byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
//            profile_img_bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
////            scale it to proper dimension
//            Drawable scaled = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(profile_img_bitmap,
//                    PROFILE_IMAGE_WIDTH, PROFILE_IMAGE_HEIGHT, true));
//            profileImage.setImageDrawable(scaled);
//        }
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
                        Boolean test = AppHelper.getUploadedProfileImgs();
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
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        loadImgs.start();
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
        AlertDialog alertDialog = new AlertDialog.Builder(UserActivity.this).create();
        alertDialog.setTitle("Change your profile image");
        alertDialog.setMessage("sure to change your profile image?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       Toast.makeText(getApplicationContext(), "Please select your favorite profile" +
                                       " image",
                               Toast.LENGTH_LONG).show();
                       uploadProfileImage();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "cancel",
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

    private String getCurrentGroupName() {
          if (AppHelper.getCurrgroupName()!=null){
              return AppHelper.getCurrgroupName();
          }
          findCurrentGroupName();
          return null;
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
                        getPostResponseFromLambda();

//            TextView Menu = (TextView) layoutHeader.findViewById(R.id.Menu);
//            if (Menu == null){
//                showDialogMessage("WIERD", "MENU IS NULL", false);
//            }
//            else Menu.setText(AppHelper.getCurrgroupName());
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
           if (resultCode == RESULT_OK && requestCode == UPLOADIMAGE) {
               profileImage.setImageDrawable(getPicture(data.getData()));
               saveProfileImgs();
           }
    }

    private void saveProfileImgs() {
        //             save images to sharePreference
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        profile_img_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] b = baos.toByteArray();
//
//        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
//
//        SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor edit=shre.edit();
//        edit.putString("image_data",encodedImage);
//        edit.commit();
//        AppHelper.setUploadedProfileImgs(true);

        ActivityCompat.requestPermissions(UserActivity.this,
                new String[]{"android.permission.WRITE_EXTERNAL_STORAGE",
                        "android.permission.READ_EXTERNAL_STORAGE"
                },
                MY_PERMISSIONS_REQUEST_READ_CONTACTS);


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
                Log.d(TAG , e.getMessage());
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
        Intent intent = new Intent(this, LoginActivity.class);
        /*pass in username info to Login*/
        if (username == null) {
            username = "";
        }
        intent.putExtra("username", username);
        setResult(RESULT_OK, intent);
        startActivity(intent);
        finish();
    }

}
