package com.talhasyed.bidmate;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.talhasyed.bidit.model.UserModel;
import com.talhasyed.bidit.storage.UserCRUD;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class UserCRUDTest {
    private UserCRUD userCRUD;

    @Before
    public void initializeCRUD() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        userCRUD = new UserCRUD(appContext.getContentResolver());
    }


    @Test
    public void newUserInsertion() throws Exception {
        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getTargetContext();
        final UserModel userModel = userCRUD.signUp(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
        Thread.sleep(100);
        assertTrue(userCRUD.checkUser(userModel.get_id()));
    }
}
