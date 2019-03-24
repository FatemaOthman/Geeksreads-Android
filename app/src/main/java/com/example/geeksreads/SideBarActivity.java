package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


public class SideBarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static String forTestUserPhotoURL,forTestUserName,forTestFollowersCount,forTestBooksCount;
    ImageView userPhoto;
    TextView userName;
    Context mContext;
    TextView followersCount;
    TextView booksCount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_bar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = this;

        //////////////////////////////////////////////////////

        //Setting the references to refer at the needed views
        NavigationView navigationViewe = findViewById(R.id.nav_view);
        navigationViewe.setNavigationItemSelectedListener(this);
        View header=navigationViewe.getHeaderView(0);
        /*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        userName = header.findViewById(R.id.UserNameTxt);
        userPhoto = header.findViewById(R.id.UserPhoto);
        ////////////////////////////////////////////////////////
        //Getting profile pic or user name clicked to go to the profile activity

        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //code here ...
                Intent myIntent = new Intent(SideBarActivity.this, Profile.class);
                SideBarActivity.this.startActivity(myIntent);

            }
        });
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //code here ...
                Intent myIntent = new Intent(SideBarActivity.this, Profile.class);
                startActivity(myIntent);

            }
        });

        JSONObject JSON = new JSONObject();

        String UrlService = "http://geeksreads.000webhostapp.com/Fatema/SideBar.php";
        GetSideBarDetails getSideBarDetails = new GetSideBarDetails();
        getSideBarDetails.execute(UrlService,JSON.toString());


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem itemFollower = menu.findItem(R.id.Followers);
        followersCount = (TextView) itemFollower.getActionView();
        followersCount.setTextColor(getResources().getColor(R.color.white));
        followersCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SideBarActivity.this, FollowActivity.class);
                SideBarActivity.this.startActivity(myIntent);
            }
        });
        MenuItem itemBook = menu.findItem(R.id.MyBooks);
        booksCount = (TextView) itemBook.getActionView();
        booksCount.setTextColor(getResources().getColor(R.color.white));
        booksCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SideBarActivity.this, BookActivity.class);
                SideBarActivity.this.startActivity(myIntent);
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.side_bar, menu);

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
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        AlertDialog D;
        D = new AlertDialog.Builder(mContext).create();

        int id=item.getItemId();
        if (id == R.id.Home) {
            D.setMessage("Home Page");
            D.show();

        } else if (id == R.id.Followers) {
            D.setMessage("Followers Page");
            D.show();

        } else if (id == R.id.MyBooks) {
            D.setMessage("Books Page");
            D.show();


        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private class GetUserPicture extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                String photoUrl = params[0];
                URL url = new URL(photoUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            }catch (Exception e){
                // Log.d(TAG,e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            userPhoto.setImageBitmap(result);

        }
    }

    public class GetSideBarDetails extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        //public static final int READ_TIMEOUT = 3000;
        //public static final int CONNECTION_TIMEOUT = 3000;
        AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new AlertDialog.Builder(mContext).create();
            dialog.setTitle("Connection Status");
        }

        @Override
        protected String doInBackground(String... params){
            String UrlString = params[0];
            String JSONString = params[1];
            String result= "";

            try {
                //Create a URL object holding our url
                URL url = new URL(UrlString);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod(REQUEST_METHOD);
                http.setDoInput(true);
                http.setDoOutput(true);

                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(JSONString,"UTF-8");

                writer.write(data);
                writer.flush();
                writer.close();
                ops.close();

                //Create a new InputStreamReader
                InputStream ips = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.ISO_8859_1));
                String line ="";
                while ((line = reader.readLine()) != null)
                {
                    result += line;
                }
                reader.close();
                ips.close();
                http.disconnect();
                return result;

            } catch (MalformedURLException e) {
                result = e.getMessage();
            } catch (IOException e) {
                result = e.getMessage();
            }
            return result;
        }

        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String result){
            if(result==null) {
                Toast.makeText(mContext,"Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            try {

                // TODO: Add your Post Execute logic here.
                JSONObject jsonObject = new JSONObject(result);
                followersCount.setText("Followers                             "+jsonObject.getString("Followers"));
                booksCount.setText("My Books                             "+jsonObject.getString("CountBooks"));
                userName.setText(jsonObject.getString("UserName"));
                SideBarActivity.GetUserPicture Pic = new SideBarActivity.GetUserPicture();
                Pic.execute(jsonObject.getString("photourl"));


                forTestUserPhotoURL = jsonObject.getString("photourl");
                forTestUserName=userName.getText().toString();
                forTestFollowersCount=jsonObject.getString("Followers");
                forTestBooksCount=jsonObject.getString("CountBooks");

            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
        }

    }





}
