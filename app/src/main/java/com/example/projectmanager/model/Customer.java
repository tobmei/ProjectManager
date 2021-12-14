package com.example.projectmanager.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Customer implements Parcelable {

    private String name, address, email, phone, contact;
    private int dbID;

    public Customer(int dbID, String name, String address, String email, String phone, String contact) {
        this.dbID = dbID;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.contact = contact;
    }
    public Customer(String name, String address, String email, String phone, String contact) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.contact = contact;
    }

    protected Customer(Parcel in) {
        dbID = in.readInt();
        name = in.readString();
        address = in.readString();
        contact = in.readString();
        phone = in.readString();
        email = in.readString();
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getDbID() {
        return dbID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dbID);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(contact);
        dest.writeString(phone);
        dest.writeString(email);
    }

    @Override
    public String toString() {
        return name;
    }
}
