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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.UserService;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.facebook.login.LoginManager;

import sharehome.com.androidsharehome2.backend.GroceryService;

public class AddGroceryListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LoginManager loginManager;

    EditText GroceryNameInput;
    Spinner StatusSpinner;
    int     groceryStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grocery_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        loginManager = LoginManager.getInstance();
        StatusSpinner=(Spinner) findViewById(R.id.spinner_groceryStatus);

        GroceryNameInput = (EditText)findViewById(R.id.input_GroceryName);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    //implemtation on functions that submit new grocery to backend.




    }

    public void SubmitGroceryToServer(View v){
        String StatusString = StatusSpinner.getSelectedItem().toString();
        if(StatusString.equals("full")){
            groceryStatus =3;
        }
        else if (StatusString.equals("running low")){
            groceryStatus = 2;
        }
        else {
            groceryStatus = 1;
        }
        GroceryService.getInstance().createNewGroceryAsync(Backendless.UserService.CurrentUser().getProperty("groupId").toString(), GroceryNameInput.getText().toString(),groceryStatus, new AsyncCallback<String>() {
            @Override
            public void handleResponse(String response) {
                System.out.println("Success in adding new grocery");
                String m = "Successfully added " + GroceryNameInput.getText().toString();
                Toast.makeText(getApplicationContext(), m, Toast.LENGTH_LONG).show();
                Log.i("Success", "successfully add new grocery to groupId"+Backendless.UserService.CurrentUser().getProperty("groupId").toString());
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                String m = "Adding " + GroceryNameInput.getText().toString()+" failed.\n" + fault.getMessage();

                Toast.makeText(getApplicationContext(), m, Toast.LENGTH_LONG).show();
                Log.i("Error","Server reported an error when createing a grocery for " +GroceryNameInput.getText().toString()+ fault.getMessage() );

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
            // TODO: This doesn't exit the app
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
    //Deleted the associate default button
    public void oneTimePurchaseHandler (View view){
        Toast.makeText(getApplicationContext(),"need to be added into grocery list with onetime property",Toast.LENGTH_SHORT).show();
    }

    public void dailyPurchaseHandler(View view){

        Toast.makeText(getApplicationContext(),"need to be added into grocery list with daily purchase property",Toast.LENGTH_SHORT).show();
    }
}
