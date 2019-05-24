package com.example.android.tflitecamerademo;
import androidx.room.ColumnInfo;

import androidx.room.Entity;

import androidx.room.PrimaryKey;

import androidx.annotation.NonNull;

@Entity(tableName = "image_table")
public class Image {

    @PrimaryKey(autoGenerate = true)
    public int id;


    @ColumnInfo(name = "stage")
    public int stage = -1;

    @ColumnInfo(name = "is_upload")
    public boolean isUpload = false;

    @ColumnInfo(name = "path")
    public String imagePath;
}