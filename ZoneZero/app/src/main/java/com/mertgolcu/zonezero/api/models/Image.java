package com.mertgolcu.zonezero.api.models;

import com.google.gson.annotations.SerializedName;

public class Image {
    @SerializedName("url")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
