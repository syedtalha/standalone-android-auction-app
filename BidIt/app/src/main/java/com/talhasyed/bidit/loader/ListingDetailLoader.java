package com.talhasyed.bidit.loader;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.support.v4.content.AsyncTaskLoader;

import com.talhasyed.bidit.model.ListingModel;
import com.talhasyed.bidit.storage.ListingCRUD;
import com.talhasyed.bidit.storage.ListingProviderContract;

/**
 * Created by Talha Syed on 30-10-2016.
 */

public class ListingDetailLoader extends AsyncTaskLoader<ListingModel> {
    private ListingCRUD listingCRUD;
    private Long listingId;
    private ListingDBObserver observer;

    public ListingDetailLoader(Context context, Long listingId) {
        super(context);
        this.listingId = listingId;
        listingCRUD = new ListingCRUD(context.getContentResolver());
    }

    @Override
    public ListingModel loadInBackground() {
        return listingCRUD.get(listingId);
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
            observer = new ListingDBObserver(null);
            getContext().getContentResolver().registerContentObserver(ListingProviderContract.BASE_CONTENT_URI, true, observer);
        }
    }


    private class ListingDBObserver extends ContentObserver {
        public ListingDBObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            //super.onChange(selfChange);
            onContentChanged();
        }


    }
}
