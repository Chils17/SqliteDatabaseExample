package com.webmyne.sqlitedatabaseexample.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.webmyne.sqlitedatabaseexample.R;
import com.webmyne.sqlitedatabaseexample.helper.AppConstants;
import com.webmyne.sqlitedatabaseexample.helper.InputValidation;
import com.webmyne.sqlitedatabaseexample.helper.SessionManager;
import com.webmyne.sqlitedatabaseexample.model.User;
import com.webmyne.sqlitedatabaseexample.sql.DatabaseHelper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatTextView textViewLinkRegister;
    private AppCompatButton appCompatButtonLogin;
    private TextInputLayout textInputLayoutPassword;
    private TextInputEditText textInputEditTextPassword;
    private TextInputLayout textInputLayoutEmail;
    private TextInputEditText textInputEditTextEmail;
    private LoginActivity context;
    private DatabaseHelper databaseHelper;
    private InputValidation inputValidation;
    private NestedScrollView nestedScrollView;
    private SessionManager session;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        initStetho();
        init();
        actionListeners();
        initObjects();
    }

    private void initStetho() {
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());

    }

    private void init() {
        context = LoginActivity.this;
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        textViewLinkRegister = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);
        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);

        session = new SessionManager(context);
        Toast.makeText(context, "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
    }

    private void actionListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }

    private void initObjects() {
        databaseHelper = new DatabaseHelper(context);
        inputValidation = new InputValidation(context);
        user = new User();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonLogin:
                verifyFromSQLite();
                break;
            case R.id.textViewLinkRegister:
                // Navigate to RegisterActivity
                Intent intentRegister = new Intent(context, RegisterActivity.class);
                startActivity(intentRegister);
                finish();
                break;
        }
    }

    private void verifyFromSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_email))) {
            return;
        }

        if (databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim()
                , textInputEditTextPassword.getText().toString().trim())) {

            String email = textInputEditTextEmail.getText().toString().trim();
            user = databaseHelper.getUser(email);
            session.createLoginSession(user);
            session.getUserData(user);
            Intent accountsIntent = new Intent(context, UsersListActivity.class);
            emptyInputEditText();
            startActivity(accountsIntent);
            finish();


        } else {
            // Snack Bar to show success message that record is wrong
            Snackbar.make(nestedScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }
}
