package com.example.android.tflitecamerademo;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)


    void insertImage(Image image);

    @Delete
    void  deletImage(Image  image);

    @Query("UPDATE image_table SET stage = :stage where id = :id")
    void stage(int stage, int id);





    @Query("select * from image_table order by is_upload")
    LiveData<List<Image>> getAll();
//


}
