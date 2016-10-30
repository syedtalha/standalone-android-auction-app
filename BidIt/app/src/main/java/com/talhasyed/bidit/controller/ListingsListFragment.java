package com.talhasyed.bidit.controller;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.talhasyed.bidit.R;
import com.talhasyed.bidit.storage.ListingProviderContract;
import com.talhasyed.bidit.storage.ListingProviderContract.ListingProv;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import java.util.Locale;


public class ListingsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_ID_LIST = 0;
    public static String[] fromColumns = {
            ListingProv.NAME,
            ListingProv.DESCRIPTION,
            ListingProv.START_DATE,
            ListingProv.CLOSING_DATE,
//          ListingProv.WINNING_BID_ID,
    };
    public static int[] toViews = {
            R.id.textViewListItemListingName,
            R.id.textViewListItemListingDescription,
            R.id.textViewListItemListingStartDateTime,
            R.id.textViewListItemListingClosingDateTime

    };
    private ListView listView;
    private SimpleCursorAdapter adapter;

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
        adapter = new SimpleCursorAdapter(getContext(), R.layout.list_item_listing, null, fromColumns, toViews, 0);
        SimpleCursorAdapter.ViewBinder viewBinder = new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (columnIndex == cursor.getColumnIndex(ListingProv.START_DATE) ||columnIndex == cursor.getColumnIndex(ListingProv.CLOSING_DATE) ) {
                    ((TextView) view).setText(DateTimeFormat.shortDateTime().withLocale(Locale.getDefault()).withZone(DateTimeZone.getDefault()).print(new DateTime(Long.valueOf(cursor.getString(columnIndex)))));
                    return true;
                } else {
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
//TODO        listView.setEmptyView(view.findViewById(R.id.emptyLayoutGeneralList));
        getLoaderManager().initLoader(LOADER_ID_LIST, null, this);

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), ListingProviderContract.BASE_CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);

    }


}
