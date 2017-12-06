package com.webmyne.sqlitedatabaseexample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;

import com.webmyne.sqlitedatabaseexample.R;
import com.webmyne.sqlitedatabaseexample.helper.AppConstants;
import com.webmyne.sqlitedatabaseexample.helper.InputValidation;
import com.webmyne.sqlitedatabaseexample.model.User;
import com.webmyne.sqlitedatabaseexample.sql.DatabaseHelper;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private NestedScrollView nestedScrollView;
    private AppCompatButton appCompatButtonUpdate;
    private TextInputLayout textInputLayoutMobile;
    private TextInputEditText textInputEditTextMobile;
    private TextInputLayout textInputLayoutPassword;
    private TextInputEditText textInputEditTextPassword;
    private TextInputLayout textInputLayoutEmail;
    private TextInputEditText textInputEditTextEmail;
    private TextInputLayout textInputLayoutName;
    private TextInputEditText textInputEditTextName;
    private UpdateActivity context;
    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private User userDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        getSupportActionBar().setTitle("Update Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getIntentData();
        init();
        actionListener();
        initObjects();
    }

    private void getIntentData() {
        if (getIntent() != null) {
            userDetail = (User) getIntent().getSerializableExtra(AppConstants.UserDetail);
        }
    }

    private void init() {
        context = UpdateActivity.this;
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        appCompatButtonUpdate = (AppCompatButton) findViewById(R.id.appCompatButtonUpdate);
        textInputLayoutMobile = (TextInputLayout) findViewById(R.id.textInputLayoutMobile);
        textInputEditTextMobile = (TextInputEditText) findViewById(R.id.textInputEditTextMobile);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputEditTextName = (TextInputEditText) findViewById(R.id.textInputEditTextName);

        textInputEditTextName.setText(userDetail.getName());
        textInputEditTextEmail.setText(userDetail.getEmail());
        textInputEditTextPassword.setText(userDetail.getPassword());
        textInputEditTextMobile.setText(userDetail.getMobile());

    }

    private void actionListener() {
        appCompatButtonUpdate.setOnClickListener(this);
    }

    private void initObjects() {
        inputValidation = new InputValidation(context);
        databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonUpdate:
                updateDataToSQLite();
                break;

        }
    }

    private void updateDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }

        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }

        if (!inputValidation.isInputEditTextFilled(textInputEditTextMobile, textInputLayoutMobile, getString(R.string.error_message_mobile))) {
            return;
        }

        if (userDetail != null) {
            Log.e("data", String.valueOf(userDetail));
            userDetail.setName(textInputEditTextName.getText().toString().trim());
            userDetail.setEmail(textInputEditTextEmail.getText().toString().trim());
            userDetail.setPassword(textInputEditTextPassword.getText().toString().trim());
            userDetail.setMobile(textInputEditTextMobile.getText().toString().trim());

            databaseHelper.updateUser(userDetail);

            // Snack Bar to show success message that record saved successfully
            Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
            Intent intent = new Intent(context, UsersListActivity.class);
            startActivity(intent);
            finish();


        } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(nestedScrollView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
