package sharehome.com.androidsharehome2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler;

public class ConfirmActivity extends AppCompatActivity {
    private EditText _usernameText;
    private EditText _confcodeText;

    private Button _confirmButton;
    private TextView reqCode;
    private String userName;
    private AlertDialog userDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("");
//        setSupportActionBar(toolbar);


//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

        init();
    }

    private void init() {

        Bundle extras = getIntent().getExtras();
        if (extras !=null) {
            if(extras.containsKey("username")) {
                userName = extras.getString("username");
                _usernameText = (EditText) findViewById(R.id.input_confirm_username);
                _usernameText.setText(userName);

                _confcodeText = (EditText) findViewById(R.id.input_code);
                _confcodeText.requestFocus();

                if(extras.containsKey("destination")) {
                    String dest = extras.getString("destination");
                    String delMed = extras.getString("deliveryMed");

                    TextView screenSubtext = (TextView) findViewById(R.id.textViewConfirmSubtext_1);
                    if(dest != null && delMed != null && dest.length() > 0 && delMed.length() > 0) {
                        screenSubtext.setText("A confirmation code was sent to "+dest+" via "+delMed);
                    }
                    else {
                        screenSubtext.setText("A confirmation code was sent");
                    }
                }
            }
            else {
                TextView screenSubtext = (TextView) findViewById(R.id.textViewConfirmSubtext_1);
                screenSubtext.setText("Request for a confirmation code or confirm with the code you already have.");
            }

        }

        _confcodeText = (EditText) findViewById(R.id.input_code);
//        _confcodeText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                if(s.length() == 0) {
//                    TextView label = (TextView) findViewById(R.id.textViewConfirmCodeLabel);
//                    label.setText(_confcodeText.getHint());
////                    _confcodeText.setBackground(getDrawable(R.drawable.text_border_selector));
//                }
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                TextView label = (TextView) findViewById(R.id.textViewConfirmCodeMessage);
//                label.setText(" ");
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if(s.length() == 0) {
//                    TextView label = (TextView) findViewById(R.id.textViewConfirmCodeLabel);
//                    label.setText("");
//                }
//            }
//        });

        _confirmButton = (Button) findViewById(R.id.btn_confirm);
        _confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendConfCode();
            }
        });

        reqCode = (TextView) findViewById(R.id.resend_code);
        reqCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reqConfCode();
            }
        });
    }


    private void sendConfCode() {
        userName = _usernameText.getText().toString();
        String confirmCode = _confcodeText.getText().toString();

        if(userName == null || userName.length() < 1) {
            TextView label = (TextView) findViewById(R.id.input_confirm_UsernameMessage);
            label.setText(_usernameText.getHint()+" cannot be empty");
//            username.setBackground(getDrawable(R.drawable.text_border_error));
            return;
        }

        if(confirmCode == null || confirmCode.length() < 1) {
            TextView label = (TextView) findViewById(R.id.input_CodeMessage);
            label.setText(_confcodeText.getHint()+" cannot be empty");
//            _confcodeText.setBackground(getDrawable(R.drawable.text_border_error));
            return;
        }

        AppHelper.getPool().getUser(userName).confirmSignUpInBackground(confirmCode, true, confHandler);
    }

    private void reqConfCode() {
        userName = _usernameText.getText().toString();
        if(userName == null || userName.length() < 1) {
            TextView label = (TextView) findViewById(R.id.input_confirm_UsernameMessage);
            label.setText(_usernameText.getHint()+" cannot be empty");
//            _usernameText.setBackground(getDrawable(R.drawable.text_border_error));
            return;
        }
        AppHelper.getPool().getUser(userName).resendConfirmationCodeInBackground(resendConfCodeHandler);

    }

    GenericHandler confHandler = new GenericHandler() {
        @Override
        public void onSuccess() {
            showDialogMessage("Success!",userName+" has been confirmed!", true);
        }

        @Override
        public void onFailure(Exception exception) {
            TextView label = (TextView) findViewById(R.id.input_confirm_UsernameMessage);
            label.setText("Confirmation failed!");
//            _usernameText.setBackground(getDrawable(R.drawable.text_border_error));

            label = (TextView) findViewById(R.id.input_CodeMessage);
            label.setText("Confirmation failed!");
//            _confcodeText.setBackground(getDrawable(R.drawable.text_border_error));

            showDialogMessage("Confirmation failed", AppHelper.formatException(exception), false);
        }
    };

    VerificationHandler resendConfCodeHandler = new VerificationHandler() {
        @Override
        public void onSuccess(CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            TextView mainTitle = (TextView) findViewById(R.id.header_text);
            mainTitle.setText("Confirm your account");
            _confcodeText = (EditText) findViewById(R.id.input_code);
            _confcodeText.requestFocus();
            showDialogMessage("Confirmation code sent.","Code sent to "+cognitoUserCodeDeliveryDetails.getDestination()+" via "+cognitoUserCodeDeliveryDetails.getDeliveryMedium()+".", false);
        }

        @Override
        public void onFailure(Exception exception) {
            TextView label = (TextView) findViewById(R.id.input_confirm_UsernameMessage);
            label.setText("Confirmation code resend failed");
//            _usernameText.setBackground(getDrawable(R.drawable.text_border_error));
            showDialogMessage("Confirmation code request has failed", AppHelper.formatException(exception), false);
        }
    };

    private void showDialogMessage(String title, String body, final boolean exitActivity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();
                    if(exitActivity) {
                        exit();
                    }
                } catch (Exception e) {
                    exit();
                }
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }

    private void exit() {
        Intent intent = new Intent();
        if(userName == null)
            userName = "";
        intent.putExtra("username",userName);
        setResult(RESULT_OK, intent);
        finish();
    }
}
