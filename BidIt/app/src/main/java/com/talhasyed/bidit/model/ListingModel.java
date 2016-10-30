package com.talhasyed.bidit.model;

import org.joda.time.DateTime;

/**
 * Created by Talha Syed on 29-10-2016.
 */

public class ListingModel extends BaseModel {
    private String name;
    private String description;
    private DateTime startDate;
    private DateTime closingDate;
    private String currentBidId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(DateTime closingDate) {
        this.closingDate = closingDate;
    }

    public String getCurrentBidId() {
        return currentBidId;
    }

    public void setCurrentBidId(String currentBidId) {
        this.currentBidId = currentBidId;
    }
}
