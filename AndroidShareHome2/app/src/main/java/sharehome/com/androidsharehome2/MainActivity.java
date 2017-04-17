package sharehome.com.androidsharehome2;

import android.content.Intent;
import android.os.Bundle;
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

    CallbackManager callbackManager;
    LoginManager loginManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize Backendless server
        String appVersion = "v1";
        Backendless.initApp( this, "19E73C32-D357-313A-FF64-12612084E000", "19F8B6BD-34A1-655C-FF3E-9BD31F1B0C00",appVersion);
        //create new user
        BackendlessUser user = new BackendlessUser();

        user.setPassword("Gerald");


        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();


// login with facebook
        Backendless.UserService.loginWithFacebookSdk( MainActivity.this,
                callbackManager,
                new AsyncCallback<BackendlessUser>()
                {
                    @Override
                    public void handleResponse( BackendlessUser loggedInUser )
                    {
                        // user logged in successfully
                    }

                    @Override
                    public void handleFault( BackendlessFault fault )
                    {
                        // failed to log in
                    }
                } );



        Backendless.UserService.register( user, new AsyncCallback<BackendlessUser>()
        {
            @Override
            public void handleResponse( BackendlessUser backendlessUser )
            {
                Log.i( "Registration", backendlessUser.getEmail() + " successfully registered" );

            }
            public void handleFault( BackendlessFault fault )
            {
                // user update failed, to get the error code call fault.getCode()
                System.out.print("register failed");
            }
        } );





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

        // Create Display of everything!

        System.out.println("Here we are.");

        // ArrayList<Post> posts = server return;
        List<Post> posts = new ArrayList<Post>();
        GroupService g = null;
        try {
            g = GroupService.getInstance();
        }
        catch(Exception e){
            System.out.println("It unsuccessfully found a group");
        }
        g.getAllPostAsync("73429308-0C76-21C9-FF0C-BB7CAEEF9100", new AsyncCallback<List<Post>>() {
                @Override
                public void handleResponse(List<Post> response) {
                    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
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
        getMenuInflater().inflate(R.menu.main, menu);
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

        } else if (id == R.id.nav_new) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            //Todo:MAKE THIS to Exit
            try {
                loginManager.logOut();
                System.exit(0);
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
