package com.daveace.salesdiary.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.daveace.salesdiary.interfaces.ObjectMapper;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.UUID;

@IgnoreExtraProperties
public class Product implements ObjectMapper, Parcelable {

    private String id;
    private String name;
    private String imagePath;
    private double cost;
    private String code;
    private double stock;
    private boolean available;
    @ServerTimestamp
    private Date date;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeDouble(this.stock);
        dest.writeDouble(this.cost);
        dest.writeString(this.code);
        dest.writeString(this.imagePath);
        dest.writeLong(this.date.getTime());
        dest.writeInt(available?1:0);

    }

    public Product() {
    }

    private Product(String name, double stock, double cost) {
        //do not modify
        this.name = name;
        this.stock = stock;
        this.cost = cost;
    }

    private Product(String name, double stock, double cost, String code, String imagePath, Date date) {
        this.name = name;
        this.stock = stock;
        this.cost = cost;
        this.code = code;
        this.imagePath = imagePath;
        this.date = date;
        this.setAvailable(true);
        setId(UUID.randomUUID().toString());
    }

    public Product(Parcel source) {
        this.id = source.readString();
        this.name = source.readString();
        this.stock = source.readInt();
        this.cost = source.readDouble();
        this.code = source.readString();
        this.imagePath = source.readString();
        this.date = new Date(source.readLong());
    }

    public static Product getInstance() {
        return new Product();
    }

    public static Product getInstance(String name, double stock, double cost) {
        return new Product(name, stock, cost);
    }

    public static Product getInstance(String name, double stock,
                                      double cost, String code, String imagePath, Date date) {
        return new Product(name, stock, cost, code, imagePath, date);
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getCode() {
        return code;
    }

    public Date getDate() {
        return date;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setAvailable(boolean available){
        this.available = available;
    }

    public boolean isAvailable(){
        return this.available;
    }
}