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

    public BidModel()   {

    }
    private BidModel(Builder builder) {
        setUserId(builder.userId);
        setListingId(builder.listingId);
        setAmount(builder.amount);
        setDate(builder.date);
    }

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

    public static final class Builder {
        private String userId;
        private String listingId;
        private Double amount;
        private DateTime date;

        public Builder() {
        }

        public Builder withUserId(String val) {
            userId = val;
            return this;
        }

        public Builder withListingId(String val) {
            listingId = val;
            return this;
        }

        public Builder withAmount(Double val) {
            amount = val;
            return this;
        }

        public Builder withDate(DateTime val) {
            date = val;
            return this;
        }

        public BidModel build() {
            return new BidModel(this);
        }
    }
}
