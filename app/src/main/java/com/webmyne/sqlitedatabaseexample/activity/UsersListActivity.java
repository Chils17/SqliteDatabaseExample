package com.webmyne.sqlitedatabaseexample.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.webmyne.sqlitedatabaseexample.R;
import com.webmyne.sqlitedatabaseexample.adapter.UserAdapter;
import com.webmyne.sqlitedatabaseexample.helper.AppConstants;
import com.webmyne.sqlitedatabaseexample.helper.SessionManager;
import com.webmyne.sqlitedatabaseexample.model.User;
import com.webmyne.sqlitedatabaseexample.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.webmyne.sqlitedatabaseexample.helper.SessionManager.KEY_EMAIL;
import static com.webmyne.sqlitedatabaseexample.helper.SessionManager.KEY_NAME;


public class UsersListActivity extends AppCompatActivity {

    private AppCompatTextView txtName;
    private RecyclerView rvUserList;
    private UsersListActivity context;
    private ArrayList<User> userList;
    private UserAdapter adapter;
    private DatabaseHelper databaseHelper;
    private SessionManager session;
    private AppCompatTextView txtEmail;
    private SharedPreferences sharedPreferences;
    private AppCompatTextView txtAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        getSupportActionBar().setTitle("User Details");
        init();
        initRecycler();
    }

    private void init() {
        context = UsersListActivity.this;
        session = new SessionManager(context);
        txtName = (AppCompatTextView) findViewById(R.id.txtName);
        txtEmail = (AppCompatTextView) findViewById(R.id.txtEmail);
        txtAlert = (AppCompatTextView) findViewById(R.id.txtAlert);
        rvUserList = (RecyclerView) findViewById(R.id.rvUserList);

        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        session.checkLogin();
        sharedPreferences = getSharedPreferences("UserPreference", context.MODE_PRIVATE);

        txtName.setText(sharedPreferences.getString(KEY_NAME, ""));
        txtEmail.setText(sharedPreferences.getString(KEY_EMAIL, ""));

    }

    private void initRecycler() {
        userList = new ArrayList<>();
        adapter = new UserAdapter(context, userList, new UserAdapter.OnclickItem() {
            @Override
            public void onClickItem(final User user) {
                final CharSequence[] items = {"Edit", "Delete"};
                new AlertDialog.Builder(context)
                        .setTitle("User Records")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.e("item click", "" + i);
                                if (i == 0) {
                                    Intent intent = new Intent(context, UpdateActivity.class);
                                    intent.putExtra(AppConstants.UserDetail, user);
                                    context.startActivity(intent);
                                    userList = (ArrayList<User>) databaseHelper.getAllUser();
                                } else if (i == 1) {
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                                    alertDialog.setTitle("Confirm Delete...");
                                    alertDialog.setMessage("Are you sure you want delete this?");
                                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            databaseHelper.deleteUser(user);
                                            userList = (ArrayList<User>) databaseHelper.getAllUser();
                                            checkVisibility(userList);
                                            adapter.notifyDataSetChanged();
                                            Toast.makeText(context, "You clicked on YES", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(context, "You clicked on NO", Toast.LENGTH_SHORT).show();
                                            dialog.cancel();
                                        }
                                    });
                                    alertDialog.show();
                                }


                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });
        rvUserList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        rvUserList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(20, 10, 20, 10);
            }
        });
        rvUserList.setItemAnimator(new DefaultItemAnimator());
        rvUserList.setHasFixedSize(true);
        rvUserList.setAdapter(adapter);

        databaseHelper = new DatabaseHelper(context);

        getDataFromSQLite();
    }

    /**
     * This method is to fetch all user records from SQLite
     */
    private void getDataFromSQLite() {
        if (userList != null) {
            userList.clear();
            userList.addAll(databaseHelper.getAllUser());
            adapter.setDataList(userList);
            checkVisibility(userList);
        } else {
            checkVisibility(userList);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                session.logoutUser();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void checkVisibility(ArrayList<User> userList) {
        if (userList != null && userList.size() > 0) {
            txtAlert.setVisibility(View.GONE);
            rvUserList.setVisibility(View.VISIBLE);
        } else {
            txtAlert.setVisibility(View.VISIBLE);
            rvUserList.setVisibility(View.GONE);
        }
    }

}
