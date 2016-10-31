package com.talhasyed.bidit.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.talhasyed.bidit.storage.BidProviderContract.BidProv;
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
    private static final String ON_CONFLICT_ABORT = " ON CONFLICT ABORT ";
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


                    + UserProv.USERNAME     + TEXT_TYPE + NOT_NULL + ON_CONFLICT_ABORT + COMMA
                    + UserProv.PASSWORD     + TEXT_TYPE + NOT_NULL + ON_CONFLICT_ABORT + COMMA
                    + UserProv.NAME         + TEXT_TYPE + NOT_NULL + ON_CONFLICT_ABORT + COMMA


                    + UNIQUE + "(" +
                    UserProv.USERNAME +
                    ")" + ON_CONFLICT_ABORT +
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


                    + ListingProv.NAME                + TEXT_TYPE + NOT_NULL + ON_CONFLICT_ABORT + COMMA
                    + ListingProv.DESCRIPTION         + TEXT_TYPE + COMMA
                    + ListingProv.START_DATE          + TEXT_TYPE + NOT_NULL + ON_CONFLICT_ABORT + COMMA
                    + ListingProv.CLOSING_DATE        + TEXT_TYPE + COMMA
                    + ListingProv.CURRENT_BID_ID + TEXT_TYPE + COMMA


                    + FOREIGN_KEY+
                    "("+ListingProv.CURRENT_BID_ID +")"
                    + REFERENCES +
                    BidProv.TABLE_NAME+"("+BidProv._ID+")"
                    +ON_DELETE+ CASCADE +
                    ON_UPDATE + CASCADE


                    + UNIQUE + "(" +
                    ListingProv.CURRENT_BID_ID +
                    ")" + ON_CONFLICT_ABORT +
                    // Any other options for the CREATE command
                    " )");


    /**
     * Listing Create table start
     */
    public static final String SQL_CREATE_BIDS =
            (CREATE_TABLE
                    +
                    BidProv.TABLE_NAME
                    + " ("
                    + BidProv._ID
                    + INTEGER_TYPE + PRIMARY_KEY + COMMA


                    + BidProv.USER_ID               + TEXT_TYPE + NOT_NULL + ON_CONFLICT_ABORT + COMMA
                    + BidProv.LISTING_ID            + TEXT_TYPE + NOT_NULL + ON_CONFLICT_ABORT + COMMA
                    + BidProv.AMOUNT                + REAL_TYPE + NOT_NULL + ON_CONFLICT_ABORT + COMMA
                    + BidProv.DATE                  + TEXT_TYPE + NOT_NULL + ON_CONFLICT_ABORT + COMMA

                    + FOREIGN_KEY+
                    "("+BidProv.USER_ID +")"
                    + REFERENCES +
                    UserProv.TABLE_NAME+"("+UserProv._ID+")"
                    +ON_DELETE+ CASCADE +
                    ON_UPDATE + CASCADE

                    + FOREIGN_KEY+
                    "("+BidProv.LISTING_ID +")"
                    + REFERENCES +
                    ListingProv.TABLE_NAME+"("+ListingProv._ID+")"
                    +ON_DELETE+ CASCADE +
                    ON_UPDATE + CASCADE


                    // Any other options for the CREATE command
                    +" )");


    private static final String SQL_DELETE_USERS =
            DROP_TABLE_IF_EXISTS + UserProv.TABLE_NAME;
    private static final String SQL_DELETE_LISTINGS =
            DROP_TABLE_IF_EXISTS + ListingProv.TABLE_NAME;
    private static final String SQL_DELETE_BIDS =
            DROP_TABLE_IF_EXISTS + BidProv.TABLE_NAME;

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.enableWriteAheadLogging();

    }


    public void onCreate(SQLiteDatabase db) {
         db.execSQL(SQL_CREATE_USERS);
         db.execSQL(SQL_CREATE_LISTINGS);
         db.execSQL(SQL_CREATE_BIDS);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        truncateAllTables(db);

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);

    }

    public void truncateAllTables(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_USERS);
        db.execSQL(SQL_DELETE_LISTINGS);
        db.execSQL(SQL_DELETE_BIDS);
        onCreate(db);
    }
}
