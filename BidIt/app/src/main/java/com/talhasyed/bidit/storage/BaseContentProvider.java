package com.talhasyed.bidit.storage;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Talha Syed on 29-10-2016.
 */

public abstract class BaseContentProvider extends ContentProvider {
    public static final String COLUMN_NAME_NULLABLE = "whyIsThisIsNull";

    private BidItLocalDatabaseOpenHelper mOpenHelper;
    private SQLiteDatabase db;
    private ContentResolver contentResolver;

    protected abstract String getTableName();

    protected abstract Uri getBaseContentURI();

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        db = mOpenHelper.getWritableDatabase();
        int rowsDeleted = db.delete(getTableName(), selection, selectionArgs);
        //db.close();
        contentResolver.notifyChange(uri, null, false);
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db = mOpenHelper.getWritableDatabase();
        long newId = db.insert(getTableName(),
                COLUMN_NAME_NULLABLE,
                values);
        Uri returnUri = ContentUris.withAppendedId(uri, newId);
        contentResolver.notifyChange(uri, null, false);
        //db.close();
        return returnUri;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new BidItLocalDatabaseOpenHelper(
                getContext(),        // the application context
                BidItLocalDatabaseOpenHelper.DATABASE_NAME,              // the name of the database)
                null,                // uses the default SQLite cursor
                BidItLocalDatabaseOpenHelper.DATABASE_VERSION                    // the version number
        );
        contentResolver = getContext().getContentResolver();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor c = db.query(getTableName(), projection, selection, selectionArgs, null, null, sortOrder);
        //db.close();
        c.setNotificationUri(contentResolver, getBaseContentURI());
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        db = mOpenHelper.getWritableDatabase();
        int rowsAffected = db.update(getTableName(), values, selection, selectionArgs);
        //db.close();
        contentResolver.notifyChange(uri, null, false);
        return rowsAffected;
    }

}
