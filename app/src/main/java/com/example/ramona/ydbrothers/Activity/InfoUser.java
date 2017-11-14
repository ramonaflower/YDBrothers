package com.example.ramona.ydbrothers.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ramona.ydbrothers.Entity.UserEntities;
import com.example.ramona.ydbrothers.Models.Constant;
import com.example.ramona.ydbrothers.R;
import com.example.ramona.ydbrothers.SQLite.MyDatabaseHelper;

/**
 * Created by Ramona on 7/13/2017.
 */

public class InfoUser extends AppCompatActivity {
    ImageView imgUser;
    TextView tvUserName, tvPhone, tvAddress;
    MyDatabaseHelper db;
    String sIDUser = null;
    UserEntities userEntities = new UserEntities();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("User Infor");
        setContentView(R.layout.lt_info_user);
        db = new MyDatabaseHelper(InfoUser.this);
        initControl();
        initData();
        //initEvent();
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            sIDUser = (String) bundle.get(Constant.INFOR_USER);
        }
        userEntities = db.getUserById(sIDUser);
        Glide.with(this).load(userEntities.getsUserImage()).into(imgUser);
        tvUserName.setText(userEntities.getsUserName().toString());
        tvAddress.setText(userEntities.getsAddress().toString());
        tvPhone.setText(userEntities.getsPhone().toString());
        db.close();
    }

    private void initControl() {
        imgUser = (ImageView) findViewById(R.id.img_Student);
        tvUserName = (TextView) findViewById(R.id.txtNameStudent);
        tvAddress = (TextView) findViewById(R.id.txtAddress);
        tvPhone = (TextView) findViewById(R.id.txtMobile);
    }

}
