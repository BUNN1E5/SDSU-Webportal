package com.twobitdata.sdsuwebportal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView itemView;
    private ItemAdapter adapter;
    TextView name;
    TextView studentId;

    WebView webView;
    String PACKAGE_NAME;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        PACKAGE_NAME = getApplicationContext().getPackageName();
        //Custom Code Start
        //DataManager.cacheClasses(this);

        setTitle("Admissions");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        itemView = (RecyclerView)findViewById(R.id.item_view);
        adapter = new ItemAdapter(this, DataManager.admissions);
        itemView.setAdapter(adapter);
        itemView.setLayoutManager(new LinearLayoutManager(this));


        webView = (WebView)findViewById(R.id.campus_map);
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.getSettings().setLoadWithOverviewMode(true);
        //webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(true);

        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVisibility(View.INVISIBLE);
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
        name = (TextView) findViewById(R.id.student_name);
        name.setText(DataManager.AdmisionStatus.get("Name:"));

        studentId = (TextView)findViewById(R.id.student_id);
        studentId.setText(DataManager.AdmisionStatus.get("Student ID:"));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        } else if(id == R.id.refresh){
            DataManager.retrieveData();
            DataManager.cacheClasses();
            adapter.notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.admissions) {
            webView.setVisibility(View.INVISIBLE);
            adapter.setData(DataManager.admissions);
            adapter.notifyDataSetChanged();
            setTitle("Admissions");
        } else if (id == R.id.registration) {
            webView.setVisibility(View.INVISIBLE);
            adapter.setData(DataManager.registrations);
            adapter.notifyDataSetChanged();
            setTitle("Registration");
        } else if (id == R.id.messages) {
            webView.setVisibility(View.INVISIBLE);
            adapter.setData(DataManager.messages);
            adapter.notifyDataSetChanged();
            setTitle("Messages");
        } else if(id == R.id.Classes){
            webView.setVisibility(View.VISIBLE);
            webView.setInitialScale(150);
            webView.loadDataWithBaseURL("https://sunspot.sdsu.edu/schedule/mycalendar?category=my_calendar", DataManager.classes, "text/html", "UTF-8", null);
            setTitle("Classes");
        }else if(id == R.id.campus_map){
            webView.setVisibility(View.VISIBLE);
            webView.setInitialScale(100);
            webView.loadUrl("file:///android_asset/sdsu_map.png");
            setTitle("Campus Map");
        }
        else if(id == R.id.logout){
            AsyncTask<Void, Void, Boolean> logout = new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... params) {
                    try{
                        SDSUWebportal.instance.logout(SDSUWebportal.instance.sessionID);

                        FileOutputStream outputStream = openFileOutput(Login.fileName, Context.MODE_PRIVATE);
                        PrintWriter writer = new PrintWriter(outputStream);
                        writer.write("");
                        outputStream.close();
                    } catch(Exception e){
                        System.out.println(e.toString());
                    }
                    return true;
                }
            };
            logout.execute();

            Intent loginActivity = new Intent(this, Login.class);
            loginActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginActivity);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.twobitdata.sdsuwebportal/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.twobitdata.sdsuwebportal/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
