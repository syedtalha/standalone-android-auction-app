package com.talhasyed.bidit.storage;


import android.net.Uri;

import com.talhasyed.bidit.storage.ListingProviderContract.ListingProv;

public class ListingProvider extends BaseContentProvider {


    @Override
    protected String getTableName() {
        return ListingProv.TABLE_NAME;
    }

    @Override
    protected Uri getBaseContentURI() {
        return ListingProviderContract.BASE_CONTENT_URI;
    }
}
