package com.talhasyed.bidit.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
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

public class SignUpActivity extends AppCompatActivity {
    private UserCRUD userCRUD;

    private EditText etUsername, etPassword, etConfirmPassword, etName;
    private TextInputLayout tilUserName, tilPassword, tilConfirmPassword, tilName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userCRUD = new UserCRUD(getContentResolver());

        etUsername = (EditText) findViewById(R.id.editTextSignUpUserName);
        etPassword = (EditText) findViewById(R.id.editTextSignUpPassword);
        etConfirmPassword = (EditText) findViewById(R.id.editTextSignUpConfirmPassword);
        etName = (EditText) findViewById(R.id.editTextSignUpName);


        tilName = (TextInputLayout) findViewById(R.id.textInputLayoutSignUpName);
        tilConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutSignUpConfirmPassword);
        tilPassword = (TextInputLayout) findViewById(R.id.textInputLayoutSignUpPassword);
        tilUserName = (TextInputLayout) findViewById(R.id.textInputLayoutSignUpUserName);

        etUsername.addTextChangedListener(new SelfClearingEditTextWatcher(tilUserName));
        etPassword.addTextChangedListener(new SelfClearingEditTextWatcher(tilPassword));
        etConfirmPassword.addTextChangedListener(new SelfClearingEditTextWatcher(tilConfirmPassword));
        etName.addTextChangedListener(new SelfClearingEditTextWatcher(tilName));


        etConfirmPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptSignUp();
                    return true;
                }
                return false;
            }
        });


        Button mEmailSignInButton = (Button) findViewById(R.id.buttonSignUpSignUp);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });
    }


    private void attemptSignUp() {
        // Reset errors.
        tilUserName.setError(null);
        tilUserName.setErrorEnabled(false);
        tilPassword.setError(null);
        tilPassword.setErrorEnabled(false);
        tilConfirmPassword.setError(null);
        tilConfirmPassword.setErrorEnabled(false);
        tilName.setError(null);
        tilName.setErrorEnabled(false);


        // Store values at the time of the login attempt.
        String userName = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String confirmedPassword = etConfirmPassword.getText().toString();
        String name = etName.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            tilPassword.setError(getString(R.string.error_incorrect_password));
            tilPassword.setErrorEnabled(true);
            focusView = etPassword;
            cancel = true;
        } else {

        }


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(confirmedPassword) && !isPasswordValid(confirmedPassword)) {
            tilConfirmPassword.setError(getString(R.string.error_incorrect_password));
            tilConfirmPassword.setErrorEnabled(true);
            focusView = etConfirmPassword;
            cancel = true;
        } else {
            if (!confirmedPassword.equals(password)) {
                tilConfirmPassword.setError("Passwords do not match");
                tilConfirmPassword.setErrorEnabled(true);
                focusView = etConfirmPassword;
                cancel = true;
            }
        }

        // Check for a valid userName address.
        if (TextUtils.isEmpty(userName)) {
            tilUserName.setError(getString(R.string.error_field_required));
            tilUserName.setErrorEnabled(true);
            focusView = etUsername;
            cancel = true;
        }

        // Check for a valid userName address.
        if (TextUtils.isEmpty(name)) {
            tilName.setError(getString(R.string.error_field_required));
            tilName.setErrorEnabled(true);
            focusView = etUsername;
            cancel = true;
        }


        if (cancel) {
            focusView.requestFocus();
        } else {
            boolean authSuccess = false;
            UserModel userModel = null;
            try {
                userModel = userCRUD.signUp(userName, password, name);
                authSuccess = true;
            } catch (LocalAuthException e) {
                e.printStackTrace();
                switch (e.getErrorType()) {
                    case UserName:
                        tilUserName.setError(e.getMessage());
                        tilUserName.setErrorEnabled(true);
                        focusView = etUsername;
                        cancel = true;
                        break;
                    case Password:
                        tilPassword.setError(e.getMessage());
                        tilPassword.setErrorEnabled(true);
                        focusView = etPassword;
                        cancel = true;
                        break;
                    case General:
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                }
            }
            if (cancel) {
                focusView.requestFocus();
                return;
            }
            if (authSuccess) {
                Authentication.setLoggedInUser(userModel, getBaseContext());
                startActivity(new Intent(this, MainActivity.class));
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
