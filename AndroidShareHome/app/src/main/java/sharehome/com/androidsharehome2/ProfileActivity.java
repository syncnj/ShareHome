package sharehome.com.androidsharehome2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
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

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    private LoginManager loginManager;
    private EditText AddMeber_Email;
    private EditText CreateGroup_Name;
    String AddMember_EmailString;
    String CreateGroup_NameString;
    TextView GroupName;
    TextView Instruction;
    Button AddMemberViaEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        AddMeber_Email = (EditText)findViewById(R.id.findMember_email);
        CreateGroup_Name = (EditText)findViewById(R.id.group_name_input);
        CreateGroup_NameString = CreateGroup_Name.getText().toString();
        GroupName = (TextView) findViewById(R.id.currentGroupName);
        AddMemberViaEmail = (Button) findViewById(R.id.addMember);

//        if(Backendless.UserService.CurrentUser().getProperty("groupId") == null) {
//            GroupName.setText("Not in group.");
//            Instruction = (TextView) findViewById(R.id.textView6);
//            Instruction.setText("You are not in any GROUP yet.\n\nPlease join or create a group first.");
//            Instruction.setGravity(Gravity.CENTER);
//            AddMeber_Email.setVisibility(EditText.INVISIBLE);
//            AddMemberViaEmail.setVisibility(Button.INVISIBLE);
//        } else {
////            GroupName.setText(Backendless.UserService.CurrentUser().getProperty("groupId").toString());
////            Log.i("Error", Backendless.UserService.CurrentUser().getProperty("groupId").toString());
////            System.out.println("Error when "+Backendless.UserService.CurrentUser().getProperty("groupId").toString());
//            GroupService.getInstance().getGroupByIdAsync(Backendless.UserService.CurrentUser().getProperty("groupId").toString(), new AsyncCallback<Group>() {
//                        @Override
//                        public void handleResponse(Group response) {
//                            GroupName.setText(response.getGroupName());
//                        }
//
//                        @Override
//                        public void handleFault(BackendlessFault fault) {
//
//                        }
//                    });
//        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        loginManager = LoginManager.getInstance();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
//        if(Backendless.UserService.CurrentUser().getProperty("groupId") == null){
//            return;
//        }
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
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
        String newGroupName = groupName.getText().toString();
//        try {
//            createGroup(newGroupName, Backendless.UserService.CurrentUser().getObjectId());
//        } catch (Exception e) {
//            String m = "For whatever reason, group not created.";
//            Toast.makeText(getApplicationContext(), m, Toast.LENGTH_LONG).show();
//        }
        // Hides keyboard since button has been clicked
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        // cleans edit text
        groupName.setText("");
        // Notifies user
        String m = "Group " + newGroupName + " created!";
        Toast.makeText(getApplicationContext(), m, Toast.LENGTH_LONG).show();
    }
    public String createGroup(final String name, final String userId){

//        final GroupService group1 = GroupService.getInstance();

        new Thread(new Runnable() {
            public void run() {

                // synchronous backendless API call here:

//                //String groupID;
//                try {
//                    //System.out.println(""  + "entered ");
//                    group1.createNewGroup( name, userId);//Backendless.UserService.CurrentUser().getObjectId());
//                }
//                catch (Exception ex){
//                    System.out.println("" + ex + "exception find here");
//                }

            }
        }).start();

        return "";
    }

    public void AddMember(View v){
        AddMember_EmailString = AddMeber_Email.getText().toString();
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
    }
}
