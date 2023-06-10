package com.example.touchtime10.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

@Entity(tableName = "cashes")
public class Cash implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String date;

    private String item;

    //0:입금 1:출금
    private int cashway;

    public int getCashway() {
        return cashway;
    }

    public void setCashway(int cashway) {
        this.cashway = cashway;
    }

    public Cash(){

    };

    public Cash(@Nullable String date, @Nullable String item, @Nullable int cashway) {
        this.date = date;
        this.item = item;
        this.cashway = cashway;
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
}

