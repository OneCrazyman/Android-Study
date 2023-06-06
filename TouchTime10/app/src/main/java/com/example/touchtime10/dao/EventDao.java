package com.example.touchtime10.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.touchtime10.entity.Event;

import java.util.List;

@Dao
public interface EventDao {
    @Query("select * from events")
    List<Event> getAllEvents();

    @Query("SELECT * FROM events WHERE date = :date")
    List<Event> dateToGetEvents(String date);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Event event);

    @Query("UPDATE events SET item = :item WHERE id = :id")
    void updateItem(int id, String item);

    @Query("UPDATE events SET eventCheck = :check WHERE id = :id")
    void updateCheck(int id, int check);

    @Query("delete from events")
    void deleteAll();

    @Delete
    void reset(List<Event> events);
}
