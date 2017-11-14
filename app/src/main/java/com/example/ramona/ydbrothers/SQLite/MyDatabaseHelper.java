package com.example.ramona.ydbrothers.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ramona.ydbrothers.Entity.DebtEntities;
import com.example.ramona.ydbrothers.Entity.UserEntities;
import com.example.ramona.ydbrothers.Models.Models;
import com.example.ramona.ydbrothers.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ramona on 1/17/2017.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";

    // Phien ban DB
    private static final int DATABASE_VERSION = 1;
    // Ten CSDL
    private static final String DATABASE_NAME = "db_ydbrothers";
    Context context;
    // Ten bang
    private static final String TABLE_USER = "User";
    private static final String TABLE_DEBT = "Debt";
    // Cac truong du lieu
    private static final String COLUMN_USER_ID = "UserID";
    private static final String COLUMN_USER_NAME = "UserName";
    private static final String COLUMN_USER_IMAGE = "UserImage";
    private static final String COLUMN_USER_ADDRESS = "UserAddress";
    private static final String COLUMN_USER_PHONE = "UserPhone";

    private static final String COLUMN_DEBT_ID = "DebtID";
    private static final String COLUMN_CREDITOR_ID = "CreditorID";
    private static final String COLUMN_DEBTOR_ID = "DebtorID";
    private static final String COLUMN_DEBT = "Debt";


    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Tao cac bang
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.coCreate ....");
        // Script tao bang
        String script = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_NAME + " TEXT,"
                + COLUMN_USER_IMAGE + " TEXT,"
                + COLUMN_USER_ADDRESS + " TEXT,"
                + COLUMN_USER_PHONE + " TEXT" + ")";
        String script1 = "CREATE TABLE " + TABLE_DEBT + "("
                + COLUMN_DEBT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CREDITOR_ID + " TEXT,"
                + COLUMN_DEBTOR_ID + " TEXT,"
                + COLUMN_DEBT + " INT" + ")";
        // Chay lenh tao bang
        db.execSQL(script);
        db.execSQL(script1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean createDefaultData() {
        int countUser = this.getUserCount();
        int countDebt = this.getDebtCount();
        boolean check = false;
        if (!check) {
            if (countUser == 0 && countDebt == 0) {
                //Add Default User
                UserEntities entities = new UserEntities("Trần Nguyên Quân", "android.resource://com.example.ramona.ydbrothers/" + R.drawable.quan, "Bắc Giang", "00000");
                UserEntities entities1 = new UserEntities("Nguyễn Hoàng Minh", "android.resource://com.example.ramona.ydbrothers/" + R.drawable.minh, "Bắc Giang", "00000");
                UserEntities entities2 = new UserEntities("Nguyễn Hoàng Sơn Đông", "android.resource://com.example.ramona.ydbrothers/" + R.drawable.dong, "Bắc Giang", "00000");
                UserEntities entities3 = new UserEntities("Phương Đức Minh", "android.resource://com.example.ramona.ydbrothers/" + R.drawable.minh_sinh, "Bắc Giang", "00000");
                UserEntities entities4 = new UserEntities("Nguyễn Trọng Hiếu", "android.resource://com.example.ramona.ydbrothers/" + R.drawable.hieu, "Bắc Giang", "00000");
                addUser(entities);
                addUser(entities1);
                addUser(entities2);
                addUser(entities3);
                addUser(entities4);
                // Add Default Debt
                DebtEntities debtEntities = new DebtEntities("3", "1", 1400000);
                DebtEntities debtEntities2 = new DebtEntities("3", "2", 1400000);
                DebtEntities debtEntities1 = new DebtEntities("5", "1", 1170000);
                addDebt(debtEntities);
                addDebt(debtEntities1);
                addDebt(debtEntities2);
                Log.e("Đã vào default user: ", "in");
            }
            Models.NotifyDataSetChanged(context);
            Models.NotifyDebtChanged(context);
            check = true;
        }
        return check;
    }

    public int getUserCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        return count;
    }

    public boolean DeleteUser(String s) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_USER, COLUMN_USER_ID + " = " + s, null) > 0;
    }

    public boolean DeleteAllDebtByIDDebtor(String s) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_DEBT, COLUMN_DEBTOR_ID + " = " + s, null) > 0;
    }

    public boolean DeleteSingleDebt(String debtID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_DEBT, COLUMN_DEBT_ID + " = " + debtID, null) > 0;
    }

    public int getDebtCount() {
        String countQuery = "SELECT  * FROM " + TABLE_DEBT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        db.close();
        return count;
    }

    public boolean addUser(UserEntities user) {
        boolean success = false;
        int count = getUserCount();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getsUserName());
        values.put(COLUMN_USER_IMAGE, user.getsUserImage());
        values.put(COLUMN_USER_ADDRESS, user.getsAddress());
        values.put(COLUMN_USER_PHONE, user.getsPhone());
        // them 1 ban ghi vao bang
        db.insert(TABLE_USER, null, values);
        if (count < getUserCount()) {

            success = true;
        }
        db.close();
        return success;
    }


    public boolean addDebt(DebtEntities debt) {
        boolean success = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_DEBT_ID, debt.getsDebtID());
        values.put(COLUMN_CREDITOR_ID, debt.getsCreditorID());
        values.put(COLUMN_DEBTOR_ID, debt.getsDebtorID());
        values.put(COLUMN_DEBT, debt.getiDebt());
        // thêm vào db

        if (db.insert(TABLE_DEBT, null, values) > 0)
            success = true;
        db.close();
        return success;
    }

    public List<UserEntities> getAllUser() {
        List<UserEntities> lsUser = new ArrayList<>();
        String query = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                UserEntities user = new UserEntities();
                user.setsUserID(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)) + "");
                user.setsUserName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)) + "");
                user.setsUserImage(cursor.getString(cursor.getColumnIndex(COLUMN_USER_IMAGE)) + "");
                user.setsAddress(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ADDRESS)) + "");
                user.setsPhone(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PHONE)) + "");
                lsUser.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return lsUser;
    }

    public List<DebtEntities> getAllRowDebt() {
        List<DebtEntities> list = new ArrayList<>();
        String query = "SELECT  * FROM " + TABLE_DEBT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                DebtEntities entities = new DebtEntities();
                entities.setsCreditorID(cursor.getString(cursor.getColumnIndex(COLUMN_DEBT_ID)) + "");
                entities.setsCreditorID(cursor.getString(cursor.getColumnIndex(COLUMN_CREDITOR_ID)));
                entities.setsDebtID(cursor.getString(cursor.getColumnIndex(COLUMN_DEBTOR_ID)));
                entities.setiDebt(cursor.getInt(cursor.getColumnIndex(COLUMN_DEBT)));
                list.add(entities);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return list;
    }

    public List<DebtEntities> getListDebtor(String i) {
        List<DebtEntities> lsDebtor = new ArrayList<>();
        String query = "SELECT  * FROM " + TABLE_DEBT + " WHERE " + COLUMN_CREDITOR_ID + " = " + i;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                DebtEntities debtEntities = new DebtEntities();
                debtEntities.setsDebtID(cursor.getString(cursor.getColumnIndex(COLUMN_DEBT_ID)));
                debtEntities.setsCreditorID(cursor.getString(cursor.getColumnIndex(COLUMN_CREDITOR_ID)));
                debtEntities.setsDebtorID(cursor.getString(cursor.getColumnIndex(COLUMN_DEBTOR_ID)));
                debtEntities.setiDebt(cursor.getInt(cursor.getColumnIndex(COLUMN_DEBT)));
                lsDebtor.add(debtEntities);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return lsDebtor;
    }

    public UserEntities getUserById(String id) {
        UserEntities user = new UserEntities();
        String query = "SELECT  * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_ID + " = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (null != cursor && cursor.moveToFirst()) {
            Log.e("Ramona", "i'm in!");
            user.setsUserID(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)));
            user.setsUserName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
            user.setsUserImage(cursor.getString(cursor.getColumnIndex(COLUMN_USER_IMAGE)));
            user.setsAddress(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ADDRESS)));
            user.setsPhone(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PHONE)));
            cursor.close();
        }
        db.close();
        return user;
    }

    public DebtEntities getRowDebt(String i, String j) {
        String query = "SELECT  * FROM " + TABLE_DEBT + " WHERE " + COLUMN_CREDITOR_ID + " = " + i + " AND " + COLUMN_DEBTOR_ID + " = " + j;
        DebtEntities debtEntities = new DebtEntities();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                debtEntities.setsDebtID(cursor.getString(cursor.getColumnIndex(COLUMN_DEBT_ID)));
                debtEntities.setsCreditorID(cursor.getString(cursor.getColumnIndex(COLUMN_CREDITOR_ID)));
                debtEntities.setsDebtorID(cursor.getString(cursor.getColumnIndex(COLUMN_DEBTOR_ID)));
                debtEntities.setiDebt(cursor.getInt(cursor.getColumnIndex(COLUMN_DEBT)));
                cursor.close();
            }
        }
        db.close();
        return debtEntities;
    }

    public boolean updateDebt(String id, int i) {
        boolean check;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DEBT, i);
        int a = db.update(TABLE_DEBT, values, COLUMN_DEBT_ID + " = " + id, null);
        if (a == 0) {
            check = false;
        } else {
            check = true;
        }
        return check;
    }
    public boolean CheckDebtIsAlreadyInDBorNot (String i, String j){
        boolean check;
        String query = "SELECT  * FROM " + TABLE_DEBT + " WHERE " + COLUMN_CREDITOR_ID + " = " + i + " AND " + COLUMN_DEBTOR_ID + " = " + j;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() <= 0){
            check = false;
        } else {
            check = true;
        }
        return check;
    }
}
