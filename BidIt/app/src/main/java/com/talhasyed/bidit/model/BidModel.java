package com.talhasyed.bidit.model;

import org.joda.time.DateTime;

/**
 * Created by Talha Syed on 29-10-2016.
 */

public class BidModel extends BaseModel {
    private String userId;
    private String listingId;
    private Double amount;
    private DateTime date;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }
}
