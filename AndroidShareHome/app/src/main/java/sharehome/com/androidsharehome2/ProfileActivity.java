package sharehome.com.androidsharehome2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sharehome.com.androidsharehome2.model.*;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "ProfileActivity";
    //    private LoginManager loginManager;
    private EditText _findUserName;
    private EditText CreateGroup_Name;
    String AddUserName;
    String CreateGroup_NameString;
    TextView GroupName;
    TextView groupPrompt;
    Button AddMember;
    Button _createGroup;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> groupMemberNames;
    private AlertDialog userDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        _findUserName = (EditText) findViewById(R.id.findUserName);
        CreateGroup_Name = (EditText) findViewById(R.id.group_name_input);
        CreateGroup_Name.setEnabled(true);
        CreateGroup_NameString = CreateGroup_Name.getText().toString();
        GroupName = (TextView) findViewById(R.id.currentGroupName);
        groupPrompt = (TextView) findViewById(R.id.groupPrompt);
        //findCurrentGroupName();
        if (AppHelper.getCurrgroupName() == null){
            groupPrompt.setText("You are currently not in a group");
        }
        else {
            GroupName.setText(AppHelper.getCurrgroupName());
        }

        groupMemberNames = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,
                R.layout.task_item, groupMemberNames);
        AddMember = (Button) findViewById(R.id.addMember);
        _createGroup = (Button) findViewById(R.id.createGroupButton);
        //listGroupMembers();
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

//    private void listGroupMembers() {
//        final ListView groupMemberListView = (ListView) findViewById(R.id.group_member_names);
//        Thread getMemberNameThread = new Thread(new Runnable() {
//            public void run() {
//                ApiClientFactory factory = new ApiClientFactory();
//                final AwscodestarsharehomelambdaClient client =
//                        factory.build(AwscodestarsharehomelambdaClient.class);
//                ListOfString memberNames = client.groupGet(AppHelper.getCurrUser(), "listMembers");
//                groupMemberNames.clear();
//
//                for (String memberName : memberNames){
//                    groupMemberNames.add(memberName);
//                }
//               ProfileActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        groupMemberListView.setAdapter(adapter);
//                    }
//                });
//            }
//        });
//        getMemberNameThread.start();
//    }

    private void findCurrentGroupName() {
        final ProgressDialog progressDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);

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
                        GroupName.setText(response.get(0));
                    }
                });
            }
        });
        taskThread.start();
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
            findCurrentGroupName();
            return true;
        } else if (id == R.id.action_logout) {
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

    public void onCreateGroup(View v) {
        EditText groupName = ((EditText) findViewById(R.id.group_name_input));
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

    public void createGroup(final String newGroupName) {
        final ProgressDialog progressDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Groups...");
        progressDialog.show();
        if(newGroupName.equals("") || newGroupName == null){
            progressDialog.dismiss();
//            Toast.makeText(getApplicationContext(),
//                    "please have a valid groupName", Toast.LENGTH_LONG).show();
            showDialogMessage("Invalid group name".toUpperCase(), "please have a valid group name");
            return;
        }
        Thread taskThread = new Thread(new Runnable() {
            public void run() {
                Handler handler = new postSubmitHanlder(getMainLooper());
                ApiClientFactory factory = new ApiClientFactory();
                final AwscodestarsharehomelambdaClient client =
                        factory.build(AwscodestarsharehomelambdaClient.class);
                try {
                    final ResultStringResponse response = client.groupPost
                            (AppHelper.getCurrUser(), newGroupName, "create");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            // Notifies user
                            if (response.getResult().startsWith("succ")) {
                                String m = "Group " + newGroupName + " created!";
                                Toast.makeText(getApplicationContext(), m, Toast.LENGTH_LONG).show();
                            } else {
                                showDialogMessage("Failed to create the group: ".toUpperCase(), response.getResult());
                            }
                        }
                    });
                } catch (Exception e) {
                    progressDialog.dismiss();
                    final String errormsg = "Failed to create group: ".toUpperCase() + newGroupName;
                    final String backmsg= e.getMessage();
                    JSONObject jObject;
                    try {
                        jObject = new JSONObject(backmsg);
                        final String response = jObject.getString("result");
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                showDialogMessage(errormsg, response);
                            }
                        });
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        taskThread.start();
    }

    public void AddMember(View v) {
        final ProgressDialog progressDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Inviting user into the current group...");
        progressDialog.show();
        AddUserName = _findUserName.getText().toString();
        if(AddUserName.equals("") || AddUserName == null){
            progressDialog.dismiss();
//            Toast.makeText(getApplicationContext(),
//                    "please have a valid username", Toast.LENGTH_LONG).show();
            showDialogMessage("invalid user name".toUpperCase(), "please have a valid user name");
            return;
        }

      new Thread(new Runnable() {
            public void run() {
                TextView groupName = ((TextView) findViewById(R.id.currentGroupName));
                final String GroupName = groupName.getText().toString();
                Handler handler = new Handler(getMainLooper());
                ApiClientFactory factory = new ApiClientFactory();
                final AwscodestarsharehomelambdaClient client =
                        factory.build(AwscodestarsharehomelambdaClient.class);
                try {
                    final ResultStringResponse response = client.groupPost
                            (AddUserName, GroupName, "add");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
    //                      // Notifies user
                            if (response.getResult().startsWith("succ") || response.getResult().startsWith("Up")) {
                                String msg = "Add " + AddUserName + " Sccuessfully!";
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            } else {
                                String errormsg = "Failed to add user: " + AddUserName;
    //                            Toast.makeText(getApplicationContext(), errormsg, Toast.LENGTH_LONG).show();
                                showDialogMessage(errormsg, response.getResult());
                            }
                        }
                    });
                } catch (Exception e) {
                    progressDialog.dismiss();
                    final String errormsg = "Failed to add user: ".toUpperCase() + AddUserName;
                    final String backmsg= e.getMessage();
                    JSONObject jObject;
                    try {
                        jObject = new JSONObject(backmsg);
                        final String response = jObject.getString("result");
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                showDialogMessage(errormsg, response);
                            }
                        });
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void showDialogMessage(String title, String body){
        final AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();
                } catch (Exception e) {
                    // Log failure
                    Log.e(TAG,"Dialog dismiss failed");
                }
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }
}
