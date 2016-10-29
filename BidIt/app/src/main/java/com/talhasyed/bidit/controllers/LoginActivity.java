package com.talhasyed.bidit.controllers;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.talhasyed.bidit.R;
import com.talhasyed.bidit.credential.Authentication;
import com.talhasyed.bidit.credential.LocalAuthException;
import com.talhasyed.bidit.model.UserModel;
import com.talhasyed.bidit.storage.UserCRUD;


public class LoginActivity extends AppCompatActivity {
    private UserCRUD userCRUD;
    private EditText etUsername;
    private EditText etPassword;
    private TextInputLayout tilUserName, tilPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.textViewLoginUserName);
        etPassword = (EditText) findViewById(R.id.textViewLoginPassword);
        tilPassword = (TextInputLayout) findViewById(R.id.textInputLayoutLoginPassword);
        tilUserName = (TextInputLayout) findViewById(R.id.textInputLayoutLoginUserName);
        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.buttonSignInSignIn);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });


        userCRUD = new UserCRUD(getContentResolver());

    }


    private void attemptLogin() {
        // Reset errors.
        etUsername.setError(null);
        etPassword.setError(null);

        // Store values at the time of the login attempt.
        String userName = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            etPassword.setError(getString(R.string.error_invalid_password));
            focusView = etPassword;
            cancel = true;
        }

        // Check for a valid userName address.
        if (TextUtils.isEmpty(userName)) {
            etUsername.setError(getString(R.string.error_field_required));
            focusView = etUsername;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            boolean authSuccess = false;
            UserModel userModel = null;
            try {
                userModel = userCRUD.validateLogin(userName, password);
                authSuccess = true;
            } catch (LocalAuthException e) {
                e.printStackTrace();
                switch (e.getErrorType()) {
                    case UserName:
                        tilUserName.setError(e.getMessage());
                        tilUserName.setErrorEnabled(true);
                        break;
                    case Password:
                        tilPassword.setError(e.getMessage());
                        tilPassword.setErrorEnabled(true);
                        break;
                    case General:
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                }
            }
            if (authSuccess) {
                Authentication.setLoggedInUser(userModel, getBaseContext());
                return;
            } else {
                return;
            }
        }
    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


//   Snackbar.make(etUsername, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
//    .setAction(android.R.string.ok, new View.OnClickListener() {
//        @Override
//        @TargetApi(Build.VERSION_CODES.M)
//        public void onClick(View v) {
//            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//        }
//    });


}

