package com.talhasyed.bidit.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Talha.Syed on 10/29/2016.
 */

public class BidItLocalDatabaseOpenHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Auction.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String BOOLEAN_TYPE = " BOOLEAN";
    private static final String REAL_TYPE = " REAL";
    private static final String DOUBLE_TYPE = " DOUBLE";
    private static final String COMMA = ",";
    private static final String UNIQUE = " UNIQUE ";
    private static final String NOT_NULL = " NOT NULL ";
    private static final String ON_CONFLICT_REPLACE = " ON CONFLICT REPLACE ";
    private static final String ON_CONFLICT_IGNORE = " ON CONFLICT IGNORE ";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    private static final String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS ";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String FOREIGN_KEY = " FOREIGN KEY ";
    private static final String REFERENCES = " REFERENCES ";
    private static final String ON_DELETE = "ON DELETE ";
    private static final String ON_UPDATE = "ON UPDATE ";
    private static final String CASCADE = " CASCADE ";

    public BidItLocalDatabaseOpenHelper(Context context, String name,
                                        SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onConfigure(SQLiteDatabase db) {
        //super.onConfigure(db);//FIXME enable super
        db.enableWriteAheadLogging();

    }


    public void onCreate(SQLiteDatabase db) {
//         db.execSQL(SQL_CREATE_);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        truncateAllTables(db);

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);

    }

    public void truncateAllTables(SQLiteDatabase db) {
//        db.execSQL(SQL_DELETE_);
        onCreate(db);
    }
}
