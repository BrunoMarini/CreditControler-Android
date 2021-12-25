package com.room.demo.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Purchase {

    public Purchase(float price, String description) {
        this.price = price;
        this.description = description;
    }

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "price")
    public float price;

    @ColumnInfo(name = "description")
    public String description;

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }
}
