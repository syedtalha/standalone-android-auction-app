package com.talhasyed.bidit.storage;


import android.net.Uri;

import com.talhasyed.bidit.storage.BidProviderContract.BidProv;

public class BidProvider extends BaseContentProvider {


    @Override
    protected String getTableName() {
        return BidProv.TABLE_NAME;
    }

    @Override
    protected Uri getBaseContentURI() {
        return BidProviderContract.BASE_CONTENT_URI;
    }
}
