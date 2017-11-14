package com.example.ramona.ydbrothers.Entity;

import java.io.Serializable;

/**
 * Created by Ramona on 3/14/2017.
 */

public class DebtEntities implements Serializable {
    private String sDebtID, sCreditorID, sDebtorID;
    private int iDebt;

    public DebtEntities() {
    }

    public DebtEntities(String sCreditorID, String sDebtorID, int iDebt) {
        this.sCreditorID = sCreditorID;
        this.sDebtorID = sDebtorID;
        this.iDebt = iDebt;
    }

    public String getsDebtID() {
        return sDebtID;
    }

    public void setsDebtID(String sDebtID) {
        this.sDebtID = sDebtID;
    }

    public String getsCreditorID() {
        return sCreditorID;
    }

    public void setsCreditorID(String sCreditorID) {
        this.sCreditorID = sCreditorID;
    }

    public String getsDebtorID() {
        return sDebtorID;
    }

    public void setsDebtorID(String sDebtorID) {
        this.sDebtorID = sDebtorID;
    }

    public int getiDebt() {
        return iDebt;
    }

    public void setiDebt(int iDebt) {
        this.iDebt = iDebt;
    }
}
