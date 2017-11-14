package com.example.ramona.ydbrothers.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ramona.ydbrothers.Adapters.AdapterSpinner;
import com.example.ramona.ydbrothers.Entity.DebtEntities;
import com.example.ramona.ydbrothers.Entity.UserEntities;
import com.example.ramona.ydbrothers.Models.Constant;
import com.example.ramona.ydbrothers.Models.NumberTextWatcher;
import com.example.ramona.ydbrothers.R;
import com.example.ramona.ydbrothers.SQLite.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ramona on 7/26/2017.
 */

public class AddDebt extends AppCompatActivity {
    ImageView imageView;
    TextView textView;
    EditText editText;
    MyDatabaseHelper db;
    List<UserEntities> listSpinner = new ArrayList<>();
    List<DebtEntities> listDebt = new ArrayList<>();
    DebtEntities debtEntities;
    Spinner spinner;
    AdapterSpinner adapterSpinner;
    String sCreditor, sDebtor;
    Button btnsave1, btndone1;
    UserEntities userEntities;
    int a;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add New Debt");
        setContentView(R.layout.lt_add_new_debt);
        db = new MyDatabaseHelper(AddDebt.this);
        adapterSpinner = new AdapterSpinner(AddDebt.this, listSpinner);
        initControl();
        //initData();


        initEvent();
    }

    private void initControl() {
        spinner = (Spinner) findViewById(R.id.spinner);
        imageView = (ImageView) findViewById(R.id.img1);
        textView = (TextView) findViewById(R.id.txt1);
        editText = (EditText) findViewById(R.id.ED1);
        editText.addTextChangedListener(new NumberTextWatcher(editText));
        btnsave1 = (Button) findViewById(R.id.btnSave1);
        btndone1 = (Button) findViewById(R.id.btnDone1);
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            sCreditor = (String) bundle.get(Constant.CREDITOR_ID);
            UserEntities entities = db.getUserById(sCreditor);
            Glide.with(AddDebt.this).load(entities.getsUserImage()).into(imageView);
            textView.setText(entities.getsUserName());
        }
        listSpinner.clear();
        listSpinner.addAll(db.getAllUser());
        for (int i = 0; i<listSpinner.size(); i++){
            if (listSpinner.get(i).getsUserID().equals(sCreditor)){
                a = i;
            }
        }
        listSpinner.remove(a);
        spinner.setAdapter(adapterSpinner);
        listDebt.clear();
        listDebt.addAll(db.getAllRowDebt());
        Log.e("????", listSpinner.size() + "");
        Log.e("????", listDebt.size() + "");
        db.close();
    }

    private void initEvent() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sDebtor = listSpinner.get(position).getsUserID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sDebtor = listSpinner.get(0).getsUserID();
            }
        });
        btnsave1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().length() == 0) {
                    //Toast.makeText(AddDebt.this, "You haven't fill the information !", Toast.LENGTH_SHORT).show();
                    Toast.makeText(AddDebt.this, sCreditor+" "+sDebtor, Toast.LENGTH_SHORT).show();
                } else {
                    if (db.CheckDebtIsAlreadyInDBorNot(sCreditor, sDebtor)){
                        Toast.makeText(AddDebt.this, "This debt already exist !", Toast.LENGTH_SHORT).show();
                    } else {
                        int debt = Integer.parseInt(editText.getText().toString().replaceAll("[,]", ""));
                        debtEntities = new DebtEntities(sCreditor, sDebtor, debt);
                        Log.e("ID chủ nợ :", sCreditor);
                        Log.e("ID con nợ :", sDebtor);
                        if (db.addDebt(debtEntities)) {
                            Toast.makeText(AddDebt.this, "Add new debt successfully!", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                db.close();
            }
        });
        btndone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}
