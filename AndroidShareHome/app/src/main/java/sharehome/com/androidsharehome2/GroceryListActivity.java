package sharehome.com.androidsharehome2;

import android.content.Context;
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
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;
import java.util.List;

import sharehome.com.androidsharehome2.backend.Grocery;
import sharehome.com.androidsharehome2.backend.GroupService;
import sharehome.com.androidsharehome2.backend.Post;
import sharehome.com.androidsharehome2.backend.GroceryService;

import com.facebook.login.LoginManager;

public class GroceryListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LoginManager loginManager;
    GroupService groupService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loginManager = LoginManager.getInstance();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Create Display of everything!

        groupService = GroupService.getInstance();
        groupService.getAllGroceryAsync(Backendless.UserService.CurrentUser().getProperty("groupId").toString(), new AsyncCallback<List<Grocery>>() {
            @Override
            public void handleResponse(List<Grocery> response){
                // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                System.out.println(response);
                ListView groceryListView = (ListView) findViewById(R.id.grocery_list);
                GroceryAdapter adapter = new GroceryAdapter(getApplicationContext(), response);
                groceryListView.setAdapter(adapter);
            }


            @Override
            public void handleFault(BackendlessFault fault) {
                System.out.println("Failed to get all posts");
            }
        });



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
            groupService = GroupService.getInstance();
            groupService.getAllGroceryAsync(Backendless.UserService.CurrentUser().getProperty("groupId").toString(), new AsyncCallback<List<Grocery>>() {
                @Override
                public void handleResponse(List<Grocery> response){
                    ListView groceryListView = (ListView) findViewById(R.id.grocery_list);
                    GroceryAdapter adapter = new GroceryAdapter(getApplicationContext(), response);
                    groceryListView.setAdapter(adapter);
                }


                @Override
                public void handleFault(BackendlessFault fault) {
                    System.out.println("Failed to get all posts");
                }
            });

            return true;
        } else if(id == R.id.action_logout){
            loginManager.logOut();
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
            loginManager.logOut();
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
    public void launchAddGroceryListActivity(View view) {
        Intent intent = new Intent(GroceryListActivity.this, AddGroceryListActivity.class);
        startActivity(intent);
    }
}
