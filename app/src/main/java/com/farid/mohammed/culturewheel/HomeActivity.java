package com.farid.mohammed.culturewheel;

import android.*;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import carddata.Category;
import carddata.CategorySecond;
import carddata.DataAdapterCard;
import categorys.MonthCategory;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private DataAdapterCard dataAdapterCard;
    private ArrayList<Category> categoryList;

    private static final String SELECT_ITEM_ID = "selected";
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private int selectedId;

    private Locale locale;
    private String lang;

    public MonthCategory monthCategory;

    public Calendar calendar;
    public int daysInMonth;
    public int myDayInMonth;
    public ArrayList<String> allDays;
    public int month, year;
    public DateFormatSymbols dfs;
    public String[] months;
    public String monthName;
    int myMonth;
    String url;
    ProgressDialog mProgressDialog;
    ImageView dropBack;
    //Permision code that will be checked in the method onRequestPermissionsResult
    private int STORAGE_PERMISSION_CODE = 23;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        dropBack = (ImageView) findViewById(R.id.backdrop);

        new HtmlJsoup().execute();

        //add Toolbare
        initToolbar();
        //add Calender
        intiCalender();
        //add RecyclerView
        initViews();
        //add Locale
        lang = Locale.getDefault().toString().toLowerCase();
        initCollapsingToolbar();

        //add DrawerNavigation
        navigationView = (NavigationView) findViewById(R.id.main_drawer);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this
                , drawerLayout
                , toolbar
                , R.string.drawer_open
                , R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        selectedId = savedInstanceState == null ? R.id.home : savedInstanceState.getInt(SELECT_ITEM_ID);
        navigated(selectedId);
        if (isReadStorageAllowed()) {
            //If permission is already having then showing the toast
            Toast.makeText(HomeActivity.this, "You already have the permission", Toast.LENGTH_LONG).show();
            //Existing the method with return
            return;
        }

        //If the app has not the permission then asking for the permission
        requestStoragePermission();
    }

    private void intiCalender() {
        calendar = Calendar.getInstance();
        dfs = new DateFormatSymbols();
        daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        myDayInMonth = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        months = dfs.getShortMonths();
        year = calendar.get(Calendar.YEAR);
        myMonth = month + 1;

        if (month >= 0 && month <= 11) {
            monthName = months[month];
        }
        allDays = new ArrayList<String>();
        int x = 1;
        for (int i = 0; i < daysInMonth; i++) {
            x = i + 1;
            allDays.add(x + "");
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }

    private void initViews() {
        categoryList = new ArrayList<>();
        dataAdapterCard = new DataAdapterCard(this, categoryList);
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(dataAdapterCard);

        //init cate
        monthCategory = new MonthCategory(getApplicationContext());
        for (int i = myDayInMonth; i <= allDays.size(); i++) {
            Category a = new Category(monthCategory.draw[i], monthCategory.color[0], i + "", monthName);
            categoryList.add(a);
        }

        dataAdapterCard.notifyDataSetChanged();
        /**
         * RecyclerView item decoration - give equal margin around grid item
         */

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(),
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
                    //Toast.makeText(getApplicationContext(),
                    // categoryList.get(position).getTvMonth().toString()
                    // , Toast.LENGTH_SHORT).show();

                    try {
                        if (lang.equals("ar")) {
                            //Toast.makeText(getApplicationContext(),lang+"عربي",Toast.LENGTH_LONG).show();
                            url = "http://www.culturewheel.com/ar/day/" + year + "-" + myMonth + "-" + categoryList.get(position).getTvDay();
                        } else {
                            //Toast.makeText(getApplicationContext(),lang+"en",Toast.LENGTH_LONG).show();
                            url = "http://www.culturewheel.com/en/day/" + year + "-" + myMonth + "-" + categoryList.get(position).getTvDay();
                        }
                        Intent urlIntent = new Intent(HomeActivity.this, DayActivity.class);
                        urlIntent.putExtra("url", url);
                        urlIntent.putExtra("daytime", categoryList.get(position).getTvDay() + "/" + myMonth + "/" + year + "");
                        urlIntent.putExtra("lang", lang);
                        startActivity(urlIntent);
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
        try {
            Glide.with(this).load(R.mipmap.ic_launcher_copy).into((ImageView) findViewById(R.id.logodrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigated(int selectedId) {
        Intent intent = null;
        if (selectedId == R.id.home) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (selectedId == R.id.contact_us) {
            drawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(HomeActivity.this,ContactUsActivity.class);
            startActivity(intent);
        }
        if (selectedId == R.id.membership) {
            drawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(HomeActivity.this,MembershipActivity.class);
            startActivity(intent);
        }
        if (selectedId == R.id.about_us) {
            drawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(HomeActivity.this,AboutCultuerWheel.class);
            startActivity(intent);
        }
        if (selectedId == R.id.developer_by) {
            drawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(HomeActivity.this,PoweredByActivity.class);
            startActivity(intent);
        }
       // startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        selectedId = item.getItemId();
        navigated(selectedId);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECT_ITEM_ID, selectedId);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Configure the search info and add any event listeners...
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.activity_lang:
                    if (lang.equals("en_us") || lang.equals("en")) {
                        locale = new Locale("ar");
                    } else {
                        locale = new Locale("en");
                    }
                    locale.setDefault(locale);
                    android.content.res.Configuration config = new android.content.res.Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                    Intent intent = new Intent(this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    return super.onOptionsItemSelected(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

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

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    //collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }
    //We are calling this method to check the permission status
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.CALL_PHONE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.CALL_PHONE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.CALL_PHONE}
                , STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
    private class HtmlJsoup extends AsyncTask<Void, Void, Void> {
        ArrayList<Bitmap> bitmap;
        ArrayList<String> imgSrc;
        ArrayList<InputStream> input;
        Document document;
        Elements imgsEvent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(HomeActivity.this);
            mProgressDialog.setTitle(R.string.load1);
            mProgressDialog.setMessage(getApplicationContext().getResources().getString(R.string.loading)); mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                intiCalender();
                url = "http://www.culturewheel.com/en/day/" + year + "-" + myMonth + "-" + myDayInMonth;
                imgSrc = new ArrayList<>();
                input = new ArrayList<>();
                bitmap = new ArrayList<>();
                // Connect to the web site
                document = Jsoup.connect(url).get();
                // Using Elements to get the class data
                imgsEvent = document.select("div[class=container event-info] img[src]");
                for (int i = 0; i < imgsEvent.size(); i++) {
                    Element img = imgsEvent.get(i);
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
                Glide.with(HomeActivity.this).load(R.drawable.saqya_app).into((ImageView) findViewById(R.id.backdrop));
            } else {
                try {
                    dropBack.setImageBitmap(bitmap.get(0));
                    //
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "nnno", Toast.LENGTH_SHORT).show();
                }
            }
            mProgressDialog.dismiss();
        }
    }

}
