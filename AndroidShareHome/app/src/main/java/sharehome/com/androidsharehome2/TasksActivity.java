package sharehome.com.androidsharehome2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;



import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;



public class TasksActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    private LoginManager loginManager;
//            GroupService groupService;
//            Task header = new Task();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

//        loginManager = LoginManager.getInstance();

//        groupService = GroupService.getInstance();
//        groupService.getMemberListAsync(Backendless.UserService.CurrentUser().getProperty("groupId").toString(), new AsyncCallback<String>() {
//            @Override
//            public void handleResponse(String response) {
//                String FILENAME = "members";
//
//               try {
//                   FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
//                   fos.write(response.getBytes());
//                   fos.close();
//               }catch(Exception e){
//                   System.out.println("TasksActivity EXCEPTION when writing members to file");
//               }
//
//            }

//            @Override
//            public void handleFault(BackendlessFault fault) {
//                System.out.println("members not retrieved");
//            }
//        });

        // Prepare header for display
//        header.setTaskName("Task Name");
//        header.setUserOnDuty("Person in Charge");
//        header.setStartTime(new Date(0L));
//        header.setNextUserNameOnDuty("");

        //local mocks for new user

//BackendlessUser user = Backendless.UserService.CurrentUser();
//        ArrayList<Task> tasks = new ArrayList<>();
//        new Task("dishes", "Week", new ArrayList<User>().add(user));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//
//        groupService = GroupService.getInstance();
//        groupService.getAllTaskAsync(Backendless.UserService.CurrentUser().getProperty("groupId").toString(), new AsyncCallback<List<Task>>() {
//            @Override
//            public void handleResponse(List<Task> response) {
//                ListView taskListView = (ListView) findViewById(R.id.task_list);
//                response.add(0, header);
//                TaskAdapter adapter = new TaskAdapter(getApplicationContext(), response);
//                taskListView.setAdapter(adapter);
//            }
//
//            @Override
//            public void handleFault(BackendlessFault fault) {
//
//            }
//        });

    }
    public void onScheduleBaseSelected(AdapterView<?> parent, View view,
                                         int pos, long id){
          // An item was selected.
         Object returnBase = parent.getItemAtPosition(pos);
          //get item at position

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
//            groupService = GroupService.getInstance();
//            groupService.getAllTaskAsync(Backendless.UserService.CurrentUser().getProperty("groupId").toString(), new AsyncCallback<List<Task>>() {
//                @Override
//                public void handleResponse(List<Task> response) {
//                    ListView taskListView = (ListView) findViewById(R.id.task_list);
//                    response.add(0, header);
//                    TaskAdapter adapter = new TaskAdapter(getApplicationContext(), response);
//                    taskListView.setAdapter(adapter);
//                }
//
//                @Override
//                public void handleFault(BackendlessFault fault) {
//
//                }
//            });

            return true;
        } else if(id == R.id.action_logout){
//            loginManager.logOut();
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
//            loginManager.logOut();
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
    /**
     * Starts the AddTaskActivity
     *
     * @param view The view of the button which called this method
     */
//    public void launchAddTaskActivity(View view) {
//        Intent intent = new Intent(getApplicationContext(), AddTaskActivityNAV.class);
//        startActivity(intent);
//    }


}
