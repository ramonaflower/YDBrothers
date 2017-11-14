package com.example.ramona.ydbrothers.Activity;


import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ramona.ydbrothers.Entity.UserEntities;
import com.example.ramona.ydbrothers.Models.Constant;
import com.example.ramona.ydbrothers.Models.Models;
import com.example.ramona.ydbrothers.R;
import com.example.ramona.ydbrothers.SQLite.MyDatabaseHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by aideo on 3/29/2017.
 */

public class AddUser extends AppCompatActivity {
    private EditText EDAddUserName, EDAddAddress, EDAddPhone;
    private Button btnSave, btnDone;
    private ImageView IVAddImgUser1;
    private Bitmap img;
    private MyDatabaseHelper db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add New User");
        setContentView(R.layout.lt_add_new_user);
        db = new MyDatabaseHelper(AddUser.this);
        initControl();
        // initData();
        initEvent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            byte[] temp = data.getByteArrayExtra(Constant.CROPPED_IMAGE);
            if (temp != null) {
                img = BitmapFactory.decodeByteArray(temp, 0, temp.length);
                Glide.with(AddUser.this).load(temp).into(IVAddImgUser1);
            }
        }
    }

    private void initData() {
//        Intent intent = getIntent();
//        byte[] temp = intent.getByteArrayExtra("croppedImage");
//        if (temp != null){
//            img = BitmapFactory.decodeByteArray(temp, 0, temp.length);
//            Glide.with(AddUser.this).load(temp).into(IVAddImgUser1);
//            //IVAddImgUser1.setImageBitmap(img);
//        }

    }

    private void initControl() {
        EDAddUserName = (EditText) findViewById(R.id.EDAddUserName);

        EDAddAddress = (EditText) findViewById(R.id.EDAddAddress);

        EDAddPhone = (EditText) findViewById(R.id.EDAddPhone);

        IVAddImgUser1 = (ImageView) findViewById(R.id.IVAddImgUser1);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDone = (Button) findViewById(R.id.btnDone);
    }

    private void initEvent() {

        IVAddImgUser1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                } else {
                    Intent intent = new Intent(AddUser.this, CroperImage.class);
                    startActivityForResult(intent, 101);
                }
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EDAddUserName.getText().toString().isEmpty() || EDAddAddress.getText().toString().isEmpty() || EDAddPhone.getText().toString().isEmpty()) {
                    Toast.makeText(AddUser.this, "You haven't fill the information !", Toast.LENGTH_SHORT).show();
                } else {
                    String temp1 = saveToInternalStorage(img, Models.randomID());
                    if (temp1 == null) {
                        Toast.makeText(AddUser.this, "You haven't upload image !", Toast.LENGTH_SHORT).show();
                    } else {
                        UserEntities entities = new UserEntities();
                        //-entities.setsUserID(Models.randomID());
                        entities.setsUserName(EDAddUserName.getText().toString());
                        entities.setsAddress(EDAddAddress.getText().toString());
                        entities.setsUserImage(temp1);
                        entities.setsPhone(EDAddPhone.getText().toString());
                        if (db.addUser(entities)) {
                            Log.d("TAG", "add thanh cong");
                            Toast.makeText(AddUser.this, "User Register successfully !", Toast.LENGTH_SHORT).show();
                            Models.NotifyDataSetChanged(AddUser.this);
                        }
                    }
                }
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }


    private String saveToInternalStorage(Bitmap bitmapImage, String sName) {
        String temp = sName + ".jpg";
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        //storage/emulated/0/
        File directory = cw.getDir("imgUser", Context.MODE_PRIVATE);
        directory.mkdir();
        // Create imageDir
        File mypath = new File(directory, temp);
        FileOutputStream fos = null;
        if (bitmapImage != null) {
            try {
                fos = new FileOutputStream(mypath);
                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return mypath.getAbsolutePath();
        } else {
            return null;
        }
    }
}
