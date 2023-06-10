package com.example.touchtime10.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.touchtime10.entity.Cash;
import com.example.touchtime10.entity.Event;

import java.util.List;

@Dao
public interface CashDao {
    @Query("select * from cashes")
    List<Cash> getAllEvents();

    @Query("SELECT * FROM cashes WHERE date = :date")
    List<Cash> dateToGetCashes(String date);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Cash cash);

    @Query("UPDATE cashes SET item = :item WHERE id = :id")
    void updateItem(int id, String item);

    @Query("delete from cashes")
    void deleteAll();

    @Query("delete from cashes where id= :id")
    void delete(int id);
}
