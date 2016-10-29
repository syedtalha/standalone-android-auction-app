package com.talhasyed.bidit.storage;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Talha.Syed on 10/29/2016.
 */

public class UserProviderContract {

    /**
     * Content provider authority.
     */
    public static final String CONTENT_AUTHORITY = "com.talhasyed.bidit.provider.UserProvider";
    /**
     * Base URI.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    /**
     * Path component for "entry"-type resources..
     */
    private static final String PATH_USERS = "Users";

    private UserProviderContract() {
    }

    public static abstract class UserProv implements BaseColumns {

        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.basicsyncadapter.users";
        ///////////
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.basicsyncadapter.user";
        /**
         * Fully qualified URI for "entry" resources.
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USERS).build();
        public static final String TABLE_NAME = "Users";
        ////////////


        public static final String NAME     = "name";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";




        /**
         * generate a Fully qualified URI for a particular "entry", given the _id.
         */
        public static Uri makeURI(Long id) {
            return BASE_CONTENT_URI.buildUpon()
                    .appendPath(PATH_USERS)
                    .appendPath(String.valueOf(id)).build();
        }
    }
}




