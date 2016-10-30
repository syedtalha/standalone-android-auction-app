package com.talhasyed.bidit.storage;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Talha.Syed on 10/29/2016.
 */

public class BidProviderContract {

    /**
     * Content provider authority.
     */
    public static final String CONTENT_AUTHORITY = "com.talhasyed.bidit.provider.BidProvider";
    /**
     * Base URI.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    /**
     * Path component for "entry"-type resources..
     */
    private static final String PATH_BIDS = "Bids";
    private static final String PATH_BIDS_LIST = "BidsList";

    private BidProviderContract() {
    }

    public static abstract class BidProv implements BaseColumns {

        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.basicsyncadapter.bids";
        ///////////
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.basicsyncadapter.bid";
        /**
         * Fully qualified URI for "entry" resources.
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BIDS).build();
        public static final Uri LIST_CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BIDS_LIST).build();
        public static final String TABLE_NAME = "Bids";
        ////////////


        public static final String USER_ID          = "user_id";
        public static final String LISTING_ID       = "listing_id";
        public static final String AMOUNT           = "amount";
        public static final String DATE             = "date";




        /**
         * generate a Fully qualified URI for a particular "entry", given the _id.
         */
        public static Uri makeURI(Long id) {
            return BASE_CONTENT_URI.buildUpon()
                    .appendPath(PATH_BIDS)
                    .appendPath(String.valueOf(id)).build();
        }
    }
}




