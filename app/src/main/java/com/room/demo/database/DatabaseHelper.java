package com.room.demo.database;

import android.content.Context;

import androidx.room.Room;

import com.room.demo.utils.Constants;

public class DatabaseHelper {

    private Context mContext;
    private DatabaseObserver mDatabaseObserver;

    private AppDatabase mDb;
    private PurchaseDao mPurchaseDao = null;

    public DatabaseHelper(Context context, DatabaseObserver observer) {
        mContext = context;
        mDatabaseObserver = observer;

        connectPurchaseDatabase();
    }

    private synchronized void connectPurchaseDatabase() {
        if (mPurchaseDao != null) {
            mDatabaseObserver.onDatabaseConnected();;
        }

        new Thread(() -> {
            mDb = Room.databaseBuilder(mContext,
                    AppDatabase.class, Constants.PURCHASE_DATABASE).fallbackToDestructiveMigration().build();

            mPurchaseDao = mDb.purchaseDao();
            if (mPurchaseDao != null) {
                mDatabaseObserver.onDatabaseConnected();
            } else {
                mDatabaseObserver.onConnectError();
            }
        }).start();
    }

    public void insertElement(Purchase purchase) {
        if (mPurchaseDao == null || purchase == null) {
            mDatabaseObserver.onInsertError();
        }


        new Thread(() -> {
            mPurchaseDao.insertAll(purchase);
            mDatabaseObserver.onElementInserted();
        }).start();
    }

    public void getAllElements() {
        if (mPurchaseDao == null) {
            mDatabaseObserver.onReadError();
        }

        new Thread(() -> {
            mDatabaseObserver.onElementRead(mPurchaseDao.getAll());
        }).start();
    }

    public void deleteElementById(int id) {
        if (mPurchaseDao == null) {
            mDatabaseObserver.onDeleteError();
        }

        new Thread(() -> {
            mPurchaseDao.deleteElementById(id);
            mDatabaseObserver.onElementDeleted();
        }).start();
    }
}
