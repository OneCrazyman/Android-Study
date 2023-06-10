package com.example.touchtime10.util;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.touchtime10.dao.CashDao;
import com.example.touchtime10.dao.EventDao;
import com.example.touchtime10.entity.Cash;
import com.example.touchtime10.entity.Event;

@Database(entities = {Event.class, Cash.class},version = 4, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    private static RoomDB database;

    private static String DATABASE_NAME = "database";

    public synchronized static RoomDB getInstance(Context context)
    {
        if (database == null)
        {
            database = Room.databaseBuilder(context.getApplicationContext(), RoomDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract CashDao cashDao();
    public abstract EventDao eventDao();
}
