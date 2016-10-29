package com.talhasyed.bidit.storage;


import android.net.Uri;

import com.talhasyed.bidit.storage.UserProviderContract.UserProv;

public class UserProvider extends BaseContentProvider {


    @Override
    protected String getTableName() {
        return UserProv.TABLE_NAME;
    }

    @Override
    protected Uri getBaseContentURI() {
        return UserProviderContract.BASE_CONTENT_URI;
    }
}
