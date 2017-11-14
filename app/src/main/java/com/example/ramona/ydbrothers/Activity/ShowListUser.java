package com.example.ramona.ydbrothers.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.ramona.ydbrothers.Adapters.AdaperRVListUser;
import com.example.ramona.ydbrothers.Entity.UserEntities;
import com.example.ramona.ydbrothers.Models.Constant;
import com.example.ramona.ydbrothers.Models.VerticalSpacingDecoration;
import com.example.ramona.ydbrothers.R;
import com.example.ramona.ydbrothers.SQLite.MyDatabaseHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by aideo on 3/22/2017.
 */

public class ShowListUser extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<UserEntities> listUser = new ArrayList<>();
    private MyDatabaseHelper db;
    private AdaperRVListUser adaperRVListUser;
    private FloatingActionButton fabAddUser;
    private SearchView sv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("List Full User");
        setContentView(R.layout.lt_list_user);
        db = new MyDatabaseHelper(ShowListUser.this);
        adaperRVListUser = new AdaperRVListUser(listUser, ShowListUser.this);
        db.createDefaultData();
        Log.e("So User: ", db.getUserCount() + "");
        Log.e("So Debt: ", db.getDebtCount() + "");
        initControl();
        recyclerView.setAdapter(adaperRVListUser);
        initEvent();
    }


    private void initEvent() {

        fabAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowListUser.this, AddUser.class);
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
                if (dy > 0 && fabAddUser.getVisibility() == View.VISIBLE) {
                    fabAddUser.hide();
                } else if (dy < 0 && fabAddUser.getVisibility() != View.VISIBLE) {
                    fabAddUser.show();
                }
            }
        });
        adaperRVListUser.setOnItemClickListener(new AdaperRVListUser.clickListener() {
            @Override
            public void OnItemClickListener(int position) {
                Intent intent = new Intent(ShowListUser.this, ShowListDebtor.class);
                intent.putExtra(Constant.CREDITOR_ID, listUser.get(position).getsUserID().toString());
                Log.e("IHIHI", listUser.get(position).getsUserID().toString());
                startActivity(intent);
            }

            @Override
            public void OnLongItemClickListener(int position) {

            }

            @Override
            public void onDetailClick(String s) {
                Intent intent = new Intent(ShowListUser.this, InfoUser.class);
                intent.putExtra(Constant.INFOR_USER, s);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(String s, int position) {
                final String id = s;
                final int pos = position;
                final String path = db.getUserById(s).getsUserImage();
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowListUser.this);
                builder.setTitle("Delete User");
                builder.setMessage("Are you sure you want to delete this user ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        File fDelete = new File(path);
                        if (fDelete.exists()) {
                            if (fDelete.delete()) {
                                db.DeleteUser(id);
                                listUser.remove(pos);
                                db.DeleteAllDebtByIDDebtor(id);
                                Log.e("CheckRow", db.getDebtCount() + "");
                                adaperRVListUser.notifyDataSetChanged();
                                Toast.makeText(ShowListUser.this, "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (db.DeleteUser(id)) {
                                listUser.remove(pos);
                                db.DeleteAllDebtByIDDebtor(id);
                                Log.e("CheckRow", db.getDebtCount() + "");
                                adaperRVListUser.notifyDataSetChanged();
                                Toast.makeText(ShowListUser.this, "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }
                        db.close();
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
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search_view);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adaperRVListUser.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText.toLowerCase(Locale.getDefault());
                adaperRVListUser.getFilter().filter(newText);

                return true;
            }

        });
        return super.onCreateOptionsMenu(menu);
    }

    private void initControl() {

        recyclerView = (RecyclerView) findViewById(R.id.RVListUser);
        fabAddUser = (FloatingActionButton) findViewById(R.id.fabAddUser);
        fabAddUser.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#292929")));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new VerticalSpacingDecoration(40));
    }

    private void initData() {
        new GetAllUser().execute();
    }

    public class GetAllUser extends AsyncTask<Void, Void, List<UserEntities>> {

        @Override
        protected List<UserEntities> doInBackground(Void... params) {
            listUser.clear();
            listUser.addAll(db.getAllUser());
            db.close();
            return listUser;
        }

        @Override
        protected void onPostExecute(List<UserEntities> userEntities) {
            super.onPostExecute(userEntities);
            adaperRVListUser.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean(Constant.FLAG_CHECK_DB, false)) {
            initData();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Constant.FLAG_CHECK_DB, false);
            editor.commit();
        }
    }
}
