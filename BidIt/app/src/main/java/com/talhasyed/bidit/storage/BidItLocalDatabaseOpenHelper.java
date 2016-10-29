package com.talhasyed.bidit.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.talhasyed.bidit.storage.ListingProviderContract.ListingProv;
import com.talhasyed.bidit.storage.UserProviderContract.UserProv;

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


    /**
     * User Create table start
     */
    public static final String SQL_CREATE_USERS =
            (CREATE_TABLE
                    +
                    UserProv.TABLE_NAME
                    + " ("
                    + UserProv._ID
                    + INTEGER_TYPE + PRIMARY_KEY + COMMA


                    + UserProv.USERNAME     + TEXT_TYPE + NOT_NULL + ON_CONFLICT_IGNORE + COMMA
                    + UserProv.PASSWORD     + TEXT_TYPE + COMMA
                    + UserProv.NAME         + TEXT_TYPE + COMMA


                    + UNIQUE + "(" +
                    UserProv.USERNAME +
                    ")" + ON_CONFLICT_REPLACE +
                    // Any other options for the CREATE command
                    " )");



    /**
     * Listing Create table start
     */
    public static final String SQL_CREATE_LISTINGS =
            (CREATE_TABLE
                    +
                    ListingProv.TABLE_NAME
                    + " ("
                    + ListingProv._ID
                    + INTEGER_TYPE + PRIMARY_KEY + COMMA


                    + ListingProv.NAME                + TEXT_TYPE + COMMA
                    + ListingProv.DESCRIPTION         + TEXT_TYPE + COMMA
                    + ListingProv.START_DATE          + TEXT_TYPE + COMMA
                    + ListingProv.CLOSING_DATE        + TEXT_TYPE + COMMA
                    + ListingProv.WINNING_BID_ID      + TEXT_TYPE + COMMA



                    + UNIQUE + "(" +
                    UserProv.USERNAME +
                    ")" + ON_CONFLICT_REPLACE +
                    // Any other options for the CREATE command
                    " )");


    private static final String SQL_DELETE_USERS =
            DROP_TABLE_IF_EXISTS + UserProv.TABLE_NAME;

    @Override
    public void onConfigure(SQLiteDatabase db) {
        //super.onConfigure(db);//FIXME enable super
        db.enableWriteAheadLogging();

    }


    public void onCreate(SQLiteDatabase db) {
         db.execSQL(SQL_CREATE_USERS);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        truncateAllTables(db);

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);

    }

    public void truncateAllTables(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_USERS);
        onCreate(db);
    }
}
