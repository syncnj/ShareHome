package sharehome.com.androidsharehome2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ListView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.regions.Regions;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.List;

import sharehome.com.androidsharehome2.backend.GroupService;
import sharehome.com.androidsharehome2.backend.Post;


public class UserActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "UserActivity";

    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private GroupService g = null;
    private BackendlessUser user;
    private CognitoUser user_aws;
    private String username;
    private ProgressDialog waitDialog;
    private AlertDialog userDialog;

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
//        //initialize Backendless server
//        String appVersion = "v1";
//        callbackManager = CallbackManager.Factory.create();
//        Backendless.initApp( this, "19E73C32-D357-313A-FF64-12612084E000", "19F8B6BD-34A1-655C-FF3E-9BD31F1B0C00",appVersion);
//
//        if(Backendless.UserService.CurrentUser() == null) {
//
//            loginManager = LoginManager.getInstance();
//
//            // login with facebook
//            Backendless.UserService.loginWithFacebookSdk(MainActivity.this,
//                    callbackManager,
//                    new AsyncCallback<BackendlessUser>() {
//                        @Override
//                        public void handleResponse(BackendlessUser loggedInUser) {
//                            // user logged in successfully
//
//                            user = loggedInUser;
//
//                            if (user.getProperty("groupId") == null) {
//                                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
//                                startActivity(intent);
//                                return;
//                            }
//
//                            // Create Display of everything!
//
//                            try {
//                                g = GroupService.getInstance();
//                            } catch (Exception e) {
//                                System.out.println("It unsuccessfully found a group");
//                            }
//                            g.getAllPostAsync(user.getProperty("groupId").toString(), new AsyncCallback<List<Post>>() {
//                                @Override
//                                public void handleResponse(List<Post> response) {
//                                    // To show data, uncomment
//                                    // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
//                                    // System.out.println(response);
//                                    ListView postListView = (ListView) findViewById(R.id.post_list);
//                                    PostAdapter adapter = new PostAdapter(getApplicationContext(), response);
//                                    postListView.setAdapter(adapter);
//                                }
//
//                                @Override
//                                public void handleFault(BackendlessFault fault) {
//                                    System.out.println("Failed to get all posts");
//                                    Toast.makeText(getApplicationContext(), "Something happened :(\nUnable to retrieve posts", Toast.LENGTH_LONG).show();
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void handleFault(BackendlessFault fault) {
//                            // failed to log in
//                        }
//                    });
//        } else {
//            user = Backendless.UserService.CurrentUser();
//            try {
//                g = GroupService.getInstance();
//            } catch (Exception e) {
//                System.out.println("It unsuccessfully found a group");
//            }
//            g.getAllPostAsync(user.getProperty("groupId").toString(), new AsyncCallback<List<Post>>() {
//                @Override
//                public void handleResponse(List<Post> response) {
//                    System.out.println(response);
//                    ListView postListView = (ListView) findViewById(R.id.post_list);
//                    PostAdapter adapter = new PostAdapter(getApplicationContext(), response);
//                    postListView.setAdapter(adapter);
//                    System.out.print("Successfully retrieved posts");
//                }
//
//                @Override
//                public void handleFault(BackendlessFault fault) {
//                    System.out.println("Failed to get all posts");
//                }
//            });
//        }


        // System.out.println("userid = " + user.getObjectId().toString());


//        Backendless.UserService.register( user, new AsyncCallback<BackendlessUser>()
//        {
//            @Override
//            public void handleResponse( BackendlessUser backendlessUser )
//            {
//                Log.i( "Registration", backendlessUser.getEmail() + " successfully registered" );
//
//            }
//            public void handleFault( BackendlessFault fault )
//            {
//                // user update failed, to get the error code call fault.getCode()
//                System.out.print("register failed");
//            }
//        } );





        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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


    @Override
    public void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        super.onActivityResult( requestCode, resultCode, data );
//        callbackManager.onActivityResult( requestCode, resultCode, data );

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
        if (id == R.id.action_refresh && user != null) {// Create Display of everything!

            try {
                g = GroupService.getInstance();
            } catch (Exception e) {
                System.out.println("It unsuccessfully found a group");
            }
            g.getAllPostAsync(user.getProperty("groupId").toString(), new AsyncCallback<List<Post>>() {
                @Override
                public void handleResponse(List<Post> response) {
                    System.out.println(response);
                    ListView postListView = (ListView) findViewById(R.id.post_list);
                    PostAdapter adapter = new PostAdapter(getApplicationContext(), response);
                    postListView.setAdapter(adapter);
                    System.out.print("Successfully retrieved posts");
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    System.out.println("Failed to get all posts");
                }
            });

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

        if(user == null){
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
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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
        // Get user details from CIP service
//            AppHelper.getPool().getUser(username).getDetailsInBackground(detailsHandler);
    }

//    GetDetailsHandler detailsHandler = new GetDetailsHandler() {
//        @Override
//        public void onSuccess(CognitoUserDetails cognitoUserDetails) {
//            closeWaitDialog();
//            // Store details in the AppHandler
//            AppHelper.setUserDetails(cognitoUserDetails);
//            showAttributes();
//            // Trusted devices?
//            handleTrustedDevice();
//        }
//
//        @Override
//        public void onFailure(Exception exception) {
//            closeWaitDialog();
//            showDialogMessage("Could not fetch user details!", AppHelper.formatException(exception), true);
//        }
//    };

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