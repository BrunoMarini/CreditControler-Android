package com.room.demo.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.room.demo.utils.Constants;

@Database(entities = {Purchase.class}, version = Constants.DATABASE_VERSION)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PurchaseDao purchaseDao();
}
