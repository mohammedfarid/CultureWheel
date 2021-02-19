package com.farid.mohammed.culturewheel.carddata;

import android.graphics.Bitmap;

/**
 * Created by Mohammed on 26/10/2016.
 */

public class CategorySecond {
    private String categoryName;
    private String titleEvent;
    private String dateEvent;
    private Bitmap thumbnailsEventBitmap;


    public CategorySecond(String categoryName, String titleEvent, String dateEvent, Bitmap thumbnailsEventBitmap) {
        this.categoryName = categoryName;
        this.titleEvent = titleEvent;
        this.dateEvent = dateEvent;
        this.thumbnailsEventBitmap = thumbnailsEventBitmap;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTitleEvent() {
        return titleEvent;
    }

    public void setTitleEvent(String titleEvent) {
        this.titleEvent = titleEvent;
    }

    public String getDateEvent() {
        return dateEvent;
    }

    public void setDateEventt(String dateEvent) {
        this.dateEvent = dateEvent;
    }

    public Bitmap getThumbnailsEventBitmap() {
        return thumbnailsEventBitmap;
    }

    public void setThumbnailsEventBitmap(Bitmap thumbnailsEventBitmap) {
        this.thumbnailsEventBitmap = thumbnailsEventBitmap;
    }
}
