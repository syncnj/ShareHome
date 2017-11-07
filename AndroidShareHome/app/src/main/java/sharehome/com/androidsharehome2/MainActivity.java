package sharehome.com.androidsharehome2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private GroupService g = null;
    private BackendlessUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, AddPostActivity.class);
        startActivity(intent);
        // Exit if coming from different activity logging out
        if(getIntent().getBooleanExtra("EXIT", false)){
            finish();
            return;
        }
        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:a64bd04e-ca82-401e-918f-74fef106fcfb", // Identity pool ID
                Regions.US_EAST_1 // Region
        );
        setContentView(R.layout.activity_main);
        // Initialize the Cognito Sync client
        CognitoSyncManager syncClient = new CognitoSyncManager(
                getApplicationContext(),
                Regions.US_EAST_1, // Region
                credentialsProvider);
        // Create a record in a dataset and synchronize with the server
        Dataset dataset = syncClient.openOrCreateDataset("myDataset");
        dataset.put("myKey", "myValue");
        dataset.synchronize(new DefaultSyncCallback() {
            @Override
            public void onSuccess(Dataset dataset, List newRecords) {
                //Your handler code here
                Toast.makeText(getBaseContext(), "success!!!!!!", Toast.LENGTH_LONG).show();
            }
        });
        //initialize Backendless server
        String appVersion = "v1";
        callbackManager = CallbackManager.Factory.create();
        Backendless.initApp( this, "19E73C32-D357-313A-FF64-12612084E000", "19F8B6BD-34A1-655C-FF3E-9BD31F1B0C00",appVersion);

        if(Backendless.UserService.CurrentUser() == null) {

            loginManager = LoginManager.getInstance();

            // login with facebook
            Backendless.UserService.loginWithFacebookSdk(MainActivity.this,
                    callbackManager,
                    new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser loggedInUser) {
                            // user logged in successfully

                            user = loggedInUser;

                            if (user.getProperty("groupId") == null) {
                                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                startActivity(intent);
                                return;
                            }

                            // Create Display of everything!

                            try {
                                g = GroupService.getInstance();
                            } catch (Exception e) {
                                System.out.println("It unsuccessfully found a group");
                            }
                            g.getAllPostAsync(user.getProperty("groupId").toString(), new AsyncCallback<List<Post>>() {
                                @Override
                                public void handleResponse(List<Post> response) {
                                    // To show data, uncomment
                                    // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                                    // System.out.println(response);
                                    ListView postListView = (ListView) findViewById(R.id.post_list);
                                    PostAdapter adapter = new PostAdapter(getApplicationContext(), response);
                                    postListView.setAdapter(adapter);
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    System.out.println("Failed to get all posts");
                                    Toast.makeText(getApplicationContext(), "Something happened :(\nUnable to retrieve posts", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            // failed to log in
                        }
                    });
        } else {
            user = Backendless.UserService.CurrentUser();
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
        }


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
                Intent intent = new Intent(getApplicationContext(), AddPostActivity.class);
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
        callbackManager.onActivityResult( requestCode, resultCode, data );
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
                loginManager.logOut();
                finish();
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
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            try {
                loginManager.logOut();
                finish();
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
        Intent intent = new Intent(getApplicationContext(), AddPostActivity.class);
        startActivity(intent);
    }
    public void LogoutFromFacebook(View v){

        Snackbar.make(v, "Replace with your own action ", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}