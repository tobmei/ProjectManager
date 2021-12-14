package com.example.projectmanager.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

public class Project implements Parcelable {

    private String name, description, createDate;
    private int customerID;
    private boolean status;
    private int dbID;

    public Project(int dbID, String name, int customerID, String description, String createDate, boolean status) {
        this.dbID = dbID;
        this.name = name;
        this.description = description;
        this.customerID = customerID;
        this.createDate = createDate;
        this.status = status;
    }

    public Project(String name, int customerID, String description, String createDate, boolean status) {
        this.name = name;
        this.description = description;
        this.customerID = customerID;
        this.createDate = createDate;
        this.status = status;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dbID);
        dest.writeInt(customerID);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(createDate);
        dest.writeBoolean(status);
    }

    protected Project(Parcel in) {
        dbID = in.readInt();
        customerID = in.readInt();
        name = in.readString();
        description = in.readString();
        createDate = in.readString();
        status = in.readByte() != 0;
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

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

    public int getDbID() {
        return dbID;
    }

    @Override
    public String toString() {
        return name;
    }
}
