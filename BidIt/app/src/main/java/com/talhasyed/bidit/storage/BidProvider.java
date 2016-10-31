package com.talhasyed.bidit.storage;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.talhasyed.bidit.storage.BidProviderContract.BidProv;
import com.talhasyed.bidit.storage.UserProviderContract.UserProv;

public class BidProvider extends BaseContentProvider {


    @Override
    protected String getTableName() {
        return BidProv.TABLE_NAME;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final int deleted = super.delete(uri, selection, selectionArgs);
        contentResolver.notifyChange(ListingProviderContract.BASE_CONTENT_URI, null, false);
        return deleted;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (uri.getPath().equals(BidProv.LIST_CONTENT_URI.getPath()))   {
            SQLiteDatabase db = mOpenHelper.getReadableDatabase();
            Cursor c = db.query(BidProv.TABLE_NAME+" INNER JOIN "+ UserProv.TABLE_NAME+" ON "+ BidProv.TABLE_NAME+".["+ BidProv.USER_ID+"] = "+UserProv.TABLE_NAME+"."+UserProv._ID, new String[]{BidProv.TABLE_NAME+".*",UserProv.NAME},selection,selectionArgs,null,null,sortOrder);
            c.setNotificationUri(contentResolver, getBaseContentURI());
            return c;
        }   else    {
            return super.query(uri, projection, selection, selectionArgs, sortOrder);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final Uri inserted = super.insert(uri, values);
        contentResolver.notifyChange(ListingProviderContract.BASE_CONTENT_URI, null, false);
        return inserted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int updateed = super.update(uri, values, selection, selectionArgs);
        contentResolver.notifyChange(ListingProviderContract.BASE_CONTENT_URI, null, false);
        return updateed;
    }

    @Override
    protected Uri getBaseContentURI() {
        return BidProviderContract.BASE_CONTENT_URI;
    }
}
