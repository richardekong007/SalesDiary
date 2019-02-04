package com.daveace.salesdiary.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.daveace.salesdiary.interfaces.ObjectMapper;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.UUID;

@IgnoreExtraProperties
public class SalesEvent implements ObjectMapper, Parcelable {
    private String id;
    private String productId;
    private String traderId;
    private String customerId;
    private double price;
    private double sales;
    private double left;
    @ServerTimestamp
    private Date date;
    private GeoPoint loc;

    public final static Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public SalesEvent createFromParcel(Parcel source) {
            return new SalesEvent(source);
        }

        @Override
        public SalesEvent[] newArray(int size) {
            return new SalesEvent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flag) {
        dest.writeString(this.id);
        dest.writeString(this.productId);
        dest.writeString(this.traderId);
        dest.writeString(this.customerId);
        dest.writeDouble(this.price);
        dest.writeDouble(this.sales);
        dest.writeDouble(this.left);
        dest.writeLong(this.date.getTime());
        dest.writeDouble(this.loc.getLatitude());
        dest.writeDouble(this.loc.getLongitude());
    }

    private SalesEvent() {
    }

    private SalesEvent(Parcel source) {
        this.productId = source.readString();
        this.traderId = source.readString();
        this.customerId = source.readString();
        this.price = source.readDouble();
        this.sales = source.readDouble();
        this.left = source.readDouble();
        this.date = new Date(source.readLong());
        //may not work
        this.loc = new GeoPoint(source.readDouble(), source.readDouble());

    }

    private SalesEvent(String productId, String traderId, String customerId,
                       Double price, double sales, double left, Date date, GeoPoint loc) {

        this.productId = productId;
        this.traderId = traderId;
        this.customerId = customerId;
        this.price = price;
        this.sales = sales;
        this.left = left;
        this.date = date;
        this.loc = loc;
        setId(UUID.randomUUID().toString());
    }

    public static SalesEvent getInstance() {
        return new SalesEvent();
    }

    public static SalesEvent getInstance(String productId, String traderId, String customerId,
                                         double price, double sales, double left,
                                         Date date, GeoPoint loc) {

        return new SalesEvent(productId, traderId,
                customerId, price, sales, left, date, loc);

    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTraderId() {
        return traderId;
    }

    public void setTraderId(String traderId) {
        this.traderId = traderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public double getLeft() {
        return left;
    }

    public void setLeft(Integer left) {
        this.left = left;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public GeoPoint getLoc() {
        return loc;
    }

    public void setLoc(GeoPoint loc) {
        this.loc = loc;
    }
}