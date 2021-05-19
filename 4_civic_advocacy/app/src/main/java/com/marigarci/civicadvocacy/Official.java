package com.marigarci.civicadvocacy;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;

public class Official implements Parcelable {

    private String name;
    private String office;
    private String party;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String phone;
    private String website;
    private String email;
    private String image;
    private HashMap<String, String> socials;

    //Constructors
    public Official(){}

    public Official(String name, String office) {
        this.name = name;
        this.office = office;
    }

    public Official(String office){
        this.office = office;
    }

    //Get Methods
    public String getName() {
        return name;
    }
    public String getOffice() {
        return office;
    }
    public String getParty() {
        return party;
    }
    public String getAddress() {
        return address;
    }
    public String getCity() {
        return city;
    }
    public String getState() {
        return state;
    }
    public String getZip() {
        return zip;
    }
    public String getPhone() {
        return phone;
    }
    public String getWebsite() {
        return website;
    }
    public String getEmail() {
        return email;
    }
    public String getImage() {
        return image;
    }
    public HashMap<String, String> getSocials() {
        return socials;
    }

    //Set Methods
    public void setName(String name) {
        this.name = name;
    }
    public void setOffice(String office) {
        this.office = office;
    }
    public void setParty(String party) {
        this.party = party;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setState(String state) {
        this.state = state;
    }
    public void setZip(String zip) {
        this.zip = zip;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setWebsite(String website) {
        this.website = website;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public void setSocials(HashMap<String, String> socials) {
        this.socials = socials;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(zip);
        dest.writeString(party);
        dest.writeString(phone);
        dest.writeString(website);
        dest.writeString(email);
        dest.writeString(image);
        dest.writeString(office);
        dest.writeSerializable(socials);

    }

    public static final Parcelable.Creator<Official> CREATOR
            = new Parcelable.Creator<Official>() {
        public Official createFromParcel(Parcel in) {
            return new Official(in);
        }

        public Official[] newArray(int size) {
            return new Official[size];
        }
    };

    private Official(Parcel in) {
        name = in.readString();
        address = in.readString();
        city = in.readString();
        state = in.readString();
        zip = in.readString();
        party = in.readString();
        phone = in.readString();
        website = in.readString();
        email = in.readString();
        image = in.readString();
        office = in.readString();
        socials = (HashMap<String, String>) in.readSerializable();
    }
}
