package com.room.demo.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PurchaseDao {

    @Insert
    void insertAll(Purchase... purchases);

    @Query("SELECT * FROM purchase")
    List<Purchase> getAll();

    @Query("SELECT price FROM purchase")
    List<Float> getAllPrices();

    @Query("DELETE FROM purchase")
    void delete();

    @Query("DELETE FROM purchase WHERE id = :elementId")
    void deleteElementById(int elementId);

    @Delete
    void delete(Purchase purchase);
}
