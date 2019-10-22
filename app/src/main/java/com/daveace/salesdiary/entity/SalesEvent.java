package com.daveace.salesdiary.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.daveace.salesdiary.interfaces.Mappable;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.UUID;

@IgnoreExtraProperties
public class SalesEvent implements Mappable, Parcelable {
    private String id;
    private String productId;
    private String traderId;
    private String customerId;
    private double salesPrice;
    private double costPrice;
    private double sales;
    private double left;
    @ServerTimestamp
    private Date date;
    private Double latitude;
    private Double longitude;

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
        dest.writeDouble(this.salesPrice);
        dest.writeDouble(this.costPrice);
        dest.writeDouble(this.sales);
        dest.writeDouble(this.left);
        dest.writeLong(this.date.getTime());
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }

    private SalesEvent() {
    }

    private SalesEvent(Parcel source) {
        this.productId = source.readString();
        this.traderId = source.readString();
        this.customerId = source.readString();
        this.salesPrice = source.readDouble();
        this.costPrice = source.readDouble();
        this.sales = source.readDouble();
        this.left = source.readDouble();
        this.date = new Date(source.readLong());
        this.latitude = source.readDouble();
        this.longitude = source.readDouble();

    }

    private SalesEvent(String productId, String traderId, String customerId, Double costPrice,
                       Double salesPrice, double sales, double left, Date date) {

        this.productId = productId;
        this.traderId = traderId;
        this.customerId = customerId;
        this.costPrice = costPrice;
        this.salesPrice = salesPrice;
        this.sales = sales;
        this.left = left;
        this.date = date;
        setId(UUID.randomUUID().toString());
    }

    public static SalesEvent getInstance() {
        return new SalesEvent();
    }

    public static SalesEvent getInstance(String productId, String traderId, String customerId,
                                         double costPrice, double salesPrice, double sales, double left,
                                         Date date) {

        return new SalesEvent(productId, traderId,
                customerId, costPrice, salesPrice, sales, left, date);

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

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public double getCostPrice() {
        return this.costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}