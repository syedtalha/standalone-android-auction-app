package com.talhasyed.bidit.storage;


import android.content.ContentValues;
import android.net.Uri;

import com.talhasyed.bidit.storage.BidProviderContract.BidProv;

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
