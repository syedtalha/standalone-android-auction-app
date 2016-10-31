package com.talhasyed.bidit.storage;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.talhasyed.bidit.model.ListingModel;
import com.talhasyed.bidit.storage.ListingProviderContract.ListingProv;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Talha Syed on 29-10-2016.
 */

public class ListingCRUD extends BaseCRUD {

    public ListingCRUD(ContentResolver contentResolver) {
        super(contentResolver);
    }


    private ContentValues intoContentValues(ListingModel listingModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ListingProv.NAME, listingModel.getName());
        contentValues.put(ListingProv.DESCRIPTION, listingModel.getDescription());
        contentValues.put(ListingProv.CURRENT_BID_ID, listingModel.getCurrentBidId());
        contentValues.put(ListingProv.START_DATE, listingModel.getStartDate() != null ? listingModel.getStartDate().getMillis() : null);
        contentValues.put(ListingProv.CLOSING_DATE, listingModel.getClosingDate() != null ? listingModel.getClosingDate().getMillis() : null);
        return contentValues;
    }


    private ListingModel intoModel(Cursor c) {
        ListingModel listingModel = new ListingModel();
        listingModel.set_id(c.getLong(c.getColumnIndex(ListingProv._ID)));
        listingModel.setName(c.getString(c.getColumnIndex(ListingProv.NAME)));
        listingModel.setDescription(c.getString(c.getColumnIndex(ListingProv.DESCRIPTION)));
        listingModel.setCurrentBidId(c.getString(c.getColumnIndex(ListingProv.CURRENT_BID_ID)));
        listingModel.setStartDate(c.isNull(c.getColumnIndex(ListingProv.START_DATE)) ? null : new DateTime(Long.valueOf(c.getString(c.getColumnIndex(ListingProv.START_DATE)))));
        listingModel.setClosingDate(c.isNull(c.getColumnIndex(ListingProv.CLOSING_DATE)) ? null : new DateTime(Long.valueOf(c.getString(c.getColumnIndex(ListingProv.CLOSING_DATE)))));
        return listingModel;
    }


    public ListingModel get(Long listingId) {
        final Cursor cursor = contentResolver.query(ListingProv.CONTENT_URI, null, ListingProv._ID + " = ? ", new String[]{String.valueOf(listingId)}, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            final ListingModel listingModel = intoModel(cursor);
            cursor.close();
            return listingModel;
        }
        return null;
    }

    public Long insert(@NonNull ListingModel listing) throws SQLiteConstraintException {
        final Uri inserted = contentResolver.insert(ListingProv.CONTENT_URI, intoContentValues(listing));
        if (inserted != null) {
            return Long.parseLong(inserted.getLastPathSegment());
        } else {
            return null;
        }
    }


    public boolean postCurrentBid(Long listing_id, Long bid_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ListingProv.CURRENT_BID_ID, bid_id);
        final int updated = contentResolver.update(ListingProv.CONTENT_URI, contentValues, ListingProv._ID + " = ? ", new String[]{String.valueOf(listing_id)});
        if (updated < 1) {
            return false;
        } else {
            return true;
        }
    }


    private ArrayList<ListingModel> arrayFromCursor(Cursor c) {
        ArrayList<ListingModel> listingModels = new ArrayList<>();
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                listingModels.add(intoModel(c));
                if (!c.moveToNext()) {
                    break;
                }
            }
            c.close();
        }
        return listingModels;
    }

    public List<ListingModel> getAllActiveListings() {
        final Cursor cursor = contentResolver.query(ListingProv.CONTENT_URI, null, ListingProv.CLOSING_DATE + " > ? ", new String[]{String.valueOf(new DateTime().getMillis())}, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            final List<ListingModel> listingModel = arrayFromCursor(cursor);
            cursor.close();
            return listingModel;
        } else {
            return new ArrayList<ListingModel>();
        }
    }
}