package com.talhasyed.bidit.storage;

import android.content.ContentResolver;

/**
 * Created by Talha Syed on 29-10-2016.
 */

public abstract class BaseCRUD {
    protected ContentResolver contentResolver;

    public BaseCRUD(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }


}
