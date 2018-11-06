package com.aasana.caedu.ocrprognostic.model;

public class Message {
    private String mSource = null;
    private String mPriority = null;
    private String mMessage = null;

    public Message(String mSource, String mPriority, String mMessage) {
        this.mSource = mSource;
        this.mPriority = mPriority;
        this.mMessage = mMessage;
    }

    public void setmSource(String mSource) {
        this.mSource = mSource;
    }

    public void setmPriority(String mPriority) {
        this.mPriority = mPriority;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public String getmSource() {
        return mSource;
    }

    public String getmPriority() {
        return mPriority;
    }

    public String getmMessage() {
        return mMessage;
    }
}
