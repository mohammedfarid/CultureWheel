package com.farid.mohammed.culturewheel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.util.ArrayList;

import carddata.CategorySecond;

public class DetailsEventActivity extends AppCompatActivity {
    String url;
    ProgressDialog mProgressDialog;
    Intent intent;
    ImageView majorImage, clockImage, locImage, tagImage;
    TextView titleText, clockText, locText, tagText, detailsText;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_event);
        majorImage = (ImageView) findViewById(R.id.thumbnail);
        clockImage = (ImageView) findViewById(R.id.clockImg);
        locImage = (ImageView) findViewById(R.id.locImg);
        tagImage = (ImageView) findViewById(R.id.tagImg);
        titleText = (TextView) findViewById(R.id.namText);
        clockText = (TextView) findViewById(R.id.clockText);
        locText = (TextView) findViewById(R.id.locText);
        tagText = (TextView) findViewById(R.id.tagText);
        detailsText = (TextView) findViewById(R.id.detailsText);
        detailsText.setMovementMethod(new ScrollingMovementMethod());
        intent = getIntent();
        url = intent.getStringExtra("url");
        Toast.makeText(getApplicationContext(),url, Toast.LENGTH_SHORT).show();
        //add Toolbare
        initToolbar();
        new HtmlJsoup().execute();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        // Configure the search info and add any event listeners...
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    this.finish();
                    break;
                default:
                    return super.onOptionsItemSelected(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    private class HtmlJsoup extends AsyncTask<Void, Void, Void> {
        ArrayList<Bitmap> bitmap;
        ArrayList<String> imgSrc, nameList, timeList, tagList, locationList, detailsList, detailsList2, detailsList3;
        ArrayList<InputStream> input;
        Document document;
        Elements imgsEvent, namesEvent, timesEvent, tagEvent, locationsEvent, detailsEvent, detailsEvent2, detailsEvent3;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(DetailsEventActivity.this);
            mProgressDialog.setTitle(R.string.load3);
            mProgressDialog.setMessage(getApplicationContext().getResources().getString(R.string.loading));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                imgSrc = new ArrayList<>();
                input = new ArrayList<>();
                bitmap = new ArrayList<>();
                nameList = new ArrayList<>();
                timeList = new ArrayList<>();
                locationList = new ArrayList<>();
                detailsList = new ArrayList<>();
                detailsList2 = new ArrayList<>();
                detailsList3 = new ArrayList<>();
                tagList = new ArrayList<>();
                // Connect to the web site
                document = Jsoup.connect(url).get();

                // Using Elements to get the class data
                imgsEvent = document.select("div[class=event-img col-md-4 col-sm-6 col-xs-12] img[src]");
                namesEvent = document.select("div[class=container event-info] h1[class=event-title-o]");
                timesEvent = document.select("div[class=place-left] h1[class=event-description-o place-left font20] ");
                tagEvent = document.select("div[class=place-left] h1[class=event-description-o place-left font20 horizontal-space]");
                locationsEvent = document.select("div[class=place-left] h1[class=event-description-o  place-left font20]");
                detailsEvent = document.select("div[class = col-md-8 col-xs-12 place-left eventDetailsCont]");
                for (int i = 0; i < imgsEvent.size(); i++) {
                    Element img = imgsEvent.get(i);
                    Element nam = namesEvent.get(i);
                    Element tim = timesEvent.get(i);
                    Element loc = locationsEvent.get(i);
                    Element tags = tagEvent.get(i);
                    Element det = detailsEvent.get(i);
                    nameList.add(nam.text());
                    timeList.add(tim.text());
                    locationList.add(loc.text());
                    detailsList.add(det.text());
                    tagList.add(tags.text());
                    // Locate the src attribute
                    imgSrc.add(img.attr("src"));
                    // Download image from URL
                    input.add(new java.net.URL(imgSrc.get(i)).openStream());
                    // Decode Bitmap
                    bitmap.add(BitmapFactory.decodeStream(input.get(i)));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (bitmap.isEmpty()) {
                Toast.makeText(getApplicationContext(), "no", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    majorImage.setImageBitmap(bitmap.get(0));
                    titleText.setText(nameList.get(0));
                    clockText.setText(timeList.get(0));
                    tagText.setText(tagList.get(0));
                    locText.setText(locationList.get(0));
                    detailsText.setText(detailsList.get(0));
                } catch (Exception e) {

                }
            }
            mProgressDialog.dismiss();
        }
    }

}
