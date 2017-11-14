package com.example.ramona.ydbrothers.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.example.ramona.ydbrothers.Entity.DebtEntities;
import com.example.ramona.ydbrothers.Entity.UserEntities;
import com.example.ramona.ydbrothers.Models.Constant;
import com.example.ramona.ydbrothers.Models.Models;
import com.example.ramona.ydbrothers.Models.NumberTextWatcher;
import com.example.ramona.ydbrothers.R;
import com.example.ramona.ydbrothers.SQLite.MyDatabaseHelper;

/**
 * Created by Ramona on 7/19/2017.
 */

public class DetailDebt extends AppCompatActivity {
    TextView TVUserCreditor, TVUserDebtor, TVTotalDebt;
    ToggleButton toggleButton;
    EditText EDDebt;
    ImageView IVCreditor, IVDebtor;
    Button btnSave;
    String sCreditor, sDebtor;
    UserEntities creditor, debtor;
    DebtEntities debtEntities;
    MyDatabaseHelper db;
    Boolean check = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Detail Debt");
        setContentView(R.layout.lt_detail_deb);
        db = new MyDatabaseHelper(DetailDebt.this);
        initControl();
        initData();
        initEvent();
    }

    private void initControl() {
        IVCreditor = (ImageView) findViewById(R.id.IVCreditor);
        IVDebtor = (ImageView) findViewById(R.id.IVDebtor);
        EDDebt = (EditText) findViewById(R.id.ETDebtIncrease);
        EDDebt.addTextChangedListener(new NumberTextWatcher(EDDebt));
        TVUserCreditor = (TextView) findViewById(R.id.TVUserCreditor);
        TVUserDebtor = (TextView) findViewById(R.id.TVUserDebtor);
        TVTotalDebt = (TextView) findViewById(R.id.TVTotalDebt);
        btnSave = (Button) findViewById(R.id.btnSave);
        toggleButton = (ToggleButton) findViewById(R.id.ToggleButton);
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constant.MYPACKAGE);
        if (bundle != null) {
            sCreditor = bundle.getString(Constant.CREDITOR_ID);
            sDebtor = bundle.getString(Constant.DEBTOR_ID);
            debtEntities = db.getRowDebt(sCreditor, sDebtor);
            creditor = db.getUserById(sCreditor);
            debtor = db.getUserById(sDebtor);
            TVUserCreditor.setText(creditor.getsUserName());
            TVUserDebtor.setText(debtor.getsUserName());
            TVTotalDebt.setText(Models.formatAmount(debtEntities.getiDebt()) + " VND");
            Glide.with(DetailDebt.this).load(creditor.getsUserImage()).into(IVCreditor);
            Glide.with(DetailDebt.this).load(debtor.getsUserImage()).into(IVDebtor);
        }
    }

    private void initEvent() {
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check = true;
                } else {
                    check = false;
                }
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EDDebt.getText().length() != 0) {
                    if (check) {
                        debtEntities = db.getRowDebt(sCreditor, sDebtor);
                        int newDebt = debtEntities.getiDebt() - Integer.parseInt(EDDebt.getText().toString().replaceAll("[,]", ""));
                        if (newDebt > 0) {
                            db.updateDebt(debtEntities.getsDebtID(), newDebt);
                            Models.NotifyDebtChanged(DetailDebt.this);
                            TVTotalDebt.setText(Models.formatAmount(newDebt) + " VND");

                        } else if (newDebt == 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(DetailDebt.this);
                            builder.setTitle("Delete Debt");
                            builder.setMessage("Are you sure you want to delete this debt ?");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (db.DeleteSingleDebt(debtEntities.getsDebtID())) {
                                        Models.NotifyDebtChanged(DetailDebt.this);
                                        finish();
                                    }
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        } else if (newDebt < 0) {
                            Toast.makeText(DetailDebt.this, "You input value is larger than debt !", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        debtEntities = db.getRowDebt(sCreditor, sDebtor);
                        int newdebt = debtEntities.getiDebt() + Integer.parseInt(EDDebt.getText().toString().replaceAll("[,]", ""));
                        db.updateDebt(debtEntities.getsDebtID(), newdebt);
                        Models.NotifyDebtChanged(DetailDebt.this);
                        TVTotalDebt.setText(Models.formatAmount(newdebt) + " VND");

                    }
                } else {
                    Toast.makeText(DetailDebt.this, "You haven't fill the information !", Toast.LENGTH_SHORT).show();
                }
                db.close();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        if (sharedPreferences.getBoolean(Constant.FLAG_CHECK_DEBT, false)) {
//            initData();
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putBoolean(Constant.FLAG_CHECK_DEBT, false);
//            editor.commit();
//        }
    }
}
