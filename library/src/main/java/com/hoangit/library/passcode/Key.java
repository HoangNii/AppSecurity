package com.hoangit.library.passcode;

public class Key {
    private String number;
    private String tvNumber;

    public Key(String number, String tvNumber) {
        this.number = number;
        this.tvNumber = tvNumber;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTvNumber() {
        return tvNumber;
    }

    public void setTvNumber(String tvNumber) {
        this.tvNumber = tvNumber;
    }
}
