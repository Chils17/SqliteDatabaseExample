package com.webmyne.sqlitedatabaseexample.activity;

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
import android.view.View;

import com.webmyne.sqlitedatabaseexample.R;
import com.webmyne.sqlitedatabaseexample.helper.AppConstants;
import com.webmyne.sqlitedatabaseexample.helper.InputValidation;
import com.webmyne.sqlitedatabaseexample.model.User;
import com.webmyne.sqlitedatabaseexample.sql.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private NestedScrollView nestedScrollView;
    private AppCompatTextView appCompatTextViewLoginLink;
    private AppCompatButton appCompatButtonRegister;
    private TextInputLayout textInputLayoutConfirmPassword;
    private TextInputEditText textInputEditTextConfirmPassword;
    private TextInputLayout textInputLayoutPassword;
    private TextInputEditText textInputEditTextPassword;
    private TextInputLayout textInputLayoutEmail;
    private TextInputEditText textInputEditTextEmail;
    private TextInputLayout textInputLayoutName;
    private TextInputEditText textInputEditTextName;
    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private User user;
    private RegisterActivity context;
    private TextInputLayout textInputLayoutMobile;
    private TextInputEditText textInputEditTextMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        init();
        actionListeners();
        initObjects();
    }

    private void init() {
        context = RegisterActivity.this;
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        appCompatTextViewLoginLink = (AppCompatTextView) findViewById(R.id.appCompatTextViewLoginLink);
        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.appCompatButtonRegister);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmPassword);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputEditTextName = (TextInputEditText) findViewById(R.id.textInputEditTextName);
        textInputLayoutMobile = (TextInputLayout) findViewById(R.id.textInputLayoutMobile);
        textInputEditTextMobile = (TextInputEditText) findViewById(R.id.textInputEditTextMobile);

    }

    private void actionListeners() {
        appCompatButtonRegister.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);
    }

    private void initObjects() {
        inputValidation = new InputValidation(context);
        databaseHelper = new DatabaseHelper(context);
        user = new User();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.appCompatButtonRegister:
                postDataToSQLite();
                break;

            case R.id.appCompatTextViewLoginLink:
                Intent intent = new Intent(context, LoginActivity.class);
                intent.putExtra(AppConstants.UserName, textInputEditTextEmail.getText().toString().trim());
                startActivity(intent);
                finish();
                break;
        }
    }

    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword,
                textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
            return;
        }

        if (!inputValidation.isInputEditTextFilled(textInputEditTextMobile, textInputLayoutMobile, getString(R.string.error_message_mobile))) {
            return;
        }

        if (!databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim())) {
            String name = textInputEditTextName.getText().toString().trim();
            String email = textInputEditTextEmail.getText().toString().trim();
            String password = textInputEditTextPassword.getText().toString().trim();
            String mobile = textInputEditTextMobile.getText().toString().trim();

            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setMobile(mobile);

            databaseHelper.addUser(user);

            // Snack Bar to show success message that record saved successfully
            Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
            emptyInputEditText();
            Intent intent = new Intent(context, UsersListActivity.class);
            startActivity(intent);


        } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(nestedScrollView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
        }

    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextName.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
        textInputEditTextMobile.setText(null);
    }
}
