package sharehome.com.androidsharehome2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.NewPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.exceptions.CognitoNotAuthorizedException;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.BindView;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static final int LOGIN = 4;

    @BindView(R.id.input_username) EditText _usernameText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;

    //Continuations
    private MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation;
    private ForgotPasswordContinuation forgotPasswordContinuation;
    private NewPasswordContinuation newPasswordContinuation;

    // User Details
    private String username;
    private String password;
    private ProgressDialog progressDialog;
    private AlertDialog userDialog;

//    // authenticationHandler
//    protected AuthenticationHandler authHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        Intent intent = new Intent(this, ConfirmActivity.class);
//        startActivity(intent);

        Intent intent = new Intent(this, AddDifferentPostActivity.class);
        startActivity(intent);


        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
//                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        // Initialize application
        AppHelper.init(getApplicationContext());
        findCurrent();
    }

    public void login() {
        Log.d(TAG, "Login");
        username = _usernameText.getText().toString();
        password = _passwordText.getText().toString();
        if (!validate()) {
            onLoginFailed();
            return;
        }

        username = _usernameText.getText().toString();
        password = _passwordText.getText().toString();

        _loginButton.setEnabled(false);
        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();


        AppHelper.setUser(username);
        AppHelper.getPool().getUser(username).getSessionInBackground(authenticationHandler);
//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onLoginSuccess or onLoginFailed
//                        onLoginSuccess();
//                        // onLoginFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }

    private void showDialogMessage(String title, String body) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();
                } catch (Exception e) {
                    //
                }
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }

    private void closeWaitDialog() {
        try {
            progressDialog.dismiss();
        }
        catch (Exception e) {
            //
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_SIGNUP) {
//            if (resultCode == RESULT_OK) {
//
//                // TODO: Implement successful signup logic here
//                // By default we just finish the Activity and log them in automatically
//                this.finish();
//            }
//        }
        super.onActivityResult( requestCode, resultCode, data );
        switch (requestCode) {
            case REQUEST_SIGNUP:
                // Register user
                // TODO: Implement successful signup logic here
                if(resultCode == RESULT_OK) {
                    String name = data.getStringExtra("username");
                    if (!name.isEmpty()) {
                        _usernameText.setText(name);
                        _passwordText.setText("");
                        _passwordText.requestFocus();
                    }
                    String userPasswd = data.getStringExtra("password");
                    if (!userPasswd.isEmpty()) {
                        _passwordText.setText(userPasswd);
                    }
//                    if (!name.isEmpty() && !userPasswd.isEmpty()) {
//                        // We have the user details, so sign in!
//                        username = name;
//                        password = userPasswd;
//                        AppHelper.getPool().getUser(username).getSessionInBackground(authenticationHandler);
//                    }
                }
                break;
            case LOGIN:
                if(resultCode == RESULT_OK){
                    clearInput();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        Toast.makeText(getBaseContext(), "Login Successful!", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public void onLoginFailed() {
//        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
        TextView label = (TextView) findViewById(R.id.input_PasswordMessage);
        label.setText("");
    }

    public boolean validate() {
        boolean valid = true;

//        username = _usernameText.getText().toString();
//        password = _passwordText.getText().toString();

        if (username.isEmpty()) {
            _usernameText.setError("Enter a valid username");
            TextView label = (TextView) findViewById(R.id.input_UsernameMessage);
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 1 || password.length() > 10) {
            _passwordText.setError("Between 1 and 10 alphanumeric characters");
            valid = false;
            TextView label = (TextView) findViewById(R.id.input_PasswordMessage);
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    // callback
    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession cognitoUserSession, CognitoDevice device) {
            Log.e(TAG, "Auth Success");
            AppHelper.setCurrSession(cognitoUserSession);
            AppHelper.newDevice(device);
            closeWaitDialog();
            onLoginSuccess();
            /*pass username and password info to main activity task*/
            launchUser();
        }

        @Override
        public void getAuthenticationDetails(AuthenticationContinuation continuation, String username) {
//            closeWaitDialog();
            Locale.setDefault(Locale.US);
            getUserAuthentication(continuation, username);
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation) {
            closeWaitDialog();
//            mfaAuth(multiFactorAuthenticationContinuation);
        }

        @Override
        public void onFailure(Exception e) {
            closeWaitDialog();
//            TextView label = (TextView) findViewById(R.id.textViewUserIdMessage);
//            label.setText("Sign-in failed");
////            _passwordText.setBackground(getDrawable(R.drawable.text_border_error));
//
//            label = (TextView) findViewById(R.id.textViewUserIdMessage);
//            label.setText("Sign-in failed");
//            _usernameText.setBackground(getDrawable(R.drawable.text_border_error));
            showDialogMessage("Login failed", AppHelper.formatException(e));
            onLoginFailed();
        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {
            /**
             * For Custom authentication challenge, implement your logic to present challenge to the
             * user and pass the user's responses to the continuation.
             */
            if ("NEW_PASSWORD_REQUIRED".equals(continuation.getChallengeName())) {
                // This is the first sign-in attempt for an admin created user
                newPasswordContinuation = (NewPasswordContinuation) continuation;
                AppHelper.setUserAttributeForDisplayFirstLogIn(newPasswordContinuation.getCurrentUserAttributes(),
                        newPasswordContinuation.getRequiredAttributes());
                closeWaitDialog();
//                firstTimeSignIn();
            }
        }
    };

    private void launchUser() {
        Intent userActivity = new Intent(this, UserActivity.class);
        userActivity.putExtra("username", username);
        startActivityForResult(userActivity, LOGIN);
    }

    private void findCurrent() {
        CognitoUser user = AppHelper.getPool().getCurrentUser();
        username = user.getUserId();
        if(username != null) {
            AppHelper.setUser(username);
            _usernameText.setText(user.getUserId());
            user.getSessionInBackground(authenticationHandler);
        }
    }

    private void mfaAuth(MultiFactorAuthenticationContinuation continuation) {
        multiFactorAuthenticationContinuation = continuation;
        Intent mfaActivity = new Intent(this, MFAActivity.class);
        mfaActivity.putExtra("mode", multiFactorAuthenticationContinuation.getParameters().getDeliveryMedium());
        startActivityForResult(mfaActivity, 5);
    }

    private void clearInput() {
        if(_usernameText == null) {
            _usernameText = (EditText) findViewById(R.id.input_username);
        }

        if(_passwordText == null) {
            _passwordText = (EditText) findViewById(R.id.input_password);
        }

        _usernameText.setText("");
        _usernameText.requestFocus();
//        _usernameText.setBackground(getDrawable(R.drawable.text_border_selector));
        _passwordText.setText("");
//        _passwordText.setBackground(getDrawable(R.drawable.text_border_selector));
    }

    private void getUserAuthentication(AuthenticationContinuation continuation, String username) {
        if(username != null) {
            this.username = username;
            AppHelper.setUser(username);
        }
        if(this.password == null) {
            _usernameText.setText(username);
            password = _passwordText.getText().toString();
            if(password == null || password.length() < 1) {
                TextView label = (TextView) findViewById(R.id.input_PasswordMessage);
//                inPassword.setBackground(getDrawable(R.drawable.text_border_error));
                return;
            }

        }
        if (validate()){
            AuthenticationDetails authenticationDetails = new AuthenticationDetails(username, password, null);
            continuation.setAuthenticationDetails(authenticationDetails);
            continuation.continueTask();
        }
    }


}

