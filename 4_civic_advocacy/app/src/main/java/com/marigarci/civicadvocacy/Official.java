package com.marigarci.civicadvocacy;

import java.io.Serializable;

public class Official implements Serializable {


    private String name;
    private String office;
    private String party;
    private String address;
    private String phone;
    private String website;
    private String email;
    private String image;
    private Socials socials;

    //Constructors
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

    public Socials getSocials() {
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

    public void setSocials(Socials socials) {
        this.socials = socials;
    }
}
