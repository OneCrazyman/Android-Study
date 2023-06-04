package com.example.touchtime10.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

//캘린더의 일정 데이터
@Entity(tableName = "events")
public class Event implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String date;

    private String item;

    @ColumnInfo(defaultValue = "false")
    private boolean eventCheck;

    public Event(){

    };

    public Event(@Nullable String date,@Nullable String item) {
        this.date = date;
        this.item = item;
//        this.check = false; //라디오버튼 비활성화 default
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public boolean isEventCheck() {
        return eventCheck;
    }

    public void setEventCheck(boolean eventCheck) {
        this.eventCheck = eventCheck;
    }
}
