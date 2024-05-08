package com.sgp.spendsync.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Databasehelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "budget";
    public static final String TABLE_INCOME = "income";
    public static final String TABLE_EXPENSE = "expense";
    public static final String TABLE_TRANSACTION = "trans";

    SQLiteDatabase db;
    Context context;
    public Databasehelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String trans = "CREATE TABLE " + TABLE_TRANSACTION + " (id integer PRIMARY KEY autoincrement,amount text, note text, transaction_type text,category_type text, datentime text, image_pos integer,month text)";
        String categoryincome = "CREATE TABLE " + TABLE_INCOME + "(id integer PRIMARY KEY autoincrement,categoryname text,image_pos integer)";
        String categoryexpense = "CREATE TABLE " + TABLE_EXPENSE + "(id integer PRIMARY KEY autoincrement,categoryname text,image_pos integer)";

        db.execSQL(trans);
        db.execSQL(categoryincome);
        db.execSQL(categoryexpense);
    }

//    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String trans = "DROP TABLE IF EXISTS " + TABLE_TRANSACTION;
        String categoryincome = "DROP TABLE IF EXISTS " + TABLE_INCOME;
        String categoryexpense = "DROP TABLE IF EXISTS " + TABLE_EXPENSE;

        db.execSQL(trans);
        db.execSQL(categoryincome);
        db.execSQL(categoryexpense);
        onCreate(db);
    }
//
    public Cursor gettransactiondata() {
        db = this.getReadableDatabase();
        String result = " select * from " + TABLE_TRANSACTION;
        Cursor cursor = db.rawQuery(result, null);
        return cursor;
    }

    public Cursor getCategoryIncomeData() {
        db = this.getReadableDatabase();
        String result = " select * from " + TABLE_INCOME;

        Log.e("resultIncome--)",""+result.toString());
        Cursor cursor = db.rawQuery(result, null);
        return cursor;
    }

    public Cursor getCategoryExpenseData() {
        db = this.getReadableDatabase();
        String result = " select * from " + TABLE_EXPENSE;
        Cursor cursor = db.rawQuery(result, null);
        return cursor;
    }

    public boolean insertIncomeData(String amount, String note, String transaction_type, String category_type, int image_pos) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        SimpleDateFormat simpleFormatter = new SimpleDateFormat("hh:mm:ss\n dd/MM/yyyy");
        Date now = new Date();
        String datentime = simpleFormatter.format(now).toString();

        SimpleDateFormat simpleFormatter1 = new SimpleDateFormat("MM");
        Date now1 = new Date();
        String monthName = simpleFormatter1.format(now1).toString();


        Log.e("Mothaname--)",""+monthName);
        cv.put("amount", amount);
        cv.put("note", note);
        cv.put("transaction_type", transaction_type);
        cv.put("category_type", category_type);
        cv.put("datentime", datentime);
        cv.put("image_pos", image_pos);
        cv.put("month", monthName);

        long result = db.insert(TABLE_TRANSACTION, null, cv);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean categoryIncomeData(String categoryname, int image_pos) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("categoryname", categoryname);
        cv.put("image_pos", image_pos);
        long result = db.insert(TABLE_INCOME, null, cv);
        db.close();

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean categoryExpenseData(String categoryname, int image_pos) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("categoryname", categoryname);
        cv.put("image_pos", image_pos);
        long result = db.insert(TABLE_EXPENSE, null, cv);
        db.close();

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public int getDataCategoryIncomeCount() {
        db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + TABLE_INCOME;
        Cursor cursor = db.rawQuery(count, null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public int getDataCategoryExpenseCount() {
        db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + TABLE_EXPENSE;
        Cursor cursor = db.rawQuery(count, null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public Cursor getDataIncome() {
        db = this.getReadableDatabase();
        String query = "select * from " + TABLE_TRANSACTION;
        Cursor cursor = db.rawQuery("select amount from trans where transaction_type=?", new String[]{"INCOME"});
        return cursor;
    }

    public Cursor getTotalIncome(int month) {

        String  ms = String.valueOf(month);
        if (month < 10) {
            ms = "0" + (month);
        }

        db = this.getReadableDatabase();
        String query = "select * from " + TABLE_TRANSACTION;
        Cursor cursor = db.rawQuery("select amount from trans where transaction_type=?AND month=?", new String[]{"INCOME",ms} );
        return cursor;
    }

    public Cursor getTotalExpense(int month) {

        String  ms = String.valueOf(month);
        if (month < 10) {
            ms = "0" + (month);
        }

        db = this.getReadableDatabase();
        String query = "select * from " + TABLE_TRANSACTION;
        Cursor cursor = db.rawQuery("select amount from trans where transaction_type=?AND month=?", new String[]{"EXPENSE",ms} );
        return cursor;
    }

    public Cursor getDataExpense() {
        db = this.getReadableDatabase();
        String query = "select * from " + TABLE_TRANSACTION;
        Cursor cursor = db.rawQuery("select amount from trans where transaction_type=?", new String[]{"EXPENSE"});
        return cursor;
    }

    public int deleteTransactionData(int id) {
        db = this.getWritableDatabase();
        return db.delete(TABLE_TRANSACTION, "id=" + id, null);
    }

    public int deleteincomecategory(int id) {
        db = this.getWritableDatabase();
        return db.delete(TABLE_INCOME, "id=" + id, null);
    }

    public int deleteexpensecategory(int id) {
        db = this.getWritableDatabase();
        return db.delete(TABLE_EXPENSE, "id=" + id, null);
    }

    public Cursor getMonthIncome(int mnth) {
        db = this.getReadableDatabase();

        String  ms = String.valueOf(mnth);
        if (mnth < 10) {
            ms = "0" + (mnth);
        }

        Cursor cursor = db.rawQuery("select * from trans where transaction_type=? AND month=?", new String[]{"INCOME",ms} );
        return cursor;
    }
    public Cursor getMonthExpense(int mnth) {
        db = this.getReadableDatabase();

        String  ms = String.valueOf(mnth);
        if (mnth < 10) {
            ms = "0" + (mnth);
        }

        Cursor cursor = db.rawQuery("select * from trans where transaction_type=? AND month=?", new String[]{"EXPENSE",ms} );
        return cursor;
    }

    public boolean updateTransactionData(int id, String amount, String note) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss\ndd MMM, yyyy");
        Date now = new Date();
        String date = simpleDateFormat.format(now).toString();

        cv.put("id", id);
        cv.put("amount", amount);
        cv.put("note", note);
        cv.put("datentime", date);
        cv.put("month", date);

        long result = db.update(TABLE_TRANSACTION, cv, "id=" + id, null);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor dataExport(String from1,String to1) {

        db = this.getReadableDatabase();
        String query = "select * from trans where substr(datentime,17)||substr(datentime,14,2)||substr(datentime,11,2) between '"+from1+"' and '"+to1+"'";
        Log.e("Query",""+query);
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor getData() {
        db = this.getReadableDatabase();
        String query = "select * from " + TABLE_TRANSACTION;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }
}