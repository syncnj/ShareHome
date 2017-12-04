package sharehome.com.androidsharehome2;

import android.app.ProgressDialog;
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
//import com.amazonaws.mobileconnectors.apigateway.*;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;



public class UserActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "UserActivity";

//    private CallbackManager callbackManager;
//    private LoginManager loginManager;
//    private GroupService g = null;
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

//        call api
//        ApiClientFactory factory = new ApiClientFactory();
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
        if (id == R.id.action_refresh && false) {// Create Display of everything!

            try {

            } catch (Exception e) {
                System.out.println("It unsuccessfully found a group");
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