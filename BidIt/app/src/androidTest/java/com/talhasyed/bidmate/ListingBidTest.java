package com.talhasyed.bidmate;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.talhasyed.bidit.credential.LocalAuthException;
import com.talhasyed.bidit.model.BidModel;
import com.talhasyed.bidit.model.ListingModel;
import com.talhasyed.bidit.model.UserModel;
import com.talhasyed.bidit.storage.BidCRUD;
import com.talhasyed.bidit.storage.ListingCRUD;
import com.talhasyed.bidit.storage.UserCRUD;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class ListingBidTest {

    private ListingCRUD listingCRUD;
    private BidCRUD bidCRUD;
    private UserCRUD userCRUD;
    private UserModel userModel;
    private Long listingId;

    @Before
    public void initialize() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        listingCRUD = new ListingCRUD(appContext.getContentResolver());
        bidCRUD = new BidCRUD(appContext.getContentResolver());
        userCRUD = new UserCRUD(appContext.getContentResolver());
        listingId = listingCRUD.insert(new ListingModel.Builder()
                .withName("ajkshd")
                .withDescription("alksdjf")
                .withClosingDate(new DateTime().plusMinutes(5))
                .build()
        );
        try {
            userModel = userCRUD.signUp(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
        } catch (LocalAuthException e) {
            e.printStackTrace();
        }


    }


    @Test
    public void listingBidInsertionAndValidation() throws Exception {
        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getTargetContext();
        if (listingCRUD.get(listingId).getClosingDate().isAfterNow()) {
            assertEquals(bidCRUD.insert(new BidModel.Builder()
                    .withUserId(String.valueOf(userModel.get_id()))
                    .withListingId(String.valueOf(listingId))
                    .withAmount((bidCRUD.getHighestBidFor(listingId) == null ? 0.0 : bidCRUD.getHighestBidFor(listingId)) + 1)
                    .build()), BidCRUD.SUCCESS);
        }

    }
}
