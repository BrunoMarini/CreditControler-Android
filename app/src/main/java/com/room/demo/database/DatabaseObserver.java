package com.room.demo.database;

import java.util.List;

public interface DatabaseObserver {
    void onDatabaseConnected();

    void onConnectError();

    void onElementInserted();

    void onInsertError();

    void onElementRead(List<Purchase> purchases);

    void onReadError();

    void onElementDeleted();

    void onDeleteError();
}
