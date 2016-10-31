package com.talhasyed.bidit.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.talhasyed.bidit.credential.LocalAuthException;
import com.talhasyed.bidit.model.BidModel;
import com.talhasyed.bidit.model.ListingModel;
import com.talhasyed.bidit.model.UserModel;
import com.talhasyed.bidit.storage.BidCRUD;
import com.talhasyed.bidit.storage.ListingCRUD;
import com.talhasyed.bidit.storage.UserCRUD;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class AutoBotService extends Service {
    public static final String BOT_USERNAME = "donaldtrump";
    public static final String BOT_NAME = "Donald Trump";
    public static final String BOT_PASSWORD = "makeamericagreatagain";
    private final IBinder binder = new LocalBinder();
    private UserCRUD userCRUD;
    private BidCRUD bidCRUD;
    private ListingCRUD listingCRUD;
    private UserModel donaldTrump;
    private Timer timer;

    public AutoBotService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        userCRUD = new UserCRUD(getContentResolver());
        bidCRUD = new BidCRUD(getContentResolver());
        listingCRUD = new ListingCRUD(getContentResolver());
        try {
            donaldTrump = userCRUD.getOrCreateBot();
        } catch (LocalAuthException e) {
            e.printStackTrace();//TODO handle this just in case Donald Trump manages to rig the election
        }



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (timer!=null)    {
            timer.cancel();
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkForRunningPresedentialElectionsAndBid();
            }
        }, 100, 15000);
        return START_NOT_STICKY;
    }

    private void checkForRunningPresedentialElectionsAndBid() {
        List<ListingModel> allActiveListings = listingCRUD.getAllActiveListings();
        if (allActiveListings != null && !allActiveListings.isEmpty()) {
            for (ListingModel listing :
                    allActiveListings) {
                Double highestBidSoFar = bidCRUD.getHighestBidFor(listing.get_id());
                if (highestBidSoFar == null) {
                    highestBidSoFar = 0.0;
                }
                bidCRUD.insert(new BidModel.Builder()
                        .withAmount(highestBidSoFar + new Random().nextInt(100))
                        .withListingId(String.valueOf(listing.get_id()))
                        .withUserId(String.valueOf(donaldTrump.get_id()))
                        .build());
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {

    }

}
