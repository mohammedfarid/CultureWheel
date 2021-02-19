package com.farid.mohammed.culturewheel.carddata;

/**
 * Created by Mohammed on 26/10/2016.
 */

public class Categories {
    private int thumbnailsEvent;
    private int overFlow;
    private String tvDay;
    private String tvMonth;

    public Categories(int overFlow, int thumbnailsEvent, String tvDay, String tvMonth) {
        this.overFlow=overFlow;
        this.thumbnailsEvent = thumbnailsEvent;
        this.tvDay = tvDay;
        this.tvMonth = tvMonth;
    }

    public int getOverFlow() {
        return overFlow;
    }

    public void setOverFlow(int overFlow) {
        this.overFlow = overFlow;
    }

    public int getThumbnailsEvent() {
        return thumbnailsEvent;
    }

    public void setThumbnailsEvent(int thumbnailsEvent) {
        this.thumbnailsEvent = thumbnailsEvent;
    }

    public String getTvDay() {
        return tvDay;
    }

    public void setTvDay(String tvDay) {
        this.tvDay = tvDay;
    }

    public String getTvMonth() {
        return tvMonth;
    }

    public void setTvMonth(String tvMonth) {
        this.tvMonth = tvMonth;
    }

}
