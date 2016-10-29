package com.talhasyed.bidit.storage;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Talha.Syed on 10/29/2016.
 */

public class ListingProviderContract {

    /**
     * Content provider authority.
     */
    public static final String CONTENT_AUTHORITY = "com.talhasyed.bidit.provider.ListingProvider";
    /**
     * Base URI.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    /**
     * Path component for "entry"-type resources..
     */
    private static final String PATH_LISTINGS = "Listings";

    private ListingProviderContract() {
    }

    public static abstract class ListingProv implements BaseColumns {

        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.basicsyncadapter.listings";
        ///////////
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.basicsyncadapter.listing";
        /**
         * Fully qualified URI for "entry" resources.
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LISTINGS).build();
        public static final String TABLE_NAME = "Listings";
        ////////////


        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String START_DATE = "start_date";
        public static final String CLOSING_DATE = "closing_date";
        public static final String WINNING_BID_ID = "winning_bid_id";


        public static final String COLUMN_NAME_NULLABLE = "whyIsThisIsNull";


        /**
         * generate a Fully qualified URI for a particular "entry", given the _id.
         */
        public static Uri makeURI(Long id) {
            return BASE_CONTENT_URI.buildUpon()
                    .appendPath(PATH_LISTINGS)
                    .appendPath(String.valueOf(id)).build();
        }
    }
}




