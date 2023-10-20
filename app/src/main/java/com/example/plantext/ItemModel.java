package com.example.plantext;

import android.net.Uri;

public class ItemModel {
    private String itemName;
    private String itemImage;
    private String key;
    public ItemModel() {
    }

    public ItemModel(String itemName, String itemImage) {
        this.itemName = itemName;
        this.itemImage = itemImage;

    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
