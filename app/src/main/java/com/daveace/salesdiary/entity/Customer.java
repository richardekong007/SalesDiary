package com.daveace.salesdiary.entity;


import android.os.Parcel;
import android.os.Parcelable;

import com.daveace.salesdiary.interfaces.ObjectMapper;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.GeoPoint;

import java.util.UUID;

@IgnoreExtraProperties
public class Customer implements ObjectMapper, Parcelable {

    private String id;
    private String name;
    private String email;
    private String phone;
    private String company;
    private GeoPoint loc;
    private String signaturePath;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Customer createFromParcel(Parcel source) {
            return new Customer(source);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
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
        dest.writeString(this.email);
        dest.writeString(this.phone);
        dest.writeString(this.company);
    }

    private Customer() {
    }

    private Customer(Parcel source) {
        this.id = source.readString();
        this.name = source.readString();
        this.email = source.readString();
        this.phone = source.readString();
        this.company = source.readString();
    }


    private Customer(String name, String email, String phone, String company) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.company = company;
        setId(UUID.randomUUID().toString());
    }

    public static Customer getInstance() {
        return new Customer();
    }

    public static Customer getInstance(String name, String email, String phone, String company) {
        return new Customer(name, email, phone, company);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public GeoPoint getLoc() {
        return loc;
    }

    public void setLoc(GeoPoint loc) {
        this.loc = loc;
    }

    public String getSignaturePath() {
        return signaturePath;
    }

    public void setSignaturePath(String signaturePath) {
        this.signaturePath = signaturePath;
    }

}
