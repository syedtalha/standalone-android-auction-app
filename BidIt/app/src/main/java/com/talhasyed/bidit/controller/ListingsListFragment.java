package com.talhasyed.bidit.controller;


import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.talhasyed.bidit.R;
import com.talhasyed.bidit.credential.Authentication;
import com.talhasyed.bidit.model.BidModel;
import com.talhasyed.bidit.model.ListingModel;
import com.talhasyed.bidit.storage.BidCRUD;
import com.talhasyed.bidit.storage.ListingCRUD;
import com.talhasyed.bidit.storage.ListingProviderContract;
import com.talhasyed.bidit.storage.ListingProviderContract.ListingProv;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import java.util.Locale;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;


public class ListingsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {
    private static final int LOADER_ID_LIST = 0;
    public static String[] fromColumns = {
            ListingProv.NAME,
            ListingProv.DESCRIPTION,
            ListingProv.START_DATE,
            ListingProv.CLOSING_DATE,
            ListingProv.CURRENT_BID_ID,
            ListingProv._ID,
    };
    public static int[] toViews = {
            R.id.textViewListItemListingName,
            R.id.textViewListItemListingDescription,
            R.id.textViewListItemListingStartDateTime,
            R.id.textViewListItemListingClosingDateTime,
            R.id.textViewListItemListingMyBid,
            R.id.textViewListItemListingHighestBid

    };
    private ListView listView;
    private SimpleCursorAdapter adapter;

    private ListingCRUD listingCRUD;
    private BidCRUD bidCRUD;
    private Long userId;

    public ListingsListFragment() {

    }


    public static ListingsListFragment newInstance() {
        ListingsListFragment fragment = new ListingsListFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userId = Authentication.getLoggedInUserId(getContext());
        listingCRUD = new ListingCRUD(getContext().getContentResolver());
        bidCRUD = new BidCRUD(getContext().getContentResolver());
        adapter = new SimpleCursorAdapter(getContext(), R.layout.list_item_listing, null, fromColumns, toViews, 0);
        SimpleCursorAdapter.ViewBinder viewBinder = new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (columnIndex == cursor.getColumnIndex(ListingProv.START_DATE) || columnIndex == cursor.getColumnIndex(ListingProv.CLOSING_DATE)) {
                    ((TextView) view).setText(DateTimeFormat.shortTime().withLocale(Locale.getDefault()).withZone(DateTimeZone.getDefault()).print(new DateTime(Long.valueOf(cursor.getString(columnIndex)))));
                    return true;
                } else if (columnIndex == cursor.getColumnIndex(ListingProv.CURRENT_BID_ID)) {
                    final Double highestBidForUser = bidCRUD.getHighestBidForUser(Long.valueOf(cursor.getString(cursor.getColumnIndex(ListingProv._ID))), userId);
                    if (highestBidForUser != null) {
                        ((TextView) view).setText(String.valueOf(highestBidForUser));

                    } else {
                        ((TextView) view).setText("Not Bidded");
                    }

                    return true;
                } else if (columnIndex == cursor.getColumnIndex(ListingProv._ID)) {
                    final Double highestBid = bidCRUD.getHighestBidFor(Long.valueOf(cursor.getString(cursor.getColumnIndex(ListingProv._ID))));
                    if (highestBid != null) {
                        ((TextView) view).setText(String.valueOf(highestBid));

                    } else {
                        ((TextView) view).setText("No Bids");
                    }

                    return true;
                }

                else {
                    return false;
                }
            }
        };
        adapter.setViewBinder(viewBinder);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listings_list, container, false);
        listView = (ListView) view.findViewById(R.id.listViewListingsList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
//TODO        listView.setEmptyView(view.findViewById(R.id.emptyLayoutGeneralList));
        getLoaderManager().initLoader(LOADER_ID_LIST, null, this);

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getContext(),
                ListingProviderContract.BASE_CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, final long l) {
        final ListingModel listing = listingCRUD.get(l);
        if (listing.getClosingDate().isBeforeNow()) {
            Toast.makeText(getContext(), "Sorry, Auction closed!", Toast.LENGTH_SHORT).show();
            return;
        }
        final Double maxVal = bidCRUD.getHighestBidFor(l);
        final MaterialNumberPicker numberPicker = new MaterialNumberPicker.Builder(getContext())
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
        new AlertDialog.Builder(getContext())
                .setTitle("Choose the bid amount")
                .setView(numberPicker)
                .setPositiveButton("Confirm Bid", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String bidResponse = bidCRUD.insert(new BidModel.Builder()
                                .withAmount(Double.parseDouble(String.valueOf(numberPicker.getValue())))
                               // .withDate(new DateTime())done inside insert
                                .withListingId(String.valueOf(l))
                                .withUserId(String.valueOf(Authentication.getLoggedInUserId(getContext())))
                                .build());
                        Toast.makeText(getContext(), bidResponse == null ? "Failed" : bidResponse, Toast.LENGTH_SHORT).show();

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
}
