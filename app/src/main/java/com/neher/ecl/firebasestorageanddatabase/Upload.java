package com.neher.ecl.firebasestorageanddatabase;

import com.google.firebase.database.Exclude;

public class Upload {
    private String mName;
    private String mImageUrl;
    private String mKey;

    public Upload(String name, String imageUrl) {
        if(name.trim() == null) {
            name = "No Name";
        }
        mName = name;
        mImageUrl = imageUrl;
    }

    public Upload() {
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    @Exclude
    public void setKey(String key) {
        mKey = key;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }
}
