package com.talhasyed.bidit.storage;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.talhasyed.bidit.model.BidModel;
import com.talhasyed.bidit.storage.BidProviderContract.BidProv;

import org.joda.time.DateTime;

/**
 * Created by Talha Syed on 29-10-2016.
 */

public class BidCRUD extends BaseCRUD {

    public BidCRUD(ContentResolver contentResolver) {
        super(contentResolver);
    }


    private ContentValues intoContentValues(BidModel bidModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BidProv.USER_ID, bidModel.getUserId());
        contentValues.put(BidProv.LISTING_ID, bidModel.getListingId());
        contentValues.put(BidProv.AMOUNT, bidModel.getAmount());
        contentValues.put(BidProv.DATE, bidModel.getDate() != null ? bidModel.getDate().getMillis() : null);
        return contentValues;
    }


    private BidModel intoModel(Cursor c) {
        BidModel bidModel = new BidModel();
        bidModel.set_id(c.getLong(c.getColumnIndex(BidProv._ID)));
        bidModel.setUserId(c.getString(c.getColumnIndex(BidProv.USER_ID)));
        bidModel.setListingId(c.getString(c.getColumnIndex(BidProv.LISTING_ID)));
        bidModel.setAmount(c.isNull(c.getColumnIndex(BidProv.AMOUNT)) ? null : c.getDouble(c.getColumnIndex(BidProv.AMOUNT)));
        bidModel.setDate(c.isNull(c.getColumnIndex(BidProv.DATE)) ? null : new DateTime(Long.valueOf(c.getString(c.getColumnIndex(BidProv.DATE)))));
        return bidModel;
    }


    public BidModel get(Long bidId) {
        final Cursor cursor = contentResolver.query(BidProv.CONTENT_URI, null, BidProv._ID + " = ? ", new String[]{String.valueOf(bidId)}, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            final BidModel bidModel = intoModel(cursor);
            cursor.close();
            return bidModel;
        }
        return null;
    }

    public Long insert(@NonNull BidModel bid) throws SQLiteConstraintException {
        final Uri inserted = contentResolver.insert(BidProv.CONTENT_URI, intoContentValues(bid));
        if (inserted != null) {
            return Long.parseLong(inserted.getLastPathSegment());
        } else {
            return null;
        }
    }

    public Double getHighestBidFor(Long listinId) {
        final Cursor cursor = contentResolver.query(
                BidProv.CONTENT_URI,
                new String[]{" MAX(" + BidProv.AMOUNT + ") AS " + BidProv.AMOUNT},
                BidProv.LISTING_ID + " = ? ",
                new String[]{String.valueOf(listinId)},
                null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            final double maxVal = cursor.getDouble(cursor.getColumnIndex(BidProv.AMOUNT));
            cursor.close();
            if (maxVal == 0.0) {
                return null;
            }
            return maxVal;
        }
        return null;
    }

}