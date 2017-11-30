package com.webmyne.sqlitedatabaseexample.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.webmyne.sqlitedatabaseexample.R;
import com.webmyne.sqlitedatabaseexample.activity.UpdateActivity;
import com.webmyne.sqlitedatabaseexample.helper.AppConstants;
import com.webmyne.sqlitedatabaseexample.model.User;
import com.webmyne.sqlitedatabaseexample.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chiragpatel on 29-11-2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    private final Context context;
    private ArrayList<User> userList;
    private DatabaseHelper helper;
    private OnclickItem onclickItem;


    public UserAdapter(Context context, ArrayList<User> userList, OnclickItem onclickItem) {
        this.context = context;
        this.userList = userList;
        this.onclickItem = onclickItem;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.setValues(userList.get(position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setDataList(ArrayList<User> data) {
        userList = new ArrayList<>();
        userList = data;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        private final AppCompatTextView txtName;
        private final AppCompatTextView txtEmail;
        private final AppCompatTextView txtPassword;
        private final AppCompatTextView txtMobile;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtPassword = itemView.findViewById(R.id.txtPassword);
            txtMobile = itemView.findViewById(R.id.txtMobile);
        }

        public void setValues(final User user) {
            Log.e("data", user.getName());
            txtName.setText(user.getName());
            txtEmail.setText(user.getEmail());
            txtPassword.setText(user.getPassword());
            txtMobile.setText(user.getMobile());
            // Glide.with(context).load(movie.getPoster_path()).placeholder(R.mipmap.ic_launcher).into(imgPic);
            // Glide.with(context).load(movie.getPoster_path()).placeholder(R.mipmap.ic_launcher)
            //    .into("http://image.tmdb.org/t/p/ + 9O7gLzmreU0nGkIB6K3BsJbzvNv.jpg");
            helper = new DatabaseHelper(context);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onclickItem.onClickItem(user);
                    return false;
                }
            });
        }
    }

    public interface OnclickItem {
        void onClickItem(User user);
    }


}
