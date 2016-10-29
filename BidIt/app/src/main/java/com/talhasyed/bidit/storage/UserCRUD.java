package com.talhasyed.bidit.storage;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.talhasyed.bidit.credential.LocalAuthException;
import com.talhasyed.bidit.model.UserModel;
import com.talhasyed.bidit.storage.UserProviderContract.UserProv;

/**
 * Created by Talha Syed on 29-10-2016.
 */

public class UserCRUD extends BaseCRUD {

    public UserCRUD(ContentResolver contentResolver) {
        super(contentResolver);
    }

    public UserModel validateLogin(@NonNull String userName, @NonNull String password) throws LocalAuthException {
        if (userName == null) {
            throw new LocalAuthException("Username null", LocalAuthException.AuthErrorType.UserName);
        }
        if (userName == "") {
            throw new LocalAuthException("Username empty", LocalAuthException.AuthErrorType.UserName);
        }
        final Cursor queryCursor = contentResolver.query(
                UserProv.CONTENT_URI,
                null,
                UserProv.USERNAME + " = '?' ",
                new String[]{userName},
                null);
        if (queryCursor != null && queryCursor.getCount() > 1) {
            if (queryCursor.moveToFirst()) {
                final String actualPassword = queryCursor.getString(queryCursor.getColumnIndex(UserProv.PASSWORD));
                if (actualPassword != null) {
                    if (actualPassword.equals(password)) {
                        return intoModel(queryCursor);
                    } else {
                        throw new LocalAuthException("Wrong Password", LocalAuthException.AuthErrorType.Password);

                    }
                } else {
                    throw new LocalAuthException("Password Null in DB", LocalAuthException.AuthErrorType.General);
                }
            } else {
                throw new LocalAuthException("DB error", LocalAuthException.AuthErrorType.General);
            }

        } else {
            throw new LocalAuthException("Username not found", LocalAuthException.AuthErrorType.UserName);
        }

    }


    private UserModel intoModel(Cursor c) {
        UserModel userModel = new UserModel();
        userModel.set_id(c.getLong(c.getColumnIndex(UserProv._ID)));
        userModel.setName(c.getString(c.getColumnIndex(UserProv.NAME)));
        userModel.setUserName(c.getString(c.getColumnIndex(UserProv.USERNAME)));
        userModel.setPassword(c.getString(c.getColumnIndex(UserProv.PASSWORD)));
        return userModel;
    }
}