package sharehome.com.androidsharehome2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import sharehome.com.androidsharehome2.model.*;

import static sharehome.com.androidsharehome2.AppHelper.*;

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

    public LinearLayout layoutHeader;
    public ImageView profileImage;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static int UPLOADIMAGE = 0;

    public static final ApiClientFactory factory = new ApiClientFactory();
    public static final AwscodestarsharehomelambdaClient client =
            factory.build(AwscodestarsharehomelambdaClient.class);
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
        findCurrentGroupName();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        layoutHeader = (LinearLayout) navigationView.getHeaderView(0);
        TextView welcomeText = (TextView) layoutHeader.findViewById(R.id.WelcomeText);
        String text = welcomeText.getText().toString() + " " +
                AppHelper.getCurrUser();
        welcomeText.setText(text);
        profileImage = (ImageView) layoutHeader.findViewById(R.id.profileImage);
//        if(AppHelper.getUploadedProfileImgs()) {
//            loadProfileImage();
//        }
            setImageView();
            loadProfileImage();
    }
    private void setImageView() {
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asktoUploadImage();
            }
        });
    }
    private void asktoUploadImage() {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(ProfileActivity.this).create();
        alertDialog.setTitle("Change your profile image");
        alertDialog.setMessage("sure to change your profile image?");
        alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Please select your favorite profile" +
                                        " image",
                                Toast.LENGTH_LONG).show();
                        uploadProfileImage();
                    }
                });
        alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, "cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
        alertDialog.getButton(Dialog.BUTTON_NEGATIVE).
                setTextColor(Color.parseColor("#3399ff"));
        alertDialog.getButton(Dialog.BUTTON_POSITIVE).
                setTextColor(Color.parseColor("#3399ff"));
    }
    private void uploadProfileImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, UPLOADIMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UPLOADIMAGE) {
            profileImage.setImageDrawable(getPicture(data.getData()));
            saveProfileImgs();
        }
    }

    private void saveProfileImgs() {
        ActivityCompat.requestPermissions(ProfileActivity.this,
                new String[]{"android.permission.WRITE_EXTERNAL_STORAGE",
                        "android.permission.READ_EXTERNAL_STORAGE"
                },
                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
    }

    public Drawable getPicture(Uri selectedImage) {
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(selectedImage);
        } catch (FileNotFoundException e) {
            return getResources().getDrawable(R.drawable.logo);
        }
        Drawable source = Drawable.createFromStream(inputStream, selectedImage.toString());
        source.setBounds(0,72,0,72);
        profile_img_bitmap = ((BitmapDrawable) source).getBitmap();
        Drawable scaled = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(profile_img_bitmap,
                PROFILE_IMAGE_WIDTH, PROFILE_IMAGE_HEIGHT, true));
        return scaled;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
//                    #####################################################
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    profile_img_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] b = baos.toByteArray();
//
                    final String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                    // Open the file.
                    try {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory()+ "//" + ProfileImgFN);
                                    fos.write(encodedImage.getBytes());
                                    fos.close();
                                    AppHelper.setUploadedProfileImgs(true);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //                    #####################################################
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
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
            getPool().getCurrentUser().signOut();
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
            getPool().getCurrentUser().signOut();
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

                try {
                    final ResultStringResponse response = client.groupPost
                            (AppHelper.getCurrUser(), newGroupName, "create");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            // Notifies user
//                            if (response.getResult().startsWith("succ")) {
//                                String m = "Group " + newGroupName + " created!";
//                                Toast.makeText(getApplicationContext(), m, Toast.LENGTH_LONG).show();
//                            } else {
                                showDialogMessage("Successfully created group: ".toUpperCase() + newGroupName
                                        , response.getResult());
//                            }
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
                                if(response.contains("Group")&& response.contains("already exists!"))
                                showDialogMessage(errormsg, String.format("Group \"%s\" already exists!", newGroupName));
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
        if(getCurrgroupName() == null){
            progressDialog.dismiss();
            showDialogMessage("Failed to add user: ".toUpperCase() + AddUserName,
                    "you currently do not belong to a group");
            return;
        }

      new Thread(new Runnable() {
            public void run() {
                TextView groupName = ((TextView) findViewById(R.id.currentGroupName));
                final String GroupName = groupName.getText().toString();
                Handler handler = new Handler(getMainLooper());

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
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                showDialogMessage(msg, response.getResult());
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
                                if (response.contains("does not exist!") && response.contains("User"))
                                showDialogMessage(errormsg, String.format("User \"%s\" does not exist!",AddUserName));
                                else showDialogMessage(errormsg, response);
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
        userDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        userDialog.getButton(Dialog.BUTTON_NEUTRAL).
                setTextColor(Color.parseColor("#3399ff"));
    }

    private void loadProfileImage() {
        final Handler handler = new Handler(getMainLooper());
        Thread loadImgs = new Thread(new Runnable() {
            @Override
            public void run() {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(Environment.getExternalStorageDirectory()+ "//" +
                            ProfileImgFN);
                    byte[] reader = new byte[fis.available ()];
                    while(fis.read(reader)!=-1){}
                    String previouslyEncodedImage = (new String(reader));
                    Log.i("Data", ProfileImgFN);
                    fis.close();
                    if( !previouslyEncodedImage.equalsIgnoreCase("") ){
                        if (!AppHelper.getUploadedProfileImgs()){
                            byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
                            profile_img_bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                            AppHelper.setUploadedProfileImgs(true);
                        }
//            scale it to proper dimension
                        final Drawable scaled = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(profile_img_bitmap,
                                PROFILE_IMAGE_WIDTH, PROFILE_IMAGE_HEIGHT, true));
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                profileImage.setImageDrawable(scaled);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        loadImgs.start();
    }

}
