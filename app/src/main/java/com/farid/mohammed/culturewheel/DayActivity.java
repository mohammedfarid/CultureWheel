package com.farid.mohammed.culturewheel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.farid.mohammed.culturewheel.carddata.CategorySecond;
import com.farid.mohammed.culturewheel.carddata.DataAdapterCardSecond;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.util.ArrayList;


public class DayActivity extends AppCompatActivity {
    String url, dating, lang;
    ProgressDialog mProgressDialog;
    Intent intent;
    ArrayList<String> imgSrc, nameList, timeList, locationList, linksList;
    ImageView backGround;
    TextView noShow;
    LinearLayout linearLayout;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private DataAdapterCardSecond dataAdapterCardSecond;
    private ArrayList<CategorySecond> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        intent = getIntent();
        url = intent.getStringExtra("url");
        dating = intent.getStringExtra("daytime");
        lang = intent.getStringExtra("lang");
        Toast.makeText(getApplicationContext(), dating, Toast.LENGTH_LONG).show();
        backGround = (ImageView) findViewById(R.id.backdrop2);
        noShow = (TextView) findViewById(R.id.no_tv);
        linearLayout = (LinearLayout) findViewById(R.id.no_linear);
        //add Toolbare
        initToolbar();
        //add RecyclerView
        initViews();
        new HtmlJsoup().execute();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle(dating);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initViews() {
        categoryList = new ArrayList<>();
        dataAdapterCardSecond = new DataAdapterCardSecond(this, categoryList);

        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(dataAdapterCardSecond);
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            final GestureDetector gestureDetector = new GestureDetector(getApplicationContext(),
                    new GestureDetector.SimpleOnGestureListener() {

                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }

                    });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);


                    try {
                        url = linksList.get(position);
                        Intent details = new Intent(DayActivity.this, DetailsEventActivity.class);
                        details.putExtra("url", url);
                        startActivity(details);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        // Configure the search info and add any event listeners...
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            if (item.getItemId() == android.R.id.home) {
                this.finish();
            } else {
                return super.onOptionsItemSelected(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private class HtmlJsoup extends AsyncTask<Void, Void, String> {
        ArrayList<Bitmap> bitmap;
        ArrayList<InputStream> input;
        Document document;
        Elements imgsEvent, namesEvent, timesEvent, locationsEvent, linksEvent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(DayActivity.this);
            mProgressDialog.setTitle(R.string.load2);
            mProgressDialog.setMessage(getApplicationContext().getResources().getString(R.string.loading));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                imgSrc = new ArrayList<>();
                input = new ArrayList<>();
                bitmap = new ArrayList<>();
                nameList = new ArrayList<>();
                timeList = new ArrayList<>();
                locationList = new ArrayList<>();
                linksList = new ArrayList<>();
                // Connect to the web site
                System.setProperty("javax.net.ssl.trustStore", "/path/to/web2.uconn.edu.jks");
                document = Jsoup.connect(url).get();

                // Using Elements to get the class data
                imgsEvent = document.select("div[class=container event-info] img[src]");
                namesEvent = document.select("div[class=container event-info] h2[class=event-title-o]");
                timesEvent = document.select("div[class=container event-info] h2[class=event-description-o arabicNumber place-left]");
                locationsEvent = document.select("div[class=container event-info] h2[class=event-description-o place-left horizontal-space]");
                linksEvent = document.select("div[class=inline-block event-btns-o] a[href]#Button5");
                for (int i = 0; i < imgsEvent.size(); i++) {
                    Element img = imgsEvent.get(i);
                    Element nam = namesEvent.get(i);
                    Element tim = timesEvent.get(i);
                    Element loc = locationsEvent.get(i);
                    Element lin = linksEvent.get(i);
                    nameList.add(nam.text());
                    timeList.add(tim.text());
                    locationList.add(loc.text());
                    linksList.add(lin.attr("href"));
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (bitmap.isEmpty()) {
                linearLayout.setVisibility(View.VISIBLE);
                noShow.setVisibility(View.VISIBLE);
                backGround.setVisibility(View.VISIBLE);
                backGround.setImageResource(R.mipmap.sad);
                noShow.setText(R.string.noshow);
            } else {
                //init cate
                for (int i = 0; i < imgsEvent.size(); i++) {
                    CategorySecond a = new CategorySecond(nameList.get(i), timeList.get(i),
                            locationList.get(i), bitmap.get(i));
                    categoryList.add(a);
                }
            }
            dataAdapterCardSecond.notifyDataSetChanged();
            mProgressDialog.dismiss();
        }
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private final int spanCount;
        private final int spacing;
        private final boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)
                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}
