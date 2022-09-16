package com.gwiyo.voting.Models;

public class candidatesModel {
    String candidateimage,candidatemessage,candidatename,candidateId;

    public candidatesModel() {
    }

    public candidatesModel(String candidateimage, String candidatemessage, String candidatename, String candidateId) {
        this.candidateimage = candidateimage;
        this.candidatemessage = candidatemessage;
        this.candidatename = candidatename;
        this.candidateId = candidateId;
    }

    public String getCandidateimage() {
        return candidateimage;
    }

    public void setCandidateimage(String candidateimage) {
        this.candidateimage = candidateimage;
    }

    public String getCandidatemessage() {
        return candidatemessage;
    }

    public void setCandidatemessage(String candidatemessage) {
        this.candidatemessage = candidatemessage;
    }

    public String getCandidatename() {
        return candidatename;
    }

    public void setCandidatename(String candidatename) {
        this.candidatename = candidatename;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }
}
