package com.talhasyed.bidit.storage;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
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
                UserProv.USERNAME + " = ? ",
                new String[]{userName},
                null);
        if (queryCursor != null && queryCursor.getCount() > 0) {
            if (queryCursor.moveToFirst()) {
                final String actualPassword = queryCursor.getString(queryCursor.getColumnIndex(UserProv.PASSWORD));
                if (actualPassword != null) {
                    if (actualPassword.equals(password)) {
                        final UserModel userModel = intoModel(queryCursor);
                        queryCursor.close();
                        return userModel;
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

    public boolean checkUser(long id) {
        final Cursor query = contentResolver.query(
                UserProv.CONTENT_URI,
                new String[]{UserProv._ID},
                UserProv._ID + " = ? ",
                new String[]{String.valueOf(id)},
                null);
        return (query != null && query.getCount() > 0);
    }

    public UserModel signUp(@NonNull String userName, @NonNull String password, @NonNull String name) throws LocalAuthException {
        final Cursor query = contentResolver.query(
                UserProv.CONTENT_URI,
                null,
                UserProv.USERNAME + " = ? ",
                new String[]{userName},
                null);
        if (query != null && query.getCount() > 0) {
            throw new LocalAuthException("Username already taken", LocalAuthException.AuthErrorType.UserName);
        }
        query.close();

        final UserModel userModel = new UserModel.Builder()
                .withName(name)
                .withUserName(userName)
                .withPassword(password)
                .build();
        final Uri newUri = contentResolver.insert(
                UserProv.CONTENT_URI,
                intoContentValues(userModel));
        if (newUri != null) {
            userModel.set_id(Long.valueOf(newUri.getLastPathSegment()));
            return userModel;
        } else {
            throw new LocalAuthException("DB insertion exception", LocalAuthException.AuthErrorType.General);
        }


    }

    private ContentValues intoContentValues(UserModel userModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserProv.NAME, userModel.getName());
        contentValues.put(UserProv.PASSWORD, userModel.getPassword());
        contentValues.put(UserProv.USERNAME, userModel.getUserName());
        return contentValues;
    }


    private UserModel intoModel(Cursor c) {
        UserModel userModel = new UserModel();
        userModel.set_id(c.getLong(c.getColumnIndex(UserProv._ID)));
        userModel.setName(c.getString(c.getColumnIndex(UserProv.NAME)));
        userModel.setUserName(c.getString(c.getColumnIndex(UserProv.USERNAME)));
        userModel.setPassword(c.getString(c.getColumnIndex(UserProv.PASSWORD)));
        return userModel;
    }


    public UserModel get(Long userId) {
        final Cursor cursor = contentResolver.query(UserProv.CONTENT_URI, null, UserProv._ID + " = ? ", new String[]{String.valueOf(userId)}, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            final UserModel userModel = intoModel(cursor);
            cursor.close();
            return userModel;
        }
        return null;
    }
}