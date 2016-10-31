package com.talhasyed.bidit.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.talhasyed.bidit.R;
import com.talhasyed.bidit.credential.Authentication;
import com.talhasyed.bidit.loader.UserDetailLoader;
import com.talhasyed.bidit.model.UserModel;
import com.talhasyed.bidit.service.AutoBotService;
import com.talhasyed.bidit.storage.UserCRUD;

public class MainActivity extends AutoBotServiceBoundActivity
        implements
        NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<UserModel> {
    private static final int LOADER_ID_USER_DETAILS = 0;
    private UserCRUD userCRUD;
    private TextView tvName, tvUserName;
    private UserModel loggedInUser;
    private Long loggedInUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                startActivity(new Intent(getBaseContext(), AddItemActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        tvName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewMainNavHeaderName);
        tvUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewMainNavHeaderUsername);


        userCRUD = new UserCRUD(getContentResolver());
        loggedInUserId = Authentication.getLoggedInUserId(getBaseContext());
        if (loggedInUserId == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            if (!userCRUD.checkUser(loggedInUserId)) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                loggedInUser = userCRUD.get(loggedInUserId);
            }
        }


        if (loggedInUser != null) {
            getSupportLoaderManager().initLoader(LOADER_ID_USER_DETAILS, null, this).forceLoad();
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_logout) {
            Authentication.clearAll(getBaseContext());
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public Loader<UserModel> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID_USER_DETAILS) {
            return new UserDetailLoader(this, loggedInUserId);
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<UserModel> loader, UserModel data) {
        bindUserViewToModel(data);
    }

    private void bindUserViewToModel(UserModel data) {
        loggedInUser = data;
        tvName.setText(data.getName());
        tvUserName.setText(data.getUserName());
    }

    @Override
    public void onLoaderReset(Loader<UserModel> loader) {

    }


}
