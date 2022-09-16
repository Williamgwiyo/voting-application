package com.gwiyo.voting.Models;

public class UsersModel {
    String accountType,email,name,status,uid,voterID;

    public UsersModel() {
    }

    public UsersModel(String accountType, String email, String name, String status, String uid, String voterID) {
        this.accountType = accountType;
        this.email = email;
        this.name = name;
        this.status = status;
        this.uid = uid;
        this.voterID = voterID;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getVoterID() {
        return voterID;
    }

    public void setVoterID(String voterID) {
        this.voterID = voterID;
    }
}
