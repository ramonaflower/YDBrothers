package com.example.ramona.ydbrothers.Entity;

import java.io.Serializable;

/**
 * Created by Ramona on 3/14/2017.
 */

public class UserEntities implements Serializable {

    private String sUserID, sUserName, sUserImage, sAddress, sPhone;

    public UserEntities() {
    }

    public UserEntities(String sUserName, String sUserImage, String sAddress, String sPhone) {
        this.sUserName = sUserName;
        this.sUserImage = sUserImage;
        this.sAddress = sAddress;
        this.sPhone = sPhone;
    }

    public String getsUserID() {
        return sUserID;
    }

    public void setsUserID(String sUserID) {
        this.sUserID = sUserID;
    }

    public String getsUserName() {
        return sUserName;
    }

    public void setsUserName(String sUserName) {
        this.sUserName = sUserName;
    }

    public String getsUserImage() {
        return sUserImage;
    }

    public void setsUserImage(String sUserImage) {
        this.sUserImage = sUserImage;
    }

    public String getsAddress() {
        return sAddress;
    }

    public void setsAddress(String sAddress) {
        this.sAddress = sAddress;
    }

    public String getsPhone() {
        return sPhone;
    }

    public void setsPhone(String sPhone) {
        this.sPhone = sPhone;
    }
}
