package sharehome.com.androidsharehome2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Contacts;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.Response;
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;

import sharehome.com.androidsharehome2.model.TaskList;
import sharehome.com.androidsharehome2.model.TaskListItem;
import sharehome.com.androidsharehome2.model.*;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    private LoginManager loginManager;
    private EditText _findUserName;
    private EditText CreateGroup_Name;
    String AddUserName;
    String CreateGroup_NameString;
    TextView GroupName;
    TextView Instruction;
    Button AddMember;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        _findUserName = (EditText)findViewById(R.id.findUserName);
        CreateGroup_Name = (EditText)findViewById(R.id.group_name_input);
        CreateGroup_NameString = CreateGroup_Name.getText().toString();
        GroupName = (TextView) findViewById(R.id.currentGroupName);
        findCurrentGroupName();
        AddMember = (Button) findViewById(R.id.addMember);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void findCurrentGroupName() {
        final ProgressDialog progressDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        AddUserName = _findUserName.getText().toString();

        Thread taskThread = new Thread(new Runnable() {
            public void run() {
                Handler handler = new taskSubmitHandler(getMainLooper());
                ApiClientFactory factory = new ApiClientFactory();
                final AwscodestarsharehomelambdaClient client =
                        factory.build(AwscodestarsharehomelambdaClient.class);
                final ResultStringResponse response = client.groupGet(AppHelper.getCurrUser());

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        GroupName.setText(response.getResult());
                    }
                });
            }
        });
        taskThread.start();
    }

    @Override
    public void onBackPressed() {
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
          //  loginManager.logOut();
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

         if (id == R.id.nav_logout) {
//            loginManager.logOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            finish();
        }


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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onCreateGroup (View v){
        EditText groupName = ((EditText)findViewById(R.id.group_name_input));
        final String newGroupName = groupName.getText().toString();

        // Hides keyboard since button has been clicked
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        // cleans edit text
        groupName.setText("");
        createGroup(newGroupName);

    }
    public void createGroup(final String newGroupName){
        final ProgressDialog progressDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Groups...");
        progressDialog.show();
        Thread taskThread = new Thread(new Runnable() {
            public void run() {
                Handler handler = new taskSubmitHandler(getMainLooper());
                ApiClientFactory factory = new ApiClientFactory();
                final AwscodestarsharehomelambdaClient client =
                        factory.build(AwscodestarsharehomelambdaClient.class);
                client.groupPost
                        (AppHelper.getCurrUser(), newGroupName, "create");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        // Notifies user
                        String m = "Group " + newGroupName + " created!";
                        Toast.makeText(getApplicationContext(), m, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

//        GroupService g = GroupService.getInstance();
//        g.addMemberbyEmailAsync(AddMember_EmailString, Backendless.UserService.CurrentUser().getProperty("groupId").toString(), new AsyncCallback<Boolean>() {
//            @Override
//            public void handleResponse(Boolean response) {
//                AddMeber_Email.setText("");
//                Toast.makeText(ProfileActivity.this, "New member added to group", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void handleFault(BackendlessFault fault) {
//                Toast.makeText(ProfileActivity.this, fault.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
public void AddMember(View v){
    final ProgressDialog progressDialog = new ProgressDialog(this,
            R.style.AppTheme_Dark_Dialog);
    progressDialog.setIndeterminate(true);
    progressDialog.setMessage("Inviting user into the current group...");
    progressDialog.show();
    AddUserName = _findUserName.getText().toString();
    final Runnable UI_Update = new Runnable() {
        @Override
        public void run() {
            progressDialog.dismiss();
            // Notifies user
            String msg = "Add " + AddUserName + " Sccuessfully!";
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    };

    Thread taskThread = new Thread(new Runnable() {
        public void run() {
            Handler handler = new taskSubmitHandler(getMainLooper());
            ApiClientFactory factory = new ApiClientFactory();
            final AwscodestarsharehomelambdaClient client =
                    factory.build(AwscodestarsharehomelambdaClient.class);
            client.groupPost
                    (AppHelper.getCurrUser(), AddUserName, "add");
            handler.post(UI_Update);
            }
        });
    taskThread.start();
    }
}
