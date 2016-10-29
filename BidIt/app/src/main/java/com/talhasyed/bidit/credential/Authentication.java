package com.talhasyed.bidit.credential;

import android.content.Context;
import android.content.SharedPreferences;

import com.talhasyed.bidit.model.UserModel;

/**
 * Created by Talha Syed on 29-10-2016.
 */

public class Authentication {
    private static final String PREFS_NAME = "login_info";
    private static final String KEY_LOGGED_IN_USER_ID = "key_logged_in_user_id";


    private static Long getLoggedInUserId(Context context) {
        final long userId = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getLong(KEY_LOGGED_IN_USER_ID, -1);
        return userId == -1 ? null : userId;
    }

    public static boolean setLoggedInUserId(Long userId, Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(KEY_LOGGED_IN_USER_ID, userId);
        return editor.commit();
    }

    public static boolean setLoggedInUser(UserModel user, Context context) {
        return setLoggedInUserId(user.get_id(), context);
    }
}
