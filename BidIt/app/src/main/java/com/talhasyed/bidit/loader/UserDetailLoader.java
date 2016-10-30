package com.talhasyed.bidit.loader;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.support.v4.content.AsyncTaskLoader;

import com.talhasyed.bidit.model.UserModel;
import com.talhasyed.bidit.storage.UserCRUD;
import com.talhasyed.bidit.storage.UserProviderContract;

/**
 * Created by Talha Syed on 30-10-2016.
 */

public class UserDetailLoader extends AsyncTaskLoader<UserModel> {
    private UserCRUD userCRUD;
    private Long userId;
    private UserDBObserver observer;

    public UserDetailLoader(Context context, Long userId) {
        super(context);
        this.userId = userId;
        userCRUD = new UserCRUD(context.getContentResolver());
    }

    @Override
    public UserModel loadInBackground() {
        return userCRUD.get(userId);
    }


    @Override
    protected void onReset() {
        super.onReset();
        // The Loader is being reset, so we should stop monitoring for changes.
        if (observer != null) {
            getContext().getContentResolver().unregisterContentObserver(observer);
            observer = null;
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        // Begin monitoring the underlying data source.
        if (observer == null) {
            observer = new UserDBObserver(null);
            getContext().getContentResolver().registerContentObserver(UserProviderContract.BASE_CONTENT_URI, true, observer);
        }
    }


    private class UserDBObserver extends ContentObserver {
        public UserDBObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            //super.onChange(selfChange);
            onContentChanged();
        }


    }
}
