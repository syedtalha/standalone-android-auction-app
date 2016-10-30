package com.talhasyed.bidit.controller;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.talhasyed.bidit.R;
import com.talhasyed.bidit.credential.Authentication;
import com.talhasyed.bidit.loader.ListingDetailLoader;
import com.talhasyed.bidit.model.BidModel;
import com.talhasyed.bidit.model.ListingModel;
import com.talhasyed.bidit.storage.BidCRUD;
import com.talhasyed.bidit.storage.BidProviderContract;
import com.talhasyed.bidit.storage.BidProviderContract.BidProv;
import com.talhasyed.bidit.storage.ListingCRUD;
import com.talhasyed.bidit.storage.UserProviderContract;
import com.talhasyed.bidit.views.CountDownTimerView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import java.util.Locale;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;

public class ListingDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ListingModel> {
    public static final String KEY_LISTING_ID = "key_listing_id";
    private static final int LOADER_ID_LISTING_DETAILS = 0;
    private static final int LOADER_ID_BID_LIST = 1;
    public static String[] fromColumns = {
            BidProv.AMOUNT,
            BidProv.DATE,
            UserProviderContract.UserProv.NAME,
    };
    public static int[] toViews = {
            R.id.textViewListItemBidAmount,
            R.id.textViewListItemBidDate,
            R.id.textViewListItemBidUsersName
    };
    private ListingCRUD listingCRUD;
    private BidCRUD bidCRUD;
    private TextView tvName, tvDesc;
    private ListingModel listing;
    private Long listingId;
    private ListView listView;
    private CountDownTimerView timerView;
    private SimpleCursorAdapter adapter;
    private LoaderManager.LoaderCallbacks<? extends Object> listCallback;

    public ListingDetailActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_detail);

        tvName = (TextView) findViewById(R.id.textViewListingDetailName);
        tvDesc = (TextView) findViewById(R.id.textViewListingDetailDescription);
        timerView = (CountDownTimerView) findViewById(R.id.countDownTimerViewListingDetailTimer);
        listView = (ListView) findViewById(R.id.listViewListingDetailBidHistory);

        listingId = getIntent().getExtras().getLong(KEY_LISTING_ID);
        getSupportLoaderManager().initLoader(LOADER_ID_LISTING_DETAILS, null, this).forceLoad();
        bidCRUD = new BidCRUD(getContentResolver());
        listingCRUD = new ListingCRUD(getContentResolver());

        adapter = new SimpleCursorAdapter(this, R.layout.list_item_bid, null, fromColumns, toViews, 0);
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (columnIndex==cursor.getColumnIndex(BidProv.DATE))   {
                    ((TextView) view).setText(DateTimeFormat.mediumDateTime().withLocale(Locale.getDefault()).withZone(DateTimeZone.getDefault()).print(new DateTime(Long.valueOf(cursor.getString(columnIndex)))));
                    return true;
                }
                return false;
            }
        });
        listView.setAdapter(adapter);
        listCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(
                        getApplicationContext(),
                        BidProv.LIST_CONTENT_URI,
                        null,
                        BidProv.LISTING_ID+" = ? ",
                        new String[]{String.valueOf(listingId)}, BidProv.DATE+" DESC ");
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                adapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                adapter.swapCursor(null);

            }
        };
        getSupportLoaderManager().initLoader(LOADER_ID_BID_LIST, null, listCallback);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.listing_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_bid) {
            tryToBid();
        }

        return super.onOptionsItemSelected(item);
    }

    private void tryToBid() {
        if (listing.getClosingDate().isBeforeNow()) {
            Toast.makeText(this, "Sorry, Auction closed!", Toast.LENGTH_SHORT).show();
            return;
        }
        final Double maxVal = bidCRUD.getHighestBidFor(listingId);
        final MaterialNumberPicker numberPicker = new MaterialNumberPicker.Builder(this)
                .maxValue(999999)
                .defaultValue(1)
                .textSize(30)
                .enableFocusability(true)
                .build();
        if (maxVal != null) {
            numberPicker.setMinValue(maxVal.intValue() + 1);
            numberPicker.setValue(maxVal.intValue() + 1);
        } else {
            //TODO numberPicker.setMinValue(listing.getMinValue);
        }
        new AlertDialog.Builder(this)
                .setTitle("Choose the bid amount")
                .setView(numberPicker)
                .setPositiveButton("Confirm Bid", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String bidResponse = bidCRUD.insert(new BidModel.Builder()
                                .withAmount(Double.parseDouble(String.valueOf(numberPicker.getValue())))
                                // .withDate(new DateTime())done inside insert
                                .withListingId(String.valueOf(listingId))
                                .withUserId(String.valueOf(Authentication.getLoggedInUserId(getApplicationContext())))
                                .build());
                        Toast.makeText(getApplicationContext(), bidResponse == null ? "Failed" : bidResponse, Toast.LENGTH_SHORT).show();

                    }
                })
                .setNeutralButton("Cancel Bid", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }


    @Override
    public Loader<ListingModel> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID_LISTING_DETAILS) {
            return new ListingDetailLoader(this, listingId);
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<ListingModel> loader, ListingModel data) {
        bindModelToView(data);

    }

    private void bindModelToView(ListingModel data) {
        setTitle(data.getName());
        listing = data;
        tvDesc.setText(data.getDescription());
        tvName.setText(data.getName());

        timerView.resetView();
        final long closeTime = data.getClosingDate().getMillis();
        final long startTime = data.getStartDate().getMillis();
        if (closeTime > System.currentTimeMillis()) {
            timerView.setTime(closeTime, startTime);
            timerView.startCountDown();
        } else {
            timerView.resetView();
        }

    }

    @Override
    public void onLoaderReset(Loader<ListingModel> loader) {

    }
}
