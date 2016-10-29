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
import com.talhasyed.bidit.textwatchers.SelfClearingEditTextWatcher;


public class LoginActivity extends AppCompatActivity {
    private UserCRUD userCRUD;
    private EditText etUsername;
    private EditText etPassword;
    private TextInputLayout tilUserName, tilPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.editTextLoginUserName);
        etPassword = (EditText) findViewById(R.id.editTextLoginPassword);
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
        etUsername.addTextChangedListener(new SelfClearingEditTextWatcher(tilUserName));
        etPassword.addTextChangedListener(new SelfClearingEditTextWatcher(tilPassword));

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
        tilUserName.setError(null);
        tilPassword.setError(null);

        // Store values at the time of the login attempt.
        String userName = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            tilPassword.setError(getString(R.string.error_invalid_password));
            tilPassword.setErrorEnabled(true);
            focusView = etPassword;
            cancel = true;
        }

        // Check for a valid userName address.
        if (TextUtils.isEmpty(userName)) {
            tilUserName.setError(getString(R.string.error_field_required));
            tilUserName.setErrorEnabled(true);
            focusView = etUsername;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
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
        return password.length() > 4;
    }


}

