package com.daveace.salesdiary.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.daveace.salesdiary.interfaces.Mappable;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User implements Mappable, Parcelable {

    private String id;
    private String name;
    private String email;
    private String password;
    private String phone;

    public final static Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flag) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.password);
    }

    public User() {

    }

    public User(Parcel source) {
        this.id = source.readString();
        this.name = source.readString();
        this.email = source.readString();
        this.password = source.readString();
    }


    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
