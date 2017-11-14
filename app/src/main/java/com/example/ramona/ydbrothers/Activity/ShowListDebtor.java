package com.example.ramona.ydbrothers.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.ramona.ydbrothers.Adapters.AdapterRVListDebtor;
import com.example.ramona.ydbrothers.Entity.DebtEntities;
import com.example.ramona.ydbrothers.Models.Constant;
import com.example.ramona.ydbrothers.Models.VerticalSpacingDecoration;
import com.example.ramona.ydbrothers.R;
import com.example.ramona.ydbrothers.SQLite.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aideo on 3/22/2017.
 */

public class ShowListDebtor extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterRVListDebtor adapterRVListDebtor;
    MyDatabaseHelper db;
    String sIDCreditor;
    List<DebtEntities> listDebtor = new ArrayList<>();
    FloatingActionButton fabAddDebt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("List Debtor");
        setContentView(R.layout.lt_list_deb);
        db = new MyDatabaseHelper(ShowListDebtor.this);
        adapterRVListDebtor = new AdapterRVListDebtor(listDebtor, ShowListDebtor.this, db);
        initControl();
        recyclerView.setAdapter(adapterRVListDebtor);
        initEvent();
    }

    private void initControl() {
        fabAddDebt = (FloatingActionButton) findViewById(R.id.fabAddDebt);
        fabAddDebt.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#292929")));
        recyclerView = (RecyclerView) findViewById(R.id.RVListDeb);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new VerticalSpacingDecoration(40));
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            sIDCreditor = (String) bundle.get(Constant.CREDITOR_ID);
            new GetListDebtor(sIDCreditor).execute();
            Log.e("checkid", sIDCreditor);
        }
    }

    private void initEvent() {
        adapterRVListDebtor.setOnItemClickListener(new AdapterRVListDebtor.clickListener() {
            @Override
            public void OnItemClickListener(int position) {
                Intent intent = new Intent(ShowListDebtor.this, DetailDebt.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constant.CREDITOR_ID, sIDCreditor);
                bundle.putString(Constant.DEBTOR_ID, listDebtor.get(position).getsDebtorID());
                intent.putExtra(Constant.MYPACKAGE, bundle);
                startActivity(intent);
            }

            @Override
            public void OnLongItemClickListener(int position) {

            }
        });
        fabAddDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowListDebtor.this, AddDebt.class);
                intent.putExtra(Constant.CREDITOR_ID, sIDCreditor);
                startActivity(intent);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fabAddDebt.getVisibility() == View.VISIBLE) {
                    fabAddDebt.hide();
                } else if (dy < 0 && fabAddDebt.getVisibility() != View.VISIBLE) {
                    fabAddDebt.show();
                }
            }
        });
    }

    public class GetListDebtor extends AsyncTask<Void, Void, List<DebtEntities>> {
        private String id;

        public GetListDebtor(String id) {
            this.id = id;
        }

        @Override
        protected List<DebtEntities> doInBackground(Void... params) {
            listDebtor.clear();
            listDebtor.addAll(db.getListDebtor(id));
            Log.e("checkid", "id2 " + id);
            db.close();
            return listDebtor;
        }

        @Override
        protected void onPostExecute(List<DebtEntities> debtEntities) {
            super.onPostExecute(debtEntities);
            adapterRVListDebtor.notifyDataSetChanged();
        }
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
        initData();
    }
}
