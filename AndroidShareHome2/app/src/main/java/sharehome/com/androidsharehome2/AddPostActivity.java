package sharehome.com.androidsharehome2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import sharehome.com.androidsharehome2.backend.PostService;
import com.facebook.login.LoginManager;

import static sharehome.com.androidsharehome2.R.id.fab;

public class AddPostActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button SubmitPost;
    private EditText PostNameInput;
    private EditText PostContentInput;
    private LoginManager loginManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loginManager = LoginManager.getInstance();

        SubmitPost = (Button)findViewById(R.id.submitPost);
        PostNameInput = (EditText)findViewById(R.id.PostNameInput) ;
        PostContentInput = (EditText)findViewById(R.id.PostContentInput) ;


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    // Eliminate this method, directly call createPost
    public void SubmitPostToServer(View v){
        createPost(PostNameInput.getText().toString(), PostContentInput.getText().toString());
        System.out.println(Backendless.UserService.CurrentUser().getObjectId().toString() +Backendless.UserService.CurrentUser().getProperty("groupId").toString() + "userid");
    }

    public String createPost(final String postTitle, final String postContent){

        PostService.getInstance().createNewPostAsync(Backendless.UserService.CurrentUser().getObjectId(),
                Backendless.UserService.CurrentUser().getProperty("groupId").toString(), postTitle, postContent, new AsyncCallback<String>() {
            @Override
            public void handleResponse(String response) {
                String m = "Posted successfully.";
                Toast.makeText(getApplicationContext(), m, Toast.LENGTH_LONG).show();
                PostNameInput.setText("");
                PostContentInput.setText("");
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                String m = "Check internet connection and try again.";//  + fault.getMessage();
                Toast.makeText(getApplicationContext(), m, Toast.LENGTH_LONG).show();
            }
        });
        return "Hi";

//        final PostService postService = PostService.getInstance();
//        new Thread(new Runnable() {
//            public void run() {
//                try {
//                    postService.createNewPost(Backendless.UserService.CurrentUser().getObjectId(),
//                            Backendless.UserService.CurrentUser().getProperty("groupId").toString(), postTitle, postContent);
//                }
//                catch (Exception e){
//                    System.out.println(e + " exception found on creating post");
//                }
//            }
//        }).start();
//        return "";
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
            Intent intent = new Intent(this, MainActivity.class);
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
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            loginManager.logOut();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
