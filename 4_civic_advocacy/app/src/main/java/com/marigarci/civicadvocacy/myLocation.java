package com.marigarci.civicadvocacy;

import android.os.Parcel;
import android.os.Parcelable;

public class myLocation implements Parcelable{
    private String zip;
    private String state;
    private String city;

    public myLocation() {
    }

    public String getCity() {
        return city;
    }
    public String getZip() {
        return zip;
    }
    public String getState() {
        return state;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public void setZip(String zip) {
        this.zip = zip;
    }
    public void setState(String state) {
        this.state = state;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.city);
        dest.writeString(this.zip);
        dest.writeString(this.state);
    }


    protected myLocation(Parcel in) {
        this.city = in.readString();
        this.zip = in.readString();
        this.state = in.readString();
    }

    public static final Parcelable.Creator<myLocation> CREATOR = new Parcelable.Creator<myLocation>() {
        @Override
        public myLocation createFromParcel(Parcel source) {
            return new myLocation(source);
        }

        @Override
        public myLocation[] newArray(int size) {
            return new myLocation[size];
        }
    };
}
