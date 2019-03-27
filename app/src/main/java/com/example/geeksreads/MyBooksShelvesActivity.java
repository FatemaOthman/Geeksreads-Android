package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
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

public class MyBooksShelvesActivity extends AppCompatActivity {

    /** Global Variables to Store Context of this Activity itself */
    private Context mContext;

    /** Global Public Static Variables used for Testing */
    public static String sForTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO Remove the below two lines after integration
        LoginActivity.sCurrentUserID = "iiiidddd1142019";
        LoginActivity.sCurrentToken = "xYzAbCdToKeN";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books_shelves);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mContext = this;

        Button readButton = findViewById(R.id.ReadBtn);
        Button currentlyReadingButton = findViewById(R.id.CurrentlyReadingBtn);
        Button wantToReadButton = findViewById(R.id.WantToReadBtn);


        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(MyBooksShelvesActivity.this, ReadBooksActivity.class);
                startActivity(myIntent);

            }
        });
        currentlyReadingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MyBooksShelvesActivity.this,CurrentlyReadingActivity.class);
                startActivity(myIntent);

            }
        });
        wantToReadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MyBooksShelvesActivity.this, WantToReadActivity.class);
                startActivity(myIntent);

            }
        });

        UpdateBookShelfCount updateReadShelf = new UpdateBookShelfCount("Read");
        UpdateBookShelfCount updateWantToReadShelf = new UpdateBookShelfCount("WantToRead");
        UpdateBookShelfCount updateCurrentlyReadingShelf = new UpdateBookShelfCount("CurrentlyReading");

        /* URL For Login API */
        String urlService = "http://geeksreads.000webhostapp.com/Morsy/GetBookShelf.php";

        JSONObject mJSON = new JSONObject();
        try {
            mJSON.put("UserID", LoginActivity.sCurrentUserID);
            mJSON.put("UserToken", LoginActivity.sCurrentToken);
        }catch (JSONException e) {
            e.printStackTrace();
        }

        /* Requesting Read Shelf Count */
        try {
            mJSON.put("ShelfName", "Read");
        }catch (JSONException e) {
            e.printStackTrace();
        }
        updateReadShelf.execute(urlService, mJSON.toString());

        /* Requesting Want To Read Shelf Count */
        try {
            mJSON.put("ShelfName", "WantToRead");
        }catch (JSONException e) {
            e.printStackTrace();
        }
        updateWantToReadShelf.execute(urlService, mJSON.toString());

        /* Requesting Currently Reading Shelf Count */
        try {
            mJSON.put("ShelfName", "CurrentlyReading");
        }catch (JSONException e) {
            e.printStackTrace();
        }
        updateCurrentlyReadingShelf.execute(urlService, mJSON.toString());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setMaxWidth(800);
        searchView.setQueryHint("Search books");
        searchView.setBackgroundColor(getResources().getColor(R.color.white));
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Class that get the data from host and Add it to its views.
     *  The Parameters are host Url and toSend Data.
     */
    public class UpdateBookShelfCount extends AsyncTask<String, String, String> {
        static final String REQUEST_METHOD="GET";
        String passedString;

        public UpdateBookShelfCount(String inpString)
        {
            passedString = inpString;
        }
        @Override
        protected void onPreExecute() {
            /* Do Nothing */
        }

        @Override
        protected String doInBackground(String... params){
            String UrlString = params[0];
            String JSONString = params[1];

            String result= "";


            try {
                /* Create a URL object holding our url */
                URL url = new URL(UrlString);
                /* Create an HTTP Connection and adjust its options */
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod(REQUEST_METHOD);
                http.setDoInput(true);
                http.setDoOutput(true);

                /* A Stream object to hold the sent data to API Call */
                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("Json","UTF-8")+"="+URLEncoder.encode(JSONString,"UTF-8");

                writer.write(data);
                writer.flush();
                writer.close();
                ops.close();

                /* A Stream object to get the returned data from API Call */
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

            }
            /* Handling Exceptions */
            catch (MalformedURLException e) {
                result = e.getMessage();
            } catch (IOException e) {
                result = e.getMessage();
            }
            return result;
        }

        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String result){ ;

            if(result==null) {
                Toast.makeText(mContext,"Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                /* Creating a JSON Object to parse the data in */
                final JSONObject jsonObject = new JSONObject(result);

                sForTest = jsonObject.getString("ShelfCount");

                String shelfCount = jsonObject.getString("ShelfCount");

                if (shelfCount != null)
                {
                    if (this.passedString.equals("Read"))
                    {
                        Button readBtn = findViewById(R.id.ReadBtn);
                        readBtn.setText("Read  " + shelfCount);
                    }
                    else if (passedString.equals("WantToRead"))
                    {
                        Button wantToReadBtn = findViewById(R.id.WantToReadBtn);
                        wantToReadBtn.setText("Want to Read  " + shelfCount);
                    }
                    else if (passedString.equals("CurrentlyReading"))
                    {
                        Button currentlyReadingBtn = findViewById(R.id.CurrentlyReadingBtn);
                        currentlyReadingBtn.setText("Currently Reading  " + shelfCount);
                    }
                }
            }
            /* Catching Exceptions */
            catch(JSONException e)
            {
                e.printStackTrace();
            }
        }

    }
}
